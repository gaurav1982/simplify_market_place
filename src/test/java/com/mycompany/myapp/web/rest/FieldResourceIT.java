package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Field;
import com.mycompany.myapp.domain.enumeration.FieldType;
import com.mycompany.myapp.repository.FieldRepository;
import com.mycompany.myapp.repository.search.FieldSearchRepository;
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
 * Integration tests for the {@link FieldResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FieldResourceIT {

    private static final String DEFAULT_FIELD_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_LABEL = "BBBBBBBBBB";

    private static final FieldType DEFAULT_FIELD_TYPE = FieldType.String;
    private static final FieldType UPDATED_FIELD_TYPE = FieldType.Date;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/fields";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FieldRepository fieldRepository;

    @Mock
    private FieldRepository fieldRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.FieldSearchRepositoryMockConfiguration
     */
    @Autowired
    private FieldSearchRepository mockFieldSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFieldMockMvc;

    private Field field;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createEntity(EntityManager em) {
        Field field = new Field()
            .fieldName(DEFAULT_FIELD_NAME)
            .fieldLabel(DEFAULT_FIELD_LABEL)
            .fieldType(DEFAULT_FIELD_TYPE)
            .isDeleted(DEFAULT_IS_DELETED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return field;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Field createUpdatedEntity(EntityManager em) {
        Field field = new Field()
            .fieldName(UPDATED_FIELD_NAME)
            .fieldLabel(UPDATED_FIELD_LABEL)
            .fieldType(UPDATED_FIELD_TYPE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return field;
    }

    @BeforeEach
    public void initTest() {
        field = createEntity(em);
    }

    @Test
    @Transactional
    void createField() throws Exception {
        int databaseSizeBeforeCreate = fieldRepository.findAll().size();
        // Create the Field
        restFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isCreated());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate + 1);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testField.getFieldLabel()).isEqualTo(DEFAULT_FIELD_LABEL);
        assertThat(testField.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testField.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testField.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testField.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testField.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testField.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).save(testField);
    }

    @Test
    @Transactional
    void createFieldWithExistingId() throws Exception {
        // Create the Field with an existing ID
        field.setId(1L);

        int databaseSizeBeforeCreate = fieldRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeCreate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void getAllFields() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get all the fieldList
        restFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldLabel").value(hasItem(DEFAULT_FIELD_LABEL)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFieldsWithEagerRelationshipsIsEnabled() throws Exception {
        when(fieldRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fieldRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFieldsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(fieldRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFieldMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(fieldRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        // Get the field
        restFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(field.getId().intValue()))
            .andExpect(jsonPath("$.fieldName").value(DEFAULT_FIELD_NAME))
            .andExpect(jsonPath("$.fieldLabel").value(DEFAULT_FIELD_LABEL))
            .andExpect(jsonPath("$.fieldType").value(DEFAULT_FIELD_TYPE.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingField() throws Exception {
        // Get the field
        restFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field
        Field updatedField = fieldRepository.findById(field.getId()).get();
        // Disconnect from session so that the updates on updatedField are not directly saved in db
        em.detach(updatedField);
        updatedField
            .fieldName(UPDATED_FIELD_NAME)
            .fieldLabel(UPDATED_FIELD_LABEL)
            .fieldType(UPDATED_FIELD_TYPE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedField.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testField.getFieldLabel()).isEqualTo(UPDATED_FIELD_LABEL);
        assertThat(testField.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testField.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testField.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testField.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testField.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testField.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository).save(testField);
    }

    @Test
    @Transactional
    void putNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, field.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void putWithIdMismatchField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void partialUpdateFieldWithPatch() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field using partial update
        Field partialUpdatedField = new Field();
        partialUpdatedField.setId(field.getId());

        partialUpdatedField.updatedBy(UPDATED_UPDATED_BY).updatedAt(UPDATED_UPDATED_AT);

        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getFieldName()).isEqualTo(DEFAULT_FIELD_NAME);
        assertThat(testField.getFieldLabel()).isEqualTo(DEFAULT_FIELD_LABEL);
        assertThat(testField.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
        assertThat(testField.getIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testField.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testField.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testField.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testField.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateFieldWithPatch() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();

        // Update the field using partial update
        Field partialUpdatedField = new Field();
        partialUpdatedField.setId(field.getId());

        partialUpdatedField
            .fieldName(UPDATED_FIELD_NAME)
            .fieldLabel(UPDATED_FIELD_LABEL)
            .fieldType(UPDATED_FIELD_TYPE)
            .isDeleted(UPDATED_IS_DELETED)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedField))
            )
            .andExpect(status().isOk());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);
        Field testField = fieldList.get(fieldList.size() - 1);
        assertThat(testField.getFieldName()).isEqualTo(UPDATED_FIELD_NAME);
        assertThat(testField.getFieldLabel()).isEqualTo(UPDATED_FIELD_LABEL);
        assertThat(testField.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
        assertThat(testField.getIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testField.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testField.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testField.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testField.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, field.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void patchWithIdMismatchField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(field))
            )
            .andExpect(status().isBadRequest());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamField() throws Exception {
        int databaseSizeBeforeUpdate = fieldRepository.findAll().size();
        field.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFieldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(field)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Field in the database
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(0)).save(field);
    }

    @Test
    @Transactional
    void deleteField() throws Exception {
        // Initialize the database
        fieldRepository.saveAndFlush(field);

        int databaseSizeBeforeDelete = fieldRepository.findAll().size();

        // Delete the field
        restFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, field.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Field> fieldList = fieldRepository.findAll();
        assertThat(fieldList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Field in Elasticsearch
        verify(mockFieldSearchRepository, times(1)).deleteById(field.getId());
    }

    @Test
    @Transactional
    void searchField() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        fieldRepository.saveAndFlush(field);
        when(mockFieldSearchRepository.search(queryStringQuery("id:" + field.getId()))).thenReturn(Collections.singletonList(field));

        // Search the field
        restFieldMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + field.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(field.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldName").value(hasItem(DEFAULT_FIELD_NAME)))
            .andExpect(jsonPath("$.[*].fieldLabel").value(hasItem(DEFAULT_FIELD_LABEL)))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
