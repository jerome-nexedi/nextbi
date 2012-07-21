package org.palo.api;

public abstract interface Cell {
  public static final int NUMERIC = 1;

  public static final int STRING = 2;

  public abstract Cube getCube();

  public abstract Element[] getPath();

  public abstract Element[] getCoordinate();

  public abstract Object getValue();

  public abstract int getType();

  public abstract boolean hasRule();

  public abstract String getRuleId();

  public abstract boolean isConsolidated();

  public abstract boolean isEmpty();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Cell JD-Core Version: 0.5.4
 */