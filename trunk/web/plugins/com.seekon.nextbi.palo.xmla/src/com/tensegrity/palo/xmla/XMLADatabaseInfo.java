package com.tensegrity.palo.xmla;

import com.tensegrity.palo.xmla.parsers.XMLACubeRequestor;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.PropertyInfo;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMLADatabaseInfo implements DatabaseInfo, XMLAPaloInfo {
  private String name;

  private String id;

  private int dimensionCount = 0;

  private int cubeCount;

  private final Map cubes;

  private boolean dimensionCountSet = false;

  private boolean cubeCountSet = false;

  private final XMLAConnection connection;

  public XMLADatabaseInfo(XMLAConnection paramXMLAConnection, String paramString) {
    this.name = paramString;
    this.id = paramString;
    this.cubes = new LinkedHashMap();
    this.connection = paramXMLAConnection;
  }

  public void setName(String paramString) {
    this.name = paramString;
  }

  public int getCubeCount() {
    if (!this.cubeCountSet) {
      this.cubeCountSet = true;
      XMLACubeRequestor localXMLACubeRequestor = new XMLACubeRequestor(
        this.connection, this);
    }
    return this.cubeCount;
  }

  public int getDimensionCount() {
    return this.dimensionCount;
  }

  public final void setCubeCount(int paramInt) {
    this.cubeCount = paramInt;
  }

  public final void setDimensionCount(int paramInt) {
    this.dimensionCount = paramInt;
  }

  public String getName() {
    return this.name;
  }

  public int getStatus() {
    return 1;
  }

  public int getToken() {
    return 0;
  }

  public String getId() {
    return this.id;
  }

  public int getType() {
    return 0;
  }

  public String toString() {
    return "Database " + this.name + " [" + this.id + "]. Cubes: " + getCubeCount()
      + ", Dimensions: " + getDimensionCount() + ", Status: " + getStatus()
      + ", Token: " + getToken() + ", Type: " + getType();
  }

  public void addCubeInternal(XMLACubeInfo paramXMLACubeInfo) {
    this.cubes.put(paramXMLACubeInfo.getId(), paramXMLACubeInfo);
  }

  public XMLACubeInfo getCubeInternal(String paramString) {
    return (XMLACubeInfo) this.cubes.get(paramString);
  }

  public int getCubeCountInternal() {
    return this.cubes.size();
  }

  public XMLACubeInfo[] getCubesInternal() {
    return (XMLACubeInfo[]) (XMLACubeInfo[]) this.cubes.values().toArray(
      new XMLACubeInfo[0]);
  }

  public boolean isSystem() {
    return false;
  }

  public boolean isUserInfo() {
    return false;
  }

  public String[] getAllKnownPropertyIds(DbConnection paramDbConnection) {
    return new String[0];
  }

  public PropertyInfo getProperty(DbConnection paramDbConnection, String paramString) {
    return null;
  }

  public boolean canBeModified() {
    return false;
  }

  public boolean canCreateChildren() {
    return false;
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.XMLADatabaseInfo JD-Core Version:
 * 0.5.4
 */