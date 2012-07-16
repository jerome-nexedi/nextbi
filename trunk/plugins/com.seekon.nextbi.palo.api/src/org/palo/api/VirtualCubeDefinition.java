package org.palo.api;

/** @deprecated */
public abstract interface VirtualCubeDefinition
{
  public abstract Cube getSourceCube();

  public abstract String getName();

  public abstract VirtualDimensionDefinition[] getVirtualDimensionDefinitions();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.VirtualCubeDefinition
 * JD-Core Version:    0.5.4
 */