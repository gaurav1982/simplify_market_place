package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.JobPreference;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link JobPreference} entity.
 */
public interface JobPreferenceSearchRepository extends ElasticsearchRepository<JobPreference, Long> {}
