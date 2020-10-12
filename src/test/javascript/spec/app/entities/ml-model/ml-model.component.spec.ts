import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { TeachPredictorTestModule } from '../../../test.module';
import { MLModelComponent } from 'app/entities/ml-model/ml-model.component';
import { MLModelService } from 'app/entities/ml-model/ml-model.service';
import { MLModel } from 'app/shared/model/ml-model.model';

describe('Component Tests', () => {
  describe('MLModel Management Component', () => {
    let comp: MLModelComponent;
    let fixture: ComponentFixture<MLModelComponent>;
    let service: MLModelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TeachPredictorTestModule],
        declarations: [MLModelComponent],
      })
        .overrideTemplate(MLModelComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MLModelComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MLModelService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MLModel(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.mLModels && comp.mLModels[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
