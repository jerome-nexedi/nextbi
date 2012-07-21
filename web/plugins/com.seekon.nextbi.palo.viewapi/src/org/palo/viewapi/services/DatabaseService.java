package org.palo.viewapi.services;

import java.sql.Connection;

public abstract interface DatabaseService {
  public abstract void connect();

  public abstract void disconnect();

  public abstract boolean isConnected();

  public abstract Connection getConnection();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.services.DatabaseService JD-Core
 * Version: 0.5.4
 */