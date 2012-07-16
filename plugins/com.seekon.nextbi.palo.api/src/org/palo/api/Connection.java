package org.palo.api;

import com.tensegrity.palojava.ServerInfo;
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;

public abstract interface Connection extends Writable
{
  public static final int TYPE_LEGACY = 1;
  public static final int TYPE_HTTP = 2;
  public static final int TYPE_XMLA = 3;
  public static final int TYPE_WSS = 4;
  public static final int DEFAULT_TIMEOUT = 30000;

  public abstract String getServer();

  public abstract String getService();

  public abstract String getUsername();

  public abstract String getPassword();

  public abstract void reload();

  public abstract void ping();

  public abstract boolean isConnected();

  public abstract void disconnect();

  public abstract int getDatabaseCount();

  public abstract Database getDatabaseAt(int paramInt);

  public abstract Database[] getDatabases();

  public abstract Database[] getSystemDatabases();

  public abstract Database[] getUserInfoDatabases();

  public abstract Database getDatabaseByName(String paramString);

  public abstract Database getDatabaseById(String paramString);

  public abstract Database addDatabase(String paramString);

  public abstract Database addUserInfoDatabase(String paramString);

  public abstract void removeDatabase(Database paramDatabase);

  public abstract boolean save();

  public abstract void addConnectionListener(ConnectionListener paramConnectionListener);

  public abstract void removeConnectionListener(ConnectionListener paramConnectionListener);

  public abstract boolean isLegacy();

  public abstract int getType();

  public abstract String getFunctions();

  public abstract boolean login(String paramString1, String paramString2);

  public abstract FavoriteViewTreeNode loadFavoriteViews();

  public abstract void storeFavoriteViews(FavoriteViewTreeNode paramFavoriteViewTreeNode);

  public abstract String[] getAllPropertyIds();

  public abstract Property2 getProperty(String paramString);

  public abstract void addProperty(Property2 paramProperty2);

  public abstract void removeProperty(String paramString);

  public abstract ServerInfo getInfo();

  public abstract ConnectionContext getContext();

  public abstract Object getData(String paramString);

  public abstract void clearCache();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Connection
 * JD-Core Version:    0.5.4
 */