import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OTP from './otp';
import OTPDetail from './otp-detail';
import OTPUpdate from './otp-update';
import OTPDeleteDialog from './otp-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OTPUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OTPUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OTPDetail} />
      <ErrorBoundaryRoute path={match.url} component={OTP} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OTPDeleteDialog} />
  </>
);

export default Routes;
