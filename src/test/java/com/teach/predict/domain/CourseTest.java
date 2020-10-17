package com.teach.predict.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class CourseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setCode(1L);
        Course course2 = new Course();
        course2.setCode(course1.getCode());
        assertThat(course1).isEqualTo(course2);
        course2.setCode(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setCode(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
