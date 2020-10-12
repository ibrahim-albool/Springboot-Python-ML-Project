import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TeachPredictorTestModule } from '../../../test.module';
import { MLModelDetailComponent } from 'app/entities/ml-model/ml-model-detail.component';
import { MLModel } from 'app/shared/model/ml-model.model';

describe('Component Tests', () => {
  describe('MLModel Management Detail Component', () => {
    let comp: MLModelDetailComponent;
    let fixture: ComponentFixture<MLModelDetailComponent>;
    const route = ({ data: of({ mLModel: new MLModel(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [TeachPredictorTestModule],
        declarations: [MLModelDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(MLModelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MLModelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load mLModel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.mLModel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
