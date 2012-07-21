/*    */package org.palo.api.ext.subsets.impl;

/*    */
/*    */import org.palo.api.Dimension; /*    */
import org.palo.api.DimensionFilter; /*    */
import org.palo.api.Hierarchy; /*    */
import org.palo.api.HierarchyFilter;

/*    */
/*    */class FlatStateHandler extends AbstractStateHandler
/*    */{
  /*    */public final boolean isFlat()
  /*    */{
    /* 66 */return true;
    /*    */}

  /*    */
  /*    */public final DimensionFilter createDimensionFilter(Dimension dimension) {
    /* 70 */return null;
    /*    */}

  /*    */
  /*    */public final HierarchyFilter createHierarchyFilter(Hierarchy hierarchy) {
    /* 74 */return new FlatStateFilter(this.subsetState);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ext.subsets.impl.FlatStateHandler JD-Core
 * Version: 0.5.4
 */