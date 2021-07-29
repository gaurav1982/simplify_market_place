package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Resume;
import com.mycompany.myapp.domain.enumeration.FileType;
import com.mycompany.myapp.repository.ResumeRepository;
import com.mycompany.myapp.repository.search.ResumeSearchRepository;
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
 * Integration tests for the {@link ResumeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResumeResourceIT {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final FileType DEFAULT_FILETYPE = FileType.PDF;
    private static final FileType UPDATED_FILETYPE = FileType.DOC;

    private static final String DEFAULT_RESUME_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_RESUME_TITLE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/resumes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/resumes";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResumeRepository resumeRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ResumeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ResumeSearchRepository mockResumeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResumeMockMvc;

    private Resume resume;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createEntity(EntityManager em) {
        Resume resume = new Resume()
            .path(DEFAULT_PATH)
            .filetype(DEFAULT_FILETYPE)
            .resumeTitle(DEFAULT_RESUME_TITLE)
            .isDefault(DEFAULT_IS_DEFAULT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedAt(DEFAULT_UPDATED_AT);
        return resume;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Resume createUpdatedEntity(EntityManager em) {
        Resume resume = new Resume()
            .path(UPDATED_PATH)
            .filetype(UPDATED_FILETYPE)
            .resumeTitle(UPDATED_RESUME_TITLE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);
        return resume;
    }

    @BeforeEach
    public void initTest() {
        resume = createEntity(em);
    }

    @Test
    @Transactional
    void createResume() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();
        // Create the Resume
        restResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate + 1);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testResume.getFiletype()).isEqualTo(DEFAULT_FILETYPE);
        assertThat(testResume.getResumeTitle()).isEqualTo(DEFAULT_RESUME_TITLE);
        assertThat(testResume.getIsDefault()).isEqualTo(DEFAULT_IS_DEFAULT);
        assertThat(testResume.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testResume.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testResume.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testResume.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(1)).save(testResume);
    }

    @Test
    @Transactional
    void createResumeWithExistingId() throws Exception {
        // Create the Resume with an existing ID
        resume.setId(1L);

        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResumeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void getAllResumes() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList
        restResumeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].filetype").value(hasItem(DEFAULT_FILETYPE.toString())))
            .andExpect(jsonPath("$.[*].resumeTitle").value(hasItem(DEFAULT_RESUME_TITLE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get the resume
        restResumeMockMvc
            .perform(get(ENTITY_API_URL_ID, resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resume.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.filetype").value(DEFAULT_FILETYPE.toString()))
            .andExpect(jsonPath("$.resumeTitle").value(DEFAULT_RESUME_TITLE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResume() throws Exception {
        // Get the resume
        restResumeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume
        Resume updatedResume = resumeRepository.findById(resume.getId()).get();
        // Disconnect from session so that the updates on updatedResume are not directly saved in db
        em.detach(updatedResume);
        updatedResume
            .path(UPDATED_PATH)
            .filetype(UPDATED_FILETYPE)
            .resumeTitle(UPDATED_RESUME_TITLE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResume.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testResume.getFiletype()).isEqualTo(UPDATED_FILETYPE);
        assertThat(testResume.getResumeTitle()).isEqualTo(UPDATED_RESUME_TITLE);
        assertThat(testResume.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testResume.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testResume.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testResume.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testResume.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository).save(testResume);
    }

    @Test
    @Transactional
    void putNonExistingResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resume.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void putWithIdMismatchResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void partialUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume
            .path(UPDATED_PATH)
            .filetype(UPDATED_FILETYPE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdBy(UPDATED_CREATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResume.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testResume.getFiletype()).isEqualTo(UPDATED_FILETYPE);
        assertThat(testResume.getResumeTitle()).isEqualTo(DEFAULT_RESUME_TITLE);
        assertThat(testResume.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testResume.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testResume.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testResume.getUpdatedBy()).isEqualTo(DEFAULT_UPDATED_BY);
        assertThat(testResume.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateResumeWithPatch() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume using partial update
        Resume partialUpdatedResume = new Resume();
        partialUpdatedResume.setId(resume.getId());

        partialUpdatedResume
            .path(UPDATED_PATH)
            .filetype(UPDATED_FILETYPE)
            .resumeTitle(UPDATED_RESUME_TITLE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdBy(UPDATED_CREATED_BY)
            .createdAt(UPDATED_CREATED_AT)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedAt(UPDATED_UPDATED_AT);

        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResume.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResume))
            )
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testResume.getFiletype()).isEqualTo(UPDATED_FILETYPE);
        assertThat(testResume.getResumeTitle()).isEqualTo(UPDATED_RESUME_TITLE);
        assertThat(testResume.getIsDefault()).isEqualTo(UPDATED_IS_DEFAULT);
        assertThat(testResume.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testResume.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testResume.getUpdatedBy()).isEqualTo(UPDATED_UPDATED_BY);
        assertThat(testResume.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resume.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resume))
            )
            .andExpect(status().isBadRequest());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();
        resume.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResumeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(0)).save(resume);
    }

    @Test
    @Transactional
    void deleteResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        int databaseSizeBeforeDelete = resumeRepository.findAll().size();

        // Delete the resume
        restResumeMockMvc
            .perform(delete(ENTITY_API_URL_ID, resume.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Resume in Elasticsearch
        verify(mockResumeSearchRepository, times(1)).deleteById(resume.getId());
    }

    @Test
    @Transactional
    void searchResume() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        when(mockResumeSearchRepository.search(queryStringQuery("id:" + resume.getId()))).thenReturn(Collections.singletonList(resume));

        // Search the resume
        restResumeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].filetype").value(hasItem(DEFAULT_FILETYPE.toString())))
            .andExpect(jsonPath("$.[*].resumeTitle").value(hasItem(DEFAULT_RESUME_TITLE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}
