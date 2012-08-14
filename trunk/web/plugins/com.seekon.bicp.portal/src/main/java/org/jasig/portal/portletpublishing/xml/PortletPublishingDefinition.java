package org.jasig.portal.portletpublishing.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jasig.portal.xml.PortletDescriptor;
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
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="portlet-descriptor" type="{https://source.jasig.org/schemas/uportal}portlet-descriptor" minOccurs="0"/>
 *         &lt;element name="step" type="{https://source.jasig.org/schemas/uportal/portlet-publishing}step" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "portletDescriptor", "steps" })
@XmlRootElement(name = "portlet-publishing-definition")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class PortletPublishingDefinition implements Serializable, Equals, HashCode,
  ToString {

  @XmlElement(name = "portlet-descriptor")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected PortletDescriptor portletDescriptor;

  @XmlElement(name = "step", required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<Step> steps;

  /**
   * Gets the value of the portletDescriptor property.
   * 
   * @return possible object is {@link PortletDescriptor }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public PortletDescriptor getPortletDescriptor() {
    return portletDescriptor;
  }

  /**
   * Sets the value of the portletDescriptor property.
   * 
   * @param value
   *          allowed object is {@link PortletDescriptor }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setPortletDescriptor(PortletDescriptor value) {
    this.portletDescriptor = value;
  }

  /**
   * Gets the value of the steps property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the steps property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getSteps().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Step }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<Step> getSteps() {
    if (steps == null) {
      steps = new ArrayList<Step>();
    }
    return this.steps;
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
      PortletDescriptor thePortletDescriptor;
      thePortletDescriptor = this.getPortletDescriptor();
      strategy.appendField(locator, this, "portletDescriptor", buffer,
        thePortletDescriptor);
    }
    {
      List<Step> theSteps;
      theSteps = (((this.steps != null) && (!this.steps.isEmpty())) ? this
        .getSteps() : null);
      strategy.appendField(locator, this, "steps", buffer, theSteps);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof PortletPublishingDefinition)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    final PortletPublishingDefinition that = ((PortletPublishingDefinition) object);
    {
      PortletDescriptor lhsPortletDescriptor;
      lhsPortletDescriptor = this.getPortletDescriptor();
      PortletDescriptor rhsPortletDescriptor;
      rhsPortletDescriptor = that.getPortletDescriptor();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "portletDescriptor",
        lhsPortletDescriptor), LocatorUtils.property(thatLocator,
        "portletDescriptor", rhsPortletDescriptor), lhsPortletDescriptor,
        rhsPortletDescriptor)) {
        return false;
      }
    }
    {
      List<Step> lhsSteps;
      lhsSteps = (((this.steps != null) && (!this.steps.isEmpty())) ? this
        .getSteps() : null);
      List<Step> rhsSteps;
      rhsSteps = (((that.steps != null) && (!that.steps.isEmpty())) ? that
        .getSteps() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "steps", lhsSteps),
        LocatorUtils.property(thatLocator, "steps", rhsSteps), lhsSteps, rhsSteps)) {
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
      PortletDescriptor thePortletDescriptor;
      thePortletDescriptor = this.getPortletDescriptor();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "portletDescriptor", thePortletDescriptor), currentHashCode,
        thePortletDescriptor);
    }
    {
      List<Step> theSteps;
      theSteps = (((this.steps != null) && (!this.steps.isEmpty())) ? this
        .getSteps() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "steps",
        theSteps), currentHashCode, theSteps);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
