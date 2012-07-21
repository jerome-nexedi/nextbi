package com.tensegrity.palojava;

import java.math.BigInteger;

public abstract interface CubeInfo extends PaloInfo, PaloConstants {
  public static final int STATUS_UNLOADED = 0;

  public static final int STATUS_LOADED = 1;

  public static final int STATUS_CHANGED = 2;

  public abstract String getName();

  public abstract DatabaseInfo getDatabase();

  public abstract String[] getDimensions();

  public abstract BigInteger getCellCount();

  public abstract int getDimensionCount();

  public abstract BigInteger getFilledCellCount();

  public abstract int getStatus();

  public abstract int getToken();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.CubeInfo JD-Core Version: 0.5.4
 */