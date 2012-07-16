/*     */package org.palo.api.impl.views;

/*     */
/*     */import java.io.InputStream; /*     */
import java.util.Arrays; /*     */
import java.util.Collection; /*     */
import javax.xml.parsers.SAXParser; /*     */
import javax.xml.parsers.SAXParserFactory; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.CubeView; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.impl.PersistenceErrorImpl;

/*     */
/*     */class CubeViewReader
/*     */{
  /* 60 */private static CubeViewReader instance = new CubeViewReader();

  /*     */
  /* 62 */static CubeViewReader getInstance() {
    return instance;
  }

  /*     */
  /*     */
  /*     */CubeView fromXML(InputStream input, String viewId, Database database,
    Collection errors)
  /*     */{
    /* 74 */CubeViewXMLHandler xmlHandler = new CubeViewXMLHandler(database, viewId);
    /*     */
    /* 76 */if (database.getConnection().isLegacy())
      /* 77 */xmlHandler.useLegacy();
    /*     */try {
      /* 79 */SAXParserFactory sF = SAXParserFactory.newInstance();
      /* 80 */SAXParser parser = sF.newSAXParser();
      /* 81 */parser.parse(input, xmlHandler);
      /*     */}
    /*     */catch (Exception e) {
      /* 84 */xmlHandler.addViewError(
      /* 87 */new PersistenceErrorImpl("Failed to load cube view", viewId, null,
        database, viewId,
        /* 86 */32, null,
        /* 87 */0));
      /*     */}
    /*     */
    /* 95 */if (xmlHandler.hasErrors())
      /* 96 */errors.addAll(Arrays.asList(xmlHandler.getErrors()));
    /* 97 */return xmlHandler.getCubeView();
    /*     */}

  /*     */
  /*     */static final String getLeafName(String name) {
    /* 101 */int cutIt = name.indexOf("@@");
    /* 102 */if (cutIt > 0)
      /* 103 */name = name.substring(0, cutIt);
    /* 104 */return name;
    /*     */}

  /*     */
  /*     */static final int[] getRepetitions(String reps) {
    /* 108 */if (reps == null)
      /* 109 */return new int[1];
    /* 110 */String[] _reps = reps.split(",");
    /* 111 */int[] repetitions = new int[_reps.length];
    /* 112 */for (int i = 0; i < _reps.length; ++i) {
      /* 113 */repetitions[i] = Integer.parseInt(_reps[i]);
      /*     */}
    /* 115 */return repetitions;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.views.CubeViewReader JD-Core Version: 0.5.4
 */