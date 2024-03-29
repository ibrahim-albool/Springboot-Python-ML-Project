package com.teach.predict.service.mapper;


import com.teach.predict.domain.*;
import com.teach.predict.service.dto.CourseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {


    @Mapping(target = "teachers", ignore = true)
    @Mapping(target = "removeTeachers", ignore = true)
    Course toEntity(CourseDTO courseDTO);

    default Course fromCode(Long code) {
        if (code == null) {
            return null;
        }
        Course course = new Course();
        course.setCode(code);
        return course;
    }
}
