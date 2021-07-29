import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import JobSpecificField from './job-specific-field';
import JobSpecificFieldDetail from './job-specific-field-detail';
import JobSpecificFieldUpdate from './job-specific-field-update';
import JobSpecificFieldDeleteDialog from './job-specific-field-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JobSpecificFieldUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JobSpecificFieldUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JobSpecificFieldDetail} />
      <ErrorBoundaryRoute path={match.url} component={JobSpecificField} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JobSpecificFieldDeleteDialog} />
  </>
);

export default Routes;
