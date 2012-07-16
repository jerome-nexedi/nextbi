package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Role;

public abstract interface IFolderRoleManagement extends IAssociationManagement {
  public abstract List<String> getFolders(Role paramRole) throws SQLException;

  public abstract List<String> getRoles(ExplorerTreeNode paramExplorerTreeNode)
    throws SQLException;

  public abstract List<String> getRoles(String paramString) throws SQLException;

  public abstract void delete(Role paramRole) throws SQLException;

  public abstract void delete(ExplorerTreeNode paramExplorerTreeNode)
    throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IFolderRoleManagement JD-Core
 * Version: 0.5.4
 */