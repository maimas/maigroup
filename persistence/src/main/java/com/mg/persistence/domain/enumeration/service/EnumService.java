package com.mg.persistence.domain.enumeration.service;


import com.mg.persistence.commons.TrackingIdGenerator;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.enumeration.model.EnumModel;
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

/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public class EnumService implements EntityService<EnumModel> {

    private QueryService queryService;

    public EnumService(QueryService queryService) {
        this.queryService = queryService;
    }

    //    todo: enhance cache as there should be a limit or expiration
    private static HashMap<String, EnumModel> enumCache = new HashMap<>();

    @Override
    public EnumModel enrichMetadata(EnumModel model) {
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
    public void validateOnSchema(EnumModel model) {
        //not required for Enum
    }


    @Override
    public EnumModel findOne(String fieldName, Object enumValue, String itemType) {
        Query query = new Query().addCriteria(Criteria.where(fieldName).is(enumValue));

        EnumModel dbEnum = (EnumModel) queryService.findOne(query, itemType, EnumModel.class);
        if (dbEnum != null) {
            enumCache.put(dbEnum.getTrackingId(), dbEnum);
        }
        return dbEnum;
    }

    /**
     * @param query    - search criteria
     * @param itemType - enum name to search for
     * @return
     */
    @Override
    public List<EnumModel> find(Query query, String itemType) {
        return queryService.find(query, itemType, EnumModel.class);
    }

    @Override
    public EnumModel save(EnumModel enumModel) {
        enrichMetadata(enumModel);
        validateOnSchema(enumModel);

        EnumModel cachedEnum = enumCache.get(enumModel.getTrackingId());

        EnumModel existentEnum = (cachedEnum != null) ? cachedEnum : findOne(SystemFiled.Value, enumModel.getValue(), enumModel.getItemType());

        if (existentEnum != null) {//exists - update values
            existentEnum.setGroup(enumModel.getGroup());
            existentEnum.setValue(enumModel.getValue());
            existentEnum.setCaption(enumModel.getCaption());
            existentEnum.setCaptionTransKey(enumModel.getCaptionTransKey());
            existentEnum.setDescription(enumModel.getDescription());
            existentEnum.setDescriptionTransKey(enumModel.getDescriptionTransKey());
            existentEnum = (EnumModel) queryService.save(existentEnum, existentEnum.getItemType());

            enumCache.put(existentEnum.getTrackingId(), existentEnum);//update cache
            log.debug(String.format("Updated enum. Collection [%s] caption [%s]", existentEnum.getItemType(), existentEnum.getCaption()));
            return existentEnum;

        } else {//create from scratch
            EnumModel dbEnum = (EnumModel) queryService.save(enumModel, enumModel.getItemType());
            enumCache.put(dbEnum.getTrackingId(), dbEnum);
            log.debug(String.format("Created enum. Collection [%s] caption [%s]", dbEnum.getItemType(), dbEnum.getCaption()));
            return dbEnum;
        }

    }

    @Override
    public void save(List<EnumModel> models) {
    }
}


