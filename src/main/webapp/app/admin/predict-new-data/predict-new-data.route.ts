import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';
import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { PredictNewDataComponent } from './predict-new-data.component';

@Injectable({ providedIn: 'root' })
export class PredictNewDataResolve {
  constructor() {}
}

export const predictNewDataRoute: Routes = [
  {
    path: '',
    component: PredictNewDataComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'Predict New Data',
    },
    canActivate: [UserRouteAccessService],
  },
];
