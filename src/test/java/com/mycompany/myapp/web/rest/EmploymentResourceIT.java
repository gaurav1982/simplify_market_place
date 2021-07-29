package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employment;
import com.mycompany.myapp.repository.EmploymentRepository;
import com.mycompany.myapp.repository.search.EmploymentSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmploymentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmploymentResourceIT {

    private static final String DEFAULT_JOB_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_JOB_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LAST_SALARY = 1;
    private static final Integer UPDATED_LAST_SALARY = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/employments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/employments";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmploymentRepository employmentRepository;

    @Mock
    private EmploymentRepository employmentRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.EmploymentSearchRepositoryMockConfiguration
     */
    @Autowired
    private EmploymentSearchRepository mockEmploymentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmploymentMockMvc;

    private Employment employment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employment createEntity(EntityManager em) {
        Employment employment = new Employment()
            .jobTitle(DEFAULT_JOB_TITLE)
            .companyName(DEFAULT_COMPANY_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .lastSalary(DEFAULT_LAST_SALARY)
            .description(DEFAULT_DESCRIPTION)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return employment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employment createUpdatedEntity(EntityManager em) {
        Employment employment = new Employment()
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastSalary(UPDATED_LAST_SALARY)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return employment;
    }

    @BeforeEach
    public void initTest() {
        employment = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployment() throws Exception {
        int databaseSizeBeforeCreate = employmentRepository.findAll().size();
        // Create the Employment
        restEmploymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employment)))
            .andExpect(status().isCreated());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeCreate + 1);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(DEFAULT_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEmployment.getLastSalary()).isEqualTo(DEFAULT_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testEmployment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmployment.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testEmployment.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testEmployment.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(1)).save(testEmployment);
    }

    @Test
    @Transactional
    void createEmploymentWithExistingId() throws Exception {
        // Create the Employment with an existing ID
        employment.setId(1L);

        int databaseSizeBeforeCreate = employmentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmploymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employment)))
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void getAllEmployments() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        // Get all the employmentList
        restEmploymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employment.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastSalary").value(hasItem(DEFAULT_LAST_SALARY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmploymentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(employmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmploymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employmentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmploymentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(employmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmploymentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(employmentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEmployment() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        // Get the employment
        restEmploymentMockMvc
            .perform(get(ENTITY_API_URL_ID, employment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employment.getId().intValue()))
            .andExpect(jsonPath("$.jobTitle").value(DEFAULT_JOB_TITLE))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.lastSalary").value(DEFAULT_LAST_SALARY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEmployment() throws Exception {
        // Get the employment
        restEmploymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEmployment() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();

        // Update the employment
        Employment updatedEmployment = employmentRepository.findById(employment.getId()).get();
        // Disconnect from session so that the updates on updatedEmployment are not directly saved in db
        em.detach(updatedEmployment);
        updatedEmployment
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastSalary(UPDATED_LAST_SALARY)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployment))
            )
            .andExpect(status().isOk());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEmployment.getLastSalary()).isEqualTo(UPDATED_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployment.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployment.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository).save(testEmployment);
    }

    @Test
    @Transactional
    void putNonExistingEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void partialUpdateEmploymentWithPatch() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();

        // Update the employment using partial update
        Employment partialUpdatedEmployment = new Employment();
        partialUpdatedEmployment.setId(employment.getId());

        partialUpdatedEmployment
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY);

        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployment))
            )
            .andExpect(status().isOk());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testEmployment.getLastSalary()).isEqualTo(DEFAULT_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployment.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testEmployment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployment.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployment.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateEmploymentWithPatch() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();

        // Update the employment using partial update
        Employment partialUpdatedEmployment = new Employment();
        partialUpdatedEmployment.setId(employment.getId());

        partialUpdatedEmployment
            .jobTitle(UPDATED_JOB_TITLE)
            .companyName(UPDATED_COMPANY_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .lastSalary(UPDATED_LAST_SALARY)
            .description(UPDATED_DESCRIPTION)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployment))
            )
            .andExpect(status().isOk());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);
        Employment testEmployment = employmentList.get(employmentList.size() - 1);
        assertThat(testEmployment.getJobTitle()).isEqualTo(UPDATED_JOB_TITLE);
        assertThat(testEmployment.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testEmployment.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmployment.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testEmployment.getLastSalary()).isEqualTo(UPDATED_LAST_SALARY);
        assertThat(testEmployment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testEmployment.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testEmployment.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testEmployment.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testEmployment.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employment))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployment() throws Exception {
        int databaseSizeBeforeUpdate = employmentRepository.findAll().size();
        employment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmploymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employment in the database
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(0)).save(employment);
    }

    @Test
    @Transactional
    void deleteEmployment() throws Exception {
        // Initialize the database
        employmentRepository.saveAndFlush(employment);

        int databaseSizeBeforeDelete = employmentRepository.findAll().size();

        // Delete the employment
        restEmploymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, employment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employment> employmentList = employmentRepository.findAll();
        assertThat(employmentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Employment in Elasticsearch
        verify(mockEmploymentSearchRepository, times(1)).deleteById(employment.getId());
    }

    @Test
    @Transactional
    void searchEmployment() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        employmentRepository.saveAndFlush(employment);
        when(mockEmploymentSearchRepository.search(queryStringQuery("id:" + employment.getId())))
            .thenReturn(Collections.singletonList(employment));

        // Search the employment
        restEmploymentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + employment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employment.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobTitle").value(hasItem(DEFAULT_JOB_TITLE)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastSalary").value(hasItem(DEFAULT_LAST_SALARY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
