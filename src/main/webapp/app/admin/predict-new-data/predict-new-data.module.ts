import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeachPredictorSharedModule } from 'app/shared/shared.module';
import { PredictNewDataComponent } from './predict-new-data.component';
import { predictNewDataRoute } from './predict-new-data.route';

@NgModule({
  imports: [TeachPredictorSharedModule, RouterModule.forChild(predictNewDataRoute)],
  declarations: [PredictNewDataComponent],
})
export class PredictNewDataModule {}
