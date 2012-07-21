/*     */package com.tensegrity.palojava.impl;

/*     */
/*     */import com.tensegrity.palojava.DatabaseInfo; /*     */
import com.tensegrity.palojava.DimensionInfo; /*     */
import com.tensegrity.palojava.HierarchyInfo;

/*     */
/*     */public class DimensionInfoImpl
/*     */implements DimensionInfo
/*     */{
  /*     */private final String id;

  /*     */private final int type;

  /*     */private final DatabaseInfo database;

  /*     */private String name;

  /*     */private int elCount;

  /*     */private int maxLevel;

  /*     */private int maxIndent;

  /*     */private int maxDepth;

  /*     */private String attrDimId;

  /*     */private String attrCubeId;

  /*     */private String rightsCubeId;

  /*     */private int token;

  /*     */private final HierarchyInfo defaultHierarchy;

  /*     */
  /*     */public DimensionInfoImpl(DatabaseInfo database, String id, int type)
  /*     */{
    /* 57 */this.id = id;
    /* 58 */this.type = type;
    /* 59 */this.database = database;
    /* 60 */this.defaultHierarchy = new HierarchyInfoImpl(this);
    /*     */}

  /*     */
  /*     */public final DatabaseInfo getDatabase() {
    /* 64 */return this.database;
    /*     */}

  /*     */
  /*     */public final String getId() {
    /* 68 */return this.id;
    /*     */}

  /*     */
  /*     */public final synchronized String getName() {
    /* 72 */return this.name;
    /*     */}

  /*     */
  /*     */public final int getType() {
    /* 76 */return this.type;
    /*     */}

  /*     */
  /*     */public final synchronized void setName(String name) {
    /* 80 */this.name = name;
    /*     */}

  /*     */
  /*     */public final String getAttributeCube() {
    /* 84 */return this.attrCubeId;
    /*     */}

  /*     */
  /*     */public final void setAttributeCube(String attrCubeId) {
    /* 88 */this.attrCubeId = attrCubeId;
    /*     */}

  /*     */
  /*     */public final synchronized String getAttributeDimension() {
    /* 92 */return this.attrDimId;
    /*     */}

  /*     */
  /*     */public final synchronized void setAttributeDimension(String attrDimId) {
    /* 96 */this.attrDimId = attrDimId;
    /*     */}

  /*     */
  /*     */public final synchronized int getElementCount() {
    /* 100 */return this.elCount;
    /*     */}

  /*     */
  /*     */public final synchronized void setElementCount(int elCount) {
    /* 104 */this.elCount = elCount;
    /*     */}

  /*     */
  /*     */public final synchronized int getMaxDepth() {
    /* 108 */return this.maxDepth;
    /*     */}

  /*     */
  /*     */public final synchronized void setMaxDepth(int maxDepth) {
    /* 112 */this.maxDepth = maxDepth;
    /*     */}

  /*     */
  /*     */public final synchronized int getMaxIndent() {
    /* 116 */return this.maxIndent;
    /*     */}

  /*     */
  /*     */public final synchronized void setMaxIndent(int maxIndent) {
    /* 120 */this.maxIndent = maxIndent;
    /*     */}

  /*     */
  /*     */public final synchronized int getMaxLevel() {
    /* 124 */return this.maxLevel;
    /*     */}

  /*     */
  /*     */public final synchronized void setMaxLevel(int maxLevel) {
    /* 128 */this.maxLevel = maxLevel;
    /*     */}

  /*     */
  /*     */public final synchronized String getRightsCube() {
    /* 132 */return this.rightsCubeId;
    /*     */}

  /*     */
  /*     */public final synchronized void setRightsCube(String rightsCubeId) {
    /* 136 */this.rightsCubeId = rightsCubeId;
    /*     */}

  /*     */
  /*     */public final synchronized int getToken() {
    /* 140 */return this.token;
    /*     */}

  /*     */
  /*     */public final synchronized void setToken(int token) {
    /* 144 */this.token = token;
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 148 */return true;
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 152 */return true;
    /*     */}

  /*     */
  /*     */public HierarchyInfo getActiveHierarchy() {
    /* 156 */return this.defaultHierarchy;
    /*     */}

  /*     */
  /*     */public HierarchyInfo getDefaultHierarchy() {
    /* 160 */return this.defaultHierarchy;
    /*     */}

  /*     */
  /*     */public void setActiveHierarchy(HierarchyInfo hier)
  /*     */{
    /*     */}

  /*     */
  /*     */public HierarchyInfo[] getHierarchies() {
    /* 168 */return new HierarchyInfo[] { this.defaultHierarchy };
    /*     */}

  /*     */
  /*     */public int getHierarchyCount() {
    /* 172 */return 1;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.impl.DimensionInfoImpl JD-Core
 * Version: 0.5.4
 */