package com.mg.persistence.domain.attachment.service;


import com.mg.persistence.config.MongoDBConfig;
import com.mg.persistence.domain.SystemCollection;
import com.mg.persistence.domain.SystemFiled;
import com.mg.persistence.domain.bizitem.model.BizItemModel;
import com.mg.persistence.domain.bizitem.service.BizItemConverterService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrmaim on 8/17/2019.
 */
@Service
@Log4j2
public abstract class BizItemAttachmentService {

    @Autowired
    private MongoDBConfig mongoDBConfig;
    @Autowired
    private BizItemConverterService converterService;

    protected void saveAtt(BizItemModel model) {
        if (model.getItemType() == null) {
            model.setItemType(SystemCollection.Attachment);
        }

        converterService.castContent(model);

        GridFS collectionFS = getGridFs((String) model.get(SystemFiled.RelatedItemType));
        ByteArrayInputStream dataStream = new ByteArrayInputStream((byte[]) model.get(SystemFiled.Data));

        GridFSInputFile fsFile = collectionFS.createFile(dataStream, true);
        fsFile.setFilename((String) model.get(SystemFiled.FileName));
        fsFile.setContentType((String) model.get(SystemFiled.DataType));

        // metadata
        fsFile.put(SystemFiled.TrackingId, model.getTrackingId());
        fsFile.put(SystemFiled.CreatedDate, model.getCreatedDate());
        fsFile.put(SystemFiled.ModifiedDate, model.getModifiedDate());
        fsFile.put(SystemFiled.RelatedItemTID, model.get(SystemFiled.RelatedItemTID));
        fsFile.put(SystemFiled.RelatedItemType, model.get(SystemFiled.RelatedItemType));
        fsFile.put(SystemFiled.ItemType, model.getItemType());

        if (model.get_id() != null) {//update the attachment id has an id
            fsFile.setId(new ObjectId(model.get_id()));
        }

        fsFile.save();
    }

    protected List<BizItemModel> findAtts(Criteria criteria, String relatedItemType) {
        DBObject dbObject = BasicDBObject.parse(criteria.getCriteriaObject().toJson());
        List<GridFSDBFile> dbFiles = getGridFs(relatedItemType).find(dbObject);


        return toBizItemModels(dbFiles);
    }


//    GridFSDBFile findAtt(String id, String itemType) {
//        String collectionName = getAttCollectionName(new BizItemModel(itemType));
//        GridFSDBFile gridFSDBFile = getGridFs(collectionName).find(new ObjectId(id));
//
//        return gridFSDBFile
//    }


    private List<BizItemModel> toBizItemModels(List<GridFSDBFile> dbFiles) {
        List<BizItemModel> attList = new ArrayList<>();
        dbFiles.parallelStream().forEach(it -> {
            try {
                attList.add(toBizItemModel(it));
            } catch (IOException e) {
                log.error(e);
                e.printStackTrace();
            }
        });

        return attList;
    }

    private BizItemModel toBizItemModel(GridFSDBFile file) throws IOException {
        BizItemModel model = new BizItemModel((String) file.get(SystemFiled.ItemType));

        model.set_id(file.getId().toString());
        model.setTrackingId((String) file.get(SystemFiled.TrackingId));

        model.set(SystemFiled.CreatedDate, file.get(SystemFiled.CreatedDate));
        model.set(SystemFiled.ModifiedDate, file.get(SystemFiled.ModifiedDate));
        model.set(SystemFiled.RelatedItemTID, file.get(SystemFiled.RelatedItemTID));
        model.set(SystemFiled.RelatedItemType, file.get(SystemFiled.RelatedItemType));
        model.set(SystemFiled.Data, IOUtils.toByteArray(file.getInputStream()));
        model.set(SystemFiled.DataType, file.getContentType());
        model.set(SystemFiled.FileName, file.getFilename());

        return model;
    }


    private GridFS getGridFs(String bucketName) {
        return new GridFS(new DB(mongoDBConfig.getClient(), mongoDBConfig.getDatabaseName()), bucketName);
    }
}


