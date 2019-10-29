package com.mg.web.rest;


import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitem.service.BizItemService;
import com.mg.persistence.exceptions.BizItemSchemaValidationException;
import com.mg.persistence.exceptions.ValidationSchemaNotFoundException;
import com.mg.persistence.services.QueryAggregateService;
import com.mg.web.rest.advancedsearch.RestSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@RestController
public abstract class AbstractBizItemRestController {


    @Autowired
    private QueryAggregateService queryAggregateService;

    @Autowired
    protected RestSearchService restSearchService;

    @Autowired
    private BizItemService bizItemService;

    public ResponseEntity save(BizItemModel bizItem, String itemType) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        failOnBizItemTypeMismatch(bizItem, itemType);
        bizItemService.save(bizItem);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity save(List<BizItemModel> bizItemsBatch, String itemType) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        failOnBizItemTypeMismatch(bizItemsBatch, itemType);
        bizItemService.save(bizItemsBatch);
        return ResponseEntity.ok().build();
    }


    public ResponseEntity delete(String itemType, String trackingId) {
        bizItemService.delete(itemType, trackingId);
        return ResponseEntity.ok().build();
    }


    protected ResponseEntity get(String itemType, String fieldName, Object fieldValue) {
        List<BizItemModel> models = queryAggregateService.find(Criteria.where(fieldName).is(fieldValue), itemType);
        return ResponseEntity.ok(models);
    }

    protected ResponseEntity get(String itemType, Criteria criteria) {
        List<BizItemModel> models = queryAggregateService.find(criteria, itemType);
        return ResponseEntity.ok(models);
    }

    protected ResponseEntity<List<BizItemModel>> getAttachments(String itemType, Criteria criteria) {
        List<BizItemModel> models = bizItemService.findAttachments(itemType, criteria);
        return ResponseEntity.ok(models);
    }

    protected ResponseEntity saveAttachment(BizItemModel attachment) throws ValidationSchemaNotFoundException, BizItemSchemaValidationException {
        bizItemService.saveAttachment(attachment);
        return new ResponseEntity(HttpStatus.OK);
    }


    public ResponseEntity search(String itemType, Map<String, String> queryParams, Pageable pageable) {
        Object results = restSearchService.search(queryParams, itemType, BizItemModel.class, Optional.of(pageable));
        return ResponseEntity.ok(results);
    }


    private void failOnBizItemTypeMismatch(List<BizItemModel> bizItems, String expectedType) {
        bizItems.forEach(it -> failOnBizItemTypeMismatch(it, expectedType));
    }

    private void failOnBizItemTypeMismatch(BizItemModel bizItem, String expectedType) {
        String msg = String.format("Invalid BizIteType [%s] passed. Expected [%s]", bizItem.getItemType(), expectedType);
        Assert.isTrue(Objects.equals(bizItem.getItemType(), expectedType), msg);
    }

    protected BizItemService getBizitemService() {
        return bizItemService;
    }

}
