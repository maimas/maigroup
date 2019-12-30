package com.mg.persistence.services;

import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitemRelations.model.BizItemRelationsModel;
import com.mg.persistence.domain.bizitemRelations.service.BizItemRelationsService;
import com.mg.persistence.services.base.AbstractQueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * This service is designed to retrieve in aggregated format.
 * If the item has predefined relations, those will be retrieved automatically and attached to the model.
 * <p>
 * Example: Organization is te mode. Provider and User are models related to the Organization.
 * During the Organization retrieval, Provider and User will be automatically attached.
 */
@Service
@Log4j2
public class QueryAggregateService extends AbstractQueryService {

    @Autowired
    private BizItemRelationsService relationsService;

    public QueryAggregateService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }


    /**
     * Finds the items based on the criteria including the relations defined in the <code>BizItemRelationsModel</code>
     *
     * @param criteria       - search criteria
     * @param collectionName - target collection name
     * @return - list of <code>BizItemModel</code>
     */
    public List<BizItemModel> find(Criteria criteria, String collectionName) {
        final BizItemRelationsModel relationsModel = relationsService.findRelations(collectionName);
        final List<AggregationOperation> aggOperations = new ArrayList<>();

        //lookup relations
        if (relationsModel != null) {
            relationsModel.getRelationList().forEach(relation -> {
                String outFieldName = SystemFiled.Relations + "." + relation.getFrom();


                LookupOperation lockup = LookupOperation.newLookup()
                        .from(relation.getFrom())
                        .localField(relation.getLocalField())
                        .foreignField(relation.getForeignField())
                        .as(outFieldName);

                aggOperations.add(lockup);

                if (relation.isUnwind()) {
                    UnwindOperation unwind = unwind(outFieldName, true);
                    aggOperations.add(unwind);
                }
            });
        }

        //match
        aggOperations.add(match(criteria));

        //create pipeline
        Aggregation aggregation = newAggregation(aggOperations);

        List<BizItemModel> results = getMongoTemplate()
                .aggregate(aggregation, collectionName, BizItemModel.class)
                .getMappedResults();

        logQuery("SELECT", collectionName, aggOperations.toString(), System.currentTimeMillis(), String.valueOf(results.size()));
        return results;
    }
}
