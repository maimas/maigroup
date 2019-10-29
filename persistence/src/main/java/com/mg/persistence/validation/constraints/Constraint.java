package com.mg.persistence.validation.constraints;

public interface Constraint {

    String getConstraintString();

    Object getFieldValue();

    String getFieldName();

    boolean isValid();

    String getViolationMsg();
}
