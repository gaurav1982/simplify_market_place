import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './location-prefrence.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const LocationPrefrenceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const locationPrefrenceEntity = useAppSelector(state => state.locationPrefrence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="locationPrefrenceDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.locationPrefrence.detail.title">LocationPrefrence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{locationPrefrenceEntity.id}</dd>
          <dt>
            <span id="prefrenceOrder">
              <Translate contentKey="simplifyMarketplaceApp.locationPrefrence.prefrenceOrder">Prefrence Order</Translate>
            </span>
          </dt>
          <dd>{locationPrefrenceEntity.prefrenceOrder}</dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.locationPrefrence.worker">Worker</Translate>
          </dt>
          <dd>{locationPrefrenceEntity.worker ? locationPrefrenceEntity.worker.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/location-prefrence" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/location-prefrence/${locationPrefrenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocationPrefrenceDetail;
