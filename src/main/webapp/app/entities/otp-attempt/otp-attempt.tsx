import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './otp-attempt.reducer';
import { IOTPAttempt } from 'app/shared/model/otp-attempt.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OTPAttempt = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const oTPAttemptList = useAppSelector(state => state.oTPAttempt.entities);
  const loading = useAppSelector(state => state.oTPAttempt.loading);

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
      <h2 id="otp-attempt-heading" data-cy="OTPAttemptHeading">
        <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.home.title">OTP Attempts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.home.createLabel">Create new OTP Attempt</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.oTPAttempt.home.search')}
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
        {oTPAttemptList && oTPAttemptList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.otp">Otp</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.ip">Ip</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.coookie">Coookie</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.createdAt">Created At</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {oTPAttemptList.map((oTPAttempt, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${oTPAttempt.id}`} color="link" size="sm">
                      {oTPAttempt.id}
                    </Button>
                  </td>
                  <td>{oTPAttempt.otp}</td>
                  <td>{oTPAttempt.email}</td>
                  <td>{oTPAttempt.phone}</td>
                  <td>{oTPAttempt.ip}</td>
                  <td>{oTPAttempt.coookie}</td>
                  <td>{oTPAttempt.createdBy}</td>
                  <td>
                    {oTPAttempt.createdAt ? <TextFormat type="date" value={oTPAttempt.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${oTPAttempt.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${oTPAttempt.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${oTPAttempt.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.home.notFound">No OTP Attempts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default OTPAttempt;
