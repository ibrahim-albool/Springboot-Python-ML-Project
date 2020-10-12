package com.teach.predict.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MLModelMapperTest {

    private MLModelMapper mLModelMapper;

    @BeforeEach
    public void setUp() {
        mLModelMapper = new MLModelMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(mLModelMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(mLModelMapper.fromId(null)).isNull();
    }
}
