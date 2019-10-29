package com.mg.persistence.domain.enumeration.model;

import com.mg.persistence.domain.GenericModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EnumModel extends GenericModel {

    private Object value;
    private String group, caption, captionTransKey, description, descriptionTransKey;

}
