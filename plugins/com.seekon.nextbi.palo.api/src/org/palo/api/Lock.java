package org.palo.api;

/** @deprecated */
public abstract interface Lock
{
  public abstract String getId();

  public abstract Cell[] getArea();

  public abstract int getSteps();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Lock
 * JD-Core Version:    0.5.4
 */