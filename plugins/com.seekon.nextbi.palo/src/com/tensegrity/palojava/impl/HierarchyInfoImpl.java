/*    */package com.tensegrity.palojava.impl;

/*    */
/*    */import com.tensegrity.palojava.DimensionInfo; /*    */
import com.tensegrity.palojava.HierarchyInfo;

/*    */
/*    */public class HierarchyInfoImpl
/*    */implements HierarchyInfo
/*    */{
  /*    */private DimensionInfo dimension;

  /*    */
  /*    */public HierarchyInfoImpl(DimensionInfo dimensionInfo)
  /*    */{
    /* 41 */this.dimension = dimensionInfo;
    /*    */}

  /*    */
  /*    */public int getDimensionCount() {
    /* 45 */return 1;
    /*    */}

  /*    */
  /*    */public String getName() {
    /* 49 */return this.dimension.getName();
    /*    */}

  /*    */
  /*    */public String getId() {
    /* 53 */return this.dimension.getId();
    /*    */}

  /*    */
  /*    */public int getType() {
    /* 57 */return this.dimension.getType();
    /*    */}

  /*    */
  /*    */public void rename(String name) {
    /*    */}

  /*    */
  /*    */public boolean canBeModified() {
    /* 64 */return true;
    /*    */}

  /*    */
  /*    */public boolean canCreateChildren() {
    /* 68 */return true;
    /*    */}

  /*    */
  /*    */public DimensionInfo getDimension() {
    /* 72 */return this.dimension;
    /*    */}

  /*    */
  /*    */public int getElementCount() {
    /* 76 */return this.dimension.getElementCount();
    /*    */}

  /*    */
  /*    */public int getMaxDepth() {
    /* 80 */return this.dimension.getMaxDepth();
    /*    */}

  /*    */
  /*    */public int getMaxLevel() {
    /* 84 */return this.dimension.getMaxLevel();
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.impl.HierarchyInfoImpl JD-Core
 * Version: 0.5.4
 */