import dayjs from 'dayjs';
import { IResume } from 'app/shared/model/resume.model';
import { IJobPreference } from 'app/shared/model/job-preference.model';
import { ILocationPrefrence } from 'app/shared/model/location-prefrence.model';
import { IEducation } from 'app/shared/model/education.model';
import { IEmployment } from 'app/shared/model/employment.model';

export interface IWorker {
  id?: number;
  name?: string | null;
  email?: string | null;
  phone?: number | null;
  description?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  resumes?: IResume[] | null;
  jobprefrences?: IJobPreference[] | null;
  locationprefrences?: ILocationPrefrence[] | null;
  educations?: IEducation[] | null;
  employments?: IEmployment[] | null;
}

export const defaultValue: Readonly<IWorker> = {};
