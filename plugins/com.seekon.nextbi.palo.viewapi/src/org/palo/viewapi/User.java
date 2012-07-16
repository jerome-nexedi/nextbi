package org.palo.viewapi;

public abstract interface User extends DomainObject {
  public abstract String getFirstname();

  public abstract String getLastname();

  public abstract String getLoginName();

  public abstract String getPassword();
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.User JD-Core Version: 0.5.4
 */