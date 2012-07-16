package com.tensegrity.palo.xmla.builders;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.parsers.XMLADatabaseRequestor;
import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatabaseInfoBuilder
{
  private XMLAClient xmlaClient;
  private String connectionName;

  public XMLADatabaseInfo getDatabaseInfo(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString)
  {
    if (paramString == null)
      return null;
    XMLADatabaseRequestor localXMLADatabaseRequestor = new XMLADatabaseRequestor(paramXMLAConnection);
    localXMLADatabaseRequestor.setCatalogNameRestriction(paramString);
    if (!localXMLADatabaseRequestor.requestDatabases(paramXMLAClient))
      return null;
    return localXMLADatabaseRequestor.getDatabaseInfos()[0];
  }

  private int requestCubes(String paramString)
  {
    int i = 0;
    try
    {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setFormat("Tabular");
      localXMLAProperties.setContent("SchemaData");
      localXMLAProperties.setCatalog(paramString);
      localXMLARestrictions.setCatalog(paramString);
      Document localDocument = this.xmlaClient.getCubeList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return 0;
      for (int j = 0; j < localNodeList1.getLength(); ++j)
      {
        NodeList localNodeList2 = localNodeList1.item(j).getChildNodes();
        for (int k = 0; k < localNodeList2.getLength(); ++k)
        {
          if ((localNodeList2.item(k).getNodeType() != 1) || (!localNodeList2.item(k).getNodeName().equals("CUBE_NAME")))
            continue;
          ++i;
        }
      }
      return i;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return 0;
  }

  private int requestDimensions(String paramString)
  {
    int i = 0;
    try
    {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setCatalog(paramString);
      localXMLARestrictions.setCatalog(paramString);
      Document localDocument = this.xmlaClient.getDimensionList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return 0;
      int j = 0;
      int k = localNodeList1.getLength();
      while (j < k)
      {
        NodeList localNodeList2 = localNodeList1.item(j).getChildNodes();
        for (int l = 0; l < localNodeList2.getLength(); ++l)
        {
          if ((localNodeList2.item(l).getNodeType() != 1) || (!localNodeList2.item(l).getNodeName().equals("DIMENSION_UNIQUE_NAME")))
            continue;
          ++i;
        }
        ++j;
      }
      return i;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return 0;
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.builders.DatabaseInfoBuilder
 * JD-Core Version:    0.5.4
 */