package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.JobSpecificField;
import com.mycompany.myapp.repository.JobSpecificFieldRepository;
import com.mycompany.myapp.repository.search.JobSpecificFieldSearchRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JobSpecificFieldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobSpecificFieldResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/job-specific-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/job-specific-fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobSpecificFieldRepository jobSpecificFieldRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.JobSpecificFieldSearchRepositoryMockConfiguration
     */
    @Autowired
    private JobSpecificFieldSearchRepository mockJobSpecificFieldSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobSpecificFieldMockMvc;

    private JobSpecificField jobSpecificField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobSpecificField createEntity(EntityManager em) {
        JobSpecificField jobSpecificField = new JobSpecificField().value(DEFAULT_VALUE);
        return jobSpecificField;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobSpecificField createUpdatedEntity(EntityManager em) {
        JobSpecificField jobSpecificField = new JobSpecificField().value(UPDATED_VALUE);
        return jobSpecificField;
    }

    @BeforeEach
    public void initTest() {
        jobSpecificField = createEntity(em);
    }

    @Test
    @Transactional
    void createJobSpecificField() throws Exception {
        int databaseSizeBeforeCreate = jobSpecificFieldRepository.findAll().size();
        // Create the JobSpecificField
        restJobSpecificFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isCreated());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeCreate + 1);
        JobSpecificField testJobSpecificField = jobSpecificFieldList.get(jobSpecificFieldList.size() - 1);
        assertThat(testJobSpecificField.getValue()).isEqualTo(DEFAULT_VALUE);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(1)).save(testJobSpecificField);
    }

    @Test
    @Transactional
    void createJobSpecificFieldWithExistingId() throws Exception {
        // Create the JobSpecificField with an existing ID
        jobSpecificField.setId(1L);

        int databaseSizeBeforeCreate = jobSpecificFieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobSpecificFieldMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeCreate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void getAllJobSpecificFields() throws Exception {
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);

        // Get all the jobSpecificFieldList
        restJobSpecificFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSpecificField.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getJobSpecificField() throws Exception {
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);

        // Get the jobSpecificField
        restJobSpecificFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, jobSpecificField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobSpecificField.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingJobSpecificField() throws Exception {
        // Get the jobSpecificField
        restJobSpecificFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobSpecificField() throws Exception {
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);

        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();

        // Update the jobSpecificField
        JobSpecificField updatedJobSpecificField = jobSpecificFieldRepository.findById(jobSpecificField.getId()).get();
        // Disconnect from session so that the updates on updatedJobSpecificField are not directly saved in db
        em.detach(updatedJobSpecificField);
        updatedJobSpecificField.value(UPDATED_VALUE);

        restJobSpecificFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobSpecificField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobSpecificField))
            )
            .andExpect(status().isOk());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);
        JobSpecificField testJobSpecificField = jobSpecificFieldList.get(jobSpecificFieldList.size() - 1);
        assertThat(testJobSpecificField.getValue()).isEqualTo(UPDATED_VALUE);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository).save(testJobSpecificField);
    }

    @Test
    @Transactional
    void putNonExistingJobSpecificField() throws Exception {
        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();
        jobSpecificField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobSpecificFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobSpecificField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobSpecificField() throws Exception {
        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();
        jobSpecificField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSpecificFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobSpecificField() throws Exception {
        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();
        jobSpecificField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSpecificFieldMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void partialUpdateJobSpecificFieldWithPatch() throws Exception {
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);

        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();

        // Update the jobSpecificField using partial update
        JobSpecificField partialUpdatedJobSpecificField = new JobSpecificField();
        partialUpdatedJobSpecificField.setId(jobSpecificField.getId());

        partialUpdatedJobSpecificField.value(UPDATED_VALUE);

        restJobSpecificFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobSpecificField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobSpecificField))
            )
            .andExpect(status().isOk());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);
        JobSpecificField testJobSpecificField = jobSpecificFieldList.get(jobSpecificFieldList.size() - 1);
        assertThat(testJobSpecificField.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateJobSpecificFieldWithPatch() throws Exception {
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);

        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();

        // Update the jobSpecificField using partial update
        JobSpecificField partialUpdatedJobSpecificField = new JobSpecificField();
        partialUpdatedJobSpecificField.setId(jobSpecificField.getId());

        partialUpdatedJobSpecificField.value(UPDATED_VALUE);

        restJobSpecificFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobSpecificField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobSpecificField))
            )
            .andExpect(status().isOk());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);
        JobSpecificField testJobSpecificField = jobSpecificFieldList.get(jobSpecificFieldList.size() - 1);
        assertThat(testJobSpecificField.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingJobSpecificField() throws Exception {
        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();
        jobSpecificField.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobSpecificFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobSpecificField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobSpecificField() throws Exception {
        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();
        jobSpecificField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSpecificFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobSpecificField() throws Exception {
        int databaseSizeBeforeUpdate = jobSpecificFieldRepository.findAll().size();
        jobSpecificField.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobSpecificFieldMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobSpecificField))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobSpecificField in the database
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(0)).save(jobSpecificField);
    }

    @Test
    @Transactional
    void deleteJobSpecificField() throws Exception {
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);

        int databaseSizeBeforeDelete = jobSpecificFieldRepository.findAll().size();

        // Delete the jobSpecificField
        restJobSpecificFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobSpecificField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobSpecificField> jobSpecificFieldList = jobSpecificFieldRepository.findAll();
        assertThat(jobSpecificFieldList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the JobSpecificField in Elasticsearch
        verify(mockJobSpecificFieldSearchRepository, times(1)).deleteById(jobSpecificField.getId());
    }

    @Test
    @Transactional
    void searchJobSpecificField() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        jobSpecificFieldRepository.saveAndFlush(jobSpecificField);
        when(mockJobSpecificFieldSearchRepository.search(queryStringQuery("id:" + jobSpecificField.getId())))
            .thenReturn(Collections.singletonList(jobSpecificField));

        // Search the jobSpecificField
        restJobSpecificFieldMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + jobSpecificField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobSpecificField.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
}
