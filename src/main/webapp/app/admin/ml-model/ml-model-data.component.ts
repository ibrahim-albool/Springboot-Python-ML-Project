import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MLModelService } from './ml-model.service';
import { Router } from '@angular/router';
import { interval, Subject } from 'rxjs';
import { debounceTime, switchMap } from 'rxjs/operators';

@Component({
  selector: 'jhi-ml-model-data',
  templateUrl: './ml-model-data.component.html',
})
export class MlModelDataComponent implements OnInit {
  private _alert = new Subject<string>();
  graphLoss: any;
  graphAccuracy: any;

  myData: any;
  graphDataX?: any;
  graphLossDataY?: any;
  graphAccuracyDataY?: any;
  ans?: number[];

  isLoading = false;
  alertSuccessfulMessage = '';
  alertUnsuccessfulMessage = '';

  metricsData: any;
  metricsTp?: string;
  metricsTn?: string;
  metricsFp?: string;
  metricsFn?: string;
  metricsAccuracy?: string;
  metricsPrecision?: string;
  metricsRecall?: string;

  constructor(private mLModelService: MLModelService, private router: Router) {}

  ngOnInit(): void {
    this._alert.pipe(debounceTime(5000)).subscribe(() => ([this.alertSuccessfulMessage, this.alertUnsuccessfulMessage] = ['', '']));
    this.isLoading = true;
    this.loadModelData();
  }

  loadModelData(): void {
    this.mLModelService.modelHistory().subscribe(
      response => this.loadModelDataSuccessful(response),
      response => this.loadModelDataUnsuccessful(response),
      () => this.loadModelDataComplete()
    );
  }
  loadModelDataSuccessful(response: HttpResponse<any>): void {
    if (response.body !== null) {
      this.myData = response.body;
    }
  }
  loadModelDataUnsuccessful(response: HttpErrorResponse): void {
    if (response.status === 404) {
      this.alertUnsuccessfulMessage = response.error.message;
    }
    this._alert.next();
  }
  loadModelDataComplete(): void {
    this.graphLossDataY = this.myData.loss;
    this.graphAccuracyDataY = this.myData.accuracy;
    this.graphDataX = this.range(this.graphLossDataY.length > 0 ? 1 : 0, this.graphLossDataY.length);
    this.draw();
  }

  loadModelMetrics(): void {
    this.mLModelService.modelMetrics().subscribe(
      response => this.loadModelMetricsSuccessful(response),
      response => this.loadModelMetricsUnsuccessful(response),
      () => this.loadModelMetricsComplete()
    );
  }
  loadModelMetricsSuccessful(response: HttpResponse<any>): void {
    if (response.body !== null) {
      this.metricsData = response.body;
    }
  }
  loadModelMetricsUnsuccessful(response: HttpErrorResponse): void {
    if (response.status === 404) {
      this.alertUnsuccessfulMessage = response.error.message;
    } else {
      this.alertUnsuccessfulMessage = 'Error: ' + response.status + ' -> (' + response.error.title + ')';
    }
    this._alert.next();
  }
  loadModelMetricsComplete(): void {
    this.metricsTp = this.metricsData.tp;
    this.metricsTn = this.metricsData.tn;
    this.metricsFp = this.metricsData.fp;
    this.metricsFn = this.metricsData.fn;
    this.metricsAccuracy = this.metricsData.accuracy;
    this.metricsPrecision = this.metricsData.precision;
    this.metricsRecall = this.metricsData.recall;
    this.isLoading = false;
  }

  deleteModelAndTeachers(): void {
    this.mLModelService.deleteModelAndTeachers().subscribe(
      response => this.deletionSuccessful(response),
      response => this.deletionUnsuccessful(response)
    );
  }
  deletionSuccessful(response: HttpResponse<any>): any {
    if (response.status === 200) {
      this.alertSuccessfulMessage = response.body.message + 'Redirecting to home page.';
    }
    this._alert.next();
    this.router.navigate(['']);
  }
  deletionUnsuccessful(response: HttpErrorResponse): void {
    if (response.status === 404) {
      this.alertUnsuccessfulMessage = response.error.message;
    } else {
      this.alertUnsuccessfulMessage = 'Error: ' + response.status + ' -> (' + response.error.title + ')';
    }
    this._alert.next();
  }

  range(start: number, end: number): number[] {
    this.ans = [];
    for (let i = start; i <= end; i++) {
      this.ans.push(i);
    }
    return this.ans;
  }

  draw(): void {
    this.graphLoss = {
      data: [
        {
          type: 'scatter',
          x: this.graphDataX,
          y: this.graphLossDataY,
          mode: 'lines',
          name: 'Red',
          line: {
            color: 'rgb(219, 64, 82)',
            width: 1,
          },
        },
      ],
      layout: {
        title: 'Model Binary Crossentropy Loss',
        xaxis: {
          title: 'Epochs',
          showgrid: false,
          zeroline: false,
        },
        yaxis: {
          title: 'Loss',
          showline: false,
        },
      },
    };
    this.graphAccuracy = {
      data: [
        {
          type: 'scatter',
          x: this.graphDataX,
          y: this.graphAccuracyDataY,
          mode: 'lines',
          name: 'Blue',
          line: {
            color: 'rgb(55, 128, 191)',
            width: 1,
          },
        },
      ],
      layout: {
        title: 'Model Training Data Accuracy',
        xaxis: {
          title: 'Epochs',
          showgrid: false,
          zeroline: false,
        },
        yaxis: {
          title: 'Accuracy',
          showline: false,
        },
      },
    };
    this.loadModelMetrics();
  }
}
