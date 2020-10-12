package com.teach.predict.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.teach.predict.domain.MLModel} entity. This class is used
 * in {@link com.teach.predict.web.rest.MLModelResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ml-models?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MLModelCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private FloatFilter tp;

    private FloatFilter tn;

    private FloatFilter fp;

    private FloatFilter fn;

    private FloatFilter accuracy;

    private FloatFilter precision;

    private FloatFilter recall;

    public MLModelCriteria() {
    }

    public MLModelCriteria(MLModelCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tp = other.tp == null ? null : other.tp.copy();
        this.tn = other.tn == null ? null : other.tn.copy();
        this.fp = other.fp == null ? null : other.fp.copy();
        this.fn = other.fn == null ? null : other.fn.copy();
        this.accuracy = other.accuracy == null ? null : other.accuracy.copy();
        this.precision = other.precision == null ? null : other.precision.copy();
        this.recall = other.recall == null ? null : other.recall.copy();
    }

    @Override
    public MLModelCriteria copy() {
        return new MLModelCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public FloatFilter getTp() {
        return tp;
    }

    public void setTp(FloatFilter tp) {
        this.tp = tp;
    }

    public FloatFilter getTn() {
        return tn;
    }

    public void setTn(FloatFilter tn) {
        this.tn = tn;
    }

    public FloatFilter getFp() {
        return fp;
    }

    public void setFp(FloatFilter fp) {
        this.fp = fp;
    }

    public FloatFilter getFn() {
        return fn;
    }

    public void setFn(FloatFilter fn) {
        this.fn = fn;
    }

    public FloatFilter getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(FloatFilter accuracy) {
        this.accuracy = accuracy;
    }

    public FloatFilter getPrecision() {
        return precision;
    }

    public void setPrecision(FloatFilter precision) {
        this.precision = precision;
    }

    public FloatFilter getRecall() {
        return recall;
    }

    public void setRecall(FloatFilter recall) {
        this.recall = recall;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MLModelCriteria that = (MLModelCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tp, that.tp) &&
            Objects.equals(tn, that.tn) &&
            Objects.equals(fp, that.fp) &&
            Objects.equals(fn, that.fn) &&
            Objects.equals(accuracy, that.accuracy) &&
            Objects.equals(precision, that.precision) &&
            Objects.equals(recall, that.recall);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tp,
        tn,
        fp,
        fn,
        accuracy,
        precision,
        recall
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MLModelCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tp != null ? "tp=" + tp + ", " : "") +
                (tn != null ? "tn=" + tn + ", " : "") +
                (fp != null ? "fp=" + fp + ", " : "") +
                (fn != null ? "fn=" + fn + ", " : "") +
                (accuracy != null ? "accuracy=" + accuracy + ", " : "") +
                (precision != null ? "precision=" + precision + ", " : "") +
                (recall != null ? "recall=" + recall + ", " : "") +
            "}";
    }

}
