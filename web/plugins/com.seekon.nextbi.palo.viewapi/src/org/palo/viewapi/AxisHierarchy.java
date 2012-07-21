package org.palo.viewapi;

import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Hierarchy;
import org.palo.api.subsets.Subset2;

public abstract interface AxisHierarchy {
  public static final String USE_ALIAS = "com.tensegrity.palo.axis.use_alias";

  public abstract Property<?>[] getProperties();

  public abstract Property<?> getProperty(String paramString);

  public abstract void addProperty(Property<?> paramProperty);

  public abstract void removeProperty(Property<?> paramProperty);

  public abstract void addSelectedElement(Element paramElement);

  public abstract Hierarchy getHierarchy();

  public abstract Element[] getSelectedElements();

  public abstract Subset2 getSubset();

  public abstract boolean hasSelectedElements();

  public abstract void removeSelectedElement(Element paramElement);

  public abstract void clearSelectedElements();

  public abstract void setSubset(Subset2 paramSubset2);

  public abstract void setSubsetMissing(String paramString);

  public abstract void setAliasMissing(String paramString);

  public abstract String getSubsetMissing();

  public abstract String getAliasMissing();

  public abstract ElementNode[] getRootNodes();

  public abstract String getElementNameFor(String paramString);

  public abstract void setLocalFilter(LocalFilter paramLocalFilter);

  public abstract LocalFilter getLocalFilter();

  public abstract boolean contains(Element paramElement);

  public abstract Axis getAxis();

  public abstract void setAxis(Axis paramAxis);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.AxisHierarchy JD-Core Version: 0.5.4
 */