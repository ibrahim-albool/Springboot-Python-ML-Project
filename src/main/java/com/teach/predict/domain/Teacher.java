package com.teach.predict.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.teach.predict.domain.enumeration.Specialization;

import com.teach.predict.domain.enumeration.Qualification;

import com.teach.predict.domain.enumeration.Stage;

/**
 * A Teacher.
 */
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "number")
    private Long number;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;

    @Column(name = "evaluation")
    private Integer evaluation;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualification")
    private Qualification qualification;

    @Enumerated(EnumType.STRING)
    @Column(name = "stage")
    private Stage stage;

    @Column(name = "sum_of_hours")
    private Integer sumOfHours;

    @Column(name = "creation_date")
    private Instant creationDate;

    @Column(name = "is_predicted")
    private Boolean isPredicted;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "teacher_courses",
        joinColumns = @JoinColumn(name = "teacher_number", referencedColumnName = "number"),
        inverseJoinColumns = {@JoinColumn(name = "courses_code", referencedColumnName = "code"),
            @JoinColumn(name = "courses_specialization", referencedColumnName = "specialization")})
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getNumber() {
        return number;
    }

    public Teacher number(Long number) {
        this.number = number;
        return this;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public Teacher specialization(Specialization specialization) {
        this.specialization = specialization;
        return this;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Integer getEvaluation() {
        return evaluation;
    }

    public Teacher evaluation(Integer evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    public void setEvaluation(Integer evaluation) {
        this.evaluation = evaluation;
    }

    public Qualification getQualification() {
        return qualification;
    }

    public Teacher qualification(Qualification qualification) {
        this.qualification = qualification;
        return this;
    }

    public void setQualification(Qualification qualification) {
        this.qualification = qualification;
    }

    public Stage getStage() {
        return stage;
    }

    public Teacher stage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Integer getSumOfHours() {
        return sumOfHours;
    }

    public Teacher sumOfHours(Integer sumOfHours) {
        this.sumOfHours = sumOfHours;
        return this;
    }

    public void setSumOfHours(Integer sumOfHours) {
        this.sumOfHours = sumOfHours;
    }

    public Boolean isIsPredicted() {
        return isPredicted;
    }

    public Teacher isPredicted(Boolean isPredicted) {
        this.isPredicted = isPredicted;
        return this;
    }

    public void setIsPredicted(Boolean isPredicted) {
        this.isPredicted = isPredicted;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Teacher courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Teacher addCourses(Course course) {
        this.courses.add(course);
        course.getTeachers().add(this);
        return this;
    }

    public Teacher removeCourses(Course course) {
        this.courses.remove(course);
        course.getTeachers().remove(this);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here


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
        if (!(o instanceof Teacher)) {
            return false;
        }
        return number != null && number.equals(((Teacher) o).number);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "number=" + getNumber() +
            ", specialization='" + getSpecialization() + "'" +
            ", evaluation=" + getEvaluation() +
            ", qualification='" + getQualification() + "'" +
            ", stage='" + getStage() + "'" +
            ", sumOfHours=" + getSumOfHours() +
            ", isPredicted='" + isIsPredicted() + "'" +
            "}";
    }
}
