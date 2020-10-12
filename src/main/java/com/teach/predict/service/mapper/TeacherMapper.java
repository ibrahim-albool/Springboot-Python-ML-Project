package com.teach.predict.service.mapper;


import com.teach.predict.domain.*;
import com.teach.predict.service.dto.TeacherDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Teacher} and its DTO {@link TeacherDTO}.
 */
@Mapper(componentModel = "spring", uses = {CourseMapper.class})
public interface TeacherMapper extends EntityMapper<TeacherDTO, Teacher> {


    @Mapping(target = "removeCourses", ignore = true)

    default Teacher fromNumber(Long number) {
        if (number == null) {
            return null;
        }
        Teacher teacher = new Teacher();
        teacher.setNumber(number);
        return teacher;
    }
}
