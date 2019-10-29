
package com.mg.persistence.domain.enumeration.definition;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for EnumerationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EnumerationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Enum" type="{}EnumType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="itemType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="createAtRuntime" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnumerationType", propOrder = {
    "_enum"
})
public class EnumerationType {

    @XmlElement(name = "Enum")
    protected List<EnumType> _enum;
    @XmlAttribute(name = "itemType")
    protected String itemType;
    @XmlAttribute(name = "createAtRuntime")
    protected String createAtRuntime;
    @XmlAttribute(name = "type")
    protected String type;

    /**
     * Gets the value of the enum property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enum property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnum().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EnumType }
     * 
     * 
     */
    public List<EnumType> getEnum() {
        if (_enum == null) {
            _enum = new ArrayList<EnumType>();
        }
        return this._enum;
    }

    /**
     * Gets the value of the from property.
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
     * Sets the value of the from property.
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
     * Gets the value of the createAtRuntime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreateAtRuntime() {
        return createAtRuntime;
    }

    /**
     * Sets the value of the createAtRuntime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreateAtRuntime(String value) {
        this.createAtRuntime = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
