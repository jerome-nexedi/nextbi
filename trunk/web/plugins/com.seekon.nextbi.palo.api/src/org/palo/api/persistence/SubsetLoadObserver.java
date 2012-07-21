package org.palo.api.persistence;

import org.palo.api.Hierarchy;
import org.palo.api.subsets.Subset2;

/** @deprecated */
public abstract interface SubsetLoadObserver {
  public abstract void loadComplete(Subset2 paramSubset2);

  public abstract void loadFailed(String paramString1, String paramString2,
    int paramInt, Hierarchy paramHierarchy);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.persistence.SubsetLoadObserver JD-Core Version:
 * 0.5.4
 */