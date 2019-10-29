package com.mg.persistence.domain.enumeration.reader;


import com.mg.persistence.domain.XmlMetadataUnmarshaller;
import com.mg.persistence.domain.enumeration.definition.EnumType;
import com.mg.persistence.domain.enumeration.definition.EnumerationType;
import com.mg.persistence.domain.enumeration.model.EnumModel;

import java.util.ArrayList;
import java.util.List;


public class EnumXmlReader extends XmlMetadataUnmarshaller<EnumerationType> {

    public EnumXmlReader() {
        super.setContextPath("com.mg.persistence.domain.enumeration.definition");
    }

    public List<EnumModel> toEnumModels(EnumerationType enumeration) {
        List<EnumModel> result = new ArrayList<>();

        enumeration.getEnum().forEach(en -> {
            EnumModel model = toEnumModel(en, enumeration.getType());
            model.setItemType(enumeration.getItemType());
            result.add(model);
        });
        return result;
    }


    private EnumModel toEnumModel(EnumType en, String dataType) {
        EnumModel model = new EnumModel();
        model.setGroup(en.getGroup());
        model.setValue(convert(en.getEnValue(), dataType));
        model.setCaption(en.getCaption());
        model.setCaptionTransKey(en.getCaptionTransKey());
        model.setDescription(en.getDescription());
        model.setDescriptionTransKey(en.getDescriptionTransKey());

        return model;
    }

}
