import dayjs from 'dayjs';

export interface IOTPAttempt {
  id?: number;
  otp?: number | null;
  email?: string | null;
  phone?: number | null;
  ip?: string | null;
  coookie?: string | null;
  createdBy?: string | null;
  createdAt?: string | null;
}

export const defaultValue: Readonly<IOTPAttempt> = {};
