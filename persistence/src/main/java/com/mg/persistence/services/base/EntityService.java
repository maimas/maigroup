package com.mg.persistence.services.base;


import com.mg.persistence.exceptions.BizItemRelationException;
import com.mg.persistence.exceptions.BizItemSchemaValidationException;
import com.mg.persistence.exceptions.ValidationSchemaNotFoundException;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface EntityService<T> {


    /**
     * Find one bizItem
     *
     * @param fieldName - name of the field.
     * @param value     - to search for.
     * @param itemType  - target itemType
     * @return - result with one bizItem or null in case if not found
     */
    T findOne(String fieldName, Object value, String itemType);

    /**
     * Find all the models by criteria.
     *
     * @param query    - search criteria
     * @param itemType - item type to search for
     * @return - list of items that matches search criteria
     */
    List<T> find(Query query, String itemType);

    /**
     * Persists model in to the database
     *
     * @param model - bizItem to persist
     * @return - saved model
     */
    T save(T model) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException, BizItemRelationException;


    /**
     * Persists models in to the database
     *
     * @param models - bizItems to persist
     */
    void save(List<T> models) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException, BizItemRelationException;


    /**
     * Should enrich system fields of the bizItem model.
     * Ex: TrackingId, createdDate, modifiedDate,etc.
     *
     * @param model - model to enrich
     */
    T enrichMetadata(T model);

    /**
     * Performs model validation according to the validation schema predefined constrains.
     *
     * @param model - bizItem to validate
     * @throws ValidationSchemaNotFoundException - if schema not found
     * @throws BizItemSchemaValidationException  - if at list one field from the model fails constraint validation defined in the validation schema
     * @throws BizItemRelationException          - if the relation model is invalid
     */
    void validateOnSchema(T model) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException, BizItemRelationException;

}
