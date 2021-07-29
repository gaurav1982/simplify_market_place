import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Worker from './worker';
import WorkerDetail from './worker-detail';
import WorkerUpdate from './worker-update';
import WorkerDeleteDialog from './worker-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WorkerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WorkerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WorkerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Worker} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WorkerDeleteDialog} />
  </>
);

export default Routes;
