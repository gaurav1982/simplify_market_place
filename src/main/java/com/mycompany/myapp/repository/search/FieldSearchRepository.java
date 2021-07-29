package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Field;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Field} entity.
 */
public interface FieldSearchRepository extends ElasticsearchRepository<Field, Long> {}
