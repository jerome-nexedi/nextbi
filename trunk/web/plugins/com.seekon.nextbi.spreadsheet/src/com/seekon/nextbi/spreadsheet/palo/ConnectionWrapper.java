package com.seekon.nextbi.spreadsheet.palo;

import org.palo.api.Connection;
import org.palo.api.ConnectionContext;
import org.palo.api.ConnectionListener;
import org.palo.api.Database;
import org.palo.api.Property2;
import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;

import com.tensegrity.palojava.LicenseInfo;
import com.tensegrity.palojava.ServerInfo;

public class ConnectionWrapper implements Connection{
  
  private Connection connection;

  public ConnectionWrapper(Connection connection) {
    super();
    this.connection = connection;
  }

  public void addConnectionListener(ConnectionListener connectionListener) {
    connection.addConnectionListener(connectionListener);
  }

  public Database addDatabase(String name) {
    return connection.addDatabase(name);
  }

  public void addProperty(Property2 property) {
    connection.addProperty(property);
  }

  public Database addUserInfoDatabase(String name) {
    return connection.addUserInfoDatabase(name);
  }

  public boolean canBeModified() {
    return connection.canBeModified();
  }

  public boolean canCreateChildren() {
    return connection.canCreateChildren();
  }

  public void clearCache() {
    connection.clearCache();
  }

  public void disconnect() {
    connection.disconnect();
  }

  public String[] getAllPropertyIds() {
    return connection.getAllPropertyIds();
  }

  public ConnectionContext getContext() {
    return connection.getContext();
  }

  public Object getData(String id) {
    return connection.getData(id);
  }

  public Database getDatabaseAt(int index) {
    return connection.getDatabaseAt(index);
  }

  public Database getDatabaseById(String id) {
    return connection.getDatabaseById(id);
  }

  public Database getDatabaseByName(String name) {
    return connection.getDatabaseByName(name);
  }

  public int getDatabaseCount() {
    return connection.getDatabaseCount();
  }

  public Database[] getDatabases() {
    return connection.getDatabases();
  }

  public String getFunctions() {
    return connection.getFunctions();
  }

  public ServerInfo getInfo() {
    return connection.getInfo();
  }

  public LicenseInfo getLicenseInfo() {
    return connection.getLicenseInfo();
  }

  public String getPassword() {
    return connection.getPassword();
  }

  public Property2 getProperty(String id) {
    return connection.getProperty(id);
  }

  public String getServer() {
    return connection.getServer();
  }

  public String getService() {
    return connection.getService();
  }

  public Database[] getSystemDatabases() {
    return connection.getSystemDatabases();
  }

  public int getType() {
    return connection.getType();
  }

  public Database[] getUserInfoDatabases() {
    return connection.getUserInfoDatabases();
  }

  public String getUsername() {
    return connection.getUsername();
  }

  public boolean isConnected() {
    return connection.isConnected();
  }

  public boolean isLegacy() {
    return connection.isLegacy();
  }

  public FavoriteViewTreeNode loadFavoriteViews() {
    return connection.loadFavoriteViews();
  }

  public boolean login(String username, String password) {
    return connection.login(username, password);
  }

  public void ping() {
    connection.ping();
  }

  public void reload() {
    connection.reload();
  }

  public void removeConnectionListener(ConnectionListener connectionListener) {
    connection.removeConnectionListener(connectionListener);
  }

  public void removeDatabase(Database database) {
    connection.removeDatabase(database);
  }

  public void removeProperty(String id) {
    connection.removeProperty(id);
  }

  public boolean save() {
    return connection.save();
  }

  public void storeFavoriteViews(FavoriteViewTreeNode favoriteViews) {
    connection.storeFavoriteViews(favoriteViews);
  }
  
  
}
