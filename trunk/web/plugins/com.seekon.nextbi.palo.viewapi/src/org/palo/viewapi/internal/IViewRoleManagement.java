package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Role;
import org.palo.viewapi.View;

public abstract interface IViewRoleManagement extends IAssociationManagement {
  public abstract List<String> getViews(Role paramRole) throws SQLException;

  public abstract List<String> getRoles(View paramView) throws SQLException;

  public abstract List<String> getRoles(String paramString) throws SQLException;

  public abstract void delete(Role paramRole) throws SQLException;

  public abstract void delete(View paramView) throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IViewRoleManagement JD-Core
 * Version: 0.5.4
 */