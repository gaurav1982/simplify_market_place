package com.mycompany.myapp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.mycompany.myapp.domain.Worker;
import com.mycompany.myapp.repository.WorkerRepository;
import com.mycompany.myapp.repository.search.WorkerSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Worker}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkerResource {

    private final Logger log = LoggerFactory.getLogger(WorkerResource.class);

    private static final String ENTITY_NAME = "worker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkerRepository workerRepository;

    private final WorkerSearchRepository workerSearchRepository;

    public WorkerResource(WorkerRepository workerRepository, WorkerSearchRepository workerSearchRepository) {
        this.workerRepository = workerRepository;
        this.workerSearchRepository = workerSearchRepository;
    }

    /**
     * {@code POST  /workers} : Create a new worker.
     *
     * @param worker the worker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new worker, or with status {@code 400 (Bad Request)} if the worker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/workers")
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) throws URISyntaxException {
        log.debug("REST request to save Worker : {}", worker);
        if (worker.getId() != null) {
            throw new BadRequestAlertException("A new worker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Worker result = workerRepository.save(worker);
        workerSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/workers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /workers/:id} : Updates an existing worker.
     *
     * @param id the id of the worker to save.
     * @param worker the worker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worker,
     * or with status {@code 400 (Bad Request)} if the worker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the worker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/workers/{id}")
    public ResponseEntity<Worker> updateWorker(@PathVariable(value = "id", required = false) final Long id, @RequestBody Worker worker)
        throws URISyntaxException {
        log.debug("REST request to update Worker : {}, {}", id, worker);
        if (worker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, worker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Worker result = workerRepository.save(worker);
        workerSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, worker.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /workers/:id} : Partial updates given fields of an existing worker, field will ignore if it is null
     *
     * @param id the id of the worker to save.
     * @param worker the worker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated worker,
     * or with status {@code 400 (Bad Request)} if the worker is not valid,
     * or with status {@code 404 (Not Found)} if the worker is not found,
     * or with status {@code 500 (Internal Server Error)} if the worker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/workers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Worker> partialUpdateWorker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Worker worker
    ) throws URISyntaxException {
        log.debug("REST request to partial update Worker partially : {}, {}", id, worker);
        if (worker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, worker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Worker> result = workerRepository
            .findById(worker.getId())
            .map(
                existingWorker -> {
                    if (worker.getName() != null) {
                        existingWorker.setName(worker.getName());
                    }
                    if (worker.getEmail() != null) {
                        existingWorker.setEmail(worker.getEmail());
                    }
                    if (worker.getPhone() != null) {
                        existingWorker.setPhone(worker.getPhone());
                    }
                    if (worker.getDescription() != null) {
                        existingWorker.setDescription(worker.getDescription());
                    }
                    if (worker.getCreatedBy() != null) {
                        existingWorker.setCreatedBy(worker.getCreatedBy());
                    }
                    if (worker.getCreatedAt() != null) {
                        existingWorker.setCreatedAt(worker.getCreatedAt());
                    }
                    if (worker.getUpdatedBy() != null) {
                        existingWorker.setUpdatedBy(worker.getUpdatedBy());
                    }
                    if (worker.getUpdatedAt() != null) {
                        existingWorker.setUpdatedAt(worker.getUpdatedAt());
                    }

                    return existingWorker;
                }
            )
            .map(workerRepository::save)
            .map(
                savedWorker -> {
                    workerSearchRepository.save(savedWorker);

                    return savedWorker;
                }
            );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, worker.getId().toString())
        );
    }

    /**
     * {@code GET  /workers} : get all the workers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workers in body.
     */
    @GetMapping("/workers")
    public List<Worker> getAllWorkers() {
        log.debug("REST request to get all Workers");
        return workerRepository.findAll();
    }

    /**
     * {@code GET  /workers/:id} : get the "id" worker.
     *
     * @param id the id of the worker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the worker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/workers/{id}")
    public ResponseEntity<Worker> getWorker(@PathVariable Long id) {
        log.debug("REST request to get Worker : {}", id);
        Optional<Worker> worker = workerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(worker);
    }

    /**
     * {@code DELETE  /workers/:id} : delete the "id" worker.
     *
     * @param id the id of the worker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/workers/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        log.debug("REST request to delete Worker : {}", id);
        workerRepository.deleteById(id);
        workerSearchRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/workers?query=:query} : search for the worker corresponding
     * to the query.
     *
     * @param query the query of the worker search.
     * @return the result of the search.
     */
    @GetMapping("/_search/workers")
    public List<Worker> searchWorkers(@RequestParam String query) {
        log.debug("REST request to search Workers for query {}", query);
        return StreamSupport
            .stream(workerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
