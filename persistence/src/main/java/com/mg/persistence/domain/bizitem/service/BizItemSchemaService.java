package com.mg.persistence.domain.bizitem.service;


import com.mg.persistence.commons.TrackingIdGenerator;
import com.mg.persistence.domain.SystemCollection;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.exceptions.BizItemRelationException;
import com.mg.persistence.exceptions.BizItemSchemaValidationException;
import com.mg.persistence.exceptions.ValidationSchemaNotFoundException;
import com.mg.persistence.services.QueryService;
import com.mg.persistence.services.base.EntityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.mg.persistence.commons.Messages.MSG_TASK_END_OK;
import static com.mg.persistence.commons.Messages.MSG_TASK_START;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public class BizItemSchemaService implements EntityService<BizItemSchemaModel> {

    private QueryService queryService;


    public BizItemSchemaService(QueryService queryService) {
        this.queryService = queryService;
    }

    private static HashMap<String, BizItemSchemaModel> schemasCache = new HashMap<>();

    @Override
    public BizItemSchemaModel enrichMetadata(BizItemSchemaModel model) {
        if (model.getCreatedDate() == null) {
            model.setCreatedDate(new Date(System.currentTimeMillis()));
        }
        if (model.getTrackingId() == null) {
            model.setTrackingId(TrackingIdGenerator.generateUnique());
        }
        model.setModifiedDate(new Date(System.currentTimeMillis()));
        return model;
    }

    @Override
    public void validateOnSchema(BizItemSchemaModel model) {
        //not required
    }

    /**
     * Finds the BizItem itemType schema.
     *
     * @param targetItemType - the itemType name for which the schema is defined.
     * @param collection     - itemType that stores the schemas.
     * @return
     */
    @Override
    public BizItemSchemaModel findOne(String fieldName, Object targetItemType, String collection) {
        BizItemSchemaModel cachedSchema = schemasCache.get(String.valueOf(targetItemType));
        if (cachedSchema != null) {
            log.trace(String.format("Using validation schema [%s] from the cache.", targetItemType));
            return cachedSchema;
        } else {
            log.trace(String.format("Validation schema %s is not cached yet, performing DB lookup...", targetItemType));
            Query query = new Query().addCriteria(Criteria.where(SystemFiled.TargetItemType).is(targetItemType));
            return (BizItemSchemaModel) queryService.findOne(query, collection, BizItemSchemaModel.class);
        }
    }

    @Override
    public List<BizItemSchemaModel> find(Query query, String itemType) {
        return (List<BizItemSchemaModel>) queryService.find(query, itemType, BizItemModel.class);
    }

    @Override
    public BizItemSchemaModel save(BizItemSchemaModel schema) {
        enrichMetadata(schema);

        BizItemSchemaModel dbBizItemSchema = findOne(SystemFiled.TargetItemType, schema.getTargetItemType(), schema.getItemType());

        if (dbBizItemSchema != null) {//delete if already exists
            schema.setCreatedDate(dbBizItemSchema.getCreatedDate());//save initial creation data

            Query query = new Query().addCriteria(Criteria.where(SystemFiled.TargetItemType).is(schema.getTargetItemType()));
            queryService.delete(query, SystemCollection.BizItemSchemas);
            log.debug(String.format("Deleted old schema as it already exists. BizItem [%s] TrackingId [%s]", schema.getTargetItemType(), schema.getTrackingId()));
        }


        log.debug(String.format("Saved validation schema. BizItem [%s] TrackingId [%s]", schema.getTargetItemType(), schema.getTrackingId()));
        dbBizItemSchema = (BizItemSchemaModel) queryService.save(schema, schema.getItemType());

        schemasCache.put(dbBizItemSchema.getTargetItemType(), dbBizItemSchema);
        log.trace(String.format("Validation schema [%s] added in to cache.", dbBizItemSchema.getTargetItemType()));

        updateCollectionIndexes(dbBizItemSchema);
        return dbBizItemSchema;
    }

    @Override
    public void save(List<BizItemSchemaModel> models) {
    }


    public void clearCache() {
        log.info(String.format(MSG_TASK_START, "Removing BizItemsSchema cache"));
        schemasCache.clear();
        log.info(MSG_TASK_END_OK);
    }

    /**
     * Creates or deletes the indexes in the <code>targetItemType</code>.
     * If field is marked as <code>indexed="true"</code> in the schema - it will try to create it if already not created.
     * If field is marked as <code>indexed="false"</code> in the schema - it will delete the index.
     *
     * @param schemaModel - bizItem schema definition
     */
    private void updateCollectionIndexes(BizItemSchemaModel schemaModel) {
        String collection = schemaModel.getTargetItemType();

        if (!SystemCollection.Attachment.equalsIgnoreCase(collection) && !SystemCollection.Errors.equalsIgnoreCase(collection)) {
            log.info(String.format("Updating [%s] indexes based on schema definition from [%s].", schemaModel.getTargetItemType(), SystemCollection.BizItemSchemas));

            schemaModel.getProperties().forEach(prp -> {
                if (prp.isIndexed()) {
                    queryService.createIndex(collection, prp.getIndexName(), prp.isUnique());
                } else {
                    queryService.dropIndex(collection, prp.getIndexName());
                }
            });
        }

    }

}


