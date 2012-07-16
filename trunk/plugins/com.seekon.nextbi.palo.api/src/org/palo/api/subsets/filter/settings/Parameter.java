package org.palo.api.subsets.filter.settings;

import org.palo.api.subsets.Subset2;

public abstract interface Parameter
{
  public abstract String getName();

  public abstract Object getValue();

  public abstract void bind(Subset2 paramSubset2);

  public abstract void unbind();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.Parameter
 * JD-Core Version:    0.5.4
 */