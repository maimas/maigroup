package com.mg.persistence.validation.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * Validates if the list values are matching the regular expression
 * Expects List of strings or a string that represents a list (abc,def,xyz)
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RegexList extends AbstractConstraint {

    private String regex;


    public RegexList(String fieldName, Object fieldValue, String constraintString) {
        this.constraintName = getClass().getSimpleName();
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.constraintString = constraintString;
    }


    public boolean isValid() {
        if (fieldValue == null) {
            return true;
        }
        setParameters();
        AtomicBoolean valid = new AtomicBoolean(true);

        if (fieldValue != null) {

            if (fieldValue instanceof List) {
                ((List) fieldValue).forEach(element -> {
                    if (!Pattern.matches(regex, String.valueOf(element))) {
                        valid.set(false);
                    }
                });
                return valid.get();
            }

            if (fieldValue instanceof String) {
                String[] elements = String.valueOf(fieldValue).split(",");
                Arrays.stream(elements).forEach(element -> {
                    if (!Pattern.matches(regex, String.valueOf(element))) {
                        valid.set(false);
                    }
                });
                return valid.get();
            }

        }
        return false;
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

