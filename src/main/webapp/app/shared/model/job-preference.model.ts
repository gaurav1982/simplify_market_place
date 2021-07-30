import dayjs from 'dayjs';
import { IJobSpecificField } from 'app/shared/model/job-specific-field.model';
import { IWorker } from 'app/shared/model/worker.model';
import { ICategory } from 'app/shared/model/category.model';
import { EngagementType } from 'app/shared/model/enumerations/engagement-type.model';
import { LocationType } from 'app/shared/model/enumerations/location-type.model';

export interface IJobPreference {
  id?: number;
  hourlyRate?: number | null;
  dailyRate?: number | null;
  monthlyRate?: number | null;
  hourPerDay?: number | null;
  hourPerWeek?: number | null;
  engagementType?: EngagementType | null;
  locationType?: LocationType | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  jobspecificfields?: IJobSpecificField[] | null;
  worker?: IWorker | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IJobPreference> = {};
