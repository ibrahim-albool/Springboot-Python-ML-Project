import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITeacher, Teacher } from 'app/shared/model/teacher.model';
import { TeacherService } from './teacher.service';
import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from 'app/entities/course/course.service';

@Component({
  selector: 'jhi-teacher-update',
  templateUrl: './teacher-update.component.html',
})
export class TeacherUpdateComponent implements OnInit {
  isSaving = false;
  courses: ICourse[] = [];

  editForm = this.fb.group({
    number: [],
    specialization: [],
    evaluation: [],
    qualification: [],
    stage: [],
    sumOfHours: [],
    isPredicted: [],
    courses: [],
  });

  constructor(
    protected teacherService: TeacherService,
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teacher }) => {
      this.updateForm(teacher);

      this.courseService.query().subscribe((res: HttpResponse<ICourse[]>) => (this.courses = res.body || []));
    });
  }

  updateForm(teacher: ITeacher): void {
    this.editForm.patchValue({
      number: teacher.number,
      specialization: teacher.specialization,
      evaluation: teacher.evaluation,
      qualification: teacher.qualification,
      stage: teacher.stage,
      sumOfHours: teacher.sumOfHours,
      isPredicted: teacher.isPredicted,
      courses: teacher.courses,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teacher = this.createFromForm();
    if (teacher.number !== undefined) {
      this.subscribeToSaveResponse(this.teacherService.update(teacher));
    } else {
      this.subscribeToSaveResponse(this.teacherService.create(teacher));
    }
  }

  private createFromForm(): ITeacher {
    return {
      ...new Teacher(),
      number: this.editForm.get(['number'])!.value,
      specialization: this.editForm.get(['specialization'])!.value,
      evaluation: this.editForm.get(['evaluation'])!.value,
      qualification: this.editForm.get(['qualification'])!.value,
      stage: this.editForm.get(['stage'])!.value,
      sumOfHours: this.editForm.get(['sumOfHours'])!.value,
      isPredicted: this.editForm.get(['isPredicted'])!.value,
      courses: this.editForm.get(['courses'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeacher>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ICourse): any {
    return item.id;
  }

  getSelected(selectedVals: ICourse[], option: ICourse): ICourse {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
