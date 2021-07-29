import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OTPAttempt from './otp-attempt';
import OTPAttemptDetail from './otp-attempt-detail';
import OTPAttemptUpdate from './otp-attempt-update';
import OTPAttemptDeleteDialog from './otp-attempt-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OTPAttemptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OTPAttemptUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OTPAttemptDetail} />
      <ErrorBoundaryRoute path={match.url} component={OTPAttempt} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OTPAttemptDeleteDialog} />
  </>
);

export default Routes;
