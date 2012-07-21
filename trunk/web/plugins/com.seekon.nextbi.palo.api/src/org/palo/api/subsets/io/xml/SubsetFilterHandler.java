package org.palo.api.subsets.io.xml;

import org.palo.api.Dimension;
import org.palo.api.subsets.SubsetFilter;

public abstract interface SubsetFilterHandler {
  public abstract String getXPath();

  public abstract void enter(String paramString);

  public abstract void leave(String paramString1, String paramString2);

  public abstract SubsetFilter createFilter(Dimension paramDimension);

  public abstract void setSubsetVersion(String paramString);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.subsets.io.xml.SubsetFilterHandler JD-Core
 * Version: 0.5.4
 */