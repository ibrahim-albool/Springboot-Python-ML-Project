package com.teach.predict.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link com.teach.predict.domain.MLModel} entity.
 */
public class MLModelDTO implements Serializable {
    
    private Long id;

    private Float tp;

    private Float tn;

    private Float fp;

    private Float fn;

    private Float accuracy;

    private Float precision;

    private Float recall;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getTp() {
        return tp;
    }

    public void setTp(Float tp) {
        this.tp = tp;
    }

    public Float getTn() {
        return tn;
    }

    public void setTn(Float tn) {
        this.tn = tn;
    }

    public Float getFp() {
        return fp;
    }

    public void setFp(Float fp) {
        this.fp = fp;
    }

    public Float getFn() {
        return fn;
    }

    public void setFn(Float fn) {
        this.fn = fn;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public Float getPrecision() {
        return precision;
    }

    public void setPrecision(Float precision) {
        this.precision = precision;
    }

    public Float getRecall() {
        return recall;
    }

    public void setRecall(Float recall) {
        this.recall = recall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MLModelDTO)) {
            return false;
        }

        return id != null && id.equals(((MLModelDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MLModelDTO{" +
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
