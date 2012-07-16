/*     */package org.palo.api.impl.subsets;

/*     */
/*     */import java.util.HashMap; /*     */
import java.util.Iterator; /*     */
import java.util.LinkedHashSet; /*     */
import java.util.Map; /*     */
import java.util.Set; /*     */
import org.palo.api.Attribute; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.SubsetState; /*     */
import org.palo.api.utils.ElementUtilities;

/*     */
/*     */public class SubsetStateBuilder
/*     */{
  /*     */private String id;

  /*     */private String name;

  /*     */private String expression;

  /*     */private Attribute searchAttribute;

  /* 67 */private final Set elements = new LinkedHashSet();

  /* 68 */private final Map elPaths = new HashMap();

  /* 69 */private final Map elPos = new HashMap();

  /*     */
  /*     */public final void setId(String id) {
    /* 72 */this.id = id;
    /*     */}

  /*     */
  /*     */final void setName(String name) {
    /* 76 */this.name = name;
    /*     */}

  /*     */
  /*     */final void setExpression(String expression) {
    /* 80 */this.expression = expression;
    /*     */}

  /*     */
  /*     */final void setSearchAttribute(Attribute searchAttribute) {
    /* 84 */this.searchAttribute = searchAttribute;
    /*     */}

  /*     */
  /*     */final void addElement(Element element) {
    /* 88 */if (element != null)
      /* 89 */this.elements.add(element);
    /*     */}

  /*     */
  /*     */final void setPaths(Element element, String paths) {
    /* 93 */if (element == null) {
      /* 94 */return;
      /*     */}
    /* 96 */if (paths == null)
      /* 97 */paths = ElementUtilities.getPaths(element);
    /* 98 */this.elPaths.put(element, paths);
    /*     */}

  /*     */
  /*     */final void setPositions(Element element, String positions) {
    /* 102 */if ((element == null) || (positions == null))
      /* 103 */return;
    /* 104 */this.elPos.put(element, positions);
    /*     */}

  /*     */
  /*     */public final SubsetState createState() {
    /* 108 */SubsetStateImpl state = new SubsetStateImpl(this.id);
    /* 109 */if (this.name != null)
      /* 110 */state.setName(this.name);
    /* 111 */if (this.expression != null)
      /* 112 */state.setExpression(this.expression);
    /* 113 */if (this.searchAttribute != null)
      /* 114 */state.setSearchAttribute(this.searchAttribute);
    /* 115 */if (!this.elements.isEmpty()) {
      /* 116 */for (Iterator it = this.elements.iterator(); it.hasNext();) {
        /* 117 */Element element = (Element) it.next();
        /* 118 */state.addVisibleElment(element);
        /* 119 */state.setPaths(element, (String) this.elPaths.get(element));
        /* 120 */state.setPosition(element, (String) this.elPos.get(element));
        /*     */}
      /*     */}
    /* 123 */return state;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.subsets.SubsetStateBuilder JD-Core Version:
 * 0.5.4
 */