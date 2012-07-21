package com.tensegrity.palo.xmla.builders;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAElementInfo;
import com.tensegrity.palo.xmla.XMLAHierarchyInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import com.tensegrity.palo.xmla.XMLAVariableInfo;
import com.tensegrity.palojava.loader.ElementLoader;
import java.io.IOException;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VariableInfoBuilder {
  private XMLAClient xmlaClient;

  private XMLADatabaseInfo xmlaDatabase;

  private String connectionName;

  public XMLAVariableInfo[] requestVariables(XMLAClient paramXMLAClient,
    XMLADatabaseInfo paramXMLADatabaseInfo, String paramString) {
    try {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      this.xmlaClient = paramXMLAClient;
      this.connectionName = paramXMLAClient.getConnections()[0].getName();
      this.xmlaDatabase = paramXMLADatabaseInfo;
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setCatalog(this.xmlaDatabase.getId());
      localXMLARestrictions.setCatalog(this.xmlaDatabase.getId());
      localXMLARestrictions.setCubeName(paramString);
      Document localDocument = this.xmlaClient.getSAPVariableList(
        localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      return storeVariables(localNodeList);
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    }
    return new XMLAVariableInfo[0];
  }

  private XMLAVariableInfo[] storeVariables(NodeList paramNodeList) {
    if ((paramNodeList == null) || (paramNodeList.getLength() == 0))
      return new XMLAVariableInfo[0];
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    int j = paramNodeList.getLength();
    while (i < j) {
      XMLAVariableInfo localXMLAVariableInfo = new XMLAVariableInfo();
      NodeList localNodeList = paramNodeList.item(i).getChildNodes();
      for (int k = 0; k < localNodeList.getLength(); ++k) {
        if (localNodeList.item(k).getNodeType() != 1)
          continue;
        String str1 = localNodeList.item(k).getNodeName();
        String str2 = XMLAClient.getTextFromDOMElement(localNodeList.item(k));
        if (str1.equals("VARIABLE_NAME")) {
          localXMLAVariableInfo.setId(str2);
        } else if (str1.equals("VARIABLE_CAPTION")) {
          localXMLAVariableInfo.setName(str2);
        } else if (str1.equals("VARIABLE_UID")) {
          localXMLAVariableInfo.setUId(str2);
        } else if (str1.equals("VARIABLE_ORDINAL")) {
          localXMLAVariableInfo.setOrdinal(str2);
        } else if (str1.equals("VARIABLE_TYPE")) {
          try {
            localXMLAVariableInfo.setType(Integer.parseInt(str2));
          } catch (NumberFormatException localNumberFormatException1) {
            localXMLAVariableInfo.setType(0);
          }
        } else if (str1.equals("DATA_TYPE")) {
          localXMLAVariableInfo.setDataType(str2);
        } else if (str1.equals("CHARACTER_MAXIMUM_LENGTH")) {
          localXMLAVariableInfo.setCharacterMaximumLength(str2);
        } else if (str1.equals("VARIABLE_PROCESSING_TYPE")) {
          try {
            localXMLAVariableInfo.setVariableProcessingType(Integer.parseInt(str2));
          } catch (NumberFormatException localNumberFormatException2) {
            localXMLAVariableInfo.setVariableProcessingType(0);
          }
        } else if (str1.equals("VARIABLE_SELECTION_TYPE")) {
          try {
            localXMLAVariableInfo.setSelectionType(Integer.parseInt(str2));
          } catch (NumberFormatException localNumberFormatException3) {
            localXMLAVariableInfo.setSelectionType(0);
          }
        } else if (str1.equals("VARIABLE_ENTRY_TYPE")) {
          try {
            localXMLAVariableInfo.setInputType(Integer.parseInt(str2));
          } catch (NumberFormatException localNumberFormatException4) {
            localXMLAVariableInfo.setInputType(3);
          }
        } else if (str1.equals("REFERENCE_DIMENSION")) {
          localXMLAVariableInfo.setReferenceDimension(str2);
        } else if (str1.equals("REFERENCE_HIERARCHY")) {
          localXMLAVariableInfo.setReferenceHierarchy(str2);
        } else if (str1.equals("DEFAULT_LOW")) {
          localXMLAVariableInfo.setDefaultLow(str2);
        } else if (str1.equals("DEFAULT_LOW_CAP")) {
          localXMLAVariableInfo.setDefaultLowCap(str2);
        } else if (str1.equals("DEFAULT_HIGH")) {
          localXMLAVariableInfo.setDefaultHigh(str2);
        } else if (str1.equals("DEFAULT_HIGH_CAP")) {
          localXMLAVariableInfo.setDefaultHighCap(str2);
        } else {
          if (!str1.equals("DESCRIPTION"))
            continue;
          localXMLAVariableInfo.setDescription(str2);
        }
      }
      localXMLAVariableInfo.setHideConsolidations(localXMLAVariableInfo
        .getReferenceDimension().equals(
          localXMLAVariableInfo.getReferenceHierarchy()));
      localArrayList.add(localXMLAVariableInfo);
      ++i;
    }
    return (XMLAVariableInfo[]) localArrayList.toArray(new XMLAVariableInfo[0]);
  }

  public void requestVarElements(XMLAVariableInfo paramXMLAVariableInfo,
    XMLAConnection paramXMLAConnection, XMLADatabaseInfo paramXMLADatabaseInfo,
    XMLAClient paramXMLAClient) {
    try {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      this.xmlaDatabase = paramXMLADatabaseInfo;
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setCatalog(this.xmlaDatabase.getId());
      localXMLARestrictions.setCatalog(this.xmlaDatabase.getId());
      localXMLARestrictions.setHierarchyUniqueName(paramXMLAVariableInfo
        .getReferenceHierarchy());
      XMLAClient.printStackTrace(Thread.currentThread().getStackTrace(), System.err);
      Document localDocument = this.xmlaClient.getMemberList(localXMLARestrictions,
        localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      this.xmlaClient = paramXMLAClient;
      buildStructure(localNodeList, paramXMLAVariableInfo, paramXMLAConnection);
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    }
  }

  private void buildStructure(NodeList paramNodeList,
    XMLAVariableInfo paramXMLAVariableInfo, XMLAConnection paramXMLAConnection) {
    if ((paramNodeList == null) || (paramNodeList.getLength() == 0)) {
      paramXMLAVariableInfo.setElementDimension(new XMLADimensionInfo(
        this.xmlaClient, "VariableDimension", "varDimensionID", this.xmlaDatabase,
        null, paramXMLAConnection));
      return;
    }
    XMLADimensionInfo localXMLADimensionInfo = new XMLADimensionInfo(
      this.xmlaClient, "VariableDimension", "varDimensionID", this.xmlaDatabase,
      null, paramXMLAConnection);
    XMLAHierarchyInfo localXMLAHierarchyInfo = new XMLAHierarchyInfo(
      localXMLADimensionInfo, "VariableDimension", "varDimensionID");
    localXMLADimensionInfo.clearMembersInternal();
    ElementLoader localElementLoader = paramXMLAConnection
      .getElementLoader(localXMLADimensionInfo);
    Object localObject;
    int j;
    for (int i = 0; i < paramNodeList.getLength(); ++i) {
      localObject = paramNodeList.item(i).getChildNodes();
      XMLAElementInfo localXMLAElementInfo1 = null;
      String str1 = "";
      String str2 = "";
      String str3 = "";
      for (j = 0; j < ((NodeList) localObject).getLength(); ++j) {
        if (((NodeList) localObject).item(j).getNodeType() != 1)
          continue;
        Node localNode = ((NodeList) localObject).item(j);
        String str4 = localNode.getNodeName();
        if (str4.equals("MEMBER_NAME")) {
          str1 = XMLAClient.getTextFromDOMElement(localNode);
          str3 = str1;
        } else if (str4.equals("MEMBER_UNIQUE_NAME")) {
          str2 = XMLAClient.getTextFromDOMElement(localNode);
          str3 = str2;
          localXMLAElementInfo1 = localXMLADimensionInfo.getMemberInternal(str2);
          if (localXMLAElementInfo1 != null)
            continue;
          localXMLAElementInfo1 = new XMLAElementInfo(localXMLAHierarchyInfo,
            localXMLADimensionInfo, this.xmlaClient, paramXMLAConnection);
          if (str1.length() != 0)
            localXMLAElementInfo1.setName(str1);
          else
            localXMLAElementInfo1.setName(str2);
          localXMLAElementInfo1.setUniqueName(str2);
          localXMLAElementInfo1.setId(str2);
          localXMLAElementInfo1.setPosition(localXMLADimensionInfo
            .getMemberCountInternal());
          localXMLADimensionInfo.addMemberInternal(localXMLAElementInfo1);
        } else {
          String str5;
          if (str4.equals("MEMBER_TYPE")) {
            str5 = XMLAClient
              .getTextFromDOMElement(((NodeList) localObject).item(j));
            try {
              int l = Integer.parseInt(str5);
              if ((l == 1) || (l == 3))
                localXMLAElementInfo1.setType(1);
              else
                localXMLAElementInfo1.setType(2);
            } catch (NumberFormatException localNumberFormatException) {
              localXMLAElementInfo1.setType(2);
            }
          } else if (str4.equals("PARENT_UNIQUE_NAME")) {
            str5 = XMLAClient.getTextFromDOMElement(localNode);
            XMLAElementInfo localXMLAElementInfo2 = localXMLADimensionInfo
              .getMemberInternal(str5);
            if (localXMLAElementInfo2 == null) {
              localXMLAElementInfo2 = new XMLAElementInfo(localXMLAHierarchyInfo,
                localXMLADimensionInfo, this.xmlaClient, paramXMLAConnection);
              localXMLAElementInfo2.setUniqueName(str5);
              localXMLAElementInfo2.setId(str5);
            }
            localXMLAElementInfo2.addChildInternal(localXMLAElementInfo1);
            localXMLAElementInfo2.addChild(localXMLAElementInfo1);
            localXMLADimensionInfo.addMemberInternal(localXMLAElementInfo2);
            localXMLAElementInfo1
              .setParentInternal(new XMLAElementInfo[] { localXMLAElementInfo2 });
            localXMLAElementInfo1.setParents(new String[] { localXMLAElementInfo2
              .getId() });
          } else {
            if (!((NodeList) localObject).item(j).getNodeName().equals(
              "MEMBER_CAPTION"))
              continue;
            str5 = XMLAClient
              .getTextFromDOMElement(((NodeList) localObject).item(j));
            localXMLAElementInfo1.setName(str5);
          }
        }
      }
      if ((localXMLADimensionInfo == null) || (localXMLAElementInfo1 == null))
        continue;
      if (str2.length() != 0) {
        localXMLAElementInfo1.setUniqueName(str2);
        localXMLAElementInfo1.setId(str2);
      }
      localXMLADimensionInfo.addMemberInternal(localXMLAElementInfo1);
    }
    XMLAElementInfo[] arrayOfXMLAElementInfo;
    if (paramXMLAVariableInfo.getHideConsolidations()) {
      arrayOfXMLAElementInfo = localXMLADimensionInfo.getMembersInternal();
      localXMLADimensionInfo.clearMembersInternal();
      for (XMLAElementInfo str4 : arrayOfXMLAElementInfo) {
        if (str4.getChildrenInternal().length == 0) {
          localXMLADimensionInfo.addMemberInternal(str4);
          str4.setParents(null);
          str4.setParentCount(0);
        }
        str4.clearChildren();
      }
    } else {
      arrayOfXMLAElementInfo = localXMLADimensionInfo.getMembersInternal();
      for (XMLAElementInfo str4 : arrayOfXMLAElementInfo) {
        str4.clearChildren();
        str4.setChildren(str4.getChildrenInternal());
        localElementLoader.loaded(str4);
      }
    }
    paramXMLAVariableInfo.setElementDimension(localXMLADimensionInfo);
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.builders.VariableInfoBuilder JD-Core
 * Version: 0.5.4
 */