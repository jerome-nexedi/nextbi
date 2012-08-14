package org.jasig.portal.portletpublishing.xml;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for singleChoiceDisplay.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="singleChoiceDisplay">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="hidden"/>
 *     &lt;enumeration value="select"/>
 *     &lt;enumeration value="radio"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "singleChoiceDisplay")
@XmlEnum
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public enum SingleChoiceDisplay {

  @XmlEnumValue("hidden")
  HIDDEN("hidden"), @XmlEnumValue("select")
  SELECT("select"), @XmlEnumValue("radio")
  RADIO("radio");
  private final String value;

  SingleChoiceDisplay(String v) {
    value = v;
  }

  public String value() {
    return value;
  }

  public static SingleChoiceDisplay fromValue(String v) {
    for (SingleChoiceDisplay c : SingleChoiceDisplay.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

}
