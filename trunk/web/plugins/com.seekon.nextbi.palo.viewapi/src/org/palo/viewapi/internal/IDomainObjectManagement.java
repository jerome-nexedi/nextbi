package org.palo.viewapi.internal;

import java.sql.SQLException;

import org.palo.viewapi.DomainObject;

public abstract interface IDomainObjectManagement {
  public abstract DomainObject find(String paramString) throws SQLException;

  public abstract DomainObject findByName(String paramString) throws SQLException;

  public abstract void update(DomainObject paramDomainObject) throws SQLException;

  public abstract void insert(DomainObject paramDomainObject) throws SQLException;

  public abstract void delete(DomainObject paramDomainObject) throws SQLException;

  public abstract void reset();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IDomainObjectManagement
 * JD-Core Version: 0.5.4
 */