package com.tensegrity.palo.xmla.parsers;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAHierarchyInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.HierarchyInfo;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLADimensionRequestor extends AbstractXMLARequestor
{
  public static String ITEM_CATALOG_NAME = "CATALOG_NAME";
  public static String ITEM_SCHEMA_NAME = "SCHEMA_NAME";
  public static String ITEM_CUBE_NAME = "CUBE_NAME";
  public static String ITEM_DIMENSION_NAME = "DIMENSION_NAME";
  public static String ITEM_DIMENSION_UNIQUE_NAME = "DIMENSION_UNIQUE_NAME";
  public static String ITEM_DIMENSION_GUID = "DIMENSION_GUID";
  public static String ITEM_DIMENSION_CAPTION = "DIMENSION_CAPTION";
  public static String ITEM_DIMENSION_ORDINAL = "DIMENSION_ORDINAL";
  public static String ITEM_DIMENSION_TYPE = "DIMENSION_TYPE";
  public static String ITEM_DIMENSION_CARDINALITY = "DIMENSION_CARDINALITY";
  public static String ITEM_DEFAULT_HIERARCHY = "DEFAULT_HIERARCHY";
  public static String ITEM_DESCRIPTION = "DESCRIPTION";
  public static String ITEM_IS_VIRTUAL = "IS_VIRTUAL";
  public static String ITEM_IS_READWRITE = "IS_READWRITE";
  public static String ITEM_DIMENSION_UNIQUE_SETTINGS = "DIMENSION_UNIQUE_SETTINGS";
  public static String ITEM_DIMENSION_MASTER_UNIQUE_NAME = "DIMENSION_MASTER_UNIQUE_NAME";
  public static String ITEM_DIMENSION_IS_VISIBLE = "DIMENSION_IS_VISIBLE";
  private String restrictionCatalog;
  private String restrictionSchema;
  private String restrictionCube;
  private String restrictionDimensionName;
  private String restrictionDimensionUniqueName;
  private String restrictionCubeSource;
  private String restrictionDimensionVisibility;
  private final ArrayList<XMLADimensionInfo> dimensionInfos = new ArrayList();
  private final XMLACubeInfo cube;
  private final XMLAConnection connection;

  public XMLADimensionRequestor(XMLACubeInfo paramXMLACubeInfo, XMLAConnection paramXMLAConnection)
  {
    activateItem(ITEM_CATALOG_NAME);
    activateItem(ITEM_SCHEMA_NAME);
    activateItem(ITEM_CUBE_NAME);
    activateItem(ITEM_DIMENSION_NAME);
    activateItem(ITEM_DIMENSION_UNIQUE_NAME);
    activateItem(ITEM_DIMENSION_GUID);
    activateItem(ITEM_DIMENSION_CAPTION);
    activateItem(ITEM_DIMENSION_ORDINAL);
    activateItem(ITEM_DIMENSION_TYPE);
    activateItem(ITEM_DIMENSION_CARDINALITY);
    activateItem(ITEM_DEFAULT_HIERARCHY);
    activateItem(ITEM_DESCRIPTION);
    activateItem(ITEM_IS_VIRTUAL);
    activateItem(ITEM_IS_READWRITE);
    activateItem(ITEM_DIMENSION_UNIQUE_SETTINGS);
    activateItem(ITEM_DIMENSION_MASTER_UNIQUE_NAME);
    activateItem(ITEM_DIMENSION_IS_VISIBLE);
    this.cube = paramXMLACubeInfo;
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

  public void setDimensionNameRestriction(String paramString)
  {
    this.restrictionDimensionName = paramString;
  }

  public void setDimensionUniqueNameRestriction(String paramString)
  {
    this.restrictionDimensionUniqueName = paramString;
  }

  public void setCubeSourceRestriction(String paramString)
  {
    this.restrictionCubeSource = paramString;
  }

  public void setDimensionVisibilityRestriction(String paramString)
  {
    this.restrictionDimensionVisibility = paramString;
  }

  private final XMLARestrictions setRestrictions()
  {
    XMLARestrictions localXMLARestrictions = new XMLARestrictions();
    localXMLARestrictions.setCatalog(this.restrictionCatalog);
    localXMLARestrictions.setSchema(this.restrictionSchema);
    localXMLARestrictions.setCubeName(this.restrictionCube);
    localXMLARestrictions.setDimensionName(this.restrictionDimensionName);
    localXMLARestrictions.setDimensionUniqueName(this.restrictionDimensionUniqueName);
    localXMLARestrictions.setCubeSource(this.restrictionCubeSource);
    localXMLARestrictions.setDimensionVisibility(this.restrictionDimensionVisibility);
    return localXMLARestrictions;
  }

  public XMLADimensionInfo[] requestDimensions(XMLAClient paramXMLAClient)
  {
    this.dimensionInfos.clear();
    try
    {
      XMLARestrictions localXMLARestrictions = setRestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      String str = paramXMLAClient.getConnections()[0].getName();
      localXMLAProperties.setDataSourceInfo(str);
      localXMLAProperties.setCatalog(this.cube.getDatabase().getId());
      Document localDocument = paramXMLAClient.getDimensionList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      if ((localNodeList == null) || (localNodeList.getLength() == 0))
        return new XMLADimensionInfo[0];
      parseXMLANodeList(localNodeList, str, paramXMLAClient);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return new XMLADimensionInfo[0];
    }
    return (XMLADimensionInfo[])this.dimensionInfos.toArray(new XMLADimensionInfo[0]);
  }

  protected void parseResult(HashMap<String, String> paramHashMap, String paramString, XMLAClient paramXMLAClient)
  {
    String str1 = (String)paramHashMap.get(ITEM_DIMENSION_CAPTION);
    String str2 = (String)paramHashMap.get(ITEM_DIMENSION_UNIQUE_NAME);
    XMLADimensionInfo localXMLADimensionInfo = new XMLADimensionInfo(paramXMLAClient, str1, str2, (XMLADatabaseInfo)this.cube.getDatabase(), this.cube.getId(), this.connection);
    localXMLADimensionInfo.setDimensionUniqueName(str2);
    try
    {
      localXMLADimensionInfo.setElementCount(Integer.parseInt((String)paramHashMap.get(ITEM_DIMENSION_CARDINALITY)));
    }
    catch (Exception localException1)
    {
    }
    try
    {
      int i = Integer.parseInt((String)paramHashMap.get(ITEM_DIMENSION_TYPE));
      localXMLADimensionInfo.setXmlaType((i == 2) ? 1 : 0);
    }
    catch (Exception localException2)
    {
    }
    String str3 = XMLADimensionInfo.getIDString((String)paramHashMap.get(ITEM_DEFAULT_HIERARCHY), this.cube.getId());
    Object localObject = this.connection.getHierarchy(localXMLADimensionInfo, str3);
    if (localObject == null)
    {
      HierarchyInfo[] arrayOfHierarchyInfo1 = this.connection.getHierarchies(localXMLADimensionInfo);
      if (arrayOfHierarchyInfo1 != null)
        for (HierarchyInfo localHierarchyInfo : arrayOfHierarchyInfo1)
        {
          if (localHierarchyInfo == null)
            continue;
          localObject = localHierarchyInfo;
          break;
        }
    }
    localXMLADimensionInfo.setDefaultHierarchy((XMLAHierarchyInfo)localObject);
    localXMLADimensionInfo.setHierarchyUniqueName(((HierarchyInfo)localObject).getId());
    this.dimensionInfos.add(localXMLADimensionInfo);
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.parsers.XMLADimensionRequestor
 * JD-Core Version:    0.5.4
 */