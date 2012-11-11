/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy.builders;

/**
 * <code>LegacyUtils</code>
 * <p>Provides some useful utility methods </p>
 * 
 * @author Arnd Houben
 * @version $Id: LegacyUtils.java,v 1.1 2007/04/11 16:45:38 ArndHouben Exp $
 */
public class LegacyUtils {

  private static final String SYSTEM_PREFIX = "#_";

  private static final String SYSTEM_POSTFIX = "_";

  private static final String SYSTEM_DB = "System";

  public static boolean isSystemDatabase(String dbName) {
    return dbName.equals(SYSTEM_DB);
  }

  public static boolean isSystemCube(String cubeName) {
    return cubeName.startsWith(SYSTEM_PREFIX);
  }

  public static boolean isSystemDimension(String dimName) {
    return dimName.startsWith(SYSTEM_PREFIX) && dimName.endsWith(SYSTEM_POSTFIX);
  }

  public static boolean isAttributeDimension(String dimName) {
    return dimName.startsWith(SYSTEM_PREFIX) && dimName.endsWith(SYSTEM_POSTFIX);
  }

  public static boolean isAttributeCube(String cubeName) {
    return cubeName.startsWith(SYSTEM_PREFIX);
  }
}
