package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Validates if a number is equal or less than max allowed.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Max extends AbstractConstraint {

    private int max;


    public Max(String fieldName, Object fieldValue, String constraint) {
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
            return Long.parseLong(String.valueOf(fieldValue)) <= max;

        } else if (fieldValue instanceof Double) {
            return Double.parseDouble(String.valueOf(fieldValue)) <= max;

        } else {
            return Integer.parseInt(String.valueOf(fieldValue)) <= max;
        }
    }

    public String getViolationMsg() {
        return String.format("Invalid value [%s] for the field [%s]. It should be equal ot less than [%s]", fieldValue, fieldName, max);
    }

    private void setParameters() {
        String[] cSplits = constraintString.split("_");
        max = Integer.parseInt(cSplits[1]);
    }
}

