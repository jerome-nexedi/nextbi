package org.palo.viewapi;

import org.palo.api.Hierarchy;
import org.palo.api.utils.ElementPath;

public abstract interface Axis extends AxisProperties {
  public abstract String getId();

  public abstract void setName(String paramString);

  public abstract String getName();

  public abstract Hierarchy[] getHierarchies();

  public abstract AxisHierarchy add(Hierarchy paramHierarchy);

  public abstract void add(AxisHierarchy paramAxisHierarchy);

  public abstract AxisHierarchy remove(Hierarchy paramHierarchy);

  public abstract void remove(AxisHierarchy paramAxisHierarchy);

  public abstract void removeAll();

  public abstract AxisHierarchy[] getAxisHierarchies();

  public abstract AxisHierarchy getAxisHierarchy(Hierarchy paramHierarchy);

  public abstract AxisHierarchy getAxisHierarchy(String paramString);

  public abstract ElementPath[] getExpandedPaths();

  public abstract void addExpanded(ElementPath paramElementPath);

  public abstract void addExpanded(ElementPath[] paramArrayOfElementPath);

  public abstract void removeExpanded(ElementPath paramElementPath);

  public abstract void removeAllExpandedPaths();

  public abstract Axis copy();

  public abstract Property<?>[] getProperties();

  public abstract Property<?> getProperty(String paramString);

  public abstract void addProperty(Property<?> paramProperty);

  public abstract void removeProperty(Property<?> paramProperty);

  public abstract void removeAllProperties();

  public abstract View getView();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.Axis JD-Core Version: 0.5.4
 */