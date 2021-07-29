import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './otp-attempt.reducer';
import { IOTPAttempt } from 'app/shared/model/otp-attempt.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OTPAttemptUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const oTPAttemptEntity = useAppSelector(state => state.oTPAttempt.entity);
  const loading = useAppSelector(state => state.oTPAttempt.loading);
  const updating = useAppSelector(state => state.oTPAttempt.updating);
  const updateSuccess = useAppSelector(state => state.oTPAttempt.updateSuccess);

  const handleClose = () => {
    props.history.push('/otp-attempt');
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
      ...oTPAttemptEntity,
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
          ...oTPAttemptEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.oTPAttempt.home.createOrEditLabel" data-cy="OTPAttemptCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.oTPAttempt.home.createOrEditLabel">Create or edit a OTPAttempt</Translate>
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
                  id="otp-attempt-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.otp')}
                id="otp-attempt-otp"
                name="otp"
                data-cy="otp"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.email')}
                id="otp-attempt-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.phone')}
                id="otp-attempt-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.ip')}
                id="otp-attempt-ip"
                name="ip"
                data-cy="ip"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.coookie')}
                id="otp-attempt-coookie"
                name="coookie"
                data-cy="coookie"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.createdBy')}
                id="otp-attempt-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.oTPAttempt.createdAt')}
                id="otp-attempt-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/otp-attempt" replace color="info">
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

export default OTPAttemptUpdate;
