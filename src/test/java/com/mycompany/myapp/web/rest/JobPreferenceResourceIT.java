package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.JobPreference;
import com.mycompany.myapp.domain.enumeration.EngagementType;
import com.mycompany.myapp.domain.enumeration.LocationType;
import com.mycompany.myapp.repository.JobPreferenceRepository;
import com.mycompany.myapp.repository.search.JobPreferenceSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link JobPreferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JobPreferenceResourceIT {

    private static final Integer DEFAULT_HOURLY_RATE = 1;
    private static final Integer UPDATED_HOURLY_RATE = 2;

    private static final Integer DEFAULT_DAILY_RATE = 1;
    private static final Integer UPDATED_DAILY_RATE = 2;

    private static final Integer DEFAULT_MONTHLY_RATE = 1;
    private static final Integer UPDATED_MONTHLY_RATE = 2;

    private static final Integer DEFAULT_HOUR_PER_DAY = 1;
    private static final Integer UPDATED_HOUR_PER_DAY = 2;

    private static final Integer DEFAULT_HOUR_PER_WEEK = 1;
    private static final Integer UPDATED_HOUR_PER_WEEK = 2;

    private static final EngagementType DEFAULT_ENGAGEMENT_TYPE = EngagementType.FullTime;
    private static final EngagementType UPDATED_ENGAGEMENT_TYPE = EngagementType.Consultant;

    private static final LocationType DEFAULT_LOCATION_TYPE = LocationType.Worklocation;
    private static final LocationType UPDATED_LOCATION_TYPE = LocationType.Homelocation;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/job-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/job-preferences";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JobPreferenceRepository jobPreferenceRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.JobPreferenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private JobPreferenceSearchRepository mockJobPreferenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJobPreferenceMockMvc;

    private JobPreference jobPreference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobPreference createEntity(EntityManager em) {
        JobPreference jobPreference = new JobPreference()
            .hourlyRate(DEFAULT_HOURLY_RATE)
            .dailyRate(DEFAULT_DAILY_RATE)
            .monthlyRate(DEFAULT_MONTHLY_RATE)
            .hourPerDay(DEFAULT_HOUR_PER_DAY)
            .hourPerWeek(DEFAULT_HOUR_PER_WEEK)
            .engagementType(DEFAULT_ENGAGEMENT_TYPE)
            .locationType(DEFAULT_LOCATION_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return jobPreference;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JobPreference createUpdatedEntity(EntityManager em) {
        JobPreference jobPreference = new JobPreference()
            .hourlyRate(UPDATED_HOURLY_RATE)
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyRate(UPDATED_MONTHLY_RATE)
            .hourPerDay(UPDATED_HOUR_PER_DAY)
            .hourPerWeek(UPDATED_HOUR_PER_WEEK)
            .engagementType(UPDATED_ENGAGEMENT_TYPE)
            .locationType(UPDATED_LOCATION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return jobPreference;
    }

    @BeforeEach
    public void initTest() {
        jobPreference = createEntity(em);
    }

    @Test
    @Transactional
    void createJobPreference() throws Exception {
        int databaseSizeBeforeCreate = jobPreferenceRepository.findAll().size();
        // Create the JobPreference
        restJobPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobPreference)))
            .andExpect(status().isCreated());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeCreate + 1);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(DEFAULT_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(DEFAULT_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(DEFAULT_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(DEFAULT_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(DEFAULT_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(DEFAULT_LOCATION_TYPE);
        assertThat(testJobPreference.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testJobPreference.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testJobPreference.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testJobPreference.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(1)).save(testJobPreference);
    }

    @Test
    @Transactional
    void createJobPreferenceWithExistingId() throws Exception {
        // Create the JobPreference with an existing ID
        jobPreference.setId(1L);

        int databaseSizeBeforeCreate = jobPreferenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobPreference)))
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void getAllJobPreferences() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        // Get all the jobPreferenceList
        restJobPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE)))
            .andExpect(jsonPath("$.[*].dailyRate").value(hasItem(DEFAULT_DAILY_RATE)))
            .andExpect(jsonPath("$.[*].monthlyRate").value(hasItem(DEFAULT_MONTHLY_RATE)))
            .andExpect(jsonPath("$.[*].hourPerDay").value(hasItem(DEFAULT_HOUR_PER_DAY)))
            .andExpect(jsonPath("$.[*].hourPerWeek").value(hasItem(DEFAULT_HOUR_PER_WEEK)))
            .andExpect(jsonPath("$.[*].engagementType").value(hasItem(DEFAULT_ENGAGEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locationType").value(hasItem(DEFAULT_LOCATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getJobPreference() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        // Get the jobPreference
        restJobPreferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, jobPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jobPreference.getId().intValue()))
            .andExpect(jsonPath("$.hourlyRate").value(DEFAULT_HOURLY_RATE))
            .andExpect(jsonPath("$.dailyRate").value(DEFAULT_DAILY_RATE))
            .andExpect(jsonPath("$.monthlyRate").value(DEFAULT_MONTHLY_RATE))
            .andExpect(jsonPath("$.hourPerDay").value(DEFAULT_HOUR_PER_DAY))
            .andExpect(jsonPath("$.hourPerWeek").value(DEFAULT_HOUR_PER_WEEK))
            .andExpect(jsonPath("$.engagementType").value(DEFAULT_ENGAGEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.locationType").value(DEFAULT_LOCATION_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingJobPreference() throws Exception {
        // Get the jobPreference
        restJobPreferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJobPreference() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();

        // Update the jobPreference
        JobPreference updatedJobPreference = jobPreferenceRepository.findById(jobPreference.getId()).get();
        // Disconnect from session so that the updates on updatedJobPreference are not directly saved in db
        em.detach(updatedJobPreference);
        updatedJobPreference
            .hourlyRate(UPDATED_HOURLY_RATE)
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyRate(UPDATED_MONTHLY_RATE)
            .hourPerDay(UPDATED_HOUR_PER_DAY)
            .hourPerWeek(UPDATED_HOUR_PER_WEEK)
            .engagementType(UPDATED_ENGAGEMENT_TYPE)
            .locationType(UPDATED_LOCATION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJobPreference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJobPreference))
            )
            .andExpect(status().isOk());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(UPDATED_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(UPDATED_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(UPDATED_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(UPDATED_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(UPDATED_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testJobPreference.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJobPreference.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testJobPreference.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testJobPreference.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository).save(testJobPreference);
    }

    @Test
    @Transactional
    void putNonExistingJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jobPreference.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void putWithIdMismatchJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jobPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jobPreference)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void partialUpdateJobPreferenceWithPatch() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();

        // Update the jobPreference using partial update
        JobPreference partialUpdatedJobPreference = new JobPreference();
        partialUpdatedJobPreference.setId(jobPreference.getId());

        partialUpdatedJobPreference.hourPerDay(UPDATED_HOUR_PER_DAY).hourPerWeek(UPDATED_HOUR_PER_WEEK).createdBy(UPDATED_CREATED_BY);

        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobPreference))
            )
            .andExpect(status().isOk());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(DEFAULT_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(DEFAULT_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(DEFAULT_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(UPDATED_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(UPDATED_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(DEFAULT_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(DEFAULT_LOCATION_TYPE);
        assertThat(testJobPreference.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJobPreference.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testJobPreference.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testJobPreference.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateJobPreferenceWithPatch() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();

        // Update the jobPreference using partial update
        JobPreference partialUpdatedJobPreference = new JobPreference();
        partialUpdatedJobPreference.setId(jobPreference.getId());

        partialUpdatedJobPreference
            .hourlyRate(UPDATED_HOURLY_RATE)
            .dailyRate(UPDATED_DAILY_RATE)
            .monthlyRate(UPDATED_MONTHLY_RATE)
            .hourPerDay(UPDATED_HOUR_PER_DAY)
            .hourPerWeek(UPDATED_HOUR_PER_WEEK)
            .engagementType(UPDATED_ENGAGEMENT_TYPE)
            .locationType(UPDATED_LOCATION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJobPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJobPreference))
            )
            .andExpect(status().isOk());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);
        JobPreference testJobPreference = jobPreferenceList.get(jobPreferenceList.size() - 1);
        assertThat(testJobPreference.getHourlyRate()).isEqualTo(UPDATED_HOURLY_RATE);
        assertThat(testJobPreference.getDailyRate()).isEqualTo(UPDATED_DAILY_RATE);
        assertThat(testJobPreference.getMonthlyRate()).isEqualTo(UPDATED_MONTHLY_RATE);
        assertThat(testJobPreference.getHourPerDay()).isEqualTo(UPDATED_HOUR_PER_DAY);
        assertThat(testJobPreference.getHourPerWeek()).isEqualTo(UPDATED_HOUR_PER_WEEK);
        assertThat(testJobPreference.getEngagementType()).isEqualTo(UPDATED_ENGAGEMENT_TYPE);
        assertThat(testJobPreference.getLocationType()).isEqualTo(UPDATED_LOCATION_TYPE);
        assertThat(testJobPreference.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testJobPreference.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testJobPreference.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testJobPreference.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jobPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jobPreference))
            )
            .andExpect(status().isBadRequest());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJobPreference() throws Exception {
        int databaseSizeBeforeUpdate = jobPreferenceRepository.findAll().size();
        jobPreference.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJobPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jobPreference))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JobPreference in the database
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(0)).save(jobPreference);
    }

    @Test
    @Transactional
    void deleteJobPreference() throws Exception {
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);

        int databaseSizeBeforeDelete = jobPreferenceRepository.findAll().size();

        // Delete the jobPreference
        restJobPreferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, jobPreference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JobPreference> jobPreferenceList = jobPreferenceRepository.findAll();
        assertThat(jobPreferenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the JobPreference in Elasticsearch
        verify(mockJobPreferenceSearchRepository, times(1)).deleteById(jobPreference.getId());
    }

    @Test
    @Transactional
    void searchJobPreference() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        jobPreferenceRepository.saveAndFlush(jobPreference);
        when(mockJobPreferenceSearchRepository.search(queryStringQuery("id:" + jobPreference.getId())))
            .thenReturn(Collections.singletonList(jobPreference));

        // Search the jobPreference
        restJobPreferenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + jobPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].hourlyRate").value(hasItem(DEFAULT_HOURLY_RATE)))
            .andExpect(jsonPath("$.[*].dailyRate").value(hasItem(DEFAULT_DAILY_RATE)))
            .andExpect(jsonPath("$.[*].monthlyRate").value(hasItem(DEFAULT_MONTHLY_RATE)))
            .andExpect(jsonPath("$.[*].hourPerDay").value(hasItem(DEFAULT_HOUR_PER_DAY)))
            .andExpect(jsonPath("$.[*].hourPerWeek").value(hasItem(DEFAULT_HOUR_PER_WEEK)))
            .andExpect(jsonPath("$.[*].engagementType").value(hasItem(DEFAULT_ENGAGEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].locationType").value(hasItem(DEFAULT_LOCATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
