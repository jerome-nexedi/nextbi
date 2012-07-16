package org.palo.viewapi;

public abstract interface PaloConnection extends DomainObject
{
  public static final int TYPE_LEGACY = 1;
  public static final int TYPE_HTTP = 2;
  public static final int TYPE_XMLA = 3;
  public static final int TYPE_WSS = 4;

  public abstract String getName();

  public abstract String getHost();

  public abstract String getService();

  public abstract String getDescription();

  public abstract int getType();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.PaloConnection
 * JD-Core Version:    0.5.4
 */