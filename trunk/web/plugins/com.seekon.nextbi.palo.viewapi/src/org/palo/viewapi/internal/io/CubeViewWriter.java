/*     */package org.palo.viewapi.internal.io;

/*     */
/*     */import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.palo.api.exceptions.PaloIOException;
import org.palo.viewapi.Axis;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.Property;
import org.palo.viewapi.internal.io.xml.AxisHandler;
import org.palo.viewapi.internal.io.xml.FormatHandler;
import org.palo.viewapi.internal.io.xml.PropertyHandler;
import org.palo.viewapi.uimodels.formats.Format;

/*     */
/*     */public class CubeViewWriter
/*     */{
  /* 60 */private static CubeViewWriter instance = new CubeViewWriter();

  /*     */
  /* 62 */static final CubeViewWriter getInstance() {
    return instance;
  }

  /*     */
  /*     */
  /*     */public final void toXML(OutputStream out, CubeView view)
  /*     */throws PaloIOException
  /*     */{
    /*     */try
    /*     */{
      /* 70 */toXMLInternal(out, view);
      /*     */} catch (Exception e) {
      /* 72 */PaloIOException pex =
      /* 73 */new PaloIOException("Writing cube view to xml failed!", e);
      /* 74 */pex.setData(view);
      /* 75 */throw pex;
      /*     */}
    /*     */}

  /*     */
  /*     */private final void toXMLInternal(OutputStream output, CubeView view)
    throws Exception
  /*     */{
    /* 81 */PrintWriter w = new PrintWriter(
    /* 82 */new BufferedWriter(new OutputStreamWriter(output, "UTF-8")));
    /*     */try
    /*     */{
      /* 85 */w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
      /* 86 */w.write("<?palocubeview version=\"0.1\"?>\r\n");
      /*     */
      /* 89 */writeViewElement(w, view);
      /*     */
      /* 92 */Property[] properties = view.getProperties();
      /*     */Property[] arrayOfProperty1 = properties;
      /* 93 */int str2 = arrayOfProperty1.length;
      for (int str1 = 0; str1 < str2; ++str1) {
        Property prop = arrayOfProperty1[str1];
        /* 94 */String propertyXML = PropertyHandler.getPersistenceString(prop);
        /* 95 */w.write(propertyXML);
      }
      /*     */
      /*     */
      /* 99 */Axis[] axes = view.getAxes();
      /* 100 */int str3 = axes.length;

      /* 100 */for (str2 = 0; str2 < str3; ++str2) {
        Axis axis = axes[str2];
        /* 101 */String axisXML = AxisHandler.getPersistenceString(axis);
        /* 102 */w.write(axisXML);
      }
      /*     */
      /*     */
      /* 106 */Format[] formats = view.getFormats();
      /* 107 */int propertyXML = formats.length;
      for (str3 = 0; str3 < propertyXML; ++str3) {
        Format format = formats[str3];
        /* 108 */String formatXML = FormatHandler.getPersistenceString(format);
        /* 109 */w.write(formatXML);
      }
      /*     */
      /*     */
      /* 113 */w.write("</view>\r\n");
      /*     */} finally {
      /* 115 */w.close();
      /*     */}
    /*     */}

  /*     */
  /*     */private final void writeViewElement(PrintWriter w, CubeView view) {
    /* 120 */String id = view.getId();
    /* 121 */String name = view.getName();
    /* 122 */String cubeId = view.getCube().getId();
    /* 123 */StringBuffer viewElement = new StringBuffer();
    /* 124 */viewElement.append("<view id=\"");
    /* 125 */viewElement.append(id);
    /* 126 */viewElement.append("\" name=\"");
    /* 127 */viewElement.append(modify(name));
    /* 128 */viewElement.append("\" cube=\"");
    /* 129 */viewElement.append(cubeId);
    /* 130 */viewElement.append("\">\r\n");
    /* 131 */w.write(viewElement.toString());
    /*     */}

  /*     */
  /*     */protected String modify(String x) {
    /* 135 */x = x.replaceAll("&", "&amp;");
    /* 136 */x = x.replaceAll("\"", "&quot;");
    /* 137 */x = x.replaceAll("'", "&apos;");
    /* 138 */x = x.replaceAll("<", "&lt;");
    /* 139 */x = x.replaceAll(">", "&gt;");
    /* 140 */return x;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.io.CubeViewWriter JD-Core
 * Version: 0.5.4
 */