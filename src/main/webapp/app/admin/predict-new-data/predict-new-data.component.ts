import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { PredictNewData } from 'app/shared/model/predict-new-data.model';
import { PredictNewDataService } from './predict-new-data.service';

@Component({
  selector: 'jhi-predict-new-data',
  templateUrl: './predict-new-data.component.html',
})
export class PredictNewDataComponent implements OnInit {
  predictDataFile!: PredictNewData;
  authorities: string[] = [];

  isSaving = false;
  success = false;
  error = false;
  error5001 = false;
  error5002 = false;
  predictDataForm = this.fb.group({
    fileName: [''],
  });

  constructor(private predictNewDataService: PredictNewDataService, private route: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.predictNewDataService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
  }

  save(): void {
    this.isSaving = true;
    this.predictDataForm.get('fileName')?.disable();
    this.error = false;
    this.error5001 = false;
    this.error5002 = false;
    const fileNameTemp = this.predictDataForm.get(['fileName'])!.value;
    this.predictNewDataService.predictData(fileNameTemp).subscribe(
      () => this.onSaveSuccess(),
      response => this.onSaveError(response)
    );
  }

  private updateForm(predictDataFile: PredictNewData): void {
    this.predictDataForm.patchValue({
      fileName: predictDataFile.fileName,
    });
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.success = true;
    this.predictDataForm.get('fileName')?.enable();
  }

  private onSaveError(response: HttpErrorResponse): void {
    this.isSaving = false;
    this.predictDataForm.get('fileName')?.enable();
    if (response.status === 500 && response.error.detail === 'No value present') {
      this.error5001 = true;
    } else if (
      response.status === 500 &&
      response.error.detail ===
        'I/O error on GET request for "http://localhost:5000/predictXNew": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect'
    ) {
      this.error5002 = true;
    } else {
      this.error = true;
    }
  }
}
