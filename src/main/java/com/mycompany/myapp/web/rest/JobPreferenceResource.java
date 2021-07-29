package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.JobPreference;
import com.mycompany.myapp.repository.JobPreferenceRepository;
import com.mycompany.myapp.repository.search.JobPreferenceSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.JobPreference}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JobPreferenceResource {

    private final Logger log = LoggerFactory.getLogger(JobPreferenceResource.class);

    private static final String ENTITY_NAME = "jobPreference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobPreferenceRepository jobPreferenceRepository;

    private final JobPreferenceSearchRepository jobPreferenceSearchRepository;

    public JobPreferenceResource(
        JobPreferenceRepository jobPreferenceRepository,
        JobPreferenceSearchRepository jobPreferenceSearchRepository
    ) {
        this.jobPreferenceRepository = jobPreferenceRepository;
        this.jobPreferenceSearchRepository = jobPreferenceSearchRepository;
    }

    /**
     * {@code POST  /job-preferences} : Create a new jobPreference.
     *
     * @param jobPreference the jobPreference to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobPreference, or with status {@code 400 (Bad Request)} if the jobPreference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-preferences")
    public ResponseEntity<JobPreference> createJobPreference(@RequestBody JobPreference jobPreference) throws URISyntaxException {
        log.debug("REST request to save JobPreference : {}", jobPreference);
        if (jobPreference.getId() != null) {
            throw new BadRequestAlertException("A new jobPreference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobPreference result = jobPreferenceRepository.save(jobPreference);
        jobPreferenceSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/job-preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-preferences/:id} : Updates an existing jobPreference.
     *
     * @param id the id of the jobPreference to save.
     * @param jobPreference the jobPreference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobPreference,
     * or with status {@code 400 (Bad Request)} if the jobPreference is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobPreference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-preferences/{id}")
    public ResponseEntity<JobPreference> updateJobPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobPreference jobPreference
    ) throws URISyntaxException {
        log.debug("REST request to update JobPreference : {}, {}", id, jobPreference);
        if (jobPreference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobPreference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobPreference result = jobPreferenceRepository.save(jobPreference);
        jobPreferenceSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobPreference.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-preferences/:id} : Partial updates given fields of an existing jobPreference, field will ignore if it is null
     *
     * @param id the id of the jobPreference to save.
     * @param jobPreference the jobPreference to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobPreference,
     * or with status {@code 400 (Bad Request)} if the jobPreference is not valid,
     * or with status {@code 404 (Not Found)} if the jobPreference is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobPreference couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-preferences/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<JobPreference> partialUpdateJobPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JobPreference jobPreference
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobPreference partially : {}, {}", id, jobPreference);
        if (jobPreference.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobPreference.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobPreference> result = jobPreferenceRepository
            .findById(jobPreference.getId())
            .map(
                existingJobPreference -> {
                    if (jobPreference.getHourlyRate() != null) {
                        existingJobPreference.setHourlyRate(jobPreference.getHourlyRate());
                    }
                    if (jobPreference.getDailyRate() != null) {
                        existingJobPreference.setDailyRate(jobPreference.getDailyRate());
                    }
                    if (jobPreference.getMonthlyRate() != null) {
                        existingJobPreference.setMonthlyRate(jobPreference.getMonthlyRate());
                    }
                    if (jobPreference.getHourPerDay() != null) {
                        existingJobPreference.setHourPerDay(jobPreference.getHourPerDay());
                    }
                    if (jobPreference.getHourPerWeek() != null) {
                        existingJobPreference.setHourPerWeek(jobPreference.getHourPerWeek());
                    }
                    if (jobPreference.getEngagementType() != null) {
                        existingJobPreference.setEngagementType(jobPreference.getEngagementType());
                    }
                    if (jobPreference.getLocationType() != null) {
                        existingJobPreference.setLocationType(jobPreference.getLocationType());
                    }
                    if (jobPreference.getCreatedBy() != null) {
                        existingJobPreference.setCreatedBy(jobPreference.getCreatedBy());
                    }
                    if (jobPreference.getCreatedAt() != null) {
                        existingJobPreference.setCreatedAt(jobPreference.getCreatedAt());
                    }
                    if (jobPreference.getUpdatedBy() != null) {
                        existingJobPreference.setUpdatedBy(jobPreference.getUpdatedBy());
                    }
                    if (jobPreference.getUpdatedAt() != null) {
                        existingJobPreference.setUpdatedAt(jobPreference.getUpdatedAt());
                    }

                    return existingJobPreference;
                }
            )
            .map(jobPreferenceRepository::save)
            .map(
                savedJobPreference -> {
                    jobPreferenceSearchRepository.save(savedJobPreference);

                    return savedJobPreference;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jobPreference.getId().toString())
        );
    }

    /**
     * {@code GET  /job-preferences} : get all the jobPreferences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobPreferences in body.
     */
    @GetMapping("/job-preferences")
    public List<JobPreference> getAllJobPreferences() {
        log.debug("REST request to get all JobPreferences");
        return jobPreferenceRepository.findAll();
    }

    /**
     * {@code GET  /job-preferences/:id} : get the "id" jobPreference.
     *
     * @param id the id of the jobPreference to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobPreference, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-preferences/{id}")
    public ResponseEntity<JobPreference> getJobPreference(@PathVariable Long id) {
        log.debug("REST request to get JobPreference : {}", id);
        Optional<JobPreference> jobPreference = jobPreferenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jobPreference);
    }

    /**
     * {@code DELETE  /job-preferences/:id} : delete the "id" jobPreference.
     *
     * @param id the id of the jobPreference to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-preferences/{id}")
    public ResponseEntity<Void> deleteJobPreference(@PathVariable Long id) {
        log.debug("REST request to delete JobPreference : {}", id);
        jobPreferenceRepository.deleteById(id);
        jobPreferenceSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/job-preferences?query=:query} : search for the jobPreference corresponding
     * to the query.
     *
     * @param query the query of the jobPreference search.
     * @return the result of the search.
     */
    @GetMapping("/_search/job-preferences")
    public List<JobPreference> searchJobPreferences(@RequestParam String query) {
        log.debug("REST request to search JobPreferences for query {}", query);
        return StreamSupport
            .stream(jobPreferenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
