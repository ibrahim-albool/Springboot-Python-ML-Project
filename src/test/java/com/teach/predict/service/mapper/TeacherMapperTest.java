package com.teach.predict.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = new TeacherMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long number = 1L;
        assertThat(teacherMapper.fromNumber(number).getNumber()).isEqualTo(number);
        assertThat(teacherMapper.fromNumber(null)).isNull();
    }
}
