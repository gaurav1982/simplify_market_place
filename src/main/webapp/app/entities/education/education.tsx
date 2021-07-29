import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './education.reducer';
import { IEducation } from 'app/shared/model/education.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Education = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const educationList = useAppSelector(state => state.education.entities);
  const loading = useAppSelector(state => state.education.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const startSearching = e => {
    if (search) {
      dispatch(searchEntities({ query: search }));
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="education-heading" data-cy="EducationHeading">
        <Translate contentKey="simplifyMarketplaceApp.education.home.title">Educations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.education.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.education.home.createLabel">Create new Education</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('simplifyMarketplaceApp.education.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {educationList && educationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.degreeName">Degree Name</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.institute">Institute</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.yearOfPassing">Year Of Passing</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.marks">Marks</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.startDate">Start Date</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.endDate">End Date</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.degreeType">Degree Type</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.education.id">Id</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {educationList.map((education, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${education.id}`} color="link" size="sm">
                      {education.id}
                    </Button>
                  </td>
                  <td>{education.degreeName}</td>
                  <td>{education.institute}</td>
                  <td>{education.yearOfPassing}</td>
                  <td>{education.marks}</td>
                  <td>
                    {education.startDate ? <TextFormat type="date" value={education.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{education.endDate ? <TextFormat type="date" value={education.endDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.DegreeType.${education.degreeType}`} />
                  </td>
                  <td>{education.description}</td>
                  <td>{education.createdBy}</td>
                  <td>
                    {education.createdAt ? <TextFormat type="date" value={education.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{education.updatedBy}</td>
                  <td>
                    {education.updatedAt ? <TextFormat type="date" value={education.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{education.id ? <Link to={`worker/${education.id.id}`}>{education.id.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${education.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${education.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${education.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="simplifyMarketplaceApp.education.home.notFound">No Educations found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Education;
