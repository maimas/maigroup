
package com.mg.persistence.domain.bizitemRelations.definition;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.platform.core.persistence.domain.bizitemRelations.definition package. 
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

    private final static QName _BizItemRelations_QNAME = new QName("", "BizItemRelations");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.platform.core.persistence.domain.bizitemRelations.definition
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BizItemRelationsType }
     * 
     */
    public BizItemRelationsType createBizItemRelationsType() {
        return new BizItemRelationsType();
    }

    /**
     * Create an instance of {@link RelationType }
     * 
     */
    public RelationType createRelationType() {
        return new RelationType();
    }

    /**
     * Create an instance of {@link ExcludeType }
     * 
     */
    public ExcludeType createExcludeType() {
        return new ExcludeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BizItemRelationsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "BizItemRelations")
    public JAXBElement<BizItemRelationsType> createBizItemRelations(BizItemRelationsType value) {
        return new JAXBElement<BizItemRelationsType>(_BizItemRelations_QNAME, BizItemRelationsType.class, null, value);
    }

}
