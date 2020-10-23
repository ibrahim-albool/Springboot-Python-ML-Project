import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IPredictNewData } from 'app/shared/model/predict-new-data.model';

type EntityResponseType = HttpResponse<IPredictNewData>;
type EntityArrayResponseType = HttpResponse<IPredictNewData[]>;

@Injectable({ providedIn: 'root' })
export class PredictNewDataService {
  public resourceUrl = SERVER_API_URL + 'api/predictData';

  constructor(protected http: HttpClient) {}

  predictData(newDataFile: string, predictDataModel: IPredictNewData): Observable<EntityResponseType> {
    return this.http.post<IPredictNewData>(`${this.resourceUrl}/${newDataFile}`, predictDataModel, { observe: 'response' });
  }
  authorities(): Observable<string[]> {
    return this.http.get<string[]>(SERVER_API_URL + 'api/users/authorities');
  }
  // POST: /api/predictData
  // http://localhost:9000/api/predictData?newDataFile=teachers_test_300.xlsx
  // takes in the path variables:
  // newDataFile
  // returns Status 200 with the body Prediction succeeded. in the success scenario.
  // Status 500 in the failure scenario, when data in the model is not present.
  // {
  //     "type": "https://www.jhipster.tech/problem/problem-with-message",
  //     "title": "Internal Server Error",
  //     "status": 500,
  //     "detail": "No value present",
  //     "path": "/api/predictData",
  //     "message": "error.http.500"
  // }
  // or 500 with the json when python server is down:
  // {
  //     "type": "https://www.jhipster.tech/problem/problem-with-message",
  //     "title": "Internal Server Error",
  //     "status": 500,
  //     "detail": "I/O error on GET request for \"http://localhost:5000/predictXNew\": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect",
  //     "path": "/api/predictData",
  //     "message": "error.http.500"
  // }
}
