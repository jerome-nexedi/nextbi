package com.tensegrity.palo.xmla;

import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.ElementInfoBuilder;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.ElementInfo;
import com.tensegrity.palojava.HierarchyInfo;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.PropertyInfo;
import com.tensegrity.palojava.RuleInfo;
import com.tensegrity.palojava.loader.ElementLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class XMLAElementInfo implements ElementInfo, XMLAPaloInfo {
  private String id;

  private String name;

  private String uniqueName;

  private String hierarchyUniqueName;

  private Set<XMLAElementInfo> children;

  private String[] parents;

  private int type;

  private int level = -1;

  private int depth = -1;

  private int position = -1;

  private int parentCount;

  private int estimatedChildCount;

  private XMLAHierarchyInfo hierarchy;

  private final XMLADimensionInfo dimension;

  private XMLAElementInfo[] internalParents;

  private final Map kids;

  private boolean calculated;

  private RuleInfo rule = null;

  private String internalName;

  private boolean hasChildren;

  private final XMLAClient xmlaClient;

  private final XMLAConnection xmlaConnection;

  public XMLAElementInfo(XMLAHierarchyInfo paramXMLAHierarchyInfo,
    XMLADimensionInfo paramXMLADimensionInfo, XMLAClient paramXMLAClient,
    XMLAConnection paramXMLAConnection) {
    this.hierarchy = paramXMLAHierarchyInfo;
    this.dimension = paramXMLADimensionInfo;
    this.children = new LinkedHashSet();
    this.kids = new LinkedHashMap();
    this.calculated = false;
    this.xmlaClient = paramXMLAClient;
    this.xmlaConnection = paramXMLAConnection;
  }

  public String getHierarchyUniqueName() {
    return this.hierarchyUniqueName;
  }

  public void addChild(XMLAElementInfo paramXMLAElementInfo) {
    this.children.add(paramXMLAElementInfo);
  }

  public void clearChildren() {
    this.children.clear();
  }

  public void setChildren(XMLAElementInfo[] paramArrayOfXMLAElementInfo) {
    clearChildren();
    this.children.addAll(Arrays.asList(paramArrayOfXMLAElementInfo));
  }

  public void setId(String paramString) {
    String str = paramString.replaceAll("\\[", "((");
    str = str.replaceAll("\\]", "))");
    str = str.replaceAll(":", "**");
    str = str.replaceAll(",", "(comma)");
    this.id = str;
  }

  public void setName(String paramString) {
    this.name = paramString;
  }

  public void setInternalName(String paramString) {
    this.internalName = paramString;
  }

  public String getInternalName() {
    return this.internalName;
  }

  public void setUniqueName(String paramString) {
    this.uniqueName = paramString;
  }

  public void setHierarchyUniqueName(String paramString) {
    this.hierarchyUniqueName = paramString;
  }

  public void setParentCount(int paramInt) {
    this.parentCount = paramInt;
  }

  public void setType(int paramInt) {
    this.type = paramInt;
  }

  public String[] getChildren() {
    if (this.hierarchy == null)
      this.hierarchy = this.dimension.getDefaultHierarchy();
    ElementLoader localElementLoader;
    if (this.hierarchy != null)
      localElementLoader = this.xmlaConnection.getElementLoader(this.hierarchy);
    else
      localElementLoader = this.xmlaConnection.getElementLoader(this.dimension);
    PaloInfo[] localObject1;
    if ((this.children.isEmpty()) && (getChildrenCount() > 0)) {
      localObject1 = BuilderRegistry.getInstance().getElementInfoBuilder()
        .getChildren(this.xmlaConnection, this.xmlaClient,
          ((XMLADimensionInfo) this.hierarchy.getDimension()).getCubeId(), this);
      for (PaloInfo localPaloInfo : localObject1) {
        this.children.add((XMLAElementInfo) localPaloInfo);
        localElementLoader.loaded(localPaloInfo);
      }
    } else {
      localObject1 = (ElementInfo[]) this.children.toArray(new XMLAElementInfo[0]);
    }
    String[] localObject1String = new String[localObject1.length];
    for (int i = 0; i < localObject1.length; ++i)
      localObject1String[i] = localObject1[i].getId();
    return localObject1String;
  }

  public int getChildrenCount() {
    if (this.children.isEmpty())
      return this.estimatedChildCount;
    return this.children.size();
  }

  public boolean hasChildren() {
    return this.hasChildren;
  }

  public void setHasChildren(boolean paramBoolean) {
    this.hasChildren = paramBoolean;
  }

  public final void setEstimatedChildCount(int paramInt) {
    this.estimatedChildCount = paramInt;
  }

  public int getDepth() {
    return this.depth;
  }

  public void setDepth(int paramInt) {
    this.depth = paramInt;
  }

  public DimensionInfo getDimension() {
    if (this.hierarchy == null)
      return this.dimension;
    return this.hierarchy.getDimension();
  }

  public HierarchyInfo getHierarchy() {
    return this.hierarchy;
  }

  public int getIndent() {
    return 0;
  }

  public int getLevel() {
    return this.level;
  }

  public void setLevel(int paramInt) {
    this.level = paramInt;
  }

  public String getName() {
    if ((this.name == null) || (this.name.trim().length() == 0))
      return " ";
    return this.name;
  }

  public int getParentCount() {
    return this.parentCount;
  }

  public String[] getParents() {
    if (this.parents == null)
      return new String[0];
    return this.parents;
  }

  public int getPosition() {
    return this.position;
  }

  public void setPosition(int paramInt) {
    this.position = paramInt;
  }

  public double[] getWeights() {
    int i = getChildrenCount();
    if (i == 0)
      return new double[0];
    double[] arrayOfDouble = new double[i];
    double d = 1.0D;
    for (int j = 0; j < i; ++j)
      arrayOfDouble[j] = d;
    return arrayOfDouble;
  }

  public void setParents(String[] paramArrayOfString) {
    this.parents = paramArrayOfString;
  }

  public String getId() {
    return this.id;
  }

  public int getType() {
    if ((getChildrenCount() > 0) || (this.estimatedChildCount > 0))
      return 4;
    return this.type;
  }

  public String toString() {
    return "Element " + getName() + " [" + getId() + "]: " + getParentCount()
      + " parent(s), " + getChildrenCount() + " children, Type: " + getType();
  }

  public String getUniqueName() {
    return this.uniqueName;
  }

  public void addChildInternal(XMLAElementInfo paramXMLAElementInfo) {
    this.kids.put(Integer.valueOf(paramXMLAElementInfo.getUniqueName().hashCode()),
      paramXMLAElementInfo);
  }

  public XMLAElementInfo getChildInternal(String paramString) {
    return (XMLAElementInfo) this.kids.get(paramString);
  }

  public XMLAElementInfo[] getChildrenInternal() {
    return (XMLAElementInfo[]) (XMLAElementInfo[]) this.kids.values().toArray(
      new XMLAElementInfo[0]);
  }

  public void setParentInternal(XMLAElementInfo[] paramArrayOfXMLAElementInfo) {
    this.parents = new String[paramArrayOfXMLAElementInfo.length];
    for (int i = 0; i < paramArrayOfXMLAElementInfo.length; ++i)
      this.parents[i] = paramArrayOfXMLAElementInfo[i].getId();
    setParentCount(paramArrayOfXMLAElementInfo.length);
    this.internalParents = paramArrayOfXMLAElementInfo;
  }

  public XMLAElementInfo[] getParentsInternal() {
    return this.internalParents;
  }

  public boolean isCalculated() {
    return this.calculated;
  }

  public RuleInfo getRule() {
    return this.rule;
  }

  public void setRule(RuleInfo paramRuleInfo) {
    this.rule = paramRuleInfo;
  }

  public void setCalculated(boolean paramBoolean) {
    this.calculated = paramBoolean;
  }

  public String[] getAllKnownPropertyIds(DbConnection paramDbConnection) {
    return new String[0];
  }

  public PropertyInfo getProperty(DbConnection paramDbConnection, String paramString) {
    return null;
  }

  public boolean canBeModified() {
    return false;
  }

  public boolean canCreateChildren() {
    return false;
  }

  public void update(String[] paramArrayOfString) {
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.XMLAElementInfo JD-Core Version:
 * 0.5.4
 */