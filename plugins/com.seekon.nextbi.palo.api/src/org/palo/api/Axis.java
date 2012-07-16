package org.palo.api;

import org.palo.api.subsets.Subset2;
import org.palo.api.utils.ElementPath;

public abstract interface Axis
{
  public abstract String getId();

  public abstract void setName(String paramString);

  public abstract String getName();

  public abstract Hierarchy getHierarchy(Dimension paramDimension);

  /** @deprecated */
  public abstract void add(Dimension paramDimension);

  public abstract void add(Hierarchy paramHierarchy);

  /** @deprecated */
  public abstract void remove(Dimension paramDimension);

  public abstract void remove(Hierarchy paramHierarchy);

  /** @deprecated */
  public abstract Dimension[] getDimensions();

  public abstract Hierarchy[] getHierarchies();

  public abstract Subset getActiveSubset(Dimension paramDimension);

  public abstract void setActiveSubset(Dimension paramDimension, Subset paramSubset);

  public abstract void setActiveSubset2(Dimension paramDimension, Subset2 paramSubset2);

  public abstract Subset2 getActiveSubset2(Dimension paramDimension);

  /** @deprecated */
  public abstract Element getSelectedElement(Dimension paramDimension);

  public abstract Element getSelectedElement(Hierarchy paramHierarchy);

  /** @deprecated */
  public abstract void setSelectedElement(Dimension paramDimension, Element paramElement);

  public abstract void setSelectedElement(Hierarchy paramHierarchy, Element paramElement);

  /** @deprecated */
  public abstract void addExpanded(Dimension paramDimension, Element[] paramArrayOfElement, int paramInt);

  /** @deprecated */
  public abstract void addExpanded(Hierarchy paramHierarchy, Element[] paramArrayOfElement, int paramInt);

  /** @deprecated */
  public abstract void removeExpanded(Dimension paramDimension, Element[] paramArrayOfElement, int paramInt);

  /** @deprecated */
  public abstract void removeExpanded(Hierarchy paramHierarchy, Element[] paramArrayOfElement, int paramInt);

  /** @deprecated */
  public abstract Element[][] getExpanded(Dimension paramDimension);

  /** @deprecated */
  public abstract Element[][] getExpanded(Hierarchy paramHierarchy);

  public abstract ElementPath[] getExpandedPaths();

  public abstract void addExpanded(ElementPath paramElementPath);

  public abstract void addExpanded(ElementPath[] paramArrayOfElementPath);

  public abstract void removeExpanded(ElementPath paramElementPath);

  /** @deprecated */
  public abstract int[] getRepetitionsForExpanded(Dimension paramDimension, Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract int[] getRepetitionsForExpanded(Hierarchy paramHierarchy, Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract void addHidden(Dimension paramDimension, Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract void addHidden(Hierarchy paramHierarchy, Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract void removeHidden(Dimension paramDimension, Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract void removeHidden(Hierarchy paramHierarchy, Element[] paramArrayOfElement);

  /** @deprecated */
  public abstract Element[][] getHidden(Dimension paramDimension);

  /** @deprecated */
  public abstract Element[][] getHidden(Hierarchy paramHierarchy);

  public abstract void addVisible(ElementPath paramElementPath);

  public abstract void removeVisible(ElementPath paramElementPath);

  /** @deprecated */
  public abstract ElementPath[] getVisiblePaths(Dimension paramDimension);

  public abstract ElementPath[] getVisiblePaths(Hierarchy paramHierarchy);

  public abstract boolean isVisible(ElementPath paramElementPath);

  /** @deprecated */
  public abstract void addProperty(String paramString1, String paramString2);

  /** @deprecated */
  public abstract void removeProperty(String paramString);

  /** @deprecated */
  public abstract String[] getProperties();

  /** @deprecated */
  public abstract String getPropertyValue(String paramString);

  /** @deprecated */
  public abstract String getData(String paramString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Axis
 * JD-Core Version:    0.5.4
 */