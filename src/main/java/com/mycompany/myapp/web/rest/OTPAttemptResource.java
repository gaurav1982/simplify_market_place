package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.OTPAttempt;
import com.mycompany.myapp.repository.OTPAttemptRepository;
import com.mycompany.myapp.repository.search.OTPAttemptSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.OTPAttempt}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OTPAttemptResource {

    private final Logger log = LoggerFactory.getLogger(OTPAttemptResource.class);

    private static final String ENTITY_NAME = "oTPAttempt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OTPAttemptRepository oTPAttemptRepository;

    private final OTPAttemptSearchRepository oTPAttemptSearchRepository;

    public OTPAttemptResource(OTPAttemptRepository oTPAttemptRepository, OTPAttemptSearchRepository oTPAttemptSearchRepository) {
        this.oTPAttemptRepository = oTPAttemptRepository;
        this.oTPAttemptSearchRepository = oTPAttemptSearchRepository;
    }

    /**
     * {@code POST  /otp-attempts} : Create a new oTPAttempt.
     *
     * @param oTPAttempt the oTPAttempt to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oTPAttempt, or with status {@code 400 (Bad Request)} if the oTPAttempt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/otp-attempts")
    public ResponseEntity<OTPAttempt> createOTPAttempt(@RequestBody OTPAttempt oTPAttempt) throws URISyntaxException {
        log.debug("REST request to save OTPAttempt : {}", oTPAttempt);
        if (oTPAttempt.getId() != null) {
            throw new BadRequestAlertException("A new oTPAttempt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OTPAttempt result = oTPAttemptRepository.save(oTPAttempt);
        oTPAttemptSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/otp-attempts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /otp-attempts/:id} : Updates an existing oTPAttempt.
     *
     * @param id the id of the oTPAttempt to save.
     * @param oTPAttempt the oTPAttempt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oTPAttempt,
     * or with status {@code 400 (Bad Request)} if the oTPAttempt is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oTPAttempt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/otp-attempts/{id}")
    public ResponseEntity<OTPAttempt> updateOTPAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OTPAttempt oTPAttempt
    ) throws URISyntaxException {
        log.debug("REST request to update OTPAttempt : {}, {}", id, oTPAttempt);
        if (oTPAttempt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oTPAttempt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oTPAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OTPAttempt result = oTPAttemptRepository.save(oTPAttempt);
        oTPAttemptSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oTPAttempt.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /otp-attempts/:id} : Partial updates given fields of an existing oTPAttempt, field will ignore if it is null
     *
     * @param id the id of the oTPAttempt to save.
     * @param oTPAttempt the oTPAttempt to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oTPAttempt,
     * or with status {@code 400 (Bad Request)} if the oTPAttempt is not valid,
     * or with status {@code 404 (Not Found)} if the oTPAttempt is not found,
     * or with status {@code 500 (Internal Server Error)} if the oTPAttempt couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/otp-attempts/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OTPAttempt> partialUpdateOTPAttempt(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OTPAttempt oTPAttempt
    ) throws URISyntaxException {
        log.debug("REST request to partial update OTPAttempt partially : {}, {}", id, oTPAttempt);
        if (oTPAttempt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oTPAttempt.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oTPAttemptRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OTPAttempt> result = oTPAttemptRepository
            .findById(oTPAttempt.getId())
            .map(
                existingOTPAttempt -> {
                    if (oTPAttempt.getOtp() != null) {
                        existingOTPAttempt.setOtp(oTPAttempt.getOtp());
                    }
                    if (oTPAttempt.getEmail() != null) {
                        existingOTPAttempt.setEmail(oTPAttempt.getEmail());
                    }
                    if (oTPAttempt.getPhone() != null) {
                        existingOTPAttempt.setPhone(oTPAttempt.getPhone());
                    }
                    if (oTPAttempt.getIp() != null) {
                        existingOTPAttempt.setIp(oTPAttempt.getIp());
                    }
                    if (oTPAttempt.getCoookie() != null) {
                        existingOTPAttempt.setCoookie(oTPAttempt.getCoookie());
                    }
                    if (oTPAttempt.getCreatedBy() != null) {
                        existingOTPAttempt.setCreatedBy(oTPAttempt.getCreatedBy());
                    }
                    if (oTPAttempt.getCreatedAt() != null) {
                        existingOTPAttempt.setCreatedAt(oTPAttempt.getCreatedAt());
                    }

                    return existingOTPAttempt;
                }
            )
            .map(oTPAttemptRepository::save)
            .map(
                savedOTPAttempt -> {
                    oTPAttemptSearchRepository.save(savedOTPAttempt);

                    return savedOTPAttempt;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oTPAttempt.getId().toString())
        );
    }

    /**
     * {@code GET  /otp-attempts} : get all the oTPAttempts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oTPAttempts in body.
     */
    @GetMapping("/otp-attempts")
    public List<OTPAttempt> getAllOTPAttempts() {
        log.debug("REST request to get all OTPAttempts");
        return oTPAttemptRepository.findAll();
    }

    /**
     * {@code GET  /otp-attempts/:id} : get the "id" oTPAttempt.
     *
     * @param id the id of the oTPAttempt to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oTPAttempt, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/otp-attempts/{id}")
    public ResponseEntity<OTPAttempt> getOTPAttempt(@PathVariable Long id) {
        log.debug("REST request to get OTPAttempt : {}", id);
        Optional<OTPAttempt> oTPAttempt = oTPAttemptRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(oTPAttempt);
    }

    /**
     * {@code DELETE  /otp-attempts/:id} : delete the "id" oTPAttempt.
     *
     * @param id the id of the oTPAttempt to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/otp-attempts/{id}")
    public ResponseEntity<Void> deleteOTPAttempt(@PathVariable Long id) {
        log.debug("REST request to delete OTPAttempt : {}", id);
        oTPAttemptRepository.deleteById(id);
        oTPAttemptSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/otp-attempts?query=:query} : search for the oTPAttempt corresponding
     * to the query.
     *
     * @param query the query of the oTPAttempt search.
     * @return the result of the search.
     */
    @GetMapping("/_search/otp-attempts")
    public List<OTPAttempt> searchOTPAttempts(@RequestParam String query) {
        log.debug("REST request to search OTPAttempts for query {}", query);
        return StreamSupport
            .stream(oTPAttemptSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
