import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntity, updateEntity, createEntity, reset } from './field.reducer';
import { IField } from 'app/shared/model/field.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const FieldUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const categories = useAppSelector(state => state.category.entities);
  const fieldEntity = useAppSelector(state => state.field.entity);
  const loading = useAppSelector(state => state.field.loading);
  const updating = useAppSelector(state => state.field.updating);
  const updateSuccess = useAppSelector(state => state.field.updateSuccess);

  const handleClose = () => {
    props.history.push('/field');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getCategories({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...fieldEntity,
      ...values,
      categories: mapIdList(values.categories),
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
          ...fieldEntity,
          fieldType: 'String',
          categories: fieldEntity?.categories?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="simplifyMarketplaceApp.field.home.createOrEditLabel" data-cy="FieldCreateUpdateHeading">
            <Translate contentKey="simplifyMarketplaceApp.field.home.createOrEditLabel">Create or edit a Field</Translate>
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
                  id="field-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.fieldName')}
                id="field-fieldName"
                name="fieldName"
                data-cy="fieldName"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.fieldLabel')}
                id="field-fieldLabel"
                name="fieldLabel"
                data-cy="fieldLabel"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.fieldType')}
                id="field-fieldType"
                name="fieldType"
                data-cy="fieldType"
                type="select"
              >
                <option value="String">{translate('simplifyMarketplaceApp.FieldType.String')}</option>
                <option value="Date">{translate('simplifyMarketplaceApp.FieldType.Date')}</option>
                <option value="Integer">{translate('simplifyMarketplaceApp.FieldType.Integer')}</option>
                <option value="Rate">{translate('simplifyMarketplaceApp.FieldType.Rate')}</option>
                <option value="Email">{translate('simplifyMarketplaceApp.FieldType.Email')}</option>
              </ValidatedField>
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.isDeleted')}
                id="field-isDeleted"
                name="isDeleted"
                data-cy="isDeleted"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.createdBy')}
                id="field-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.createdAt')}
                id="field-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.updatedBy')}
                id="field-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.updatedAt')}
                id="field-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="date"
              />
              <ValidatedField
                label={translate('simplifyMarketplaceApp.field.category')}
                id="field-category"
                data-cy="category"
                type="select"
                multiple
                name="categories"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/field" replace color="info">
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

export default FieldUpdate;
