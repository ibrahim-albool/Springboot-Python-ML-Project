import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class MLModelService {
  public resourceUrl = SERVER_API_URL + 'api/ml-models';

  constructor(protected http: HttpClient) {}

  trainMLModel(trainingFile: string, labelsFile: string): Observable<any> {
    return this.http.post('api/trainMLModel', null, {
      observe: 'response',
      params: new HttpParams().set('trainingFile', trainingFile).set('labelsFile', labelsFile),
    });
  }
  modelHistory(): Observable<HttpResponse<{}>> {
    return this.http.get('api/modelHistory', { observe: 'response' });
  }

  modelMetrics(): Observable<HttpResponse<{}>> {
    return this.http.get('api/modelMetrics', { observe: 'response' });
  }

  deleteModelAndTeachers(): Observable<HttpResponse<{}>> {
    return this.http.delete('api/deleteModelAndTeachers', { observe: 'response' });
  }

  authorities(): Observable<string[]> {
    return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
  }
}
