package com.teach.predict.service.impl;

import com.teach.predict.service.MLModelService;
import com.teach.predict.domain.MLModel;
import com.teach.predict.repository.MLModelRepository;
import com.teach.predict.service.dto.MLModelDTO;
import com.teach.predict.service.mapper.MLModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MLModel}.
 */
@Service
@Transactional
public class MLModelServiceImpl implements MLModelService {

    private final Logger log = LoggerFactory.getLogger(MLModelServiceImpl.class);

    private final MLModelRepository mLModelRepository;

    private final MLModelMapper mLModelMapper;

    public MLModelServiceImpl(MLModelRepository mLModelRepository, MLModelMapper mLModelMapper) {
        this.mLModelRepository = mLModelRepository;
        this.mLModelMapper = mLModelMapper;
    }

    @Override
    public MLModelDTO save(MLModelDTO mLModelDTO) {
        log.debug("Request to save MLModel : {}", mLModelDTO);
        MLModel mLModel = mLModelMapper.toEntity(mLModelDTO);
        mLModel = mLModelRepository.save(mLModel);
        return mLModelMapper.toDto(mLModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MLModelDTO> findAll() {
        log.debug("Request to get all MLModels");
        return mLModelRepository.findAll().stream()
            .map(mLModelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MLModelDTO> findOne(Long id) {
        log.debug("Request to get MLModel : {}", id);
        return mLModelRepository.findById(id)
            .map(mLModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MLModel : {}", id);
        mLModelRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        mLModelRepository.deleteAllInBatch();
    }

    @Override
    public long count() {
        return mLModelRepository.count();
    }
}
