package com.mg.persistence.validation.constraints;

import lombok.Data;

@Data
public abstract class AbstractConstraint implements Constraint {

    protected String constraintName, constraintString, fieldName;
    protected Object fieldValue;


    public String getViolationMsg() {
        return String.format("Constraint [%s] violation detected for filed [%s]", constraintString, fieldName);
    }
}

