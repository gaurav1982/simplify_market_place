import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import LocationPrefrence from './location-prefrence';
import LocationPrefrenceDetail from './location-prefrence-detail';
import LocationPrefrenceUpdate from './location-prefrence-update';
import LocationPrefrenceDeleteDialog from './location-prefrence-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LocationPrefrenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LocationPrefrenceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LocationPrefrenceDetail} />
      <ErrorBoundaryRoute path={match.url} component={LocationPrefrence} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={LocationPrefrenceDeleteDialog} />
  </>
);

export default Routes;
