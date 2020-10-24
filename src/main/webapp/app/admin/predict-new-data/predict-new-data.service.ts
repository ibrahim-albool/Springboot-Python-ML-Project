import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IPredictNewData } from 'app/shared/model/predict-new-data.model';

type EntityResponseType = HttpResponse<IPredictNewData>;
type EntityArrayResponseType = HttpResponse<IPredictNewData[]>;

@Injectable({ providedIn: 'root' })
export class PredictNewDataService {
  public resourceUrl = SERVER_API_URL + 'api/predictData';

  constructor(protected http: HttpClient) {}

  predictData(newDataFile: string): Observable<{}> {
    return this.http.post(this.resourceUrl, null, {
      params: new HttpParams().set('newDataFile', newDataFile),
    });
  }
  authorities(): Observable<string[]> {
    return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
  }
}
