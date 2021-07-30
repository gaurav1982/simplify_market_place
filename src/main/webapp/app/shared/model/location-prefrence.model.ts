import { IWorker } from 'app/shared/model/worker.model';

export interface ILocationPrefrence {
  id?: number;
  prefrenceOrder?: number | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<ILocationPrefrence> = {};
