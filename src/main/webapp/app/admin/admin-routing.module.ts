import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HomeComponent } from '../home/home.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: '',
        component: HomeComponent,
      },
      {
        path: 'user-management',
        loadChildren: () => import('./user-management/user-management.module').then(m => m.UserManagementModule),
        data: {
          pageTitle: 'Users',
        },
      },
      {
        path: 'predict-new-data',
        loadChildren: () => import('./predict-new-data/predict-new-data.module').then(m => m.PredictNewDataModule),
        data: {
          pageTitle: 'Predict New Data',
        },
      },
      {
        path: 'ml-model',
        loadChildren: () => import('./ml-model/ml-model.module').then(m => m.MLModelModule),
        data: {
          pageTitle: 'Machine Learning Model',
        },
      },
    ]),
  ],
})
export class AdminRoutingModule {}
