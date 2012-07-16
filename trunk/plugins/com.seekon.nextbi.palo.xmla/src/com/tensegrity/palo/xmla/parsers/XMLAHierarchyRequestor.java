package com.tensegrity.palo.xmla.parsers;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAHierarchyInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLAHierarchyRequestor extends AbstractXMLARequestor
{
  public static String ITEM_CATALOG_NAME = "CATALOG_NAME";
  public static String ITEM_SCHEMA_NAME = "SCHEMA_NAME";
  public static String ITEM_CUBE_NAME = "CUBE_NAME";
  public static String ITEM_DIMENSION_UNIQUE_NAME = "DIMENSION_UNIQUE_NAME";
  public static String ITEM_HIERARCHY_NAME = "HIERARCHY_NAME";
  public static String ITEM_HIERARCHY_UNIQUE_NAME = "HIERARCHY_UNIQUE_NAME";
  public static String ITEM_HIERARCHY_GUID = "HIERARCHY_GUID";
  public static String ITEM_HIERARCHY_CAPTION = "HIERARCHY_CAPTION";
  public static String ITEM_DIMENSION_TYPE = "DIMENSION_TYPE";
  public static String ITEM_HIERARCHY_CARDINALITY = "HIERARCHY_CARDINALITY";
  public static String ITEM_DEFAULT_MEMBER = "DEFAULT_MEMBER";
  public static String ITEM_ALL_MEMBER = "ALL_MEMBER";
  public static String ITEM_DESCRIPTION = "DESCRIPTION";
  public static String ITEM_STRUCTURE = "STRUCTURE";
  public static String ITEM_IS_VIRTUAL = "IS_VIRTUAL";
  public static String ITEM_IS_READWRITE = "IS_READWRITE";
  public static String ITEM_DIMENSION_UNIQUE_SETTINGS = "DIMENSION_UNIQUE_SETTINGS";
  public static String ITEM_DIMENSION_MASTER_UNIQUE_NAME = "DIMENSION_MASTER_UNIQUE_NAME";
  public static String ITEM_DIMENSION_IS_VISIBLE = "DIMENSION_IS_VISIBLE";
  public static String ITEM_HIERARCHY_ORDINAL = "HIERARCHY_ORDINAL";
  public static String ITEM_DIMENSION_IS_SHARED = "DIMENSION_IS_SHARED";
  public static String ITEM_HIERARCHY_IS_VISIBLE = "HIERARCHY_IS_VISIBLE";
  public static String ITEM_HIERARCHY_ORIGIN = "HIERARCHY_ORIGIN";
  public static String ITEM_HIERARCHY_DISPLAY_FOLDER = "HIERARCHY_DISPLAY_FOLDER";
  public static String ITEM_INSTANCE_SELECTION = "INSTANCE_SELECTION";
  private String restrictionCatalog;
  private String restrictionSchema;
  private String restrictionCube;
  private String restrictionDimensionUniqueName;
  private String restrictionHierarchyName;
  private String restrictionHierarchyUniqueName;
  private String restrictionHierarchyOrigin;
  private String restrictionCubeSource;
  private String restrictionHierarchyVisibility;
  private final ArrayList<XMLAHierarchyInfo> hierarchyInfos = new ArrayList();
  private final XMLADatabaseInfo database;
  private final XMLADimensionInfo dimension;
  private final XMLAConnection connection;

  public XMLAHierarchyRequestor(XMLADimensionInfo paramXMLADimensionInfo, XMLADatabaseInfo paramXMLADatabaseInfo, XMLAConnection paramXMLAConnection)
  {
    activateItem(ITEM_CATALOG_NAME);
    activateItem(ITEM_SCHEMA_NAME);
    activateItem(ITEM_CUBE_NAME);
    activateItem(ITEM_DIMENSION_UNIQUE_NAME);
    activateItem(ITEM_HIERARCHY_NAME);
    activateItem(ITEM_HIERARCHY_UNIQUE_NAME);
    activateItem(ITEM_HIERARCHY_GUID);
    activateItem(ITEM_HIERARCHY_CAPTION);
    activateItem(ITEM_DIMENSION_TYPE);
    activateItem(ITEM_HIERARCHY_CARDINALITY);
    activateItem(ITEM_DEFAULT_MEMBER);
    activateItem(ITEM_ALL_MEMBER);
    activateItem(ITEM_DESCRIPTION);
    activateItem(ITEM_STRUCTURE);
    activateItem(ITEM_IS_VIRTUAL);
    activateItem(ITEM_IS_READWRITE);
    activateItem(ITEM_DIMENSION_UNIQUE_SETTINGS);
    activateItem(ITEM_DIMENSION_MASTER_UNIQUE_NAME);
    activateItem(ITEM_DIMENSION_IS_VISIBLE);
    activateItem(ITEM_HIERARCHY_ORDINAL);
    activateItem(ITEM_DIMENSION_IS_SHARED);
    activateItem(ITEM_HIERARCHY_IS_VISIBLE);
    activateItem(ITEM_HIERARCHY_ORIGIN);
    activateItem(ITEM_HIERARCHY_DISPLAY_FOLDER);
    activateItem(ITEM_INSTANCE_SELECTION);
    this.database = paramXMLADatabaseInfo;
    this.dimension = paramXMLADimensionInfo;
    this.connection = paramXMLAConnection;
  }

  public void setCatalogNameRestriction(String paramString)
  {
    this.restrictionCatalog = paramString;
  }

  public void setSchemaNameRestriction(String paramString)
  {
    this.restrictionSchema = paramString;
  }

  public void setCubeNameRestriction(String paramString)
  {
    this.restrictionCube = paramString;
  }

  public void setDimensionUniqueNameRestriction(String paramString)
  {
    this.restrictionDimensionUniqueName = paramString;
  }

  public void setHierarchyNameRestriction(String paramString)
  {
    this.restrictionHierarchyName = paramString;
  }

  public void setHierarchyUniqueNameRestriction(String paramString)
  {
    this.restrictionHierarchyUniqueName = paramString;
  }

  public void setHierarchyOriginRestriction(String paramString)
  {
    this.restrictionHierarchyOrigin = paramString;
  }

  public void setCubeSourceRestriction(String paramString)
  {
    this.restrictionCubeSource = paramString;
  }

  public void setHierarchyVisibilityRestriction(String paramString)
  {
    this.restrictionHierarchyVisibility = paramString;
  }

  private final XMLARestrictions setRestrictions()
  {
    XMLARestrictions localXMLARestrictions = new XMLARestrictions();
    localXMLARestrictions.setCatalog(this.restrictionCatalog);
    localXMLARestrictions.setSchema(this.restrictionSchema);
    localXMLARestrictions.setCubeName(this.restrictionCube);
    localXMLARestrictions.setDimensionUniqueName(this.restrictionDimensionUniqueName);
    localXMLARestrictions.setHierarchyName(this.restrictionHierarchyName);
    localXMLARestrictions.setHierarchyUniqueName(this.restrictionHierarchyUniqueName);
    localXMLARestrictions.setHierarchyOrigin(this.restrictionHierarchyOrigin);
    localXMLARestrictions.setCubeSource(this.restrictionCubeSource);
    localXMLARestrictions.setHierarchyVisibility(this.restrictionHierarchyVisibility);
    return localXMLARestrictions;
  }

  public XMLAHierarchyInfo[] requestHierarchies(XMLAClient paramXMLAClient)
  {
    this.hierarchyInfos.clear();
    try
    {
      XMLARestrictions localXMLARestrictions = setRestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      String str = paramXMLAClient.getConnections()[0].getName();
      localXMLAProperties.setDataSourceInfo(str);
      localXMLAProperties.setCatalog(this.database.getId());
      Document localDocument = paramXMLAClient.getHierarchyList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      if ((localNodeList == null) || (localNodeList.getLength() == 0))
        return new XMLAHierarchyInfo[0];
      parseXMLANodeList(localNodeList, str, paramXMLAClient);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return new XMLAHierarchyInfo[0];
    }
    this.dimension.setHierarchyCount(this.hierarchyInfos.size());
    return (XMLAHierarchyInfo[])this.hierarchyInfos.toArray(new XMLAHierarchyInfo[0]);
  }

  protected void parseResult(HashMap<String, String> paramHashMap, String paramString, XMLAClient paramXMLAClient)
  {
    String str1 = XMLADimensionInfo.getIDString((String)paramHashMap.get(ITEM_HIERARCHY_UNIQUE_NAME), this.dimension.getCubeId());
    String str2 = (String)paramHashMap.get(ITEM_HIERARCHY_CAPTION);
    XMLAHierarchyInfo localXMLAHierarchyInfo = new XMLAHierarchyInfo(this.dimension, str2, str1);
    localXMLAHierarchyInfo.setCardinality((String)paramHashMap.get(ITEM_HIERARCHY_CARDINALITY));
    this.hierarchyInfos.add(localXMLAHierarchyInfo);
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.parsers.XMLAHierarchyRequestor
 * JD-Core Version:    0.5.4
 */