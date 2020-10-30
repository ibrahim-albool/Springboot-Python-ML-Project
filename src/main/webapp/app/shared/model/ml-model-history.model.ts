export interface IHistory {
  loss?: number[];
  accuracy?: number[];
}

export class History implements IHistory {
  constructor(public loss?: number[], public accuracy?: number[]) {}
}
