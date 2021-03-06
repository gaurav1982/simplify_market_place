import { IClient } from 'app/shared/model/client.model';
import { IEmployment } from 'app/shared/model/employment.model';

export interface ILocation {
  id?: number;
  country?: string | null;
  state?: string | null;
  city?: string | null;
  client?: IClient | null;
  employments?: IEmployment[] | null;
}

export const defaultValue: Readonly<ILocation> = {};
