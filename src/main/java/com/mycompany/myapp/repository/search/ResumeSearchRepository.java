package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Resume;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Resume} entity.
 */
public interface ResumeSearchRepository extends ElasticsearchRepository<Resume, Long> {}
