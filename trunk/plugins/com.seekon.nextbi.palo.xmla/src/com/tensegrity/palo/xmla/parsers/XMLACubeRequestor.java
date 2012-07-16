package com.tensegrity.palo.xmla.parsers;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import com.tensegrity.palo.xmla.XMLAVariableInfo;
import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.VariableInfoBuilder;
import com.tensegrity.palojava.PropertyInfo;
import com.tensegrity.palojava.loader.PropertyLoader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class XMLACubeRequestor extends AbstractXMLARequestor
{
  public static String ITEM_CATALOG_NAME = "CATALOG_NAME";
  public static String ITEM_SCHEMA_NAME = "SCHEMA_NAME";
  public static String ITEM_CUBE_NAME = "CUBE_NAME";
  public static String ITEM_CUBE_TYPE = "CUBE_TYPE";
  public static String ITEM_CUBE_GUID = "CUBE_GUID";
  public static String ITEM_CREATED_ON = "CREATED_ON";
  public static String ITEM_LAST_SCHEMA_UPDATE = "LAST_SCHEMA_UPDATE";
  public static String ITEM_SCHEMA_UPDATED_BY = "SCHEMA_UPDATED_BY";
  public static String ITEM_LAST_DATA_UPDATE = "LAST_DATA_UPDATE";
  public static String ITEM_DATA_UPDATED_BY = "DATA_UPDATED_BY";
  public static String ITEM_DESCRIPTION = "DESCRIPTION";
  public static String ITEM_IS_DRILLTHROUGH_ENABLED = "IS_DRILLTHROUGH_ENABLED";
  public static String ITEM_IS_LINKABLE = "IS_LINKABLE";
  public static String ITEM_IS_WRITE_ENABLED = "IS_WRITE_ENABLED";
  public static String ITEM_IS_SQL_ENABLED = "IS_SQL_ENABLED";
  public static String ITEM_CUBE_CAPTION = "CUBE_CAPTION";
  public static String ITEM_BASE_CUBE_NAME = "BASE_CUBE_NAME";
  public static String ITEM_ANNOTATIONS = "ANNOTATIONS";
  private String restrictionCatalog;
  private String restrictionSchema;
  private String restrictionCube;
  private String restrictionCubeSource;
  private String restrictionBaseCube;
  private String propertyCatalog;
  private final ArrayList<XMLACubeInfo> cubeInfos = new ArrayList();
  private final XMLADatabaseInfo database;
  private final XMLAConnection connection;

  public XMLACubeRequestor(XMLAConnection paramXMLAConnection, XMLADatabaseInfo paramXMLADatabaseInfo)
  {
    activateItem(ITEM_CATALOG_NAME);
    activateItem(ITEM_SCHEMA_NAME);
    activateItem(ITEM_CUBE_NAME);
    activateItem(ITEM_CUBE_TYPE);
    activateItem(ITEM_CUBE_GUID);
    activateItem(ITEM_CREATED_ON);
    activateItem(ITEM_LAST_SCHEMA_UPDATE);
    activateItem(ITEM_SCHEMA_UPDATED_BY);
    activateItem(ITEM_LAST_DATA_UPDATE);
    activateItem(ITEM_DATA_UPDATED_BY);
    activateItem(ITEM_DESCRIPTION);
    activateItem(ITEM_IS_DRILLTHROUGH_ENABLED);
    activateItem(ITEM_IS_LINKABLE);
    activateItem(ITEM_IS_WRITE_ENABLED);
    activateItem(ITEM_IS_SQL_ENABLED);
    activateItem(ITEM_CUBE_CAPTION);
    activateItem(ITEM_BASE_CUBE_NAME);
    activateItem(ITEM_ANNOTATIONS);
    this.database = paramXMLADatabaseInfo;
    this.connection = paramXMLAConnection;
  }

  public void setCatalogNameProperty(String paramString)
  {
    this.propertyCatalog = paramString;
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

  public void setCubeSourceRestriction(String paramString)
  {
    this.restrictionCubeSource = paramString;
  }

  public void setBaseCubeRestriction(String paramString)
  {
    this.restrictionBaseCube = paramString;
  }

  private final XMLARestrictions setRestrictions()
  {
    XMLARestrictions localXMLARestrictions = new XMLARestrictions();
    localXMLARestrictions.setCatalog(this.restrictionCatalog);
    localXMLARestrictions.setSchema(this.restrictionSchema);
    localXMLARestrictions.setCubeName(this.restrictionCube);
    localXMLARestrictions.setCubeSource(this.restrictionCubeSource);
    localXMLARestrictions.setBaseCube(this.restrictionBaseCube);
    return localXMLARestrictions;
  }

  public XMLACubeInfo[] requestCubes(XMLAClient paramXMLAClient)
  {
    this.cubeInfos.clear();
    try
    {
      XMLARestrictions localXMLARestrictions = setRestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      String str = paramXMLAClient.getConnections()[0].getName();
      localXMLAProperties.setDataSourceInfo(str);
      localXMLAProperties.setCatalog(this.propertyCatalog);
      Document localDocument = paramXMLAClient.getCubeList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      if ((localNodeList == null) || (localNodeList.getLength() == 0))
        return new XMLACubeInfo[0];
      parseXMLANodeList(localNodeList, str, paramXMLAClient);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return new XMLACubeInfo[0];
    }
    return (XMLACubeInfo[])this.cubeInfos.toArray(new XMLACubeInfo[0]);
  }

  protected void parseResult(HashMap<String, String> paramHashMap, String paramString, XMLAClient paramXMLAClient)
  {
    String str = (String)paramHashMap.get(ITEM_CUBE_NAME);
    XMLACubeInfo localXMLACubeInfo = new XMLACubeInfo(str, str, this.database, paramString, paramXMLAClient, this.connection);
    BigInteger localBigInteger = new BigInteger("1");
    XMLADimensionRequestor localXMLADimensionRequestor = new XMLADimensionRequestor(localXMLACubeInfo, this.connection);
    localXMLADimensionRequestor.setCatalogNameRestriction(this.restrictionCatalog);
    localXMLADimensionRequestor.setCubeNameRestriction(str);
    XMLADimensionInfo[] arrayOfXMLADimensionInfo = localXMLADimensionRequestor.requestDimensions(paramXMLAClient);
    localXMLACubeInfo.setDimensionCount(arrayOfXMLADimensionInfo.length);
    ArrayList localArrayList = new ArrayList();
    for (XMLADimensionInfo localObject3 : arrayOfXMLADimensionInfo)
      localArrayList.add(localObject3.getId());
    localXMLACubeInfo.setDimensions((String[])localArrayList.toArray(new String[0]));
    localXMLACubeInfo.setCellCount(localBigInteger);
    localXMLACubeInfo.setFilledCellCount(localBigInteger);
    
    PropertyInfo propertyInfo = this.connection.getPropertyLoader().load("SAP_VARIABLES");
    if ((propertyInfo != null) && (Boolean.parseBoolean((propertyInfo).getValue())))
    {
    	XMLAVariableInfo[] localObject2 = BuilderRegistry.getInstance().getVariableInfoBuilder().requestVariables(paramXMLAClient, (XMLADatabaseInfo)localXMLACubeInfo.getDatabase(), localXMLACubeInfo.getId());
      localXMLACubeInfo.setVariables(localObject2);
      if ((XMLAClient.IGNORE_VARIABLE_CUBES) && (localObject2 != null) && (localObject2.length > 0))
        return;
    }
    if (paramXMLAClient.isSAP())
    {
    	String localObject2 = (String)paramHashMap.get(ITEM_DESCRIPTION);
      if ((localObject2 != null) && (((String)localObject2).trim().length() > 0))
        localXMLACubeInfo.setName((String)localObject2);
    }
    Object localObject2 = (String)paramHashMap.get(ITEM_CUBE_CAPTION);
    if ((localObject2 != null) && (((String)localObject2).trim().length() > 0))
      localXMLACubeInfo.setName((String)localObject2);
    this.cubeInfos.add(localXMLACubeInfo);
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.parsers.XMLACubeRequestor
 * JD-Core Version:    0.5.4
 */