import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './resume.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ResumeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const resumeEntity = useAppSelector(state => state.resume.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="resumeDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.resume.detail.title">Resume</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.id}</dd>
          <dt>
            <span id="path">
              <Translate contentKey="simplifyMarketplaceApp.resume.path">Path</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.path}</dd>
          <dt>
            <span id="filetype">
              <Translate contentKey="simplifyMarketplaceApp.resume.filetype">Filetype</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.filetype}</dd>
          <dt>
            <span id="resumeTitle">
              <Translate contentKey="simplifyMarketplaceApp.resume.resumeTitle">Resume Title</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.resumeTitle}</dd>
          <dt>
            <span id="isDefault">
              <Translate contentKey="simplifyMarketplaceApp.resume.isDefault">Is Default</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.isDefault ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.resume.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.resume.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {resumeEntity.createdAt ? <TextFormat value={resumeEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.resume.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.resume.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {resumeEntity.updatedAt ? <TextFormat value={resumeEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.resume.id">Id</Translate>
          </dt>
          <dd>{resumeEntity.id ? resumeEntity.id.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/resume" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/resume/${resumeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ResumeDetail;
