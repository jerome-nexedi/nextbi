package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palo.xmla.XMLAConnection;
import com.tensegrity.palo.xmla.XMLADimensionInfo;
import com.tensegrity.palo.xmla.XMLAElementInfo;
import com.tensegrity.palo.xmla.XMLAHierarchyInfo;
import com.tensegrity.palo.xmla.XMLAProperties;
import com.tensegrity.palo.xmla.XMLARestrictions;
import com.tensegrity.palo.xmla.XMLAServerInfo;
import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.ElementInfoBuilder;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.ElementInfo;
import com.tensegrity.palojava.HierarchyInfo;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.loader.ElementLoader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLAElementLoader extends ElementLoader
{
  private Set<String> elementIds = null;
  private final XMLAClient xmlaClient;
  private final boolean LOAD_ALL_ELEMENTS;
  private final HashMap<Integer, ElementInfo[]> depthCache;
  private final XMLAHierarchyInfo hierarchy;

  public XMLAElementLoader(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, DimensionInfo paramDimensionInfo)
  {
    super(paramXMLAConnection, paramDimensionInfo.getDefaultHierarchy());
    this.xmlaClient = paramXMLAClient;
    this.depthCache = new HashMap();
    this.hierarchy = null;
    this.LOAD_ALL_ELEMENTS = true;
  }

  public XMLAElementLoader(XMLAConnection paramXMLAConnection, XMLAClient paramXMLAClient, HierarchyInfo paramHierarchyInfo)
  {
    super(paramXMLAConnection, paramHierarchyInfo);
    this.xmlaClient = paramXMLAClient;
    this.depthCache = new HashMap();
    this.hierarchy = ((XMLAHierarchyInfo)paramHierarchyInfo);
    this.LOAD_ALL_ELEMENTS = true;
  }

  public String[] getAllElementIds()
  {
    if (this.elementIds == null)
      if (this.LOAD_ALL_ELEMENTS)
        loadElements();
      else
        loadAllElementIds();
    return (String[])this.elementIds.toArray(new String[0]);
  }

  public final ElementInfo load(int paramInt)
  {
    String[] arrayOfString = getAllElementIds();
    if ((paramInt < 0) || (paramInt > arrayOfString.length - 1))
      return null;
    return load(arrayOfString[paramInt]);
  }

  public ElementInfo[] getElementsAtDepth(int paramInt)
  {
    if (this.depthCache.containsKey(Integer.valueOf(paramInt)))
      return (ElementInfo[])this.depthCache.get(Integer.valueOf(paramInt));
    XMLAElementInfo[] arrayOfXMLAElementInfo = BuilderRegistry.getInstance().getElementInfoBuilder().getElements((XMLAConnection)this.paloConnection, this.xmlaClient, ((XMLADimensionInfo)this.hierarchy.getDimension()).getCubeId(), this.hierarchy, paramInt);
    this.depthCache.put(Integer.valueOf(paramInt), arrayOfXMLAElementInfo);
    return arrayOfXMLAElementInfo;
  }

  public ElementInfo loadByName(String paramString)
  {
    Object localObject;
    if (this.LOAD_ALL_ELEMENTS)
    {
      localObject = findElement(paramString);
      if (localObject == null)
        loadElements();
      return findElement(paramString);
    }
    String str = XMLADimensionInfo.transformId(paramString);
    if (!this.loadedInfo.containsKey(str))
    {
      localObject = BuilderRegistry.getInstance().getElementInfoBuilder().getElementInfo((XMLAConnection)this.paloConnection, this.xmlaClient, this.hierarchy, paramString);
      loaded((PaloInfo)localObject);
    }
    else
    {
      localObject = (ElementInfo)this.loadedInfo.get(str);
    }
    return (ElementInfo)localObject;
  }

  public final ElementInfo[] getChildren(ElementInfo paramElementInfo)
  {
    XMLAElementInfo localXMLAElementInfo1 = (XMLAElementInfo)paramElementInfo;
    String[] arrayOfString = localXMLAElementInfo1.getChildren();
    XMLAElementInfo[] arrayOfXMLAElementInfo1 = null;
    if ((arrayOfString == null) || (arrayOfString.length == 0))
    {
      if (!localXMLAElementInfo1.hasChildren())
      {
        arrayOfXMLAElementInfo1 = new XMLAElementInfo[0];
      }
      else
      {
        arrayOfXMLAElementInfo1 = BuilderRegistry.getInstance().getElementInfoBuilder().getChildren((XMLAConnection)this.paloConnection, this.xmlaClient, ((XMLADimensionInfo)this.hierarchy.getDimension()).getCubeId(), (XMLAElementInfo)paramElementInfo);
        for (XMLAElementInfo localXMLAElementInfo2 : arrayOfXMLAElementInfo1)
          loaded(localXMLAElementInfo2);
        localXMLAElementInfo1.setChildren(arrayOfXMLAElementInfo1);
      }
    }
    else
    {
      arrayOfXMLAElementInfo1 = new XMLAElementInfo[arrayOfString.length];
      for (int i = 0; i < arrayOfXMLAElementInfo1.length; ++i)
        arrayOfXMLAElementInfo1[i] = ((XMLAElementInfo)load(arrayOfString[i]));
    }
    return arrayOfXMLAElementInfo1;
  }

  protected final void reload()
  {
    System.out.println("XMLAElementLoader::reload.");
  }

  public final ElementInfo findElement(String paramString)
  {
    Collection localCollection = getLoaded();
    String str = paramString.replaceAll("\\[", "((");
    str = str.replaceAll("\\]", "))");
    str = str.replaceAll(":", "**");
    Iterator localIterator = localCollection.iterator();
    PaloInfo localPaloInfo;
    ElementInfo localElementInfo;
    while (localIterator.hasNext())
    {
      localPaloInfo = (PaloInfo)localIterator.next();
      if (localPaloInfo instanceof ElementInfo)
      {
        localElementInfo = (ElementInfo)localPaloInfo;
        if (localElementInfo.getId().equals(str))
          return localElementInfo;
      }
    }
    localIterator = localCollection.iterator();
    while (localIterator.hasNext())
    {
      localPaloInfo = (PaloInfo)localIterator.next();
      if (localPaloInfo instanceof ElementInfo)
      {
        localElementInfo = (ElementInfo)localPaloInfo;
        if (localElementInfo.getName().equals(str))
          return localElementInfo;
      }
    }
    return null;
  }

  private void loadAllElementIds()
  {
    this.elementIds = new LinkedHashSet();
    String str1 = this.xmlaClient.getConnections()[0].getName();
    try
    {
      XMLARestrictions localXMLARestrictions = new XMLARestrictions();
      XMLAProperties localXMLAProperties = new XMLAProperties();
      localXMLAProperties.setDataSourceInfo(str1);
      localXMLAProperties.setCatalog(this.hierarchy.getDimension().getDatabase().getId());
      localXMLARestrictions.setCatalog(this.hierarchy.getDimension().getDatabase().getId());
      String str2 = XMLADimensionInfo.transformId(this.hierarchy.getDimension().getId());
      int i = str2.indexOf("|.#.|");
      String str3 = str2.substring(0, i);
      localXMLARestrictions.setHierarchyUniqueName(XMLADimensionInfo.transformId(XMLADimensionInfo.getDimIdFromId(this.hierarchy.getId())));
      localXMLARestrictions.setCubeName(str3);
      Document localDocument = this.xmlaClient.getMemberList(localXMLARestrictions, localXMLAProperties);
      NodeList localNodeList1 = localDocument.getElementsByTagName("row");
      if ((localNodeList1 == null) || (localNodeList1.getLength() == 0))
        return;
      for (int j = 0; j < localNodeList1.getLength(); ++j)
      {
        NodeList localNodeList2 = localNodeList1.item(j).getChildNodes();
        for (int k = 0; k < localNodeList2.getLength(); ++k)
        {
          if (localNodeList2.item(k).getNodeType() != 1)
            continue;
          Node localNode = localNodeList2.item(k);
          String str4 = localNode.getNodeName();
          if (!str4.equals("MEMBER_UNIQUE_NAME"))
            continue;
          String str5 = XMLAClient.getTextFromDOMElement(localNode);
          String str6 = str5.replaceAll("\\[", "((");
          str6 = str6.replaceAll("\\]", "))");
          str6 = str6.replaceAll(":", "**");
          this.elementIds.add(str6);
        }
      }
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }

  private final void loadElements()
  {
    this.elementIds = new LinkedHashSet();
    if (this.hierarchy == null)
    {
      System.out.println("Hierarchy == null");
      return;
    }
    XMLAElementInfo[] arrayOfXMLAElementInfo1 = BuilderRegistry.getInstance().getElementInfoBuilder().getElements((XMLAConnection)this.paloConnection, this.xmlaClient, ((XMLADimensionInfo)this.hierarchy.getDimension()).getCubeId(), this.hierarchy);
    XMLAElementLoader localXMLAElementLoader = null;
    if (this.hierarchy != null)
      localXMLAElementLoader = (XMLAElementLoader)((XMLAConnection)this.paloConnection).getElementLoader(this.hierarchy.getDimension());
    for (XMLAElementInfo localXMLAElementInfo : arrayOfXMLAElementInfo1)
    {
      this.elementIds.add(localXMLAElementInfo.getId());
      loaded(localXMLAElementInfo);
      if (localXMLAElementLoader == null)
        continue;
      localXMLAElementLoader.loaded(localXMLAElementInfo);
    }
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.loader.XMLAElementLoader
 * JD-Core Version:    0.5.4
 */