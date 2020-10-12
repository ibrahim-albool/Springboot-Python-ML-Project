import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMLModel } from 'app/shared/model/ml-model.model';
import { MLModelService } from './ml-model.service';
import { MLModelDeleteDialogComponent } from './ml-model-delete-dialog.component';

@Component({
  selector: 'jhi-ml-model',
  templateUrl: './ml-model.component.html',
})
export class MLModelComponent implements OnInit, OnDestroy {
  mLModels?: IMLModel[];
  eventSubscriber?: Subscription;

  constructor(protected mLModelService: MLModelService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.mLModelService.query().subscribe((res: HttpResponse<IMLModel[]>) => (this.mLModels = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMLModels();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IMLModel): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInMLModels(): void {
    this.eventSubscriber = this.eventManager.subscribe('mLModelListModification', () => this.loadAll());
  }

  delete(mLModel: IMLModel): void {
    const modalRef = this.modalService.open(MLModelDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mLModel = mLModel;
  }
}
