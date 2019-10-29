package com.mg.persistence.validation;


import com.mg.persistence.domain.bizitem.model.SchemaProperty;
import com.mg.persistence.domain.enumeration.service.EnumService;
import com.mg.persistence.validation.constraints.Enum;
import com.mg.persistence.validation.constraints.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Log4j2
public class ConstraintProvider {

    private EnumService enumService;

    ConstraintProvider(EnumService enumService) {
        this.enumService = enumService;
    }

    /**
     * Provides the Model Property constraints.
     *
     * @param valueToValidate - property value to be validated
     * @param schemaProperty  - schema property object that holds all meta info about the field
     * @return - list of validation constraints defined for the provided field.
     */
    List<Constraint> getPropertyConstraints(Object valueToValidate, SchemaProperty schemaProperty) {
        List<Constraint> result = new ArrayList<>();
        log.trace("Property constraints lookup: " + schemaProperty.getName());

        List<String> propertyConstraints = schemaProperty.getValidationConstraints();
        if (propertyConstraints.isEmpty()) {
            log.warn(String.format("Found property with no constraints. Please review xml definition for model Property [%s]", schemaProperty.getName()));
        }

        schemaProperty.getValidationConstraints().forEach(cName -> {
            Constraint constraint = getConstraint(schemaProperty, cName, valueToValidate);
            result.add(constraint);
        });

        log.trace(String.format("Property [%s] constraints found. Total constraints[%s].", schemaProperty.getName(), result.size()));
        return result;
    }

    private Constraint getConstraint(SchemaProperty property, String constraint, Object valueToValidate) {

        if ("NotNull".equalsIgnoreCase(constraint)) {
            return new NotNull(property.getCaption(), valueToValidate, constraint);

        } else if (constraint.startsWith("Size_")) {
            return new Size(property.getCaption(), valueToValidate, constraint);

        } else if ("PastDate".equalsIgnoreCase(constraint)) {
            return new PastDate(property.getCaption(), valueToValidate, constraint);

        } else if ("PastYear".equalsIgnoreCase(constraint)) {
            return new PastYear(property.getCaption(), valueToValidate, constraint);

        } else if ("FutureDate".equalsIgnoreCase(constraint)) {
            return new FutureDate(property.getCaption(), valueToValidate, constraint);

        } else if ("FutureYear".equalsIgnoreCase(constraint)) {
            return new FutureYear(property.getCaption(), valueToValidate, constraint);

        } else if ("BooleanType".equalsIgnoreCase(constraint)) {
            return new BooleanType(property.getCaption(), valueToValidate, constraint);

        } else if (constraint.startsWith("Max_")) {
            return new Max(property.getCaption(), valueToValidate, constraint);

        } else if (constraint.startsWith("Min_")) {
            return new Min(property.getCaption(), valueToValidate, constraint);

        } else if (constraint.startsWith("MaxSizeMB_")) {
            return new MaxSizeMB(property.getCaption(), valueToValidate, constraint);

        } else if (constraint.startsWith("RegexList")) {
            return new RegexList(property.getCaption(), valueToValidate, constraint);

        } else if (constraint.startsWith("Regex")) {
            return new Regex(property.getCaption(), valueToValidate, constraint);

        } else if ("EnumList".equalsIgnoreCase(constraint)) {
            return new EnumList(enumService, property, valueToValidate, constraint);

        } else if ("Enum".equalsIgnoreCase(constraint)) {
            return new Enum(enumService, property, valueToValidate, constraint);
//            c.setAutogenerated(property.isAutogenerated());
//            c.setConstraintString(constraint);
//            c.setFieldName(property.getEnumName());
//            c.setFieldValue(valueToValidate);
//            return c;
        }

        throw new RuntimeException(String.format("Constraint definition not found [%s] for the filed [%s]", constraint, property.getName()));
    }


}


