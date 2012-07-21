package org.palo.api;

public abstract interface HierarchyFilter {
  public abstract void init(Hierarchy paramHierarchy);

  public abstract boolean acceptElement(Element paramElement);

  public abstract boolean isFlat();

  public abstract ElementNode[] postprocessRootNodes(
    ElementNode[] paramArrayOfElementNode);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.HierarchyFilter JD-Core Version: 0.5.4
 */