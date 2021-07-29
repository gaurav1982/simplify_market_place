package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.LocationPrefrence;
import com.mycompany.myapp.repository.LocationPrefrenceRepository;
import com.mycompany.myapp.repository.search.LocationPrefrenceSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LocationPrefrence}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LocationPrefrenceResource {

    private final Logger log = LoggerFactory.getLogger(LocationPrefrenceResource.class);

    private static final String ENTITY_NAME = "locationPrefrence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocationPrefrenceRepository locationPrefrenceRepository;

    private final LocationPrefrenceSearchRepository locationPrefrenceSearchRepository;

    public LocationPrefrenceResource(
        LocationPrefrenceRepository locationPrefrenceRepository,
        LocationPrefrenceSearchRepository locationPrefrenceSearchRepository
    ) {
        this.locationPrefrenceRepository = locationPrefrenceRepository;
        this.locationPrefrenceSearchRepository = locationPrefrenceSearchRepository;
    }

    /**
     * {@code POST  /location-prefrences} : Create a new locationPrefrence.
     *
     * @param locationPrefrence the locationPrefrence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new locationPrefrence, or with status {@code 400 (Bad Request)} if the locationPrefrence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/location-prefrences")
    public ResponseEntity<LocationPrefrence> createLocationPrefrence(@RequestBody LocationPrefrence locationPrefrence)
        throws URISyntaxException {
        log.debug("REST request to save LocationPrefrence : {}", locationPrefrence);
        if (locationPrefrence.getId() != null) {
            throw new BadRequestAlertException("A new locationPrefrence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LocationPrefrence result = locationPrefrenceRepository.save(locationPrefrence);
        locationPrefrenceSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/location-prefrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /location-prefrences/:id} : Updates an existing locationPrefrence.
     *
     * @param id the id of the locationPrefrence to save.
     * @param locationPrefrence the locationPrefrence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationPrefrence,
     * or with status {@code 400 (Bad Request)} if the locationPrefrence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the locationPrefrence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/location-prefrences/{id}")
    public ResponseEntity<LocationPrefrence> updateLocationPrefrence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationPrefrence locationPrefrence
    ) throws URISyntaxException {
        log.debug("REST request to update LocationPrefrence : {}, {}", id, locationPrefrence);
        if (locationPrefrence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationPrefrence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationPrefrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LocationPrefrence result = locationPrefrenceRepository.save(locationPrefrence);
        locationPrefrenceSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationPrefrence.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /location-prefrences/:id} : Partial updates given fields of an existing locationPrefrence, field will ignore if it is null
     *
     * @param id the id of the locationPrefrence to save.
     * @param locationPrefrence the locationPrefrence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated locationPrefrence,
     * or with status {@code 400 (Bad Request)} if the locationPrefrence is not valid,
     * or with status {@code 404 (Not Found)} if the locationPrefrence is not found,
     * or with status {@code 500 (Internal Server Error)} if the locationPrefrence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/location-prefrences/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LocationPrefrence> partialUpdateLocationPrefrence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LocationPrefrence locationPrefrence
    ) throws URISyntaxException {
        log.debug("REST request to partial update LocationPrefrence partially : {}, {}", id, locationPrefrence);
        if (locationPrefrence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, locationPrefrence.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!locationPrefrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocationPrefrence> result = locationPrefrenceRepository
            .findById(locationPrefrence.getId())
            .map(
                existingLocationPrefrence -> {
                    if (locationPrefrence.getPrefrenceOrder() != null) {
                        existingLocationPrefrence.setPrefrenceOrder(locationPrefrence.getPrefrenceOrder());
                    }

                    return existingLocationPrefrence;
                }
            )
            .map(locationPrefrenceRepository::save)
            .map(
                savedLocationPrefrence -> {
                    locationPrefrenceSearchRepository.save(savedLocationPrefrence);

                    return savedLocationPrefrence;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, locationPrefrence.getId().toString())
        );
    }

    /**
     * {@code GET  /location-prefrences} : get all the locationPrefrences.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of locationPrefrences in body.
     */
    @GetMapping("/location-prefrences")
    public List<LocationPrefrence> getAllLocationPrefrences() {
        log.debug("REST request to get all LocationPrefrences");
        return locationPrefrenceRepository.findAll();
    }

    /**
     * {@code GET  /location-prefrences/:id} : get the "id" locationPrefrence.
     *
     * @param id the id of the locationPrefrence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the locationPrefrence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/location-prefrences/{id}")
    public ResponseEntity<LocationPrefrence> getLocationPrefrence(@PathVariable Long id) {
        log.debug("REST request to get LocationPrefrence : {}", id);
        Optional<LocationPrefrence> locationPrefrence = locationPrefrenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(locationPrefrence);
    }

    /**
     * {@code DELETE  /location-prefrences/:id} : delete the "id" locationPrefrence.
     *
     * @param id the id of the locationPrefrence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/location-prefrences/{id}")
    public ResponseEntity<Void> deleteLocationPrefrence(@PathVariable Long id) {
        log.debug("REST request to delete LocationPrefrence : {}", id);
        locationPrefrenceRepository.deleteById(id);
        locationPrefrenceSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/location-prefrences?query=:query} : search for the locationPrefrence corresponding
     * to the query.
     *
     * @param query the query of the locationPrefrence search.
     * @return the result of the search.
     */
    @GetMapping("/_search/location-prefrences")
    public List<LocationPrefrence> searchLocationPrefrences(@RequestParam String query) {
        log.debug("REST request to search LocationPrefrences for query {}", query);
        return StreamSupport
            .stream(locationPrefrenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
