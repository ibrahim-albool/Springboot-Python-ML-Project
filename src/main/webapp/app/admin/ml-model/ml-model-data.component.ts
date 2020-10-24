import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { IMLModel } from 'app/shared/model/ml-model.model';
import { MLModelService } from './ml-model.service';

@Component({
  selector: 'jhi-ml-model-data',
  templateUrl: './ml-model-data.component.html',
})
export class MlModelDataComponent implements OnInit {
  authorities: string[] = [];

  constructor(private mLModelService: MLModelService, private route: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.mLModelService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
  }

  // save(): void {
  //   const fileNameTemp = this.predictDataForm.get(['fileName'])!.value;
  //   this.predictNewDataService.predictData(fileNameTemp).subscribe(
  //     () => this.onSaveSuccess(),
  //     response => this.onSaveError(response)
  //   );
  // }
}
