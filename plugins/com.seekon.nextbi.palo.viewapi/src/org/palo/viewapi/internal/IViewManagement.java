package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.api.Cube;
import org.palo.viewapi.Account;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.View;

public abstract interface IViewManagement extends IDomainObjectManagement {
  public abstract List<View> findViews(Role paramRole) throws SQLException;

  public abstract List<View> findViews(User paramUser) throws SQLException;

  public abstract List<View> findViews(Account paramAccount) throws SQLException;

  public abstract boolean hasViews(Account paramAccount) throws SQLException;

  public abstract View findByName(String paramString, Cube paramCube,
    Account paramAccount) throws SQLException;

  public abstract List<View> listViews() throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IViewManagement JD-Core
 * Version: 0.5.4
 */