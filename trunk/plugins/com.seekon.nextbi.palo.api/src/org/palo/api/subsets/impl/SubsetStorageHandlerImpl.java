package org.palo.api.subsets.impl;

import org.palo.api.Hierarchy;
import org.palo.api.exceptions.PaloIOException;
import org.palo.api.subsets.Subset2;
import org.palo.api.subsets.SubsetStorageHandler;

public abstract class SubsetStorageHandlerImpl
  implements SubsetStorageHandler
{
  protected abstract String getSubsetId(Hierarchy paramHierarchy, String paramString, int paramInt);

  protected abstract String[] getSubsetIDs(Hierarchy paramHierarchy);

  protected abstract String[] getSubsetIDs(Hierarchy paramHierarchy, int paramInt);

  protected abstract String getSubsetName(String paramString);

  protected abstract String[] getSubsetNames(Hierarchy paramHierarchy);

  protected abstract String[] getSubsetNames(Hierarchy paramHierarchy, int paramInt);

  protected abstract boolean hasSubsets(Hierarchy paramHierarchy, int paramInt);

  protected abstract void remove(Subset2 paramSubset2);

  protected abstract void remove(String paramString, int paramInt, Hierarchy paramHierarchy);

  protected abstract void save(Subset2 paramSubset2)
    throws PaloIOException;

  protected abstract void rename(Subset2 paramSubset2, String paramString)
    throws PaloIOException;

  protected abstract String newSubsetCell(String paramString, Hierarchy paramHierarchy, int paramInt)
    throws PaloIOException;

  protected abstract Subset2 load(String paramString, Hierarchy paramHierarchy, int paramInt, SubsetHandlerImpl paramSubsetHandlerImpl)
    throws PaloIOException;

  protected abstract String getDefinition(Subset2 paramSubset2)
    throws PaloIOException;

  protected abstract void setDefinition(Subset2 paramSubset2, String paramString)
    throws PaloIOException;

  protected abstract void validate(Subset2 paramSubset2)
    throws PaloIOException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.impl.SubsetStorageHandlerImpl
 * JD-Core Version:    0.5.4
 */