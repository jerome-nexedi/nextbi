package org.jasig.portal.portletpublishing.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

/**
 * <p>
 * Java class for step complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="step">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parameter" type="{https://source.jasig.org/schemas/uportal/portlet-publishing}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="preference" type="{https://source.jasig.org/schemas/uportal/portlet-publishing}preference" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="arbitrary-preferences" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "step", propOrder = { "name", "description", "parameters",
  "preferences", "arbitraryPreferences" })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class Step implements Serializable, Equals, HashCode, ToString {

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String name;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String description;

  @XmlElement(name = "parameter")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<Parameter> parameters;

  @XmlElement(name = "preference")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<Preference> preferences;

  @XmlElement(name = "arbitrary-preferences")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected Object arbitraryPreferences;

  /**
   * Gets the value of the name property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the value of the description property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getDescription() {
    return description;
  }

  /**
   * Sets the value of the description property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setDescription(String value) {
    this.description = value;
  }

  /**
   * Gets the value of the parameters property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the parameters property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getParameters().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Parameter }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<Parameter> getParameters() {
    if (parameters == null) {
      parameters = new ArrayList<Parameter>();
    }
    return this.parameters;
  }

  /**
   * Gets the value of the preferences property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the preferences property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getPreferences().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Preference }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<Preference> getPreferences() {
    if (preferences == null) {
      preferences = new ArrayList<Preference>();
    }
    return this.preferences;
  }

  /**
   * Gets the value of the arbitraryPreferences property.
   * 
   * @return possible object is {@link Object }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public Object getArbitraryPreferences() {
    return arbitraryPreferences;
  }

  /**
   * Sets the value of the arbitraryPreferences property.
   * 
   * @param value
   *          allowed object is {@link Object }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setArbitraryPreferences(Object value) {
    this.arbitraryPreferences = value;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String toString() {
    final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
    final StringBuilder buffer = new StringBuilder();
    append(null, buffer, strategy);
    return buffer.toString();
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public StringBuilder append(ObjectLocator locator, StringBuilder buffer,
    ToStringStrategy strategy) {
    strategy.appendStart(locator, this, buffer);
    appendFields(locator, buffer, strategy);
    strategy.appendEnd(locator, this, buffer);
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer,
    ToStringStrategy strategy) {
    {
      String theName;
      theName = this.getName();
      strategy.appendField(locator, this, "name", buffer, theName);
    }
    {
      String theDescription;
      theDescription = this.getDescription();
      strategy.appendField(locator, this, "description", buffer, theDescription);
    }
    {
      List<Parameter> theParameters;
      theParameters = (((this.parameters != null) && (!this.parameters.isEmpty())) ? this
        .getParameters()
        : null);
      strategy.appendField(locator, this, "parameters", buffer, theParameters);
    }
    {
      List<Preference> thePreferences;
      thePreferences = (((this.preferences != null) && (!this.preferences.isEmpty())) ? this
        .getPreferences()
        : null);
      strategy.appendField(locator, this, "preferences", buffer, thePreferences);
    }
    {
      Object theArbitraryPreferences;
      theArbitraryPreferences = this.getArbitraryPreferences();
      strategy.appendField(locator, this, "arbitraryPreferences", buffer,
        theArbitraryPreferences);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof Step)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    final Step that = ((Step) object);
    {
      String lhsName;
      lhsName = this.getName();
      String rhsName;
      rhsName = that.getName();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "name", lhsName),
        LocatorUtils.property(thatLocator, "name", rhsName), lhsName, rhsName)) {
        return false;
      }
    }
    {
      String lhsDescription;
      lhsDescription = this.getDescription();
      String rhsDescription;
      rhsDescription = that.getDescription();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "description",
        lhsDescription), LocatorUtils.property(thatLocator, "description",
        rhsDescription), lhsDescription, rhsDescription)) {
        return false;
      }
    }
    {
      List<Parameter> lhsParameters;
      lhsParameters = (((this.parameters != null) && (!this.parameters.isEmpty())) ? this
        .getParameters()
        : null);
      List<Parameter> rhsParameters;
      rhsParameters = (((that.parameters != null) && (!that.parameters.isEmpty())) ? that
        .getParameters()
        : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "parameters",
        lhsParameters), LocatorUtils.property(thatLocator, "parameters",
        rhsParameters), lhsParameters, rhsParameters)) {
        return false;
      }
    }
    {
      List<Preference> lhsPreferences;
      lhsPreferences = (((this.preferences != null) && (!this.preferences.isEmpty())) ? this
        .getPreferences()
        : null);
      List<Preference> rhsPreferences;
      rhsPreferences = (((that.preferences != null) && (!that.preferences.isEmpty())) ? that
        .getPreferences()
        : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "preferences",
        lhsPreferences), LocatorUtils.property(thatLocator, "preferences",
        rhsPreferences), lhsPreferences, rhsPreferences)) {
        return false;
      }
    }
    {
      Object lhsArbitraryPreferences;
      lhsArbitraryPreferences = this.getArbitraryPreferences();
      Object rhsArbitraryPreferences;
      rhsArbitraryPreferences = that.getArbitraryPreferences();
      if (!strategy.equals(LocatorUtils.property(thisLocator,
        "arbitraryPreferences", lhsArbitraryPreferences), LocatorUtils.property(
        thatLocator, "arbitraryPreferences", rhsArbitraryPreferences),
        lhsArbitraryPreferences, rhsArbitraryPreferences)) {
        return false;
      }
    }
    return true;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(Object object) {
    final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
    return equals(null, null, object, strategy);
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
    int currentHashCode = 1;
    {
      String theName;
      theName = this.getName();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name",
        theName), currentHashCode, theName);
    }
    {
      String theDescription;
      theDescription = this.getDescription();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "description", theDescription), currentHashCode, theDescription);
    }
    {
      List<Parameter> theParameters;
      theParameters = (((this.parameters != null) && (!this.parameters.isEmpty())) ? this
        .getParameters()
        : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "parameters", theParameters), currentHashCode, theParameters);
    }
    {
      List<Preference> thePreferences;
      thePreferences = (((this.preferences != null) && (!this.preferences.isEmpty())) ? this
        .getPreferences()
        : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "preferences", thePreferences), currentHashCode, thePreferences);
    }
    {
      Object theArbitraryPreferences;
      theArbitraryPreferences = this.getArbitraryPreferences();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "arbitraryPreferences", theArbitraryPreferences), currentHashCode,
        theArbitraryPreferences);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
