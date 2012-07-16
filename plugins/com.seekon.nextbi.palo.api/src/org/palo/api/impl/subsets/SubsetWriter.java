/*     */package org.palo.api.impl.subsets;

/*     */
/*     */import java.io.BufferedWriter; /*     */
import java.io.OutputStream; /*     */
import java.io.OutputStreamWriter; /*     */
import java.io.PrintStream; /*     */
import java.io.PrintWriter; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Subset; /*     */
import org.palo.api.SubsetState; /*     */
import org.palo.api.impl.xml.XMLUtil;

/*     */
/*     */class SubsetWriter
/*     */{
  /* 57 */private static SubsetWriter instance = new SubsetWriter();

  /*     */
  /* 59 */static final SubsetWriter getInstance() {
    return instance;
  }

  /*     */
  /*     */
  /*     */final void toXML(OutputStream output, Subset subset)
  /*     */{
    /*     */try
    /*     */{
      /* 67 */toXMLInternal(output, subset);
      /*     */} catch (Exception e) {
      /* 69 */System.err.println(
      /* 70 */"SubsetWriter.toXML: " + e.getLocalizedMessage());
      /*     */}
    /*     */}

  /*     */
  /*     */private final void toXMLInternal(OutputStream output, Subset subset)
    throws Exception
  /*     */{
    /* 76 */PrintWriter w = new PrintWriter(
    /* 77 */new BufferedWriter(new OutputStreamWriter(output, "UTF-8")));
    /*     */try {
      /* 79 */w.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
      /* 80 */w.write("<?palosubset version=\"1.1\"?>\r\n");
      /* 81 */w.write("<subset\r\n");
      /* 82 */w.write("  id=\"" + XMLUtil.printQuoted(subset.getId()) + "\"\r\n");
      /* 83 */w.write("  name=\"" + XMLUtil.printQuoted(subset.getName())
        + "\"\r\n");
      /* 84 */w.write("  description=\""
        + XMLUtil.printQuoted(subset.getDescription()) + "\"\r\n");
      /* 85 */w.write("  sourceDimensionId=\""
        + XMLUtil.printQuoted(subset.getDimension().getId()) + "\"\r\n");
      /* 86 */if (subset.getAlias() != null)
        /* 87 */w.write("  alias=\""
          + XMLUtil.printQuoted(subset.getAlias().getName()) + "\"\r\n");
      /* 88 */SubsetState activeState = subset.getActiveState();
      /* 89 */String activeStateId = (activeState == null) ? "" : activeState
        .getId();
      /* 90 */w.write("  activeStateId=\"" + XMLUtil.printQuoted(activeStateId)
        + "\">\r\n");
      /*     */
      /* 93 */SubsetState[] states = subset.getStates();
      /* 94 */for (int i = 0; i < states.length; ++i) {
        /* 95 */SubsetState state = states[i];
        /* 96 */w.write("<state\r\n");
        /* 97 */w.write("  id=\"" + XMLUtil.printQuoted(state.getId()) + "\"\r\n");
        /* 98 */String name = state.getName();
        /* 99 */if (name == null)
          /* 100 */name = "";
        /* 101 */w.write("  name=\"" + XMLUtil.printQuoted(name) + "\">\r\n");
        /* 102 */writeExpression(w, state.getExpression());
        /* 103 */if (state.getSearchAttribute() != null)
          /* 104 */writeSearchAttribute(w, state.getSearchAttribute());
        /* 105 */writeElements(w, state);
        /* 106 */w.write("</state>\r\n");
        /*     */}
      /* 108 */w.write("</subset>\r\n");
      /*     */} finally {
      /* 110 */w.close();
      /*     */}
    /*     */}

  /*     */
  /*     */private final void writeExpression(PrintWriter w, String expression) {
    /* 115 */w.write("<expression expr=\"" + XMLUtil.printQuoted(expression)
      + "\"/>\r\n");
    /*     */}

  /*     */
  /*     */private final void writeSearchAttribute(PrintWriter w,
    Attribute searchAttribute) {
    /* 119 */w.write("<search attribute=\""
      + XMLUtil.printQuoted(searchAttribute.getName()) + "\"/>\r\n");
    /*     */}

  /*     */
  /*     */private final void writeElements(PrintWriter w, SubsetState state) {
    /* 123 */Element[] elements = state.getVisibleElements();
    /* 124 */StringBuffer elTag = new StringBuffer();
    /* 125 */for (int i = 0; i < elements.length; ++i)
    /*     */{
      /* 129 */elTag.append("<element id=\"");
      /* 130 */elTag.append(XMLUtil.printQuoted(elements[i].getId()));
      /* 131 */elTag.append("\" ");
      /* 132 */String elPaths =
      /* 133 */((SubsetStateImpl) state).getPathsAsString(elements[i]);
      /* 134 */if (elPaths != null) {
        /* 135 */elTag.append("paths=\"");
        /* 136 */elTag.append(XMLUtil.printQuoted(elPaths));
        /* 137 */elTag.append("\" ");
        /*     */}
      /* 139 */String positions =
      /* 140 */((SubsetStateImpl) state).getPositionsAsString(elements[i]);
      /* 141 */if ((positions != null) && (positions.length() > 0)) {
        /* 142 */elTag.append("pos=\"");
        /* 143 */elTag.append(XMLUtil.printQuoted(positions));
        /* 144 */elTag.append("\" ");
        /*     */}
      /* 146 */elTag.append("/>\r\n");
      /*     */}
    /* 148 */w.write(elTag.toString());
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.subsets.SubsetWriter JD-Core Version: 0.5.4
 */