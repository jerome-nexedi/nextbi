package com.tensegrity.palojava;

public abstract interface RuleInfo extends PaloInfo {
  public static final int UNDEFINED = -1;

  public abstract String getDefinition();

  public abstract CubeInfo getCube();

  public abstract String getExternalIdentifier();

  public abstract String getComment();

  public abstract long getTimestamp();

  public abstract boolean useExternalIdentifier();

  public abstract boolean isActive();

  public abstract void setActive(boolean paramBoolean);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.RuleInfo JD-Core Version: 0.5.4
 */