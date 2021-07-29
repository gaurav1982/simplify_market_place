import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './otp-attempt.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OTPAttemptDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const oTPAttemptEntity = useAppSelector(state => state.oTPAttempt.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="oTPAttemptDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.detail.title">OTPAttempt</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.id}</dd>
          <dt>
            <span id="otp">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.otp">Otp</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.otp}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.email">Email</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.phone}</dd>
          <dt>
            <span id="ip">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.ip">Ip</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.ip}</dd>
          <dt>
            <span id="coookie">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.coookie">Coookie</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.coookie}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{oTPAttemptEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {oTPAttemptEntity.createdAt ? (
              <TextFormat value={oTPAttemptEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/otp-attempt" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/otp-attempt/${oTPAttemptEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OTPAttemptDetail;
