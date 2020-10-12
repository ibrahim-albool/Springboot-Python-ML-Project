import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { TeachPredictorTestModule } from '../../../test.module';
import { MLModelUpdateComponent } from 'app/entities/ml-model/ml-model-update.component';
import { MLModelService } from 'app/entities/ml-model/ml-model.service';
import { MLModel } from 'app/shared/model/ml-model.model';

describe('Component Tests', () => {
  describe('MLModel Management Update Component', () => {
    let comp: MLModelUpdateComponent;
    let fixture: ComponentFixture<MLModelUpdateComponent>;
    let service: MLModelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TeachPredictorTestModule],
        declarations: [MLModelUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(MLModelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MLModelUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MLModelService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MLModel(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MLModel();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
