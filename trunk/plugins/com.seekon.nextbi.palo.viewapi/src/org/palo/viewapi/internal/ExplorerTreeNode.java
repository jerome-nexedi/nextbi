package org.palo.viewapi.internal;

import org.palo.api.parameters.ParameterReceiver;
import org.palo.viewapi.GuardedObject;

public abstract interface ExplorerTreeNode extends ParameterReceiver, GuardedObject {
  public abstract String getId();

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract ExplorerTreeNode getRoot();

  public abstract ExplorerTreeNode getParent();

  public abstract void setParent(ExplorerTreeNode paramExplorerTreeNode);

  public abstract ExplorerTreeNode[] getChildren();

  public abstract boolean addChild(ExplorerTreeNode paramExplorerTreeNode);

  public abstract boolean removeChild(ExplorerTreeNode paramExplorerTreeNode);

  public abstract boolean removeChildById(String paramString);

  public abstract boolean removeChildByName(String paramString);

  public abstract boolean clearAllChildren();

  public abstract String getPersistenceString();

  public abstract String getConnectionId();

  public abstract int getType();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.ExplorerTreeNode JD-Core
 * Version: 0.5.4
 */