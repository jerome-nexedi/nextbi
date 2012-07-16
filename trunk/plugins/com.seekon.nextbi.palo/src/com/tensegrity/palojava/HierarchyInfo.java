package com.tensegrity.palojava;

public abstract interface HierarchyInfo extends PaloInfo
{
  public abstract DimensionInfo getDimension();

  public abstract String getName();

  public abstract int getElementCount();

  public abstract void rename(String paramString);

  public abstract int getMaxLevel();

  public abstract int getMaxDepth();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.HierarchyInfo
 * JD-Core Version:    0.5.4
 */