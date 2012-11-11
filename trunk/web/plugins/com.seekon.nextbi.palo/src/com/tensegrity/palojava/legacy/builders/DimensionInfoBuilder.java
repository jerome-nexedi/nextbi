/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy.builders;

import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.impl.DimensionInfoImpl;

/**
 * <code></code>
 * TODO DOCUMENT ME
 * 
 * @author Arnd Houben
 * @version $Id: DimensionInfoBuilder.java,v 1.2 2007/04/13 13:56:25 ArndHouben Exp $
 */
public class DimensionInfoBuilder {

  //--------------------------------------------------------------------------
  // FACTORY
  //	
  private static final DimensionInfoBuilder instance = new DimensionInfoBuilder();

  public static final DimensionInfoBuilder getInstance() {
    instance.init();
    return instance;
  }

  //--------------------------------------------------------------------------
  // INSTANCE
  //	
  private String name;

  private DatabaseInfo database;

  private DimensionInfoBuilder() {
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final void setDatabase(DatabaseInfo database) {
    this.database = database;
  }

  public final DimensionInfo create() {
    int type = getType();
    if (notEnoughInformation() || type < 0)
      throw new PaloException("Not enough information to create DimensionInfo!!");
    DimensionInfoImpl dimInfo = new DimensionInfoImpl(database, name, type);
    dimInfo.setName(name);

    //		String id = response[0];
    //		String name = response[1];
    //		int elCount = Integer.parseInt(response[2]);
    //		int maxLevel = Integer.parseInt(response[3]);
    //		int maxIndent = Integer.parseInt(response[4]);
    //		int maxDepth = Integer.parseInt(response[5]);
    //		int type = Integer.parseInt(response[6]);
    //		String attrDimId = response[7];
    //		String attrCubeId = response[8];
    //		String rightsCubeId = response[9];
    //		int token = Integer.parseInt(response[10]);
    //		DimensionInfoImpl info = 
    //			new DimensionInfoImpl((DatabaseInfo)parent,id,name,type);
    //		info.setElementCount(elCount);
    //		info.setMaxLevel(maxLevel);
    //		info.setMaxIndent(maxIndent);
    //		info.setMaxDepth(maxDepth);
    //		info.setAttributeDimension(attrDimId);
    //		info.setAttributeCube(attrCubeId);
    //		info.setRightsCube(rightsCubeId);
    //		info.setToken(token);
    //		return info;
    //
    return dimInfo;
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  private final void init() {
    name = null;
    database = null;
  }

  private final boolean notEnoughInformation() {
    return (name == null) || (database == null);
  }

  private final int getType() {
    if (LegacyUtils.isSystemDatabase(name))
      return DimensionInfo.TYPE_SYSTEM;
    else if (LegacyUtils.isAttributeCube(name))
      return DimensionInfo.TYPE_ATTRIBUTE;

    return DimensionInfo.TYPE_NORMAL;
  }

}
