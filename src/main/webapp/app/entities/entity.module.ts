import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'teacher',
        loadChildren: () => import('./teacher/teacher.module').then(m => m.TeachPredictorTeacherModule),
      },
      {
        path: 'course',
        loadChildren: () => import('./course/course.module').then(m => m.TeachPredictorCourseModule),
      },
    ]),
  ],
})
export class TeachPredictorEntityModule {}
