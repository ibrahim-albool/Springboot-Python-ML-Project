package com.teach.predict.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Qualification;
import com.teach.predict.domain.enumeration.Stage;
import io.github.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.teach.predict.domain.Teacher} entity. This class is used
 * in {@link com.teach.predict.web.rest.TeacherResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /teachers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TeacherCriteria implements Serializable, Criteria {
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
     * Class for filtering Qualification
     */
    public static class QualificationFilter extends Filter<Qualification> {

        public QualificationFilter() {
        }

        public QualificationFilter(QualificationFilter filter) {
            super(filter);
        }

        @Override
        public QualificationFilter copy() {
            return new QualificationFilter(this);
        }

    }
    /**
     * Class for filtering Stage
     */
    public static class StageFilter extends Filter<Stage> {

        public StageFilter() {
        }

        public StageFilter(StageFilter filter) {
            super(filter);
        }

        @Override
        public StageFilter copy() {
            return new StageFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter number;

    private SpecializationFilter specialization;

    private IntegerFilter evaluation;

    private QualificationFilter qualification;

    private StageFilter stage;

    private IntegerFilter sumOfHours;

    private InstantFilter creationDate;

    private BooleanFilter isPredicted;


    public TeacherCriteria() {
    }

    public TeacherCriteria(TeacherCriteria other) {
        this.number = other.number == null ? null : other.number.copy();
        this.creationDate = other.creationDate == null ? null : other.creationDate.copy();
        this.specialization = other.specialization == null ? null : other.specialization.copy();
        this.evaluation = other.evaluation == null ? null : other.evaluation.copy();
        this.qualification = other.qualification == null ? null : other.qualification.copy();
        this.stage = other.stage == null ? null : other.stage.copy();
        this.sumOfHours = other.sumOfHours == null ? null : other.sumOfHours.copy();
        this.isPredicted = other.isPredicted == null ? null : other.isPredicted.copy();
    }

    @Override
    public TeacherCriteria copy() {
        return new TeacherCriteria(this);
    }

    public LongFilter getNumber() {
        return number;
    }

    public void setNumber(LongFilter number) {
        this.number = number;
    }

    public SpecializationFilter getSpecialization() {
        return specialization;
    }

    public void setSpecialization(SpecializationFilter specialization) {
        this.specialization = specialization;
    }

    public IntegerFilter getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(IntegerFilter evaluation) {
        this.evaluation = evaluation;
    }

    public QualificationFilter getQualification() {
        return qualification;
    }

    public void setQualification(QualificationFilter qualification) {
        this.qualification = qualification;
    }

    public StageFilter getStage() {
        return stage;
    }

    public void setStage(StageFilter stage) {
        this.stage = stage;
    }

    public IntegerFilter getSumOfHours() {
        return sumOfHours;
    }

    public void setSumOfHours(IntegerFilter sumOfHours) {
        this.sumOfHours = sumOfHours;
    }

    public BooleanFilter getIsPredicted() {
        return isPredicted;
    }

    public void setIsPredicted(BooleanFilter isPredicted) {
        this.isPredicted = isPredicted;
    }

    public InstantFilter getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(InstantFilter creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TeacherCriteria that = (TeacherCriteria) o;
        return
            Objects.equals(number, that.number) &&
                Objects.equals(specialization, that.specialization) &&
                Objects.equals(evaluation, that.evaluation) &&
                Objects.equals(qualification, that.qualification) &&
                Objects.equals(stage, that.stage) &&
                Objects.equals(sumOfHours, that.sumOfHours) &&
                Objects.equals(isPredicted, that.isPredicted) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        number,
        specialization,
        evaluation,
        qualification,
        stage,
        sumOfHours,
        isPredicted,
        creationDate
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TeacherCriteria{" +
                (number != null ? "number=" + number + ", " : "") +
                (specialization != null ? "specialization=" + specialization + ", " : "") +
                (evaluation != null ? "evaluation=" + evaluation + ", " : "") +
                (qualification != null ? "qualification=" + qualification + ", " : "") +
                (stage != null ? "stage=" + stage + ", " : "") +
                (sumOfHours != null ? "sumOfHours=" + sumOfHours + ", " : "") +
                (isPredicted != null ? "isPredicted=" + isPredicted + ", " : "") +
            "}";
    }

}
