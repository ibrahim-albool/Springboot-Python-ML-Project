import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITeacher } from 'app/shared/model/teacher.model';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { TeacherService } from './teacher.service';
import { TeacherDeleteDialogComponent } from './teacher-delete-dialog.component';

@Component({
  selector: 'jhi-teacher',
  templateUrl: './teacher.component.html',
})
export class TeacherComponent implements OnInit, OnDestroy {
  teachers?: ITeacher[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  number = '';
  specialization = [{ name: 'Arabic' }, { name: 'English' }, { name: 'Math' }, { name: 'Science' }];
  spec = '';
  evaluation = '';
  qualification = [{ name: 'Bachelor' }, { name: 'Diploma' }, { name: 'Master' }, { name: 'PhD' }];
  qual = '';
  stage = [{ name: 'Primary' }, { name: 'Secondary' }];
  stagE = '';
  sumOfHours = '';
  isPredicted = [{ name: 'true' }, { name: 'false' }];
  isPredicteD = '';

  constructor(
    protected teacherService: TeacherService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.teacherService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'number.equals': this.number,
        'specialization.equals': this.spec,
        'evaluation.equals': this.evaluation,
        'qualification.equals': this.qual,
        'stage.equals': this.stagE,
        'sumOfHours.equals': this.sumOfHours,
        'isPredicted.equals': this.isPredicteD,
      })
      .subscribe(
        (res: HttpResponse<ITeacher[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInTeachers();
  }

  onNumberChange(event: any): void {
    this.number = event.target.value;
    this.loadPage(1, true);
  }
  onSpecializationChange(event: any): void {
    this.spec = event;
    this.loadPage(1, true);
  }
  onEvaluationChange(event: any): void {
    this.evaluation = event.target.value;
    this.loadPage(1, true);
  }
  onQualificationChange(event: any): void {
    this.qual = event;
    this.loadPage(1, true);
  }
  onStageChange(event: any): void {
    this.stagE = event;
    this.loadPage(1, true);
  }
  onSumOfHoursChange(event: any): void {
    this.sumOfHours = event.target.value;
    this.loadPage(1, true);
  }
  onIsPredictedChange(event: any): void {
    this.isPredicteD = event;
    this.loadPage(1, true);
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ITeacher): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.number!;
  }

  registerChangeInTeachers(): void {
    this.eventSubscriber = this.eventManager.subscribe('teacherListModification', () => this.loadPage());
  }

  delete(teacher: ITeacher): void {
    const modalRef = this.modalService.open(TeacherDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.teacher = teacher;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'number') {
      result.push('number');
    }
    return result;
  }

  protected onSuccess(data: ITeacher[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/teacher'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.teachers = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
    this.number = '';
    this.spec = '';
    this.evaluation = '';
    this.qual = '';
    this.stagE = '';
    this.sumOfHours = '';
    this.isPredicteD = '';
    this.loadPage(1, true);
  }
}
