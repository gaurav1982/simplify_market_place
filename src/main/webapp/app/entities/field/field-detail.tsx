import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './field.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FieldDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const fieldEntity = useAppSelector(state => state.field.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fieldDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.field.detail.title">Field</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.id}</dd>
          <dt>
            <span id="fieldName">
              <Translate contentKey="simplifyMarketplaceApp.field.fieldName">Field Name</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.fieldName}</dd>
          <dt>
            <span id="fieldLabel">
              <Translate contentKey="simplifyMarketplaceApp.field.fieldLabel">Field Label</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.fieldLabel}</dd>
          <dt>
            <span id="fieldType">
              <Translate contentKey="simplifyMarketplaceApp.field.fieldType">Field Type</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.fieldType}</dd>
          <dt>
            <span id="isDeleted">
              <Translate contentKey="simplifyMarketplaceApp.field.isDeleted">Is Deleted</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.isDeleted ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.field.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.field.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.createdAt ? <TextFormat value={fieldEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.field.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.field.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{fieldEntity.updatedAt ? <TextFormat value={fieldEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.field.category">Category</Translate>
          </dt>
          <dd>
            {fieldEntity.categories
              ? fieldEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {fieldEntity.categories && i === fieldEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/field" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/field/${fieldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FieldDetail;
