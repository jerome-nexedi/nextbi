package org.palo.api;

/** @deprecated */
public abstract interface VirtualDimensionDefinition
{
  public abstract Dimension getSourceDimension();

  public abstract DimensionFilter getFilter();

  public abstract Element[] getElements();

  public abstract ElementNode[] getRootElements();

  public abstract boolean isFlat();

  public abstract String getActiveSubset();

  public abstract Hierarchy getActiveHierarchy();

  public abstract void setActiveHierarchy(Hierarchy paramHierarchy);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.VirtualDimensionDefinition
 * JD-Core Version:    0.5.4
 */