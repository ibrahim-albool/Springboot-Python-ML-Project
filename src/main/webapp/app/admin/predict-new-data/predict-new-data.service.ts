import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';

@Injectable({ providedIn: 'root' })
export class PredictNewDataService {
  public resourceUrl = SERVER_API_URL + 'api/predictData';

  constructor(protected http: HttpClient) {}

  predictData(newDataFile: string): Observable<any> {
    return this.http.post(this.resourceUrl, null, {
      observe: 'response',
      params: new HttpParams().set('newDataFile', newDataFile),
    });
  }

  modelMetrics(): Observable<HttpResponse<{}>> {
    return this.http.get('api/modelMetrics', { observe: 'response' });
  }

  authorities(): Observable<string[]> {
    return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
  }
}
