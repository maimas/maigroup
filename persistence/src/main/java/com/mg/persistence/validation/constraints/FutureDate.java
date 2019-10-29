package com.mg.persistence.validation.constraints;

import com.mg.persistence.commons.PropertyUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Validates if the date is a future date.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class FutureDate extends AbstractConstraint {

    private Date currentDate;


    public FutureDate(String fieldName, Object fieldValue, String constraint) {
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

        Date value = null;
        setParameters();

        if (fieldValue instanceof Date) {
            value = (Date) fieldValue;

        } else if (fieldValue instanceof String) {
            value = PropertyUtils.getAsDateTime(fieldValue);

        } else if (fieldValue instanceof Long) {
            value = PropertyUtils.getAsDateTime(fieldValue);
        }
        return (value != null) && value.after(currentDate);
    }

    public String getViolationMsg() {
        return String.format("Invalid future date value [%s] for the field [%s]", fieldName, fieldValue);
    }

    private void setParameters() {
        currentDate = new Date(System.currentTimeMillis());
    }

}

