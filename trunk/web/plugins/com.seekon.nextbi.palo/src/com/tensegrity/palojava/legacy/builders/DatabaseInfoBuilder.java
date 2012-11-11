/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy.builders;

import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.impl.DatabaseInfoImpl;

/**
 * <code></code>
 * TODO DOCUMENT ME
 * 
 * @author Arnd Houben
 * @version $Id: DatabaseInfoBuilder.java,v 1.2 2007/04/13 13:56:25 ArndHouben Exp $
 */
public class DatabaseInfoBuilder {

  //--------------------------------------------------------------------------
  // FACTORY
  //	
  private static final DatabaseInfoBuilder instance = new DatabaseInfoBuilder();

  public static final DatabaseInfoBuilder getInstance() {
    instance.init();
    return instance;
  }

  //--------------------------------------------------------------------------
  // INSTANCE
  //	
  private String name;

  private DatabaseInfoBuilder() {
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final DatabaseInfo create() {
    int type = getType();
    if (name == null || type < 0)
      throw new PaloException("Not enough information to create DatabaseInfo!!");
    DatabaseInfoImpl dbInfo = new DatabaseInfoImpl(name, type);
    dbInfo.setName(name);

    //		String id = response[0];
    //		String name = response[1];
    //		int dimCount = Integer.parseInt(response[2]);
    //		int cubeCount = Integer.parseInt(response[3]);
    //		int status = Integer.parseInt(response[4]);
    //		int type = Integer.parseInt(response[5]);
    //		int token = Integer.parseInt(response[6]);
    //		DatabaseInfoImpl info = new DatabaseInfoImpl(id,name,type);
    //		info.setDimensionCount(dimCount);
    //		info.setCubeCount(cubeCount);
    //		info.setStatus(status);
    //		info.setToken(token);

    return dbInfo;
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  private final void init() {
    name = null;
  }

  private final int getType() {
    if (LegacyUtils.isSystemDatabase(name))
      return DatabaseInfo.TYPE_SYSTEM;

    return DatabaseInfo.TYPE_NORMAL;
  }
}
