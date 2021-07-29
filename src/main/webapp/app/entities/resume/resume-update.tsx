import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IWorker } from 'app/shared/model/worker.model';
import { getEntities as getWorkers } from 'app/entities/worker/worker.reducer';
import { getEntity, updateEntity, createEntity, reset } from './resume.reducer';
import { IResume } from 'app/shared/model/resume.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ResumeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const workers = useAppSelector(state => state.worker.entities);
  const resumeEntity = useAppSelector(state => state.resume.entity);
  const loading = useAppSelector(state => state.resume.loading);
  const updating = useAppSelector(state => state.resume.updating);
  const updateSuccess = useAppSelector(state => state.resume.updateSuccess);

  const handleClose = () => {
    props.history.push('/resume');
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
      ...resumeEntity,
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
          ...resumeEntity,
          filetype: 'PDF',
          idId: resumeEntity?.id?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.resume.home.createOrEditLabel" data-cy="ResumeCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.resume.home.createOrEditLabel">Create or edit a Resume</Translate>
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
                  id="resume-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.path')}
                id="resume-path"
                name="path"
                data-cy="path"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.filetype')}
                id="resume-filetype"
                name="filetype"
                data-cy="filetype"
                type="select"
              >
                <option value="PDF">{translate('simplifyMarketplaceApp.FileType.PDF')}</option>
                <option value="DOC">{translate('simplifyMarketplaceApp.FileType.DOC')}</option>
                <option value="PPT">{translate('simplifyMarketplaceApp.FileType.PPT')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.resumeTitle')}
                id="resume-resumeTitle"
                name="resumeTitle"
                data-cy="resumeTitle"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.isDefault')}
                id="resume-isDefault"
                name="isDefault"
                data-cy="isDefault"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.createdBy')}
                id="resume-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.createdAt')}
                id="resume-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.updatedBy')}
                id="resume-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.resume.updatedAt')}
                id="resume-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <ValidatedField id="resume-id" name="idId" data-cy="id" label={translate('simplifyMarketplaceApp.resume.id')} type="select">
                <option value="" key="0" />
                {workers
                  ? workers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/resume" replace color="info">
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

export default ResumeUpdate;
