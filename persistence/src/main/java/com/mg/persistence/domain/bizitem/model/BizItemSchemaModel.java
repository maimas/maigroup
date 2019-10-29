package com.mg.persistence.domain.bizitem.model;


import com.mg.persistence.domain.GenericModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;

/**
 * This is a wrapper class of the <Code>BizItemSchemaXmlReader</Code> result.
 * It will hold the information of the BizItem definition defined in the <itemType>.schema.xml
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BizItemSchemaModel extends GenericModel {

    private List<SchemaProperty> properties;

    /**
     * Name of the itemType that schema is dedicated for.
     */
    private String targetItemType;

    public SchemaProperty getProperty(@NonNull String fieldName) {
        return properties
                .stream()
                .filter(it -> Objects.equals(it.getName(), fieldName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("BizItem Schema Property not found  -> [%s]", fieldName)));
    }


    public boolean hasProperty(String fieldName) {
        SchemaProperty prp = properties
                .stream()
                .filter(it -> Objects.equals(it.getName(), fieldName))
                .findFirst()
                .orElse(null);

        return prp != null;
    }

}
