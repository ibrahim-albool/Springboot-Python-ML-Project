package com.teach.predict.service;

import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.service.dto.CourseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.teach.predict.domain.Course}.
 */
public interface CourseService {

    /**
     * Save a course.
     *
     * @param courseDTO the entity to save.
     * @return the persisted entity.
     */
    CourseDTO save(CourseDTO courseDTO);

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CourseDTO> findAll(Pageable pageable);


    /**
     *
     * @param code
     * @param specialization
     * @return
     */
    Optional<CourseDTO> findOne(Long code, Specialization specialization);

    /**
     *
     * @param code
     * @param specialization
     */
    void delete(Long code, Specialization specialization);
}
