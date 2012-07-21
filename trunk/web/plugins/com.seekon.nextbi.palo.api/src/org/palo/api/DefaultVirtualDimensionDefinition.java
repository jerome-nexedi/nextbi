/*     */package org.palo.api;

/*     */
/*     */public class DefaultVirtualDimensionDefinition
/*     */implements VirtualDimensionDefinition
/*     */{
  /*     */private final DimensionFilter filter;

  /*     */private final Dimension sourceDimension;

  /*     */private final boolean isFlat;

  /*     */private final Element[] elements;

  /*     */private final ElementNode[] rootNodes;

  /*     */private final String activeSubset;

  /*     */private Hierarchy activeHierarchy;

  /*     */
  /*     */public DefaultVirtualDimensionDefinition(Dimension sourceDimension,
    DimensionFilter filter, String activeSubset)
  /*     */{
    /* 62 */if (sourceDimension == null)
      /* 63 */throw new IllegalArgumentException("sourceDimension cannot be null");
    /* 64 */this.sourceDimension = sourceDimension;
    /* 65 */this.filter = filter;
    /* 66 */this.isFlat = filter.isFlat();
    /* 67 */this.elements = null;
    /* 68 */this.rootNodes = null;
    /* 69 */this.activeSubset = activeSubset;
    /* 70 */this.activeHierarchy = sourceDimension.getDefaultHierarchy();
    /*     */}

  /*     */
  /*     */public DefaultVirtualDimensionDefinition(Dimension sourceDimension,
    Element[] elements, ElementNode[] rootNodes, boolean isFlat, String activeSubset) {
    /* 74 */this.isFlat = isFlat;
    /* 75 */this.elements = elements;
    /* 76 */this.rootNodes = rootNodes;
    /* 77 */this.sourceDimension = sourceDimension;
    /* 78 */this.filter = null;
    /* 79 */this.activeSubset = activeSubset;
    /* 80 */this.activeHierarchy = sourceDimension.getDefaultHierarchy();
    /*     */}

  /*     */
  /*     */public void setActiveHierarchy(Hierarchy hier) {
    /* 84 */this.activeHierarchy = hier;
    /*     */}

  /*     */
  /*     */public Dimension getSourceDimension() {
    /* 88 */return this.sourceDimension;
    /*     */}

  /*     */
  /*     */public DimensionFilter getFilter() {
    /* 92 */return this.filter;
    /*     */}

  /*     */
  /*     */public boolean isFlat() {
    /* 96 */return this.isFlat;
    /*     */}

  /*     */
  /*     */public Element[] getElements() {
    /* 100 */return this.elements;
    /*     */}

  /*     */
  /*     */public ElementNode[] getRootElements() {
    /* 104 */return this.rootNodes;
    /*     */}

  /*     */
  /*     */public final String getActiveSubset() {
    /* 108 */return this.activeSubset;
    /*     */}

  /*     */
  /*     */public final Hierarchy getActiveHierarchy() {
    /* 112 */return this.activeHierarchy;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.DefaultVirtualDimensionDefinition JD-Core
 * Version: 0.5.4
 */