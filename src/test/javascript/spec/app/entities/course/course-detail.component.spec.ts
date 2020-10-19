import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeachPredictorTestModule } from '../../../test.module';
import { CourseDetailComponent } from 'app/entities/course/course-detail.component';
import { Course } from 'app/shared/model/course.model';

describe('Component Tests', () => {
  describe('Course Management Detail Component', () => {
    let comp: CourseDetailComponent;
    let fixture: ComponentFixture<CourseDetailComponent>;
    const route = ({ data: of({ course: new Course(123,"Arabic") }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TeachPredictorTestModule],
        declarations: [CourseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CourseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load course on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.course).toEqual(jasmine.objectContaining({ code: 123, specialization: "Arabic" }));
      });
    });
  });
});
