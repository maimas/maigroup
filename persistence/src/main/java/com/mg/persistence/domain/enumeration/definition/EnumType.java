
package com.mg.persistence.domain.enumeration.definition;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for EnumType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnumType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="group" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enValue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="caption" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="captionTransKey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="descriptionTransKey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="envalue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="captiontranskey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="descriptiontranskey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnumType", propOrder = {
    "value"
})
public class EnumType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "group")
    protected String group;
    @XmlAttribute(name = "enValue")
    protected String enValue;
    @XmlAttribute(name = "caption")
    protected String caption;
    @XmlAttribute(name = "captionTransKey")
    protected String captionTransKey;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "descriptionTransKey")
    protected String descriptionTransKey;
    @XmlAttribute(name = "envalue")
    protected String envalue;
    @XmlAttribute(name = "captiontranskey")
    protected String captiontranskey;
    @XmlAttribute(name = "descriptiontranskey")
    protected String descriptiontranskey;

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the group property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the value of the group property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroup(String value) {
        this.group = value;
    }

    /**
     * Gets the value of the enValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnValue() {
        return enValue;
    }

    /**
     * Sets the value of the enValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnValue(String value) {
        this.enValue = value;
    }

    /**
     * Gets the value of the caption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Sets the value of the caption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaption(String value) {
        this.caption = value;
    }

    /**
     * Gets the value of the captionTransKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaptionTransKey() {
        return captionTransKey;
    }

    /**
     * Sets the value of the captionTransKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaptionTransKey(String value) {
        this.captionTransKey = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the descriptionTransKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionTransKey() {
        return descriptionTransKey;
    }

    /**
     * Sets the value of the descriptionTransKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionTransKey(String value) {
        this.descriptionTransKey = value;
    }

    /**
     * Gets the value of the envalue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnvalue() {
        return envalue;
    }

    /**
     * Sets the value of the envalue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnvalue(String value) {
        this.envalue = value;
    }

    /**
     * Gets the value of the captiontranskey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaptiontranskey() {
        return captiontranskey;
    }

    /**
     * Sets the value of the captiontranskey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaptiontranskey(String value) {
        this.captiontranskey = value;
    }

    /**
     * Gets the value of the descriptiontranskey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptiontranskey() {
        return descriptiontranskey;
    }

    /**
     * Sets the value of the descriptiontranskey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptiontranskey(String value) {
        this.descriptiontranskey = value;
    }

}
