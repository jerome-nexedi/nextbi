package org.palo.viewapi.services;

import org.palo.api.Hierarchy;
import org.palo.api.subsets.Subset2;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.exceptions.OperationFailedException;
import org.palo.viewapi.internal.DynamicFolder;
import org.palo.viewapi.internal.ExplorerTreeNode;
import org.palo.viewapi.internal.FolderElement;
import org.palo.viewapi.internal.StaticFolder;

public abstract interface FolderService extends Service {
  public abstract DynamicFolder createDynamicFolder(String paramString,
    ExplorerTreeNode paramExplorerTreeNode, Hierarchy paramHierarchy,
    Subset2 paramSubset2, PaloConnection paramPaloConnection)
    throws OperationFailedException;

  public abstract StaticFolder createStaticFolder(String paramString,
    ExplorerTreeNode paramExplorerTreeNode, PaloConnection paramPaloConnection)
    throws OperationFailedException;

  public abstract FolderElement createFolderElement(String paramString,
    ExplorerTreeNode paramExplorerTreeNode, PaloConnection paramPaloConnection)
    throws OperationFailedException;

  public abstract boolean doesTreeNodeExist(ExplorerTreeNode paramExplorerTreeNode);

  public abstract ExplorerTreeNode getTreeNode(String paramString);

  public abstract ExplorerTreeNode getTreeRoot();

  public abstract void save(ExplorerTreeNode paramExplorerTreeNode)
    throws OperationFailedException;

  public abstract void delete(ExplorerTreeNode paramExplorerTreeNode)
    throws OperationFailedException;

  public abstract void add(Role paramRole, ExplorerTreeNode paramExplorerTreeNode)
    throws OperationFailedException;

  public abstract void remove(Role paramRole, ExplorerTreeNode paramExplorerTreeNode)
    throws OperationFailedException;

  public abstract void setOwner(User paramUser,
    ExplorerTreeNode paramExplorerTreeNode);

  public abstract void setName(String paramString,
    ExplorerTreeNode paramExplorerTreeNode);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.services.FolderService JD-Core Version:
 * 0.5.4
 */