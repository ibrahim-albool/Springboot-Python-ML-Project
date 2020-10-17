package com.teach.predict.repository;

import com.teach.predict.domain.Course;

import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.web.rest.AuditResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.DoubleStream;

/**
 * Spring Data  repository for the Course entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    Optional<Course> findByCode(Long code);

    Optional<Course> findByCodeAndSpecialization(Long id, Specialization specialization);

    void deleteByCodeAndSpecialization(Long code, Specialization specialization);
}
