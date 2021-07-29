import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './field.reducer';
import { IField } from 'app/shared/model/field.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Field = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const fieldList = useAppSelector(state => state.field.entities);
  const loading = useAppSelector(state => state.field.loading);

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
      <h2 id="field-heading" data-cy="FieldHeading">
        <Translate contentKey="simplifyMarketplaceApp.field.home.title">Fields</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.field.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.field.home.createLabel">Create new Field</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.field.home.search')}
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
        {fieldList && fieldList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.fieldName">Field Name</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.fieldLabel">Field Label</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.fieldType">Field Type</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.isDeleted">Is Deleted</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.field.category">Category</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fieldList.map((field, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${field.id}`} color="link" size="sm">
                      {field.id}
                    </Button>
                  </td>
                  <td>{field.fieldName}</td>
                  <td>{field.fieldLabel}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.FieldType.${field.fieldType}`} />
                  </td>
                  <td>{field.isDeleted ? 'true' : 'false'}</td>
                  <td>{field.createdBy}</td>
                  <td>{field.createdAt ? <TextFormat type="date" value={field.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{field.updatedBy}</td>
                  <td>{field.updatedAt ? <TextFormat type="date" value={field.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    {field.categories
                      ? field.categories.map((val, j) => (
                          <span key={j}>
                            <Link to={`category/${val.id}`}>{val.id}</Link>
                            {j === field.categories.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${field.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${field.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${field.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.field.home.notFound">No Fields found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Field;
