package com.tensegrity.palo.xmla.builders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLACubeInfo;
import com.tensegrity.palo.xmla.XMLADatabaseInfo;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAElementInfo;
import com.tensegrity.palo.xmla.XMLAExecuteProperties;
import com.tensegrity.palo.xmla.XMLAHierarchyInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.impl.RuleImpl;

public class ElementInfoBuilder
{
  private XMLAClient xmlaClient;
  private XMLADimensionInfo dimension;
  private String cubeId;
  private String connectionName;
  private long totalTime = 0L;
  private int maxDepth;
  private int maxLevel;
  private XMLAConnection xmlaConnection;
  private XMLAHierarchyInfo hierarchy;

  public void getElementsTest(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString1, String paramString2, String paramString3)
  {
    XMLAClient.setVerbose(true);
    XMLAExecuteProperties localXMLAExecuteProperties = new XMLAExecuteProperties();
    String str1 = paramXMLAClient.getConnections()[0].getName();
    localXMLAExecuteProperties.setDataSourceInfo(str1);
    localXMLAExecuteProperties.setCatalog(paramString1);
    try
    {
      StringBuffer localStringBuffer = new StringBuffer("SELECT ");
      localStringBuffer.append(paramString3);
      localStringBuffer.append(".Levels(0) ON 0 FROM $" + paramString2);
      System.out.println("Query == " + localStringBuffer);
      Document localDocument = paramXMLAClient.execute(localStringBuffer.toString(), localXMLAExecuteProperties);
      if (localDocument == null)
      {
        System.out.println("Whoops: Document == null");
        return;
      }
      NodeList localNodeList1 = localDocument.getElementsByTagName("Tuple");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
      {
        System.out.println("Null result...");
        return;
      }
      for (int i = 0; i < localNodeList1.getLength(); ++i)
      {
        try
        {
          String str2 = localNodeList1.item(i).getParentNode().getParentNode().getAttributes().getNamedItem("name").getNodeValue();
          if (!str2.equalsIgnoreCase("axis0")){
          	System.out.println(XMLAClient.getTextFromDOMElement(localNodeList1.item(i)));
          	return;
          }
        }
        catch (Exception localException)
        {
        	System.out.println(XMLAClient.getTextFromDOMElement(localNodeList1.item(i)));
        	return;
        }
        NodeList localNodeList2 = localNodeList1.item(i).getChildNodes().item(0).getChildNodes();
        for (int j = 0; j < localNodeList2.getLength(); ++j)
        {
          if (localNodeList2.item(j).getNodeType() != 1)
            continue;
          Node localNode = localNodeList2.item(j);
          String str3 = localNode.getNodeName();
          if (!str3.equals("UName"))
            continue;
         
        }
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    XMLAClient.setVerbose(false);
  }

  public XMLAElementInfo[] getElements(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString, XMLADimensionInfo paramXMLADimensionInfo)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = paramXMLADimensionInfo;
    this.hierarchy = null;
    this.cubeId = paramString;
    this.xmlaConnection = paramXMLAConnection;
    return requestElements(null);
  }

  public XMLAElementInfo[] getElements(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString, XMLAHierarchyInfo paramXMLAHierarchyInfo)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = ((XMLADimensionInfo)paramXMLAHierarchyInfo.getDimension());
    this.hierarchy = paramXMLAHierarchyInfo;
    this.cubeId = paramString;
    this.xmlaConnection = paramXMLAConnection;
    return requestElements(null);
  }

