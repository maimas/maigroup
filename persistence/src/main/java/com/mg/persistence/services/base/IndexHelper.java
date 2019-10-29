package com.mg.persistence.services.base;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class IndexHelper {

    private MongoTemplate mongoTemplate;

    public IndexHelper(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void createIndex(String collectionName, String fieldName, boolean unique) {
        createIndex(collectionName, fieldName, unique, Sort.Direction.ASC);
    }

    public void createIndex(String collectionName, String fieldName, boolean unique, Sort.Direction direction) {
        IndexInfo dbIndex = getIndexInfo(collectionName, fieldName);

        if (dbIndex != null) {
            if (dbIndex.isUnique() != unique) {//exists but with different configuration - delete
                log.debug(String.format("- Dropping index as it was changed. Collection [%s] field [%s] unique [%s]", collectionName, fieldName, unique));
                dropIndex(collectionName, fieldName);
            }
        }

        Index index = unique ?
                new Index().unique().background().on(fieldName, direction) :
                new Index().background().on(fieldName, direction);

        mongoTemplate.indexOps(collectionName).ensureIndex(index);
        log.debug(String.format("- Index created on itemType [%s] field [%s]", collectionName, fieldName));
    }

    public void dropIndex(String collectionName, String fieldName) {

        IndexInfo indexInfo = getIndexInfo(collectionName, fieldName);

        if (indexInfo != null && !isIdField(fieldName)) {
            mongoTemplate.indexOps(collectionName).dropIndex(indexInfo.getName());
            log.debug(String.format("Index deleted on itemType [%s] field [%s]", collectionName, fieldName));
        }
    }

    private boolean isIdField(String fieldName) {
        return "_id".equalsIgnoreCase(fieldName) || "_id".equalsIgnoreCase(fieldName) ||
                "id_".equalsIgnoreCase(fieldName) || "_id_".equalsIgnoreCase(fieldName);
    }

//    private boolean idxExistsWithDifferentOptions(String collection, String fileName, boolean unique) {
//        IndexInfo indexInfo = getIndexInfo(collection, fileName);
//        if (indexInfo != null) {
//            return indexInfo.isUnique() != unique;
//        }
//        return false;
//    }

    private IndexInfo getIndexInfo(String collection, String fieldName) {
        List<IndexInfo> collect = mongoTemplate.indexOps(collection).getIndexInfo().stream()
                .filter(idx -> (idx.getName().startsWith(fieldName + "_"))).collect(Collectors.toList());

        return collect.isEmpty() ? null : collect.get(0);
    }

}
