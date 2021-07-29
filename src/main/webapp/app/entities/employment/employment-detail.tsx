import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './employment.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EmploymentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const employmentEntity = useAppSelector(state => state.employment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employmentDetailsHeading">
          <Translate contentKey="simplifyMarketplaceApp.employment.detail.title">Employment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.id}</dd>
          <dt>
            <span id="jobTitle">
              <Translate contentKey="simplifyMarketplaceApp.employment.jobTitle">Job Title</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.jobTitle}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="simplifyMarketplaceApp.employment.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.companyName}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="simplifyMarketplaceApp.employment.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {employmentEntity.startDate ? (
              <TextFormat value={employmentEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="simplifyMarketplaceApp.employment.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {employmentEntity.endDate ? <TextFormat value={employmentEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastSalary">
              <Translate contentKey="simplifyMarketplaceApp.employment.lastSalary">Last Salary</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.lastSalary}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="simplifyMarketplaceApp.employment.description">Description</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.description}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="simplifyMarketplaceApp.employment.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.createdBy}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="simplifyMarketplaceApp.employment.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {employmentEntity.createdAt ? (
              <TextFormat value={employmentEntity.createdAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="simplifyMarketplaceApp.employment.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{employmentEntity.updatedBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="simplifyMarketplaceApp.employment.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {employmentEntity.updatedAt ? (
              <TextFormat value={employmentEntity.updatedAt} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.employment.id">Id</Translate>
          </dt>
          <dd>
            {employmentEntity.ids
              ? employmentEntity.ids.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {employmentEntity.ids && i === employmentEntity.ids.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="simplifyMarketplaceApp.employment.id">Id</Translate>
          </dt>
          <dd>{employmentEntity.id ? employmentEntity.id.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/employment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employment/${employmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmploymentDetail;
