import { ITeacher } from 'app/shared/model/teacher.model';
import { Specialization } from 'app/shared/model/enumerations/specialization.model';
import { Type } from 'app/shared/model/enumerations/type.model';

export interface ICourse {
  id?: number;
  code?: string;
  specialization?: Specialization;
  name?: string;
  type?: Type;
  hours?: number;
  teachers?: ITeacher[];
}

export class Course implements ICourse {
  constructor(
    public id?: number,
    public code?: string,
    public specialization?: Specialization,
    public name?: string,
    public type?: Type,
    public hours?: number,
    public teachers?: ITeacher[]
  ) {}
}
