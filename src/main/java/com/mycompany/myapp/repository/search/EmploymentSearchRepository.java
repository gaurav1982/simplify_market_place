package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Employment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Employment} entity.
 */
public interface EmploymentSearchRepository extends ElasticsearchRepository<Employment, Long> {}
