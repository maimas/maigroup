package com.mg.persistence.validation.constraints;

public class NotNull extends AbstractConstraint {


    public NotNull(String fieldName, Object fieldValue, String constraint) {
        this.constraintString = constraint;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintName = getClass().getSimpleName();
    }

    public boolean isValid() {
        return fieldValue != null;
    }

    public String getViolationMsg() {
        return String.format("Field [%s] value [%s] should not be null", fieldName, fieldValue);
    }


}

