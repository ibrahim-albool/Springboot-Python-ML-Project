package com.teach.predict.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Type;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.teach.predict.domain.Course} entity. This class is used
 * in {@link com.teach.predict.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Specialization
     */
    public static class SpecializationFilter extends Filter<Specialization> {

        public SpecializationFilter() {
        }

        public SpecializationFilter(SpecializationFilter filter) {
            super(filter);
        }

        @Override
        public SpecializationFilter copy() {
            return new SpecializationFilter(this);
        }

    }
    /**
     * Class for filtering Type
     */
    public static class TypeFilter extends Filter<Type> {

        public TypeFilter() {
        }

        public TypeFilter(TypeFilter filter) {
            super(filter);
        }

        @Override
        public TypeFilter copy() {
            return new TypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter code;

    private SpecializationFilter specialization;

    private StringFilter name;

    private TypeFilter type;

    private IntegerFilter hours;

    private LongFilter teachersId;

    public CourseCriteria() {
    }

    public CourseCriteria(CourseCriteria other) {
        this.code = other.code == null ? null : other.code.copy();
        this.specialization = other.specialization == null ? null : other.specialization.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.hours = other.hours == null ? null : other.hours.copy();
        this.teachersId = other.teachersId == null ? null : other.teachersId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
    }

    public LongFilter getCode() {
        return code;
    }

    public void setCode(LongFilter code) {
        this.code = code;
    }

    public SpecializationFilter getSpecialization() {
        return specialization;
    }

    public void setSpecialization(SpecializationFilter specialization) {
        this.specialization = specialization;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public TypeFilter getType() {
        return type;
    }

    public void setType(TypeFilter type) {
        this.type = type;
    }

    public IntegerFilter getHours() {
        return hours;
    }

    public void setHours(IntegerFilter hours) {
        this.hours = hours;
    }

    public LongFilter getTeachersId() {
        return teachersId;
    }

    public void setTeachersId(LongFilter teachersId) {
        this.teachersId = teachersId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(code, that.code) &&
            Objects.equals(specialization, that.specialization) &&
            Objects.equals(name, that.name) &&
            Objects.equals(type, that.type) &&
            Objects.equals(hours, that.hours) &&
            Objects.equals(teachersId, that.teachersId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        code,
        specialization,
        name,
        type,
        hours,
        teachersId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseCriteria{" +
                (code != null ? "code=" + code + ", " : "") +
                (specialization != null ? "specialization=" + specialization + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (hours != null ? "hours=" + hours + ", " : "") +
                (teachersId != null ? "teachersId=" + teachersId + ", " : "") +
            "}";
    }

}
