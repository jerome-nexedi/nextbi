package org.palo.api;

/** @deprecated */
public abstract interface Subset extends NamedEntity {
  public abstract String getId();

  public abstract void setName(String paramString);

  public abstract String getName();

  public abstract void setDescription(String paramString);

  public abstract String getDescription();

  public abstract Attribute getAlias();

  public abstract void setAlias(Attribute paramAttribute);

  /** @deprecated */
  public abstract Dimension getDimension();

  public abstract Hierarchy getHierarchy();

  public abstract void addState(SubsetState paramSubsetState);

  public abstract void removeState(SubsetState paramSubsetState);

  public abstract SubsetState[] getStates();

  public abstract SubsetState getState(String paramString);

  public abstract SubsetState getActiveState();

  public abstract void setActiveState(SubsetState paramSubsetState);

  public abstract void save();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Subset JD-Core Version: 0.5.4
 */