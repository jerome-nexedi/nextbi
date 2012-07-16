package com.tensegrity.palojava;

public abstract interface ConnectionInfo
{
  public abstract String getHost();

  public abstract String getPort();

  public abstract String getUsername();

  public abstract String getPassword();

  public abstract void setData(String paramString, Object paramObject);

  public abstract Object getData(String paramString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.ConnectionInfo
 * JD-Core Version:    0.5.4
 */