  public XMLAElementInfo[] getElements(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString, XMLADimensionInfo paramXMLADimensionInfo, int paramInt)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = paramXMLADimensionInfo;
    this.cubeId = paramString;
    this.xmlaConnection = paramXMLAConnection;
    return requestElementsAtLevel(null, paramInt);
  }

  public XMLAElementInfo[] getElements(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString, XMLAHierarchyInfo paramXMLAHierarchyInfo, int paramInt)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = ((XMLADimensionInfo)paramXMLAHierarchyInfo.getDimension());
    this.hierarchy = paramXMLAHierarchyInfo;
    this.cubeId = paramString;
    this.xmlaConnection = paramXMLAConnection;
    return requestElementsAtLevel(null, paramInt);
  }

  public XMLAElementInfo[] getChildren(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, String paramString, XMLAElementInfo paramXMLAElementInfo)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = ((XMLADimensionInfo)paramXMLAElementInfo.getDimension());
    this.hierarchy = ((XMLAHierarchyInfo)paramXMLAElementInfo.getHierarchy());
    this.cubeId = paramString;
    this.xmlaConnection = paramXMLAConnection;
    return requestElements(paramXMLAElementInfo);
  }

  public XMLAElementInfo getElementInfo(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, XMLADimensionInfo paramXMLADimensionInfo, String paramString)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = paramXMLADimensionInfo;
    this.xmlaConnection = paramXMLAConnection;
    return requestElement(paramString);
  }

  public XMLAElementInfo getElementInfo(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, XMLAHierarchyInfo paramXMLAHierarchyInfo, String paramString)
  {
    this.xmlaClient = paramXMLAClient;
    this.connectionName = paramXMLAClient.getConnections()[0].getName();
    this.dimension = ((XMLADimensionInfo)paramXMLAHierarchyInfo.getDimension());
    this.hierarchy = paramXMLAHierarchyInfo;
    this.xmlaConnection = paramXMLAConnection;
    return requestElement(paramString);
  }

  private XMLAElementInfo requestElement(String paramString)
  {
    try
    {
      paramString = XMLADimensionInfo.transformId(paramString);
      paramString = paramString.replaceAll("&", "&amp;");
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setCatalog(this.dimension.getDatabase().getId());
      localXMLARestrictions.setCatalog(this.dimension.getDatabase().getId());
      localXMLARestrictions.setMemberUniqueName(paramString);
      localXMLARestrictions.setCubeName(this.dimension.getCubeId());
      Document localDocument = this.xmlaClient.getMemberList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return null;
      NodeList localNodeList2 = localNodeList1.item(0).getChildNodes();
      XMLAElementInfo localXMLAElementInfo1 = null;
      String str1 = "<not initialized>";
      for (int i = 0; i < localNodeList2.getLength(); ++i)
      {
        if (localNodeList2.item(i).getNodeType() != 1)
          continue;
        Node localNode = localNodeList2.item(i);
        String str2 = localNode.getNodeName();
        String str3;
        if (str2.equals("MEMBER_UNIQUE_NAME"))
        {
          str3 = XMLAClient.getTextFromDOMElement(localNode);
          localXMLAElementInfo1 = this.dimension.getMemberInternal(str3);
          if (localXMLAElementInfo1 == null)
          {
            localXMLAElementInfo1 = new XMLAElementInfo(this.hierarchy, this.dimension, this.xmlaClient, this.xmlaConnection);
            localXMLAElementInfo1.setName(str3);
            localXMLAElementInfo1.setUniqueName(str3);
            localXMLAElementInfo1.setId(str3);
            this.dimension.addMemberInternal(localXMLAElementInfo1);
          }
        }
        else if (str2.equals("MEMBER_TYPE"))
        {
          str3 = XMLAClient.getTextFromDOMElement(localNodeList2.item(i));
          try
          {
            int j = Integer.parseInt(str3);
            if ((j == 1) || (j == 3))
              localXMLAElementInfo1.setType(1);
            else
              localXMLAElementInfo1.setType(2);
          }
          catch (NumberFormatException localNumberFormatException2)
          {
            localXMLAElementInfo1.setType(2);
          }
        }
        else if (str2.equals("PARENT_UNIQUE_NAME"))
        {
          str3 = XMLAClient.getTextFromDOMElement(localNode);
          XMLAElementInfo localXMLAElementInfo2 = this.dimension.getMemberInternal(str3);
          if (localXMLAElementInfo2 == null)
          {
            localXMLAElementInfo2 = new XMLAElementInfo(this.hierarchy, this.dimension, this.xmlaClient, this.xmlaConnection);
            localXMLAElementInfo2.setUniqueName(str3);
            localXMLAElementInfo2.setId(str3);
          }
          localXMLAElementInfo2.addChildInternal(localXMLAElementInfo1);
          localXMLAElementInfo2.addChild(localXMLAElementInfo1);
          this.dimension.addMemberInternal(localXMLAElementInfo2);
          localXMLAElementInfo1.setParentInternal(new XMLAElementInfo[] { localXMLAElementInfo2 });
          localXMLAElementInfo1.setParents(new String[] { localXMLAElementInfo2.getId() });
        }
        else if (localNodeList2.item(i).getNodeName().equals("MEMBER_CAPTION"))
        {
          str3 = XMLAClient.getTextFromDOMElement(localNodeList2.item(i));
          localXMLAElementInfo1.setName(str3);
        }
        else if (localNodeList2.item(i).getNodeName().equals("MEMBER_ORDINAL"))
        {
          str1 = XMLAClient.getTextFromDOMElement(localNodeList2.item(i));
        }
        else
        {
          if (!localNodeList2.item(i).getNodeName().equals("CHILDREN_CARDINALITY"))
            continue;
          str3 = XMLAClient.getTextFromDOMElement(localNodeList2.item(i));
          int k = Integer.parseInt(str3);
          localXMLAElementInfo1.setHasChildren(k > 0);
          localXMLAElementInfo1.setEstimatedChildCount(k);
        }
      }
      try
      {
        localXMLAElementInfo1.setPosition(Integer.parseInt(str1));
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        localXMLAElementInfo1.setPosition(2147483647);
      }
      return localXMLAElementInfo1;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return null;
  }

  private XMLAElementInfo[] requestElements(XMLAElementInfo paramXMLAElementInfo)
  {
    try
    {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setCatalog(this.dimension.getDatabase().getId());
      localXMLARestrictions.setCatalog(this.dimension.getDatabase().getId());
      if (this.cubeId != null)
        localXMLARestrictions.setCubeName(this.cubeId);
      if (paramXMLAElementInfo == null)
      {
        if (this.hierarchy == null)
          localXMLARestrictions.setHierarchyUniqueName(this.dimension.getHierarchyUniqueName());
        else
          localXMLARestrictions.setHierarchyUniqueName(XMLADimensionInfo.transformId(XMLADimensionInfo.getDimIdFromId(this.hierarchy.getId())));
      }
      else
      {
        localXMLARestrictions.setTreeOp(1);
        if (this.hierarchy == null)
          localXMLARestrictions.setHierarchyUniqueName(this.dimension.getHierarchyUniqueName());
        else
          localXMLARestrictions.setHierarchyUniqueName(XMLADimensionInfo.transformId(XMLADimensionInfo.getDimIdFromId(this.hierarchy.getId())));
        String localObject = paramXMLAElementInfo.getUniqueName().replaceAll("&", "&amp;");
        localXMLARestrictions.setMemberUniqueName((String)localObject);
      }
      Object localObject = this.xmlaClient.getMemberList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = ((Document)localObject).getElementsByTagName("row");
      if ((((localNodeList == null) || (localNodeList.getLength() == 0))) && (paramXMLAElementInfo == null))
        this.dimension.setElementCount(0);
      return buildStructure(localNodeList, (XMLADatabaseInfo)this.dimension.getDatabase(), this.cubeId, this.dimension, this.hierarchy);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return new XMLAElementInfo[0];
  }

  private XMLAElementInfo[] requestElementsAtLevel(XMLAElementInfo paramXMLAElementInfo, int paramInt)
  {
    try
    {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(this.connectionName);
      localXMLAProperties.setCatalog(this.dimension.getDatabase().getId());
      localXMLARestrictions.setCatalog(this.dimension.getDatabase().getId());
      localXMLARestrictions.setLevelNumber("" + paramInt);
      if (this.cubeId != null)
        localXMLARestrictions.setCubeName(this.cubeId);
      if (this.hierarchy == null)
        localXMLARestrictions.setHierarchyUniqueName(this.dimension.getHierarchyUniqueName());
      else
        localXMLARestrictions.setHierarchyUniqueName(XMLADimensionInfo.transformId(XMLADimensionInfo.getDimIdFromId(this.hierarchy.getId())));
      Document localDocument = this.xmlaClient.getMemberList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList = localDocument.getElementsByTagName("row");
      if ((((localNodeList == null) || (localNodeList.getLength() == 0))) && (((localNodeList == null) || (localNodeList.getLength() == 0))) && (paramXMLAElementInfo == null))
        this.dimension.setElementCount(0);
      XMLAElementInfo[] arrayOfXMLAElementInfo = buildStructure(localNodeList, (XMLADatabaseInfo)this.dimension.getDatabase(), this.cubeId, this.dimension, this.hierarchy);
      return arrayOfXMLAElementInfo;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    return new XMLAElementInfo[0];
  }

  private XMLAElementInfo[] buildStructure(NodeList paramNodeList, XMLADatabaseInfo paramXMLADatabaseInfo, String paramString, XMLADimensionInfo paramXMLADimensionInfo, XMLAHierarchyInfo paramXMLAHierarchyInfo)
  {
    if ((paramNodeList == null) || (paramNodeList.getLength() == 0))
      return new XMLAElementInfo[0];
    ArrayList localArrayList = new ArrayList();
    Object localObject1;
    for (int i = 0; i < paramNodeList.getLength(); ++i)
    {
      localObject1 = paramNodeList.item(i).getChildNodes();
      XMLAElementInfo localXMLAElementInfo = null;
      String str1 = "";
      String str2 = "";
      for (int j = 0; j < ((NodeList)localObject1).getLength(); ++j)
      {
        if (((NodeList)localObject1).item(j).getNodeType() != 1)
          continue;
        Node localNode = ((NodeList)localObject1).item(j);
        String str3 = localNode.getNodeName();
        if (str3.equals("MEMBER_NAME"))
        {
          str1 = XMLAClient.getTextFromDOMElement(localNode);
        }
        else if (str3.equals("MEMBER_UNIQUE_NAME"))
        {
          str2 = XMLAClient.getTextFromDOMElement(localNode);
          localXMLAElementInfo = paramXMLADimensionInfo.getMemberInternal(str2);
          if (localXMLAElementInfo != null)
            continue;
          localXMLAElementInfo = new XMLAElementInfo(paramXMLAHierarchyInfo, this.dimension, this.xmlaClient, this.xmlaConnection);
          if (str1.length() != 0)
            localXMLAElementInfo.setName(str1);
          else
            localXMLAElementInfo.setName(str2);
          localXMLAElementInfo.setUniqueName(str2);
          localXMLAElementInfo.setId(str2);
          localXMLAElementInfo.setPosition(paramXMLADimensionInfo.getMemberCountInternal());
          paramXMLADimensionInfo.addMemberInternal(localXMLAElementInfo);
        }
        else
        {
          String str4;
          if (str3.equals("MEMBER_TYPE"))
          {
            str4 = XMLAClient.getTextFromDOMElement(((NodeList)localObject1).item(j));
            if (str2.indexOf("Avg Salary") != -1)
              System.err.println("Member Type: " + str4 + " for " + str2 + ", " + str1);
            try
            {
              int k = Integer.parseInt(str4);
              if ((k == 1) || (k == 3))
                localXMLAElementInfo.setType(1);
              else
                localXMLAElementInfo.setType(2);
            }
            catch (NumberFormatException localNumberFormatException)
            {
              localXMLAElementInfo.setType(2);
            }
          }
          else
          {
          	
            if (str3.equals("PARENT_UNIQUE_NAME"))
            {
            	XMLAElementInfo localObject2;
              str4 = XMLAClient.getTextFromDOMElement(localNode);
              localObject2 = paramXMLADimensionInfo.getMemberInternal(str4);
              if (localObject2 == null)
              {
                localObject2 = new XMLAElementInfo(paramXMLAHierarchyInfo, this.dimension, this.xmlaClient, this.xmlaConnection);
                ((XMLAElementInfo)localObject2).setUniqueName(str4);
                ((XMLAElementInfo)localObject2).setId(str4);
              }
              ((XMLAElementInfo)localObject2).addChildInternal(localXMLAElementInfo);
              ((XMLAElementInfo)localObject2).addChild(localXMLAElementInfo);
              paramXMLADimensionInfo.addMemberInternal((XMLAElementInfo)localObject2);
              localXMLAElementInfo.setParentInternal(new XMLAElementInfo[] { localObject2 });
              localXMLAElementInfo.setParents(new String[] { ((XMLAElementInfo)localObject2).getId() });
            }
            else if (((NodeList)localObject1).item(j).getNodeName().equals("MEMBER_CAPTION"))
            {
              str4 = XMLAClient.getTextFromDOMElement(((NodeList)localObject1).item(j));
              localXMLAElementInfo.setName(str4);
            }
            else if (((NodeList)localObject1).item(j).getNodeName().equals("EXPRESSION"))
            {
              str4 = XMLAClient.getTextFromDOMElement(((NodeList)localObject1).item(j));
              if (str4.trim().length() != 0)
              {
                localXMLAElementInfo.setCalculated(true);
                CubeInfo localObject2 = this.xmlaConnection.getCubeLoader(paramXMLADimensionInfo.getDatabase()).load(paramString);
                RuleImpl localRuleImpl = new RuleImpl((XMLACubeInfo)localObject2, str4.trim());
                localRuleImpl.setComment("Rule comment");
                localRuleImpl.setExternalIdentifier(str4.trim());
                localRuleImpl.setDefinition(str4.trim());
                localRuleImpl.setTimestamp(new Date().getTime());
                localXMLAElementInfo.setRule(localRuleImpl);
              }
            }
            else
            {
              if (!((NodeList)localObject1).item(j).getNodeName().equals("CHILDREN_CARDINALITY"))
                continue;
              str4 = XMLAClient.getTextFromDOMElement(((NodeList)localObject1).item(j));
              int l = Integer.parseInt(str4);
              localXMLAElementInfo.setHasChildren(l > 0);
              localXMLAElementInfo.setEstimatedChildCount(l);
            }
          }
        }
      }
      if ((paramXMLADimensionInfo != null) && (localXMLAElementInfo != null))
      {
        if (str2.length() != 0)
        {
          localXMLAElementInfo.setUniqueName(str2);
          localXMLAElementInfo.setId(str2);
        }
        paramXMLADimensionInfo.addMemberInternal(localXMLAElementInfo);
        localArrayList.add(localXMLAElementInfo);
      }
      paramXMLADimensionInfo.setElementCount(paramXMLADimensionInfo.getMemberCountInternal());
    }
    if (!this.xmlaConnection.usedByWPalo())
    {
      XMLAElementInfo[] arrayOfXMLAElementInfo = (XMLAElementInfo[])localArrayList.toArray(new XMLAElementInfo[0]);
      return traverseDimension(paramXMLADimensionInfo, arrayOfXMLAElementInfo);
    }
    return (XMLAElementInfo[])localArrayList.toArray(new XMLAElementInfo[0]);
  }

  private int setDepth(XMLADimensionInfo paramXMLADimensionInfo, XMLAElementInfo paramXMLAElementInfo, int paramInt1, LinkedHashSet paramLinkedHashSet, int paramInt2)
  {
    if ((paramXMLAElementInfo == null) || (paramXMLADimensionInfo == null))
      return paramInt2;
    if (paramInt1 > paramXMLAElementInfo.getDepth())
    {
      paramXMLAElementInfo.setDepth(paramInt1);
    }
    else
    {
      paramLinkedHashSet.add(paramXMLAElementInfo);
      return paramInt2;
    }
    if (paramInt1 > this.maxDepth)
      this.maxDepth = paramInt1;
    paramXMLAElementInfo.setPosition(paramInt2);
    paramLinkedHashSet.add(paramXMLAElementInfo);
    String[] arrayOfString = paramXMLAElementInfo.getChildren();
    int i = 0;
    int j = arrayOfString.length;
    while (i < j)
    {
      XMLAElementInfo localXMLAElementInfo = paramXMLADimensionInfo.getMemberByIdInternal(arrayOfString[i]);
      paramInt2 = setDepth(paramXMLADimensionInfo, localXMLAElementInfo, paramInt1 + 1, paramLinkedHashSet, paramInt2 + 1);
      ++i;
    }
    return paramInt2;
  }

  private void setLevel(XMLADimensionInfo paramXMLADimensionInfo, XMLAElementInfo paramXMLAElementInfo, int paramInt, LinkedHashSet paramLinkedHashSet)
  {
    if ((paramXMLAElementInfo == null) || (paramXMLADimensionInfo == null))
      return;
    if (paramInt > paramXMLAElementInfo.getLevel())
    {
      paramXMLAElementInfo.setLevel(paramInt);
    }
    else
    {
      paramLinkedHashSet.add(paramXMLAElementInfo);
      return;
    }
    paramLinkedHashSet.add(paramXMLAElementInfo);
    if (paramInt > this.maxLevel)
      this.maxLevel = paramInt;
    String[] arrayOfString = paramXMLAElementInfo.getParents();
    int i = 0;
    int j = arrayOfString.length;
    while (i < j)
    {
      XMLAElementInfo localXMLAElementInfo = paramXMLADimensionInfo.getMemberByIdInternal(arrayOfString[i]);
      setLevel(paramXMLADimensionInfo, localXMLAElementInfo, paramInt + 1, paramLinkedHashSet);
      ++i;
    }
  }

  private XMLAElementInfo[] traverseDimension(XMLADimensionInfo paramXMLADimensionInfo, XMLAElementInfo[] paramArrayOfXMLAElementInfo)
  {
    this.maxLevel = 0;
    this.maxDepth = 0;
    ArrayList localArrayList1 = new ArrayList();
    ArrayList localArrayList2 = new ArrayList();
    int i = 0;
    int j = paramArrayOfXMLAElementInfo.length;
    while (i < j)
    {
      if (paramArrayOfXMLAElementInfo[i].getParentCount() == 0)
        localArrayList1.add(paramArrayOfXMLAElementInfo[i]);
      if (paramArrayOfXMLAElementInfo[i].getChildrenCount() == 0)
        localArrayList2.add(paramArrayOfXMLAElementInfo[i]);
      ++i;
    }
    LinkedHashSet localLinkedHashSet = new LinkedHashSet();
    j = 0;
    int k = 0;
    int l = localArrayList1.size();
    XMLAElementInfo localXMLAElementInfo;
    while (k < l)
    {
      localXMLAElementInfo = (XMLAElementInfo)localArrayList1.get(k);
      j = setDepth(paramXMLADimensionInfo, localXMLAElementInfo, 0, localLinkedHashSet, j);
      ++k;
    }
    k = 0;
    l = localArrayList2.size();
    while (k < l)
    {
      localXMLAElementInfo = (XMLAElementInfo)localArrayList2.get(k);
      setLevel(paramXMLADimensionInfo, localXMLAElementInfo, 0, localLinkedHashSet);
      ++k;
    }
    BuilderRegistry.getInstance().getDimensionInfoBuilder().updateMaxLevelAndDepth(paramXMLADimensionInfo, this.maxDepth, this.maxLevel);
    XMLAElementInfo[] arrayOfXMLAElementInfo = (XMLAElementInfo[])(XMLAElementInfo[])localLinkedHashSet.toArray(new XMLAElementInfo[0]);
    return paramArrayOfXMLAElementInfo;
  }

  public void setElementListInternal(String paramString, XMLADimensionInfo paramXMLADimensionInfo, List paramList)
  {
    throw new RuntimeException("No longer supported.");
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.builders.ElementInfoBuilder
 * JD-Core Version:    0.5.4
 */