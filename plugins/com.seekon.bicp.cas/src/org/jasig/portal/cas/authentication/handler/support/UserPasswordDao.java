package org.jasig.portal.cas.authentication.handler.support;

public abstract interface UserPasswordDao {
  public abstract String getPasswordHash(String paramString);
}