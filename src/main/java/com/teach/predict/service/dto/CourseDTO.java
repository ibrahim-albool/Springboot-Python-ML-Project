package com.teach.predict.service.dto;

import java.io.Serializable;

import com.teach.predict.domain.Course;
import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Type;

/**
 * A DTO for the {@link com.teach.predict.domain.Course} entity.
 */
public class CourseDTO implements Serializable {

    private String code;

    private Specialization specialization;

    private String name;

    private Type type;

    private Integer hours;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        return code != null && specialization != null && code.equals(((CourseDTO) o).code)
            && specialization.equals(((CourseDTO) o).specialization);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
            "code='" + getCode() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", hours=" + getHours() +
            "}";
    }
}
