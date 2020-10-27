import { Component } from '@angular/core';

@Component({
  selector: 'jhi-ml-model-data',
  template: '<plotly-plot [data]="graph.data" [layout]="graph.layout"></plotly-plot>',
})
export class MlModelDataComponent {
  public graph = {
    data: [
      { x: [1, 2, 3], y: [2, 6, 3], type: 'scatter', mode: 'lines+points', marker: { color: 'red' } },
      { x: [1, 2, 3], y: [2, 5, 3], type: 'bar' },
    ],
    layout: { width: 320, height: 240, title: 'A Fancy Plot' },
  };
}

// import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
// declare var dataPlot: any;

// @Component({
//   selector: 'jhi-ml-model-data',
//   templateUrl: './ml-model-data.component.html',
// })
// export class MlModelDataComponent implements OnInit {
//   @ViewChild('div1') div1!: QueryList<ElementRef>;
//   @ViewChild('div2') div2!: QueryList<ElementRef>;

//   myData: any;
//   constructor(div1: QueryList<ElementRef>, div2: QueryList<ElementRef>) {
//     div1.first.nativeElement.getAttribute('id');
//     div2.last.nativeElement.getAttribute('id');
//   }

//   ngOnInit(): void {

//     this.myData = {
//       "loss": [
//         0.6437230565968681,
//         0.013746686827610521,
//         0.015016184188425541
//       ],
//       "accuracy": [
//         0.6740923523902893,
//         0.9039342403411865,
//         0.9947881698608398,
//         0.9943200945854187
//       ]
//     }
//     this.funssss();
//   }

//   funssss(): void {
//     dataPlot(this.myData);
//     dataPlot.divs(this.div1.first.nativeElement.getAttribute('id'), this.div2.last.nativeElement.getAttribute('id'));
//   }
// }
