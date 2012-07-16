package org.palo.api;

public abstract interface ExportContext {
  public static final String OR = "or";

  public static final String XOR = "xor";

  public static final String AND = "and";

  public static final int TYPE_BOTH = 0;

  public static final int TYPE_NUMERIC = 1;

  public static final int TYPE_STRING = 2;

  public abstract void setBlocksize(int paramInt);

  public abstract int getBlocksize();

  public abstract void setType(int paramInt);

  public abstract int getType();

  public abstract void setBaseCellsOnly(boolean paramBoolean);

  public abstract boolean isBaseCellsOnly();

  public abstract void setIgnoreEmptyCells(boolean paramBoolean);

  public abstract boolean ignoreEmptyCells();

  public abstract void setUseRules(boolean paramBoolean);

  public abstract boolean isUseRules();

  public abstract void setCellsArea(Element[][] paramArrayOfElement);

  public abstract Element[][] getCellsArea();

  public abstract void setExportAfter(Element[] paramArrayOfElement);

  public abstract Element[] getExportAfter();

  public abstract Condition createCondition(String paramString, double paramDouble);

  public abstract Condition createCondition(String paramString1, String paramString2);

  public abstract void setCondition(Condition paramCondition);

  public abstract void setCombinedCondition(Condition paramCondition1,
    Condition paramCondition2, String paramString);

  public abstract String getConditionRepresentation();

  public abstract void reset();

  public abstract double getProgress();

  public abstract void setProgress(double paramDouble);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.ExportContext JD-Core Version: 0.5.4
 */