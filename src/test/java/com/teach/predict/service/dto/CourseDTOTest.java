package com.teach.predict.service.dto;

import com.teach.predict.domain.enumeration.Specialization;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class CourseDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseDTO.class);
        CourseDTO courseDTO1 = new CourseDTO();
        courseDTO1.setCode(1L);
        courseDTO1.setSpecialization(Specialization.English);
        CourseDTO courseDTO2 = new CourseDTO();
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO2.setCode(courseDTO1.getCode());
        courseDTO2.setSpecialization(courseDTO1.getSpecialization());
        assertThat(courseDTO1).isEqualTo(courseDTO2);
        courseDTO2.setCode(2L);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
        courseDTO1.setCode(null);
        assertThat(courseDTO1).isNotEqualTo(courseDTO2);
    }
}
