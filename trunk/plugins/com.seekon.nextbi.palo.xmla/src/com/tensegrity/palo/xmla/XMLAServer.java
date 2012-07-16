package com.tensegrity.palo.xmla;

import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.PaloServer;
import com.tensegrity.palojava.ServerInfo;

public class XMLAServer
  implements PaloServer
{
  private final XMLAConnection connection;

  public XMLAServer(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    this.connection = new XMLAConnection(paramString1, paramString2, paramString3, paramString4);
  }

  public DbConnection connect()
  {
    return this.connection;
  }

  public void disconnect()
  {
    this.connection.disconnect();
  }

  public ServerInfo getInfo()
  {
    if (!this.connection.isConnected())
      throw new PaloException("XMLA Server is not connected.");
    return this.connection.getServerInfo();
  }

  public void ping()
    throws PaloException
  {
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLAServer
 * JD-Core Version:    0.5.4
 */