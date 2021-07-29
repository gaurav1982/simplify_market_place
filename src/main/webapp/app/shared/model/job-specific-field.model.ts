import { IJobPreference } from 'app/shared/model/job-preference.model';

export interface IJobSpecificField {
  id?: number;
  value?: string | null;
  id?: IJobPreference | null;
}

export const defaultValue: Readonly<IJobSpecificField> = {};
