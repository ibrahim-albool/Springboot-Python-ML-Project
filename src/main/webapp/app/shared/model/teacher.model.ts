import { ICourse } from 'app/shared/model/course.model';
import { Specialization } from 'app/shared/model/enumerations/specialization.model';
import { Qualification } from 'app/shared/model/enumerations/qualification.model';
import { Stage } from 'app/shared/model/enumerations/stage.model';

export interface ITeacher {
  id?: number;
  number?: number;
  specialization?: Specialization;
  evaluation?: number;
  qualification?: Qualification;
  stage?: Stage;
  sumOfHours?: number;
  isPredicted?: boolean;
  courses?: ICourse[];
}

export class Teacher implements ITeacher {
  constructor(
    public id?: number,
    public number?: number,
    public specialization?: Specialization,
    public evaluation?: number,
    public qualification?: Qualification,
    public stage?: Stage,
    public sumOfHours?: number,
    public isPredicted?: boolean,
    public courses?: ICourse[]
  ) {
    this.isPredicted = this.isPredicted || false;
  }
}
