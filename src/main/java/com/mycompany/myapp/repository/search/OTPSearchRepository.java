package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.OTP;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link OTP} entity.
 */
public interface OTPSearchRepository extends ElasticsearchRepository<OTP, Long> {}
