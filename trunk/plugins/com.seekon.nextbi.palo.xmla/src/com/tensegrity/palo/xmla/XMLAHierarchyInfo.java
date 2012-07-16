package com.tensegrity.palo.xmla;

import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.HierarchyInfo;

public class XMLAHierarchyInfo
  implements HierarchyInfo
{
  private String name;
  private String id;
  private final DimensionInfo dimension;
  private int cardinality;

  public XMLAHierarchyInfo(DimensionInfo paramDimensionInfo, String paramString1, String paramString2)
  {
    this.dimension = paramDimensionInfo;
    this.id = paramString2;
    this.name = paramString1;
  }

  public String getName()
  {
    return this.name;
  }

  public String getId()
  {
    return this.id;
  }

  public int getType()
  {
    return 0;
  }

  public void rename(String paramString)
  {
  }

  public boolean canBeModified()
  {
    return false;
  }

  public boolean canCreateChildren()
  {
    return false;
  }

  public DimensionInfo getDimension()
  {
    return this.dimension;
  }

  public int getElementCount()
  {
    return this.cardinality;
  }

  public void setCardinality(String paramString)
  {
    try
    {
      this.cardinality = Integer.parseInt(paramString);
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
  }

  public int getMaxDepth()
  {
    return this.dimension.getMaxDepth();
  }

  public int getMaxLevel()
  {
    return this.dimension.getMaxLevel();
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLAHierarchyInfo
 * JD-Core Version:    0.5.4
 */