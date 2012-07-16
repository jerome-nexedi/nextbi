package com.tensegrity.palojava;

/** @deprecated */
public abstract interface VariableInfo extends PaloInfo
{
  public static final int VAR_TYPE_UNKNOWN = 0;
  public static final int VAR_TYPE_MEMBER = 1;
  public static final int VAR_TYPE_NUMERIC = 2;
  public static final int VAR_TYPE_HIERARCHY = 3;
  public static final int VAR_PROC_TYPE_UNKNOWN = 0;
  public static final int VAR_PROC_TYPE_USER_INPUT = 1;
  public static final int VAR_SELECTION_TYPE_UNKNOWN = 0;
  public static final int VAR_SELECTION_TYPE_VALUE = 1;
  public static final int VAR_SELECTION_TYPE_INTERVAL = 2;
  public static final int VAR_SELECTION_TYPE_COMPLEX = 3;
  public static final int VAR_INPUT_TYPE_OPTIONAL = 0;
  public static final int VAR_INPUT_TYPE_MANDATORY = 1;
  public static final int VAR_INPUT_TYPE_MANDATORY_NOT_INITIAL = 2;
  public static final int VAR_INPUT_TYPE_UNKNOWN = 3;

  public abstract String getName();

  public abstract DimensionInfo getElementDimension();

  public abstract int getSelectionType();

  public abstract int getInputType();

  public abstract String getDataType();

  public abstract void setValue(ElementInfo paramElementInfo);

  public abstract void setValue(String paramString);

  public abstract void setInterval(ElementInfo paramElementInfo1, ElementInfo paramElementInfo2);

  public abstract void setInterval(String paramString1, String paramString2);

  public abstract String getText();

  public abstract void setText(String paramString);

  public abstract ElementInfo getValue();

  public abstract ElementInfo[] getInterval();

  public abstract ElementInfo[] getSelectedElements();

  public abstract void setSelectedElements(String[] paramArrayOfString);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.VariableInfo
 * JD-Core Version:    0.5.4
 */