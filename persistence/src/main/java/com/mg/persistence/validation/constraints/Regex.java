package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.regex.Pattern;

/**
 * Validates if the date is a future date.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Regex extends AbstractConstraint {

    private String regex;


    public Regex(String fieldName, Object fieldValue, String constraintString) {
        this.constraintName = getClass().getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintString = constraintString;
    }


    public boolean isValid() {
        if (fieldValue == null) {
            return true;
        }
        if (String.valueOf(fieldValue).trim().isEmpty()) {
            return false;
        }

        setParameters();
        return Pattern.matches(regex, String.valueOf(fieldValue));
    }

    public String getViolationMsg() {
        return String.format("Invalid value [%s] for the field [%s]. It does't matches the regular expression [%s]", fieldValue, fieldName, regex);
    }

    private void setParameters() {
        regex = constraintString.substring(constraintString.indexOf("_") + 1);
        if ("email".equalsIgnoreCase(regex)) {
            regex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
        }
    }
}

