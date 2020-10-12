package com.teach.predict.service;

import com.teach.predict.service.dto.MLModelDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.teach.predict.domain.MLModel}.
 */
public interface MLModelService {

    /**
     * Save a mLModel.
     *
     * @param mLModelDTO the entity to save.
     * @return the persisted entity.
     */
    MLModelDTO save(MLModelDTO mLModelDTO);

    /**
     * Get all the mLModels.
     *
     * @return the list of entities.
     */
    List<MLModelDTO> findAll();


    /**
     * Get the "id" mLModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MLModelDTO> findOne(Long id);

    /**
     * Delete the "id" mLModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
