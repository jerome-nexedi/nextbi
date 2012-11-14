package com.seekon.nextbi.spreadsheet.palo;

import java.util.HashMap;
import java.util.Map;

import org.palo.api.Connection;
import org.palo.api.ConnectionFactory;

public class ConnectionPool {

  private static ConnectionPool instance = new ConnectionPool();

  private Map<String, Connection> pool = new HashMap<String, Connection>();

  private ConnectionPool() {
  }

  public static ConnectionPool getInstance() {
    return instance;
  }

  public void addConnection(Connection connection) {
    pool.put(generateConnectionKey(connection), connection);
  }

  public Connection getConnection(String server, String service, String username,
    String password) {
    String key = generateConnectionKey(server, service, username);
    Connection connection = pool.get(key);
    if (connection == null || !connection.isConnected()) {
      connection = new ConnectionWrapper(ConnectionFactory.getInstance()
        .newConnection(server, service, username, password));
      pool.put(key, connection);
    }
    return connection;
  }

  public void disconnect(Connection connection) {
    //do nothing
  }

  private String generateConnectionKey(String server, String service, String username) {
    return server + "-" + service + "-" + username;
  }

  private String generateConnectionKey(Connection connection) {
    return generateConnectionKey(connection.getServer(), connection.getService(),
      connection.getUsername());
  }
}
