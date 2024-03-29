import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICourse } from 'app/shared/model/course.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { CourseService } from './course.service';
import { CourseDeleteDialogComponent } from './course-delete-dialog.component';

@Component({
  selector: 'jhi-course',
  templateUrl: './course.component.html',
})
export class CourseComponent implements OnInit, OnDestroy {
  courses?: ICourse[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  code = '';
  specialization = [{ name: 'Arabic' }, { name: 'English' }, { name: 'Math' }, { name: 'Science' }];
  spec = '';
  name = '';
  type = [{ name: 'C' }, { name: 'P' }];
  typE = '';
  hours = '';

  constructor(
    protected courseService: CourseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.courseService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        'code.equals': this.code,
        'specialization.equals': this.spec,
        'name.contains': this.name,
        'type.equals': this.typE,
        'hours.equals': this.hours,
      })
      .subscribe(
        (res: HttpResponse<ICourse[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.code = '';
    this.spec = '';
    this.name = '';
    this.typE = '';
    this.hours = '';
    this.handleNavigation();
    this.registerChangeInCourses();
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

  onCodeChange(event: any): void {
    this.code = event.target.value;
    this.loadPage(1, true);
  }
  onSpecializationChange(event: any): void {
    this.spec = event;
    this.loadPage(1, true);
  }
  onNameChange(event: any): void {
    this.name = event.target.value;
    this.loadPage(1, true);
  }
  onTypeChange(event: any): void {
    this.typE = event;
    this.loadPage(1, true);
  }
  onHoursChange(event: any): void {
    this.hours = event.target.value;
    this.loadPage(1, true);
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICourse): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.code!;
  }

  registerChangeInCourses(): void {
    this.eventSubscriber = this.eventManager.subscribe('courseListModification', () => this.loadPage());
  }

  delete(course: ICourse): void {
    const modalRef = this.modalService.open(CourseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.course = course;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'code') {
      result.push('code');
    }
    return result;
  }

  protected onSuccess(data: ICourse[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/course'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.courses = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.code = '';
    this.spec = '';
    this.name = '';
    this.typE = '';
    this.hours = '';
    this.loadPage(1, true);
  }
}
