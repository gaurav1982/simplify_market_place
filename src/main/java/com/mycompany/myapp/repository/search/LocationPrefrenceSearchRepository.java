package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.LocationPrefrence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link LocationPrefrence} entity.
 */
public interface LocationPrefrenceSearchRepository extends ElasticsearchRepository<LocationPrefrence, Long> {}
