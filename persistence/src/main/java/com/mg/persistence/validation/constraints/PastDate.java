package com.mg.persistence.validation.constraints;

import com.mg.persistence.commons.PropertyUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Validates if the date is a past date.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PastDate extends AbstractConstraint {

    private Date currentDate;


    public PastDate(String fieldName, Object fieldValue, String constraint) {
        constraintName = getClass().getSimpleName();
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
        Date value = null;
        if (fieldValue instanceof Date) {
            value = (Date) fieldValue;
        }
        if (fieldValue instanceof String) {
            value = PropertyUtils.getAsDateTime(fieldValue);
        }
        if (fieldValue instanceof Long) {
            value = PropertyUtils.getAsDateTime(fieldValue);
        }
        return (value != null) && value.before(currentDate);
    }

    public String getViolationMsg() {
        return String.format("Invalid past date for the field [%s], value [%s]", fieldName, fieldValue);
    }

    private void setParameters() {
        currentDate = new Date(System.currentTimeMillis());
    }

}

