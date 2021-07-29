import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Education from './education';
import EducationDetail from './education-detail';
import EducationUpdate from './education-update';
import EducationDeleteDialog from './education-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EducationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EducationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EducationDetail} />
      <ErrorBoundaryRoute path={match.url} component={Education} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={EducationDeleteDialog} />
  </>
);

export default Routes;
