import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subject, Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { MLModelService } from './ml-model.service';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'jhi-ml-model',
  templateUrl: './ml-model.component.html',
})
export class MLModelComponent implements OnInit {
  private _alert = new Subject<string>();
  authorities: string[] = [];
  eventSubscriber?: Subscription;

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

  constructor(
    private fb: FormBuilder,
    private router: Router,
    protected mLModelService: MLModelService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this._alert.pipe(debounceTime(5000)).subscribe(() => ([this.trainingSuccessfulAlert, this.trainingFailedAlert] = ['', '']));

    this.isLoading = true;
    this.mLModelService.modelMetrics().subscribe(
      () => this.successfulRedirection(),
      response => this.unsuccessfulRedirection(response)
    );
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
    this.isTraining = false;
    this.trainingModelForm.get('trainingFile')?.enable();
    this.trainingModelForm.get('labelsFile')?.enable();
    if (response.status === 200) {
      this.trainingSuccessfulAlert = response.body.message;
    }
    this._alert.next();
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
    // this.router.navigate(['/admin/ml-model', 'trained-model']);
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
