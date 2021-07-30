import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IJobPreference } from 'app/shared/model/job-preference.model';
import { getEntities as getJobPreferences } from 'app/entities/job-preference/job-preference.reducer';
import { getEntity, updateEntity, createEntity, reset } from './job-specific-field.reducer';
import { IJobSpecificField } from 'app/shared/model/job-specific-field.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const JobSpecificFieldUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const jobPreferences = useAppSelector(state => state.jobPreference.entities);
  const jobSpecificFieldEntity = useAppSelector(state => state.jobSpecificField.entity);
  const loading = useAppSelector(state => state.jobSpecificField.loading);
  const updating = useAppSelector(state => state.jobSpecificField.updating);
  const updateSuccess = useAppSelector(state => state.jobSpecificField.updateSuccess);

  const handleClose = () => {
    props.history.push('/job-specific-field');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getJobPreferences({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...jobSpecificFieldEntity,
      ...values,
      jobpreference: jobPreferences.find(it => it.id.toString() === values.jobpreferenceId.toString()),
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
          ...jobSpecificFieldEntity,
          jobpreferenceId: jobSpecificFieldEntity?.jobpreference?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.jobSpecificField.home.createOrEditLabel" data-cy="JobSpecificFieldCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.jobSpecificField.home.createOrEditLabel">
              Create or edit a JobSpecificField
            </Translate>
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
                  id="job-specific-field-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.jobSpecificField.value')}
                id="job-specific-field-value"
                name="value"
                data-cy="value"
                type="text"
              />
              <ValidatedField
                id="job-specific-field-jobpreference"
                name="jobpreferenceId"
                data-cy="jobpreference"
                label={translate('simplifyMarketplaceApp.jobSpecificField.jobpreference')}
                type="select"
              >
                <option value="" key="0" />
                {jobPreferences
                  ? jobPreferences.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-specific-field" replace color="info">
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

export default JobSpecificFieldUpdate;
