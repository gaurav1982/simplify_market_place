import dayjs from 'dayjs';
import { ICategory } from 'app/shared/model/category.model';
import { FieldType } from 'app/shared/model/enumerations/field-type.model';

export interface IField {
  id?: number;
  fieldName?: string | null;
  fieldLabel?: string | null;
  fieldType?: FieldType | null;
  isDeleted?: boolean | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
  categories?: ICategory[] | null;
}

export const defaultValue: Readonly<IField> = {
  isDeleted: false,
};
