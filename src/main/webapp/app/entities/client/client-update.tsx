import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './client.reducer';
import { IClient } from 'app/shared/model/client.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ClientUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const clientEntity = useAppSelector(state => state.client.entity);
  const loading = useAppSelector(state => state.client.loading);
  const updating = useAppSelector(state => state.client.updating);
  const updateSuccess = useAppSelector(state => state.client.updateSuccess);

  const handleClose = () => {
    props.history.push('/client');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...clientEntity,
      ...values,
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
          ...clientEntity,
          compType: 'IT',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.client.home.createOrEditLabel" data-cy="ClientCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.client.home.createOrEditLabel">Create or edit a Client</Translate>
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
                  id="client-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.compName')}
                id="client-compName"
                name="compName"
                data-cy="compName"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.compAddress')}
                id="client-compAddress"
                name="compAddress"
                data-cy="compAddress"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.compWebsite')}
                id="client-compWebsite"
                name="compWebsite"
                data-cy="compWebsite"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.compType')}
                id="client-compType"
                name="compType"
                data-cy="compType"
                type="select"
              >
                <option value="IT">{translate('simplifyMarketplaceApp.CompType.IT')}</option>
                <option value="Consultant">{translate('simplifyMarketplaceApp.CompType.Consultant')}</option>
                <option value="Hospital">{translate('simplifyMarketplaceApp.CompType.Hospital')}</option>
                <option value="MediaHouse">{translate('simplifyMarketplaceApp.CompType.MediaHouse')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.compContactNo')}
                id="client-compContactNo"
                name="compContactNo"
                data-cy="compContactNo"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.createdBy')}
                id="client-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.createdAt')}
                id="client-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.updatedBy')}
                id="client-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.client.updatedAt')}
                id="client-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/client" replace color="info">
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

export default ClientUpdate;
