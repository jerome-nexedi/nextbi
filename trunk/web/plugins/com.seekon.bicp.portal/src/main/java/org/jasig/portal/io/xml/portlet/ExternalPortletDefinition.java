package org.jasig.portal.io.xml.portlet;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import org.jasig.portal.io.xml.BasePortalDataType40;
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
 * Java class for portlet-definition element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;element name="portlet-definition">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{https://source.jasig.org/schemas/uportal/io}basePortalDataType40">
 *         &lt;sequence>
 *           &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="fname" type="{https://source.jasig.org/schemas/uportal}fname-type"/>
 *           &lt;element name="desc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="timeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *           &lt;element name="actionTimeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *           &lt;element name="eventTimeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *           &lt;element name="renderTimeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *           &lt;element name="resourceTimeout" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *           &lt;element name="portlet-descriptor" type="{https://source.jasig.org/schemas/uportal}portlet-descriptor"/>
 *           &lt;element name="category" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="group" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="user" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="parameter" type="{https://source.jasig.org/schemas/uportal/io/portlet-definition}externalPortletParameter" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="portlet-preference" type="{https://source.jasig.org/schemas/uportal/io/portlet-definition}externalPortletPreference" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "title", "name", "fname", "desc", "type",
  "timeout", "actionTimeout", "eventTimeout", "renderTimeout", "resourceTimeout",
  "portletDescriptor", "categories", "groups", "users", "parameters",
  "portletPreferences" })
