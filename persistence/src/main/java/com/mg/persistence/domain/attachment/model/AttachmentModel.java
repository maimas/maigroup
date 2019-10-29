package com.mg.persistence.domain.attachment.model;


import com.mg.persistence.domain.GenericModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Generic Persistence object wrapper.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AttachmentModel extends GenericModel {

    private byte[] data;
    private String name;

    public AttachmentModel() {
    }

    public AttachmentModel(String itemType) {
        this.setItemType(itemType);
    }

}
