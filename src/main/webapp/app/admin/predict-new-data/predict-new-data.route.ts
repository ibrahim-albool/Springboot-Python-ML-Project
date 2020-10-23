import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPredictNewData, PredictNewData } from 'app/shared/model/predict-new-data.model';
import { PredictNewDataService } from './predict-new-data.service';
import { PredictNewDataComponent } from './predict-new-data.component';

@Injectable({ providedIn: 'root' })
export class PredictNewDataResolve implements Resolve<IPredictNewData> {
  constructor(private service: PredictNewDataService, private router: Router) {}

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
      authorities: [Authority.USER],
      pageTitle: 'Predict New Data',
    },
    canActivate: [UserRouteAccessService],
  },
];
