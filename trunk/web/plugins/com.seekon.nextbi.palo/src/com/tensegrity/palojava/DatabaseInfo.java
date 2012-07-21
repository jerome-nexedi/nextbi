package com.tensegrity.palojava;

public abstract interface DatabaseInfo extends PaloInfo, PaloConstants {
  public static final int STATUS_UNLOADED = 0;

  public static final int STATUS_LOADED = 1;

  public static final int STATUS_CHANGED = 2;

  public abstract String getName();

  public abstract int getCubeCount();

  public abstract int getDimensionCount();

  public abstract int getStatus();

  public abstract int getToken();

  public abstract boolean isSystem();

  public abstract boolean isUserInfo();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.DatabaseInfo JD-Core Version: 0.5.4
 */