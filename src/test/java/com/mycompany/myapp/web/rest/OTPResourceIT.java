package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OTP;
import com.mycompany.myapp.domain.enumeration.OtpStatus;
import com.mycompany.myapp.domain.enumeration.OtpType;
import com.mycompany.myapp.repository.OTPRepository;
import com.mycompany.myapp.repository.search.OTPSearchRepository;
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
 * Integration tests for the {@link OTPResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OTPResourceIT {

    private static final Integer DEFAULT_OTP = 1;
    private static final Integer UPDATED_OTP = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Integer DEFAULT_PHONE = 1;
    private static final Integer UPDATED_PHONE = 2;

    private static final OtpType DEFAULT_TYPE = OtpType.Email;
    private static final OtpType UPDATED_TYPE = OtpType.Phone;

    private static final LocalDate DEFAULT_EXPIRY_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRY_TIME = LocalDate.now(ZoneId.systemDefault());

    private static final OtpStatus DEFAULT_STATUS = OtpStatus.Pending;
    private static final OtpStatus UPDATED_STATUS = OtpStatus.Failed;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/otps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/otps";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OTPRepository oTPRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.OTPSearchRepositoryMockConfiguration
     */
    @Autowired
    private OTPSearchRepository mockOTPSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOTPMockMvc;

    private OTP oTP;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OTP createEntity(EntityManager em) {
        OTP oTP = new OTP()
            .otp(DEFAULT_OTP)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .type(DEFAULT_TYPE)
            .expiryTime(DEFAULT_EXPIRY_TIME)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return oTP;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OTP createUpdatedEntity(EntityManager em) {
        OTP oTP = new OTP()
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return oTP;
    }

    @BeforeEach
    public void initTest() {
        oTP = createEntity(em);
    }

    @Test
    @Transactional
    void createOTP() throws Exception {
        int databaseSizeBeforeCreate = oTPRepository.findAll().size();
        // Create the OTP
        restOTPMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTP)))
            .andExpect(status().isCreated());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeCreate + 1);
        OTP testOTP = oTPList.get(oTPList.size() - 1);
        assertThat(testOTP.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOTP.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOTP.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testOTP.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testOTP.getExpiryTime()).isEqualTo(DEFAULT_EXPIRY_TIME);
        assertThat(testOTP.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOTP.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOTP.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOTP.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOTP.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(1)).save(testOTP);
    }

    @Test
    @Transactional
    void createOTPWithExistingId() throws Exception {
        // Create the OTP with an existing ID
        oTP.setId(1L);

        int databaseSizeBeforeCreate = oTPRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOTPMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTP)))
            .andExpect(status().isBadRequest());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeCreate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void getAllOTPS() throws Exception {
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);

        // Get all the oTPList
        restOTPMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oTP.getId().intValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expiryTime").value(hasItem(DEFAULT_EXPIRY_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getOTP() throws Exception {
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);

        // Get the oTP
        restOTPMockMvc
            .perform(get(ENTITY_API_URL_ID, oTP.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oTP.getId().intValue()))
            .andExpect(jsonPath("$.otp").value(DEFAULT_OTP))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.expiryTime").value(DEFAULT_EXPIRY_TIME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOTP() throws Exception {
        // Get the oTP
        restOTPMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOTP() throws Exception {
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);

        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();

        // Update the oTP
        OTP updatedOTP = oTPRepository.findById(oTP.getId()).get();
        // Disconnect from session so that the updates on updatedOTP are not directly saved in db
        em.detach(updatedOTP);
        updatedOTP
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restOTPMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOTP.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOTP))
            )
            .andExpect(status().isOk());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);
        OTP testOTP = oTPList.get(oTPList.size() - 1);
        assertThat(testOTP.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOTP.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOTP.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOTP.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOTP.getExpiryTime()).isEqualTo(UPDATED_EXPIRY_TIME);
        assertThat(testOTP.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOTP.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOTP.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOTP.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOTP.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository).save(testOTP);
    }

    @Test
    @Transactional
    void putNonExistingOTP() throws Exception {
        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();
        oTP.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOTPMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oTP.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTP))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void putWithIdMismatchOTP() throws Exception {
        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();
        oTP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oTP))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOTP() throws Exception {
        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();
        oTP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oTP)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void partialUpdateOTPWithPatch() throws Exception {
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);

        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();

        // Update the oTP using partial update
        OTP partialUpdatedOTP = new OTP();
        partialUpdatedOTP.setId(oTP.getId());

        partialUpdatedOTP.phone(UPDATED_PHONE).type(UPDATED_TYPE).expiryTime(UPDATED_EXPIRY_TIME).updatedAt(UPDATED_UPDATED_AT);

        restOTPMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOTP.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOTP))
            )
            .andExpect(status().isOk());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);
        OTP testOTP = oTPList.get(oTPList.size() - 1);
        assertThat(testOTP.getOtp()).isEqualTo(DEFAULT_OTP);
        assertThat(testOTP.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOTP.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOTP.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOTP.getExpiryTime()).isEqualTo(UPDATED_EXPIRY_TIME);
        assertThat(testOTP.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testOTP.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testOTP.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testOTP.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testOTP.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateOTPWithPatch() throws Exception {
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);

        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();

        // Update the oTP using partial update
        OTP partialUpdatedOTP = new OTP();
        partialUpdatedOTP.setId(oTP.getId());

        partialUpdatedOTP
            .otp(UPDATED_OTP)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .type(UPDATED_TYPE)
            .expiryTime(UPDATED_EXPIRY_TIME)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restOTPMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOTP.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOTP))
            )
            .andExpect(status().isOk());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);
        OTP testOTP = oTPList.get(oTPList.size() - 1);
        assertThat(testOTP.getOtp()).isEqualTo(UPDATED_OTP);
        assertThat(testOTP.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOTP.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOTP.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testOTP.getExpiryTime()).isEqualTo(UPDATED_EXPIRY_TIME);
        assertThat(testOTP.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testOTP.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testOTP.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testOTP.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testOTP.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingOTP() throws Exception {
        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();
        oTP.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOTPMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oTP.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oTP))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOTP() throws Exception {
        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();
        oTP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oTP))
            )
            .andExpect(status().isBadRequest());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOTP() throws Exception {
        int databaseSizeBeforeUpdate = oTPRepository.findAll().size();
        oTP.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOTPMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oTP)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OTP in the database
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeUpdate);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(0)).save(oTP);
    }

    @Test
    @Transactional
    void deleteOTP() throws Exception {
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);

        int databaseSizeBeforeDelete = oTPRepository.findAll().size();

        // Delete the oTP
        restOTPMockMvc.perform(delete(ENTITY_API_URL_ID, oTP.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OTP> oTPList = oTPRepository.findAll();
        assertThat(oTPList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the OTP in Elasticsearch
        verify(mockOTPSearchRepository, times(1)).deleteById(oTP.getId());
    }

    @Test
    @Transactional
    void searchOTP() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        oTPRepository.saveAndFlush(oTP);
        when(mockOTPSearchRepository.search(queryStringQuery("id:" + oTP.getId()))).thenReturn(Collections.singletonList(oTP));

        // Search the oTP
        restOTPMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + oTP.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oTP.getId().intValue())))
            .andExpect(jsonPath("$.[*].otp").value(hasItem(DEFAULT_OTP)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].expiryTime").value(hasItem(DEFAULT_EXPIRY_TIME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
