/*    */package com.tensegrity.palojava.http;

/*    */
/*    */import com.tensegrity.palojava.DbConnection; /*    */
import com.tensegrity.palojava.PaloException; /*    */
import com.tensegrity.palojava.PaloServer; /*    */
import com.tensegrity.palojava.ServerInfo;

/*    */
/*    */public class HttpPaloServer
/*    */implements PaloServer
/*    */{
  /*    */private final HttpConnection connection;

  /*    */
  /*    */public HttpPaloServer(String host, String port, int timeout)
  /*    */{
    /* 55 */this.connection = new HttpConnection(host, port, timeout);
    /*    */}

  /*    */
  /*    */public final synchronized DbConnection connect() {
    /* 59 */return this.connection;
    /*    */}

  /*    */
  /*    */public final ServerInfo getInfo() {
    /* 63 */if (!isConnected())
      /* 64 */throw new PaloException("not connected!!");
    /* 65 */return this.connection.getServerInfo();
    /*    */}

  /*    */
  /*    */public final void disconnect() {
    /* 69 */this.connection.disconnect();
    /*    */}

  /*    */
  /*    */public final void ping() {
    /* 73 */throw new PaloException(
      "HttpPaloServer#disconnect(): NOT IMPLEMENTED!!");
    /*    */}

  /*    */
  /*    */public final boolean login(String username, String password)
  /*    */{
    /* 79 */return this.connection.login(username, password);
    /*    */}

  /*    */
  /*    */private final synchronized boolean isConnected()
  /*    */{
    /* 87 */return (this.connection != null) && (this.connection.isConnected());
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.HttpPaloServer JD-Core Version:
 * 0.5.4
 */