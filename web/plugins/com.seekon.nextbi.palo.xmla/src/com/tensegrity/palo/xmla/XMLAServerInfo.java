package com.tensegrity.palo.xmla;

import com.tensegrity.palojava.ServerInfo;

public class XMLAServerInfo implements ServerInfo {
  private String id;

  private String name;

  private String description;

  private String url;

  private String authentication;

  XMLAServerInfo(String paramString) {
    this.id = paramString;
    this.name = paramString;
  }

  public int getBugfixVersion() {
    return 0;
  }

  public int getBuildNumber() {
    return 0;
  }

  public int getMajor() {
    return 4;
  }

  public int getMinor() {
    return 0;
  }

  public boolean isLegacy() {
    return false;
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public int getType() {
    return 3;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String paramString) {
    this.description = paramString;
  }

  public void setAuthentication(String paramString) {
    this.authentication = paramString;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String paramString) {
    this.url = paramString;
  }

  public boolean canBeModified() {
    return false;
  }

  public boolean canCreateChildren() {
    return false;
  }

  public int getEncryption() {
    return 0;
  }

  public int getHttpsPort() {
    return 0;
  }

  public String getServerType() {
    return "XMLA";
  }

  public String getVersion() {
    return "0";
  }

  public String[] getProperties() {
    return new String[] { "SecurityInfoProperty", this.authentication,
      "DescriptionProperty", getDescription() };
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.XMLAServerInfo JD-Core Version:
 * 0.5.4
 */