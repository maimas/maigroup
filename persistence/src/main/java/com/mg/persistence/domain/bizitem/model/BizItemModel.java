package com.mg.persistence.domain.bizitem.model;


import com.mg.persistence.domain.GenericModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedHashMap;

/**
 * Generic Persistence object wrapper.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BizItemModel extends GenericModel {


    /**
     * This field contains all the model business information, stored as key,value pairs.
     */
    private LinkedHashMap<String, Object> content = new LinkedHashMap<>();


    /**
     * This field is designed to hold the objects that might be attached to the <code>BizItemModel</code>
     *
     * <p>
     * Ex: User model might require Notifications, in this case we can add the notifications under <code>relations</code> field
     * and use it without querying multiple times the source.
     * <p>
     * Note: This field should never be persisted in the database.
     * <p>
     * Warning: @Transient - do not used as it creates unwanted behaviour
     */
    private LinkedHashMap<String, Object> relations = new LinkedHashMap<>();


    public BizItemModel() {
    }

    public BizItemModel(String itemType) {
        this.setItemType(itemType);
    }


    public BizItemModel putInContent(String key, Object value) {
        this.content.put(key, value);
        return this;
    }

    public Object getFromContent(String key) {
        return content.get(key);
    }
}
