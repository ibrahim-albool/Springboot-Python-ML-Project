import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';

import { Subject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { PredictNewDataService } from './predict-new-data.service';

@Component({
  selector: 'jhi-predict-new-data',
  templateUrl: './predict-new-data.component.html',
})
export class PredictNewDataComponent implements OnInit {
  private _alert = new Subject<string>();
  authorities: string[] = [];

  isLoading = false;
  isPredicting = false;
  predictionAvailable = false;
  predictionSuccessfulAlert = '';
  predictionFailedAlert = '';
  modelTrained = false;
  modelTrainedMessage = '';

  predictDataForm = this.fb.group({
    fileName: [''],
  });

  constructor(private predictNewDataService: PredictNewDataService, private router: Router, private fb: FormBuilder) {}

  ngOnInit(): void {
    this._alert.pipe(debounceTime(5000)).subscribe(() => ([this.predictionSuccessfulAlert, this.predictionFailedAlert] = ['', '']));

    this.isLoading = true;
    this.predictNewDataService.modelMetrics().subscribe(
      () => this.successfulModelCheck(),
      response => this.unsuccessfulModelCheck(response)
    );

    this.predictNewDataService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
  }

  predict(): void {
    this.isPredicting = true;
    this.predictDataForm.get('fileName')?.disable();
    const fileNameTemp = this.predictDataForm.get(['fileName'])!.value;
    this.predictNewDataService.predictData(fileNameTemp).subscribe(
      response => this.onPredictSuccess(response),
      response => this.onPredictError(response)
    );
  }

  onPredictSuccess(response: HttpResponse<any>): void {
    this.isPredicting = false;
    this.predictDataForm.get('fileName')?.enable();
    if (response.status === 200) {
      this.predictionSuccessfulAlert = response.body.message;
    }
    this._alert.next();
    this.router.navigate(['/teacher']);
  }

  onPredictError(response: HttpErrorResponse): void {
    this.isPredicting = false;
    this.predictDataForm.get('fileName')?.enable();
    if (response.status === 404) {
      this.predictionFailedAlert = response.error.message;
    } else {
      this.predictionFailedAlert = 'Error: ' + response.status + ' -> (' + response.error.title + ')';
    }
    this._alert.next();
  }

  successfulModelCheck(): void {
    this.isLoading = false;
    this.predictionAvailable = true;
    this.predictDataForm.get('fileName')?.enable();
  }

  unsuccessfulModelCheck(response: HttpErrorResponse): void {
    this.isLoading = false;
    this.predictionAvailable = false;
    this.predictDataForm.get('fileName')?.disable();
    if (response.status === 404) {
      this.modelTrainedMessage = response.error.message;
    } else {
      this.modelTrainedMessage = 'Error: ' + response.status + ' -> (' + response.error.title + ')';
    }
    this._alert.next();
  }
}
