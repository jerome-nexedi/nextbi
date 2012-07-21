package com.tensegrity.palo.xmla.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAElementInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.ElementInfo;
import com.tensegrity.palojava.RuleInfo;
import com.tensegrity.palojava.impl.RuleImpl;

public class RuleInfoBuilder {
  private XMLAClient xmlaClient;

  private String connectionName;

  private final HashMap functionLists = new HashMap();

  private final HashMap functionNamesLists = new HashMap();

  private final HashMap ruleInfoMap = new HashMap();

  public RuleInfo[] getRules(XMLAConnection paramXMLAConnection,
    XMLAClient paramXMLAClient, XMLACubeInfo paramXMLACubeInfo) {
    this.xmlaClient = paramXMLAClient;
    if (!this.ruleInfoMap.containsKey(paramXMLACubeInfo.getId())) {
      List localObject = new ArrayList();
      DimensionInfo[] arrayOfDimensionInfo = paramXMLAConnection
        .getDimensions(paramXMLACubeInfo);
      int i = 0;
      int j = arrayOfDimensionInfo.length;
      while (i < j) {
        XMLAElementInfo[] arrayOfXMLAElementInfo = paramXMLAConnection
          .getCubeElements(paramXMLACubeInfo,
            (XMLADimensionInfo) arrayOfDimensionInfo[i]);
        int k = 0;
        int l = arrayOfXMLAElementInfo.length;
        while (k < l) {
          if (arrayOfXMLAElementInfo[k].isCalculated())
            ((ArrayList) localObject).add(arrayOfXMLAElementInfo[k].getRule());
          ++k;
        }
        ++i;
      }
      this.ruleInfoMap.put(paramXMLACubeInfo.getId(), localObject);
    }
    RuleImpl[] localObject = (RuleImpl[]) (RuleImpl[]) ((ArrayList) this.ruleInfoMap
      .get(paramXMLACubeInfo.getId())).toArray(new RuleImpl[0]);
    return localObject;
  }

  public String getRule(XMLACubeInfo paramXMLACubeInfo,
    ElementInfo[] paramArrayOfElementInfo) {
    String str = "";
    int i = 0;
    int j = paramArrayOfElementInfo.length;
    while (i < j) {
      XMLAElementInfo localXMLAElementInfo = (XMLAElementInfo) paramArrayOfElementInfo[i];
      if (localXMLAElementInfo.isCalculated()) {
        str = localXMLAElementInfo.getRule().getId();
        break;
      }
      ++i;
    }
    return str;
  }

  public String getFunctions(XMLAClient paramXMLAClient) {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    if (!this.functionLists.containsKey(paramXMLAClient))
      requestFunctions();
    return (String) this.functionLists.get(this.xmlaClient);
  }

  public String getFunctionNames(XMLAClient paramXMLAClient) {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    if (!this.functionNamesLists.containsKey(paramXMLAClient))
      requestFunctions();
    return (String) this.functionNamesLists.get(this.xmlaClient);
  }

  private void requestFunctions() {
    try {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      Document localDocument = this.xmlaClient.getFunctionList(
        localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      storeFunctions(localNodeList);
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    }
  }

  private void storeFunctions(NodeList paramNodeList) {
    StringBuffer localStringBuffer1 = new StringBuffer("<functions>\n");
    StringBuffer localStringBuffer2 = new StringBuffer();
    if ((paramNodeList == null) || (paramNodeList.getLength() == 0)) {
      localStringBuffer1.append("</functions>\n");
      this.functionLists.put(this.xmlaClient, localStringBuffer1.toString());
      this.functionNamesLists.put(this.xmlaClient, localStringBuffer2.toString());
      return;
    }
    localStringBuffer1.append(Resource.getBaseFunctions());
    HashSet localHashSet = new HashSet();
    for (int i = 0; i < paramNodeList.getLength(); ++i) {
      NodeList localNodeList = paramNodeList.item(i).getChildNodes();
      String str1 = null;
      int j = 1;
      String str2 = null;
      String str3 = null;
      String str4 = null;
      String str5 = null;
      for (int k = 0; k < localNodeList.getLength(); ++k) {
        if (localNodeList.item(k).getNodeType() != 1)
          continue;
        if (localNodeList.item(k).getNodeName().equals("FUNCTION_NAME")) {
          str1 = XMLAClient.getTextFromDOMElement(localNodeList.item(k));
        } else if (localNodeList.item(k).getNodeName().equals("DESCRIPTION")) {
          str3 = XMLAClient.getTextFromDOMElement(localNodeList.item(k));
        } else if (localNodeList.item(k).getNodeName().equals("ORIGIN")) {
          j = Integer.parseInt(XMLAClient.getTextFromDOMElement(localNodeList
            .item(k)));
        } else if (localNodeList.item(k).getNodeName().equals("CAPTION")) {
          str2 = XMLAClient.getTextFromDOMElement(localNodeList.item(k));
        } else if (localNodeList.item(k).getNodeName().equals("PARAMETER_LIST")) {
          str4 = XMLAClient.getTextFromDOMElement(localNodeList.item(k));
        } else {
          if (!localNodeList.item(k).getNodeName().equals("RETURN_TYPE"))
            continue;
          str5 = XMLAClient.getTextFromDOMElement(localNodeList.item(k));
        }
      }
      if (localHashSet.contains(str1))
        continue;
      localHashSet.add(str1);
      str4 = str4.replaceAll("<", "");
      str4 = str4.replaceAll(">", "");
      localStringBuffer1.append("  <function>\n");
      localStringBuffer1.append("    <name>" + str1 + "</name>\n");
      localStringBuffer1.append("    <category>SQLServer</category>\n");
      localStringBuffer1.append("    <short-description language=\"english\">\n");
      localStringBuffer1.append("      " + str2 + "(" + str4 + ")\n");
      localStringBuffer1.append("    </short-description>\n");
      localStringBuffer1.append("    <short-description language=\"german\">\n");
      localStringBuffer1.append("      " + str2 + "(" + str4 + ")\n");
      localStringBuffer1.append("    </short-description>\n");
      localStringBuffer1.append("    <long-description language=\"english\">\n");
      localStringBuffer1.append("      " + str3 + "\n");
      localStringBuffer1.append("    </long-description>\n");
      localStringBuffer1.append("    <long-description language=\"german\">\n");
      localStringBuffer1.append("      " + str3 + "\n");
      localStringBuffer1.append("    </long-description>\n");
      localStringBuffer1.append("    <minimal-arguments>0</minimal-arguments>\n");
      localStringBuffer1.append("    <maximal-arguments>0</maximal-arguments>\n");
      localStringBuffer1.append("  </function>\n");
      if (localStringBuffer2.length() != 0)
        localStringBuffer2.append(", ");
      localStringBuffer2.append(str2);
    }
    localStringBuffer1.append("</functions>\n");
    this.functionLists.put(this.xmlaClient, localStringBuffer1.toString());
    this.functionNamesLists.put(this.xmlaClient, localStringBuffer2.toString());
  }

  private void parseParameters(String paramString) {
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.builders.RuleInfoBuilder JD-Core
 * Version: 0.5.4
 */