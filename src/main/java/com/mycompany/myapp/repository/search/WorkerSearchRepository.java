package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Worker;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Worker} entity.
 */
public interface WorkerSearchRepository extends ElasticsearchRepository<Worker, Long> {}
