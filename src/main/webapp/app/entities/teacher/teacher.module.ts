import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TeachPredictorSharedModule } from 'app/shared/shared.module';
import { TeacherComponent } from './teacher.component';
import { TeacherDetailComponent } from './teacher-detail.component';
import { TeacherUpdateComponent } from './teacher-update.component';
import { TeacherDeleteDialogComponent } from './teacher-delete-dialog.component';
import { teacherRoute } from './teacher.route';

@NgModule({
  imports: [TeachPredictorSharedModule, RouterModule.forChild(teacherRoute)],
  declarations: [TeacherComponent, TeacherDetailComponent, TeacherUpdateComponent, TeacherDeleteDialogComponent],
  entryComponents: [TeacherDeleteDialogComponent],
})
export class TeachPredictorTeacherModule {}
