package com.mg.persistence.domain.bizitemRelations.model;

import com.mg.persistence.domain.GenericModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BizItemRelationsModel extends GenericModel {
    private String targetItemType;
    private List<Relation> relationList = new ArrayList<>();
}
