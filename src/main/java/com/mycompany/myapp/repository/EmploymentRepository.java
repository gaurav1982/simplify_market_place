package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Employment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Employment entity.
 */
@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    @Query(
        value = "select distinct employment from Employment employment left join fetch employment.locations",
        countQuery = "select count(distinct employment) from Employment employment"
    )
    Page<Employment> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct employment from Employment employment left join fetch employment.locations")
    List<Employment> findAllWithEagerRelationships();

    @Query("select employment from Employment employment left join fetch employment.locations where employment.id =:id")
    Optional<Employment> findOneWithEagerRelationships(@Param("id") Long id);
}
