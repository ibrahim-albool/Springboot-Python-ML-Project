package com.teach.predict.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Qualification;
import com.teach.predict.domain.enumeration.Stage;

/**
 * A DTO for the {@link com.teach.predict.domain.Teacher} entity.
 */
public class TeacherDTO implements Serializable {

    private Long number;

    private Specialization specialization;

    private Integer evaluation;

    private Qualification qualification;

    private Stage stage;

    private Integer sumOfHours;

    private Instant creationDate;

    private Boolean isPredicted;

    private Set<CourseDTO> courses = new HashSet<>();

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Integer getSumOfHours() {
        return sumOfHours;
    }

    public void setSumOfHours(Integer sumOfHours) {
        this.sumOfHours = sumOfHours;
    }

    public Boolean isIsPredicted() {
        return isPredicted;
    }

    public void setIsPredicted(Boolean isPredicted) {
        this.isPredicted = isPredicted;
    }

    public Set<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(Set<CourseDTO> courses) {
        this.courses = courses;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TeacherDTO)) {
            return false;
        }

        return number != null && number.equals(((TeacherDTO) o).number);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherDTO{" +
            "number=" + getNumber() +
            ", specialization='" + getSpecialization() + "'" +
            ", evaluation=" + getEvaluation() +
            ", qualification='" + getQualification() + "'" +
            ", stage='" + getStage() + "'" +
            ", sumOfHours=" + getSumOfHours() +
            ", isPredicted='" + isIsPredicted() + "'" +
            ", courses='" + getCourses() + "'" +
            "}";
    }
}
