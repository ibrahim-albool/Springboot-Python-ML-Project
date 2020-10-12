import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IMLModel, MLModel } from 'app/shared/model/ml-model.model';
import { MLModelService } from './ml-model.service';

@Component({
  selector: 'jhi-ml-model-update',
  templateUrl: './ml-model-update.component.html',
})
export class MLModelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    tp: [],
    tn: [],
    fp: [],
    fn: [],
    accuracy: [],
    precision: [],
    recall: [],
  });

  constructor(protected mLModelService: MLModelService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mLModel }) => {
      this.updateForm(mLModel);
    });
  }

  updateForm(mLModel: IMLModel): void {
    this.editForm.patchValue({
      id: mLModel.id,
      tp: mLModel.tp,
      tn: mLModel.tn,
      fp: mLModel.fp,
      fn: mLModel.fn,
      accuracy: mLModel.accuracy,
      precision: mLModel.precision,
      recall: mLModel.recall,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mLModel = this.createFromForm();
    if (mLModel.id !== undefined) {
      this.subscribeToSaveResponse(this.mLModelService.update(mLModel));
    } else {
      this.subscribeToSaveResponse(this.mLModelService.create(mLModel));
    }
  }

  private createFromForm(): IMLModel {
    return {
      ...new MLModel(),
      id: this.editForm.get(['id'])!.value,
      tp: this.editForm.get(['tp'])!.value,
      tn: this.editForm.get(['tn'])!.value,
      fp: this.editForm.get(['fp'])!.value,
      fn: this.editForm.get(['fn'])!.value,
      accuracy: this.editForm.get(['accuracy'])!.value,
      precision: this.editForm.get(['precision'])!.value,
      recall: this.editForm.get(['recall'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMLModel>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
