import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IMLModel, MLModel } from 'app/shared/model/ml-model.model';
import { MLModelService } from './ml-model.service';
import { MLModelComponent } from './ml-model.component';
import { MLModelDetailComponent } from './ml-model-detail.component';
import { MLModelUpdateComponent } from './ml-model-update.component';
import { MlModelDataComponent } from './ml-model-data.component';

@Injectable({ providedIn: 'root' })
export class MLModelResolve implements Resolve<IMLModel> {
  constructor(private service: MLModelService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMLModel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((mLModel: HttpResponse<MLModel>) => {
          if (mLModel.body) {
            return of(mLModel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MLModel());
  }
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
    path: ':id/view',
    component: MLModelDetailComponent,
    resolve: {
      mLModel: MLModelResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'MLModels',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MLModelUpdateComponent,
    resolve: {
      mLModel: MLModelResolve,
    },
    data: {
      authorities: [Authority.ADMIN],
      pageTitle: 'MLModels',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MLModelUpdateComponent,
    resolve: {
      mLModel: MLModelResolve,
    },
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
