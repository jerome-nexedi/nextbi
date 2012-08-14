package org.jasig.portal.xml;

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
 * Java class for portlet-descriptor complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="portlet-descriptor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="isFramework" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="webAppName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element name="portletName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "portlet-descriptor", propOrder = { "webAppName", "isFramework",
  "portletName" })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class PortletDescriptor implements Serializable, Equals, HashCode, ToString {

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String webAppName;

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected Boolean isFramework;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String portletName;

  /**
   * Gets the value of the webAppName property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getWebAppName() {
    return webAppName;
  }

  /**
   * Sets the value of the webAppName property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setWebAppName(String value) {
    this.webAppName = value;
  }

  /**
   * Gets the value of the isFramework property.
   * 
   * @return possible object is {@link Boolean }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public Boolean isIsFramework() {
    return isFramework;
  }

  /**
   * Sets the value of the isFramework property.
   * 
   * @param value
   *          allowed object is {@link Boolean }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setIsFramework(Boolean value) {
    this.isFramework = value;
  }

  /**
   * Gets the value of the portletName property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getPortletName() {
    return portletName;
  }

  /**
   * Sets the value of the portletName property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setPortletName(String value) {
    this.portletName = value;
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
      String theWebAppName;
      theWebAppName = this.getWebAppName();
      strategy.appendField(locator, this, "webAppName", buffer, theWebAppName);
    }
    {
      Boolean theIsFramework;
      theIsFramework = this.isIsFramework();
      strategy.appendField(locator, this, "isFramework", buffer, theIsFramework);
    }
    {
      String thePortletName;
      thePortletName = this.getPortletName();
      strategy.appendField(locator, this, "portletName", buffer, thePortletName);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof PortletDescriptor)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    final PortletDescriptor that = ((PortletDescriptor) object);
    {
      String lhsWebAppName;
      lhsWebAppName = this.getWebAppName();
      String rhsWebAppName;
      rhsWebAppName = that.getWebAppName();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "webAppName",
        lhsWebAppName), LocatorUtils.property(thatLocator, "webAppName",
        rhsWebAppName), lhsWebAppName, rhsWebAppName)) {
        return false;
      }
    }
    {
      Boolean lhsIsFramework;
      lhsIsFramework = this.isIsFramework();
      Boolean rhsIsFramework;
      rhsIsFramework = that.isIsFramework();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "isFramework",
        lhsIsFramework), LocatorUtils.property(thatLocator, "isFramework",
        rhsIsFramework), lhsIsFramework, rhsIsFramework)) {
        return false;
      }
    }
    {
      String lhsPortletName;
      lhsPortletName = this.getPortletName();
      String rhsPortletName;
      rhsPortletName = that.getPortletName();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "portletName",
        lhsPortletName), LocatorUtils.property(thatLocator, "portletName",
        rhsPortletName), lhsPortletName, rhsPortletName)) {
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
      String theWebAppName;
      theWebAppName = this.getWebAppName();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "webAppName", theWebAppName), currentHashCode, theWebAppName);
    }
    {
      Boolean theIsFramework;
      theIsFramework = this.isIsFramework();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "isFramework", theIsFramework), currentHashCode, theIsFramework);
    }
    {
      String thePortletName;
      thePortletName = this.getPortletName();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "portletName", thePortletName), currentHashCode, thePortletName);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
