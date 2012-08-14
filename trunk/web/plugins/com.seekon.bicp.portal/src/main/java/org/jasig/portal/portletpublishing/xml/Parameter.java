package org.jasig.portal.portletpublishing.xml;

import java.io.Serializable;
import javax.annotation.Generated;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
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
 * Java class for parameter complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="parameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="label" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="example" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element ref="{https://source.jasig.org/schemas/uportal/portlet-publishing}parameter-input"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parameter", propOrder = { "name", "label", "description",
  "example", "parameterInput" })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class Parameter implements Serializable, Equals, HashCode, ToString {

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String name;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String label;

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String description;

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String example;

  @XmlElementRef(name = "parameter-input", namespace = "https://source.jasig.org/schemas/uportal/portlet-publishing", type = JAXBElement.class)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected JAXBElement<? extends ParameterInputType> parameterInput;

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
   * Gets the value of the label property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getLabel() {
    return label;
  }

  /**
   * Sets the value of the label property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setLabel(String value) {
    this.label = value;
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
   * Gets the value of the example property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getExample() {
    return example;
  }

  /**
   * Sets the value of the example property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setExample(String value) {
    this.example = value;
  }

  /**
   * Gets the value of the parameterInput property.
   * 
   * @return possible object is {@link JAXBElement }{@code <}
   *         {@link SingleTextParameterInput }{@code >} {@link JAXBElement }
   *         {@code <}{@link ParameterInputType }{@code >} {@link JAXBElement }
   *         {@code <}{@link SingleChoiceParameterInput }{@code >}
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public JAXBElement<? extends ParameterInputType> getParameterInput() {
    return parameterInput;
  }

  /**
   * Sets the value of the parameterInput property.
   * 
   * @param value
   *          allowed object is {@link JAXBElement }{@code <}
   *          {@link SingleTextParameterInput }{@code >} {@link JAXBElement }
   *          {@code <}{@link ParameterInputType }{@code >} {@link JAXBElement }
   *          {@code <}{@link SingleChoiceParameterInput }{@code >}
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setParameterInput(JAXBElement<? extends ParameterInputType> value) {
    this.parameterInput = value;
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
      String theLabel;
      theLabel = this.getLabel();
      strategy.appendField(locator, this, "label", buffer, theLabel);
    }
    {
      String theDescription;
      theDescription = this.getDescription();
      strategy.appendField(locator, this, "description", buffer, theDescription);
    }
    {
      String theExample;
      theExample = this.getExample();
      strategy.appendField(locator, this, "example", buffer, theExample);
    }
    {
      JAXBElement<? extends ParameterInputType> theParameterInput;
      theParameterInput = this.getParameterInput();
      strategy.appendField(locator, this, "parameterInput", buffer,
        theParameterInput);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof Parameter)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    final Parameter that = ((Parameter) object);
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
      String lhsLabel;
      lhsLabel = this.getLabel();
      String rhsLabel;
      rhsLabel = that.getLabel();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "label", lhsLabel),
        LocatorUtils.property(thatLocator, "label", rhsLabel), lhsLabel, rhsLabel)) {
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
      String lhsExample;
      lhsExample = this.getExample();
      String rhsExample;
      rhsExample = that.getExample();
      if (!strategy.equals(
        LocatorUtils.property(thisLocator, "example", lhsExample), LocatorUtils
          .property(thatLocator, "example", rhsExample), lhsExample, rhsExample)) {
        return false;
      }
    }
    {
      JAXBElement<? extends ParameterInputType> lhsParameterInput;
      lhsParameterInput = this.getParameterInput();
      JAXBElement<? extends ParameterInputType> rhsParameterInput;
      rhsParameterInput = that.getParameterInput();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "parameterInput",
        lhsParameterInput), LocatorUtils.property(thatLocator, "parameterInput",
        rhsParameterInput), lhsParameterInput, rhsParameterInput)) {
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
      String theLabel;
      theLabel = this.getLabel();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "label",
        theLabel), currentHashCode, theLabel);
    }
    {
      String theDescription;
      theDescription = this.getDescription();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "description", theDescription), currentHashCode, theDescription);
    }
    {
      String theExample;
      theExample = this.getExample();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "example",
        theExample), currentHashCode, theExample);
    }
    {
      JAXBElement<? extends ParameterInputType> theParameterInput;
      theParameterInput = this.getParameterInput();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "parameterInput", theParameterInput), currentHashCode, theParameterInput);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
