package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.JobSpecificField;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link JobSpecificField} entity.
 */
public interface JobSpecificFieldSearchRepository extends ElasticsearchRepository<JobSpecificField, Long> {}
