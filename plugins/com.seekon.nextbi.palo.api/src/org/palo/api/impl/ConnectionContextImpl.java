/*    */package org.palo.api.impl;

/*    */
/*    */import com.tensegrity.palojava.DbConnection; /*    */
import java.util.HashMap; /*    */
import org.palo.api.ConnectionContext; /*    */
import org.palo.api.Rights;

/*    */
/*    */class ConnectionContextImpl
/*    */implements ConnectionContext
/*    */{
  /*    */private final ConnectionImpl connection;

  /*    */private final RightsImpl rights;

  /*    */private final ServerInfoImpl serverInfo;

  /*    */private boolean doSupportSubset2;

  /* 59 */private final HashMap<String, Object> dataMap = new HashMap();

  /*    */
  /*    */ConnectionContextImpl(ConnectionImpl connection) {
    /* 62 */this.connection = connection;
    /* 63 */this.rights = new RightsImpl(connection);
    /* 64 */this.serverInfo =
    /* 65 */new ServerInfoImpl(connection,
    /* 65 */connection.getConnectionInternal().getServerInfo());
    /* 66 */setContext();
    /*    */}

  /*    */
  /*    */final void setDoSupportSubset2(boolean b) {
    /* 70 */this.doSupportSubset2 = b;
    /*    */}

  /*    */
  /*    */public final boolean doSupportSubset2() {
    /* 74 */return this.doSupportSubset2;
    /*    */}

  /*    */
  /*    */private final void setContext()
  /*    */{
    /* 79 */com.tensegrity.palojava.ServerInfo srvInfo =
    /* 80 */this.connection.getConnectionInternal().getServerInfo();
    /* 81 */setDoSupportSubset2(
    /* 82 */(srvInfo.getMajor() >= 2) && (srvInfo.getBuildNumber() > 2400));
    /*    */}

  /*    */
  /*    */public final Rights getRights() {
    /* 86 */return this.rights;
    /*    */}

  /*    */
  /*    */public org.palo.api.ServerInfo getServerInfo() {
    /* 90 */return this.serverInfo;
    /*    */}

  /*    */
  /*    */public final void setData(String id, Object data) {
    /* 94 */this.dataMap.put(id, data);
    /*    */}

  /*    */
  /*    */public final Object getData(String id) {
    /* 98 */return this.dataMap.get(id);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ConnectionContextImpl JD-Core Version:
 * 0.5.4
 */