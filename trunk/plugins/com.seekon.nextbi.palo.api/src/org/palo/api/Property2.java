package org.palo.api;

public abstract interface Property2 extends PaloObject {
  public static final int TYPE_NUMERIC = 1;

  public static final int TYPE_STRING = 2;

  public static final int TYPE_BOOLEAN = 3;

  public abstract String getValue();

  public abstract void setValue(String paramString);

  public abstract Property2 getParent();

  public abstract int getChildCount();

  public abstract Property2[] getChildren();

  public abstract String getChildValue(String paramString);

  public abstract Property2[] getChildren(String paramString);

  public abstract void addChild(Property2 paramProperty2);

  public abstract void removeChild(Property2 paramProperty2);

  public abstract void clearChildren();

  public abstract boolean isReadOnly();

  public abstract int getType();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Property2 JD-Core Version: 0.5.4
 */