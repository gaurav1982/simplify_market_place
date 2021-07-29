import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntity, updateEntity, createEntity, reset } from './job-preference.reducer';
import { IJobPreference } from 'app/shared/model/job-preference.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const JobPreferenceUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const categories = useAppSelector(state => state.category.entities);
  const jobPreferenceEntity = useAppSelector(state => state.jobPreference.entity);
  const loading = useAppSelector(state => state.jobPreference.loading);
  const updating = useAppSelector(state => state.jobPreference.updating);
  const updateSuccess = useAppSelector(state => state.jobPreference.updateSuccess);

  const handleClose = () => {
    props.history.push('/job-preference');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkers({}));
    dispatch(getCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...jobPreferenceEntity,
      ...values,
      id: workers.find(it => it.id.toString() === values.idId.toString()),
      id: categories.find(it => it.id.toString() === values.idId.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...jobPreferenceEntity,
          engagementType: 'FullTime',
          locationType: 'Worklocation',
          idId: jobPreferenceEntity?.id?.id,
          idId: jobPreferenceEntity?.id?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.jobPreference.home.createOrEditLabel" data-cy="JobPreferenceCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.jobPreference.home.createOrEditLabel">Create or edit a JobPreference</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="job-preference-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.hourlyRate')}
                id="job-preference-hourlyRate"
                name="hourlyRate"
                data-cy="hourlyRate"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.dailyRate')}
                id="job-preference-dailyRate"
                name="dailyRate"
                data-cy="dailyRate"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.monthlyRate')}
                id="job-preference-monthlyRate"
                name="monthlyRate"
                data-cy="monthlyRate"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.hourPerDay')}
                id="job-preference-hourPerDay"
                name="hourPerDay"
                data-cy="hourPerDay"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.hourPerWeek')}
                id="job-preference-hourPerWeek"
                name="hourPerWeek"
                data-cy="hourPerWeek"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.engagementType')}
                id="job-preference-engagementType"
                name="engagementType"
                data-cy="engagementType"
                type="select"
              >
                <option value="FullTime">{translate('simplifyMarketplaceApp.EngagementType.FullTime')}</option>
                <option value="Consultant">{translate('simplifyMarketplaceApp.EngagementType.Consultant')}</option>
                <option value="Freelancer">{translate('simplifyMarketplaceApp.EngagementType.Freelancer')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.locationType')}
                id="job-preference-locationType"
                name="locationType"
                data-cy="locationType"
                type="select"
              >
                <option value="Worklocation">{translate('simplifyMarketplaceApp.LocationType.Worklocation')}</option>
                <option value="Homelocation">{translate('simplifyMarketplaceApp.LocationType.Homelocation')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.createdBy')}
                id="job-preference-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.createdAt')}
                id="job-preference-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.updatedBy')}
                id="job-preference-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobPreference.updatedAt')}
                id="job-preference-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <ValidatedField
                id="job-preference-id"
                name="idId"
                data-cy="id"
                label={translate('simplifyMarketplaceApp.jobPreference.id')}
                type="select"
              >
                <option value="" key="0" />
                {workers
                  ? workers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="job-preference-id"
                name="idId"
                data-cy="id"
                label={translate('simplifyMarketplaceApp.jobPreference.id')}
                type="select"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-preference" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default JobPreferenceUpdate;
