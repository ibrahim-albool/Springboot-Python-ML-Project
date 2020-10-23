import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder } from '@angular/forms';
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
  fileNameTemp: any;

  predictDataForm = this.fb.group({
    fileName: [''],
  });

  constructor(private predictNewDataService: PredictNewDataService, private route: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ predictDataFile }) => {
      if (predictDataFile) {
        this.predictDataFile = predictDataFile;
        this.updateForm(predictDataFile);
      }
    });
    this.predictNewDataService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.updatePredictData(this.predictDataFile);
    this.predictNewDataService.predictData(this.fileNameTemp, this.predictDataFile).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  private updateForm(predictDataFile: PredictNewData): void {
    this.predictDataForm.patchValue({
      fileName: predictDataFile.fileName,
    });
  }

  private updatePredictData(predictDataFile: PredictNewData): void {
    predictDataFile.fileName = this.predictDataForm.get(['fileName'])!.value;
    this.fileNameTemp = predictDataFile.fileName;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
