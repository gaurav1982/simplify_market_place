import dayjs from 'dayjs';
import { IJobPreference } from 'app/shared/model/job-preference.model';
import { IField } from 'app/shared/model/field.model';
import { CategoryType } from 'app/shared/model/enumerations/category-type.model';

export interface ICategory {
  id?: number;
  categoryName?: CategoryType | null;
  isParent?: boolean | null;
  isActive?: boolean | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  categories?: ICategory[] | null;
  jobPreferences?: IJobPreference[] | null;
  parent?: ICategory | null;
  fields?: IField[] | null;
}

export const defaultValue: Readonly<ICategory> = {
  isParent: false,
  isActive: false,
};
