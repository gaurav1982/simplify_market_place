import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './worker.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const WorkerDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const workerEntity = useAppSelector(state => state.worker.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="workerDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.worker.detail.title">Worker</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{workerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="simplifyMarketplaceApp.worker.name">Name</Translate>
            </span>
          </dt>
          <dd>{workerEntity.name}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="simplifyMarketplaceApp.worker.email">Email</Translate>
            </span>
          </dt>
          <dd>{workerEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="simplifyMarketplaceApp.worker.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{workerEntity.phone}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="simplifyMarketplaceApp.worker.description">Description</Translate>
            </span>
          </dt>
          <dd>{workerEntity.description}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.worker.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{workerEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.worker.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {workerEntity.createdAt ? <TextFormat value={workerEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.worker.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{workerEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.worker.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {workerEntity.updatedAt ? <TextFormat value={workerEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/worker" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/worker/${workerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default WorkerDetail;
