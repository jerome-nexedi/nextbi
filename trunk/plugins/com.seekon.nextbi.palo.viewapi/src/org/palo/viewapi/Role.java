package org.palo.viewapi;

public abstract interface Role extends DomainObject {
  public static final String ADMIN = "ADMIN";

  public static final String OWNER = "OWNER";

  public abstract String getName();

  public abstract String getDescription();

  public abstract void setDescription(String paramString);

  public abstract Right getPermission();

  public abstract boolean hasPermission(Right paramRight);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.Role JD-Core Version: 0.5.4
 */