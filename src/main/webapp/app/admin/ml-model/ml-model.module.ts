import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeachPredictorSharedModule } from 'app/shared/shared.module';
import { MLModelComponent } from './ml-model.component';
import { MLModelDetailComponent } from './ml-model-detail.component';
import { MLModelUpdateComponent } from './ml-model-update.component';
import { MLModelDeleteDialogComponent } from './ml-model-delete-dialog.component';
import { mLModelRoute } from './ml-model.route';
import { MlModelDataComponent } from './ml-model-data.component';

// @ts-ignore
import * as PlotlyJS from 'plotly.js/dist/plotly.js';
import { PlotlyModule } from 'angular-plotly.js';
PlotlyModule.plotlyjs = PlotlyJS;

@NgModule({
  imports: [PlotlyModule, TeachPredictorSharedModule, RouterModule.forChild(mLModelRoute)],
  declarations: [MLModelComponent, MLModelDetailComponent, MLModelUpdateComponent, MLModelDeleteDialogComponent, MlModelDataComponent],
  entryComponents: [MLModelDeleteDialogComponent],
})
export class MLModelModule {}
