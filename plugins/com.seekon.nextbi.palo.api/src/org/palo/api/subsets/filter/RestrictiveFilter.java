package org.palo.api.subsets.filter;

import java.util.Set;
import org.palo.api.Element;
import org.palo.api.subsets.SubsetFilter;

public abstract interface RestrictiveFilter extends SubsetFilter {
  public abstract void filter(Set<Element> paramSet);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.RestrictiveFilter JD-Core
 * Version: 0.5.4
 */