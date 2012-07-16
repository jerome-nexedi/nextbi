package org.palo.api;

public abstract interface CubeView extends NamedEntity
{
  public static final String PROPERTY_ID_HIDE_EMPTY = "hideEmpty";
  public static final String PROPERTY_ID_REVERSE_HORIZONTAL_LAYOUT = "reverseHorizontal";
  public static final String PROPERTY_ID_REVERSE_VERTICAL_LAYOUT = "reverseVertical";
  public static final String PROPERTY_ID_SHOW_RULES = "showRules";

  public abstract String getId();

  public abstract Cube getCube();

  public abstract void setName(String paramString);

  public abstract String getDescription();

  public abstract void setDescription(String paramString);

  /** @deprecated */
  public abstract boolean isHideEmpty();

  /** @deprecated */
  public abstract void setHideEmpty(boolean paramBoolean);

  public abstract String getRawDefinition();

  public abstract Axis addAxis(String paramString1, String paramString2);

  public abstract void removeAxis(Axis paramAxis);

  public abstract Axis[] getAxes();

  public abstract Axis getAxis(String paramString);

  public abstract void save();

  public abstract void addProperty(String paramString1, String paramString2);

  public abstract void addProperty(Property paramProperty);

  public abstract void removeProperty(String paramString);

  public abstract void removeProperty(Property paramProperty);

  public abstract String[] getProperties();

  public abstract String getPropertyValue(String paramString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.CubeView
 * JD-Core Version:    0.5.4
 */