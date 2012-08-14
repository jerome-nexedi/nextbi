package org.jasig.portal.io.xml.eventaggr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.jasig.portal.io.xml.BasePortalDataType40;
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
 * Java class for event-aggregation element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;element name="event-aggregation">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{https://source.jasig.org/schemas/uportal/io}basePortalDataType40">
 *         &lt;sequence>
 *           &lt;element name="aggregated-group-config" type="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalAggregatedGroupConfig" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="aggregated-interval-config" type="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalAggregatedIntervalConfig" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="quarter-details" type="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalQuarterDetail" maxOccurs="4" minOccurs="0"/>
 *           &lt;element name="term-details" type="{https://source.jasig.org/schemas/uportal/io/event-aggregation}externalTermDetail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/extension>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "aggregatedGroupConfigs",
  "aggregatedIntervalConfigs", "quarterDetails", "termDetails" })
@XmlRootElement(name = "event-aggregation")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class ExternalEventAggregationConfiguration extends BasePortalDataType40
  implements Serializable, Equals, HashCode, ToString {

  @XmlElement(name = "aggregated-group-config")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalAggregatedGroupConfig> aggregatedGroupConfigs;

  @XmlElement(name = "aggregated-interval-config")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalAggregatedIntervalConfig> aggregatedIntervalConfigs;

  @XmlElement(name = "quarter-details")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalQuarterDetail> quarterDetails;

  @XmlElement(name = "term-details")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalTermDetail> termDetails;

  /**
   * Gets the value of the aggregatedGroupConfigs property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the aggregatedGroupConfigs property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getAggregatedGroupConfigs().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalAggregatedGroupConfig }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalAggregatedGroupConfig> getAggregatedGroupConfigs() {
    if (aggregatedGroupConfigs == null) {
      aggregatedGroupConfigs = new ArrayList<ExternalAggregatedGroupConfig>();
    }
    return this.aggregatedGroupConfigs;
  }

  /**
   * Gets the value of the aggregatedIntervalConfigs property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the aggregatedIntervalConfigs property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getAggregatedIntervalConfigs().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalAggregatedIntervalConfig }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalAggregatedIntervalConfig> getAggregatedIntervalConfigs() {
    if (aggregatedIntervalConfigs == null) {
      aggregatedIntervalConfigs = new ArrayList<ExternalAggregatedIntervalConfig>();
    }
    return this.aggregatedIntervalConfigs;
  }

  /**
   * Gets the value of the quarterDetails property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the quarterDetails property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getQuarterDetails().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalQuarterDetail }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalQuarterDetail> getQuarterDetails() {
    if (quarterDetails == null) {
      quarterDetails = new ArrayList<ExternalQuarterDetail>();
    }
    return this.quarterDetails;
  }

  /**
   * Gets the value of the termDetails property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the termDetails property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getTermDetails().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalTermDetail }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalTermDetail> getTermDetails() {
    if (termDetails == null) {
      termDetails = new ArrayList<ExternalTermDetail>();
    }
    return this.termDetails;
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
      List<ExternalAggregatedGroupConfig> theAggregatedGroupConfigs;
      theAggregatedGroupConfigs = (((this.aggregatedGroupConfigs != null) && (!this.aggregatedGroupConfigs
        .isEmpty())) ? this.getAggregatedGroupConfigs() : null);
      strategy.appendField(locator, this, "aggregatedGroupConfigs", buffer,
        theAggregatedGroupConfigs);
    }
    {
      List<ExternalAggregatedIntervalConfig> theAggregatedIntervalConfigs;
      theAggregatedIntervalConfigs = (((this.aggregatedIntervalConfigs != null) && (!this.aggregatedIntervalConfigs
        .isEmpty())) ? this.getAggregatedIntervalConfigs() : null);
      strategy.appendField(locator, this, "aggregatedIntervalConfigs", buffer,
        theAggregatedIntervalConfigs);
    }
    {
      List<ExternalQuarterDetail> theQuarterDetails;
      theQuarterDetails = (((this.quarterDetails != null) && (!this.quarterDetails
        .isEmpty())) ? this.getQuarterDetails() : null);
      strategy.appendField(locator, this, "quarterDetails", buffer,
        theQuarterDetails);
    }
    {
      List<ExternalTermDetail> theTermDetails;
      theTermDetails = (((this.termDetails != null) && (!this.termDetails.isEmpty())) ? this
        .getTermDetails()
        : null);
      strategy.appendField(locator, this, "termDetails", buffer, theTermDetails);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof ExternalEventAggregationConfiguration)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final ExternalEventAggregationConfiguration that = ((ExternalEventAggregationConfiguration) object);
    {
      List<ExternalAggregatedGroupConfig> lhsAggregatedGroupConfigs;
      lhsAggregatedGroupConfigs = (((this.aggregatedGroupConfigs != null) && (!this.aggregatedGroupConfigs
        .isEmpty())) ? this.getAggregatedGroupConfigs() : null);
      List<ExternalAggregatedGroupConfig> rhsAggregatedGroupConfigs;
      rhsAggregatedGroupConfigs = (((that.aggregatedGroupConfigs != null) && (!that.aggregatedGroupConfigs
        .isEmpty())) ? that.getAggregatedGroupConfigs() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator,
        "aggregatedGroupConfigs", lhsAggregatedGroupConfigs), LocatorUtils.property(
        thatLocator, "aggregatedGroupConfigs", rhsAggregatedGroupConfigs),
        lhsAggregatedGroupConfigs, rhsAggregatedGroupConfigs)) {
        return false;
      }
    }
    {
      List<ExternalAggregatedIntervalConfig> lhsAggregatedIntervalConfigs;
      lhsAggregatedIntervalConfigs = (((this.aggregatedIntervalConfigs != null) && (!this.aggregatedIntervalConfigs
        .isEmpty())) ? this.getAggregatedIntervalConfigs() : null);
      List<ExternalAggregatedIntervalConfig> rhsAggregatedIntervalConfigs;
      rhsAggregatedIntervalConfigs = (((that.aggregatedIntervalConfigs != null) && (!that.aggregatedIntervalConfigs
        .isEmpty())) ? that.getAggregatedIntervalConfigs() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator,
        "aggregatedIntervalConfigs", lhsAggregatedIntervalConfigs), LocatorUtils
        .property(thatLocator, "aggregatedIntervalConfigs",
          rhsAggregatedIntervalConfigs), lhsAggregatedIntervalConfigs,
        rhsAggregatedIntervalConfigs)) {
        return false;
      }
    }
    {
      List<ExternalQuarterDetail> lhsQuarterDetails;
      lhsQuarterDetails = (((this.quarterDetails != null) && (!this.quarterDetails
        .isEmpty())) ? this.getQuarterDetails() : null);
      List<ExternalQuarterDetail> rhsQuarterDetails;
      rhsQuarterDetails = (((that.quarterDetails != null) && (!that.quarterDetails
        .isEmpty())) ? that.getQuarterDetails() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "quarterDetails",
        lhsQuarterDetails), LocatorUtils.property(thatLocator, "quarterDetails",
        rhsQuarterDetails), lhsQuarterDetails, rhsQuarterDetails)) {
        return false;
      }
    }
    {
      List<ExternalTermDetail> lhsTermDetails;
      lhsTermDetails = (((this.termDetails != null) && (!this.termDetails.isEmpty())) ? this
        .getTermDetails()
        : null);
      List<ExternalTermDetail> rhsTermDetails;
      rhsTermDetails = (((that.termDetails != null) && (!that.termDetails.isEmpty())) ? that
        .getTermDetails()
        : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "termDetails",
        lhsTermDetails), LocatorUtils.property(thatLocator, "termDetails",
        rhsTermDetails), lhsTermDetails, rhsTermDetails)) {
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
      List<ExternalAggregatedGroupConfig> theAggregatedGroupConfigs;
      theAggregatedGroupConfigs = (((this.aggregatedGroupConfigs != null) && (!this.aggregatedGroupConfigs
        .isEmpty())) ? this.getAggregatedGroupConfigs() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "aggregatedGroupConfigs", theAggregatedGroupConfigs), currentHashCode,
        theAggregatedGroupConfigs);
    }
    {
      List<ExternalAggregatedIntervalConfig> theAggregatedIntervalConfigs;
      theAggregatedIntervalConfigs = (((this.aggregatedIntervalConfigs != null) && (!this.aggregatedIntervalConfigs
        .isEmpty())) ? this.getAggregatedIntervalConfigs() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "aggregatedIntervalConfigs", theAggregatedIntervalConfigs), currentHashCode,
        theAggregatedIntervalConfigs);
    }
    {
      List<ExternalQuarterDetail> theQuarterDetails;
      theQuarterDetails = (((this.quarterDetails != null) && (!this.quarterDetails
        .isEmpty())) ? this.getQuarterDetails() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "quarterDetails", theQuarterDetails), currentHashCode, theQuarterDetails);
    }
    {
      List<ExternalTermDetail> theTermDetails;
      theTermDetails = (((this.termDetails != null) && (!this.termDetails.isEmpty())) ? this
        .getTermDetails()
        : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "termDetails", theTermDetails), currentHashCode, theTermDetails);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
