package com.teach.predict.repository;

import com.teach.predict.domain.MLModel;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the MLModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MLModelRepository extends JpaRepository<MLModel, Long>, JpaSpecificationExecutor<MLModel> {
}
