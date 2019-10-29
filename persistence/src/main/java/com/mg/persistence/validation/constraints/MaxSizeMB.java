package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Validates if a number is grater than min allowed.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MaxSizeMB extends AbstractConstraint {

    private int max;


    public MaxSizeMB(String fieldName, Object fieldValue, String constraint) {
        this.constraintName = getClass().getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintString = constraint;
    }


    public boolean isValid() {
        if (fieldValue == null) {
            return true;

        } else if (String.valueOf(fieldValue).trim().isEmpty()) {
            return false;

        } else {
            setParameters();
            if (fieldValue instanceof byte[]) {
                int mega = ((byte[]) fieldValue).length / (1024 * 1024);//MB
                return mega <= max;

            } else if (fieldValue instanceof String) {
                int mega = (String.valueOf(fieldValue)).length() / (1024 * 1024);//MB
                return mega <= max;

            } else {
                throw new RuntimeException("Failed to determine content type for the filed -->" + fieldName);
            }
        }

    }

    public String getViolationMsg() {
        return String.format("Data size could not exceed more than %s Mb. ", max);
    }

    private void setParameters() {
        String[] cSplits = constraintString.split("_");
        max = Integer.parseInt(cSplits[1]);
    }
}

