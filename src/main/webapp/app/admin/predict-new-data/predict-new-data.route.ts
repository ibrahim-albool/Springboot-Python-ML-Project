import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPredictNewData, PredictNewData } from 'app/shared/model/predict-new-data.model';
import { PredictNewDataComponent } from './predict-new-data.component';

@Injectable({ providedIn: 'root' })
export class PredictNewDataResolve implements Resolve<IPredictNewData> {
  constructor() {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPredictNewData> | Observable<never> {
    const fileName = route.params['fileName'];
    if (fileName) {
      // return this.service.find(fileName);
    }
    return of(new PredictNewData());
  }
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
