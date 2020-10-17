package com.teach.predict.domain;

import com.teach.predict.domain.enumeration.Specialization;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CoursePK implements Serializable {

    private Long code;

    private Specialization specialization;

    public Long getCode() {
        return code;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursePK coursePK = (CoursePK) o;
        return Objects.equals(code, coursePK.code) &&
            specialization == coursePK.specialization;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, specialization);
    }
}
