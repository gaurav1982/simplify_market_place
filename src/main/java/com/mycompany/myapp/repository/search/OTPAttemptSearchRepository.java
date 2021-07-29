package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.OTPAttempt;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link OTPAttempt} entity.
 */
public interface OTPAttemptSearchRepository extends ElasticsearchRepository<OTPAttempt, Long> {}
