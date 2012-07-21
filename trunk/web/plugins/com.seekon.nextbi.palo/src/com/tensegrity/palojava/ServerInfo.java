package com.tensegrity.palojava;

public abstract interface ServerInfo extends PaloInfo {
  public static final int ENCRYPTION_NONE = 0;

  public static final int ENCRYPTION_OPTIONAL = 1;

  public static final int ENCRYPTION_REQUIRED = 2;

  public abstract int getMajor();

  public abstract int getMinor();

  public abstract int getBugfixVersion();

  public abstract int getBuildNumber();

  public abstract boolean isLegacy();

  public abstract int getHttpsPort();

  public abstract int getEncryption();

  public abstract String getName();

  public abstract String getServerType();

  public abstract String getVersion();

  public abstract String[] getProperties();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.ServerInfo JD-Core Version: 0.5.4
 */