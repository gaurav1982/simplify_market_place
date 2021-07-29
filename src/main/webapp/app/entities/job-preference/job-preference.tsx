import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './job-preference.reducer';
import { IJobPreference } from 'app/shared/model/job-preference.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const JobPreference = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const jobPreferenceList = useAppSelector(state => state.jobPreference.entities);
  const loading = useAppSelector(state => state.jobPreference.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const startSearching = e => {
    if (search) {
      dispatch(searchEntities({ query: search }));
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="job-preference-heading" data-cy="JobPreferenceHeading">
        <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.title">Job Preferences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.createLabel">Create new Job Preference</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('simplifyMarketplaceApp.jobPreference.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {jobPreferenceList && jobPreferenceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourlyRate">Hourly Rate</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.dailyRate">Daily Rate</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.monthlyRate">Monthly Rate</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourPerDay">Hour Per Day</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.hourPerWeek">Hour Per Week</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.engagementType">Engagement Type</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.locationType">Location Type</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.id">Id</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.jobPreference.id">Id</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {jobPreferenceList.map((jobPreference, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${jobPreference.id}`} color="link" size="sm">
                      {jobPreference.id}
                    </Button>
                  </td>
                  <td>{jobPreference.hourlyRate}</td>
                  <td>{jobPreference.dailyRate}</td>
                  <td>{jobPreference.monthlyRate}</td>
                  <td>{jobPreference.hourPerDay}</td>
                  <td>{jobPreference.hourPerWeek}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.EngagementType.${jobPreference.engagementType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.LocationType.${jobPreference.locationType}`} />
                  </td>
                  <td>{jobPreference.createdBy}</td>
                  <td>
                    {jobPreference.createdAt ? (
                      <TextFormat type="date" value={jobPreference.createdAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{jobPreference.updatedBy}</td>
                  <td>
                    {jobPreference.updatedAt ? (
                      <TextFormat type="date" value={jobPreference.updatedAt} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{jobPreference.id ? <Link to={`worker/${jobPreference.id.id}`}>{jobPreference.id.id}</Link> : ''}</td>
                  <td>{jobPreference.id ? <Link to={`category/${jobPreference.id.id}`}>{jobPreference.id.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${jobPreference.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${jobPreference.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`${match.url}/${jobPreference.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.notFound">No Job Preferences found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default JobPreference;
