/*     */package org.palo.api.ext.ui.table;

/*     */
/*     */import java.util.HashMap; /*     */
import java.util.HashSet; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.ext.ui.table.impl.TableFormatPersistence;

/*     */
/*     */public class TableFormatProvider
/*     */{
  /* 60 */private static final TableFormatProvider instance = new TableFormatProvider();

  /*     */
  /* 63 */private final HashMap<Element, TableFormat> formats = new HashMap();

  /*     */
  /* 65 */private final HashSet<Element> formatSet = new HashSet();

  /*     */
  /*     */public static final TableFormatProvider getInstance()
  /*     */{
    /* 72 */return instance;
    /*     */}

  /*     */
  /*     */public final TableFormat getFormat(Element element)
  /*     */{
    /* 90 */if (!this.formatSet.contains(element)) {
      /* 91 */this.formatSet.add(element);
      /* 92 */if (hasFormat(element)) {
        /* 93 */String xmlStr = getFormatDefinition(element);
        /*     */
        /* 95 */TableFormatPersistence persister = TableFormatPersistence
          .getInstance();
        /* 96 */TableFormat f = persister.read(xmlStr);
        /* 97 */this.formats.put(element, f);
        /* 98 */return f;
        /*     */}
      /*     */} else {
      /* 101 */return (TableFormat) this.formats.get(element);
      /*     */}
    /* 103 */return null;
    /*     */}

  /*     */
  /*     */public final boolean hasFormat(Element element)
  /*     */{
    /* 115 */if (element == null)
      /* 116 */return false;
    /* 117 */Hierarchy hierarchy = element.getHierarchy();
    /* 118 */if (hierarchy.getDimension().getDatabase().getConnection().isLegacy())
      /* 119 */return false;
    /* 120 */if ((hierarchy.isAttributeHierarchy()) ||
    /* 121 */(hierarchy.getDimension().isSystemDimension())) {
      /* 122 */return false;
      /*     */}
    /* 124 */return getFormatDefinition(element).length() > 0;
    /*     */}

  /*     */
  /*     */private final String getFormatDefinition(Element element)
  /*     */{
    /* 132 */Attribute attribute = getFormatAttribute(element);
    /* 133 */if (attribute != null)
    /*     */{
      /* 135 */return attribute.getValue(element).toString();
      /*     */}
    /* 137 */return "";
    /*     */}

  /*     */
  /*     */private final Attribute getFormatAttribute(Element element)
  /*     */{
    /* 142 */return getFormatAttribute(element.getHierarchy());
    /*     */}

  /*     */
  /*     */private final Attribute getFormatAttribute(Hierarchy hierarchy) {
    /*     */try {
      /* 147 */Attribute[] attributes = hierarchy.getAttributes();
      /* 148 */for (int i = 0; i < attributes.length; ++i)
        /* 149 */if ("format".equalsIgnoreCase(attributes[i].getName()))
          /* 150 */return attributes[i];
      /*     */}
    /*     */catch (Exception localException)
    /*     */{
      /*     */}
    /* 155 */return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.ui.table.TableFormatProvider JD-Core
 * Version: 0.5.4
 */