package org.palo.api.subsets;

import org.palo.api.Hierarchy;
import org.palo.api.exceptions.PaloIOException;
import org.palo.api.subsets.filter.EffectiveFilter;
import org.palo.api.subsets.filter.settings.FilterSetting;

public abstract interface SubsetFilter
{
  public static final int TYPE_TEXT = 1;
  public static final int TYPE_HIERARCHICAL = 2;
  public static final int TYPE_PICKLIST = 4;
  public static final int TYPE_DATA = 8;
  public static final int TYPE_SORTING = 16;
  public static final int TYPE_ATTRIBUTE = 32;
  public static final int TYPE_ALIAS = 64;

  public abstract int getType();

  public abstract void reset();

  public abstract FilterSetting getSettings();

  public abstract void initialize();

  public abstract Hierarchy getHierarchy();

  public abstract void add(EffectiveFilter paramEffectiveFilter);

  public abstract void remove(EffectiveFilter paramEffectiveFilter);

  public abstract void adapt(SubsetFilter paramSubsetFilter);

  public abstract SubsetFilter copy();

  public abstract Subset2 getSubset();

  public abstract void bind(Subset2 paramSubset2);

  public abstract void unbind();

  public abstract void validateSettings()
    throws PaloIOException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.SubsetFilter
 * JD-Core Version:    0.5.4
 */