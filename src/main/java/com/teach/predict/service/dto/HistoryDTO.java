package com.teach.predict.service.dto;

import com.teach.predict.domain.Course;
import com.teach.predict.domain.enumeration.Specialization;
import com.teach.predict.domain.enumeration.Type;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link Course} entity.
 */
public class HistoryDTO implements Serializable {

    private List<Double> loss;

    private List<Double> accuracy;

    public List<Double> getLoss() {
        return loss;
    }

    public List<Double> getAccuracy() {
        return accuracy;
    }

    public void setLoss(List<Double> loss) {
        this.loss = loss;
    }

    public void setAccuracy(List<Double> accuracy) {
        this.accuracy = accuracy;
    }


    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
            "loss='" + getLoss() + "'" +
            "accuracy" + getAccuracy() + "'" +
            "}";
    }
}
