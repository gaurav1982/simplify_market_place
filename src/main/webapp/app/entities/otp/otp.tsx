import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './otp.reducer';
import { IOTP } from 'app/shared/model/otp.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OTP = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const oTPList = useAppSelector(state => state.oTP.entities);
  const loading = useAppSelector(state => state.oTP.loading);

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
      <h2 id="otp-heading" data-cy="OTPHeading">
        <Translate contentKey="simplifyMarketplaceApp.oTP.home.title">OTPS</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.oTP.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.oTP.home.createLabel">Create new OTP</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.oTP.home.search')}
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
        {oTPList && oTPList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.otp">Otp</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.phone">Phone</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.expiryTime">Expiry Time</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.oTP.updatedAt">Updated At</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {oTPList.map((oTP, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${oTP.id}`} color="link" size="sm">
                      {oTP.id}
                    </Button>
                  </td>
                  <td>{oTP.otp}</td>
                  <td>{oTP.email}</td>
                  <td>{oTP.phone}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.OtpType.${oTP.type}`} />
                  </td>
                  <td>{oTP.expiryTime ? <TextFormat type="date" value={oTP.expiryTime} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.OtpStatus.${oTP.status}`} />
                  </td>
                  <td>{oTP.createdBy}</td>
                  <td>{oTP.createdAt ? <TextFormat type="date" value={oTP.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{oTP.updatedBy}</td>
                  <td>{oTP.updatedAt ? <TextFormat type="date" value={oTP.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${oTP.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${oTP.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${oTP.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.oTP.home.notFound">No OTPS found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default OTP;
