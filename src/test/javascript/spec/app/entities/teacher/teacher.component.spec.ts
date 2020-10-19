import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { TeachPredictorTestModule } from '../../../test.module';
import { TeacherComponent } from 'app/entities/teacher/teacher.component';
import { TeacherService } from 'app/entities/teacher/teacher.service';
import { Teacher } from 'app/shared/model/teacher.model';

describe('Component Tests', () => {
  describe('Teacher Management Component', () => {
    let comp: TeacherComponent;
    let fixture: ComponentFixture<TeacherComponent>;
    let service: TeacherService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TeachPredictorTestModule],
        declarations: [TeacherComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'number,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'number,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(TeacherComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeacherComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TeacherService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Teacher(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.teachers && comp.teachers[0]).toEqual(jasmine.objectContaining({ number: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Teacher(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.teachers && comp.teachers[0]).toEqual(jasmine.objectContaining({ number: 123 }));
    });

    it('should calculate the sort attribute for an number', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['number,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'number']);
    });
  });
});
