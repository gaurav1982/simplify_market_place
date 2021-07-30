import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './employment.reducer';
import { IEmployment } from 'app/shared/model/employment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Employment = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const employmentList = useAppSelector(state => state.employment.entities);
  const loading = useAppSelector(state => state.employment.loading);

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
      <h2 id="employment-heading" data-cy="EmploymentHeading">
        <Translate contentKey="simplifyMarketplaceApp.employment.home.title">Employments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.employment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.employment.home.createLabel">Create new Employment</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.employment.home.search')}
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
        {employmentList && employmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.jobTitle">Job Title</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.companyName">Company Name</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.endDate">End Date</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.lastSalary">Last Salary</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.employment.worker">Worker</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {employmentList.map((employment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${employment.id}`} color="link" size="sm">
                      {employment.id}
                    </Button>
                  </td>
                  <td>{employment.jobTitle}</td>
                  <td>{employment.companyName}</td>
                  <td>
                    {employment.startDate ? <TextFormat type="date" value={employment.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {employment.endDate ? <TextFormat type="date" value={employment.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{employment.lastSalary}</td>
                  <td>{employment.description}</td>
                  <td>{employment.createdBy}</td>
                  <td>
                    {employment.createdAt ? <TextFormat type="date" value={employment.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{employment.updatedBy}</td>
                  <td>
                    {employment.updatedAt ? <TextFormat type="date" value={employment.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {employment.locations
                      ? employment.locations.map((val, j) => (
                          <span key={j}>
                            <Link to={`location/${val.id}`}>{val.id}</Link>
                            {j === employment.locations.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>{employment.worker ? <Link to={`worker/${employment.worker.id}`}>{employment.worker.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${employment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${employment.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${employment.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.employment.home.notFound">No Employments found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Employment;
