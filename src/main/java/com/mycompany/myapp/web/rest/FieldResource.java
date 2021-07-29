package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Field;
import com.mycompany.myapp.repository.FieldRepository;
import com.mycompany.myapp.repository.search.FieldSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Field}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FieldResource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    private static final String ENTITY_NAME = "field";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FieldRepository fieldRepository;

    private final FieldSearchRepository fieldSearchRepository;

    public FieldResource(FieldRepository fieldRepository, FieldSearchRepository fieldSearchRepository) {
        this.fieldRepository = fieldRepository;
        this.fieldSearchRepository = fieldSearchRepository;
    }

    /**
     * {@code POST  /fields} : Create a new field.
     *
     * @param field the field to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new field, or with status {@code 400 (Bad Request)} if the field has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fields")
    public ResponseEntity<Field> createField(@RequestBody Field field) throws URISyntaxException {
        log.debug("REST request to save Field : {}", field);
        if (field.getId() != null) {
            throw new BadRequestAlertException("A new field cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Field result = fieldRepository.save(field);
        fieldSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/fields/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fields/:id} : Updates an existing field.
     *
     * @param id the id of the field to save.
     * @param field the field to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated field,
     * or with status {@code 400 (Bad Request)} if the field is not valid,
     * or with status {@code 500 (Internal Server Error)} if the field couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fields/{id}")
    public ResponseEntity<Field> updateField(@PathVariable(value = "id", required = false) final Long id, @RequestBody Field field)
        throws URISyntaxException {
        log.debug("REST request to update Field : {}, {}", id, field);
        if (field.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, field.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Field result = fieldRepository.save(field);
        fieldSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, field.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fields/:id} : Partial updates given fields of an existing field, field will ignore if it is null
     *
     * @param id the id of the field to save.
     * @param field the field to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated field,
     * or with status {@code 400 (Bad Request)} if the field is not valid,
     * or with status {@code 404 (Not Found)} if the field is not found,
     * or with status {@code 500 (Internal Server Error)} if the field couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fields/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Field> partialUpdateField(@PathVariable(value = "id", required = false) final Long id, @RequestBody Field field)
        throws URISyntaxException {
        log.debug("REST request to partial update Field partially : {}, {}", id, field);
        if (field.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, field.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Field> result = fieldRepository
            .findById(field.getId())
            .map(
                existingField -> {
                    if (field.getFieldName() != null) {
                        existingField.setFieldName(field.getFieldName());
                    }
                    if (field.getFieldLabel() != null) {
                        existingField.setFieldLabel(field.getFieldLabel());
                    }
                    if (field.getFieldType() != null) {
                        existingField.setFieldType(field.getFieldType());
                    }
                    if (field.getIsDeleted() != null) {
                        existingField.setIsDeleted(field.getIsDeleted());
                    }
                    if (field.getCreatedBy() != null) {
                        existingField.setCreatedBy(field.getCreatedBy());
                    }
                    if (field.getCreatedAt() != null) {
                        existingField.setCreatedAt(field.getCreatedAt());
                    }
                    if (field.getUpdatedBy() != null) {
                        existingField.setUpdatedBy(field.getUpdatedBy());
                    }
                    if (field.getUpdatedAt() != null) {
                        existingField.setUpdatedAt(field.getUpdatedAt());
                    }

                    return existingField;
                }
            )
            .map(fieldRepository::save)
            .map(
                savedField -> {
                    fieldSearchRepository.save(savedField);

                    return savedField;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, field.getId().toString())
        );
    }

    /**
     * {@code GET  /fields} : get all the fields.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fields in body.
     */
    @GetMapping("/fields")
    public List<Field> getAllFields(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Fields");
        return fieldRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /fields/:id} : get the "id" field.
     *
     * @param id the id of the field to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the field, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fields/{id}")
    public ResponseEntity<Field> getField(@PathVariable Long id) {
        log.debug("REST request to get Field : {}", id);
        Optional<Field> field = fieldRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(field);
    }

    /**
     * {@code DELETE  /fields/:id} : delete the "id" field.
     *
     * @param id the id of the field to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fields/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        log.debug("REST request to delete Field : {}", id);
        fieldRepository.deleteById(id);
        fieldSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fields?query=:query} : search for the field corresponding
     * to the query.
     *
     * @param query the query of the field search.
     * @return the result of the search.
     */
    @GetMapping("/_search/fields")
    public List<Field> searchFields(@RequestParam String query) {
        log.debug("REST request to search Fields for query {}", query);
        return StreamSupport
            .stream(fieldSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
