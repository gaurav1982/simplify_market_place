import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './otp.reducer';
import { IOTP } from 'app/shared/model/otp.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OTPUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const oTPEntity = useAppSelector(state => state.oTP.entity);
  const loading = useAppSelector(state => state.oTP.loading);
  const updating = useAppSelector(state => state.oTP.updating);
  const updateSuccess = useAppSelector(state => state.oTP.updateSuccess);

  const handleClose = () => {
    props.history.push('/otp');
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
      ...oTPEntity,
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
          ...oTPEntity,
          type: 'Email',
          status: 'Pending',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.oTP.home.createOrEditLabel" data-cy="OTPCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.oTP.home.createOrEditLabel">Create or edit a OTP</Translate>
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
                  id="otp-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('simplifyMarketplaceApp.oTP.otp')} id="otp-otp" name="otp" data-cy="otp" type="text" />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.email')}
                id="otp-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.phone')}
                id="otp-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField label={translate('simplifyMarketplaceApp.oTP.type')} id="otp-type" name="type" data-cy="type" type="select">
                <option value="Email">{translate('simplifyMarketplaceApp.OtpType.Email')}</option>
                <option value="Phone">{translate('simplifyMarketplaceApp.OtpType.Phone')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.expiryTime')}
                id="otp-expiryTime"
                name="expiryTime"
                data-cy="expiryTime"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.status')}
                id="otp-status"
                name="status"
                data-cy="status"
                type="select"
              >
                <option value="Pending">{translate('simplifyMarketplaceApp.OtpStatus.Pending')}</option>
                <option value="Failed">{translate('simplifyMarketplaceApp.OtpStatus.Failed')}</option>
                <option value="Approved">{translate('simplifyMarketplaceApp.OtpStatus.Approved')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.createdBy')}
                id="otp-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.createdAt')}
                id="otp-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.updatedBy')}
                id="otp-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTP.updatedAt')}
                id="otp-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/otp" replace color="info">
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

export default OTPUpdate;
