package org.palo.api;

/** @deprecated */
public abstract interface DimensionFilter
{
  public abstract void init(Dimension paramDimension);

  public abstract boolean acceptElement(Element paramElement);

  public abstract boolean isFlat();

  public abstract ElementNode[] postprocessRootNodes(ElementNode[] paramArrayOfElementNode);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.DimensionFilter
 * JD-Core Version:    0.5.4
 */