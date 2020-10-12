package com.teach.predict.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.teach.predict.domain.Teacher;
import com.teach.predict.domain.*; // for static metamodels
import com.teach.predict.repository.TeacherRepository;
import com.teach.predict.service.dto.TeacherCriteria;
import com.teach.predict.service.dto.TeacherDTO;
import com.teach.predict.service.mapper.TeacherMapper;

/**
 * Service for executing complex queries for {@link Teacher} entities in the database.
 * The main input is a {@link TeacherCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TeacherDTO} or a {@link Page} of {@link TeacherDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TeacherQueryService extends QueryService<Teacher> {

    private final Logger log = LoggerFactory.getLogger(TeacherQueryService.class);

    private final TeacherRepository teacherRepository;

    private final TeacherMapper teacherMapper;

    public TeacherQueryService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    /**
     * Return a {@link List} of {@link TeacherDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TeacherDTO> findByCriteria(TeacherCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherMapper.toDto(teacherRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TeacherDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TeacherDTO> findByCriteria(TeacherCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.findAll(specification, page)
            .map(teacherMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TeacherCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Teacher> specification = createSpecification(criteria);
        return teacherRepository.count(specification);
    }

    /**
     * Function to convert {@link TeacherCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Teacher> createSpecification(TeacherCriteria criteria) {
        Specification<Teacher> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Teacher_.number));
            }
            if (criteria.getNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNumber(), Teacher_.number));
            }
            if (criteria.getSpecialization() != null) {
                specification = specification.and(buildSpecification(criteria.getSpecialization(), Teacher_.specialization));
            }
            if (criteria.getEvaluation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEvaluation(), Teacher_.evaluation));
            }
            if (criteria.getQualification() != null) {
                specification = specification.and(buildSpecification(criteria.getQualification(), Teacher_.qualification));
            }
            if (criteria.getStage() != null) {
                specification = specification.and(buildSpecification(criteria.getStage(), Teacher_.stage));
            }
            if (criteria.getSumOfHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSumOfHours(), Teacher_.sumOfHours));
            }
            if (criteria.getIsPredicted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsPredicted(), Teacher_.isPredicted));
            }
            if (criteria.getCoursesId() != null) {
                specification = specification.and(buildSpecification(criteria.getCoursesId(),
                    root -> root.join(Teacher_.courses, JoinType.LEFT).get(Course_.id)));
            }
        }
        return specification;
    }
}
