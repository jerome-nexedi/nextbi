/*     */package org.palo.viewapi.internal.io.xml;

/*     */
/*     */import java.util.Stack;

import org.palo.api.PaloAPIException;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.Property;
import org.palo.viewapi.internal.util.XMLUtil;
import org.xml.sax.Attributes;

/*     */
/*     */public class PropertyHandler
/*     */implements IXMLHandler
/*     */{
  /*     */public static final String XPATH = "/view/property";

  /*     */private Property<Object> property;

  /*     */private final CubeView view;

  /*     */private final Stack<Property<Object>> parentProperties;

  /*     */
  /*     */public PropertyHandler(CubeView view)
  /*     */{
    /* 61 */this.view = view;
    /* 62 */this.parentProperties = new Stack();
    /*     */}

  /*     */
  /*     */public void enter(String path, Attributes attributes) {
    /* 66 */if ((this.property == null) && (path.equals("/view/property")))
    /*     */{
      /* 68 */String id = attributes.getValue("id");
      /* 69 */if ((id == null) || (id.equals(""))) {
        /* 70 */throw new PaloAPIException(
          "PropertyHandler: no property id defined!");
        /*     */}
      /*     */
      /* 73 */String value = attributes.getValue("value");
      /* 74 */if (value == null) {
        /* 75 */throw new PaloAPIException(
          "PropertyHandler: no property value specified!");
        /*     */}
      /*     */
      /* 79 */this.property = this.view.addProperty(id, value);
      /* 80 */this.parentProperties.push(this.property);
      /* 81 */} else if ((path.startsWith("/view/property"))
      && (path.endsWith("property"))) {
      /* 82 */if (this.property == null) {
        /* 83 */throw new PaloAPIException("PropertyHandler: no property created!");
        /*     */}
      /*     */
      /* 86 */String id = attributes.getValue("id");
      /* 87 */if ((id == null) || (id.equals(""))) {
        /* 88 */throw new PaloAPIException(
          "PropertyHandler: no property id defined!");
        /*     */}
      /*     */
      /* 91 */String value = attributes.getValue("value");
      /* 92 */if (value == null) {
        /* 93 */throw new PaloAPIException(
          "PropertyHandler: no property value specified!");
        /*     */}
      /*     */
      /* 96 */Property prop = new Property(
      /* 97 */(Property) this.parentProperties.peek(), id, value);
      /* 98 */this.parentProperties.push(prop);
      /*     */}
    /*     */}

  /*     */
  /*     */public String getXPath() {
    /* 103 */return "/view/property";
    /*     */}

  /*     */
  /*     */public void leave(String path, String value) {
    /* 107 */this.parentProperties.pop();
    /* 108 */if (this.property == null)
      /* 109 */throw new PaloAPIException("PropertyHandler: no property created!");
    /*     */}

  /*     */
  /*     */public static final String getPersistenceString(Property<Object> property)
  /*     */{
    /* 114 */StringBuffer xml = new StringBuffer();
    /* 115 */xml.append("<property");
    /* 116 */xml.append(" id=\"" + XMLUtil.printQuoted(property.getId()) + "\"");
    /* 117 */xml.append(" value=\""
      + XMLUtil.printQuoted(property.getValue().toString()) + "\">\r\n");
    /* 118 */for (Property prop : property.getChildren()) {
      /* 119 */xml.append(getPersistenceString(prop));
      /*     */}
    /* 121 */xml.append("</property>");
    /* 122 */return xml.toString();
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.xml.PropertyHandler JD-Core
 * Version: 0.5.4
 */