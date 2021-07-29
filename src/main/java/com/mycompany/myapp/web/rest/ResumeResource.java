package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Resume;
import com.mycompany.myapp.repository.ResumeRepository;
import com.mycompany.myapp.repository.search.ResumeSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Resume}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResumeResource {

    private final Logger log = LoggerFactory.getLogger(ResumeResource.class);

    private static final String ENTITY_NAME = "resume";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResumeRepository resumeRepository;

    private final ResumeSearchRepository resumeSearchRepository;

    public ResumeResource(ResumeRepository resumeRepository, ResumeSearchRepository resumeSearchRepository) {
        this.resumeRepository = resumeRepository;
        this.resumeSearchRepository = resumeSearchRepository;
    }

    /**
     * {@code POST  /resumes} : Create a new resume.
     *
     * @param resume the resume to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resume, or with status {@code 400 (Bad Request)} if the resume has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resumes")
    public ResponseEntity<Resume> createResume(@RequestBody Resume resume) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resume);
        if (resume.getId() != null) {
            throw new BadRequestAlertException("A new resume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Resume result = resumeRepository.save(resume);
        resumeSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resumes/:id} : Updates an existing resume.
     *
     * @param id the id of the resume to save.
     * @param resume the resume to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resume,
     * or with status {@code 400 (Bad Request)} if the resume is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resume couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resumes/{id}")
    public ResponseEntity<Resume> updateResume(@PathVariable(value = "id", required = false) final Long id, @RequestBody Resume resume)
        throws URISyntaxException {
        log.debug("REST request to update Resume : {}, {}", id, resume);
        if (resume.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resume.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Resume result = resumeRepository.save(resume);
        resumeSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resume.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resumes/:id} : Partial updates given fields of an existing resume, field will ignore if it is null
     *
     * @param id the id of the resume to save.
     * @param resume the resume to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resume,
     * or with status {@code 400 (Bad Request)} if the resume is not valid,
     * or with status {@code 404 (Not Found)} if the resume is not found,
     * or with status {@code 500 (Internal Server Error)} if the resume couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resumes/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Resume> partialUpdateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Resume resume
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resume partially : {}, {}", id, resume);
        if (resume.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resume.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Resume> result = resumeRepository
            .findById(resume.getId())
            .map(
                existingResume -> {
                    if (resume.getPath() != null) {
                        existingResume.setPath(resume.getPath());
                    }
                    if (resume.getFiletype() != null) {
                        existingResume.setFiletype(resume.getFiletype());
                    }
                    if (resume.getResumeTitle() != null) {
                        existingResume.setResumeTitle(resume.getResumeTitle());
                    }
                    if (resume.getIsDefault() != null) {
                        existingResume.setIsDefault(resume.getIsDefault());
                    }
                    if (resume.getCreatedBy() != null) {
                        existingResume.setCreatedBy(resume.getCreatedBy());
                    }
                    if (resume.getCreatedAt() != null) {
                        existingResume.setCreatedAt(resume.getCreatedAt());
                    }
                    if (resume.getUpdatedBy() != null) {
                        existingResume.setUpdatedBy(resume.getUpdatedBy());
                    }
                    if (resume.getUpdatedAt() != null) {
                        existingResume.setUpdatedAt(resume.getUpdatedAt());
                    }

                    return existingResume;
                }
            )
            .map(resumeRepository::save)
            .map(
                savedResume -> {
                    resumeSearchRepository.save(savedResume);

                    return savedResume;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resume.getId().toString())
        );
    }

    /**
     * {@code GET  /resumes} : get all the resumes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resumes in body.
     */
    @GetMapping("/resumes")
    public List<Resume> getAllResumes() {
        log.debug("REST request to get all Resumes");
        return resumeRepository.findAll();
    }

    /**
     * {@code GET  /resumes/:id} : get the "id" resume.
     *
     * @param id the id of the resume to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resume, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resumes/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        log.debug("REST request to get Resume : {}", id);
        Optional<Resume> resume = resumeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resume);
    }

    /**
     * {@code DELETE  /resumes/:id} : delete the "id" resume.
     *
     * @param id the id of the resume to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeRepository.deleteById(id);
        resumeSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/resumes?query=:query} : search for the resume corresponding
     * to the query.
     *
     * @param query the query of the resume search.
     * @return the result of the search.
     */
    @GetMapping("/_search/resumes")
    public List<Resume> searchResumes(@RequestParam String query) {
        log.debug("REST request to search Resumes for query {}", query);
        return StreamSupport
            .stream(resumeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
