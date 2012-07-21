package org.palo.viewapi.uimodels.axis;

import org.palo.api.Hierarchy;
import org.palo.api.utils.ElementPath;
import org.palo.viewapi.Axis;
import org.palo.viewapi.AxisHierarchy;
import org.palo.viewapi.uimodels.axis.events.AxisModelListener;

public abstract interface AxisModel {
  public abstract Axis getAxis();

  public abstract void addListener(AxisModelListener paramAxisModelListener);

  public abstract void removeListener(AxisModelListener paramAxisModelListener);

  public abstract void add(AxisHierarchy paramAxisHierarchy);

  public abstract void add(int paramInt, AxisHierarchy paramAxisHierarchy);

  public abstract void remove(AxisHierarchy paramAxisHierarchy);

  public abstract void removeAll();

  public abstract int getAxisHierarchyCount();

  public abstract AxisHierarchy[] getAxisHierarchies();

  public abstract AxisHierarchy getAxisHierarchy(String paramString);

  public abstract void swap(AxisHierarchy paramAxisHierarchy1,
    AxisHierarchy paramAxisHierarchy2);

  public abstract Hierarchy[] getHierarchies();

  public abstract AxisItem[] getRoots();

  public abstract AxisItem getItem(ElementPath paramElementPath);

  public abstract AxisItem getItem(String paramString);

  public abstract AxisItem getItem(String paramString, int paramInt);

  public abstract AxisItem findItem(String paramString1, String paramString2);

  public abstract AxisItem findItem(String paramString1, int paramInt,
    String paramString2);

  public abstract int getMaximumLevel(Hierarchy paramHierarchy);

  public abstract AxisItem getNextSibling(AxisItem paramAxisItem);

  public abstract AxisItem getFirstSibling(AxisItem paramAxisItem);

  public abstract AxisItem getLastSibling(AxisItem paramAxisItem);

  public abstract AxisItem getLastChildInNextHierarchy(AxisItem paramAxisItem);

  public abstract AxisItem getFirstChildInNextHierarchy(AxisItem paramAxisItem);

  public abstract AxisItem[] getSiblings(AxisItem paramAxisItem);

  public abstract void expand(AxisItem paramAxisItem);

  public abstract void collapse(AxisItem paramAxisItem);

  public abstract void showAllParents(boolean paramBoolean);

  public abstract void refresh();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.uimodels.axis.AxisModel JD-Core
 * Version: 0.5.4
 */