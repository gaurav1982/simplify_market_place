package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Education;
import com.mycompany.myapp.repository.EducationRepository;
import com.mycompany.myapp.repository.search.EducationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Education}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EducationResource {

    private final Logger log = LoggerFactory.getLogger(EducationResource.class);

    private static final String ENTITY_NAME = "education";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EducationRepository educationRepository;

    private final EducationSearchRepository educationSearchRepository;

    public EducationResource(EducationRepository educationRepository, EducationSearchRepository educationSearchRepository) {
        this.educationRepository = educationRepository;
        this.educationSearchRepository = educationSearchRepository;
    }

    /**
     * {@code POST  /educations} : Create a new education.
     *
     * @param education the education to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new education, or with status {@code 400 (Bad Request)} if the education has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/educations")
    public ResponseEntity<Education> createEducation(@RequestBody Education education) throws URISyntaxException {
        log.debug("REST request to save Education : {}", education);
        if (education.getId() != null) {
            throw new BadRequestAlertException("A new education cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Education result = educationRepository.save(education);
        educationSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/educations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /educations/:id} : Updates an existing education.
     *
     * @param id the id of the education to save.
     * @param education the education to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated education,
     * or with status {@code 400 (Bad Request)} if the education is not valid,
     * or with status {@code 500 (Internal Server Error)} if the education couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/educations/{id}")
    public ResponseEntity<Education> updateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Education education
    ) throws URISyntaxException {
        log.debug("REST request to update Education : {}, {}", id, education);
        if (education.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, education.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Education result = educationRepository.save(education);
        educationSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, education.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /educations/:id} : Partial updates given fields of an existing education, field will ignore if it is null
     *
     * @param id the id of the education to save.
     * @param education the education to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated education,
     * or with status {@code 400 (Bad Request)} if the education is not valid,
     * or with status {@code 404 (Not Found)} if the education is not found,
     * or with status {@code 500 (Internal Server Error)} if the education couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/educations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Education> partialUpdateEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Education education
    ) throws URISyntaxException {
        log.debug("REST request to partial update Education partially : {}, {}", id, education);
        if (education.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, education.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!educationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Education> result = educationRepository
            .findById(education.getId())
            .map(
                existingEducation -> {
                    if (education.getDegreeName() != null) {
                        existingEducation.setDegreeName(education.getDegreeName());
                    }
                    if (education.getInstitute() != null) {
                        existingEducation.setInstitute(education.getInstitute());
                    }
                    if (education.getYearOfPassing() != null) {
                        existingEducation.setYearOfPassing(education.getYearOfPassing());
                    }
                    if (education.getMarks() != null) {
                        existingEducation.setMarks(education.getMarks());
                    }
                    if (education.getStartDate() != null) {
                        existingEducation.setStartDate(education.getStartDate());
                    }
                    if (education.getEndDate() != null) {
                        existingEducation.setEndDate(education.getEndDate());
                    }
                    if (education.getDegreeType() != null) {
                        existingEducation.setDegreeType(education.getDegreeType());
                    }
                    if (education.getDescription() != null) {
                        existingEducation.setDescription(education.getDescription());
                    }
                    if (education.getCreatedBy() != null) {
                        existingEducation.setCreatedBy(education.getCreatedBy());
                    }
                    if (education.getCreatedAt() != null) {
                        existingEducation.setCreatedAt(education.getCreatedAt());
                    }
                    if (education.getUpdatedBy() != null) {
                        existingEducation.setUpdatedBy(education.getUpdatedBy());
                    }
                    if (education.getUpdatedAt() != null) {
                        existingEducation.setUpdatedAt(education.getUpdatedAt());
                    }

                    return existingEducation;
                }
            )
            .map(educationRepository::save)
            .map(
                savedEducation -> {
                    educationSearchRepository.save(savedEducation);

                    return savedEducation;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, education.getId().toString())
        );
    }

    /**
     * {@code GET  /educations} : get all the educations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of educations in body.
     */
    @GetMapping("/educations")
    public List<Education> getAllEducations() {
        log.debug("REST request to get all Educations");
        return educationRepository.findAll();
    }

    /**
     * {@code GET  /educations/:id} : get the "id" education.
     *
     * @param id the id of the education to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the education, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/educations/{id}")
    public ResponseEntity<Education> getEducation(@PathVariable Long id) {
        log.debug("REST request to get Education : {}", id);
        Optional<Education> education = educationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(education);
    }

    /**
     * {@code DELETE  /educations/:id} : delete the "id" education.
     *
     * @param id the id of the education to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/educations/{id}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        log.debug("REST request to delete Education : {}", id);
        educationRepository.deleteById(id);
        educationSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/educations?query=:query} : search for the education corresponding
     * to the query.
     *
     * @param query the query of the education search.
     * @return the result of the search.
     */
    @GetMapping("/_search/educations")
    public List<Education> searchEducations(@RequestParam String query) {
        log.debug("REST request to search Educations for query {}", query);
        return StreamSupport
            .stream(educationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
