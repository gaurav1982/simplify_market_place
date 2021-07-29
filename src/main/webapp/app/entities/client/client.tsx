import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './client.reducer';
import { IClient } from 'app/shared/model/client.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Client = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const clientList = useAppSelector(state => state.client.entities);
  const loading = useAppSelector(state => state.client.loading);

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
      <h2 id="client-heading" data-cy="ClientHeading">
        <Translate contentKey="simplifyMarketplaceApp.client.home.title">Clients</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.client.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.client.home.createLabel">Create new Client</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.client.home.search')}
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
        {clientList && clientList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.compName">Comp Name</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.compAddress">Comp Address</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.compWebsite">Comp Website</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.compType">Comp Type</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.compContactNo">Comp Contact No</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.client.updatedAt">Updated At</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {clientList.map((client, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${client.id}`} color="link" size="sm">
                      {client.id}
                    </Button>
                  </td>
                  <td>{client.compName}</td>
                  <td>{client.compAddress}</td>
                  <td>{client.compWebsite}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.CompType.${client.compType}`} />
                  </td>
                  <td>{client.compContactNo}</td>
                  <td>{client.createdBy}</td>
                  <td>{client.createdAt ? <TextFormat type="date" value={client.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{client.updatedBy}</td>
                  <td>{client.updatedAt ? <TextFormat type="date" value={client.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${client.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${client.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${client.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.client.home.notFound">No Clients found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Client;
