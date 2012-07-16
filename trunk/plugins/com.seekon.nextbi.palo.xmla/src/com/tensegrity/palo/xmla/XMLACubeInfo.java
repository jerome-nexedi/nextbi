package com.tensegrity.palo.xmla;

import com.tensegrity.palo.xmla.parsers.XMLADimensionRequestor;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.PropertyInfo;
import com.tensegrity.palojava.impl.PropertyInfoImpl;
import java.math.BigInteger;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMLACubeInfo
  implements CubeInfo, XMLAPaloInfo
{
  private String name;
  private XMLADatabaseInfo database;
  private XMLAVariableInfo[] variables;
  private int dimensionCount;
  private String[] dimensions = null;
  private BigInteger cellCount;
  private BigInteger filledCellCount;
  private final Map dimensionMap;
  private String id;
  private String connectionName;
  private final XMLAClient xmlaClient;
  private final XMLAConnection xmlaConnection;

  public XMLACubeInfo(String paramString1, String paramString2, XMLADatabaseInfo paramXMLADatabaseInfo, String paramString3, XMLAClient paramXMLAClient, XMLAConnection paramXMLAConnection)
  {
    this.name = paramString1;
    this.id = paramString2;
    this.database = paramXMLADatabaseInfo;
    this.dimensionMap = new LinkedHashMap();
    this.variables = new XMLAVariableInfo[0];
    this.connectionName = paramString3;
    this.xmlaClient = paramXMLAClient;
    this.xmlaConnection = paramXMLAConnection;
  }

  public BigInteger getCellCount()
  {
    return this.cellCount;
  }

  public DatabaseInfo getDatabase()
  {
    return this.database;
  }

  public int getDimensionCount()
  {
    return this.dimensionCount;
  }

  public String[] getDimensions()
  {
    if (this.dimensions == null)
    {
      XMLADimensionRequestor localXMLADimensionRequestor = new XMLADimensionRequestor(this, this.xmlaConnection);
      localXMLADimensionRequestor.setCatalogNameRestriction(getDatabase().getId());
      localXMLADimensionRequestor.setCubeNameRestriction(getId());
      XMLADimensionInfo[] arrayOfXMLADimensionInfo = localXMLADimensionRequestor.requestDimensions(this.xmlaClient);
      this.dimensions = new String[arrayOfXMLADimensionInfo.length];
      for (int i = 0; i < arrayOfXMLADimensionInfo.length; ++i)
        this.dimensions[i] = arrayOfXMLADimensionInfo[i].getId();
    }
    return this.dimensions;
  }

  public BigInteger getFilledCellCount()
  {
    return this.filledCellCount;
  }

  public final void setDimensionCount(int paramInt)
  {
    this.dimensionCount = paramInt;
  }

  public final void setDimensions(String[] paramArrayOfString)
  {
    this.dimensions = paramArrayOfString;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String paramString)
  {
    this.name = paramString;
  }

  public int getStatus()
  {
    return 1;
  }

  public int getToken()
  {
    return 0;
  }

  public String getId()
  {
    return this.id;
  }

  public void setId(String paramString)
  {
    this.id = paramString;
  }

  public int getType()
  {
    return 0;
  }

  public final void setCellCount(BigInteger paramBigInteger)
  {
    this.cellCount = paramBigInteger;
  }

  public final void setFilledCellCount(BigInteger paramBigInteger)
  {
    this.filledCellCount = paramBigInteger;
  }

  public String toString()
  {
    return "Cube " + getName() + " [" + getId() + "]: " + getDatabase().getName() + ", DimensionCount: " + getDimensionCount() + ", Cells: " + getCellCount();
  }

  public void addDimensionInternal(XMLADimensionInfo paramXMLADimensionInfo)
  {
    this.dimensionMap.put(paramXMLADimensionInfo.getName(), paramXMLADimensionInfo);
  }

  public XMLADimensionInfo getDimensionInternal(String paramString)
  {
    return (XMLADimensionInfo)this.dimensionMap.get(paramString);
  }

  public XMLADimensionInfo[] getDimensionsInternal()
  {
    return (XMLADimensionInfo[])(XMLADimensionInfo[])this.dimensionMap.values().toArray(new XMLADimensionInfo[0]);
  }

  public void setVariables(XMLAVariableInfo[] paramArrayOfXMLAVariableInfo)
  {
    this.variables = paramArrayOfXMLAVariableInfo;
  }

  private XMLAVariableInfo[] getVariables()
  {
    return this.variables;
  }

  public String[] getAllKnownPropertyIds(DbConnection paramDbConnection)
  {
    return new String[] { "SAP_VARIABLE_DEF" };
  }

  private void addProp(String paramString1, String paramString2, PropertyInfo paramPropertyInfo)
  {
    PropertyInfoImpl localPropertyInfoImpl = new PropertyInfoImpl(paramString1, paramString2, paramPropertyInfo, 2, true);
    paramPropertyInfo.addChild(localPropertyInfoImpl);
  }

  private void addElements(PropertyInfo paramPropertyInfo, XMLAElementInfo[] paramArrayOfXMLAElementInfo)
  {
    if (paramArrayOfXMLAElementInfo == null)
      return;
    for (XMLAElementInfo localXMLAElementInfo : paramArrayOfXMLAElementInfo)
    {
      PropertyInfoImpl localPropertyInfoImpl = new PropertyInfoImpl(localXMLAElementInfo.getId(), localXMLAElementInfo.getName(), paramPropertyInfo, 2, true);
      addElements(localPropertyInfoImpl, localXMLAElementInfo.getChildrenInternal());
      paramPropertyInfo.addChild(localPropertyInfoImpl);
    }
  }

  private void addElementTree(XMLAElementInfo[] paramArrayOfXMLAElementInfo, PropertyInfo paramPropertyInfo)
  {
    PropertyInfoImpl localPropertyInfoImpl1 = new PropertyInfoImpl("ELEMENTS", "True", paramPropertyInfo, 3, true);
    paramPropertyInfo.addChild(localPropertyInfoImpl1);
    for (XMLAElementInfo localXMLAElementInfo : paramArrayOfXMLAElementInfo)
    {
      if (localXMLAElementInfo.getParentCount() != 0)
        continue;
      PropertyInfoImpl localPropertyInfoImpl2 = new PropertyInfoImpl(localXMLAElementInfo.getId(), localXMLAElementInfo.getName(), localPropertyInfoImpl1, 2, true);
      addElements(localPropertyInfoImpl2, localXMLAElementInfo.getChildrenInternal());
      localPropertyInfoImpl1.addChild(localPropertyInfoImpl2);
    }
  }

  private void addDefaultSelection(XMLAVariableInfo paramXMLAVariableInfo, PropertyInfo paramPropertyInfo, XMLAElementInfo[] paramArrayOfXMLAElementInfo)
  {
    String str = "";
    switch (paramXMLAVariableInfo.getSelectionType())
    {
    case 1:
    case 3:
      if ((paramArrayOfXMLAElementInfo != null) && (paramArrayOfXMLAElementInfo.length > 0))
        str = paramArrayOfXMLAElementInfo[0].getId();
      break;
    case 2:
      if ((paramArrayOfXMLAElementInfo != null) && (paramArrayOfXMLAElementInfo.length > 0))
      {
        str = paramArrayOfXMLAElementInfo[0].getId();
        if (paramArrayOfXMLAElementInfo.length > 1)
          str = str + "\n" + paramArrayOfXMLAElementInfo[1].getId();
        else
          str = str + "\n" + paramArrayOfXMLAElementInfo[0].getId();
      }
    }
    PropertyInfoImpl localPropertyInfoImpl = new PropertyInfoImpl("SELECTEDVALUES", str, paramPropertyInfo, 2, false);
    paramPropertyInfo.addChild(localPropertyInfoImpl);
  }

  public PropertyInfo getProperty(DbConnection paramDbConnection, String paramString)
  {
    if (paramString.equals("SAP_VARIABLE_DEF"))
    {
      PropertyInfoImpl localPropertyInfoImpl1 = new PropertyInfoImpl(paramString, "True", null, 3, true);
      for (XMLAVariableInfo localXMLAVariableInfo : getVariables())
      {
        PropertyInfoImpl localPropertyInfoImpl2 = new PropertyInfoImpl("SAP_VAR", "True", localPropertyInfoImpl1, 3, true);
        localPropertyInfoImpl1.addChild(localPropertyInfoImpl2);
        addProp("ID", localXMLAVariableInfo.getId(), localPropertyInfoImpl2);
        addProp("NAME", localXMLAVariableInfo.getName(), localPropertyInfoImpl2);
        addProp("UID", localXMLAVariableInfo.getUId(), localPropertyInfoImpl2);
        addProp("ORDINAL", localXMLAVariableInfo.getOrdinal(), localPropertyInfoImpl2);
        addProp("TYPE", "" + localXMLAVariableInfo.getType(), localPropertyInfoImpl2);
        addProp("DATATYPE", localXMLAVariableInfo.getDataType(), localPropertyInfoImpl2);
        addProp("CHARMAXLENGTH", localXMLAVariableInfo.getCharacterMaximumLength(), localPropertyInfoImpl2);
        addProp("PROCESSINGTYPE", "" + localXMLAVariableInfo.getCharacterProcessingType(), localPropertyInfoImpl2);
        addProp("SELECTIONTYPE", "" + localXMLAVariableInfo.getSelectionType(), localPropertyInfoImpl2);
        addProp("ENTRYTYPE", "" + localXMLAVariableInfo.getInputType(), localPropertyInfoImpl2);
        addProp("REFERENCEDIMENSION", localXMLAVariableInfo.getReferenceDimension(), localPropertyInfoImpl2);
        addProp("REFERENCEHIERARCHY", localXMLAVariableInfo.getReferenceHierarchy(), localPropertyInfoImpl2);
        addProp("DEFAULTLOW", localXMLAVariableInfo.getDefaultLow(), localPropertyInfoImpl2);
        addProp("DEFAULTLOWCAP", localXMLAVariableInfo.getDefaultLowCap(), localPropertyInfoImpl2);
        addProp("DEFAULTHIGH", localXMLAVariableInfo.getDefaultHigh(), localPropertyInfoImpl2);
        addProp("DEFAULTHIGHCAP", localXMLAVariableInfo.getDefaultHighCap(), localPropertyInfoImpl2);
        addProp("DESCRIPTION", localXMLAVariableInfo.getDescription(), localPropertyInfoImpl2);
        localXMLAVariableInfo.loadVariableElements(this.xmlaClient, (XMLAConnection)paramDbConnection, this.database);
        XMLADimensionInfo localXMLADimensionInfo = (XMLADimensionInfo)localXMLAVariableInfo.getElementDimension();
        if (localXMLADimensionInfo != null)
        {
          XMLAElementInfo[] arrayOfXMLAElementInfo = localXMLADimensionInfo.getMembersInternal();
          addElementTree(arrayOfXMLAElementInfo, localPropertyInfoImpl2);
          addDefaultSelection(localXMLAVariableInfo, localPropertyInfoImpl2, arrayOfXMLAElementInfo);
        }
        else
        {
          addDefaultSelection(localXMLAVariableInfo, localPropertyInfoImpl2, null);
        }
      }
      return localPropertyInfoImpl1;
    }
    return null;
  }

  public boolean canBeModified()
  {
    return true;
  }

  public boolean canCreateChildren()
  {
    return true;
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLACubeInfo
 * JD-Core Version:    0.5.4
 */