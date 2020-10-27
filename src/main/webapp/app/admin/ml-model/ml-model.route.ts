import { Injectable } from '@angular/core';
import { Routes } from '@angular/router';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { MLModelComponent } from './ml-model.component';
import { MlModelDataComponent } from './ml-model-data.component';

@Injectable({ providedIn: 'root' })
export class MLModelResolve {
  constructor() {}
}

export const mLModelRoute: Routes = [
  {
    path: '',
    component: MLModelComponent,
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'MLModels',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'trained-model',
    component: MlModelDataComponent,
    resolve: {
      mLModel: MLModelResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'Trained Model',
    },
    canActivate: [UserRouteAccessService],
  },
];
