/*     */package com.tensegrity.palojava.impl;

/*     */
/*     */import com.tensegrity.palojava.ServerInfo;

/*     */
/*     */public class ServerInfoImpl
/*     */implements ServerInfo
/*     */{
  /*     */private final boolean isLegacy;

  /*     */private final int bugfixVersion;

  /*     */private final int buildNumber;

  /*     */private final int majorNumber;

  /*     */private final int minorNumber;

  /*     */private final int httpsPort;

  /*     */private final int encryption;

  /*     */private final String name;

  /*     */private final String serverType;

  /*     */
  /*     */public ServerInfoImpl(int buildNumber, int bugfixNumber,
    int majorNumber, int minorNumber, int httpsPort, int encryption, boolean isLegacy)
  /*     */{
    /* 62 */this.isLegacy = isLegacy;
    /* 63 */this.buildNumber = buildNumber;
    /* 64 */this.bugfixVersion = bugfixNumber;
    /* 65 */this.majorNumber = majorNumber;
    /* 66 */this.minorNumber = minorNumber;
    /* 67 */this.httpsPort = httpsPort;
    /* 68 */this.encryption = encryption;
    /* 69 */this.name = "PaloServer";
    /* 70 */this.serverType = "Palo";
    /*     */}

  /*     */
  /*     */public int getBugfixVersion()
  /*     */{
    /* 75 */return this.bugfixVersion;
    /*     */}

  /*     */
  /*     */public int getBuildNumber() {
    /* 79 */return this.buildNumber;
    /*     */}

  /*     */
  /*     */public int getMajor() {
    /* 83 */return this.majorNumber;
    /*     */}

  /*     */
  /*     */public int getMinor() {
    /* 87 */return this.minorNumber;
    /*     */}

  /*     */
  /*     */public boolean isLegacy() {
    /* 91 */return this.isLegacy;
    /*     */}

  /*     */
  /*     */public String getId() {
    /* 95 */return Integer.toString(this.buildNumber);
    /*     */}

  /*     */
  /*     */public int getType()
  /*     */{
    /* 100 */return 2;
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 104 */return false;
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 108 */return false;
    /*     */}

  /*     */
  /*     */public int getEncryption() {
    /* 112 */return this.encryption;
    /*     */}

  /*     */
  /*     */public int getHttpsPort() {
    /* 116 */return this.httpsPort;
    /*     */}

  /*     */
  /*     */public String getName() {
    /* 120 */return this.name;
    /*     */}

  /*     */
  /*     */public String getServerType() {
    /* 124 */return this.serverType;
    /*     */}

  /*     */
  /*     */public String getVersion() {
    /* 128 */StringBuffer vStr = new StringBuffer(this.majorNumber);
    /* 129 */vStr.append(".");
    /* 130 */vStr.append(this.minorNumber);
    /* 131 */return vStr.toString();
    /*     */}

  /*     */
  /*     */public String[] getProperties() {
    /* 135 */return new String[0];
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.impl.ServerInfoImpl JD-Core Version:
 * 0.5.4
 */