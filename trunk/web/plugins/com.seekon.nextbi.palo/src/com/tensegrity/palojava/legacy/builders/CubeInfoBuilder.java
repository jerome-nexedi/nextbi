/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy.builders;

import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.impl.CubeInfoImpl;

/**
 * <code></code>
 * TODO DOCUMENT ME
 * 
 * @author Arnd Houben
 * @version $Id: CubeInfoBuilder.java,v 1.2 2007/08/14 20:19:14 ArndHouben Exp $
 */
public class CubeInfoBuilder {

  //--------------------------------------------------------------------------
  // FACTORY
  //	
  private static final CubeInfoBuilder instance = new CubeInfoBuilder();

  public static final CubeInfoBuilder getInstance() {
    instance.init();
    return instance;
  }

  //--------------------------------------------------------------------------
  // INSTANCE
  //	
  private String name;

  private DatabaseInfo database;

  private String[] dimensionNames;

  private CubeInfoBuilder() {
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final void setDimensions(String[] dimensionNames) {
    this.dimensionNames = dimensionNames;
  }

  public final void setDatabase(DatabaseInfo database) {
    this.database = database;
  }

  public final CubeInfo create() {
    int type = getType();
    if (notEnoughInformation() || type < 0)
      throw new PaloException("Not enough information to create CubeInfo!!");
    CubeInfoImpl cubeInfo = new CubeInfoImpl(database, name, type, dimensionNames);
    cubeInfo.setName(name);
    //		try {
    //			String id = response[0];
    //			String name = response[1];
    //			int dimCount = Integer.parseInt(response[2]);
    //			String[] dims = getDimensions(response[3]);
    //			int cellCount = Integer.parseInt(response[4]);
    //			int filledCellCount = Integer.parseInt(response[5]);
    //			int status = Integer.parseInt(response[6]);
    //			int type = Integer.parseInt(response[7]);
    //			int token = Integer.parseInt(response[8]);
    //			CubeInfoImpl info = new CubeInfoImpl((DatabaseInfo)parent,id,name,type,dims);
    //			//now set the rest:
    //			info.setDimensionCount(dimCount);
    //			info.setCellCount(cellCount);
    //			info.setFilledCellCount(filledCellCount);
    //			info.setStatus(status);
    //			info.setToken(token);
    //			return info;
    //		
    return cubeInfo;
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  private final void init() {
    name = null;
    database = null;
    dimensionNames = null;
  }

  private final boolean notEnoughInformation() {
    return (name == null) || (database == null) || (dimensionNames == null);
  }

  private final int getType() {
    if (LegacyUtils.isSystemDatabase(name))
      return CubeInfo.TYPE_SYSTEM;
    else if (LegacyUtils.isAttributeCube(name))
      return CubeInfo.TYPE_ATTRIBUTE;

    return CubeInfo.TYPE_NORMAL;
  }

}
