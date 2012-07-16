package org.palo.api;

public abstract interface PaloObject extends NamedEntity, Writable
{
  public static final int TYPE_NORMAL = 2;
  public static final int TYPE_SYSTEM = 4;
  public static final int TYPE_ATTRIBUTE = 8;
  public static final int TYPE_USER_INFO = 16;
  public static final int TYPE_GPU = 32;

  public abstract String getId();

  public abstract int getType();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.PaloObject
 * JD-Core Version:    0.5.4
 */