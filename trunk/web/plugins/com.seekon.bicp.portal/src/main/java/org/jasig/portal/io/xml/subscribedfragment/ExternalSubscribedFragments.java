package org.jasig.portal.io.xml.subscribedfragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * Java class for subscribed-fragments element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;element name="subscribed-fragments">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{https://source.jasig.org/schemas/uportal/io}basePortalDataType40">
 *         &lt;sequence>
 *           &lt;element name="subscribed-fragment" type="{https://source.jasig.org/schemas/uportal/io/subscribed-fragment}subscribedFragmentType" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *         &lt;attribute name="username" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;/extension>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "subscribedFragments" })
@XmlRootElement(name = "subscribed-fragments")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class ExternalSubscribedFragments extends BasePortalDataType40 implements
  Serializable, Equals, HashCode, ToString {

  @XmlElement(name = "subscribed-fragment", required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<SubscribedFragmentType> subscribedFragments;

  @XmlAttribute(name = "username", required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String username;

  /**
   * Gets the value of the subscribedFragments property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the subscribedFragments property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getSubscribedFragments().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link SubscribedFragmentType }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<SubscribedFragmentType> getSubscribedFragments() {
    if (subscribedFragments == null) {
      subscribedFragments = new ArrayList<SubscribedFragmentType>();
    }
    return this.subscribedFragments;
  }

  /**
   * Gets the value of the username property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getUsername() {
    return username;
  }

  /**
   * Sets the value of the username property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setUsername(String value) {
    this.username = value;
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
      List<SubscribedFragmentType> theSubscribedFragments;
      theSubscribedFragments = (((this.subscribedFragments != null) && (!this.subscribedFragments
        .isEmpty())) ? this.getSubscribedFragments() : null);
      strategy.appendField(locator, this, "subscribedFragments", buffer,
        theSubscribedFragments);
    }
    {
      String theUsername;
      theUsername = this.getUsername();
      strategy.appendField(locator, this, "username", buffer, theUsername);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof ExternalSubscribedFragments)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final ExternalSubscribedFragments that = ((ExternalSubscribedFragments) object);
    {
      List<SubscribedFragmentType> lhsSubscribedFragments;
      lhsSubscribedFragments = (((this.subscribedFragments != null) && (!this.subscribedFragments
        .isEmpty())) ? this.getSubscribedFragments() : null);
      List<SubscribedFragmentType> rhsSubscribedFragments;
      rhsSubscribedFragments = (((that.subscribedFragments != null) && (!that.subscribedFragments
        .isEmpty())) ? that.getSubscribedFragments() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "subscribedFragments",
        lhsSubscribedFragments), LocatorUtils.property(thatLocator,
        "subscribedFragments", rhsSubscribedFragments), lhsSubscribedFragments,
        rhsSubscribedFragments)) {
        return false;
      }
    }
    {
      String lhsUsername;
      lhsUsername = this.getUsername();
      String rhsUsername;
      rhsUsername = that.getUsername();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "username",
        lhsUsername), LocatorUtils.property(thatLocator, "username", rhsUsername),
        lhsUsername, rhsUsername)) {
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
      List<SubscribedFragmentType> theSubscribedFragments;
      theSubscribedFragments = (((this.subscribedFragments != null) && (!this.subscribedFragments
        .isEmpty())) ? this.getSubscribedFragments() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "subscribedFragments", theSubscribedFragments), currentHashCode,
        theSubscribedFragments);
    }
    {
      String theUsername;
      theUsername = this.getUsername();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "username",
        theUsername), currentHashCode, theUsername);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
