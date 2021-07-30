import dayjs from 'dayjs';
import { IWorker } from 'app/shared/model/worker.model';
import { FileType } from 'app/shared/model/enumerations/file-type.model';

export interface IResume {
  id?: number;
  path?: string | null;
  filetype?: FileType | null;
  resumeTitle?: string | null;
  isDefault?: boolean | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  worker?: IWorker | null;
}

export const defaultValue: Readonly<IResume> = {
  isDefault: false,
};
