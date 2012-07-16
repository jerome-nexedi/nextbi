package org.palo.api;

/** @deprecated */
public abstract interface SubsetState {
  public abstract String getId();

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract String getExpression();

  public abstract void setExpression(String paramString);

  public abstract Attribute getSearchAttribute();

  public abstract void setSearchAttribute(Attribute paramAttribute);

  public abstract Element[] getVisibleElements();

  public abstract void addVisibleElment(Element paramElement);

  public abstract void addVisibleElement(Element paramElement, int paramInt);

  public abstract void removeVisibleElement(Element paramElement);

  public abstract void removeAllVisibleElements();

  public abstract String[] getPaths(Element paramElement);

  public abstract void addPath(Element paramElement, String paramString);

  public abstract boolean containsPath(Element paramElement, String paramString);

  public abstract void removePath(Element paramElement, String paramString);

  public abstract int[] getPositions(Element paramElement);

  public abstract boolean isVisible(Element paramElement);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.SubsetState JD-Core Version: 0.5.4
 */