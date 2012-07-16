package org.palo.viewapi.services;

import java.sql.SQLException;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.exceptions.AuthenticationFailedException;

public abstract interface AuthenticationService
{
  public abstract AuthUser authenticate(String paramString1, String paramString2)
    throws AuthenticationFailedException;

  public abstract AuthUser authenticateHash(String paramString1, String paramString2)
    throws AuthenticationFailedException;

  public abstract AuthUser authenticateAdmin()
    throws SQLException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.services.AuthenticationService
 * JD-Core Version:    0.5.4
 */