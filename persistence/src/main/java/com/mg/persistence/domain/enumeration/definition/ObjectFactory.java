
package com.mg.persistence.domain.enumeration.definition;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.platform.core.persistence.domain.definition.ex package.
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

    private final static QName _Enumeration_QNAME = new QName("", "Enumeration");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.platform.core.persistence.domain.definition.ex
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EnumerationType }
     * 
     */
    public EnumerationType createEnumerationType() {
        return new EnumerationType();
    }

    /**
     * Create an instance of {@link EnumType }
     * 
     */
    public EnumType createEnumType() {
        return new EnumType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnumerationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Enumeration")
    public JAXBElement<EnumerationType> createEnumeration(EnumerationType value) {
        return new JAXBElement<EnumerationType>(_Enumeration_QNAME, EnumerationType.class, null, value);
    }

}
