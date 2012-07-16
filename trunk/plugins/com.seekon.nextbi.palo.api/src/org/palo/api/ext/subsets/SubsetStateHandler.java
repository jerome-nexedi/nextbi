package org.palo.api.ext.subsets;

import org.palo.api.Dimension;
import org.palo.api.DimensionFilter;
import org.palo.api.Hierarchy;
import org.palo.api.HierarchyFilter;
import org.palo.api.Subset;
import org.palo.api.SubsetState;

/** @deprecated */
public abstract interface SubsetStateHandler
{
  public abstract void use(Subset paramSubset, SubsetState paramSubsetState);

  public abstract Subset getSubset();

  public abstract SubsetState getSubsetState();

  public abstract DimensionFilter createDimensionFilter(Dimension paramDimension);

  public abstract HierarchyFilter createHierarchyFilter(Hierarchy paramHierarchy);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.subsets.SubsetStateHandler
 * JD-Core Version:    0.5.4
 */