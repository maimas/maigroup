package com.mg.persistence.domain.bizitem.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a wrapper class of the <Code>PropertyType</Code>.
 * It will hold the information of the PropertyType xml property definition, defined in the <itemType>.schema.xml
 */
@Data
public class SchemaProperty {


    /**
     * Indicated whether the property is system or custom
     */
    private boolean system;
    private boolean indexed, unique, autogenerated;
    /**
     * Index name has to be same at the property path. Ex: User -> content.email
     */
    private String indexName;

    private String name, caption, captionTransKey, type, typeName, enumName;
    private List<String> validationConstraints = new ArrayList<>();
}
