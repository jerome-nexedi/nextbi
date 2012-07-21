package org.palo.api.subsets;

import org.palo.api.Subset;

public abstract interface SubsetStorageHandler {
  public abstract boolean canRead(int paramInt);

  public abstract boolean canWrite(int paramInt);

  public abstract void convert(Subset[] paramArrayOfSubset, int paramInt,
    boolean paramBoolean);

  public abstract void reset();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.SubsetStorageHandler JD-Core Version:
 * 0.5.4
 */