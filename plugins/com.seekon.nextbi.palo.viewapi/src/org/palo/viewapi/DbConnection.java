package org.palo.viewapi;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract interface DbConnection {
  public abstract void connect() throws SQLException;

  public abstract void disconnect();

  public abstract boolean isConnected();

  public abstract Connection getConnection();

  public abstract Properties getSqlCommands();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.DbConnection JD-Core Version: 0.5.4
 */