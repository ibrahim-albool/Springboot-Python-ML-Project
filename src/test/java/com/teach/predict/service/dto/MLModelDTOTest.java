package com.teach.predict.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class MLModelDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MLModelDTO.class);
        MLModelDTO mLModelDTO1 = new MLModelDTO();
        mLModelDTO1.setId(1L);
        MLModelDTO mLModelDTO2 = new MLModelDTO();
        assertThat(mLModelDTO1).isNotEqualTo(mLModelDTO2);
        mLModelDTO2.setId(mLModelDTO1.getId());
        assertThat(mLModelDTO1).isEqualTo(mLModelDTO2);
        mLModelDTO2.setId(2L);
        assertThat(mLModelDTO1).isNotEqualTo(mLModelDTO2);
        mLModelDTO1.setId(null);
        assertThat(mLModelDTO1).isNotEqualTo(mLModelDTO2);
    }
}
