import dayjs from 'dayjs';
import { OtpType } from 'app/shared/model/enumerations/otp-type.model';
import { OtpStatus } from 'app/shared/model/enumerations/otp-status.model';

export interface IOTP {
  id?: number;
  otp?: number | null;
  email?: string | null;
  phone?: number | null;
  type?: OtpType | null;
  expiryTime?: string | null;
  status?: OtpStatus | null;
  createdBy?: string | null;
  createdAt?: string | null;
  updatedBy?: string | null;
  updatedAt?: string | null;
}

export const defaultValue: Readonly<IOTP> = {};
