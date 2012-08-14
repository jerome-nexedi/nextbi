package org.jasig.portal.io.xml.ssd;

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
 * Java class for stylesheet-descriptor element declaration.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;element name="stylesheet-descriptor">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;extension base="{https://source.jasig.org/schemas/uportal/io}basePortalDataType40">
 *         &lt;sequence>
 *           &lt;element name="name" type="{https://source.jasig.org/schemas/uportal}fname-type"/>
 *           &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *           &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *           &lt;element name="url-syntax-helper" type="{https://source.jasig.org/schemas/uportal}fname-type" minOccurs="0"/>
 *           &lt;element name="output-property" type="{https://source.jasig.org/schemas/uportal/io/stylesheet-descriptor}externalOutputPropertyDescriptor" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="stylesheet-parameter" type="{https://source.jasig.org/schemas/uportal/io/stylesheet-descriptor}externalStylesheetParameterDescriptor" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="layout-attribute" type="{https://source.jasig.org/schemas/uportal/io/stylesheet-descriptor}externalLayoutAttributeDescriptor" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "name", "description", "uri", "urlSyntaxHelper",
  "outputProperties", "stylesheetParameters", "layoutAttributes" })
@XmlRootElement(name = "stylesheet-descriptor")
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class ExternalStylesheetDescriptor extends BasePortalDataType40 implements
  Serializable, Equals, HashCode, ToString {

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String name;

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String description;

  @XmlElement(required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String uri;

  @XmlElement(name = "url-syntax-helper")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected String urlSyntaxHelper;

  @XmlElement(name = "output-property")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalOutputPropertyDescriptor> outputProperties;

  @XmlElement(name = "stylesheet-parameter")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalStylesheetParameterDescriptor> stylesheetParameters;

  @XmlElement(name = "layout-attribute")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<ExternalLayoutAttributeDescriptor> layoutAttributes;

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
   * Gets the value of the uri property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getUri() {
    return uri;
  }

  /**
   * Sets the value of the uri property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setUri(String value) {
    this.uri = value;
  }

  /**
   * Gets the value of the urlSyntaxHelper property.
   * 
   * @return possible object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public String getUrlSyntaxHelper() {
    return urlSyntaxHelper;
  }

  /**
   * Sets the value of the urlSyntaxHelper property.
   * 
   * @param value
   *          allowed object is {@link String }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setUrlSyntaxHelper(String value) {
    this.urlSyntaxHelper = value;
  }

  /**
   * Gets the value of the outputProperties property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the outputProperties property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getOutputProperties().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalOutputPropertyDescriptor }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalOutputPropertyDescriptor> getOutputProperties() {
    if (outputProperties == null) {
      outputProperties = new ArrayList<ExternalOutputPropertyDescriptor>();
    }
    return this.outputProperties;
  }

  /**
   * Gets the value of the stylesheetParameters property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the stylesheetParameters property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getStylesheetParameters().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalStylesheetParameterDescriptor }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalStylesheetParameterDescriptor> getStylesheetParameters() {
    if (stylesheetParameters == null) {
      stylesheetParameters = new ArrayList<ExternalStylesheetParameterDescriptor>();
    }
    return this.stylesheetParameters;
  }

  /**
   * Gets the value of the layoutAttributes property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the layoutAttributes property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getLayoutAttributes().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link ExternalLayoutAttributeDescriptor }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<ExternalLayoutAttributeDescriptor> getLayoutAttributes() {
    if (layoutAttributes == null) {
      layoutAttributes = new ArrayList<ExternalLayoutAttributeDescriptor>();
    }
    return this.layoutAttributes;
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
      String theUri;
      theUri = this.getUri();
      strategy.appendField(locator, this, "uri", buffer, theUri);
    }
    {
      String theUrlSyntaxHelper;
      theUrlSyntaxHelper = this.getUrlSyntaxHelper();
      strategy.appendField(locator, this, "urlSyntaxHelper", buffer,
        theUrlSyntaxHelper);
    }
    {
      List<ExternalOutputPropertyDescriptor> theOutputProperties;
      theOutputProperties = (((this.outputProperties != null) && (!this.outputProperties
        .isEmpty())) ? this.getOutputProperties() : null);
      strategy.appendField(locator, this, "outputProperties", buffer,
        theOutputProperties);
    }
    {
      List<ExternalStylesheetParameterDescriptor> theStylesheetParameters;
      theStylesheetParameters = (((this.stylesheetParameters != null) && (!this.stylesheetParameters
        .isEmpty())) ? this.getStylesheetParameters() : null);
      strategy.appendField(locator, this, "stylesheetParameters", buffer,
        theStylesheetParameters);
    }
    {
      List<ExternalLayoutAttributeDescriptor> theLayoutAttributes;
      theLayoutAttributes = (((this.layoutAttributes != null) && (!this.layoutAttributes
        .isEmpty())) ? this.getLayoutAttributes() : null);
      strategy.appendField(locator, this, "layoutAttributes", buffer,
        theLayoutAttributes);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof ExternalStylesheetDescriptor)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final ExternalStylesheetDescriptor that = ((ExternalStylesheetDescriptor) object);
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
      String lhsUri;
      lhsUri = this.getUri();
      String rhsUri;
      rhsUri = that.getUri();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "uri", lhsUri),
        LocatorUtils.property(thatLocator, "uri", rhsUri), lhsUri, rhsUri)) {
        return false;
      }
    }
    {
      String lhsUrlSyntaxHelper;
      lhsUrlSyntaxHelper = this.getUrlSyntaxHelper();
      String rhsUrlSyntaxHelper;
      rhsUrlSyntaxHelper = that.getUrlSyntaxHelper();
      if (!strategy.equals(LocatorUtils.property(thisLocator, "urlSyntaxHelper",
        lhsUrlSyntaxHelper), LocatorUtils.property(thatLocator, "urlSyntaxHelper",
        rhsUrlSyntaxHelper), lhsUrlSyntaxHelper, rhsUrlSyntaxHelper)) {
        return false;
      }
    }
    {
      List<ExternalOutputPropertyDescriptor> lhsOutputProperties;
      lhsOutputProperties = (((this.outputProperties != null) && (!this.outputProperties
        .isEmpty())) ? this.getOutputProperties() : null);
      List<ExternalOutputPropertyDescriptor> rhsOutputProperties;
      rhsOutputProperties = (((that.outputProperties != null) && (!that.outputProperties
        .isEmpty())) ? that.getOutputProperties() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "outputProperties",
        lhsOutputProperties), LocatorUtils.property(thatLocator, "outputProperties",
        rhsOutputProperties), lhsOutputProperties, rhsOutputProperties)) {
        return false;
      }
    }
    {
      List<ExternalStylesheetParameterDescriptor> lhsStylesheetParameters;
      lhsStylesheetParameters = (((this.stylesheetParameters != null) && (!this.stylesheetParameters
        .isEmpty())) ? this.getStylesheetParameters() : null);
      List<ExternalStylesheetParameterDescriptor> rhsStylesheetParameters;
      rhsStylesheetParameters = (((that.stylesheetParameters != null) && (!that.stylesheetParameters
        .isEmpty())) ? that.getStylesheetParameters() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator,
        "stylesheetParameters", lhsStylesheetParameters), LocatorUtils.property(
        thatLocator, "stylesheetParameters", rhsStylesheetParameters),
        lhsStylesheetParameters, rhsStylesheetParameters)) {
        return false;
      }
    }
    {
      List<ExternalLayoutAttributeDescriptor> lhsLayoutAttributes;
      lhsLayoutAttributes = (((this.layoutAttributes != null) && (!this.layoutAttributes
        .isEmpty())) ? this.getLayoutAttributes() : null);
      List<ExternalLayoutAttributeDescriptor> rhsLayoutAttributes;
      rhsLayoutAttributes = (((that.layoutAttributes != null) && (!that.layoutAttributes
        .isEmpty())) ? that.getLayoutAttributes() : null);
      if (!strategy.equals(LocatorUtils.property(thisLocator, "layoutAttributes",
        lhsLayoutAttributes), LocatorUtils.property(thatLocator, "layoutAttributes",
        rhsLayoutAttributes), lhsLayoutAttributes, rhsLayoutAttributes)) {
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
      String theUri;
      theUri = this.getUri();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "uri",
        theUri), currentHashCode, theUri);
    }
    {
      String theUrlSyntaxHelper;
      theUrlSyntaxHelper = this.getUrlSyntaxHelper();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "urlSyntaxHelper", theUrlSyntaxHelper), currentHashCode, theUrlSyntaxHelper);
    }
    {
      List<ExternalOutputPropertyDescriptor> theOutputProperties;
      theOutputProperties = (((this.outputProperties != null) && (!this.outputProperties
        .isEmpty())) ? this.getOutputProperties() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "outputProperties", theOutputProperties), currentHashCode,
        theOutputProperties);
    }
    {
      List<ExternalStylesheetParameterDescriptor> theStylesheetParameters;
      theStylesheetParameters = (((this.stylesheetParameters != null) && (!this.stylesheetParameters
        .isEmpty())) ? this.getStylesheetParameters() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "stylesheetParameters", theStylesheetParameters), currentHashCode,
        theStylesheetParameters);
    }
    {
      List<ExternalLayoutAttributeDescriptor> theLayoutAttributes;
      theLayoutAttributes = (((this.layoutAttributes != null) && (!this.layoutAttributes
        .isEmpty())) ? this.getLayoutAttributes() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator,
        "layoutAttributes", theLayoutAttributes), currentHashCode,
        theLayoutAttributes);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
