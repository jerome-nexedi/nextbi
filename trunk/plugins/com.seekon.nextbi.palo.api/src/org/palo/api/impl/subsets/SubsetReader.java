/*     */package org.palo.api.impl.subsets;

/*     */
/*     */import java.io.InputStream; /*     */
import java.io.PrintStream; /*     */
import javax.xml.parsers.SAXParser; /*     */
import javax.xml.parsers.SAXParserFactory; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Subset;

/*     */
/*     */class SubsetReader
/*     */{
  /* 53 */private static SubsetReader instance = new SubsetReader();

  /*     */
  /*     */static final SubsetReader getInstance() {
    /* 56 */return instance;
    /*     */}

  /*     */
  /*     */final Subset fromXML(InputStream input, String key, Database database)
  /*     */{
    /*     */try
    /*     */{
      /* 65 */SAXParserFactory sF = SAXParserFactory.newInstance();
      /* 66 */SAXParser parser = null;
      /* 67 */SubsetXMLHandler xmlHandler = new SubsetXMLHandler(database, key);
      /* 68 */if (database.getConnection().isLegacy())
        /* 69 */xmlHandler.useLegacy();
      /* 70 */parser = sF.newSAXParser();
      /* 71 */parser.parse(input, xmlHandler);
      /* 72 */return xmlHandler.getSubset();
      /*     */}
    /*     */catch (Exception ex)
    /*     */{
      /* 98 */System.err
      /* 99 */.println("XML Exception during subset loading: " + ex.getMessage());
      /* 100 */}
    return null;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.subsets.SubsetReader JD-Core Version: 0.5.4
 */