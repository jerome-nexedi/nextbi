/*     */package org.palo.api.ext.subsets.impl;

/*     */
/*     */import java.util.Arrays; /*     */
import java.util.HashSet; /*     */
import java.util.Set; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.DimensionFilter; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ElementNode; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.HierarchyFilter; /*     */
import org.palo.api.SubsetState;

/*     */
/*     */class HierarchicalStateHandler extends AbstractStateHandler
/*     */{
  /*     */public final boolean isFlat()
  /*     */{
    /* 60 */return false;
    /*     */}

  /*     */
  /*     */public final DimensionFilter createDimensionFilter(Dimension dimension) {
    /* 64 */return new DimensionFilter()
    /*     */{
      /*     */Set elementSet;

      /*     */
      /*     */public void init(Dimension dimension)
      /*     */{
        /*     */}

      /*     */
      /*     */public boolean acceptElement(Element element)
      /*     */{
        /* 76 */return this.elementSet.contains(element);
        /*     */}

      /*     */
      /*     */public boolean isFlat() {
        /* 80 */return false;
        /*     */}

      /*     */
      /*     */public ElementNode[] postprocessRootNodes(ElementNode[] rootNodes) {
        /* 84 */return null;
        /*     */}
      /*     */
    };
    /*     */}

  /*     */
  /*     */public HierarchyFilter createHierarchyFilter(Hierarchy hierarchy) {
    /* 90 */return new HierarchyFilter()
    /*     */{
      /*     */Set elementSet;

      /*     */
      /*     */public void init(Hierarchy hierarchy)
      /*     */{
        /*     */}

      /*     */
      /*     */public boolean acceptElement(Element element)
      /*     */{
        /* 102 */return this.elementSet.contains(element);
        /*     */}

      /*     */
      /*     */public boolean isFlat() {
        /* 106 */return false;
        /*     */}

      /*     */
      /*     */public ElementNode[] postprocessRootNodes(ElementNode[] rootNodes) {
        /* 110 */return null;
        /*     */}
      /*     */
    };
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.impl.HierarchicalStateHandler
 * JD-Core Version: 0.5.4
 */