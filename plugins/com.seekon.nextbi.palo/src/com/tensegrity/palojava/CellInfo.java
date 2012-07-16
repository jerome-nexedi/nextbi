package com.tensegrity.palojava;

public abstract interface CellInfo extends PaloInfo
{
  public static final int SPLASH_MODE_DISABLED = 0;
  public static final int SPLASH_MODE_DEFAULT = 1;
  public static final int SPLASH_MODE_ADD = 2;
  public static final int SPLASH_MODE_SET = 3;
  public static final int SPLASH_MODE_UNKNOWN = 4;
  public static final int TYPE_NUMERIC = 1;
  public static final int TYPE_STRING = 2;
  public static final int TYPE_ERROR = 99;

  public abstract boolean exists();

  public abstract Object getValue();

  public abstract String[] getCoordinate();

  public abstract int getType();

  public abstract String getRule();

  public abstract void setRule(String paramString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.CellInfo
 * JD-Core Version:    0.5.4
 */