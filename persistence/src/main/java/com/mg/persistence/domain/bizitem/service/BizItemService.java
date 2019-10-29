package com.mg.persistence.domain.bizitem.service;


import com.mg.persistence.commons.TrackingIdGenerator;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.attachment.service.BizItemAttachmentService;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.exceptions.BizItemSchemaValidationException;
import com.mg.persistence.exceptions.ValidationSchemaNotFoundException;
import com.mg.persistence.services.QueryService;
import com.mg.persistence.services.base.EntityService;
import com.mg.persistence.validation.BizItemValidationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public class BizItemService extends BizItemAttachmentService implements EntityService<BizItemModel> {

    private QueryService queryService;
    private BizItemValidationService bizItemValidationService;


    public BizItemService(QueryService queryService, BizItemValidationService bizItemValidationService) {
        this.queryService = queryService;
        this.bizItemValidationService = bizItemValidationService;
    }


    @Override
    public BizItemModel enrichMetadata(BizItemModel model) {
        if (model.getCreatedDate() == null) {
            model.setCreatedDate(new Date(System.currentTimeMillis()));
        }
        if (model.getTrackingId() == null) {
            model.setTrackingId(TrackingIdGenerator.generateUnique());
        }
        //trim if present
        if (model.getItemType() != null) {
            model.setItemType(model.getItemType().trim());
        }
        if (model.getTrackingId() != null) {
            model.setTrackingId(model.getTrackingId().trim());
        }

        //remove relations from the model - this field is newer persit
        model.setRelations(null);

        model.setModifiedDate(new Date(System.currentTimeMillis()));
        return model;
    }

    @Override
    public void validateOnSchema(BizItemModel model) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        bizItemValidationService.validateAndThrow(model);
    }

    @Override
    public BizItemModel findOne(String fieldName, Object value, String collection) {
        Query query = new Query().addCriteria(Criteria.where(fieldName).is(value));

        return (BizItemModel) queryService.findOne(query, collection, BizItemModel.class);
    }

    @Override
    public List<BizItemModel> find(Query query, String itemType) {
        return (List<BizItemModel>) queryService.find(query, itemType, BizItemModel.class);
    }

    @Override
    public BizItemModel save(BizItemModel bizItem) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        enrichMetadata(bizItem);
        log.debug(String.format("Saving BizItem... Collection [%s] TrackingId [%s]", bizItem.getItemType(), bizItem.getTrackingId()));
        validateOnSchema(bizItem);
        return (BizItemModel) queryService.save(bizItem, bizItem.getItemType());
    }


    @Override
    public void save(List<BizItemModel> bizItems) throws BizItemSchemaValidationException {

        if (bizItems == null || bizItems.isEmpty()) {
            return;
        }

        String itemType = bizItems.get(0).getItemType();

        bizItems.forEach(this::enrichMetadata);
        log.debug(String.format("Saving BizItem in bulk... Collection [%s] Total items [%s]", itemType, bizItems.size()));

        List<Object> validModels = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        bizItems.forEach(it -> {
            try {
                validateOnSchema(it);
                validModels.add(it);

            } catch (Exception e) {
                errors.add(e.getLocalizedMessage());
            }
        });


        queryService.saveAll(validModels, itemType);

        if (!errors.isEmpty()) {
            log.error(String.format("Bulk save failed validation fro [%s] from [%s]", errors.size(), validModels.size()));
            throw new BizItemSchemaValidationException(itemType, errors);
        }
    }


    public BizItemModel findOne(Query query, String collection) {
        return (BizItemModel) queryService.findOne(query, collection, BizItemModel.class);
    }

    public List<BizItemModel> find(String fieldName, Object value, String collection) {
        Query query = new Query().addCriteria(Criteria.where(fieldName).is(value));

        return (List<BizItemModel>) queryService.find(query, collection, BizItemModel.class);
    }

    public BizItemModel findOneByRegex(String fieldName, String regex, String options, String collection) {
        Query query = new Query().addCriteria(Criteria.where(fieldName).regex(regex, options));

        return (BizItemModel) queryService.findOne(query, collection, BizItemModel.class);
    }

    public void delete(String itemType, String trackingId) {
        log.debug(String.format("Deleting BizItem... Collection [%s] TrackingId [%s]", itemType, trackingId));
        Query query = new Query().addCriteria(Criteria.where(SystemFiled.TrackingId).is(trackingId));
        queryService.delete(query, itemType);
    }

    //-------------------------------------------------------------------
    //-------------Attachment--------------------------------------------
    //-------------------------------------------------------------------

    public void saveAttachment(BizItemModel model) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        enrichMetadata(model);
        log.debug(String.format("Saving BizItem attachment... for Collection [%s] TrackingId [%s]", model.getItemType(), model.getTrackingId()));
        validateOnSchema(model);

        super.saveAtt(model);
    }

    public List<BizItemModel> findAttachments(String relatedItemType, Criteria criteria) {
        return findAtts(criteria, relatedItemType);
    }


}


