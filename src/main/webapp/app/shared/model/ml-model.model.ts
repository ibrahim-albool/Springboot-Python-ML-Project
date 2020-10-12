export interface IMLModel {
  id?: number;
  tp?: number;
  tn?: number;
  fp?: number;
  fn?: number;
  accuracy?: number;
  precision?: number;
  recall?: number;
}

export class MLModel implements IMLModel {
  constructor(
    public id?: number,
    public tp?: number,
    public tn?: number,
    public fp?: number,
    public fn?: number,
    public accuracy?: number,
    public precision?: number,
    public recall?: number
  ) {}
}
