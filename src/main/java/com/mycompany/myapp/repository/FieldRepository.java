package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Field;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Field entity.
 */
@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {
    @Query(
        value = "select distinct field from Field field left join fetch field.categories",
        countQuery = "select count(distinct field) from Field field"
    )
    Page<Field> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct field from Field field left join fetch field.categories")
    List<Field> findAllWithEagerRelationships();

    @Query("select field from Field field left join fetch field.categories where field.id =:id")
    Optional<Field> findOneWithEagerRelationships(@Param("id") Long id);
}
