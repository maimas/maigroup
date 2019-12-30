package com.mg.persistence.services.base;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class AbstractQueryService {

    private MongoTemplate mongoTemplate;

    public AbstractQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    public MongoCollection getCollection(Class clazz) {
        return getCollection(clazz.getSimpleName());
    }

    public MongoCollection getCollection(String collection) {
        if (!mongoTemplate.collectionExists(collection)) {
            log.warn("Collection not found, this can lead to unexpected results. Collection name: " + collection);
        }
        return mongoTemplate.getCollection(collection);
    }


    public List convertToEntity(FindIterable<Document> iterable, Class entityClass) {
        List result = new ArrayList();
        for (Document doc : iterable) {
            result.add(mongoTemplate.getConverter().read(entityClass, doc));
        }
        return result;
    }
//
//    public Object convertToEntity(Document document, Class entityClass) {
//        return mongoTemplate.getConverter().read(entityClass, document);
//    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }


    protected void logQuery(String operation, String collectionName, String query, long start, String count) {
        long duration = getDuration(start, System.currentTimeMillis());
        log.debug(String.format("%s executed: Entity=[%s] Duration=[%s ms] Results: [%s] [%s]",
                operation, collectionName, duration, count, query));
    }

    protected long getDuration(long start, long end) {
        return (end - start);
    }
}
