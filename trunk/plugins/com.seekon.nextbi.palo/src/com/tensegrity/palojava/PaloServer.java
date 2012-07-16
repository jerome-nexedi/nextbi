package com.tensegrity.palojava;

public abstract interface PaloServer
{

  /** @deprecated */
  public static final int TYPE_LEGACY = 1;
  public static final int TYPE_HTTP = 2;
  public static final int TYPE_XMLA = 3;
  public static final int TYPE_WSS = 4;

  public abstract ServerInfo getInfo();

  public abstract DbConnection connect();

  public abstract void disconnect();

  public abstract void ping()
    throws PaloException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.PaloServer
 * JD-Core Version:    0.5.4
 */