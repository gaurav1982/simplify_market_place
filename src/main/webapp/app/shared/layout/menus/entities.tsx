import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/category">
      <Translate contentKey="global.menu.entities.category" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/field">
      <Translate contentKey="global.menu.entities.field" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/client">
      <Translate contentKey="global.menu.entities.client" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/worker">
      <Translate contentKey="global.menu.entities.worker" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/resume">
      <Translate contentKey="global.menu.entities.resume" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/job-preference">
      <Translate contentKey="global.menu.entities.jobPreference" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/job-specific-field">
      <Translate contentKey="global.menu.entities.jobSpecificField" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/location">
      <Translate contentKey="global.menu.entities.location" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/location-prefrence">
      <Translate contentKey="global.menu.entities.locationPrefrence" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/education">
      <Translate contentKey="global.menu.entities.education" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/employment">
      <Translate contentKey="global.menu.entities.employment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/otp">
      <Translate contentKey="global.menu.entities.otp" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/otp-attempt">
      <Translate contentKey="global.menu.entities.otpAttempt" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
