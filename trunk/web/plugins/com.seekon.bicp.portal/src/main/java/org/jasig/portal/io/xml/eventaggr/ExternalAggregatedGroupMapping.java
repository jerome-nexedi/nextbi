package org.jasig.portal.io.xml.eventaggr;

import java.io.Serializable;
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
 * Java class for externalAggregatedGroupMapping complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="externalAggregatedGroupMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="groupService" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="groupName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "externalAggregatedGroupMapping", propOrder = { "groupService",
  "groupName" })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class ExternalAggregatedGroupMapping implements Serializable, Equals,
  HashCode, ToString {

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String groupService;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String groupName;

  /**
   * Gets the value of the groupService property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getGroupService() {
    return groupService;
  }

  /**
   * Sets the value of the groupService property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setGroupService(String value) {
    this.groupService = value;
  }

  /**
   * Gets the value of the groupName property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getGroupName() {
    return groupName;
  }

  /**
   * Sets the value of the groupName property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setGroupName(String value) {
    this.groupName = value;
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
      String theGroupService;
      theGroupService = this.getGroupService();
      strategy.appendField(locator, this, "groupService", buffer, theGroupService);
    }
    {
      String theGroupName;
      theGroupName = this.getGroupName();
      strategy.appendField(locator, this, "groupName", buffer, theGroupName);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof ExternalAggregatedGroupMapping)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    final ExternalAggregatedGroupMapping that = ((ExternalAggregatedGroupMapping) object);
    {
      String lhsGroupService;
      lhsGroupService = this.getGroupService();
      String rhsGroupService;
      rhsGroupService = that.getGroupService();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "groupService",
        lhsGroupService), LocatorUtils.property(thatLocator, "groupService",
        rhsGroupService), lhsGroupService, rhsGroupService)) {
        return false;
      }
    }
    {
      String lhsGroupName;
      lhsGroupName = this.getGroupName();
      String rhsGroupName;
      rhsGroupName = that.getGroupName();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "groupName",
        lhsGroupName),
        LocatorUtils.property(thatLocator, "groupName", rhsGroupName), lhsGroupName,
        rhsGroupName)) {
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
      String theGroupService;
      theGroupService = this.getGroupService();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "groupService", theGroupService), currentHashCode, theGroupService);
    }
    {
      String theGroupName;
      theGroupName = this.getGroupName();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "groupName", theGroupName), currentHashCode, theGroupName);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
