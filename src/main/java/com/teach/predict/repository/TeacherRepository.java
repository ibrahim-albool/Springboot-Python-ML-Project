package com.teach.predict.repository;

import com.teach.predict.domain.Teacher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Teacher entity.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>, JpaSpecificationExecutor<Teacher> {

    @Query(value = "select distinct teacher from Teacher teacher left join fetch teacher.courses",
        countQuery = "select count(distinct teacher) from Teacher teacher")
    Page<Teacher> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct teacher from Teacher teacher left join fetch teacher.courses")
    List<Teacher> findAllWithEagerRelationships();

    @Query("select teacher from Teacher teacher left join fetch teacher.courses where teacher.id =:id")
    Optional<Teacher> findOneWithEagerRelationships(@Param("id") Long id);
}
