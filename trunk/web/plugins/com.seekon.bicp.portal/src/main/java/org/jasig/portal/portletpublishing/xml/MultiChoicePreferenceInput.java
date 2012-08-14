package org.jasig.portal.portletpublishing.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{https://source.jasig.org/schemas/uportal/portlet-publishing}multi-valued-preference-input-type">
 *       &lt;sequence>
 *         &lt;element name="option" type="{https://source.jasig.org/schemas/uportal/portlet-publishing}option" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="display" type="{https://source.jasig.org/schemas/uportal/portlet-publishing}multiChoiceDisplay" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "options" })
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public class MultiChoicePreferenceInput extends MultiValuedPreferenceInputType
  implements Serializable, Equals, HashCode, ToString {

  @XmlElement(name = "option", required = true)
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected List<Option> options;

  @XmlAttribute(name = "display")
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  protected MultiChoiceDisplay display;

  /**
   * Gets the value of the options property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
   * for the options property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getOptions().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link Option }
   * 
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public List<Option> getOptions() {
    if (options == null) {
      options = new ArrayList<Option>();
    }
    return this.options;
  }

  /**
   * Gets the value of the display property.
   * 
   * @return possible object is {@link MultiChoiceDisplay }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public MultiChoiceDisplay getDisplay() {
    return display;
  }

  /**
   * Sets the value of the display property.
   * 
   * @param value
   *          allowed object is {@link MultiChoiceDisplay }
   * 
   */
  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public void setDisplay(MultiChoiceDisplay value) {
    this.display = value;
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
      List<Option> theOptions;
      theOptions = (((this.options != null) && (!this.options.isEmpty())) ? this
        .getOptions() : null);
      strategy.appendField(locator, this, "options", buffer, theOptions);
    }
    {
      MultiChoiceDisplay theDisplay;
      theDisplay = this.getDisplay();
      strategy.appendField(locator, this, "display", buffer, theDisplay);
    }
    return buffer;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator,
    Object object, EqualsStrategy strategy) {
    if (!(object instanceof MultiChoicePreferenceInput)) {
      return false;
    }
    if (this == object) {
      return true;
    }
    if (!super.equals(thisLocator, thatLocator, object, strategy)) {
      return false;
    }
    final MultiChoicePreferenceInput that = ((MultiChoicePreferenceInput) object);
    {
      List<Option> lhsOptions;
      lhsOptions = (((this.options != null) && (!this.options.isEmpty())) ? this
        .getOptions() : null);
      List<Option> rhsOptions;
      rhsOptions = (((that.options != null) && (!that.options.isEmpty())) ? that
        .getOptions() : null);
      if (!strategy.equals(
        LocatorUtils.property(thisLocator, "options", lhsOptions), LocatorUtils
          .property(thatLocator, "options", rhsOptions), lhsOptions, rhsOptions)) {
        return false;
      }
    }
    {
      MultiChoiceDisplay lhsDisplay;
      lhsDisplay = this.getDisplay();
      MultiChoiceDisplay rhsDisplay;
      rhsDisplay = that.getDisplay();
      if (!strategy.equals(
        LocatorUtils.property(thisLocator, "display", lhsDisplay), LocatorUtils
          .property(thatLocator, "display", rhsDisplay), lhsDisplay, rhsDisplay)) {
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
      List<Option> theOptions;
      theOptions = (((this.options != null) && (!this.options.isEmpty())) ? this
        .getOptions() : null);
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "options",
        theOptions), currentHashCode, theOptions);
    }
    {
      MultiChoiceDisplay theDisplay;
      theDisplay = this.getDisplay();
      currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "display",
        theDisplay), currentHashCode, theDisplay);
    }
    return currentHashCode;
  }

  @Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
  public int hashCode() {
    final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
    return this.hashCode(null, strategy);
  }

}
