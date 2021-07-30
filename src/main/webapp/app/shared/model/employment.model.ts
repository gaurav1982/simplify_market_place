import dayjs from 'dayjs';
import { ILocation } from 'app/shared/model/location.model';
import { IWorker } from 'app/shared/model/worker.model';

export interface IEmployment {
  id?: number;
  jobTitle?: string | null;
  companyName?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  lastSalary?: number | null;
  description?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  locations?: ILocation[] | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IEmployment> = {};
