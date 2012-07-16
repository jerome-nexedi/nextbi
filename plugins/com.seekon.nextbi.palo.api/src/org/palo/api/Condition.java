package org.palo.api;

public abstract interface Condition {
  public static final String EQ = "==";

  public static final String LT = "<";

  public static final String GT = ">";

  public static final String LTE = "<=";

  public static final String GTE = ">=";

  public static final String NEQ = "!=";

  public abstract void setValue(double paramDouble);

  public abstract void setValue(String paramString);

  public abstract String getValue();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.Condition JD-Core Version: 0.5.4
 */