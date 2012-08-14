package org.jasig.portal.io.xml.eventaggr;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for externalAggregationInterval.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="externalAggregationInterval">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MINUTE"/>
 *     &lt;enumeration value="FIVE_MINUTE"/>
 *     &lt;enumeration value="HOUR"/>
 *     &lt;enumeration value="DAY"/>
 *     &lt;enumeration value="WEEK"/>
 *     &lt;enumeration value="MONTH"/>
 *     &lt;enumeration value="CALENDAR_QUARTER"/>
 *     &lt;enumeration value="ACADEMIC_TERM"/>
 *     &lt;enumeration value="YEAR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "externalAggregationInterval")
@XmlEnum
@Generated(value = "com.sun.tools.xjc.Driver", date = "2012-05-11T02:18:31+08:00", comments = "JAXB RI v2.2.5-b10")
public enum ExternalAggregationInterval {

  MINUTE, FIVE_MINUTE, HOUR, DAY, WEEK, MONTH, CALENDAR_QUARTER, ACADEMIC_TERM, YEAR;

  public String value() {
    return name();
  }

  public static ExternalAggregationInterval fromValue(String v) {
    return valueOf(v);
  }

}
