package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OTPAttempt;
import com.mycompany.myapp.repository.OTPAttemptRepository;
import com.mycompany.myapp.repository.search.OTPAttemptSearchRepository;
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
 * Integration tests for the {@link OTPAttemptResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OTPAttemptResourceIT {

    private static final Integer DEFAULT_OTP = 1;
    private static final Integer UPDATED_OTP = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_COOOKIE = "AAAAAAAAAA";
    private static final String UPDATED_COOOKIE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/otp-attempts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/otp-attempts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OTPAttemptRepository oTPAttemptRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.OTPAttemptSearchRepositoryMockConfiguration
     */
    @Autowired
    private OTPAttemptSearchRepository mockOTPAttemptSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOTPAttemptMockMvc;

    private OTPAttempt oTPAttempt;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OTPAttempt createEntity(EntityManager em) {
        OTPAttempt oTPAttempt = new OTPAttempt()
            .otp(DEFAULT_OTP)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .ip(DEFAULT_IP)
            .coookie(DEFAULT_COOOKIE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT);
        return oTPAttempt;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OTPAttempt createUpdatedEntity(EntityManager em) {
        OTPAttempt oTPAttempt = new OTPAttempt()
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);
        return oTPAttempt;
    }

    @BeforeEach
    public void initTest() {
        oTPAttempt = createEntity(em);
    }

    @Test
    @Transactional
    void createOTPAttempt() throws Exception {
        int databaseSizeBeforeCreate = oTPAttemptRepository.findAll().size();
        // Create the OTPAttempt
        restOTPAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTPAttempt)))
            .andExpect(status().isCreated());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeCreate + 1);
        OTPAttempt testOTPAttempt = oTPAttemptList.get(oTPAttemptList.size() - 1);
        assertThat(testOTPAttempt.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOTPAttempt.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOTPAttempt.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testOTPAttempt.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testOTPAttempt.getCoookie()).isEqualTo(DEFAULT_COOOKIE);
        assertThat(testOTPAttempt.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOTPAttempt.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(1)).save(testOTPAttempt);
    }

    @Test
    @Transactional
    void createOTPAttemptWithExistingId() throws Exception {
        // Create the OTPAttempt with an existing ID
        oTPAttempt.setId(1L);

        int databaseSizeBeforeCreate = oTPAttemptRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOTPAttemptMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTPAttempt)))
            .andExpect(status().isBadRequest());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeCreate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void getAllOTPAttempts() throws Exception {
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);

        // Get all the oTPAttemptList
        restOTPAttemptMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oTPAttempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].coookie").value(hasItem(DEFAULT_COOOKIE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    void getOTPAttempt() throws Exception {
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);

        // Get the oTPAttempt
        restOTPAttemptMockMvc
            .perform(get(ENTITY_API_URL_ID, oTPAttempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oTPAttempt.getId().intValue()))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.coookie").value(DEFAULT_COOOKIE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOTPAttempt() throws Exception {
        // Get the oTPAttempt
        restOTPAttemptMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOTPAttempt() throws Exception {
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);

        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();

        // Update the oTPAttempt
        OTPAttempt updatedOTPAttempt = oTPAttemptRepository.findById(oTPAttempt.getId()).get();
        // Disconnect from session so that the updates on updatedOTPAttempt are not directly saved in db
        em.detach(updatedOTPAttempt);
        updatedOTPAttempt
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restOTPAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOTPAttempt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOTPAttempt))
            )
            .andExpect(status().isOk());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);
        OTPAttempt testOTPAttempt = oTPAttemptList.get(oTPAttemptList.size() - 1);
        assertThat(testOTPAttempt.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOTPAttempt.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOTPAttempt.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOTPAttempt.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOTPAttempt.getCoookie()).isEqualTo(UPDATED_COOOKIE);
        assertThat(testOTPAttempt.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOTPAttempt.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository).save(testOTPAttempt);
    }

    @Test
    @Transactional
    void putNonExistingOTPAttempt() throws Exception {
        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();
        oTPAttempt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOTPAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oTPAttempt.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oTPAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void putWithIdMismatchOTPAttempt() throws Exception {
        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();
        oTPAttempt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPAttemptMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oTPAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOTPAttempt() throws Exception {
        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();
        oTPAttempt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPAttemptMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTPAttempt)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void partialUpdateOTPAttemptWithPatch() throws Exception {
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);

        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();

        // Update the oTPAttempt using partial update
        OTPAttempt partialUpdatedOTPAttempt = new OTPAttempt();
        partialUpdatedOTPAttempt.setId(oTPAttempt.getId());

        partialUpdatedOTPAttempt
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE)
            .createdAt(UPDATED_CREATED_AT);

        restOTPAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOTPAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOTPAttempt))
            )
            .andExpect(status().isOk());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);
        OTPAttempt testOTPAttempt = oTPAttemptList.get(oTPAttemptList.size() - 1);
        assertThat(testOTPAttempt.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOTPAttempt.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOTPAttempt.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOTPAttempt.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOTPAttempt.getCoookie()).isEqualTo(UPDATED_COOOKIE);
        assertThat(testOTPAttempt.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOTPAttempt.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOTPAttemptWithPatch() throws Exception {
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);

        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();

        // Update the oTPAttempt using partial update
        OTPAttempt partialUpdatedOTPAttempt = new OTPAttempt();
        partialUpdatedOTPAttempt.setId(oTPAttempt.getId());

        partialUpdatedOTPAttempt
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .ip(UPDATED_IP)
            .coookie(UPDATED_COOOKIE)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT);

        restOTPAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOTPAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOTPAttempt))
            )
            .andExpect(status().isOk());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);
        OTPAttempt testOTPAttempt = oTPAttemptList.get(oTPAttemptList.size() - 1);
        assertThat(testOTPAttempt.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOTPAttempt.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOTPAttempt.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOTPAttempt.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testOTPAttempt.getCoookie()).isEqualTo(UPDATED_COOOKIE);
        assertThat(testOTPAttempt.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOTPAttempt.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOTPAttempt() throws Exception {
        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();
        oTPAttempt.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOTPAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oTPAttempt.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oTPAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOTPAttempt() throws Exception {
        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();
        oTPAttempt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oTPAttempt))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOTPAttempt() throws Exception {
        int databaseSizeBeforeUpdate = oTPAttemptRepository.findAll().size();
        oTPAttempt.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPAttemptMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oTPAttempt))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OTPAttempt in the database
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(0)).save(oTPAttempt);
    }

    @Test
    @Transactional
    void deleteOTPAttempt() throws Exception {
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);

        int databaseSizeBeforeDelete = oTPAttemptRepository.findAll().size();

        // Delete the oTPAttempt
        restOTPAttemptMockMvc
            .perform(delete(ENTITY_API_URL_ID, oTPAttempt.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OTPAttempt> oTPAttemptList = oTPAttemptRepository.findAll();
        assertThat(oTPAttemptList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OTPAttempt in Elasticsearch
        verify(mockOTPAttemptSearchRepository, times(1)).deleteById(oTPAttempt.getId());
    }

    @Test
    @Transactional
    void searchOTPAttempt() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        oTPAttemptRepository.saveAndFlush(oTPAttempt);
        when(mockOTPAttemptSearchRepository.search(queryStringQuery("id:" + oTPAttempt.getId())))
            .thenReturn(Collections.singletonList(oTPAttempt));

        // Search the oTPAttempt
        restOTPAttemptMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + oTPAttempt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oTPAttempt.getId().intValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].coookie").value(hasItem(DEFAULT_COOOKIE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
}
