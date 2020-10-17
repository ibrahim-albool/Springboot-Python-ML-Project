import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { TeachPredictorTestModule } from '../../../test.module';
import { CourseComponent } from 'app/entities/course/course.component';
import { CourseService } from 'app/entities/course/course.service';
import { Course } from 'app/shared/model/course.model';

describe('Component Tests', () => {
  describe('Course Management Component', () => {
    let comp: CourseComponent;
    let fixture: ComponentFixture<CourseComponent>;
    let service: CourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TeachPredictorTestModule],
        declarations: [CourseComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'code,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'code,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(CourseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CourseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(CourseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Course(123,"Arabic")],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.courses && comp.courses[0]).toEqual(jasmine.objectContaining({ code: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Course(123,"Arabic")],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.courses && comp.courses[0]).toEqual(jasmine.objectContaining({ code: 123 }));
    });

    it('should calculate the sort attribute for an code', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['code,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'code']);
    });
  });
});
