
package com.mg.persistence.domain.bizitemRelations.definition;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RelationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Exclude" type="{}ExcludeType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="from" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="foreignField" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="localField" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="unwind" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RelationType", propOrder = {
    "exclude"
})
public class RelationType {

    @XmlElement(name = "Exclude")
    protected List<ExcludeType> exclude;
    @XmlAttribute(name = "from")
    protected String from;
    @XmlAttribute(name = "foreignField")
    protected String foreignField;
    @XmlAttribute(name = "localField")
    protected String localField;
    @XmlAttribute(name = "unwind")
    protected String unwind;

    /**
     * Gets the value of the exclude property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the exclude property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getExclude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExcludeType }
     * 
     * 
     */
    public List<ExcludeType> getExclude() {
        if (exclude == null) {
            exclude = new ArrayList<ExcludeType>();
        }
        return this.exclude;
    }

    /**
     * Gets the value of the from property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrom(String value) {
        this.from = value;
    }

    /**
     * Gets the value of the foreignField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeignField() {
        return foreignField;
    }

    /**
     * Sets the value of the foreignField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeignField(String value) {
        this.foreignField = value;
    }

    /**
     * Gets the value of the localField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalField() {
        return localField;
    }

    /**
     * Sets the value of the localField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalField(String value) {
        this.localField = value;
    }

    /**
     * Gets the value of the unwind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnwind() {
        return unwind;
    }

    /**
     * Sets the value of the unwind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnwind(String value) {
        this.unwind = value;
    }

}
