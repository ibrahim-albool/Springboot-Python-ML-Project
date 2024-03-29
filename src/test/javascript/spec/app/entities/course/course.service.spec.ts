import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CourseService } from 'app/entities/course/course.service';
import { ICourse, Course } from 'app/shared/model/course.model';
import { Specialization } from 'app/shared/model/enumerations/specialization.model';
import { Type } from 'app/shared/model/enumerations/type.model';

describe('Service Tests', () => {
  describe('Course Service', () => {
    let injector: TestBed;
    let service: CourseService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourse;
    let expectedResult: ICourse | ICourse[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(CourseService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new Course(0, 'AAAAAAA', Specialization.Arabic, 'AAAAAAA', Type.C, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Course', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Course()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Course', () => {
        const returnedFromService = Object.assign(
          {
            code: 'BBBBBB',
            specialization: 'BBBBBB',
            name: 'BBBBBB',
            type: 'BBBBBB',
            hours: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Course', () => {
        const returnedFromService = Object.assign(
          {
            code: 'BBBBBB',
            specialization: 'BBBBBB',
            name: 'BBBBBB',
            type: 'BBBBBB',
            hours: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Course', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
