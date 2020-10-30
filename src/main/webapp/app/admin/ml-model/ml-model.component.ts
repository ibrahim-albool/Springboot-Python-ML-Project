import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subject, Subscription } from 'rxjs';

import { MLModelService } from './ml-model.service';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { interval } from 'rxjs';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'jhi-ml-model',
  templateUrl: './ml-model.component.html',
})
export class MLModelComponent implements OnInit, OnDestroy {
  private _alert = new Subject<string>();
  checkModelMetricsSubscription?: Subscription;
  authorities: string[] = [];

  isLoading = false;
  isTraining = false;
  trainingAvailable = true;

  modelTrainedMessage = '';
  trainingSuccessfulAlert = '';
  trainingFailedAlert = '';

  trainingModelForm = this.fb.group({
    trainingFile: ['', [Validators.required]],
    labelsFile: ['', [Validators.required]],
  });

  checkMetricsCounter = 0;

  constructor(private fb: FormBuilder, private router: Router, protected mLModelService: MLModelService) {}

  ngOnInit(): void {
    this._alert.pipe(debounceTime(5000)).subscribe(() => ([this.trainingSuccessfulAlert, this.trainingFailedAlert] = ['', '']));

    this.isLoading = true;
    this.mLModelService.modelMetrics().subscribe(
      () => this.successfulRedirection(),
      response => this.unsuccessfulRedirection(response)
    );

    this.mLModelService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
  }

  ngOnDestroy(): void {
    if (this.checkModelMetricsSubscription) {
      this.checkModelMetricsSubscription.unsubscribe();
    }
  }

  train(): void {
    this.isTraining = true;
    this.trainingModelForm.get('trainingFile')?.disable();
    this.trainingModelForm.get('labelsFile')?.disable();
    const trainingFileTemp = this.trainingModelForm.get(['trainingFile'])!.value;
    const labelsFileTemp = this.trainingModelForm.get(['labelsFile'])!.value;
    this.mLModelService.trainMLModel(trainingFileTemp, labelsFileTemp).subscribe(
      response => this.onTrainSuccess(response),
      response => this.onTrainError(response)
    );
  }

  onTrainSuccess(response: HttpResponse<any>): void {
    this.isTraining = true;
    this.trainingModelForm.get('trainingFile')?.disable();
    this.trainingModelForm.get('labelsFile')?.disable();
    if (response.status === 200) {
      this.trainingSuccessfulAlert = response.body.message;
    }
    this._alert.next();
    this.checkModelMetricsInterval();
  }

  checkModelMetricsInterval(): void {
    this.isTraining = true;
    const checkModelMetrics = interval(3000).pipe(switchMap(() => this.mLModelService.modelMetrics()));
    this.checkModelMetricsSubscription = checkModelMetrics.subscribe(
      () => this.successfulRedirection(),
      () => this.checkModelMetricsTimeout()
    );
  }

  checkModelMetricsTimeout(): void {
    this.checkMetricsCounter += 1;
    if (this.checkMetricsCounter > 100) {
      this.checkModelMetricsSubscription?.unsubscribe();
      this.trainingModelForm.get('trainingFile')?.disable();
      this.trainingModelForm.get('labelsFile')?.disable();
      this.isTraining = false;
      this.checkMetricsCounter = 0;
    } else {
      this.checkModelMetricsInterval();
    }
  }

  onTrainError(response: HttpErrorResponse): void {
    this.isTraining = false;
    this.trainingModelForm.get('trainingFile')?.enable();
    this.trainingModelForm.get('labelsFile')?.enable();
    if (response.status === 404) {
      this.trainingFailedAlert = response.error.message;
    } else {
      this.trainingFailedAlert = 'Error: ' + response.status + ' -> (' + response.error.title + ')';
    }
    this._alert.next();
  }

  private successfulRedirection(): void {
    this.isLoading = false;
    this.trainingAvailable = false;
    this.trainingModelForm.get('trainingFile')?.disable();
    this.trainingModelForm.get('labelsFile')?.disable();
    this.router.navigate(['/admin/ml-model', 'trained-model']);
  }

  private unsuccessfulRedirection(response: HttpErrorResponse): void {
    this.isLoading = false;
    this.trainingAvailable = true;
    if (response.status === 404) {
      this.modelTrainedMessage = response.error.message;
    } else if (response.status === 404 && 'Python server is down.') {
      this.modelTrainedMessage = response.error.message;
      this.trainingAvailable = false;
    } else {
      this.modelTrainedMessage = 'Error: ' + response.status + ' -> (' + response.error.title + ')';
    }
    this._alert.next();
  }
}
