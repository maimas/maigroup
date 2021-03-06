package com.mg.persistence.domain.bizitem.reader;


import com.mg.persistence.domain.XmlMetadataUnmarshaller;
import com.mg.persistence.domain.bizitem.definition.BizItemSchemaType;
import com.mg.persistence.domain.bizitem.definition.PropertyType;
import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.domain.bizitem.model.SchemaProperty;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reads the XML definition and converts to application schema object <code>BizItemSchemaModel</code>
 */
public class BizItemSchemaXmlReader extends XmlMetadataUnmarshaller<BizItemSchemaType> {

    public BizItemSchemaXmlReader() {
        super.setContextPath("com.mg.persistence.domain.bizitem.definition");
    }

    public BizItemSchemaModel toSchemaModel(BizItemSchemaType schemaType) {
        BizItemSchemaModel schemaModel = new BizItemSchemaModel();

        schemaModel.setItemType(schemaType.getItemType());
        schemaModel.setTargetItemType(schemaType.getTargetItemType());
        schemaModel.setProperties(toPropertyModels(schemaType));


        return schemaModel;
    }

    //------------------Primate methods-------------------------------------

    private SchemaProperty toPropertyModel(PropertyType propertyType, boolean system) {
        SchemaProperty property = new SchemaProperty();

        property.setSystem(system);
        property.setName(propertyType.getName());
        property.setCaption(propertyType.getCaption());
        property.setCaptionTransKey(propertyType.getCaptionTransKey());
        property.setType(propertyType.getType());
        property.setTypeName(propertyType.getTypeName());
        property.setIndexed((Boolean) convert(propertyType.getIndexed(), "Boolean"));
        property.setUnique((Boolean) convert(propertyType.getUnique(), "Boolean"));
        property.setIndexName(system ? property.getName() : "content." + property.getName());
        property.setAutogenerated((Boolean) convert(propertyType.getAutogenerated(), "Boolean"));
        property.setEnumName(propertyType.getEnumName());

        if (!StringUtils.isEmpty(propertyType.getConstrints())) {
            String[] validationSplits = propertyType.getConstrints().split(",");
            List<String> validationList = new ArrayList<>();

            Arrays.stream(validationSplits).forEach(v -> {
                validationList.add(v.trim());
            });

            property.setValidationConstraints(validationList);
        }

        return property;
    }

    /**
     * Converts XML property to SchemaProperty model.
     *
     * @param schema - schema to read
     * @return
     */
    private List<SchemaProperty> toPropertyModels(BizItemSchemaType schema) {
        List<SchemaProperty> properties = new ArrayList<>();

        List<PropertyType> systemPropertyList = schema.getSystem().getProperty();
        systemPropertyList.forEach(prp -> {
            SchemaProperty prpModel = toPropertyModel(prp, true);
            properties.add(prpModel);
        });

        List<PropertyType> customPropertyList = schema.getContent().getProperty();
        customPropertyList.forEach(prp -> {
            SchemaProperty prpModel = toPropertyModel(prp, false);
            properties.add(prpModel);
        });
        return properties;
    }
}
