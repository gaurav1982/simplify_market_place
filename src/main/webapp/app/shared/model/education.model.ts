import dayjs from 'dayjs';
import { IWorker } from 'app/shared/model/worker.model';
import { DegreeType } from 'app/shared/model/enumerations/degree-type.model';

export interface IEducation {
  id?: number;
  degreeName?: string | null;
  institute?: string | null;
  yearOfPassing?: number | null;
  marks?: number | null;
  startDate?: string | null;
  endDate?: string | null;
  degreeType?: DegreeType | null;
  description?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  id?: IWorker | null;
}

export const defaultValue: Readonly<IEducation> = {};
