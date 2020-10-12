package com.teach.predict.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A MLModel.
 */
@Entity
@Table(name = "ml_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MLModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tp")
    private Float tp;

    @Column(name = "tn")
    private Float tn;

    @Column(name = "fp")
    private Float fp;

    @Column(name = "fn")
    private Float fn;

    @Column(name = "accuracy")
    private Float accuracy;

    @Column(name = "jhi_precision")
    private Float precision;

    @Column(name = "recall")
    private Float recall;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTp() {
        return tp;
    }

    public MLModel tp(Float tp) {
        this.tp = tp;
        return this;
    }

    public void setTp(Float tp) {
        this.tp = tp;
    }

    public Float getTn() {
        return tn;
    }

    public MLModel tn(Float tn) {
        this.tn = tn;
        return this;
    }

    public void setTn(Float tn) {
        this.tn = tn;
    }

    public Float getFp() {
        return fp;
    }

    public MLModel fp(Float fp) {
        this.fp = fp;
        return this;
    }

    public void setFp(Float fp) {
        this.fp = fp;
    }

    public Float getFn() {
        return fn;
    }

    public MLModel fn(Float fn) {
        this.fn = fn;
        return this;
    }

    public void setFn(Float fn) {
        this.fn = fn;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public MLModel accuracy(Float accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Float getPrecision() {
        return precision;
    }

    public MLModel precision(Float precision) {
        this.precision = precision;
        return this;
    }

    public void setPrecision(Float precision) {
        this.precision = precision;
    }

    public Float getRecall() {
        return recall;
    }

    public MLModel recall(Float recall) {
        this.recall = recall;
        return this;
    }

    public void setRecall(Float recall) {
        this.recall = recall;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MLModel)) {
            return false;
        }
        return id != null && id.equals(((MLModel) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MLModel{" +
            "id=" + getId() +
            ", tp=" + getTp() +
            ", tn=" + getTn() +
            ", fp=" + getFp() +
            ", fn=" + getFn() +
            ", accuracy=" + getAccuracy() +
            ", precision=" + getPrecision() +
            ", recall=" + getRecall() +
            "}";
    }
}
