package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Employment;
import com.mycompany.myapp.repository.EmploymentRepository;
import com.mycompany.myapp.repository.search.EmploymentSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Employment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EmploymentResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentResource.class);

    private static final String ENTITY_NAME = "employment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentRepository employmentRepository;

    private final EmploymentSearchRepository employmentSearchRepository;

    public EmploymentResource(EmploymentRepository employmentRepository, EmploymentSearchRepository employmentSearchRepository) {
        this.employmentRepository = employmentRepository;
        this.employmentSearchRepository = employmentSearchRepository;
    }

    /**
     * {@code POST  /employments} : Create a new employment.
     *
     * @param employment the employment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employment, or with status {@code 400 (Bad Request)} if the employment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employments")
    public ResponseEntity<Employment> createEmployment(@RequestBody Employment employment) throws URISyntaxException {
        log.debug("REST request to save Employment : {}", employment);
        if (employment.getId() != null) {
            throw new BadRequestAlertException("A new employment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Employment result = employmentRepository.save(employment);
        employmentSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/employments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employments/:id} : Updates an existing employment.
     *
     * @param id the id of the employment to save.
     * @param employment the employment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employment,
     * or with status {@code 400 (Bad Request)} if the employment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employments/{id}")
    public ResponseEntity<Employment> updateEmployment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Employment employment
    ) throws URISyntaxException {
        log.debug("REST request to update Employment : {}, {}", id, employment);
        if (employment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Employment result = employmentRepository.save(employment);
        employmentSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employments/:id} : Partial updates given fields of an existing employment, field will ignore if it is null
     *
     * @param id the id of the employment to save.
     * @param employment the employment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employment,
     * or with status {@code 400 (Bad Request)} if the employment is not valid,
     * or with status {@code 404 (Not Found)} if the employment is not found,
     * or with status {@code 500 (Internal Server Error)} if the employment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employments/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Employment> partialUpdateEmployment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Employment employment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employment partially : {}, {}", id, employment);
        if (employment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Employment> result = employmentRepository
            .findById(employment.getId())
            .map(
                existingEmployment -> {
                    if (employment.getJobTitle() != null) {
                        existingEmployment.setJobTitle(employment.getJobTitle());
                    }
                    if (employment.getCompanyName() != null) {
                        existingEmployment.setCompanyName(employment.getCompanyName());
                    }
                    if (employment.getStartDate() != null) {
                        existingEmployment.setStartDate(employment.getStartDate());
                    }
                    if (employment.getEndDate() != null) {
                        existingEmployment.setEndDate(employment.getEndDate());
                    }
                    if (employment.getLastSalary() != null) {
                        existingEmployment.setLastSalary(employment.getLastSalary());
                    }
                    if (employment.getDescription() != null) {
                        existingEmployment.setDescription(employment.getDescription());
                    }
                    if (employment.getCreatedBy() != null) {
                        existingEmployment.setCreatedBy(employment.getCreatedBy());
                    }
                    if (employment.getCreatedAt() != null) {
                        existingEmployment.setCreatedAt(employment.getCreatedAt());
                    }
                    if (employment.getUpdatedBy() != null) {
                        existingEmployment.setUpdatedBy(employment.getUpdatedBy());
                    }
                    if (employment.getUpdatedAt() != null) {
                        existingEmployment.setUpdatedAt(employment.getUpdatedAt());
                    }

                    return existingEmployment;
                }
            )
            .map(employmentRepository::save)
            .map(
                savedEmployment -> {
                    employmentSearchRepository.save(savedEmployment);

                    return savedEmployment;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employment.getId().toString())
        );
    }

    /**
     * {@code GET  /employments} : get all the employments.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employments in body.
     */
    @GetMapping("/employments")
    public List<Employment> getAllEmployments(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Employments");
        return employmentRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /employments/:id} : get the "id" employment.
     *
     * @param id the id of the employment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employments/{id}")
    public ResponseEntity<Employment> getEmployment(@PathVariable Long id) {
        log.debug("REST request to get Employment : {}", id);
        Optional<Employment> employment = employmentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(employment);
    }

    /**
     * {@code DELETE  /employments/:id} : delete the "id" employment.
     *
     * @param id the id of the employment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employments/{id}")
    public ResponseEntity<Void> deleteEmployment(@PathVariable Long id) {
        log.debug("REST request to delete Employment : {}", id);
        employmentRepository.deleteById(id);
        employmentSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/employments?query=:query} : search for the employment corresponding
     * to the query.
     *
     * @param query the query of the employment search.
     * @return the result of the search.
     */
    @GetMapping("/_search/employments")
    public List<Employment> searchEmployments(@RequestParam String query) {
        log.debug("REST request to search Employments for query {}", query);
        return StreamSupport
            .stream(employmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
