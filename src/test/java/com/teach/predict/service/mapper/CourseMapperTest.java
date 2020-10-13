package com.teach.predict.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseMapperTest {

    private CourseMapper courseMapper;

    @BeforeEach
    public void setUp() {
        courseMapper = new CourseMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String code = "1L";
        assertThat(courseMapper.fromCode(code).getCode()).isEqualTo(code);
        assertThat(courseMapper.fromCode(null)).isNull();
    }
}
