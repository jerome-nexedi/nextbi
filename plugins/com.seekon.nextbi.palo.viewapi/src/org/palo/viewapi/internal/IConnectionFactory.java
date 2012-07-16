package org.palo.viewapi.internal;

import org.palo.api.Connection;

public abstract interface IConnectionFactory
{
  public abstract Connection createConnection(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);

  public abstract void initialize(PaloConfiguration paramPaloConfiguration);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.IConnectionFactory
 * JD-Core Version:    0.5.4
 */