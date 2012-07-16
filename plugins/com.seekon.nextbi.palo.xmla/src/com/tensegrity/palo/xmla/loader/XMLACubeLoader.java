package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import com.tensegrity.palo.xmla.XMLAVariableInfo;
import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.CubeInfoBuilder;
import com.tensegrity.palo.xmla.builders.VariableInfoBuilder;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.PropertyInfo;
import com.tensegrity.palojava.loader.CubeLoader;
import com.tensegrity.palojava.loader.PropertyLoader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLACubeLoader extends CubeLoader {
  private Set<String> cubeIds = null;

  private final XMLAClient xmlaClient;

  private final XMLAConnection xmlaConnection;

  public XMLACubeLoader(DbConnection paramDbConnection, XMLAClient paramXMLAClient,
    DatabaseInfo paramDatabaseInfo, XMLAConnection paramXMLAConnection) {
    super(paramDbConnection, paramDatabaseInfo);
    this.xmlaClient = paramXMLAClient;
    this.xmlaConnection = paramXMLAConnection;
  }

  public String[] getAllCubeIds() {
    if (this.cubeIds == null)
      loadAllCubeIds();
    return (String[]) this.cubeIds.toArray(new String[0]);
  }

  public String[] getCubeIds(DimensionInfo paramDimensionInfo) {
    CubeInfo[] arrayOfCubeInfo1 = this.xmlaConnection.getCubes(paramDimensionInfo);
    String[] arrayOfString = new String[arrayOfCubeInfo1.length];
    int i = 0;
    for (CubeInfo localCubeInfo : arrayOfCubeInfo1)
      arrayOfString[(i++)] = localCubeInfo.getId();
    return arrayOfString;
  }

  public CubeInfo loadByName(String paramString) {
    CubeInfo localCubeInfo = findCube(paramString);
    if (localCubeInfo == null)
      return loadCube(paramString);
    return localCubeInfo;
  }

  protected final void reload() {
    System.out.println("XMLACubeLoader::reload.");
  }

  private final CubeInfo findCube(String paramString) {
    Collection localCollection = getLoaded();
    Iterator localIterator = localCollection.iterator();
    while (localIterator.hasNext()) {
      PaloInfo localPaloInfo = (PaloInfo) localIterator.next();
      if (localPaloInfo instanceof CubeInfo) {
        CubeInfo localCubeInfo = (CubeInfo) localPaloInfo;
        if (localCubeInfo.getId().equals(paramString))
          return localCubeInfo;
      }
    }
    return null;
  }

  private final void loadAllCubeIds() {
    this.cubeIds = new LinkedHashSet();
    String str1 = this.xmlaClient.getConnections()[0].getName();
    try {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(str1);
      localXMLAProperties.setFormat("Tabular");
      localXMLAProperties.setContent("SchemaData");
      localXMLAProperties.setCatalog(this.database.getId());
      localXMLARestrictions.setCatalog(this.database.getId());
      Document localDocument = this.xmlaClient.getCubeList(localXMLARestrictions,
        localXMLAProperties);
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return;
      for (int i = 0; i < localNodeList1.getLength(); ++i) {
        NodeList localNodeList2 = localNodeList1.item(i).getChildNodes();
        for (int j = 0; j < localNodeList2.getLength(); ++j) {
          if ((localNodeList2.item(j).getNodeType() != 1)
            || (!localNodeList2.item(j).getNodeName().equals("CUBE_NAME")))
            continue;
          String str2 = XMLAClient.getTextFromDOMElement(localNodeList2.item(j));
          if (XMLAClient.IGNORE_VARIABLE_CUBES) {
            PropertyInfo localPropertyInfo = this.xmlaConnection.getPropertyLoader()
              .load("SAP_VARIABLES");
            if ((localPropertyInfo != null)
              && (Boolean.parseBoolean(localPropertyInfo.getValue()))) {
              XMLAVariableInfo[] arrayOfXMLAVariableInfo = BuilderRegistry
                .getInstance().getVariableInfoBuilder().requestVariables(
                  this.xmlaClient, (XMLADatabaseInfo) this.database, str2);
              if ((arrayOfXMLAVariableInfo != null)
                && (arrayOfXMLAVariableInfo.length > 0))
                continue;
            }
          }
          this.cubeIds.add(str2);
        }
      }
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    }
  }

  private final XMLACubeInfo loadCube(String paramString) {
    XMLACubeInfo localXMLACubeInfo = BuilderRegistry.getInstance()
      .getCubeInfoBuilder().getCubeInfo(this.xmlaClient,
        (XMLADatabaseInfo) this.database, paramString, this.xmlaConnection);
    this.loadedInfo.put(paramString, localXMLACubeInfo);
    return localXMLACubeInfo;
  }

  public String[] getCubeIds(int paramInt) {
    return getAllCubeIds();
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.loader.XMLACubeLoader JD-Core
 * Version: 0.5.4
 */