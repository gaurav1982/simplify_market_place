import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './category.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const CategoryDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const categoryEntity = useAppSelector(state => state.category.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="categoryDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.category.detail.title">Category</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.id}</dd>
          <dt>
            <span id="categoryName">
              <Translate contentKey="simplifyMarketplaceApp.category.categoryName">Category Name</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.categoryName}</dd>
          <dt>
            <span id="isParent">
              <Translate contentKey="simplifyMarketplaceApp.category.isParent">Is Parent</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.isParent ? 'true' : 'false'}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="simplifyMarketplaceApp.category.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.category.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.category.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {categoryEntity.createdAt ? <TextFormat value={categoryEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.category.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.category.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {categoryEntity.updatedAt ? <TextFormat value={categoryEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.category.parent">Parent</Translate>
          </dt>
          <dd>{categoryEntity.parent ? categoryEntity.parent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/category/${categoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CategoryDetail;
