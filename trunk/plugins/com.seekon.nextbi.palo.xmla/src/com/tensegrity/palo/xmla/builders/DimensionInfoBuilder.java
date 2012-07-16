package com.tensegrity.palo.xmla.builders;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAHierarchyInfo;
import com.tensegrity.palo.xmla.parsers.XMLADimensionRequestor;
import com.tensegrity.palo.xmla.parsers.XMLAHierarchyRequestor;

public class DimensionInfoBuilder {
  public XMLADimensionInfo[] getDimensionInfo(XMLAConnection paramXMLAConnection,
    XMLAClient paramXMLAClient, XMLADatabaseInfo paramXMLADatabaseInfo,
    XMLACubeInfo paramXMLACubeInfo) {
    XMLADimensionRequestor localXMLADimensionRequestor = new XMLADimensionRequestor(
      paramXMLACubeInfo, paramXMLAConnection);
    localXMLADimensionRequestor.setCubeNameRestriction(paramXMLACubeInfo.getId());
    localXMLADimensionRequestor.setCatalogNameRestriction(paramXMLADatabaseInfo
      .getId());
    return localXMLADimensionRequestor.requestDimensions(paramXMLAClient);
  }

  public XMLAHierarchyInfo[] getHierarchyInfo(XMLAConnection paramXMLAConnection,
    XMLAClient paramXMLAClient, XMLADimensionInfo paramXMLADimensionInfo) {
    XMLADatabaseInfo localXMLADatabaseInfo = (XMLADatabaseInfo) paramXMLADimensionInfo
      .getDatabase();
    XMLAHierarchyRequestor localXMLAHierarchyRequestor = new XMLAHierarchyRequestor(
      paramXMLADimensionInfo, localXMLADatabaseInfo, paramXMLAConnection);
    localXMLAHierarchyRequestor.setCatalogNameRestriction(localXMLADatabaseInfo
      .getId());
    localXMLAHierarchyRequestor.setCubeNameRestriction(paramXMLADimensionInfo
      .getCubeId());
    localXMLAHierarchyRequestor
      .setDimensionUniqueNameRestriction(paramXMLADimensionInfo
        .getDimensionUniqueName());
    return localXMLAHierarchyRequestor.requestHierarchies(paramXMLAClient);
  }

  public XMLAHierarchyInfo getHierarchyInfo(XMLAConnection paramXMLAConnection,
    XMLAClient paramXMLAClient, XMLADimensionInfo paramXMLADimensionInfo,
    String paramString) {
    XMLADatabaseInfo localXMLADatabaseInfo = (XMLADatabaseInfo) paramXMLADimensionInfo
      .getDatabase();
    XMLAHierarchyRequestor localXMLAHierarchyRequestor = new XMLAHierarchyRequestor(
      paramXMLADimensionInfo, localXMLADatabaseInfo, paramXMLAConnection);
    localXMLAHierarchyRequestor.setCatalogNameRestriction(localXMLADatabaseInfo
      .getId());
    localXMLAHierarchyRequestor.setCubeNameRestriction(paramXMLADimensionInfo
      .getCubeId());
    localXMLAHierarchyRequestor
      .setDimensionUniqueNameRestriction(paramXMLADimensionInfo
        .getDimensionUniqueName());
    localXMLAHierarchyRequestor.setHierarchyUniqueNameRestriction(XMLADimensionInfo
      .transformId(XMLADimensionInfo.getDimIdFromId(paramString)));
    XMLAHierarchyInfo[] arrayOfXMLAHierarchyInfo = localXMLAHierarchyRequestor
      .requestHierarchies(paramXMLAClient);
    if ((arrayOfXMLAHierarchyInfo == null) || (arrayOfXMLAHierarchyInfo.length < 1))
      return null;
    return arrayOfXMLAHierarchyInfo[0];
  }

  public void updateMaxLevelAndDepth(XMLADimensionInfo paramXMLADimensionInfo,
    int paramInt1, int paramInt2) {
    paramXMLADimensionInfo.setMaxDepth(paramInt1);
    paramXMLADimensionInfo.setMaxLevel(paramInt2);
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.builders.DimensionInfoBuilder
 * JD-Core Version: 0.5.4
 */