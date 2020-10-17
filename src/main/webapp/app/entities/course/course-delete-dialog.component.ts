import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICourse } from 'app/shared/model/course.model';
import { CourseService } from './course.service';
import { Specialization } from 'app/shared/model/enumerations/specialization.model';

@Component({
  templateUrl: './course-delete-dialog.component.html',
})
export class CourseDeleteDialogComponent {
  course?: ICourse;

  constructor(protected courseService: CourseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(code: number, specialization: Specialization): void {
    this.courseService.delete(code,specialization).subscribe(() => {
      this.eventManager.broadcast('courseListModification');
      this.activeModal.close();
    });
  }
}
