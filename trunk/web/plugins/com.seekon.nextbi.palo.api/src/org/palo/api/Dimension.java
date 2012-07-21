package org.palo.api;

import com.tensegrity.palojava.DimensionInfo;
import org.palo.api.subsets.SubsetHandler;

public abstract interface Dimension extends PaloObject {
  public static final int DIMENSIONEXTENDEDTYPE_REGULAR = 0;

  public static final int DIMENSIONEXTENDEDTYPE_VIRTUAL = 1;

  public abstract int getExtendedType();

  public abstract int getType();

  public abstract String getName();

  public abstract Database getDatabase();

  /** @deprecated */
  public abstract int getElementCount();

  /** @deprecated */
  public abstract Element getElementAt(int paramInt);

  /** @deprecated */
  public abstract Element[] getElements();

  /** @deprecated */
  public abstract String[] getElementNames();

  /** @deprecated */
  public abstract Element getElementByName(String paramString);

  /** @deprecated */
  public abstract Element getElementById(String paramString);

  public abstract void rename(String paramString);

  /** @deprecated */
  public abstract void addElements(String[] paramArrayOfString, int[] paramArrayOfInt);

  public abstract void addElements(String[] paramArrayOfString, int paramInt,
    Element[][] paramArrayOfElement, double[][] paramArrayOfDouble);

  public abstract void addElements(String[] paramArrayOfString,
    int[] paramArrayOfInt, Element[][] paramArrayOfElement,
    double[][] paramArrayOfDouble);

  public abstract void updateConsolidations(Consolidation[] paramArrayOfConsolidation);

  public abstract void removeConsolidations(Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract Element[] getRootElements();

  /** @deprecated */
  public abstract Element[] getElementsInOrder();

  /** @deprecated */
  public abstract ElementNode[] getElementsTree();

  /** @deprecated */
  public abstract void visitElementTree(ElementNodeVisitor paramElementNodeVisitor);

  /** @deprecated */
  public abstract ElementNode[] getAllElementNodes();

  /** @deprecated */
  public abstract void dumpElementsTree();

  /** @deprecated */
  public abstract Element addElement(String paramString, int paramInt);

  /** @deprecated */
  public abstract void removeElement(Element paramElement);

  /** @deprecated */
  public abstract void removeElements(Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract void renameElement(Element paramElement, String paramString);

  /** @deprecated */
  public abstract Consolidation newConsolidation(Element paramElement1,
    Element paramElement2, double paramDouble);

  public abstract boolean isAttributeDimension();

  /** @deprecated */
  public abstract Attribute addAttribute(String paramString);

  /** @deprecated */
  public abstract void removeAttribute(Attribute paramAttribute);

  /** @deprecated */
  public abstract void removeAllAttributes();

  /** @deprecated */
  public abstract Attribute[] getAttributes();

  /** @deprecated */
  public abstract Attribute getAttribute(String paramString);

  /** @deprecated */
  public abstract Attribute getAttributeByName(String paramString);

  /** @deprecated */
  public abstract void setAttributeValues(Attribute[] paramArrayOfAttribute,
    Element[] paramArrayOfElement, Object[] paramArrayOfObject);

  /** @deprecated */
  public abstract Object[] getAttributeValues(Attribute[] paramArrayOfAttribute,
    Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract Cube getAttributeCube();

  /** @deprecated */
  public abstract Dimension getAttributeDimension();

  /** @deprecated */
  public abstract boolean isSubsetDimension();

  /** @deprecated */
  public abstract Subset addSubset(String paramString);

  public abstract void removeSubset(Subset paramSubset);

  public abstract Subset[] getSubsets();

  public abstract Subset getSubset(String paramString);

  public abstract boolean isSystemDimension();

  public abstract boolean isUserInfoDimension();

  /** @deprecated */
  public abstract int getMaxLevel();

  /** @deprecated */
  public abstract int getMaxDepth();

  public abstract Cube[] getCubes();

  public abstract SubsetHandler getSubsetHandler();

  public abstract Hierarchy[] getHierarchies();

  public abstract int getHierarchyCount();

  public abstract String[] getHierarchiesIds();

  public abstract Hierarchy getHierarchyAt(int paramInt);

  public abstract Hierarchy getHierarchyById(String paramString);

  public abstract Hierarchy getHierarchyByName(String paramString);

  public abstract Hierarchy getDefaultHierarchy();

  public abstract DimensionInfo getInfo();

  public abstract void reload(boolean paramBoolean);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Dimension JD-Core Version: 0.5.4
 */