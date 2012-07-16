package com.tensegrity.palo.xmla;

import com.tensegrity.palo.xmla.parsers.XMLAHierarchyRequestor;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.HierarchyInfo;
import com.tensegrity.palojava.PropertyInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLADimensionInfo
  implements DimensionInfo, XMLAPaloInfo
{
  public static final int XMLA_TYPE_NORMAL = 0;
  public static final int XMLA_TYPE_MEASURES = 1;
  private String name;
  private XMLADatabaseInfo database;
  private int elementCount = 0;
  private int maxDepth = 0;
  private int maxLevel = 0;
  private String id;
  private String hierarchyUniqueName;
  private String dimensionUniqueName;
  private String dimensionCaption;
  private String hierarchyCaption;
  private String cubeId;
  private final Map elements;
  private final Map elementIds;
  private int xmlaType;
  private final List<XMLAHierarchyInfo> hierarchies;
  private int internalType = 0;
  private String defaultElementName;
  private XMLAHierarchyInfo activeHierarchy = null;
  private XMLAHierarchyInfo defaultHierarchy = null;
  private int hierarchyCount = 0;
  private final XMLAClient xmlaClient;
  private final XMLAConnection connection;

  public XMLADimensionInfo(XMLAClient paramXMLAClient, String paramString1, String paramString2, XMLADatabaseInfo paramXMLADatabaseInfo, String paramString3, XMLAConnection paramXMLAConnection)
  {
    this.name = paramString1;
    this.connection = paramXMLAConnection;
    this.database = paramXMLADatabaseInfo;
    this.cubeId = paramString3;
    if (paramString3 != null)
      this.id = getIDString(paramString2, paramString3);
    else
      this.id = getIDString(paramString2, "");
    this.elements = new LinkedHashMap();
    this.elementIds = new LinkedHashMap();
    this.hierarchies = new ArrayList();
    this.defaultElementName = "";
    this.xmlaClient = paramXMLAClient;
  }

  public String getAttributeCube()
  {
    return "";
  }

  public String getAttributeDimension()
  {
    return "";
  }

  public String getDefaultElementName()
  {
    return this.defaultElementName;
  }

  public void setDefaultElementName(String paramString)
  {
    this.defaultElementName = paramString;
  }

  public DatabaseInfo getDatabase()
  {
    return this.database;
  }

  public int getElementCount()
  {
    return getDefaultHierarchy().getElementCount();
  }

  public void setElementCount(int paramInt)
  {
    this.elementCount = paramInt;
  }

  public int getMaxDepth()
  {
    return this.maxDepth;
  }

  public int getMaxIndent()
  {
    return 0;
  }

  public int getMaxLevel()
  {
    return this.maxLevel;
  }

  public void setMaxLevel(int paramInt)
  {
    this.maxLevel = paramInt;
  }

  public void setMaxDepth(int paramInt)
  {
    this.maxDepth = paramInt;
  }

  public String getName()
  {
    return this.name;
  }

  public String getRightsCube()
  {
    return "";
  }

  public int getToken()
  {
    return 0;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public String getId()
  {
    return this.id;
  }

  public void setId(String paramString)
  {
    if (this.cubeId != null)
      this.id = getIDString(paramString, this.cubeId);
    else
      this.id = getIDString(paramString, "");
  }

  public int getType()
  {
    return 0;
  }

  public String getHierarchyUniqueName()
  {
    return this.hierarchyUniqueName;
  }

  public String getDimensionUniqueName()
  {
    return this.dimensionUniqueName;
  }

  public void setHierarchyUniqueName(String paramString)
  {
    this.hierarchyUniqueName = paramString;
  }

  public void setDimensionUniqueName(String paramString)
  {
    this.dimensionUniqueName = paramString;
  }

  public void setCubeId(String paramString)
  {
    this.cubeId = paramString;
  }

  public String getCubeId()
  {
    return this.cubeId;
  }

  public String toString()
  {
    return "Dimension " + getName() + " [" + getId() + "]: " + getElementCount() + " elements.";
  }

  public void clearMembersInternal()
  {
    this.elements.clear();
    this.elementIds.clear();
  }

  public void addMemberInternal(XMLAElementInfo paramXMLAElementInfo)
  {
    this.elements.put(paramXMLAElementInfo.getUniqueName(), paramXMLAElementInfo);
    this.elementIds.put(paramXMLAElementInfo.getId(), paramXMLAElementInfo);
  }

  public int getMemberCountInternal()
  {
    return this.elements.size();
  }

  public XMLAElementInfo getMemberInternal(String paramString)
  {
    if (this.elements.get(paramString) == null)
    {
      String str = transformId(paramString);
      return (XMLAElementInfo)this.elements.get(str);
    }
    return (XMLAElementInfo)this.elements.get(paramString);
  }

  public XMLAElementInfo getMemberByIdInternal(String paramString)
  {
    if (this.elementIds.get(paramString) == null)
    {
      String str = transformId(paramString);
      return (XMLAElementInfo)this.elementIds.get(str);
    }
    return (XMLAElementInfo)this.elementIds.get(paramString);
  }

  public XMLAElementInfo[] getMembersInternal()
  {
    return (XMLAElementInfo[])(XMLAElementInfo[])this.elements.values().toArray(new XMLAElementInfo[0]);
  }

  public static String getIDString(String paramString1, String paramString2)
  {
    String str1 = paramString2.replaceAll("\\[", "((");
    str1 = str1.replaceAll("\\]", "))");
    str1 = str1.replaceAll(":", "**");
    String str2 = paramString1.replaceAll("\\[", "((");
    str2 = str2.replaceAll("\\]", "))");
    str2 = str2.replaceAll(":", "**");
    str2 = str2.replaceAll(",", "(comma)");
    return str1 + "|.#.|" + str2;
  }

  public static String getCubeNameFromId(String paramString)
  {
    int i = paramString.indexOf("|.#.|");
    if (i == -1)
      return paramString;
    String str = paramString.substring(0, i).trim();
    str = str.replaceAll("\\*\\*", ":");
    str = str.replaceAll("\\)\\)", "]");
    str = str.replaceAll("\\(\\(", "[");
    return str;
  }

  public static String getDimIdFromId(String paramString)
  {
    int i = paramString.indexOf("|.#.|");
    if (i == -1)
      return paramString;
    String str = paramString.substring(i + "|.#.|".length()).trim();
    str = str.replaceAll("\\*\\*", ":");
    str = str.replaceAll("\\)\\)", "]");
    str = str.replaceAll("\\(\\(", "[");
    str = str.replaceAll("\\(comma\\)", ",");
    return str;
  }

  public static String transformId(String paramString)
  {
    String str = paramString.replaceAll("\\(\\(", "[");
    str = str.replaceAll("\\)\\)", "]");
    str = str.replaceAll("\\*\\*", ":");
    str = str.replaceAll("\\(comma\\)", ",");
    return str;
  }

  public void setXmlaType(int paramInt)
  {
    this.xmlaType = paramInt;
    setId(getHierarchyUniqueName());
  }

  public int getXmlaType()
  {
    return this.xmlaType;
  }

  public void setHierarchyCaption(String paramString)
  {
    this.hierarchyCaption = paramString;
  }

  public String getHierarchyCaption()
  {
    return this.hierarchyCaption;
  }

  public void setDimensionCaption(String paramString)
  {
    this.dimensionCaption = paramString;
  }

  public String getDimensionCaption()
  {
    return this.dimensionCaption;
  }

  public void addHierarchy(XMLAHierarchyInfo paramXMLAHierarchyInfo)
  {
    this.hierarchies.add(paramXMLAHierarchyInfo);
  }

  public XMLAHierarchyInfo[] getHierarchies()
  {
    if (this.hierarchies.size() == 0)
    {
      XMLAHierarchyRequestor localXMLAHierarchyRequestor = new XMLAHierarchyRequestor(this, (XMLADatabaseInfo)getDatabase(), this.connection);
      localXMLAHierarchyRequestor.setCubeNameRestriction(getCubeId());
      localXMLAHierarchyRequestor.setCatalogNameRestriction(getDatabase().getId());
      localXMLAHierarchyRequestor.setDimensionUniqueNameRestriction(getDimensionUniqueName());
      this.hierarchies.addAll(Arrays.asList(localXMLAHierarchyRequestor.requestHierarchies(this.xmlaClient)));
    }
    return (XMLAHierarchyInfo[])this.hierarchies.toArray(new XMLAHierarchyInfo[0]);
  }

  public String[] getAllKnownPropertyIds(DbConnection paramDbConnection)
  {
    return new String[0];
  }

  public PropertyInfo getProperty(DbConnection paramDbConnection, String paramString)
  {
    return null;
  }

  public int getInternalXmlaType()
  {
    return this.internalType;
  }

  public void setInternalXmlaType(int paramInt)
  {
    this.internalType = paramInt;
  }

  public boolean canBeModified()
  {
    return false;
  }

  public boolean canCreateChildren()
  {
    return false;
  }

  public void setDefaultHierarchy(XMLAHierarchyInfo paramXMLAHierarchyInfo)
  {
    this.defaultHierarchy = paramXMLAHierarchyInfo;
  }

  public XMLAHierarchyInfo getDefaultHierarchy()
  {
    return this.defaultHierarchy;
  }

  public HierarchyInfo getActiveHierarchy()
  {
    if (this.activeHierarchy == null)
      this.activeHierarchy = this.defaultHierarchy;
    return this.activeHierarchy;
  }

  public void setActiveHierarchy(HierarchyInfo paramHierarchyInfo)
  {
    this.activeHierarchy = ((XMLAHierarchyInfo)paramHierarchyInfo);
  }

  public int getHierarchyCount()
  {
    return this.hierarchyCount;
  }

  public void setHierarchyCount(int paramInt)
  {
    this.hierarchyCount = paramInt;
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLADimensionInfo
 * JD-Core Version:    0.5.4
 */