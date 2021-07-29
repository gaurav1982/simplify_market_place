import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import category from 'app/entities/category/category.reducer';
// prettier-ignore
import field from 'app/entities/field/field.reducer';
// prettier-ignore
import client from 'app/entities/client/client.reducer';
// prettier-ignore
import worker from 'app/entities/worker/worker.reducer';
// prettier-ignore
import resume from 'app/entities/resume/resume.reducer';
// prettier-ignore
import jobPreference from 'app/entities/job-preference/job-preference.reducer';
// prettier-ignore
import jobSpecificField from 'app/entities/job-specific-field/job-specific-field.reducer';
// prettier-ignore
import location from 'app/entities/location/location.reducer';
// prettier-ignore
import locationPrefrence from 'app/entities/location-prefrence/location-prefrence.reducer';
// prettier-ignore
import education from 'app/entities/education/education.reducer';
// prettier-ignore
import employment from 'app/entities/employment/employment.reducer';
// prettier-ignore
import oTP from 'app/entities/otp/otp.reducer';
// prettier-ignore
import oTPAttempt from 'app/entities/otp-attempt/otp-attempt.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  category,
  field,
  client,
  worker,
  resume,
  jobPreference,
  jobSpecificField,
  location,
  locationPrefrence,
  education,
  employment,
  oTP,
  oTPAttempt,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