@XmlRootElement(name = "portlet-definition")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class ExternalPortletDefinition extends BasePortalDataType40 implements
  Serializable, Equals, HashCode, ToString {

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String title;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String name;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String fname;

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String desc;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String type;

  @XmlElement(required = true)
  @XmlSchemaType(name = "positiveInteger")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected BigInteger timeout;

  @XmlSchemaType(name = "positiveInteger")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected BigInteger actionTimeout;

  @XmlSchemaType(name = "positiveInteger")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected BigInteger eventTimeout;

  @XmlSchemaType(name = "positiveInteger")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected BigInteger renderTimeout;

  @XmlSchemaType(name = "positiveInteger")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected BigInteger resourceTimeout;

  @XmlElement(name = "portlet-descriptor", required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected PortletDescriptor portletDescriptor;

  @XmlElement(name = "category")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<String> categories;

  @XmlElement(name = "group")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<String> groups;

  @XmlElement(name = "user")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<String> users;

  @XmlElement(name = "parameter")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalPortletParameter> parameters;

  @XmlElement(name = "portlet-preference")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalPortletPreference> portletPreferences;

  /**
   * Gets the value of the title property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getTitle() {
    return title;
  }

  /**
   * Sets the value of the title property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setTitle(String value) {
    this.title = value;
  }

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
   * Gets the value of the fname property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getFname() {
    return fname;
  }

  /**
   * Sets the value of the fname property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setFname(String value) {
    this.fname = value;
  }

  /**
   * Gets the value of the desc property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getDesc() {
    return desc;
  }

  /**
   * Sets the value of the desc property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setDesc(String value) {
    this.desc = value;
  }

  /**
   * Gets the value of the type property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getType() {
    return type;
  }

  /**
   * Sets the value of the type property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setType(String value) {
    this.type = value;
  }

  /**
   * Gets the value of the timeout property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public BigInteger getTimeout() {
    return timeout;
  }

  /**
   * Sets the value of the timeout property.
   * 
   * @param value
   *          allowed object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setTimeout(BigInteger value) {
    this.timeout = value;
  }

  /**
   * Gets the value of the actionTimeout property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public BigInteger getActionTimeout() {
    return actionTimeout;
  }

  /**
   * Sets the value of the actionTimeout property.
   * 
   * @param value
   *          allowed object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setActionTimeout(BigInteger value) {
    this.actionTimeout = value;
  }

  /**
   * Gets the value of the eventTimeout property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public BigInteger getEventTimeout() {
    return eventTimeout;
  }

  /**
   * Sets the value of the eventTimeout property.
   * 
   * @param value
   *          allowed object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setEventTimeout(BigInteger value) {
    this.eventTimeout = value;
  }

  /**
   * Gets the value of the renderTimeout property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public BigInteger getRenderTimeout() {
    return renderTimeout;
  }

  /**
   * Sets the value of the renderTimeout property.
   * 
   * @param value
   *          allowed object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setRenderTimeout(BigInteger value) {
    this.renderTimeout = value;
  }

  /**
   * Gets the value of the resourceTimeout property.
   * 
   * @return possible object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public BigInteger getResourceTimeout() {
    return resourceTimeout;
  }

  /**
   * Sets the value of the resourceTimeout property.
   * 
   * @param value
   *          allowed object is {@link BigInteger }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setResourceTimeout(BigInteger value) {
    this.resourceTimeout = value;
  }

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
   * Gets the value of the categories property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the categories property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getCategories().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<String> getCategories() {
    if (categories == null) {
      categories = new ArrayList<String>();
    }
    return this.categories;
  }

  /**
   * Gets the value of the groups property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the groups property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getGroups().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<String> getGroups() {
    if (groups == null) {
      groups = new ArrayList<String>();
    }
    return this.groups;
  }

  /**
   * Gets the value of the users property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the users property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getUsers().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<String> getUsers() {
    if (users == null) {
      users = new ArrayList<String>();
    }
    return this.users;
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
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalPortletParameter }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalPortletParameter> getParameters() {
    if (parameters == null) {
      parameters = new ArrayList<ExternalPortletParameter>();
    }
    return this.parameters;
  }

  /**
   * Gets the value of the portletPreferences property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the portletPreferences property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getPortletPreferences().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalPortletPreference }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalPortletPreference> getPortletPreferences() {
    if (portletPreferences == null) {
      portletPreferences = new ArrayList<ExternalPortletPreference>();
    }
    return this.portletPreferences;
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
      String theTitle;
      theTitle = this.getTitle();
      strategy.appendField(locator, this, "title", buffer, theTitle);
    }
    {
      String theName;
      theName = this.getName();
      strategy.appendField(locator, this, "name", buffer, theName);
    }
    {
      String theFname;
      theFname = this.getFname();
      strategy.appendField(locator, this, "fname", buffer, theFname);
    }
    {
      String theDesc;
      theDesc = this.getDesc();
      strategy.appendField(locator, this, "desc", buffer, theDesc);
    }
    {
      String theType;
      theType = this.getType();
      strategy.appendField(locator, this, "type", buffer, theType);
    }
    {
      BigInteger theTimeout;
      theTimeout = this.getTimeout();
      strategy.appendField(locator, this, "timeout", buffer, theTimeout);
    }
    {
      BigInteger theActionTimeout;
      theActionTimeout = this.getActionTimeout();
      strategy.appendField(locator, this, "actionTimeout", buffer, theActionTimeout);
    }
    {
      BigInteger theEventTimeout;
      theEventTimeout = this.getEventTimeout();
      strategy.appendField(locator, this, "eventTimeout", buffer, theEventTimeout);
    }
    {
      BigInteger theRenderTimeout;
      theRenderTimeout = this.getRenderTimeout();
      strategy.appendField(locator, this, "renderTimeout", buffer, theRenderTimeout);
    }
    {
      BigInteger theResourceTimeout;
      theResourceTimeout = this.getResourceTimeout();
      strategy.appendField(locator, this, "resourceTimeout", buffer,
        theResourceTimeout);
    }
    {
      PortletDescriptor thePortletDescriptor;
      thePortletDescriptor = this.getPortletDescriptor();
      strategy.appendField(locator, this, "portletDescriptor", buffer,
        thePortletDescriptor);
    }
    {
      List<String> theCategories;
      theCategories = (((this.categories != null) && (!this.categories.isEmpty())) ? this
        .getCategories()
        : null);
      strategy.appendField(locator, this, "categories", buffer, theCategories);
    }
    {
      List<String> theGroups;
      theGroups = (((this.groups != null) && (!this.groups.isEmpty())) ? this
        .getGroups() : null);
      strategy.appendField(locator, this, "groups", buffer, theGroups);
    }
    {
      List<String> theUsers;
      theUsers = (((this.users != null) && (!this.users.isEmpty())) ? this
        .getUsers() : null);
      strategy.appendField(locator, this, "users", buffer, theUsers);
    }
    {
      List<ExternalPortletParameter> theParameters;
      theParameters = (((this.parameters != null) && (!this.parameters.isEmpty())) ? this
        .getParameters()
        : null);
      strategy.appendField(locator, this, "parameters", buffer, theParameters);
    }
    {
      List<ExternalPortletPreference> thePortletPreferences;
      thePortletPreferences = (((this.portletPreferences != null) && (!this.portletPreferences
        .isEmpty())) ? this.getPortletPreferences() : null);
      strategy.appendField(locator, this, "portletPreferences", buffer,
        thePortletPreferences);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof ExternalPortletDefinition)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final ExternalPortletDefinition that = ((ExternalPortletDefinition) object);
    {
      String lhsTitle;
      lhsTitle = this.getTitle();
      String rhsTitle;
      rhsTitle = that.getTitle();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "title", lhsTitle),
        LocatorUtils.property(thatLocator, "title", rhsTitle), lhsTitle, rhsTitle)) {
        return false;
      }
    }
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
      String lhsFname;
      lhsFname = this.getFname();
      String rhsFname;
      rhsFname = that.getFname();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "fname", lhsFname),
        LocatorUtils.property(thatLocator, "fname", rhsFname), lhsFname, rhsFname)) {
        return false;
      }
    }
    {
      String lhsDesc;
      lhsDesc = this.getDesc();
      String rhsDesc;
      rhsDesc = that.getDesc();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "desc", lhsDesc),
        LocatorUtils.property(thatLocator, "desc", rhsDesc), lhsDesc, rhsDesc)) {
        return false;
      }
    }
    {
      String lhsType;
      lhsType = this.getType();
      String rhsType;
      rhsType = that.getType();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "type", lhsType),
        LocatorUtils.property(thatLocator, "type", rhsType), lhsType, rhsType)) {
        return false;
      }
    }
    {
      BigInteger lhsTimeout;
      lhsTimeout = this.getTimeout();
      BigInteger rhsTimeout;
      rhsTimeout = that.getTimeout();
      if (!strategy.equals(
        LocatorUtils.property(thisLocator, "timeout", lhsTimeout), LocatorUtils
          .property(thatLocator, "timeout", rhsTimeout), lhsTimeout, rhsTimeout)) {
        return false;
      }
    }
    {
      BigInteger lhsActionTimeout;
      lhsActionTimeout = this.getActionTimeout();
      BigInteger rhsActionTimeout;
      rhsActionTimeout = that.getActionTimeout();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "actionTimeout",
        lhsActionTimeout), LocatorUtils.property(thatLocator, "actionTimeout",
        rhsActionTimeout), lhsActionTimeout, rhsActionTimeout)) {
        return false;
      }
    }
    {
      BigInteger lhsEventTimeout;
      lhsEventTimeout = this.getEventTimeout();
      BigInteger rhsEventTimeout;
      rhsEventTimeout = that.getEventTimeout();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "eventTimeout",
        lhsEventTimeout), LocatorUtils.property(thatLocator, "eventTimeout",
        rhsEventTimeout), lhsEventTimeout, rhsEventTimeout)) {
        return false;
      }
    }
    {
      BigInteger lhsRenderTimeout;
      lhsRenderTimeout = this.getRenderTimeout();
      BigInteger rhsRenderTimeout;
      rhsRenderTimeout = that.getRenderTimeout();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "renderTimeout",
        lhsRenderTimeout), LocatorUtils.property(thatLocator, "renderTimeout",
        rhsRenderTimeout), lhsRenderTimeout, rhsRenderTimeout)) {
        return false;
      }
    }
    {
      BigInteger lhsResourceTimeout;
      lhsResourceTimeout = this.getResourceTimeout();
      BigInteger rhsResourceTimeout;
      rhsResourceTimeout = that.getResourceTimeout();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "resourceTimeout",
        lhsResourceTimeout), LocatorUtils.property(thatLocator, "resourceTimeout",
        rhsResourceTimeout), lhsResourceTimeout, rhsResourceTimeout)) {
        return false;
      }
    }
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
      List<String> lhsCategories;
      lhsCategories = (((this.categories != null) && (!this.categories.isEmpty())) ? this
        .getCategories()
        : null);
      List<String> rhsCategories;
      rhsCategories = (((that.categories != null) && (!that.categories.isEmpty())) ? that
        .getCategories()
        : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "categories",
        lhsCategories), LocatorUtils.property(thatLocator, "categories",
        rhsCategories), lhsCategories, rhsCategories)) {
        return false;
      }
    }
    {
      List<String> lhsGroups;
      lhsGroups = (((this.groups != null) && (!this.groups.isEmpty())) ? this
        .getGroups() : null);
      List<String> rhsGroups;
      rhsGroups = (((that.groups != null) && (!that.groups.isEmpty())) ? that
        .getGroups() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "groups", lhsGroups),
        LocatorUtils.property(thatLocator, "groups", rhsGroups), lhsGroups,
        rhsGroups)) {
        return false;
      }
    }
    {
      List<String> lhsUsers;
      lhsUsers = (((this.users != null) && (!this.users.isEmpty())) ? this
        .getUsers() : null);
      List<String> rhsUsers;
      rhsUsers = (((that.users != null) && (!that.users.isEmpty())) ? that
        .getUsers() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "users", lhsUsers),
        LocatorUtils.property(thatLocator, "users", rhsUsers), lhsUsers, rhsUsers)) {
        return false;
      }
    }
    {
      List<ExternalPortletParameter> lhsParameters;
      lhsParameters = (((this.parameters != null) && (!this.parameters.isEmpty())) ? this
        .getParameters()
        : null);
      List<ExternalPortletParameter> rhsParameters;
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
      List<ExternalPortletPreference> lhsPortletPreferences;
      lhsPortletPreferences = (((this.portletPreferences != null) && (!this.portletPreferences
        .isEmpty())) ? this.getPortletPreferences() : null);
      List<ExternalPortletPreference> rhsPortletPreferences;
      rhsPortletPreferences = (((that.portletPreferences != null) && (!that.portletPreferences
        .isEmpty())) ? that.getPortletPreferences() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "portletPreferences",
        lhsPortletPreferences), LocatorUtils.property(thatLocator,
        "portletPreferences", rhsPortletPreferences), lhsPortletPreferences,
        rhsPortletPreferences)) {
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
      String theTitle;
      theTitle = this.getTitle();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "title",
        theTitle), currentHashCode, theTitle);
    }
    {
      String theName;
      theName = this.getName();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "name",
        theName), currentHashCode, theName);
    }
    {
      String theFname;
      theFname = this.getFname();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "fname",
        theFname), currentHashCode, theFname);
    }
    {
      String theDesc;
      theDesc = this.getDesc();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "desc",
        theDesc), currentHashCode, theDesc);
    }
    {
      String theType;
      theType = this.getType();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "type",
        theType), currentHashCode, theType);
    }
    {
      BigInteger theTimeout;
      theTimeout = this.getTimeout();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "timeout",
        theTimeout), currentHashCode, theTimeout);
    }
    {
      BigInteger theActionTimeout;
      theActionTimeout = this.getActionTimeout();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "actionTimeout", theActionTimeout), currentHashCode, theActionTimeout);
    }
    {
      BigInteger theEventTimeout;
      theEventTimeout = this.getEventTimeout();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "eventTimeout", theEventTimeout), currentHashCode, theEventTimeout);
    }
    {
      BigInteger theRenderTimeout;
      theRenderTimeout = this.getRenderTimeout();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "renderTimeout", theRenderTimeout), currentHashCode, theRenderTimeout);
    }
    {
      BigInteger theResourceTimeout;
      theResourceTimeout = this.getResourceTimeout();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "resourceTimeout", theResourceTimeout), currentHashCode, theResourceTimeout);
    }
    {
      PortletDescriptor thePortletDescriptor;
      thePortletDescriptor = this.getPortletDescriptor();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "portletDescriptor", thePortletDescriptor), currentHashCode,
        thePortletDescriptor);
    }
    {
      List<String> theCategories;
      theCategories = (((this.categories != null) && (!this.categories.isEmpty())) ? this
        .getCategories()
        : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "categories", theCategories), currentHashCode, theCategories);
    }
    {
      List<String> theGroups;
      theGroups = (((this.groups != null) && (!this.groups.isEmpty())) ? this
        .getGroups() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "groups",
        theGroups), currentHashCode, theGroups);
    }
    {
      List<String> theUsers;
      theUsers = (((this.users != null) && (!this.users.isEmpty())) ? this
        .getUsers() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "users",
        theUsers), currentHashCode, theUsers);
    }
    {
      List<ExternalPortletParameter> theParameters;
      theParameters = (((this.parameters != null) && (!this.parameters.isEmpty())) ? this
        .getParameters()
        : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "parameters", theParameters), currentHashCode, theParameters);
    }
    {
      List<ExternalPortletPreference> thePortletPreferences;
      thePortletPreferences = (((this.portletPreferences != null) && (!this.portletPreferences
        .isEmpty())) ? this.getPortletPreferences() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "portletPreferences", thePortletPreferences), currentHashCode,
        thePortletPreferences);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
