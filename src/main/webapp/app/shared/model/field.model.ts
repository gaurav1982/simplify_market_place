import dayjs from 'dayjs';
import { ICategory } from 'app/shared/model/category.model';
import { FieldType1 } from 'app/shared/model/enumerations/field-type-1.model';

export interface IField {
  id?: number;
  fieldName?: string | null;
  fieldLabel?: string | null;
  fieldType?: FieldType1 | null;
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
