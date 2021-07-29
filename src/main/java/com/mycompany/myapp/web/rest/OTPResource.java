package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.OTP;
import com.mycompany.myapp.repository.OTPRepository;
import com.mycompany.myapp.repository.search.OTPSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.OTP}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OTPResource {

    private final Logger log = LoggerFactory.getLogger(OTPResource.class);

    private static final String ENTITY_NAME = "oTP";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OTPRepository oTPRepository;

    private final OTPSearchRepository oTPSearchRepository;

    public OTPResource(OTPRepository oTPRepository, OTPSearchRepository oTPSearchRepository) {
        this.oTPRepository = oTPRepository;
        this.oTPSearchRepository = oTPSearchRepository;
    }

    /**
     * {@code POST  /otps} : Create a new oTP.
     *
     * @param oTP the oTP to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oTP, or with status {@code 400 (Bad Request)} if the oTP has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/otps")
    public ResponseEntity<OTP> createOTP(@RequestBody OTP oTP) throws URISyntaxException {
        log.debug("REST request to save OTP : {}", oTP);
        if (oTP.getId() != null) {
            throw new BadRequestAlertException("A new oTP cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OTP result = oTPRepository.save(oTP);
        oTPSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/otps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /otps/:id} : Updates an existing oTP.
     *
     * @param id the id of the oTP to save.
     * @param oTP the oTP to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oTP,
     * or with status {@code 400 (Bad Request)} if the oTP is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oTP couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/otps/{id}")
    public ResponseEntity<OTP> updateOTP(@PathVariable(value = "id", required = false) final Long id, @RequestBody OTP oTP)
        throws URISyntaxException {
        log.debug("REST request to update OTP : {}, {}", id, oTP);
        if (oTP.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oTP.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oTPRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OTP result = oTPRepository.save(oTP);
        oTPSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oTP.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /otps/:id} : Partial updates given fields of an existing oTP, field will ignore if it is null
     *
     * @param id the id of the oTP to save.
     * @param oTP the oTP to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oTP,
     * or with status {@code 400 (Bad Request)} if the oTP is not valid,
     * or with status {@code 404 (Not Found)} if the oTP is not found,
     * or with status {@code 500 (Internal Server Error)} if the oTP couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/otps/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<OTP> partialUpdateOTP(@PathVariable(value = "id", required = false) final Long id, @RequestBody OTP oTP)
        throws URISyntaxException {
        log.debug("REST request to partial update OTP partially : {}, {}", id, oTP);
        if (oTP.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oTP.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oTPRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OTP> result = oTPRepository
            .findById(oTP.getId())
            .map(
                existingOTP -> {
                    if (oTP.getOtp() != null) {
                        existingOTP.setOtp(oTP.getOtp());
                    }
                    if (oTP.getEmail() != null) {
                        existingOTP.setEmail(oTP.getEmail());
                    }
                    if (oTP.getPhone() != null) {
                        existingOTP.setPhone(oTP.getPhone());
                    }
                    if (oTP.getType() != null) {
                        existingOTP.setType(oTP.getType());
                    }
                    if (oTP.getExpiryTime() != null) {
                        existingOTP.setExpiryTime(oTP.getExpiryTime());
                    }
                    if (oTP.getStatus() != null) {
                        existingOTP.setStatus(oTP.getStatus());
                    }
                    if (oTP.getCreatedBy() != null) {
                        existingOTP.setCreatedBy(oTP.getCreatedBy());
                    }
                    if (oTP.getCreatedAt() != null) {
                        existingOTP.setCreatedAt(oTP.getCreatedAt());
                    }
                    if (oTP.getUpdatedBy() != null) {
                        existingOTP.setUpdatedBy(oTP.getUpdatedBy());
                    }
                    if (oTP.getUpdatedAt() != null) {
                        existingOTP.setUpdatedAt(oTP.getUpdatedAt());
                    }

                    return existingOTP;
                }
            )
            .map(oTPRepository::save)
            .map(
                savedOTP -> {
                    oTPSearchRepository.save(savedOTP);

                    return savedOTP;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oTP.getId().toString())
        );
    }

    /**
     * {@code GET  /otps} : get all the oTPS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oTPS in body.
     */
    @GetMapping("/otps")
    public List<OTP> getAllOTPS() {
        log.debug("REST request to get all OTPS");
        return oTPRepository.findAll();
    }

    /**
     * {@code GET  /otps/:id} : get the "id" oTP.
     *
     * @param id the id of the oTP to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oTP, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/otps/{id}")
    public ResponseEntity<OTP> getOTP(@PathVariable Long id) {
        log.debug("REST request to get OTP : {}", id);
        Optional<OTP> oTP = oTPRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(oTP);
    }

    /**
     * {@code DELETE  /otps/:id} : delete the "id" oTP.
     *
     * @param id the id of the oTP to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/otps/{id}")
    public ResponseEntity<Void> deleteOTP(@PathVariable Long id) {
        log.debug("REST request to delete OTP : {}", id);
        oTPRepository.deleteById(id);
        oTPSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/otps?query=:query} : search for the oTP corresponding
     * to the query.
     *
     * @param query the query of the oTP search.
     * @return the result of the search.
     */
    @GetMapping("/_search/otps")
    public List<OTP> searchOTPS(@RequestParam String query) {
        log.debug("REST request to search OTPS for query {}", query);
        return StreamSupport.stream(oTPSearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
    }
}
