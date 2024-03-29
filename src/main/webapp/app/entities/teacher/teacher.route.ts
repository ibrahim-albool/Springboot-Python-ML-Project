import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITeacher, Teacher } from 'app/shared/model/teacher.model';
import { TeacherService } from './teacher.service';
import { TeacherComponent } from './teacher.component';
import { TeacherDetailComponent } from './teacher-detail.component';
import { TeacherUpdateComponent } from './teacher-update.component';

@Injectable({ providedIn: 'root' })
export class TeacherResolve implements Resolve<ITeacher> {
  constructor(private service: TeacherService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITeacher> | Observable<never> {
    const number = route.params['number'];
    if (number) {
      return this.service.find(number).pipe(
        flatMap((teacher: HttpResponse<Teacher>) => {
          if (teacher.body) {
            return of(teacher.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Teacher());
  }
}

export const teacherRoute: Routes = [
  {
    path: '',
    component: TeacherComponent,
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      defaultSort: 'number,asc',
      pageTitle: 'Teachers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':number/view',
    component: TeacherDetailComponent,
    resolve: {
      teacher: TeacherResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'Teachers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeacherUpdateComponent,
    resolve: {
      teacher: TeacherResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'Teachers',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':number/edit',
    component: TeacherUpdateComponent,
    resolve: {
      teacher: TeacherResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'Teachers',
    },
    canActivate: [UserRouteAccessService],
  },
];
