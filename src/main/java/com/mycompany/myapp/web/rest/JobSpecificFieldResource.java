package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.JobSpecificField;
import com.mycompany.myapp.repository.JobSpecificFieldRepository;
import com.mycompany.myapp.repository.search.JobSpecificFieldSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.JobSpecificField}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JobSpecificFieldResource {

    private final Logger log = LoggerFactory.getLogger(JobSpecificFieldResource.class);

    private static final String ENTITY_NAME = "jobSpecificField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobSpecificFieldRepository jobSpecificFieldRepository;

    private final JobSpecificFieldSearchRepository jobSpecificFieldSearchRepository;

    public JobSpecificFieldResource(
        JobSpecificFieldRepository jobSpecificFieldRepository,
        JobSpecificFieldSearchRepository jobSpecificFieldSearchRepository
    ) {
        this.jobSpecificFieldRepository = jobSpecificFieldRepository;
        this.jobSpecificFieldSearchRepository = jobSpecificFieldSearchRepository;
    }

    /**
     * {@code POST  /job-specific-fields} : Create a new jobSpecificField.
     *
     * @param jobSpecificField the jobSpecificField to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobSpecificField, or with status {@code 400 (Bad Request)} if the jobSpecificField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-specific-fields")
    public ResponseEntity<JobSpecificField> createJobSpecificField(@RequestBody JobSpecificField jobSpecificField)
        throws URISyntaxException {
        log.debug("REST request to save JobSpecificField : {}", jobSpecificField);
        if (jobSpecificField.getId() != null) {
            throw new BadRequestAlertException("A new jobSpecificField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobSpecificField result = jobSpecificFieldRepository.save(jobSpecificField);
        jobSpecificFieldSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/job-specific-fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-specific-fields/:id} : Updates an existing jobSpecificField.
     *
     * @param id the id of the jobSpecificField to save.
     * @param jobSpecificField the jobSpecificField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobSpecificField,
     * or with status {@code 400 (Bad Request)} if the jobSpecificField is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobSpecificField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-specific-fields/{id}")
    public ResponseEntity<JobSpecificField> updateJobSpecificField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobSpecificField jobSpecificField
    ) throws URISyntaxException {
        log.debug("REST request to update JobSpecificField : {}, {}", id, jobSpecificField);
        if (jobSpecificField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobSpecificField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobSpecificFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobSpecificField result = jobSpecificFieldRepository.save(jobSpecificField);
        jobSpecificFieldSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobSpecificField.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-specific-fields/:id} : Partial updates given fields of an existing jobSpecificField, field will ignore if it is null
     *
     * @param id the id of the jobSpecificField to save.
     * @param jobSpecificField the jobSpecificField to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobSpecificField,
     * or with status {@code 400 (Bad Request)} if the jobSpecificField is not valid,
     * or with status {@code 404 (Not Found)} if the jobSpecificField is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobSpecificField couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-specific-fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<JobSpecificField> partialUpdateJobSpecificField(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobSpecificField jobSpecificField
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobSpecificField partially : {}, {}", id, jobSpecificField);
        if (jobSpecificField.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobSpecificField.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobSpecificFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobSpecificField> result = jobSpecificFieldRepository
            .findById(jobSpecificField.getId())
            .map(
                existingJobSpecificField -> {
                    if (jobSpecificField.getValue() != null) {
                        existingJobSpecificField.setValue(jobSpecificField.getValue());
                    }

                    return existingJobSpecificField;
                }
            )
            .map(jobSpecificFieldRepository::save)
            .map(
                savedJobSpecificField -> {
                    jobSpecificFieldSearchRepository.save(savedJobSpecificField);

                    return savedJobSpecificField;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobSpecificField.getId().toString())
        );
    }

    /**
     * {@code GET  /job-specific-fields} : get all the jobSpecificFields.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobSpecificFields in body.
     */
    @GetMapping("/job-specific-fields")
    public List<JobSpecificField> getAllJobSpecificFields() {
        log.debug("REST request to get all JobSpecificFields");
        return jobSpecificFieldRepository.findAll();
    }

    /**
     * {@code GET  /job-specific-fields/:id} : get the "id" jobSpecificField.
     *
     * @param id the id of the jobSpecificField to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobSpecificField, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-specific-fields/{id}")
    public ResponseEntity<JobSpecificField> getJobSpecificField(@PathVariable Long id) {
        log.debug("REST request to get JobSpecificField : {}", id);
        Optional<JobSpecificField> jobSpecificField = jobSpecificFieldRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobSpecificField);
    }

    /**
     * {@code DELETE  /job-specific-fields/:id} : delete the "id" jobSpecificField.
     *
     * @param id the id of the jobSpecificField to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-specific-fields/{id}")
    public ResponseEntity<Void> deleteJobSpecificField(@PathVariable Long id) {
        log.debug("REST request to delete JobSpecificField : {}", id);
        jobSpecificFieldRepository.deleteById(id);
        jobSpecificFieldSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/job-specific-fields?query=:query} : search for the jobSpecificField corresponding
     * to the query.
     *
     * @param query the query of the jobSpecificField search.
     * @return the result of the search.
     */
    @GetMapping("/_search/job-specific-fields")
    public List<JobSpecificField> searchJobSpecificFields(@RequestParam String query) {
        log.debug("REST request to search JobSpecificFields for query {}", query);
        return StreamSupport
            .stream(jobSpecificFieldSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
