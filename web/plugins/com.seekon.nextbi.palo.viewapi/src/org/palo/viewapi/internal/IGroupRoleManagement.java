package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;

import org.palo.viewapi.Group;
import org.palo.viewapi.Role;

public abstract interface IGroupRoleManagement extends IAssociationManagement {
  public abstract List<String> getGroups(Role paramRole) throws SQLException;

  public abstract List<String> getGroups(String paramString) throws SQLException;

  public abstract List<String> getRoles(Group paramGroup) throws SQLException;

  public abstract List<String> getRoles(String paramString) throws SQLException;

  public abstract void delete(Group paramGroup) throws SQLException;

  public abstract void delete(Role paramRole) throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IGroupRoleManagement JD-Core
 * Version: 0.5.4
 */