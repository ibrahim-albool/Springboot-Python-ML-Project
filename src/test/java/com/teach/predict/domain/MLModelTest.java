package com.teach.predict.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class MLModelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MLModel.class);
        MLModel mLModel1 = new MLModel();
        mLModel1.setId(1L);
        MLModel mLModel2 = new MLModel();
        mLModel2.setId(mLModel1.getId());
        assertThat(mLModel1).isEqualTo(mLModel2);
        mLModel2.setId(2L);
        assertThat(mLModel1).isNotEqualTo(mLModel2);
        mLModel1.setId(null);
        assertThat(mLModel1).isNotEqualTo(mLModel2);
    }
}
