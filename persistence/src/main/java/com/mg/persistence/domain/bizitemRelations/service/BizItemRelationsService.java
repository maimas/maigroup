package com.mg.persistence.domain.bizitemRelations.service;


import com.mg.persistence.commons.TrackingIdGenerator;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.bizitem.model.BizItemSchemaModel;
import com.mg.persistence.domain.bizitem.service.BizItemSchemaService;
import com.mg.persistence.domain.bizitemRelations.model.BizItemRelationsModel;
import com.mg.persistence.domain.bizitemRelations.model.Relation;
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
import static com.mg.persistence.domain.SystemCollection.BizItemRelations;
import static com.mg.persistence.domain.SystemCollection.BizItemSchemas;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public class BizItemRelationsService implements EntityService<BizItemRelationsModel> {

    private QueryService queryService;
    private BizItemSchemaService schemaService;
    private static HashMap<String, BizItemRelationsModel> relationsCache = new HashMap<>();


    public BizItemRelationsService(BizItemSchemaService schemaService, QueryService queryService) {
        this.queryService = queryService;
        this.schemaService = schemaService;
    }


    @Override
    public BizItemRelationsModel enrichMetadata(BizItemRelationsModel model) {
        if (model.getCreatedDate() == null) {
            model.setCreatedDate(new Date(System.currentTimeMillis()));
        }
        if (model.getTrackingId() == null) {
            model.setTrackingId(TrackingIdGenerator.generateUnique());
        }
        model.setModifiedDate(new Date(System.currentTimeMillis()));
        return model;
    }

    /**
     * Validates if the relation definition is valid.
     *
     * @param model - bizItem to validate
     * @throws BizItemRelationException - If relation properties not present in the corresponding schema.
     */
    @Override
    public void validateOnSchema(BizItemRelationsModel model) throws BizItemRelationException {
        validateItemType(model.getTargetItemType());
        validateRelations(model);
    }

    /**
     * Finds the BizItem from schema.
     *
     * @param targetItemType - the from name for which the schema is defined.
     * @param collection     - from that stores the schemas.
     * @return
     */
    @Override
    public BizItemRelationsModel findOne(String fieldName, Object targetItemType, String collection) {
        BizItemRelationsModel cachedSchema = relationsCache.get(String.valueOf(targetItemType));
        if (cachedSchema != null) {
            log.trace(String.format("Using relations schema [%s] from the cache.", targetItemType));
            return cachedSchema;
        } else {
            log.trace(String.format("Relations schema %s is not cached yet, performing DB lookup...", targetItemType));
            Query query = new Query().addCriteria(Criteria.where(SystemFiled.TargetItemType).is(targetItemType));
            return (BizItemRelationsModel) queryService.findOne(query, collection, BizItemRelationsModel.class);
        }
    }


    public BizItemRelationsModel findRelations(String targetItemType) {
        return findOne(SystemFiled.TargetItemType, targetItemType, BizItemRelations);
    }

    @Override
    public List<BizItemRelationsModel> find(Query query, String targetItemType) {
        return (List<BizItemRelationsModel>) queryService.find(query, targetItemType, BizItemRelationsModel.class);
    }

    @Override
    public BizItemRelationsModel save(BizItemRelationsModel itemRelationsModel) throws BizItemRelationException {
        enrichMetadata(itemRelationsModel);
        validateOnSchema(itemRelationsModel);

        BizItemRelationsModel dbRelationsModel = findOne(SystemFiled.ItemType, itemRelationsModel.getTargetItemType(), itemRelationsModel.getItemType());

        if (dbRelationsModel != null) {//delete if already exists
            itemRelationsModel.setCreatedDate(dbRelationsModel.getCreatedDate());//save initial creation data

            Query query = new Query().addCriteria(Criteria.where(SystemFiled.TargetItemType).is(itemRelationsModel.getTargetItemType()));
            queryService.delete(query, itemRelationsModel.getItemType());
            log.debug(String.format("Deleted old relations schema as it already exists. BizItem [%s] TrackingId [%s]", itemRelationsModel.getTargetItemType(), itemRelationsModel.getTrackingId()));
        }

        log.debug(String.format("Saved relations schema. BizItem [%s] TrackingId [%s]", itemRelationsModel.getTargetItemType(), itemRelationsModel.getTrackingId()));
        dbRelationsModel = (BizItemRelationsModel) queryService.save(itemRelationsModel, itemRelationsModel.getItemType());

        relationsCache.put(dbRelationsModel.getTargetItemType(), dbRelationsModel);
        log.trace(String.format("Relations schema [%s] added in to cache.", dbRelationsModel.getTargetItemType()));

        return dbRelationsModel;
    }

    @Override
    public void save(List<BizItemRelationsModel> models) {
    }


    //-----------------------------Private Methods----------------------------------

    public void clearCache() {
        log.info(String.format(MSG_TASK_START, "Removing BizItemRelations cache"));
        relationsCache.clear();
        log.info(MSG_TASK_END_OK);
    }


    private void validateItemType(String itemType) throws BizItemRelationException {
        BizItemSchemaModel targetItemSchema = getBizItemSchema(itemType);

        if (targetItemSchema == null) {
            throw new BizItemRelationException(String.format("Invalid Relation. Biz item schema not found [%s].", itemType));
        }
    }

    private void validateRelations(BizItemRelationsModel relationModel) throws BizItemRelationException {
        List<Relation> relations = relationModel.getRelationList();

        for (Relation relation : relations) {
            validateItemType(relation.getFrom());

            try {
                String localField = resolvePropertyName(relation.getLocalField());
                BizItemSchemaModel targetItemSchema = getBizItemSchema(relationModel.getTargetItemType());
                targetItemSchema.getProperty(localField);

                String foreignField = resolvePropertyName(relation.getForeignField());
                BizItemSchemaModel relationSchema = getBizItemSchema(relation.getFrom());
                relationSchema.getProperty(foreignField);

            } catch (RuntimeException e) {
                String error = String.format("Invalid Relation Schema. TargetItemType [%s]. Relation [%s]." + e.getLocalizedMessage(),
                        relationModel.getTargetItemType(), relation.getFrom());
                throw new RuntimeException(error);
            }
        }
    }

    private BizItemSchemaModel getBizItemSchema(String itemType) {
        return schemaService.findOne(SystemFiled.ItemType, itemType, BizItemSchemas);
    }

    private String resolvePropertyName(String prp) {

        if (prp.contains(".")) {
            String[] tokens = prp.split("\\.");
            return tokens[tokens.length - 1];
        }
        return prp;
    }
}


