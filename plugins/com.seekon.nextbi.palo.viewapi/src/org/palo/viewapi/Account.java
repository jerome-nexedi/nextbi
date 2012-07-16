package org.palo.viewapi;

public abstract interface Account extends DomainObject {
  public abstract String getLoginName();

  public abstract String getPassword();

  public abstract PaloConnection getConnection();

  public abstract AuthUser getUser();

  public abstract void logout();

  public abstract boolean isLoggedIn();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.Account JD-Core Version: 0.5.4
 */