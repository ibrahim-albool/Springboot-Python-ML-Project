import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICourse, Course } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { CourseComponent } from './course.component';
import { CourseDetailComponent } from './course-detail.component';
import { CourseUpdateComponent } from './course-update.component';

@Injectable({ providedIn: 'root' })
export class CourseResolve implements Resolve<ICourse> {
  constructor(private service: CourseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourse> | Observable<never> {
    const code = route.params['code'];
    const specialization = route.params['specialization'];
    if (code && specialization) {
      return this.service.find(code, specialization).pipe(
        flatMap((course: HttpResponse<Course>) => {
          if (course.body) {
            return of(course.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Course());
  }
}

export const courseRoute: Routes = [
  {
    path: '',
    component: CourseComponent,
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      defaultSort: 'code,asc',
      pageTitle: 'Courses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':code/:specialization/view',
    component: CourseDetailComponent,
    resolve: {
      course: CourseResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'Courses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourseUpdateComponent,
    resolve: {
      course: CourseResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'Courses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':code/:specialization/edit',
    component: CourseUpdateComponent,
    resolve: {
      course: CourseResolve,
    },
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
      pageTitle: 'Courses',
    },
    canActivate: [UserRouteAccessService],
  },
];
