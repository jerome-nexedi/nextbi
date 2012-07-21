package org.palo.viewapi;

import org.palo.api.Cube;
import org.palo.api.parameters.ParameterReceiver;
import org.palo.viewapi.uimodels.formats.Format;

public abstract interface CubeView extends DomainObject, CubeViewProperties,
  ParameterReceiver {
  public static final String SELECTION_AXIS = "selected";

  public static final String ROW_AXIS = "rows";

  public static final String COLUMN_AXIS = "cols";

  public static final String PARAMETER_ELEMENT = "Element";

  public abstract Cube getCube();

  public abstract String getName();

  public abstract void setName(String paramString);

  public abstract String getDescription();

  public abstract void setDescription(String paramString);

  public abstract Axis addAxis(String paramString1, String paramString2);

  public abstract void removeAxis(Axis paramAxis);

  public abstract Axis[] getAxes();

  public abstract Axis getAxis(String paramString);

  public abstract CubeView copy();

  public abstract Property<Object> addProperty(String paramString, Object paramObject);

  public abstract Property<Object> getProperty(String paramString);

  public abstract void removeProperty(String paramString);

  public abstract Property<Object>[] getProperties();

  public abstract Object getPropertyValue(String paramString);

  public abstract Format[] getFormats();

  public abstract Format addFormat(String paramString);

  public abstract void addFormat(Format paramFormat);

  public abstract void removeFormat(String paramString);

  public abstract void removeAllFormats();

  public abstract boolean hasFormats();

  public abstract Format getFormat(String paramString);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.CubeView JD-Core Version: 0.5.4
 */