package org.jasig.portal.io.xml.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
import org.w3._2001.xmlschema.Adapter1;

/**
 * <p>
 * Java class for userType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="userType">
 *   &lt;complexContent>
 *     &lt;extension base="{https://source.jasig.org/schemas/uportal/io}basePortalDataType40">
 *       &lt;sequence>
 *         &lt;element name="default-user" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastPasswordChange" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="attribute" type="{https://source.jasig.org/schemas/uportal/io/user}attribute" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="username" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userType", propOrder = { "defaultUser", "password",
  "lastPasswordChange", "attributes" })
@XmlSeeAlso( { ExternalTemplateUser.class, ExternalUser.class })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class UserType extends BasePortalDataType40 implements Serializable, Equals,
  HashCode, ToString {

  @XmlElement(name = "default-user")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String defaultUser;

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String password;

  @XmlElement(type = String.class)
  @XmlJavaTypeAdapter(Adapter1.class)
  @XmlSchemaType(name = "dateTime")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected Calendar lastPasswordChange;

  @XmlElement(name = "attribute")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<Attribute> attributes;

  @XmlAttribute(name = "username", required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String username;

  /**
   * Gets the value of the defaultUser property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getDefaultUser() {
    return defaultUser;
  }

  /**
   * Sets the value of the defaultUser property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setDefaultUser(String value) {
    this.defaultUser = value;
  }

  /**
   * Gets the value of the password property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getPassword() {
    return password;
  }

  /**
   * Sets the value of the password property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setPassword(String value) {
    this.password = value;
  }

  /**
   * Gets the value of the lastPasswordChange property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public Calendar getLastPasswordChange() {
    return lastPasswordChange;
  }

  /**
   * Sets the value of the lastPasswordChange property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setLastPasswordChange(Calendar value) {
    this.lastPasswordChange = value;
  }

  /**
   * Gets the value of the attributes property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the attributes property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getAttributes().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Attribute }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<Attribute> getAttributes() {
    if (attributes == null) {
      attributes = new ArrayList<Attribute>();
    }
    return this.attributes;
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
      String theDefaultUser;
      theDefaultUser = this.getDefaultUser();
      strategy.appendField(locator, this, "defaultUser", buffer, theDefaultUser);
    }
    {
      String thePassword;
      thePassword = this.getPassword();
      strategy.appendField(locator, this, "password", buffer, thePassword);
    }
    {
      Calendar theLastPasswordChange;
      theLastPasswordChange = this.getLastPasswordChange();
      strategy.appendField(locator, this, "lastPasswordChange", buffer,
        theLastPasswordChange);
    }
    {
      List<Attribute> theAttributes;
      theAttributes = (((this.attributes != null) && (!this.attributes.isEmpty())) ? this
        .getAttributes()
        : null);
      strategy.appendField(locator, this, "attributes", buffer, theAttributes);
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
    if (!(object instanceof UserType)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final UserType that = ((UserType) object);
    {
      String lhsDefaultUser;
      lhsDefaultUser = this.getDefaultUser();
      String rhsDefaultUser;
      rhsDefaultUser = that.getDefaultUser();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "defaultUser",
        lhsDefaultUser), LocatorUtils.property(thatLocator, "defaultUser",
        rhsDefaultUser), lhsDefaultUser, rhsDefaultUser)) {
        return false;
      }
    }
    {
      String lhsPassword;
      lhsPassword = this.getPassword();
      String rhsPassword;
      rhsPassword = that.getPassword();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "password",
        lhsPassword), LocatorUtils.property(thatLocator, "password", rhsPassword),
        lhsPassword, rhsPassword)) {
        return false;
      }
    }
    {
      Calendar lhsLastPasswordChange;
      lhsLastPasswordChange = this.getLastPasswordChange();
      Calendar rhsLastPasswordChange;
      rhsLastPasswordChange = that.getLastPasswordChange();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "lastPasswordChange",
        lhsLastPasswordChange), LocatorUtils.property(thatLocator,
        "lastPasswordChange", rhsLastPasswordChange), lhsLastPasswordChange,
        rhsLastPasswordChange)) {
        return false;
      }
    }
    {
      List<Attribute> lhsAttributes;
      lhsAttributes = (((this.attributes != null) && (!this.attributes.isEmpty())) ? this
        .getAttributes()
        : null);
      List<Attribute> rhsAttributes;
      rhsAttributes = (((that.attributes != null) && (!that.attributes.isEmpty())) ? that
        .getAttributes()
        : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "attributes",
        lhsAttributes), LocatorUtils.property(thatLocator, "attributes",
        rhsAttributes), lhsAttributes, rhsAttributes)) {
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
      String theDefaultUser;
      theDefaultUser = this.getDefaultUser();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "defaultUser", theDefaultUser), currentHashCode, theDefaultUser);
    }
    {
      String thePassword;
      thePassword = this.getPassword();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "password",
        thePassword), currentHashCode, thePassword);
    }
    {
      Calendar theLastPasswordChange;
      theLastPasswordChange = this.getLastPasswordChange();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "lastPasswordChange", theLastPasswordChange), currentHashCode,
        theLastPasswordChange);
    }
    {
      List<Attribute> theAttributes;
      theAttributes = (((this.attributes != null) && (!this.attributes.isEmpty())) ? this
        .getAttributes()
        : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "attributes", theAttributes), currentHashCode, theAttributes);
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
