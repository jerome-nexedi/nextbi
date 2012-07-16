/*    */package org.palo.api.impl;

/*    */
/*    */import java.util.HashMap; /*    */
import java.util.Set; /*    */
import org.palo.api.Connection; /*    */
import org.palo.api.Property2;

/*    */
/*    */class ServerInfoImpl
/*    */implements org.palo.api.ServerInfo
/*    */{
  /*    */private final ConnectionImpl connection;

  /*    */private final HashMap<String, Property2> properties;

  /*    */private final com.tensegrity.palojava.ServerInfo serverInfo;

  /*    */
  /*    */ServerInfoImpl(Connection connection,
    com.tensegrity.palojava.ServerInfo serverInfo)
  /*    */{
    /* 49 */this.connection = ((ConnectionImpl) connection);
    /* 50 */this.properties = new HashMap();
    /* 51 */this.serverInfo = serverInfo;
    /*    */
    /* 53 */addProperty("MajorVersionProperty",
    /* 54 */Integer.toString(serverInfo.getMajor()));
    /* 55 */addProperty("MinorVersionProperty",
    /* 56 */Integer.toString(serverInfo.getMinor()));
    /* 57 */addProperty("BuildNumberProperty",
    /* 58 */Integer.toString(serverInfo.getBuildNumber()));
    /*    */
    /* 60 */String[] props = serverInfo.getProperties();
    /* 61 */for (int i = 0; i < props.length; i += 2)
      /* 62 */addProperty(props[i], props[(i + 1)]);
    /*    */}

  /*    */
  /*    */private final void addProperty(String id, String value)
  /*    */{
    /* 67 */this.properties.put(id,
    /* 68 */Property2Impl.create(this.connection, id, value, null,
    /* 68 */2, true));
    /*    */}

  /*    */
  /*    */public String getName() {
    /* 72 */return this.serverInfo.getName();
    /*    */}

  /*    */
  /*    */public String getProperty(String id) {
    /* 76 */if (this.properties.containsKey(id)) {
      /* 77 */return ((Property2) this.properties.get(id)).getValue();
      /*    */}
    /* 79 */return "";
    /*    */}

  /*    */
  /*    */public String getType() {
    /* 83 */return this.serverInfo.getServerType();
    /*    */}

  /*    */
  /*    */public String getVersion() {
    /* 87 */return this.serverInfo.getVersion();
    /*    */}

  /*    */
  /*    */public String[] getPropertyIds() {
    /* 91 */return (String[]) this.properties.keySet().toArray(new String[0]);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ServerInfoImpl JD-Core Version: 0.5.4
 */