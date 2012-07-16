package org.palo.api.ext.subsets;

import java.util.List;
import org.palo.api.Element;
import org.palo.api.ElementNode;

/** @deprecated */
public abstract interface SubsetHandler
{
  public abstract Element[] getVisibleElements();

  public abstract List getVisibleElementsAsList();

  public abstract ElementNode[] getVisibleRootNodes();

  public abstract List getVisibleRootNodesAsList();

  public abstract boolean isFlat();

  public abstract boolean isVisible(Element paramElement);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.subsets.SubsetHandler
 * JD-Core Version:    0.5.4
 */