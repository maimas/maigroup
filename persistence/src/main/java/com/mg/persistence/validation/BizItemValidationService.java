package com.mg.persistence.validation;


import com.mg.persistence.commons.PropertyUtils;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.domain.bizitem.model.SchemaProperty;
import com.mg.persistence.domain.bizitem.service.BizItemPropertyType;
import com.mg.persistence.domain.bizitem.service.BizItemSchemaService;
import com.mg.persistence.domain.enumeration.service.EnumService;
import com.mg.persistence.exceptions.BizItemSchemaValidationException;
import com.mg.persistence.exceptions.ValidationSchemaNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mg.persistence.domain.SystemCollection.BizItemSchemas;
import static com.mg.persistence.domain.SystemFiled.*;


/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public class BizItemValidationService {

    private BizItemSchemaService bizItemSchemaService;
    private EnumService enumService;

    public BizItemValidationService(BizItemSchemaService bizItemSchemaService, EnumService enumService) {
        this.bizItemSchemaService = bizItemSchemaService;
        this.enumService = enumService;
    }


    /**
     * Validates the bizItem against the object schema definition
     *
     * @param bizItem - bizItem to be validated against the schema
     * @throws ValidationSchemaNotFoundException - if the schema is not found
     * @throws BizItemSchemaValidationException  - if there is at list one violation
     */
    public void validateAndThrow(BizItemModel bizItem) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        long startTime = System.currentTimeMillis();
        List<String> violations = validate(bizItem, null);

        if (violations != null && !violations.isEmpty()) {
            logExecutionTime(startTime, bizItem);
            throw new BizItemSchemaValidationException(bizItem.getItemType(), violations);
        }
        logExecutionTime(startTime, bizItem);

    }


    /**
     * Validates the bizItem against the object schema definition.
     *
     * @param bizItem - bizItem to be validated against the schema
     * @return - list of violation error messages
     */
    public List<String> validate(BizItemModel bizItem, List<String> violationsList) throws ValidationSchemaNotFoundException {

        if (violationsList == null) {
            violationsList = new ArrayList<>();
        }

        BizItemSchemaModel validationSchema = getValidationSchema(bizItem.getItemType());

        List<String> unknownFields = getUnknownFields(bizItem, validationSchema);
        if (!unknownFields.isEmpty()) {
            violationsList.add(String.format("Unsupported fields identified %s. Please update BizItemSchema or correct the BizItemModel.", unknownFields.toString()));
        }

        violationsList.addAll(new PropertyValidator(TrackingId, bizItem.getTrackingId(), validationSchema, enumService).validate());
        violationsList.addAll(new PropertyValidator(ItemType, bizItem.getItemType(), validationSchema, enumService).validate());
        violationsList.addAll(new PropertyValidator(CreatedDate, bizItem.getCreatedDate(), validationSchema, enumService).validate());
        violationsList.addAll(new PropertyValidator(ModifiedDate, bizItem.getModifiedDate(), validationSchema, enumService).validate());

        List<SchemaProperty> schemaProperties = validationSchema.getProperties().stream().filter(it -> !it.isSystem()).collect(Collectors.toList());


        for (SchemaProperty property : schemaProperties) {
            List<String> violations = new ArrayList<>();

            if (BizItemPropertyType.Object.equalsIgnoreCase(property.getType())) {// property value is an object
                BizItemModel bizItemModel = objectFieldToModel(bizItem, property.getName());
                if (bizItemModel != null) {
                    validate(bizItemModel, violationsList);
                } else {// todo: see if this statement is required
                    violations.add(String.format("Failed to parse property [%s] from [%s]", property.getName(), bizItem.getItemType()));
                }

            } else if (BizItemPropertyType.ObjectList.equalsIgnoreCase(property.getType())) {// property value is a list of objects

                List<BizItemModel> bizItemModels = objectsFieldToModels(bizItem, property.getName());
                if (bizItemModels != null) {
                    for (BizItemModel model : bizItemModels) {
                        validate(model, violationsList);
                    }
                } else {// todo: see if this statement is required
                    violations.add(String.format("Failed to parse property [%s] from [%s]", property.getName(), bizItem.getItemType()));
                }

            } else { // simple property
                String fieldName = property.getName();
                Object value = bizItem.getContent().get(property.getName());
                violations = new PropertyValidator(fieldName, value, validationSchema, enumService).validate();
                violationsList.addAll(violations);
            }

        }

        return violationsList;
    }


    //----------------------------------------------------------------------------------
    //------------------------------Private Methods-------------------------------------
    //----------------------------------------------------------------------------------

    /**
     * Provides fields from the model that are not defined in the schema.
     * If there are unknown fields model will be considered as invalid;
     */
    private List<String> getUnknownFields(BizItemModel model, BizItemSchemaModel validationSchema) {

        List<String> unknownProperties = new ArrayList<>();

        List<SchemaProperty> schemaProperties = validationSchema.getProperties();
        model.getContent().keySet().forEach(fieldName -> {

            if (!validationSchema.hasProperty(fieldName)) {
                unknownProperties.add(fieldName);
            }
        });

        return unknownProperties;
    }

    private BizItemSchemaModel getValidationSchema(String targetCollection) throws ValidationSchemaNotFoundException {
        log.trace(String.format("Performing Validation schema lookup for [%s].", targetCollection));

        BizItemSchemaModel schema = bizItemSchemaService.findOne(SystemFiled.TargetItemType, targetCollection, BizItemSchemas);
        if (schema == null) {
            String msg = String.format("Validation Schema Name=[%s] not found in Collection=[%s]", targetCollection, BizItemSchemas);
            throw new ValidationSchemaNotFoundException(msg);
        }
        return schema;
    }

    private void logExecutionTime(long start, BizItemModel bizItem) {
        long execTime = System.currentTimeMillis() - start;
        log.debug(String.format("Validation took [%s ms] BizItem [%s]-->[%s]\n", execTime, bizItem.getItemType(), BizItemSchemas));
    }

    private BizItemModel objectFieldToModel(BizItemModel model, String prp) {
        try {
            Object item = model.getFromContent(prp);
            return PropertyUtils.getObjectAsModel(item);

        } catch (Exception e) {
            String msg = String.format("Failed to parse property [%s] from [%s]", prp, model.getItemType());
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    private List<BizItemModel> objectsFieldToModels(BizItemModel model, String prp) {
        try {
            Object item = model.getFromContent(prp);
            return PropertyUtils.getObjectListAsModels(item);

        } catch (Exception e) {
            String msg = String.format("Failed to parse property [%s] from [%s]", prp, model.getItemType());
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

}


