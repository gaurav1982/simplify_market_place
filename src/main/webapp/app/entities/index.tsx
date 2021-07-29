import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Category from './category';
import Field from './field';
import Client from './client';
import Worker from './worker';
import Resume from './resume';
import JobPreference from './job-preference';
import JobSpecificField from './job-specific-field';
import Location from './location';
import LocationPrefrence from './location-prefrence';
import Education from './education';
import Employment from './employment';
import OTP from './otp';
import OTPAttempt from './otp-attempt';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}field`} component={Field} />
      <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
      <ErrorBoundaryRoute path={`${match.url}worker`} component={Worker} />
      <ErrorBoundaryRoute path={`${match.url}resume`} component={Resume} />
      <ErrorBoundaryRoute path={`${match.url}job-preference`} component={JobPreference} />
      <ErrorBoundaryRoute path={`${match.url}job-specific-field`} component={JobSpecificField} />
      <ErrorBoundaryRoute path={`${match.url}location`} component={Location} />
      <ErrorBoundaryRoute path={`${match.url}location-prefrence`} component={LocationPrefrence} />
      <ErrorBoundaryRoute path={`${match.url}education`} component={Education} />
      <ErrorBoundaryRoute path={`${match.url}employment`} component={Employment} />
      <ErrorBoundaryRoute path={`${match.url}otp`} component={OTP} />
      <ErrorBoundaryRoute path={`${match.url}otp-attempt`} component={OTPAttempt} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
