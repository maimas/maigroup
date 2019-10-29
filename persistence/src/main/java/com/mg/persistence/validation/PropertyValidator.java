package com.mg.persistence.validation;


import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.domain.bizitem.model.SchemaProperty;
import com.mg.persistence.domain.enumeration.service.EnumService;
import com.mg.persistence.exceptions.PropertValdiatorException;
import com.mg.persistence.validation.constraints.Constraint;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Log4j2
public class PropertyValidator {

    private String fieldName;
    private Object fieldValue;
    private BizItemSchemaModel validationSchema;
    private ConstraintProvider constraintProvider;

    /**
     * Prevent initialization without parameters
     */
    private PropertyValidator() {
    }


    PropertyValidator(String fieldName,
                      Object fieldValue,
                      BizItemSchemaModel validationSchema,
                      EnumService enumService) {

        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.validationSchema = validationSchema;
        this.constraintProvider = new ConstraintProvider(enumService);
        validateArguments();
        log.trace("Building property validator for: " + fieldName);
    }


    /**
     * Validates the filed value in concordance with the defines Schema Property validationConstraints constraints.
     *
     * @return - list of violations.
     */
    List<String> validate() {
        List<Constraint> validationConstraints = getValidationConstraints();
        List<String> violationsList = new ArrayList<>();

        validationConstraints.forEach(c -> {
            if (!c.isValid()) {
                violationsList.add(c.getViolationMsg());
                log.debug(String.format("Property [%s] constraint [%s] violation detected.", c.getFieldName(), c.getConstraintString()));
            }
        });

        return violationsList;
    }


    private List<Constraint> getValidationConstraints() {
        SchemaProperty schemaProperty = validationSchema.getProperty(fieldName);
        return constraintProvider.getPropertyConstraints(fieldValue, schemaProperty);
    }


    private void validateArguments() {
        if (fieldName == null) {
            throw new PropertValdiatorException("Mandatory parameter can not be null : fieldName");
        }
        if (validationSchema == null) {
            throw new PropertValdiatorException("Mandatory parameter can not be null : validationSchema");
        }
    }
}


