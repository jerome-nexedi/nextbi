package org.palo.api;

import org.palo.api.subsets.SubsetHandler;

public abstract interface Hierarchy extends PaloObject
{
  public abstract Dimension getDimension();

  public abstract boolean isNormal();

  public abstract void rename(String paramString);

  public abstract int getElementCount();

  public abstract Element[] getElements();

  public abstract void visitElementTree(ElementNodeVisitor paramElementNodeVisitor);

  public abstract Element[] getRootElements();

  public abstract ElementNode[] getElementsTree();

  public abstract Element getElementAt(int paramInt);

  public abstract String[] getElementNames();

  public abstract Element getElementByName(String paramString);

  public abstract Element getElementById(String paramString);

  public abstract void addElements(String[] paramArrayOfString, int[] paramArrayOfInt);

  public abstract void addElements(String[] paramArrayOfString, int paramInt, Element[][] paramArrayOfElement, double[][] paramArrayOfDouble);

  public abstract void addElements(String[] paramArrayOfString, int[] paramArrayOfInt, Element[][] paramArrayOfElement, double[][] paramArrayOfDouble);

  public abstract void updateConsolidations(Consolidation[] paramArrayOfConsolidation);

  public abstract void removeConsolidations(Element[] paramArrayOfElement);

  public abstract Element[] getElementsInOrder();

  public abstract ElementNode[] getAllElementNodes();

  public abstract Element addElement(String paramString, int paramInt);

  public abstract void removeElement(Element paramElement);

  public abstract void removeElements(Element[] paramArrayOfElement);

  public abstract void renameElement(Element paramElement, String paramString);

  public abstract Consolidation newConsolidation(Element paramElement1, Element paramElement2, double paramDouble);

  public abstract int getMaxLevel();

  public abstract int getMaxDepth();

  public abstract boolean isAttributeHierarchy();

  public abstract boolean isSubsetHierarchy();

  public abstract Attribute addAttribute(String paramString);

  public abstract void removeAttribute(Attribute paramAttribute);

  public abstract void removeAllAttributes();

  public abstract Attribute[] getAttributes();

  public abstract Attribute getAttribute(String paramString);

  public abstract Attribute getAttributeByName(String paramString);

  public abstract void setAttributeValues(Attribute[] paramArrayOfAttribute, Element[] paramArrayOfElement, Object[] paramArrayOfObject);

  public abstract Object[] getAttributeValues(Attribute[] paramArrayOfAttribute, Element[] paramArrayOfElement);

  public abstract Cube getAttributeCube();

  public abstract Hierarchy getAttributeHierarchy();

  /** @deprecated */
  public abstract Subset addSubset(String paramString);

  public abstract void removeSubset(Subset paramSubset);

  public abstract Subset[] getSubsets();

  public abstract Subset getSubset(String paramString);

  public abstract SubsetHandler getSubsetHandler();

  public abstract void reload(boolean paramBoolean);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Hierarchy
 * JD-Core Version:    0.5.4
 */