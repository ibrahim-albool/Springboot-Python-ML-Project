package com.teach.predict.service.mapper;


import com.teach.predict.domain.*;
import com.teach.predict.service.dto.MLModelDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MLModel} and its DTO {@link MLModelDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MLModelMapper extends EntityMapper<MLModelDTO, MLModel> {



    default MLModel fromId(Long id) {
        if (id == null) {
            return null;
        }
        MLModel mLModel = new MLModel();
        mLModel.setId(id);
        return mLModel;
    }
}
