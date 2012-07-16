package org.palo.viewapi;

import org.palo.api.exceptions.PaloIOException;

public abstract interface View extends ParameterizedGuardedObject
{
  public abstract String getName();

  public abstract Account getAccount();

  public abstract String getCubeId();

  public abstract String getDatabaseId();

  public abstract String getDefinition();

  public abstract CubeView createCubeView(AuthUser paramAuthUser, String paramString)
    throws PaloIOException;

  public abstract CubeView getCubeView();

  public abstract void setCubeView(CubeView paramCubeView);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.View
 * JD-Core Version:    0.5.4
 */