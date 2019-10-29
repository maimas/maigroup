package com.mg.persistence.domain.bizitemRelations.reader;


import com.mg.persistence.domain.SystemCollection;
import com.mg.persistence.domain.XmlMetadataUnmarshaller;
import com.mg.persistence.domain.bizitemRelations.definition.BizItemRelationsType;
import com.mg.persistence.domain.bizitemRelations.definition.ExcludeType;
import com.mg.persistence.domain.bizitemRelations.model.BizItemRelationsModel;
import com.mg.persistence.domain.bizitemRelations.model.Relation;

import java.util.List;

public class BizItemRelationsXmlReader extends XmlMetadataUnmarshaller<BizItemRelationsType> {

    public BizItemRelationsXmlReader() {
        super.setContextPath("com.mg.persistence.domain.bizitemRelations.definition");
    }

    public BizItemRelationsModel parse(BizItemRelationsType relationsType) {
        BizItemRelationsModel relations = new BizItemRelationsModel();
        relations.setItemType(SystemCollection.BizItemRelations);
        relations.setTargetItemType(relationsType.getTargetItemType());

        relationsType.getRelation().forEach(it -> {
            Relation relation = new Relation();
            relation.setFrom(it.getFrom());
            relation.setLocalField(it.getLocalField());
            relation.setForeignField(it.getForeignField());
            relation.setUnwind(Boolean.valueOf(it.getUnwind()));
            setRelationExcludedFields(it.getExclude(), relation);

            relations.getRelationList().add(relation);
        });

        return relations;
    }


    private void setRelationExcludedFields(List<ExcludeType> content, Relation relation) {
        content.forEach(element -> {
            relation.getExcludedFields().add(element.getField());
        });
    }

}
