import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMLModel } from 'app/shared/model/ml-model.model';

@Component({
  selector: 'jhi-ml-model-detail',
  templateUrl: './ml-model-detail.component.html',
})
export class MLModelDetailComponent implements OnInit {
  mLModel: IMLModel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mLModel }) => (this.mLModel = mLModel));
  }

  previousState(): void {
    window.history.back();
  }
}
