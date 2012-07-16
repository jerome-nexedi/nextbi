/*     */package org.palo.api.ext.subsets.impl;

/*     */
/*     */import org.palo.api.Dimension; /*     */
import org.palo.api.DimensionFilter; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.HierarchyFilter; /*     */
import org.palo.api.SubsetState;

/*     */
/*     */class RegExStateHandler extends AbstractStateHandler
/*     */{
  /*     */public final boolean isFlat()
  /*     */{
    /* 57 */return false;
    /*     */}

  /*     */
  /*     */public final DimensionFilter createDimensionFilter(Dimension dimension) {
    /* 61 */return new DimensionFilter() {
      /*     */public void init(Dimension dimension) {
        /*     */}

      /*     */
      /*     */public boolean acceptElement(Element element) {
        /* 66 */if (RegExStateHandler.this.subsetState.getSearchAttribute() != null) {
          /* 67 */return element.getAttributeValue(
          /* 68 */RegExStateHandler.this.subsetState.getSearchAttribute())
            .toString()
            /* 69 */.matches(RegExStateHandler.this.subsetState.getExpression());
          /*     */}
        /* 71 */return element.getName().matches(
        /* 72 */RegExStateHandler.this.subsetState.getExpression());
        /*     */}

      /*     */
      /*     */public boolean isFlat()
      /*     */{
        /* 77 */return false;
        /*     */}

      /*     */
      /*     */public ElementNode[] postprocessRootNodes(ElementNode[] rootNodes) {
        /* 81 */return null;
        /*     */}
      /*     */
    };
    /*     */}

  /*     */
  /*     */public HierarchyFilter createHierarchyFilter(Hierarchy hierarchy) {
    /* 87 */return new HierarchyFilter() {
      /*     */public void init(Hierarchy hierarchy) {
        /*     */}

      /*     */
      /*     */public boolean acceptElement(Element element) {
        /* 92 */if (RegExStateHandler.this.subsetState.getSearchAttribute() != null) {
          /* 93 */return element.getAttributeValue(
          /* 94 */RegExStateHandler.this.subsetState.getSearchAttribute())
            .toString()
            /* 95 */.matches(RegExStateHandler.this.subsetState.getExpression());
          /*     */}
        /* 97 */return element.getName().matches(
        /* 98 */RegExStateHandler.this.subsetState.getExpression());
        /*     */}

      /*     */
      /*     */public boolean isFlat()
      /*     */{
        /* 103 */return false;
        /*     */}

      /*     */
      /*     */public ElementNode[] postprocessRootNodes(ElementNode[] rootNodes) {
        /* 107 */return null;
        /*     */}
      /*     */
    };
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.impl.RegExStateHandler JD-Core
 * Version: 0.5.4
 */