import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IMLModel } from 'app/shared/model/ml-model.model';

type EntityResponseType = HttpResponse<IMLModel>;
type EntityArrayResponseType = HttpResponse<IMLModel[]>;

@Injectable({ providedIn: 'root' })
export class MLModelService {
  public resourceUrl = SERVER_API_URL + 'api/ml-models';

  constructor(protected http: HttpClient) {}

  create(mLModel: IMLModel): Observable<EntityResponseType> {
    return this.http.post<IMLModel>(this.resourceUrl, mLModel, { observe: 'response' });
  }

  update(mLModel: IMLModel): Observable<EntityResponseType> {
    return this.http.put<IMLModel>(this.resourceUrl, mLModel, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMLModel>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMLModel[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
