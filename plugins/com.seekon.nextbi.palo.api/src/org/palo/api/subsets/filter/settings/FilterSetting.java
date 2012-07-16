package org.palo.api.subsets.filter.settings;

import org.palo.api.subsets.Subset2;

public abstract interface FilterSetting
{
  public abstract void reset();

  public abstract void adapt(FilterSetting paramFilterSetting);

  public abstract void bind(Subset2 paramSubset2);

  public abstract void unbind();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.FilterSetting
 * JD-Core Version:    0.5.4
 */