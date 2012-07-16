package org.palo.viewapi.services;

import java.util.List;
import org.palo.api.Cube;
import org.palo.viewapi.Account;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.View;
import org.palo.viewapi.exceptions.OperationFailedException;

public abstract interface ViewService extends Service
{
  public abstract View createViewAsSubobject(String paramString1, Cube paramCube, AuthUser paramAuthUser, String paramString2, String paramString3)
    throws OperationFailedException;

  public abstract View createView(String paramString1, Cube paramCube, AuthUser paramAuthUser, String paramString2, String paramString3)
    throws OperationFailedException;

  public abstract boolean doesViewExist(String paramString, Cube paramCube);

  public abstract View getView(String paramString);

  public abstract View getViewByName(String paramString, Cube paramCube);

  public abstract List<View> getViews(Account paramAccount);

  public abstract boolean hasViews(Account paramAccount);

  public abstract void save(View paramView)
    throws OperationFailedException;

  public abstract void delete(View paramView)
    throws OperationFailedException;

  public abstract void add(Role paramRole, View paramView)
    throws OperationFailedException;

  public abstract void remove(Role paramRole, View paramView)
    throws OperationFailedException;

  public abstract void setOwner(User paramUser, View paramView);

  public abstract void setAccount(Account paramAccount, View paramView);

  public abstract void setDatabase(String paramString, View paramView);

  public abstract void setCube(String paramString, View paramView);

  public abstract void setDefinition(String paramString, View paramView);

  public abstract void setName(String paramString, View paramView);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.services.ViewService
 * JD-Core Version:    0.5.4
 */