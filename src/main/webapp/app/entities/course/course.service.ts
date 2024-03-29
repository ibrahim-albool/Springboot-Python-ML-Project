import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ICourse } from 'app/shared/model/course.model';
import { Specialization } from 'app/shared/model/enumerations/specialization.model';

type EntityResponseType = HttpResponse<ICourse>;
type EntityArrayResponseType = HttpResponse<ICourse[]>;

@Injectable({ providedIn: 'root' })
export class CourseService {
  public resourceUrl = SERVER_API_URL + 'api/courses';

  constructor(protected http: HttpClient) {}

  create(course: ICourse): Observable<EntityResponseType> {
    return this.http.post<ICourse>(this.resourceUrl, course, { observe: 'response' });
  }

  update(course: ICourse): Observable<EntityResponseType> {
    return this.http.put<ICourse>(this.resourceUrl, course, { observe: 'response' });
  }

  find(code: number, specialization: Specialization): Observable<EntityResponseType> {
    return this.http.get<ICourse>(`${this.resourceUrl}/${code}/${specialization}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICourse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(code: number, specialization: Specialization): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${code}/${specialization}`, { observe: 'response' });
  }
}
