import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './category.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Category = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const categoryList = useAppSelector(state => state.category.entities);
  const loading = useAppSelector(state => state.category.loading);

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
      <h2 id="category-heading" data-cy="CategoryHeading">
        <Translate contentKey="simplifyMarketplaceApp.category.home.title">Categories</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.category.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.category.home.createLabel">Create new Category</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.category.home.search')}
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
        {categoryList && categoryList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.categoryName">Category Name</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.isParent">Is Parent</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.isActive">Is Active</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.category.parent">Parent</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {categoryList.map((category, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${category.id}`} color="link" size="sm">
                      {category.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.CategoryType.${category.categoryName}`} />
                  </td>
                  <td>{category.isParent ? 'true' : 'false'}</td>
                  <td>{category.isActive ? 'true' : 'false'}</td>
                  <td>{category.createdBy}</td>
                  <td>
                    {category.createdAt ? <TextFormat type="date" value={category.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{category.updatedBy}</td>
                  <td>
                    {category.updatedAt ? <TextFormat type="date" value={category.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{category.parent ? <Link to={`category/${category.parent.id}`}>{category.parent.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${category.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${category.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${category.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.category.home.notFound">No Categories found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Category;
