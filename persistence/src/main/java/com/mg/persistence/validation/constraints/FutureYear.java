package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

/**
 * Validates if the date is a future year.
 * PastYear
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FutureYear extends AbstractConstraint {

    private Integer currentYear;

    public FutureYear(String fieldName, Object fieldValue, String constraint) {
        this.constraintName = getClass().getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintString = constraint;
    }


    public boolean isValid() {
        if (fieldValue == null) {
            return true;
        }
        if (String.valueOf(fieldValue).trim().isEmpty()) {
            return false;
        }

        setParameters();
        int value = Integer.parseInt((String.valueOf(fieldValue)));
        return currentYear < value;
    }

    public String getViolationMsg() {
        return String.format("Invalid future year value [%s] for the field [%s].", fieldName, fieldValue);
    }

    private void setParameters() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentYear = calendar.get(Calendar.YEAR);
    }

}

