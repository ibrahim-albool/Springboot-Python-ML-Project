import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeachPredictorSharedModule } from 'app/shared/shared.module';
import { MLModelComponent } from './ml-model.component';
import { MLModelDetailComponent } from './ml-model-detail.component';
import { MLModelUpdateComponent } from './ml-model-update.component';
import { MLModelDeleteDialogComponent } from './ml-model-delete-dialog.component';
import { mLModelRoute } from './ml-model.route';

@NgModule({
  imports: [TeachPredictorSharedModule, RouterModule.forChild(mLModelRoute)],
  declarations: [MLModelComponent, MLModelDetailComponent, MLModelUpdateComponent, MLModelDeleteDialogComponent],
  entryComponents: [MLModelDeleteDialogComponent],
})
export class TeachPredictorMLModelModule {}
