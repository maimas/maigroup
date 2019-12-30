package com.mg.persistence.services;

import com.mg.persistence.services.base.AbstractQueryService;
import com.mg.persistence.services.base.IndexHelper;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.log4j.Log4j2;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class QueryService extends AbstractQueryService {

    @Autowired
    public QueryService(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }

    public long count(DBObject query, String collectionName) {
        long startTime = System.currentTimeMillis();

        long count = getCollection(collectionName).countDocuments((Bson) query);
        logQuery("COUNT", collectionName, query.toString(), startTime, String.valueOf(count));
        return count;
    }

    public Object aggregate(List<Bson> pipeline, String collectionName) {
        long startTime = System.currentTimeMillis();

        MongoCollection collection = getCollection(collectionName);
        MongoCursor mongoCursor = collection.aggregate(pipeline).iterator();
        List<Object> results = collectFromMongoCursor(mongoCursor);

        logQueryStats(collectionName, pipeline.toString(), startTime, Optional.of(results));
        return results;
    }


    /**
     * @param collectionName
     * @param query
     * @param projection
     * @param sort
     * @param pageable
     * @param limit          - if zero then it is considered as unlimited
     * @return
     */
    public Object find(String collectionName,
                       Class resultClass,
                       DBObject query,
                       Optional<Integer> limit,
                       Optional<DBObject> projection,
                       Optional<DBObject> sort,
                       Optional<Pageable> pageable) {

        long startTime = System.currentTimeMillis();

        MongoCollection collection = getCollection(collectionName);
        FindIterable iterable = collection.find((Bson) query);

        projection.ifPresent(it -> iterable.projection((Bson) it));
        pageable.ifPresent(pageable1 -> iterable.skip(pageable1.getPageNumber() * pageable1.getPageNumber()).limit(pageable1.getPageSize()));
        sort.ifPresent(dbObject -> iterable.sort((Bson) dbObject));

        if (limit.isPresent() && !pageable.isPresent()) {
            iterable.limit(limit.orElse(0));
        }

        List data = convertToEntity(iterable, resultClass);
        String queryString = String.format("Query: %s .Projection: %s.", query.toString(), (projection.isPresent() ? projection.toString() : ""));

        if (!pageable.isPresent()) {
            logQueryStats(collectionName, queryString, startTime, Optional.empty());
            return data;

        } else {
            long count = count(query, collectionName);
            PageImpl page = new PageImpl<>(data, pageable.get(), count);
            logQueryStats(collectionName, queryString, startTime, Optional.of(page));
            return page;
        }
    }


    public List find(Query query, String collectionName, Class resultClass) {
        long startTime = System.currentTimeMillis();
        MongoCollection collection = getCollection(collectionName);
        FindIterable iterable = collection.find(query.getQueryObject());
        List data = convertToEntity(iterable, resultClass);//  collectFromMongoCursor(iterable.iterator());


        String queryString = String.format("Collection %s . Query: %s . Results %s", collectionName, query.toString(), data.size());
        logQueryStats(collectionName, queryString, startTime, Optional.empty());
        return data;

    }

    public Object findOne(Query query, String collectionName, Class resultClass) {
        List list = find(query, collectionName, resultClass);
        return list.stream().findFirst().orElse(null);
    }


    public Object save(Object item, String collectionName) {
        return getMongoTemplate().save(item, collectionName);
    }

    public void saveAll(List<Object> items, String collectionName) {

        items.forEach(it -> {
            getMongoTemplate().save(it, collectionName);
        });
    }


    public void delete(Query query, String collectionName) {
        long startTime = System.currentTimeMillis();
        DeleteResult deleteResult = getCollection(collectionName).deleteMany(query.getQueryObject());
        logQuery("DELETE", collectionName, query.toString(), startTime, String.valueOf(deleteResult.getDeletedCount()));
    }


    public void createIndex(String collectionName, String fieldName, boolean unique) {
        new IndexHelper(getMongoTemplate()).createIndex(collectionName, fieldName, unique);
    }

    public void createIndex(String collectionName, String fieldName, boolean unique, Sort.Direction direction) {
        new IndexHelper(getMongoTemplate()).createIndex(collectionName, fieldName, unique, direction);
    }

    public void dropIndex(String collectionName, String fieldName) {
        new IndexHelper(getMongoTemplate()).dropIndex(collectionName, fieldName);

    }

//----------------------Private Methods--------------------------------------------

    private List<Object> collectFromMongoCursor(MongoCursor cursor) {
        List<Object> result = new ArrayList<>();

        while (cursor.hasNext()) {
            result.add(cursor.next());
        }
        return result;
    }


    private void logQueryStats(String collectionName, String query, long start, Optional<Object> result) {

        String count = "N/A";
        if (result.isPresent()) {
            if (result.get() instanceof Long) {
                count = String.valueOf(result.get());

            }
            if (result.get() instanceof List) {
                int size = ((List) result.get()).size();
                count = String.valueOf(size);


            } else if (result.get() instanceof Pageable) {
                int pageSize = ((Pageable) result.get()).getPageSize();
                int pageCount = ((Pageable) result.get()).getPageNumber();

                count = String.format("RESULTS [%s] PAGE [%s]", pageSize, pageCount);
            }
        }


        logQuery("SELECT", collectionName, query, start, count);
    }


}
