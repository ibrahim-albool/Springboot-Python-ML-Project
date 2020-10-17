import { ITeacher } from 'app/shared/model/teacher.model';
import { Specialization } from 'app/shared/model/enumerations/specialization.model';
import { Type } from 'app/shared/model/enumerations/type.model';

export interface ICourse {
  code?: number;
  specialization?: Specialization;
  name?: string;
  type?: Type;
  hours?: number;
  teachers?: ITeacher[];
}

export class Course implements ICourse {
  constructor(
    public code?: number,
    public specialization?: Specialization,
    public name?: string,
    public type?: Type,
    public hours?: number,
    public teachers?: ITeacher[]
  ) {}
}
