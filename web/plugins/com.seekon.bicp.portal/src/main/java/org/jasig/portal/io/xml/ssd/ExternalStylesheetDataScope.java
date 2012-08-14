package org.jasig.portal.io.xml.ssd;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for externalStylesheetDataScope.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="externalStylesheetDataScope">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PERSISTENT"/>
 *     &lt;enumeration value="SESSION"/>
 *     &lt;enumeration value="REQUEST"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "externalStylesheetDataScope")
@XmlEnum
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public enum ExternalStylesheetDataScope {

  PERSISTENT, SESSION, REQUEST;

  public String value() {
    return name();
  }

  public static ExternalStylesheetDataScope fromValue(String v) {
    return valueOf(v);
  }

}
