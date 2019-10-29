package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Validates if a number is grater than min allowed.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Min extends AbstractConstraint {

    private int min;


    public Min(String fieldName, Object fieldValue, String constraint) {
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

        if (fieldValue instanceof Long) {
            return Long.parseLong(String.valueOf(fieldValue)) >= min;

        } else if (fieldValue instanceof Double) {
            return Double.parseDouble(String.valueOf(fieldValue)) >= min;

        } else {
            return Integer.parseInt(String.valueOf(fieldValue)) >= min;
        }

    }


    public String getViolationMsg() {
        return String.format("Invalid value [%s] for the field [%s]. It should be equal ot grater than [%s]", fieldValue, fieldName, min);
    }

    private void setParameters() {
        String[] cSplits = constraintString.split("_");
        min = Integer.parseInt(cSplits[1]);
    }
}

