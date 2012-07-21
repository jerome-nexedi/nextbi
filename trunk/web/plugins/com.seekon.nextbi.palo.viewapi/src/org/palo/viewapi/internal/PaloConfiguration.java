/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.util.ArrayList; /*     */
import java.util.Collections; /*     */
import java.util.Comparator; /*     */
import java.util.Iterator; /*     */
import java.util.List;

/*     */
/*     */public class PaloConfiguration
/*     */{
  /*     */public static final int DEFAULT_POOL_MAX_CONNECTIONS = 10;

  /* 44 */private List servers = new ArrayList();

  /* 45 */private int poolMaxConnections = 10;

  /* 46 */private String user = "guest";

  /* 47 */private String password = "pass";

  /*     */
  /*     */public final List getServers() {
    /* 50 */return this.servers;
    /*     */}

  /*     */
  /*     */public final void setServers(List servers) {
    /* 54 */this.servers = servers;
    /*     */}

  /*     */
  /*     */public int getPoolMaxConnections() {
    /* 58 */return this.poolMaxConnections;
    /*     */}

  /*     */
  /*     */public void setPoolMaxConnections(int value) {
    /* 62 */this.poolMaxConnections = value;
    /*     */}

  /*     */
  /*     */public String getPassword() {
    /* 66 */return this.password;
    /*     */}

  /*     */
  /*     */public void setPassword(String password) {
    /* 70 */this.password = password;
    /*     */}

  /*     */
  /*     */public String getUser() {
    /* 74 */return this.user;
    /*     */}

  /*     */
  /*     */public void setUser(String user) {
    /* 78 */this.user = user;
    /*     */}

  /*     */
  /*     */public void addServer(PaloServer server) {
    /* 82 */this.servers.add(server);
    /* 83 */Collections.sort(this.servers, new ServerOrderComparator());
    /*     */}

  /*     */
  /*     */public PaloServer getServer(int order) {
    /* 87 */PaloServer r = null;
    /* 88 */for (Iterator it = this.servers.iterator(); it.hasNext();) {
      /* 89 */PaloServer server = (PaloServer) it.next();
      /* 90 */if (server.getOrder() == order) {
        /* 91 */r = server;
        /* 92 */break;
        /*     */}
      /*     */}
    /* 95 */return r;
    /*     */}

  /*     */
  /*     */public PaloServer getServer(String host, String service) {
    /* 99 */PaloServer r = null;
    /* 100 */for (Iterator it = this.servers.iterator(); it.hasNext();) {
      /* 101 */PaloServer server = (PaloServer) it.next();
      /* 102 */if ((!host.equals(server.getHost())) ||
      /* 103 */(!service.equals(server.getService())))
        continue;
      /* 104 */r = server;
      /* 105 */break;
      /*     */}
    /*     */
    /* 108 */return r;
    /*     */}

  /*     */
  /*     */public String toString() {
    /* 112 */String result = "PaloConfiguration[";
    /* 113 */int size = this.servers.size();
    /* 114 */for (int i = 0; i < size; ++i) {
      /* 115 */result = result + this.servers.get(i) + "\n";
      /*     */}
    /* 117 */result = result + "connection.pool.max = " + getPoolMaxConnections();
    /* 118 */result = result + "]";
    /* 119 */return result;
    /*     */}

  /*     */
  /*     */public static final class PaloServer
  /*     */{
    /*     */private static final String DEFAULT_PROVIDER = "palo";

    /*     */private String host;

    /*     */private int order;

    /*     */private String service;

    /*     */private String login;

    /*     */private String password;

    /* 130 */private String provider = "palo";

    /*     */private String dispName;

    /*     */
    /*     */public final String getHost() {
      /* 134 */return this.host;
      /*     */}

    /*     */
    /*     */public final void setHost(String host) {
      /* 138 */this.host = host;
      /*     */}

    /*     */
    /*     */public final String getLogin() {
      /* 142 */return this.login;
      /*     */}

    /*     */
    /*     */public final void setLogin(String login) {
      /* 146 */this.login = login;
      /*     */}

    /*     */
    /*     */public final String getPassword() {
      /* 150 */return this.password;
      /*     */}

    /*     */
    /*     */public final void setPassword(String password) {
      /* 154 */this.password = password;
      /*     */}

    /*     */
    /*     */public final String getService() {
      /* 158 */return this.service;
      /*     */}

    /*     */
    /*     */public final void setService(String service) {
      /* 162 */this.service = service;
      /*     */}

    /*     */
    /*     */public String getProvider() {
      /* 166 */return this.provider;
      /*     */}

    /*     */
    /*     */public void setProvider(String provider) {
      /* 170 */this.provider = provider;
      /*     */}

    /*     */
    /*     */public String toString() {
      /* 174 */String result = "PaloServer[";
      /* 175 */result = result + this.provider + ";";
      /* 176 */result = result + this.host + ":" + this.service + ";";
      /* 177 */result = result + this.login + ":" + this.password;
      /* 178 */result = result + "]";
      /* 179 */return result;
      /*     */}

    /*     */
    /*     */public int getOrder() {
      /* 183 */return this.order;
      /*     */}

    /*     */
    /*     */public void setOrder(int order) {
      /* 187 */this.order = order;
      /*     */}

    /*     */
    /*     */public void setDispName(String value) {
      /* 191 */this.dispName = value;
      /*     */}

    /*     */
    /*     */public String getDispName()
    /*     */{
      /* 196 */return this.dispName;
      /*     */}
    /*     */
  }

  /*     */
  /*     */private static class ServerOrderComparator implements Comparator
  /*     */{
    /*     */public int compare(Object o1, Object o2) {
      /* 203 */PaloConfiguration.PaloServer server1 = (PaloConfiguration.PaloServer) o1;
      /* 204 */PaloConfiguration.PaloServer server2 = (PaloConfiguration.PaloServer) o2;
      /* 205 */return server1.getOrder() - server2.getOrder();
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.PaloConfiguration JD-Core
 * Version: 0.5.4
 */