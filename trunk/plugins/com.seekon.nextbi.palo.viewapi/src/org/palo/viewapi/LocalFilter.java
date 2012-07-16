package org.palo.viewapi;

import org.palo.api.Element;
import org.palo.api.ElementNode;

public abstract interface LocalFilter
{
  public abstract boolean hasVisibleElements();

  public abstract void clear();

  public abstract void addVisibleElement(ElementNode paramElementNode);

  public abstract void addVisibleElement(ElementNode paramElementNode, int paramInt);

  public abstract void removeVisibleElement(ElementNode paramElementNode);

  public abstract void setVisibleElements(ElementNode[] paramArrayOfElementNode);

  public abstract ElementNode[] getVisibleElements();

  public abstract boolean isVisible(Element paramElement);

  public abstract ElementNode findElementNode(ElementNode paramElementNode);

  public abstract int indexOf(ElementNode paramElementNode);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.LocalFilter
 * JD-Core Version:    0.5.4
 */