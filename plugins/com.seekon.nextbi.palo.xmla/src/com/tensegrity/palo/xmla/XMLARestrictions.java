package com.tensegrity.palo.xmla;

public class XMLARestrictions
{
  public static String elementIndent = "  ";
  private String catalog;
  private String cubeName;
  private String dimensionUniqueName;
  private String dimensionName;
  private String hierarchyUniqueName;
  private String hierarchyName;
  private String hierarchyOrigin;
  private String hierarchyVisibility;
  private String dimensionVisibility;
  private String levelUniqueName;
  private String memberUniqueName;
  private String treeOp;
  private String levelNumber;
  private String schemaName;
  private String cubeSource;
  private String baseCube;
  private String memberType;

  public String getCatalog()
  {
    return this.catalog;
  }

  public void setCatalog(String paramString)
  {
    this.catalog = paramString;
  }

  public String getSchema()
  {
    return this.schemaName;
  }

  public void setSchema(String paramString)
  {
    this.schemaName = paramString;
  }

  public String getCubeSource()
  {
    return this.cubeSource;
  }

  public void setCubeSource(String paramString)
  {
    this.cubeSource = paramString;
  }

  public String getBaseCube()
  {
    return this.baseCube;
  }

  public void setBaseCube(String paramString)
  {
    this.baseCube = paramString;
  }

  public String getCubeName()
  {
    return this.cubeName;
  }

  public void setCubeName(String paramString)
  {
    this.cubeName = paramString;
  }

  public String getDimensionUniqueName()
  {
    return this.dimensionUniqueName;
  }

  public void setDimensionUniqueName(String paramString)
  {
    this.dimensionUniqueName = paramString;
  }

  public String getDimensionName()
  {
    return this.dimensionName;
  }

  public void setDimensionName(String paramString)
  {
    this.dimensionName = paramString;
  }

  public String getHierarchyUniqueName()
  {
    return this.hierarchyUniqueName;
  }

  public void setHierarchyUniqueName(String paramString)
  {
    this.hierarchyUniqueName = paramString;
  }

  public String getHierarchyName()
  {
    return this.hierarchyName;
  }

  public void setHierarchyName(String paramString)
  {
    this.hierarchyName = paramString;
  }

  public String getHierarchyOrigin()
  {
    return this.hierarchyOrigin;
  }

  public void setHierarchyOrigin(String paramString)
  {
    this.hierarchyOrigin = paramString;
  }

  public String getHierarchyVisibility()
  {
    return this.hierarchyVisibility;
  }

  public void setHierarchyVisibility(String paramString)
  {
    this.hierarchyVisibility = paramString;
  }

  public String getDimensionVisibility()
  {
    return this.dimensionVisibility;
  }

  public void setDimensionVisibility(String paramString)
  {
    this.dimensionVisibility = paramString;
  }

  public void setLevelUniqueName(String paramString)
  {
    this.levelUniqueName = paramString;
  }

  public String getLevelUniqueName()
  {
    return this.levelUniqueName;
  }

  public void setMemberUniqueName(String paramString)
  {
    this.memberUniqueName = paramString;
  }

  public String getMemberUniqueName()
  {
    return this.memberUniqueName;
  }

  public void setMemberType(int paramInt)
  {
    this.memberType = ("" + paramInt);
  }

  public String getMemberType()
  {
    return this.memberType;
  }

  public void setTreeOp(int paramInt)
  {
    this.treeOp = ("" + paramInt);
  }

  public String getTreeOp()
  {
    return this.treeOp;
  }

  public String getLevelNumber()
  {
    return this.levelNumber;
  }

  public void setLevelNumber(String paramString)
  {
    this.levelNumber = paramString;
  }

  private final boolean valid(String paramString)
  {
    return (paramString != null) && (paramString.trim().length() > 0);
  }

  private final String format(String paramString1, String paramString2, String paramString3)
  {
    return "\n" + paramString1 + elementIndent + "<" + paramString2 + ">" + paramString3.trim() + "</" + paramString2 + ">";
  }

  protected String getRestrictionListXML(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer("\n" + paramString + "<RestrictionList>");
    if (valid(this.catalog))
      localStringBuffer.append(format(paramString, "CATALOG_NAME", this.catalog));
    if (valid(this.cubeName))
      localStringBuffer.append(format(paramString, "CUBE_NAME", this.cubeName));
    if (valid(this.dimensionUniqueName))
      localStringBuffer.append(format(paramString, "DIMENSION_UNIQUE_NAME", this.dimensionUniqueName));
    if (valid(this.dimensionName))
      localStringBuffer.append(format(paramString, "DIMENSION_NAME", this.dimensionName));
    if (valid(this.hierarchyUniqueName))
      localStringBuffer.append(format(paramString, "HIERARCHY_UNIQUE_NAME", this.hierarchyUniqueName));
    if (valid(this.levelUniqueName))
      localStringBuffer.append(format(paramString, "LEVEL_UNIQUE_NAME", this.levelUniqueName));
    if (valid(this.memberUniqueName))
      localStringBuffer.append(format(paramString, "MEMBER_UNIQUE_NAME", this.memberUniqueName));
    if (valid(this.treeOp))
      localStringBuffer.append(format(paramString, "TREE_OP", this.treeOp));
    if (valid(this.levelNumber))
      localStringBuffer.append(format(paramString, "LEVEL_NUMBER", this.levelNumber));
    if (valid(this.schemaName))
      localStringBuffer.append(format(paramString, "SCHEMA_NAME", this.schemaName));
    if (valid(this.cubeSource))
      localStringBuffer.append(format(paramString, "CUBE_SOURCE", this.cubeSource));
    if (valid(this.baseCube))
      localStringBuffer.append(format(paramString, "BASE_CUBE_NAME", this.baseCube));
    if (valid(this.hierarchyName))
      localStringBuffer.append(format(paramString, "HIERARCHY_NAME", this.hierarchyName));
    if (valid(this.hierarchyOrigin))
      localStringBuffer.append(format(paramString, "HIERARCHY_ORIGIN", this.hierarchyOrigin));
    if (valid(this.hierarchyVisibility))
      localStringBuffer.append(format(paramString, "HIERARCHY_VISIBILITY", this.hierarchyVisibility));
    if (valid(this.dimensionVisibility))
      localStringBuffer.append(format(paramString, "DIMENSION_VISIBILITY", this.dimensionVisibility));
    if (valid(this.memberType))
      localStringBuffer.append(format(paramString, "MEMBER_TYPE", this.memberType));
    localStringBuffer.append("\n" + paramString + "</RestrictionList>");
    return localStringBuffer.toString();
  }

  public String getXML(String paramString)
  {
    return "\n" + paramString + "<Restrictions>" + getRestrictionListXML(new StringBuilder().append(paramString).append(elementIndent).toString()) + "\n" + paramString + "</Restrictions>";
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLARestrictions
 * JD-Core Version:    0.5.4
 */