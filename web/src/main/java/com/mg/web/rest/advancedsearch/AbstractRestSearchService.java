package com.mg.web.rest.advancedsearch;

import com.mg.persistence.services.QueryService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.conversions.Bson;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Dedicated to handle http request parameters.
 * Parameters are expected in a <@code>Map<String,String></@code> format
 * with the keys [jsonQuery, jsonProjection OR stage]
 * <p>
 * Created by andrmaim on 8/17/2019.
 */
public abstract class AbstractRestSearchService {

    private QueryService queryService;

    protected AbstractRestSearchService(QueryService queryService) {
        this.queryService = queryService;
    }


    /**
     * @param httpParams - rest params
     * @param itemType   - entity class based on witch the mongo itemType will be deducted to run the query
     * @param pageable   - <@code>Pageable</@code> object that holds pagination information
     * @return - Object with query results. Can be a Single or and Array of results
     */
    public Object executeQuery(Map<String, String> httpParams, String itemType,Class resultClass, Optional<Pageable> pageable) {
        validateParams(httpParams);

        DBObject query = httpParams.containsKey("jsonQuery") ? BasicDBObject.parse(httpParams.get("jsonQuery")) : null;
        DBObject projection = httpParams.containsKey("jsonProjection") ? BasicDBObject.parse(httpParams.get("jsonProjection")) : null;
        DBObject sort = httpParams.containsKey("jsonSort") ? BasicDBObject.parse(httpParams.get("jsonSort")) : null;
        Integer limit = httpParams.containsKey("jsonLimit") ? Integer.parseInt(httpParams.get("limit")) : 0;




        Object resultData;

        //simple query
        if (query != null) {
            resultData = queryService.find(itemType, resultClass, query, Optional.of(limit), Optional.ofNullable(projection), Optional.ofNullable(sort), pageable);
        }

        //pipeline execution
        else if (httpParams.containsKey("stage0")) {//there can be multiple stages stage0,stage1,...,stageN
            List<Bson> pipeline = new ArrayList<>();

            httpParams.forEach((key, stage) -> {
                if (key.startsWith("stage")) {
                    pipeline.add(BasicDBObject.parse(stage));
                }
            });
            resultData = queryService.aggregate(pipeline, itemType);
        } else {
            throw new UnsupportedOperationException("Unsupported httpParams passed. Supported query strategy params are [jsonQuery,stage1]. Found " + httpParams.keySet().toString());
        }

        return resultData;
    }

    //------------------------------Private Methods------------------------

    private void validateParams(Map<String, String> parameters) {
        String MSG_NO_PARAMS = "No parameters to execute the query";
        if (parameters == null) {
            throw new RuntimeException(MSG_NO_PARAMS);
        }

        parameters.remove("access_token");
        if (parameters.isEmpty()) {
            throw new RuntimeException(MSG_NO_PARAMS);
        }
    }

}
