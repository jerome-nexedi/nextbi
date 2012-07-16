/*     */package org.palo.api;

/*     */
/*     */public class ConnectionConfiguration
/*     */{
  /*     */private final String host;

  /*     */private final String service;

  /*     */private String name;

  /*     */private String password;

  /* 63 */private int type = 2;

  /*     */
  /* 65 */private int timeout = 30000;

  /*     */
  /* 67 */private boolean loadOnDemand = false;

  /*     */
  /*     */public ConnectionConfiguration(String host, String service)
  /*     */{
    /* 78 */this.host = host;
    /* 79 */this.service = service;
    /*     */}

  /*     */
  /*     */public final boolean doLoadOnDemand()
  /*     */{
    /* 89 */return this.loadOnDemand;
    /*     */}

  /*     */
  /*     */public final String getHost()
  /*     */{
    /* 97 */return this.host;
    /*     */}

  /*     */
  /*     */public final String getPassword()
  /*     */{
    /* 105 */return this.password;
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */public final String getPort()
  /*     */{
    /* 114 */return getService();
    /*     */}

  /*     */
  /*     */public final String getService()
  /*     */{
    /* 122 */return this.service;
    /*     */}

  /*     */
  /*     */public final int getTimeout()
  /*     */{
    /* 131 */return this.timeout;
    /*     */}

  /*     */
  /*     */public final int getType()
  /*     */{
    /* 141 */return this.type;
    /*     */}

  /*     */
  /*     */public final String getUser()
  /*     */{
    /* 149 */return this.name;
    /*     */}

  /*     */
  /*     */public final void setLoadOnDemand(boolean loadOnDemand)
  /*     */{
    /* 162 */this.loadOnDemand = loadOnDemand;
    /*     */}

  /*     */
  /*     */public final void setPassword(String password)
  /*     */{
    /* 170 */this.password = password;
    /*     */}

  /*     */
  /*     */public final void setTimeout(int timeout)
  /*     */{
    /* 180 */this.timeout = timeout;
    /*     */}

  /*     */
  /*     */public final void setType(int type)
  /*     */{
    /* 190 */this.type = type;
    /*     */}

  /*     */
  /*     */public final void setUser(String name)
  /*     */{
    /* 198 */this.name = name;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ConnectionConfiguration JD-Core Version: 0.5.4
 */