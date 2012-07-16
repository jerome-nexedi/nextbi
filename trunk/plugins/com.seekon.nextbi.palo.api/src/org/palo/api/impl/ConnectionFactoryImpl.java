/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palo.xmla.XMLAServer; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import com.tensegrity.palojava.PaloFactory; /*     */
import com.tensegrity.palojava.PaloServer; /*     */
import java.util.HashSet; /*     */
import java.util.Set; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.ConnectionConfiguration; /*     */
import org.palo.api.ConnectionFactory; /*     */
import org.palo.api.PaloAPIException;

/*     */
/*     */public class ConnectionFactoryImpl extends ConnectionFactory
/*     */{
  /* 63 */private final Set activePaloConnections = new HashSet();

  /*     */
  /*     *//** @deprecated */
  /*     */public final Connection newConnection(String server, String service,
    String user, String pass)
  /*     */{
    /* 70 */return newConnection(server, service, user, pass, false, 2);
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public final Connection newConnection(String server, String service,
    String user, String pass, boolean doLoadOnDemand, int type)
  /*     */{
    /*     */PaloServer paloSrv;
    ConnectionImpl connection;
    /* 79 */if ((type == 2) || (type == 1)) {
      /* 80 */paloSrv =
      /* 81 */PaloFactory.getInstance().createServerConnection(server, service,
        type, 30000);
      /*     */}
    /*     */else
    /*     */{
      /* 82 */if (type == 3)
        /* 83 */paloSrv = new XMLAServer(server, service, user, pass);
      /*     */else
        /* 85 */throw new PaloAPIException("Unknown connection type specified: " +
        /* 86 */type + "!!");
      /*     */}

    /*     */try
    /*     */{
      /* 90 */connection = new ConnectionImpl(paloSrv);
      /* 91 */if (connection.login(user, pass)) {
        /* 92 */if (!doLoadOnDemand) {
          /* 93 */connection.reload();
          /*     */}
        /*     */
        /*     */}
      /*     */else
      /*     */{
        /* 99 */throw new PaloAPIException("Could not login to palo server '" +
        /* 100 */server + "' as user '" + user + "'!!");
        /*     */}
      /*     */} catch (PaloException e) {
      /* 103 */throw new PaloAPIException(e.getMessage());
      /*     */}
    /* 105 */if ((connection.supportsRules()) && (type != 3)) {
      /* 106 */this.activePaloConnections.add(connection);
      /*     */}
    /* 108 */return connection;
    /*     */}

  /*     */
  /*     */public final ConnectionConfiguration getConfiguration(String host,
    String service)
  /*     */{
    /* 118 */return getConfiguration(host, service, null, null);
    /*     */}

  /*     */
  /*     */public final ConnectionConfiguration getConfiguration(String host,
    String service, String user, String password) {
    /* 122 */ConnectionConfiguration cfg = new ConnectionConfiguration(host,
      service);
    /*     */
    /* 124 */cfg.setUser(user);
    /* 125 */cfg.setPassword(password);
    /* 126 */return cfg;
    /*     */}

  /*     */
  /*     */public final Connection newConnection(ConnectionConfiguration cfg)
  /*     */{
    /* 131 */int type = cfg.getType();
    /*     */PaloServer paloSrv;
    /* 132 */if ((type == 2) || (type == 1)) {
      /* 133 */paloSrv = PaloFactory.getInstance().createServerConnection(
      /* 134 */cfg.getHost(), cfg.getPort(), type, cfg.getTimeout());
      /*     */}
    /*     */else
    /*     */{
      /* 135 */if (type == 3)
        /* 136 */paloSrv = new XMLAServer(cfg.getHost(), cfg.getPort(),
        /* 137 */cfg.getUser(), cfg.getPassword());
      /*     */else
        /* 139 */throw new PaloAPIException("Unknown connection type specified: " +
        /* 140 */type + "!!");
      /*     */}
    /* 142 */ConnectionImpl connection = new ConnectionImpl(paloSrv);
    /* 143 */if (connection.login(cfg.getUser(), cfg.getPassword())) {
      /* 144 */if (!cfg.doLoadOnDemand()) {
        /* 145 */connection.reload();
        /*     */}
      /*     */
      /*     */}
    /*     */else
    /*     */{
      /* 151 */throw new PaloAPIException("Could not login to palo server '" +
      /* 152 */cfg.getHost() + "' as user '" + cfg.getUser() +
      /* 153 */"'!!");
      /* 154 */}
    if ((connection.supportsRules()) && (type != 3)) {
      /* 155 */this.activePaloConnections.add(connection);
      /*     */}
    /* 157 */return connection;
    /*     */}

  /*     */
  /*     */final Connection[] getActiveConnections() {
    /* 161 */Connection[] cons =
    /* 162 */(Connection[]) this.activePaloConnections.toArray(new Connection[0]);
    /* 163 */int i = 0;
    for (int n = cons.length; i < n; ++i) {
      /* 164 */if (!cons[i].isConnected()) {
        /* 165 */this.activePaloConnections.remove(cons[i]);
        /*     */}
      /*     */}
    /* 168 */return (Connection[]) this.activePaloConnections
      .toArray(new Connection[0]);
    /*     */}

  /*     */
  /*     */final void removePaloConnection(Connection con) {
    /* 172 */this.activePaloConnections.remove(con);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ConnectionFactoryImpl JD-Core Version:
 * 0.5.4
 */