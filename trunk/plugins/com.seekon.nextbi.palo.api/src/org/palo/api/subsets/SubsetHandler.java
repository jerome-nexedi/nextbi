package org.palo.api.subsets;

import org.palo.api.Dimension;
import org.palo.api.Hierarchy;

public abstract interface SubsetHandler
{
  /** @deprecated */
  public abstract Dimension getDimension();

  public abstract Hierarchy getHierarchy();

  public abstract void reset();

  public abstract String getSubsetId(String paramString, int paramInt);

  public abstract Subset2 addSubset(String paramString, int paramInt);

  public abstract Subset2[] getSubsets();

  public abstract Subset2[] getSubsets(int paramInt);

  public abstract Subset2 getSubset(String paramString, int paramInt);

  public abstract String[] getSubsetIDs();

  public abstract String[] getSubsetNames();

  public abstract String[] getSubsetNames(int paramInt);

  public abstract String[] getSubsetIDs(int paramInt);

  public abstract String getSubsetName(String paramString);

  public abstract boolean hasSubsets(int paramInt);

  public abstract void remove(Subset2 paramSubset2);

  public abstract void remove(String paramString, int paramInt);

  public abstract void save(Subset2 paramSubset2);

  public abstract boolean canWrite(int paramInt);

  public abstract boolean canRead(int paramInt);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.SubsetHandler
 * JD-Core Version:    0.5.4
 */