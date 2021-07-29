package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.LocationPrefrence;
import com.mycompany.myapp.repository.LocationPrefrenceRepository;
import com.mycompany.myapp.repository.search.LocationPrefrenceSearchRepository;
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
 * Integration tests for the {@link LocationPrefrenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocationPrefrenceResourceIT {

    private static final Integer DEFAULT_PREFRENCE_ORDER = 1;
    private static final Integer UPDATED_PREFRENCE_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/location-prefrences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/location-prefrences";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocationPrefrenceRepository locationPrefrenceRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.LocationPrefrenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private LocationPrefrenceSearchRepository mockLocationPrefrenceSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocationPrefrenceMockMvc;

    private LocationPrefrence locationPrefrence;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationPrefrence createEntity(EntityManager em) {
        LocationPrefrence locationPrefrence = new LocationPrefrence().prefrenceOrder(DEFAULT_PREFRENCE_ORDER);
        return locationPrefrence;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocationPrefrence createUpdatedEntity(EntityManager em) {
        LocationPrefrence locationPrefrence = new LocationPrefrence().prefrenceOrder(UPDATED_PREFRENCE_ORDER);
        return locationPrefrence;
    }

    @BeforeEach
    public void initTest() {
        locationPrefrence = createEntity(em);
    }

    @Test
    @Transactional
    void createLocationPrefrence() throws Exception {
        int databaseSizeBeforeCreate = locationPrefrenceRepository.findAll().size();
        // Create the LocationPrefrence
        restLocationPrefrenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isCreated());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeCreate + 1);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(DEFAULT_PREFRENCE_ORDER);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(1)).save(testLocationPrefrence);
    }

    @Test
    @Transactional
    void createLocationPrefrenceWithExistingId() throws Exception {
        // Create the LocationPrefrence with an existing ID
        locationPrefrence.setId(1L);

        int databaseSizeBeforeCreate = locationPrefrenceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocationPrefrenceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void getAllLocationPrefrences() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        // Get all the locationPrefrenceList
        restLocationPrefrenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationPrefrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefrenceOrder").value(hasItem(DEFAULT_PREFRENCE_ORDER)));
    }

    @Test
    @Transactional
    void getLocationPrefrence() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        // Get the locationPrefrence
        restLocationPrefrenceMockMvc
            .perform(get(ENTITY_API_URL_ID, locationPrefrence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(locationPrefrence.getId().intValue()))
            .andExpect(jsonPath("$.prefrenceOrder").value(DEFAULT_PREFRENCE_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingLocationPrefrence() throws Exception {
        // Get the locationPrefrence
        restLocationPrefrenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocationPrefrence() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();

        // Update the locationPrefrence
        LocationPrefrence updatedLocationPrefrence = locationPrefrenceRepository.findById(locationPrefrence.getId()).get();
        // Disconnect from session so that the updates on updatedLocationPrefrence are not directly saved in db
        em.detach(updatedLocationPrefrence);
        updatedLocationPrefrence.prefrenceOrder(UPDATED_PREFRENCE_ORDER);

        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocationPrefrence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocationPrefrence))
            )
            .andExpect(status().isOk());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(UPDATED_PREFRENCE_ORDER);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository).save(testLocationPrefrence);
    }

    @Test
    @Transactional
    void putNonExistingLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, locationPrefrence.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void partialUpdateLocationPrefrenceWithPatch() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();

        // Update the locationPrefrence using partial update
        LocationPrefrence partialUpdatedLocationPrefrence = new LocationPrefrence();
        partialUpdatedLocationPrefrence.setId(locationPrefrence.getId());

        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationPrefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationPrefrence))
            )
            .andExpect(status().isOk());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(DEFAULT_PREFRENCE_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateLocationPrefrenceWithPatch() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();

        // Update the locationPrefrence using partial update
        LocationPrefrence partialUpdatedLocationPrefrence = new LocationPrefrence();
        partialUpdatedLocationPrefrence.setId(locationPrefrence.getId());

        partialUpdatedLocationPrefrence.prefrenceOrder(UPDATED_PREFRENCE_ORDER);

        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocationPrefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocationPrefrence))
            )
            .andExpect(status().isOk());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);
        LocationPrefrence testLocationPrefrence = locationPrefrenceList.get(locationPrefrenceList.size() - 1);
        assertThat(testLocationPrefrence.getPrefrenceOrder()).isEqualTo(UPDATED_PREFRENCE_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, locationPrefrence.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocationPrefrence() throws Exception {
        int databaseSizeBeforeUpdate = locationPrefrenceRepository.findAll().size();
        locationPrefrence.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocationPrefrenceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(locationPrefrence))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocationPrefrence in the database
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(0)).save(locationPrefrence);
    }

    @Test
    @Transactional
    void deleteLocationPrefrence() throws Exception {
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);

        int databaseSizeBeforeDelete = locationPrefrenceRepository.findAll().size();

        // Delete the locationPrefrence
        restLocationPrefrenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, locationPrefrence.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LocationPrefrence> locationPrefrenceList = locationPrefrenceRepository.findAll();
        assertThat(locationPrefrenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LocationPrefrence in Elasticsearch
        verify(mockLocationPrefrenceSearchRepository, times(1)).deleteById(locationPrefrence.getId());
    }

    @Test
    @Transactional
    void searchLocationPrefrence() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        locationPrefrenceRepository.saveAndFlush(locationPrefrence);
        when(mockLocationPrefrenceSearchRepository.search(queryStringQuery("id:" + locationPrefrence.getId())))
            .thenReturn(Collections.singletonList(locationPrefrence));

        // Search the locationPrefrence
        restLocationPrefrenceMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + locationPrefrence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(locationPrefrence.getId().intValue())))
            .andExpect(jsonPath("$.[*].prefrenceOrder").value(hasItem(DEFAULT_PREFRENCE_ORDER)));
    }
}
