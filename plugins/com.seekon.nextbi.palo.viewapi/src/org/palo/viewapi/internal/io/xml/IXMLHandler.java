package org.palo.viewapi.internal.io.xml;

import org.xml.sax.Attributes;

public abstract interface IXMLHandler
{
  public abstract String getXPath();

  public abstract void enter(String paramString, Attributes paramAttributes);

  public abstract void leave(String paramString1, String paramString2);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.io.xml.IXMLHandler
 * JD-Core Version:    0.5.4
 */