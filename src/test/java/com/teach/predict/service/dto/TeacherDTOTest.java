package com.teach.predict.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.teach.predict.web.rest.TestUtil;

public class TeacherDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeacherDTO.class);
        TeacherDTO teacherDTO1 = new TeacherDTO();
        teacherDTO1.setNumber(1L);
        TeacherDTO teacherDTO2 = new TeacherDTO();
        assertThat(teacherDTO1).isNotEqualTo(teacherDTO2);
        teacherDTO2.setNumber(teacherDTO1.getNumber());
        assertThat(teacherDTO1).isEqualTo(teacherDTO2);
        teacherDTO2.setNumber(2L);
        assertThat(teacherDTO1).isNotEqualTo(teacherDTO2);
        teacherDTO1.setNumber(null);
        assertThat(teacherDTO1).isNotEqualTo(teacherDTO2);
    }
}
