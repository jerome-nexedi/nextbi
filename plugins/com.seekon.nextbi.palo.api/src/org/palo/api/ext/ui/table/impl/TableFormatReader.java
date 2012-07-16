/*     */package org.palo.api.ext.ui.table.impl;

/*     */
/*     */import java.io.ByteArrayInputStream; /*     */
import javax.xml.parsers.SAXParser; /*     */
import javax.xml.parsers.SAXParserFactory; /*     */
import org.palo.api.ext.ui.table.TableFormat; /*     */
import org.palo.api.impl.xml.BaseXMLHandler; /*     */
import org.palo.api.impl.xml.StartHandler; /*     */
import org.xml.sax.Attributes;

/*     */
/*     */class TableFormatReader
/*     */{
  /* 62 */private static final TableFormatReader instance = new TableFormatReader();

  /*     */
  /* 64 */static final TableFormatReader getInstance() {
    return instance;
  }

  /*     */
  /*     */
  /*     */final TableFormat fromXML(String xmlStr)
  /*     */{
    /* 80 */if (xmlStr == null)
      /* 81 */return null;
    /* 82 */FormatXMLHandler defaultHandler = new FormatXMLHandler();
    /* 83 */SAXParserFactory sF = SAXParserFactory.newInstance();
    /*     */
    /* 85 */SAXParser parser = null;
    /*     */try
    /*     */{
      /* 88 */ByteArrayInputStream bin = new ByteArrayInputStream(xmlStr
        .getBytes("UTF-8"));
      /* 89 */parser = sF.newSAXParser();
      /* 90 */parser.parse(bin, defaultHandler);
      /* 91 */return defaultHandler.getFormatter();
      /*     */}
    /*     */catch (Exception localException) {
      /*     */}
    /* 95 */return null;
    /*     */}

  /*     */
  /*     */class FormatXMLHandler extends BaseXMLHandler {
    /*     */private final FormatBuilder cellFmtBuilder;

    /*     */private final FormatBuilder headerFmtBuilder;

    /*     */
    /*     */FormatXMLHandler() {
      /* 103 */this.cellFmtBuilder = new FormatBuilder();
      /* 104 */this.headerFmtBuilder = new FormatBuilder();
      /*     */
      /* 106 */putStartHandler("format/priority", new StartHandler()
      /*     */{
        /*     */public void startElement(String uri, String localName,
          String qName, Attributes attributes)
        /*     */{
          /* 110 */String prio = attributes.getValue("level");
          /*     */
          /* 113 */TableFormatReader.FormatXMLHandler.this.cellFmtBuilder
            .setPriority(prio);
          /* 114 */TableFormatReader.FormatXMLHandler.this.headerFmtBuilder
            .setPriority(prio);
          /*     */}
        /*     */
      });
      /* 117 */putStartHandler("format/formatcells/numberformat",
      /* 118 */new StartHandler()
      /*     */{
        /*     */public void startElement(String uri, String localName,
          String qName, Attributes attributes) {
          /* 121 */String str = attributes.getValue("template");
          /* 122 */TableFormatReader.FormatXMLHandler.this.cellFmtBuilder
            .setNumberFormatTemplate(str);
          /*     */}
        /*     */
      });
      /* 125 */putStartHandler("format/formatcells/background",
      /* 126 */new StartHandler()
      /*     */{
        /*     */public void startElement(String uri, String localName,
          String qName, Attributes attributes) {
          /* 129 */TableFormatReader.FormatXMLHandler.this.setBackground(
            TableFormatReader.FormatXMLHandler.this.cellFmtBuilder, attributes);
          /*     */}
        /*     */
      });
      /* 132 */putStartHandler("format/formatheader/background",
      /* 133 */new StartHandler()
      /*     */{
        /*     */public void startElement(String uri, String localName,
          String qName, Attributes attributes) {
          /* 136 */TableFormatReader.FormatXMLHandler.this.setBackground(
            TableFormatReader.FormatXMLHandler.this.headerFmtBuilder, attributes);
          /*     */}
        /*     */
      });
      /* 139 */putStartHandler("format/formatcells/font", new StartHandler()
      /*     */{
        /*     */public void startElement(String uri, String localName,
          String qName, Attributes attributes) {
          /* 142 */TableFormatReader.FormatXMLHandler.this.setFont(
            TableFormatReader.FormatXMLHandler.this.cellFmtBuilder, attributes);
          /*     */}
        /*     */
      });
      /* 145 */putStartHandler("format/formatheader/font", new StartHandler()
      /*     */{
        /*     */public void startElement(String uri, String localName,
          String qName, Attributes attributes) {
          /* 148 */TableFormatReader.FormatXMLHandler.this.setFont(
            TableFormatReader.FormatXMLHandler.this.headerFmtBuilder, attributes);
          /*     */}
        /*     */
      });
      /*     */}

    /*     */
    /*     */private void setBackground(FormatBuilder fmtBuilder,
      Attributes attributes)
    /*     */{
      /* 155 */fmtBuilder.setBackGroundColor(attributes.getValue("r"),
      /* 156 */attributes.getValue("g"), attributes.getValue("b"));
      /*     */}

    /*     */
    /*     */private void setFont(FormatBuilder fmtBuilder, Attributes attributes) {
      /* 160 */fmtBuilder.setFontName(attributes.getValue("name"));
      /* 161 */fmtBuilder.setFontSize(attributes.getValue("size"));
      /* 162 */fmtBuilder.setBold(attributes.getValue("bold"));
      /* 163 */fmtBuilder.setItalic(attributes.getValue("italic"));
      /* 164 */fmtBuilder.setUnderlined(attributes.getValue("underline"));
      /* 165 */fmtBuilder.setFontColor(attributes.getValue("r"),
      /* 166 */attributes.getValue("g"), attributes.getValue("b"));
      /*     */}

    /*     */
    /*     */private final TableFormat getFormatter() {
      /* 170 */DefaultTableFormat tblFormatter = new DefaultTableFormat();
      /*     */
      /* 172 */tblFormatter.setCellFormat(this.cellFmtBuilder.create());
      /* 173 */tblFormatter.setHeaderFormat(this.headerFmtBuilder.create());
      /* 174 */return tblFormatter;
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.ui.table.impl.TableFormatReader JD-Core
 * Version: 0.5.4
 */