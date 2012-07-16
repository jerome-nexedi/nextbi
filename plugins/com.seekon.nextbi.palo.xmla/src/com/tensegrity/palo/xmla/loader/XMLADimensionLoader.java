package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.parsers.XMLADimensionRequestor;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.loader.CubeLoader;
import com.tensegrity.palojava.loader.DimensionLoader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class XMLADimensionLoader extends DimensionLoader {
  public static final String DIMENSION_ID_SEP = "|.#.|";

  private Map<String, String[]> dimensionIds = null;

  private Set<String> allIds = null;

  private final XMLAClient xmlaClient;

  public XMLADimensionLoader(DbConnection paramDbConnection,
    XMLAClient paramXMLAClient, DatabaseInfo paramDatabaseInfo) {
    super(paramDbConnection, paramDatabaseInfo);
    this.xmlaClient = paramXMLAClient;
  }

  public String[] getAllDimensionIds() {
    if (this.allIds == null) {
      this.allIds = new LinkedHashSet();
      if (this.dimensionIds == null)
        this.dimensionIds = new LinkedHashMap();
      String[] arrayOfString1 = ((XMLAConnection) this.paloConnection)
        .getCubeLoader(this.database).getAllCubeIds();
      for (String str : arrayOfString1) {
        if (!this.dimensionIds.containsKey(str)) {
          XMLADimensionRequestor localXMLADimensionRequestor = new XMLADimensionRequestor(
            (XMLACubeInfo) ((XMLAConnection) this.paloConnection).getCubeLoader(
              this.database).load(str), (XMLAConnection) this.paloConnection);
          localXMLADimensionRequestor.setCatalogNameRestriction(this.database
            .getId());
          localXMLADimensionRequestor.setCubeNameRestriction(str);
          XMLADimensionInfo[] arrayOfXMLADimensionInfo1 = localXMLADimensionRequestor
            .requestDimensions(this.xmlaClient);
          ArrayList<String> localArrayList = new ArrayList();
          for (XMLADimensionInfo localXMLADimensionInfo : arrayOfXMLADimensionInfo1)
            localArrayList.add(localXMLADimensionInfo.getId());
          this.dimensionIds.put(str, localArrayList.toArray(new String[0]));
        }
        this.allIds.addAll(Arrays.asList((String[]) this.dimensionIds.get(str)));
      }
    }
    return (String[]) this.allIds.toArray(new String[0]);
  }

  public String[] getAllDimensionIdsForCube(CubeInfo paramCubeInfo) {
    if (this.dimensionIds == null)
      this.dimensionIds = new LinkedHashMap();
    if (!this.dimensionIds.containsKey(paramCubeInfo.getId())) {
      XMLADimensionRequestor localXMLADimensionRequestor = new XMLADimensionRequestor(
        (XMLACubeInfo) paramCubeInfo, (XMLAConnection) this.paloConnection);
      localXMLADimensionRequestor.setCatalogNameRestriction(this.database.getId());
      localXMLADimensionRequestor.setCubeNameRestriction(paramCubeInfo.getId());
      XMLADimensionInfo[] arrayOfXMLADimensionInfo1 = localXMLADimensionRequestor
        .requestDimensions(this.xmlaClient);
      ArrayList<String> localArrayList = new ArrayList();
      for (XMLADimensionInfo localXMLADimensionInfo : arrayOfXMLADimensionInfo1)
        localArrayList.add(localXMLADimensionInfo.getId());
      this.dimensionIds.put(paramCubeInfo.getId(), localArrayList
        .toArray(new String[0]));
    }
    return (String[]) this.dimensionIds.get(paramCubeInfo.getId());
  }

  public DimensionInfo loadByName(String paramString) {
    DimensionInfo localDimensionInfo = findDimension(paramString);
    if (localDimensionInfo == null)
      return loadDimension(paramString);
    return localDimensionInfo;
  }

  protected final void reload() {
    System.out.println("XMLADimensionLoader::reload.");
  }

  private final DimensionInfo findDimension(String paramString) {
    Collection localCollection = getLoaded();
    Iterator localIterator = localCollection.iterator();
    while (localIterator.hasNext()) {
      PaloInfo localPaloInfo = (PaloInfo) localIterator.next();
      if (localPaloInfo instanceof DimensionInfo) {
        DimensionInfo localDimensionInfo = (DimensionInfo) localPaloInfo;
        if (localDimensionInfo.getName().equals(paramString))
          return localDimensionInfo;
      }
    }
    return null;
  }

  private final XMLADimensionInfo loadDimension(String paramString) {
    return null;
  }

  public String[] getDimensionIds(int paramInt) {
    return getAllDimensionIds();
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.loader.XMLADimensionLoader JD-Core
 * Version: 0.5.4
 */