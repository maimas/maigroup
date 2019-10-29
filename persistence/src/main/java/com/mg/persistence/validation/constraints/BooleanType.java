package com.mg.persistence.validation.constraints;

public class BooleanType extends AbstractConstraint {


    public BooleanType(String fieldName, Object fieldValue, String constraint) {
        this.constraintString = constraint;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintName = getClass().getSimpleName();
    }

    public boolean isValid() {
        if (fieldValue == null) {//allow nulls
            return true;
        }

        String value = String.valueOf(fieldValue);
        return "true".equals(value) || "false".equals(value);
    }

    public String getViolationMsg() {
        return String.format("Field [%s] has invalid value [%s]. It should be [true] or [false]", fieldName, fieldValue);
    }


}

