import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Input, InputGroup, FormGroup, Form, Col, Row, Table } from 'reactstrap';
import { Translate, translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { searchEntities, getEntities } from './resume.reducer';
import { IResume } from 'app/shared/model/resume.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Resume = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [search, setSearch] = useState('');

  const resumeList = useAppSelector(state => state.resume.entities);
  const loading = useAppSelector(state => state.resume.loading);

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
      <h2 id="resume-heading" data-cy="ResumeHeading">
        <Translate contentKey="simplifyMarketplaceApp.resume.home.title">Resumes</Translate>
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="simplifyMarketplaceApp.resume.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="simplifyMarketplaceApp.resume.home.createLabel">Create new Resume</Translate>
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
                  placeholder={translate('simplifyMarketplaceApp.resume.home.search')}
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
        {resumeList && resumeList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.path">Path</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.filetype">Filetype</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.resumeTitle">Resume Title</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.isDefault">Is Default</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.createdBy">Created By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.createdAt">Created At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.updatedBy">Updated By</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.updatedAt">Updated At</Translate>
                </th>
                <th>
                  <Translate contentKey="simplifyMarketplaceApp.resume.worker">Worker</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {resumeList.map((resume, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${resume.id}`} color="link" size="sm">
                      {resume.id}
                    </Button>
                  </td>
                  <td>{resume.path}</td>
                  <td>
                    <Translate contentKey={`simplifyMarketplaceApp.FileType.${resume.filetype}`} />
                  </td>
                  <td>{resume.resumeTitle}</td>
                  <td>{resume.isDefault ? 'true' : 'false'}</td>
                  <td>{resume.createdBy}</td>
                  <td>{resume.createdAt ? <TextFormat type="date" value={resume.createdAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{resume.updatedBy}</td>
                  <td>{resume.updatedAt ? <TextFormat type="date" value={resume.updatedAt} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{resume.worker ? <Link to={`worker/${resume.worker.id}`}>{resume.worker.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${resume.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${resume.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${resume.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="simplifyMarketplaceApp.resume.home.notFound">No Resumes found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Resume;
