package com.tensegrity.palojava;

public abstract interface ElementInfo extends PaloInfo {
  public static final int TYPE_NUMERIC = 1;

  public static final int TYPE_STRING = 2;

  public static final int TYPE_CONSOLIDATED = 4;

  public static final int TYPE_RULE = 3;

  public abstract DimensionInfo getDimension();

  public abstract String getName();

  public abstract int getPosition();

  public abstract int getLevel();

  public abstract int getIndent();

  public abstract int getDepth();

  public abstract int getParentCount();

  public abstract String[] getParents();

  public abstract void setParents(String[] paramArrayOfString);

  public abstract int getChildrenCount();

  public abstract String[] getChildren();

  public abstract double[] getWeights();

  public abstract void update(String[] paramArrayOfString);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.ElementInfo JD-Core Version: 0.5.4
 */