package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;

import org.palo.viewapi.Role;
import org.palo.viewapi.User;

public abstract interface IUserRoleManagement extends IAssociationManagement {
  public abstract List<String> getRoles(User paramUser) throws SQLException;

  public abstract List<String> getRoles(String paramString) throws SQLException;

  public abstract List<String> getUsers(Role paramRole) throws SQLException;

  public abstract List<String> getUsers(String paramString) throws SQLException;

  public abstract void delete(Role paramRole) throws SQLException;

  public abstract void delete(User paramUser) throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IUserRoleManagement JD-Core
 * Version: 0.5.4
 */