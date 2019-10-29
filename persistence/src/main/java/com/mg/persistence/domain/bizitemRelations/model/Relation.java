package com.mg.persistence.domain.bizitemRelations.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Relation {
    /**
     * Collection to jon with
     */
    private String from;

    /**
     * Field of the parent parent
     */
    private String localField;
    /**
     * Field of the <code>from</code> document
     */
    private String foreignField;

    boolean unwind;

    /**
     * Fields that needs to be excluded from the <code>from</code> document
     */
    private List<String> excludedFields = new ArrayList<>();
}
