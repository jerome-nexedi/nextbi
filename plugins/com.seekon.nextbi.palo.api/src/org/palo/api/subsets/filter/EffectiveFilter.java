package org.palo.api.subsets.filter;

import org.palo.api.subsets.SubsetFilter;

public abstract interface EffectiveFilter extends SubsetFilter {
  public abstract int[] getEffectiveFilter();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.filter.EffectiveFilter JD-Core Version:
 * 0.5.4
 */