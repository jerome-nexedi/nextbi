package org.palo.api.subsets;

import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Hierarchy;
import org.palo.api.PaloObject;

public abstract interface Subset2 extends PaloObject
{
  public static final int TYPE_LOCAL = 0;
  public static final int TYPE_GLOBAL = 1;

  /** @deprecated */
  public abstract Dimension getDimension();

  public abstract Hierarchy getDimHierarchy();

  public abstract int getIndent();

  public abstract void setIndent(int paramInt);

  public abstract void reset();

  public abstract void add(SubsetFilter paramSubsetFilter);

  public abstract void remove(SubsetFilter paramSubsetFilter);

  public abstract SubsetFilter[] getFilters();

  public abstract SubsetFilter getFilter(int paramInt);

  public abstract boolean isActive(int paramInt);

  public abstract void save();

  public abstract boolean contains(Element paramElement);

  public abstract Element[] getElements();

  /** @deprecated */
  public abstract ElementNode[] getHierarchy();

  public abstract ElementNode[] getRootNodes();

  public abstract void modified();

  public abstract void rename(String paramString);

  public abstract int getType();

  public abstract Subset2 copy();

  public abstract void setDescription(String paramString);

  public abstract String getDescription();

  public abstract String getDefinition();

  public abstract Subset2 setDefinition(String paramString);

  public abstract boolean validate();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.Subset2
 * JD-Core Version:    0.5.4
 */