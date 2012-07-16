package org.palo.api;

import com.tensegrity.palojava.ElementInfo;

public abstract interface Element extends PaloObject
{
  public static final int ELEMENTTYPE_NUMERIC = 0;
  public static final int ELEMENTTYPE_STRING = 1;
  public static final int ELEMENTTYPE_CONSOLIDATED = 2;
  public static final int ELEMENTTYPE_RULE = 3;
  public static final String ELEMENTTYPE_NUMERIC_STRING = "Numeric";
  public static final String ELEMENTTYPE_STRING_STRING = "String";
  public static final String ELEMENTTYPE_CONSOLIDATED_STRING = "Consolidated";
  public static final String ELEMENTTYPE_RULE_STRING = "Rule";

  public abstract String getName();

  /** @deprecated */
  public abstract Dimension getDimension();

  public abstract Hierarchy getHierarchy();

  public abstract int getType();

  public abstract void setType(int paramInt);

  public abstract String getTypeAsString();

  public abstract int getDepth();

  public abstract int getLevel();

  public abstract void rename(String paramString);

  public abstract int getConsolidationCount();

  public abstract Consolidation getConsolidationAt(int paramInt);

  public abstract Consolidation[] getConsolidations();

  public abstract void updateConsolidations(Consolidation[] paramArrayOfConsolidation);

  public abstract int getParentCount();

  public abstract Element[] getParents();

  public abstract int getChildCount();

  public abstract Element[] getChildren();

  public abstract int getPosition();

  public abstract void move(int paramInt);

  public abstract ElementInfo getInfo();

  public abstract Object getAttributeValue(Attribute paramAttribute);

  public abstract Object[] getAttributeValues();

  public abstract void setAttributeValue(Attribute paramAttribute, Object paramObject);

  public abstract void setAttributeValues(Attribute[] paramArrayOfAttribute, Object[] paramArrayOfObject);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.Element
 * JD-Core Version:    0.5.4
 */