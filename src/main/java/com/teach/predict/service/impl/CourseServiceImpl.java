package com.teach.predict.service.impl;

import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.service.CourseService;
import com.teach.predict.domain.Course;
import com.teach.predict.repository.CourseRepository;
import com.teach.predict.service.dto.CourseDTO;
import com.teach.predict.service.mapper.CourseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Course}.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final Logger log = LoggerFactory.getLogger(CourseServiceImpl.class);

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Override
    public CourseDTO save(CourseDTO courseDTO) {
        log.debug("Request to save Course : {}", courseDTO);
        Course course = courseMapper.toEntity(courseDTO);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Courses");
        return courseRepository.findAll(pageable)
            .map(courseMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CourseDTO> findOne(Long code, Specialization specialization) {
        log.debug("Request to get Course : {}", code);
        return courseRepository.findByCodeAndSpecialization(code,specialization)
            .map(courseMapper::toDto);
    }

    @Override
    public void delete(Long code, Specialization specialization) {
        log.debug("Request to delete Course : {}", code);
        courseRepository.deleteByCodeAndSpecialization(code, specialization);
    }
}
