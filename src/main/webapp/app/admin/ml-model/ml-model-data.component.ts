import { Component, OnInit } from '@angular/core';
import { MLModelService } from './ml-model.service';

@Component({
  selector: 'jhi-ml-model-data',
  templateUrl: './ml-model-data.component.html',
})
export class MlModelDataComponent implements OnInit {
  graphLoss: any;
  graphAccuracy: any;
  myData: any;
  graphLossDataY?: any;
  graphAccuracyDataY?: any;
  graphDataXLoss?: number[];
  graphDataXAccuracy?: number[];
  ans?: number[];

  constructor(private mLModelService: MLModelService) {}

  ngOnInit(): void {
    this.getModelData();
    // this.graphDataX = this.range(this.myData.loss.length > 0 ? 1 : 0, this.myData.loss.length);
    this.graphDataXLoss = this.range(1, 600);
    this.graphDataXAccuracy = this.range(1, 600);

    this.draw();
    this.graphLoss.data.y = this.myData.loss;
    this.graphAccuracy.data.y = this.myData.accuracy;
  }
  draw(): void {
    this.graphLoss = {
      data: [
        {
          type: 'scatter',
          x: this.graphDataXLoss,
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
          x: this.graphDataXAccuracy,
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
  }

  range(start: number, end: number): number[] {
    this.ans = [];
    for (let i = start; i <= end; i++) {
      this.ans.push(i);
    }
    return this.ans;
  }

  getModelData(): void {
    this.myData = this.mLModelService.modelHistory().subscribe();
    this.graphLossDataY = this.myData.loss;

    // eslint-disable-next-line no-console
    console.log(this.myData);
    this.graphAccuracyDataY = this.myData.accuracy;
  }

  //   function range(start, end) {
  //     var ans = [];
  //     for (let i = start; i <= end; i++) {
  //         ans.push(i);
  //     }
  //     return ans;
  // }
}
// export class MlModelDataComponent {
//   public graph = {
//     data: [
//       { x: [1, 2, 3], y: [2, 6, 3], type: 'scatter', mode: 'lines+points', marker: { color: 'red' } },
//       { x: [1, 2, 3], y: [2, 5, 3], type: 'bar' },
//     ],
//     layout: { width: 320, height: 240, title: 'A Fancy Plot' },
//   };
// }
