package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;

import org.palo.viewapi.Role;

public abstract interface IRoleManagement extends IDomainObjectManagement {
  public abstract List<Role> findAll() throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IRoleManagement JD-Core
 * Version: 0.5.4
 */