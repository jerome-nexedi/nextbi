/*     */package org.palo.api.impl;

/*     */
/*     */import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.impl.subsets.SubsetPersistence; /*     */
import org.palo.api.impl.views.CubeViewManager;

/*     */
/*     *//** @deprecated */
/*     */class PaloObjects
/*     */{
  /*     */public static final String SYSTEM_PREFIX = "#_";

  /*     */public static final String SYSTEM_POSTFIX = "_";

  /*     */public static final String SYSTEM_DB = "System";

  /*     */
  /*     */public static boolean isSystemDatabase(Database database)
  /*     */{
    /* 57 */String dbName = database.getName();
    /*     */
    /* 59 */return (dbName.equals("System")) ||
    /* 59 */(dbName.equals("AdvancedSystem"));
    /*     */}

  /*     */
  /*     */public static boolean isSystemCube(Cube cube) {
    /* 63 */return isSystemCube(cube.getName());
    /*     */}

  /*     */public static boolean isSystemCube(String cubeName) {
    /* 66 */return cubeName.startsWith("#_");
    /*     */}

  /*     */
  /*     */public static boolean isSystemDimension(Dimension dimension) {
    /* 70 */return isSystemDimension(dimension.getName());
    /*     */}

  /*     */
  /*     */public static boolean isSystemDimension(String dimName) {
    /* 74 */return (dimName.startsWith("#_")) &&
    /* 74 */(dimName.endsWith("_"));
    /*     */}

  /*     */
  /*     */public static boolean isAttributeDimension(Dimension dimension) {
    /* 78 */return isAttributeDimension(dimension.getName());
    /*     */}

  /*     */
  /*     */public static boolean isAttributeDimension(String dimName)
  /*     */{
    /* 83 */return (dimName.startsWith("#_")) &&
    /* 83 */(dimName.endsWith("_"));
    /*     */}

  /*     */
  /*     */public static boolean isAttributeCube(Cube cube) {
    /* 87 */return isAttributeCube(cube.getName());
    /*     */}

  /*     */
  /*     */public static boolean isAttributeCube(String cubeName) {
    /* 91 */return cubeName.startsWith("#_");
    /*     */}

  /*     */
  /*     */public static boolean isSubsetCube(Cube cube) {
    /* 95 */return SubsetPersistence.getInstance().isSubsetCube(cube);
    /*     */}

  /*     */
  /*     */public static boolean isViewsCube(Cube cube) {
    /* 99 */return CubeViewManager.getInstance().isViewCube(cube);
    /*     */}

  /*     */
  /*     */public static String getLeafName(String systemName) {
    /* 103 */if (systemName.startsWith("#_"))
      /* 104 */systemName = systemName.substring(2);
    /* 105 */if (systemName.endsWith("_"))
      /* 106 */systemName = systemName.substring(0, systemName.length() - 1);
    /* 107 */return systemName;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.PaloObjects JD-Core Version: 0.5.4
 */