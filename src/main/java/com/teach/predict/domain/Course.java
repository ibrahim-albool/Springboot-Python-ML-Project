package com.teach.predict.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.teach.predict.domain.enumeration.Specialization;

import com.teach.predict.domain.enumeration.Type;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@IdClass(CoursePK.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "code")
    private Long code;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "specialization")
    private Specialization specialization;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Column(name = "hours")
    private Integer hours;

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getCode() {
        return code;
    }

    public Course code(Long code) {
        this.code = code;
        return this;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public Course specialization(Specialization specialization) {
        this.specialization = specialization;
        return this;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public Course type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Integer getHours() {
        return hours;
    }

    public Course hours(Integer hours) {
        this.hours = hours;
        return this;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public Course teachers(Set<Teacher> teachers) {
        this.teachers = teachers;
        return this;
    }

    public Course addTeachers(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getCourses().add(this);
        return this;
    }

    public Course removeTeachers(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getCourses().remove(this);
        return this;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return code != null && specialization != null && code.equals(((Course) o).code)
            && specialization.equals(((Course) o).specialization);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "code='" + getCode() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", hours=" + getHours() +
            "}";
    }
}
