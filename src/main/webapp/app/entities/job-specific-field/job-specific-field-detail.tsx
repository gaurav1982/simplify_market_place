import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './job-specific-field.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const JobSpecificFieldDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const jobSpecificFieldEntity = useAppSelector(state => state.jobSpecificField.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobSpecificFieldDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.jobSpecificField.detail.title">JobSpecificField</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobSpecificFieldEntity.id}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="simplifyMarketplaceApp.jobSpecificField.value">Value</Translate>
            </span>
          </dt>
          <dd>{jobSpecificFieldEntity.value}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.jobSpecificField.jobpreference">Jobpreference</Translate>
          </dt>
          <dd>{jobSpecificFieldEntity.jobpreference ? jobSpecificFieldEntity.jobpreference.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/job-specific-field" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-specific-field/${jobSpecificFieldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobSpecificFieldDetail;
