package org.palo.api;

public abstract interface ConnectionContext
{
  /** @deprecated */
  public abstract Object getData(String paramString);

  /** @deprecated */
  public abstract void setData(String paramString, Object paramObject);

  /** @deprecated */
  public abstract boolean doSupportSubset2();

  public abstract Rights getRights();

  public abstract ServerInfo getServerInfo();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ConnectionContext
 * JD-Core Version:    0.5.4
 */