import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import JobPreference from './job-preference';
import JobPreferenceDetail from './job-preference-detail';
import JobPreferenceUpdate from './job-preference-update';
import JobPreferenceDeleteDialog from './job-preference-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JobPreferenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JobPreferenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JobPreferenceDetail} />
      <ErrorBoundaryRoute path={match.url} component={JobPreference} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JobPreferenceDeleteDialog} />
  </>
);

export default Routes;
