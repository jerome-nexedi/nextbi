package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;

public abstract interface IUserManagement extends IDomainObjectManagement {
  public abstract List<User> findAll() throws SQLException;

  public abstract List<User> findAllUsersFor(Role paramRole) throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IUserManagement JD-Core
 * Version: 0.5.4
 */