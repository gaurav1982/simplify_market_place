import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './education.reducer';
import { IEducation } from 'app/shared/model/education.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EducationUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const educationEntity = useAppSelector(state => state.education.entity);
  const loading = useAppSelector(state => state.education.loading);
  const updating = useAppSelector(state => state.education.updating);
  const updateSuccess = useAppSelector(state => state.education.updateSuccess);

  const handleClose = () => {
    props.history.push('/education');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getWorkers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...educationEntity,
      ...values,
      id: workers.find(it => it.id.toString() === values.idId.toString()),
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
          ...educationEntity,
          degreeType: 'HSC',
          idId: educationEntity?.id?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.education.home.createOrEditLabel" data-cy="EducationCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.education.home.createOrEditLabel">Create or edit a Education</Translate>
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
                  id="education-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.degreeName')}
                id="education-degreeName"
                name="degreeName"
                data-cy="degreeName"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.institute')}
                id="education-institute"
                name="institute"
                data-cy="institute"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.yearOfPassing')}
                id="education-yearOfPassing"
                name="yearOfPassing"
                data-cy="yearOfPassing"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.marks')}
                id="education-marks"
                name="marks"
                data-cy="marks"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.startDate')}
                id="education-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.endDate')}
                id="education-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.degreeType')}
                id="education-degreeType"
                name="degreeType"
                data-cy="degreeType"
                type="select"
              >
                <option value="HSC">{translate('simplifyMarketplaceApp.DegreeType.HSC')}</option>
                <option value="Graduate">{translate('simplifyMarketplaceApp.DegreeType.Graduate')}</option>
                <option value="PostGraduate">{translate('simplifyMarketplaceApp.DegreeType.PostGraduate')}</option>
                <option value="Associate">{translate('simplifyMarketplaceApp.DegreeType.Associate')}</option>
                <option value="Doctrol">{translate('simplifyMarketplaceApp.DegreeType.Doctrol')}</option>
                <option value="Diploma">{translate('simplifyMarketplaceApp.DegreeType.Diploma')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.description')}
                id="education-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.createdBy')}
                id="education-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.createdAt')}
                id="education-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.updatedBy')}
                id="education-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.education.updatedAt')}
                id="education-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <ValidatedField
                id="education-id"
                name="idId"
                data-cy="id"
                label={translate('simplifyMarketplaceApp.education.id')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/education" replace color="info">
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

export default EducationUpdate;
