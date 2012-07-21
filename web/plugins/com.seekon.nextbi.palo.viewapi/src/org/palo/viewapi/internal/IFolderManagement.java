package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;

public abstract interface IFolderManagement extends IDomainObjectManagement {
  public abstract List<ExplorerTreeNode> getFolders(Role paramRole)
    throws SQLException;

  public abstract List<ExplorerTreeNode> getFolders(AuthUser paramAuthUser,
    User paramUser) throws SQLException;

  public abstract List<ExplorerTreeNode> getFolders(AuthUser paramAuthUser)
    throws SQLException;

  public abstract List<ExplorerTreeNode> reallyGetFolders(AuthUser paramAuthUser)
    throws SQLException;

  public abstract void setUser(AuthUser paramAuthUser);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IFolderManagement JD-Core
 * Version: 0.5.4
 */