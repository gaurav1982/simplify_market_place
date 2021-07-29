import dayjs from 'dayjs';
import { ILocation } from 'app/shared/model/location.model';
import { CompType } from 'app/shared/model/enumerations/comp-type.model';

export interface IClient {
  id?: number;
  compName?: string | null;
  compAddress?: string | null;
  compWebsite?: string | null;
  compType?: CompType | null;
  compContactNo?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  locations?: ILocation[] | null;
}

export const defaultValue: Readonly<IClient> = {};
