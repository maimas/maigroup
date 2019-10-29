package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

/**
 * Validates if the date is a past year.
 * PastYear
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PastYear extends AbstractConstraint {

    private Integer currentYear;


    public PastYear(String fieldName, Object fieldValue, String constraint) {
        this.constraintName = getClass().getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintString = constraint;
    }


    public boolean isValid() {
        if (fieldValue == null) {
            return true;
        }

        setParameters();

        if (String.valueOf(fieldValue).trim().isEmpty()) {
            return false;
        }
        return currentYear > Integer.parseInt((String.valueOf(fieldValue)));
    }

    public String getViolationMsg() {
        return String.format("Invalid past year for the field [%s], value [%s]", fieldName, fieldValue);
    }

    private void setParameters() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        currentYear = calendar.get(Calendar.YEAR);
    }

}

