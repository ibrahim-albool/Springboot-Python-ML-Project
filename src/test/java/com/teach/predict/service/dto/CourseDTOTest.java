package com.teach.predict.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class CourseDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseDTO.class);
        CourseDTO courseDTO1 = new CourseDTO();
        courseDTO1.setCode(1L);
        CourseDTO courseDTO2 = new CourseDTO();
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO2.setCode(courseDTO1.getCode());
        assertThat(courseDTO1).isEqualTo(courseDTO2);
        courseDTO2.setCode(2L);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO1.setCode(null);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
    }
}
