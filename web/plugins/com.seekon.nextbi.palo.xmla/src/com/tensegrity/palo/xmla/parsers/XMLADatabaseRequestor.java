package com.tensegrity.palo.xmla.parsers;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLADatabaseRequestor extends AbstractXMLARequestor {
  public static String ITEM_CATALOG_NAME = "CATALOG_NAME";

  public static String ITEM_DESCRIPTION = "DESCRIPTION";

  public static String ITEM_ROLES = "ROLES";

  public static String ITEM_DATE_MODIFIED = "DATE_MODIFIED";

  private String restrictionName;

  private final ArrayList<XMLADatabaseInfo> databaseInfos = new ArrayList();

  private final XMLAConnection connection;

  public XMLADatabaseRequestor(XMLAConnection paramXMLAConnection) {
    activateItem(ITEM_CATALOG_NAME);
    this.connection = paramXMLAConnection;
  }

  public void setCatalogNameRestriction(String paramString) {
    this.restrictionName = paramString;
  }

  public boolean requestDatabases(XMLAClient paramXMLAClient) {
    this.databaseInfos.clear();
    try {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      String str = paramXMLAClient.getConnections()[0].getName();
      localXMLAProperties.setDataSourceInfo(str);
      if ((this.restrictionName != null)
        && (this.restrictionName.trim().length() != 0))
        localXMLARestrictions.setCatalog(this.restrictionName);
      Document localDocument = paramXMLAClient.getCatalogList(localXMLARestrictions,
        localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      if ((localNodeList == null) || (localNodeList.getLength() == 0))
        return false;
      parseXMLANodeList(localNodeList, str, paramXMLAClient);
    } catch (Exception localException) {
      localException.printStackTrace();
      return false;
    }
    return true;
  }

  protected void parseResult(HashMap<String, String> paramHashMap,
    String paramString, XMLAClient paramXMLAClient) {
    String str = (String) paramHashMap.get(ITEM_CATALOG_NAME);
    XMLADatabaseInfo localXMLADatabaseInfo = new XMLADatabaseInfo(this.connection,
      str);
    localXMLADatabaseInfo.setCubeCount(1);
    localXMLADatabaseInfo.setDimensionCount(1);
    this.databaseInfos.add(localXMLADatabaseInfo);
  }

  public XMLADatabaseInfo[] getDatabaseInfos() {
    return (XMLADatabaseInfo[]) this.databaseInfos.toArray(new XMLADatabaseInfo[0]);
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.parsers.XMLADatabaseRequestor
 * JD-Core Version: 0.5.4
 */