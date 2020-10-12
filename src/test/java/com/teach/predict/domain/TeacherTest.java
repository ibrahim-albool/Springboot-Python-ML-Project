package com.teach.predict.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class TeacherTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Teacher.class);
        Teacher teacher1 = new Teacher();
        teacher1.setNumber(1L);
        Teacher teacher2 = new Teacher();
        teacher2.setNumber(teacher1.getNumber());
        assertThat(teacher1).isEqualTo(teacher2);
        teacher2.setNumber(2L);
        assertThat(teacher1).isNotEqualTo(teacher2);
        teacher1.setNumber(null);
        assertThat(teacher1).isNotEqualTo(teacher2);
    }
}
