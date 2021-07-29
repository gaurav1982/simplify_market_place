import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Employment from './employment';
import EmploymentDetail from './employment-detail';
import EmploymentUpdate from './employment-update';
import EmploymentDeleteDialog from './employment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EmploymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EmploymentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EmploymentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Employment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EmploymentDeleteDialog} />
  </>
);

export default Routes;
