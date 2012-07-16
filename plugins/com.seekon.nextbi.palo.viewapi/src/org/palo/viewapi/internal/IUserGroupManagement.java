package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Group;
import org.palo.viewapi.User;

public abstract interface IUserGroupManagement extends IAssociationManagement {
  public abstract List<String> getUsers(Group paramGroup) throws SQLException;

  public abstract List<String> getUsers(String paramString) throws SQLException;

  public abstract List<String> getGroups(User paramUser) throws SQLException;

  public abstract List<String> getGroups(String paramString) throws SQLException;

  public abstract void delete(Group paramGroup) throws SQLException;

  public abstract void delete(User paramUser) throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IUserGroupManagement JD-Core
 * Version: 0.5.4
 */