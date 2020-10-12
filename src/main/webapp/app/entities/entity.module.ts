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
      {
        path: 'ml-model',
        loadChildren: () => import('./ml-model/ml-model.module').then(m => m.TeachPredictorMLModelModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class TeachPredictorEntityModule {}
