package org.palo.viewapi;

import java.util.List;

public abstract interface Report extends GuardedObject
{
  public abstract String getName();

  public abstract String getDescription();

  public abstract List<View> getViews();

  public abstract boolean contains(View paramView);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.Report
 * JD-Core Version:    0.5.4
 */