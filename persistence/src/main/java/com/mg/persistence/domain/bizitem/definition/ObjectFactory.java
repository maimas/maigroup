
package com.mg.persistence.domain.bizitem.definition;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.platform.core.persistence.domain.bizitem.definition package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _BizItemSchema_QNAME = new QName("", "BizItemSchema");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.platform.core.persistence.domain.bizitem.definition
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BizItemSchemaType }
     * 
     */
    public BizItemSchemaType createBizItemSchemaType() {
        return new BizItemSchemaType();
    }

    /**
     * Create an instance of {@link ContentType }
     * 
     */
    public ContentType createContentType() {
        return new ContentType();
    }

    /**
     * Create an instance of {@link SystemType }
     * 
     */
    public SystemType createSystemType() {
        return new SystemType();
    }

    /**
     * Create an instance of {@link PropertyType }
     * 
     */
    public PropertyType createPropertyType() {
        return new PropertyType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BizItemSchemaType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BizItemSchema")
    public JAXBElement<BizItemSchemaType> createBizItemSchema(BizItemSchemaType value) {
        return new JAXBElement<BizItemSchemaType>(_BizItemSchema_QNAME, BizItemSchemaType.class, null, value);
    }

}
