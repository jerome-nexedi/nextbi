package com.tensegrity.palojava;

public abstract interface PropertyInfo extends PaloInfo {
  public abstract String getValue();

  public abstract void setValue(String paramString);

  public abstract PropertyInfo getParent();

  public abstract int getChildCount();

  public abstract PropertyInfo[] getChildren();

  public abstract void addChild(PropertyInfo paramPropertyInfo);

  public abstract void removeChild(PropertyInfo paramPropertyInfo);

  public abstract void clearChildren();

  public abstract boolean isReadOnly();

  public abstract PropertyInfo getChild(String paramString);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.PropertyInfo JD-Core Version: 0.5.4
 */