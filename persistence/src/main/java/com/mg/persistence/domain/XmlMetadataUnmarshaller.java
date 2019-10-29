package com.mg.persistence.domain;

import com.mg.persistence.commons.PropertyUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;

@Log4j2
public abstract class XmlMetadataUnmarshaller<T> {


    private String contextPath;

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public T readXml(File file) {
        log.debug("Reading metadata XML: " + file.getAbsolutePath());

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);

            Unmarshaller u = context.createUnmarshaller();
            JAXBElement<?> element = (JAXBElement<?>) u.unmarshal(new FileInputStream(file));
            return (T) element.getValue();

        } catch (Exception e) {
            String errMsg = String.format("Failed to parse metadata file [%s] for Context [%s] ", file.getAbsolutePath(), contextPath);
            log.error(errMsg, e);
            throw new RuntimeException(errMsg);
        }
    }


    protected Object convert(String value, String type) {
        switch (type) {
            case "String":
                return value;

            case "Integer":
                return PropertyUtils.getAsInteger(value);

            case "Date":
                return PropertyUtils.getAsDateTime(value);

            case "Boolean":
                if (StringUtils.isEmpty(value)) {
                    return false;
                }
                return PropertyUtils.getAsBoolean(value);

            default:
                throw new RuntimeException(String.format("Failed to parse value -> [%s] to type -> [%s]", value, type));
        }
    }


}
