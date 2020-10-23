export interface IPredictNewData {
  fileName?: string;
}

export class PredictNewData implements IPredictNewData {
  constructor(public fileName?: string) {}
}
