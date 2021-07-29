import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './education.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EducationDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const educationEntity = useAppSelector(state => state.education.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="educationDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.education.detail.title">Education</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{educationEntity.id}</dd>
          <dt>
            <span id="degreeName">
              <Translate contentKey="simplifyMarketplaceApp.education.degreeName">Degree Name</Translate>
            </span>
          </dt>
          <dd>{educationEntity.degreeName}</dd>
          <dt>
            <span id="institute">
              <Translate contentKey="simplifyMarketplaceApp.education.institute">Institute</Translate>
            </span>
          </dt>
          <dd>{educationEntity.institute}</dd>
          <dt>
            <span id="yearOfPassing">
              <Translate contentKey="simplifyMarketplaceApp.education.yearOfPassing">Year Of Passing</Translate>
            </span>
          </dt>
          <dd>{educationEntity.yearOfPassing}</dd>
          <dt>
            <span id="marks">
              <Translate contentKey="simplifyMarketplaceApp.education.marks">Marks</Translate>
            </span>
          </dt>
          <dd>{educationEntity.marks}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="simplifyMarketplaceApp.education.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.startDate ? <TextFormat value={educationEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="simplifyMarketplaceApp.education.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.endDate ? <TextFormat value={educationEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="degreeType">
              <Translate contentKey="simplifyMarketplaceApp.education.degreeType">Degree Type</Translate>
            </span>
          </dt>
          <dd>{educationEntity.degreeType}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="simplifyMarketplaceApp.education.description">Description</Translate>
            </span>
          </dt>
          <dd>{educationEntity.description}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.education.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{educationEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.education.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.createdAt ? <TextFormat value={educationEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.education.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{educationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.education.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {educationEntity.updatedAt ? <TextFormat value={educationEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.education.id">Id</Translate>
          </dt>
          <dd>{educationEntity.id ? educationEntity.id.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/education" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/education/${educationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EducationDetail;
