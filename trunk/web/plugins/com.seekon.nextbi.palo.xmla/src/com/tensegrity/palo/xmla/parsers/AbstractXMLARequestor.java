package com.tensegrity.palo.xmla.parsers;

import com.tensegrity.palo.xmla.XMLAClient;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractXMLARequestor {
  private final LinkedHashSet<String> activatedItems = new LinkedHashSet();

  public void deactivateItem(String paramString) {
    this.activatedItems.remove(paramString);
  }

  public void activateItem(String paramString) {
    this.activatedItems.add(paramString);
  }

  public boolean isItemActive(String paramString) {
    return this.activatedItems.contains(paramString);
  }

  protected void parseXMLANodeList(NodeList paramNodeList, String paramString,
    XMLAClient paramXMLAClient) {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    int i = 0;
    int j = paramNodeList.getLength();
    while (i < j) {
      NodeList localNodeList = paramNodeList.item(i).getChildNodes();
      for (int k = 0; k < localNodeList.getLength(); ++k) {
        Node localNode = localNodeList.item(k);
        if (localNode.getNodeType() != 1)
          continue;
        Iterator localIterator = this.activatedItems.iterator();
        while (localIterator.hasNext()) {
          String str1 = (String) localIterator.next();
          if (localNode.getNodeName().equals(str1)) {
            String str2 = XMLAClient.getTextFromDOMElement(localNode);
            if (str2 == null)
              continue;
            if (str2.length() == 0)
              continue;
            localLinkedHashMap
              .put(str1, XMLAClient.getTextFromDOMElement(localNode));
          }
        }
      }
      parseResult(localLinkedHashMap, paramString, paramXMLAClient);
      localLinkedHashMap.clear();
      ++i;
    }
  }

  protected abstract void parseResult(HashMap<String, String> paramHashMap,
    String paramString, XMLAClient paramXMLAClient);
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.parsers.AbstractXMLARequestor
 * JD-Core Version: 0.5.4
 */