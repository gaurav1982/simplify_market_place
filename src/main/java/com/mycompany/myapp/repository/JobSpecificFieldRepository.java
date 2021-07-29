package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.JobSpecificField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the JobSpecificField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobSpecificFieldRepository extends JpaRepository<JobSpecificField, Long> {}
