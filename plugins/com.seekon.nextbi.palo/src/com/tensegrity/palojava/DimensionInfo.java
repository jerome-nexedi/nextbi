package com.tensegrity.palojava;

public abstract interface DimensionInfo extends PaloInfo, PaloConstants
{
  public abstract DatabaseInfo getDatabase();

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract String getAttributeCube();

  public abstract String getAttributeDimension();

  public abstract int getElementCount();

  public abstract int getMaxDepth();

  public abstract int getMaxIndent();

  public abstract int getMaxLevel();

  public abstract String getRightsCube();

  public abstract int getToken();

  public abstract int getHierarchyCount();

  public abstract HierarchyInfo getDefaultHierarchy();

  public abstract HierarchyInfo[] getHierarchies();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.DimensionInfo
 * JD-Core Version:    0.5.4
 */