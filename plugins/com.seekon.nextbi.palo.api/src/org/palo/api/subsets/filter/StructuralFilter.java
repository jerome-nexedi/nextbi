package org.palo.api.subsets.filter;

import java.util.List;
import java.util.Set;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.subsets.SubsetFilter;

public abstract interface StructuralFilter extends SubsetFilter {
  public abstract void filter(List<ElementNode> paramList, Set<Element> paramSet);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.StructuralFilter JD-Core Version:
 * 0.5.4
 */