package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Validates if the field length has expected size.
 * Size_1_100 - min 1; max 100
 * Size_1 - min 1; max unlimited
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Size extends AbstractConstraint {

    int min = 0, max = 0;


    public Size(String fieldName, Object fieldValue, String constraint) {
        constraintName = getClass().getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintString = constraint;
    }


    public boolean isValid() {
        if (fieldValue == null) {
            return true;
        }


        setParameters();
        int length = String.valueOf(fieldValue).length();

        if (min == 0 && max != 0) {
            return length <= max;

        } else if (min != 0 && max != 0) {
            return length >= min && length <= max;

        } else if (min != 0) {
            return length >= min;

        } else if (max != 0) {
            return length >= max;
        }
        return false;
    }

    public String getViolationMsg() {
        if (min != 0 && max != 0) {
            return String.format("Field [%s] length should be between [%s] and [%s], value [%s] ", fieldName, min, max, fieldValue);

        } else if (min != 0) {
            return String.format("Field [%s] length should be more than [%s], value [%s]", fieldName, min, fieldValue);

        } else if (max != 0) {
            return String.format("Field [%s] length should be less than [%s], value [%s]", fieldName, max, fieldValue);
        }
        return super.getViolationMsg();
    }

    private void setParameters() {
        String[] cSplits = constraintString.split("_");
        if (cSplits.length >= 3) {
            min = Integer.parseInt(cSplits[1]);
            max = Integer.parseInt(cSplits[2]);
        } else {
            min = Integer.parseInt(cSplits[1]);
        }
    }

}

