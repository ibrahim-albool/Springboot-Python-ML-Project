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

import com.teach.predict.domain.MLModel;
import com.teach.predict.domain.*; // for static metamodels
import com.teach.predict.repository.MLModelRepository;
import com.teach.predict.service.dto.MLModelCriteria;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.mapper.MLModelMapper;

/**
 * Service for executing complex queries for {@link MLModel} entities in the database.
 * The main input is a {@link MLModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MLModelDTO} or a {@link Page} of {@link MLModelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MLModelQueryService extends QueryService<MLModel> {

    private final Logger log = LoggerFactory.getLogger(MLModelQueryService.class);

    private final MLModelRepository mLModelRepository;

    private final MLModelMapper mLModelMapper;

    public MLModelQueryService(MLModelRepository mLModelRepository, MLModelMapper mLModelMapper) {
        this.mLModelRepository = mLModelRepository;
        this.mLModelMapper = mLModelMapper;
    }

    /**
     * Return a {@link List} of {@link MLModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MLModelDTO> findByCriteria(MLModelCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MLModel> specification = createSpecification(criteria);
        return mLModelMapper.toDto(mLModelRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MLModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MLModelDTO> findByCriteria(MLModelCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MLModel> specification = createSpecification(criteria);
        return mLModelRepository.findAll(specification, page)
            .map(mLModelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MLModelCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MLModel> specification = createSpecification(criteria);
        return mLModelRepository.count(specification);
    }

    /**
     * Function to convert {@link MLModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MLModel> createSpecification(MLModelCriteria criteria) {
        Specification<MLModel> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MLModel_.id));
            }
            if (criteria.getTp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTp(), MLModel_.tp));
            }
            if (criteria.getTn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTn(), MLModel_.tn));
            }
            if (criteria.getFp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFp(), MLModel_.fp));
            }
            if (criteria.getFn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFn(), MLModel_.fn));
            }
            if (criteria.getAccuracy() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAccuracy(), MLModel_.accuracy));
            }
            if (criteria.getPrecision() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecision(), MLModel_.precision));
            }
            if (criteria.getRecall() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRecall(), MLModel_.recall));
            }
        }
        return specification;
    }
}
