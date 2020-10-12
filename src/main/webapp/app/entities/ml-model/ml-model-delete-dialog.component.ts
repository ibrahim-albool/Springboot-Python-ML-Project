import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMLModel } from 'app/shared/model/ml-model.model';
import { MLModelService } from './ml-model.service';

@Component({
  templateUrl: './ml-model-delete-dialog.component.html',
})
export class MLModelDeleteDialogComponent {
  mLModel?: IMLModel;

  constructor(protected mLModelService: MLModelService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mLModelService.delete(id).subscribe(() => {
      this.eventManager.broadcast('mLModelListModification');
      this.activeModal.close();
    });
  }
}
