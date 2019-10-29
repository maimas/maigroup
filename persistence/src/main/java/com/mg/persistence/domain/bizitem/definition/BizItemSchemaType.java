
package com.mg.persistence.domain.bizitem.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BizItemSchemaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BizItemSchemaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="System" type="{}SystemType"/>
 *         &lt;element name="Content" type="{}ContentType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="itemType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="targetItemType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BizItemSchemaType", propOrder = {
    "system",
    "content"
})
public class BizItemSchemaType {

    @XmlElement(name = "System", required = true)
    protected SystemType system;
    @XmlElement(name = "Content", required = true)
    protected ContentType content;
    @XmlAttribute(name = "itemType")
    protected String itemType;
    @XmlAttribute(name = "targetItemType")
    protected String targetItemType;

    /**
     * Gets the value of the system property.
     * 
     * @return
     *     possible object is
     *     {@link SystemType }
     *     
     */
    public SystemType getSystem() {
        return system;
    }

    /**
     * Sets the value of the system property.
     * 
     * @param value
     *     allowed object is
     *     {@link SystemType }
     *     
     */
    public void setSystem(SystemType value) {
        this.system = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link ContentType }
     *     
     */
    public ContentType getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentType }
     *     
     */
    public void setContent(ContentType value) {
        this.content = value;
    }

    /**
     * Gets the value of the itemType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Sets the value of the itemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemType(String value) {
        this.itemType = value;
    }

    /**
     * Gets the value of the targetItemType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetItemType() {
        return targetItemType;
    }

    /**
     * Sets the value of the targetItemType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetItemType(String value) {
        this.targetItemType = value;
    }

}
