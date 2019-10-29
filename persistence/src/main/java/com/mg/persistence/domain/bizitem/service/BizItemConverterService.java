package com.mg.persistence.domain.bizitem.service;



import com.mg.persistence.commons.PropertyUtils;
import com.mg.persistence.domain.SystemCollection;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.domain.bizitem.model.SchemaProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public class BizItemConverterService {

    private BizItemSchemaService bizItemSchemaService;

    public BizItemConverterService(BizItemSchemaService schemaService) {
        this.bizItemSchemaService = schemaService;
    }


    /**
     * Iterates oer the model properties and casts them to the schema defined data type.
     *
     * @param model - model to cast
     * @return - model with casted properties
     */
    public BizItemModel castContent(BizItemModel model) {

        if (model.getContent() != null) {
            BizItemSchemaModel schema = bizItemSchemaService.findOne(SystemFiled.TargetItemType, model.getItemType(), SystemCollection.BizItemSchemas);
            if (schema != null) {
                model.getContent().forEach((key, value) -> {
                    SchemaProperty property = schema.getProperty(key);
                    model.putInContent(key, cast(property, value));
                });

            } else {
                throw new RuntimeException("BizItem schema not found:" + model.getItemType());
            }
        }
        return model;
    }


    private Object cast(SchemaProperty schemaProperty, Object value) {
        String type = schemaProperty.getType();
        switch (type) {
            case BizItemPropertyType.String:
                return String.valueOf(value);

            case BizItemPropertyType.Integer:
                return Integer.parseInt(String.valueOf(value));

            case BizItemPropertyType.Double:
                return Double.parseDouble(String.valueOf(value));

            case BizItemPropertyType.Long:
                return Long.parseLong(String.valueOf(value));

            case BizItemPropertyType.Boolean:
                return Boolean.parseBoolean(String.valueOf(value));

            case BizItemPropertyType.Date:
                return PropertyUtils.getAsDateTime(value);

            case BizItemPropertyType.List:
                return PropertyUtils.getAsListOfStrings(value);

            case BizItemPropertyType.Enum:
            case BizItemPropertyType.EnumList:
                return value;

            case BizItemPropertyType.ByteArray:
                return PropertyUtils.getAsByteArray(value);

            case BizItemPropertyType.Object:
                BizItemModel model = PropertyUtils.getObjectAsModel(value);
                model = castContent(model);
                return model;

            case BizItemPropertyType.ObjectList:
                List<BizItemModel> models = PropertyUtils.getObjectListAsModels(value);
                models.forEach(this::castContent);
                return models;

            default:
                String msg = "Failed to covert property " + schemaProperty.getCaption();
                log.error(msg);
                throw new RuntimeException(msg);
        }
//
//        return value;
    }


}


