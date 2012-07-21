package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.DatabaseInfoBuilder;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.loader.DatabaseLoader;
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

public class XMLADatabaseLoader extends DatabaseLoader {
  private Set<String> databaseIds = null;

  private final XMLAClient xmlaClient;

  public XMLADatabaseLoader(XMLAConnection paramXMLAConnection,
    XMLAClient paramXMLAClient) {
    super(paramXMLAConnection);
    this.xmlaClient = paramXMLAClient;
  }

  public int getDatabaseCount() {
    if (this.databaseIds == null)
      loadAllDatabaseIds();
    return this.databaseIds.size();
  }

  public final String[] getAllDatabaseIds() {
    if (this.databaseIds == null)
      loadAllDatabaseIds();
    return (String[]) this.databaseIds.toArray(new String[0]);
  }

  public DatabaseInfo loadByName(String paramString) {
    DatabaseInfo localDatabaseInfo = findDatabase(paramString);
    if (localDatabaseInfo == null)
      return loadDatabase(paramString);
    return localDatabaseInfo;
  }

  protected void reload() {
    System.out.println("XMLA Database Loader::reload");
  }

  private final DatabaseInfo findDatabase(String paramString) {
    Collection localCollection = getLoaded();
    Iterator localIterator = localCollection.iterator();
    while (localIterator.hasNext()) {
      PaloInfo localPaloInfo = (PaloInfo) localIterator.next();
      if (localPaloInfo instanceof DatabaseInfo) {
        DatabaseInfo localDatabaseInfo = (DatabaseInfo) localPaloInfo;
        if (localDatabaseInfo.getId().equals(paramString))
          return localDatabaseInfo;
      }
    }
    return null;
  }

  private final void loadAllDatabaseIds() {
    this.databaseIds = new LinkedHashSet();
    String str1 = this.xmlaClient.getConnections()[0].getName();
    try {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(str1);
      Document localDocument = this.xmlaClient.getCatalogList(localXMLARestrictions,
        localXMLAProperties);
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return;
      for (int i = 0; i < localNodeList1.getLength(); ++i) {
        NodeList localNodeList2 = localNodeList1.item(i).getChildNodes();
        for (int j = 0; j < localNodeList2.getLength(); ++j) {
          if ((localNodeList2.item(j).getNodeType() != 1)
            || (!localNodeList2.item(j).getNodeName().equals("CATALOG_NAME")))
            continue;
          String str2 = XMLAClient.getTextFromDOMElement(localNodeList2.item(j));
          this.databaseIds.add(str2);
        }
      }
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    }
  }

  private final XMLADatabaseInfo loadDatabase(String paramString) {
    XMLADatabaseInfo localXMLADatabaseInfo = BuilderRegistry.getInstance()
      .getDatabaseInfoBuilder().getDatabaseInfo(
        (XMLAConnection) this.paloConnection, this.xmlaClient, paramString);
    this.loadedInfo.put(paramString, localXMLADatabaseInfo);
    return localXMLADatabaseInfo;
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.loader.XMLADatabaseLoader JD-Core
 * Version: 0.5.4
 */