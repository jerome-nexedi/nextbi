package org.jasig.portal.io.xml.eventaggr;

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
 * Java class for externalAggregatedGroupConfig complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="externalAggregatedGroupConfig">
 *   &lt;complexContent>
 *     &lt;extension base="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalAggregatedDimensionConfig">
 *       &lt;sequence>
 *         &lt;element name="include" type="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalAggregatedGroupMapping" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="exclude" type="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalAggregatedGroupMapping" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "externalAggregatedGroupConfig", propOrder = { "includes",
  "excludes" })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class ExternalAggregatedGroupConfig extends ExternalAggregatedDimensionConfig
  implements Serializable, Equals, HashCode, ToString {

  @XmlElement(name = "include")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalAggregatedGroupMapping> includes;

  @XmlElement(name = "exclude")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalAggregatedGroupMapping> excludes;

  /**
   * Gets the value of the includes property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the includes property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getIncludes().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalAggregatedGroupMapping }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalAggregatedGroupMapping> getIncludes() {
    if (includes == null) {
      includes = new ArrayList<ExternalAggregatedGroupMapping>();
    }
    return this.includes;
  }

  /**
   * Gets the value of the excludes property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the excludes property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getExcludes().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalAggregatedGroupMapping }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalAggregatedGroupMapping> getExcludes() {
    if (excludes == null) {
      excludes = new ArrayList<ExternalAggregatedGroupMapping>();
    }
    return this.excludes;
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
    super.appendFields(locator, buffer, strategy);
    {
      List<ExternalAggregatedGroupMapping> theIncludes;
      theIncludes = (((this.includes != null) && (!this.includes.isEmpty())) ? this
        .getIncludes() : null);
      strategy.appendField(locator, this, "includes", buffer, theIncludes);
    }
    {
      List<ExternalAggregatedGroupMapping> theExcludes;
      theExcludes = (((this.excludes != null) && (!this.excludes.isEmpty())) ? this
        .getExcludes() : null);
      strategy.appendField(locator, this, "excludes", buffer, theExcludes);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof ExternalAggregatedGroupConfig)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final ExternalAggregatedGroupConfig that = ((ExternalAggregatedGroupConfig) object);
    {
      List<ExternalAggregatedGroupMapping> lhsIncludes;
      lhsIncludes = (((this.includes != null) && (!this.includes.isEmpty())) ? this
        .getIncludes() : null);
      List<ExternalAggregatedGroupMapping> rhsIncludes;
      rhsIncludes = (((that.includes != null) && (!that.includes.isEmpty())) ? that
        .getIncludes() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "includes",
        lhsIncludes), LocatorUtils.property(thatLocator, "includes", rhsIncludes),
        lhsIncludes, rhsIncludes)) {
        return false;
      }
    }
    {
      List<ExternalAggregatedGroupMapping> lhsExcludes;
      lhsExcludes = (((this.excludes != null) && (!this.excludes.isEmpty())) ? this
        .getExcludes() : null);
      List<ExternalAggregatedGroupMapping> rhsExcludes;
      rhsExcludes = (((that.excludes != null) && (!that.excludes.isEmpty())) ? that
        .getExcludes() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "excludes",
        lhsExcludes), LocatorUtils.property(thatLocator, "excludes", rhsExcludes),
        lhsExcludes, rhsExcludes)) {
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
    int currentHashCode = super.hashCode(locator, strategy);
    {
      List<ExternalAggregatedGroupMapping> theIncludes;
      theIncludes = (((this.includes != null) && (!this.includes.isEmpty())) ? this
        .getIncludes() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "includes",
        theIncludes), currentHashCode, theIncludes);
    }
    {
      List<ExternalAggregatedGroupMapping> theExcludes;
      theExcludes = (((this.excludes != null) && (!this.excludes.isEmpty())) ? this
        .getExcludes() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "excludes",
        theExcludes), currentHashCode, theExcludes);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
