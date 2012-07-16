package org.palo.viewapi;

import java.util.List;
import org.palo.viewapi.services.Service;

public abstract interface AuthUser extends User
{
  public abstract List<Account> getAccounts();

  public abstract List<Group> getGroups();

  public abstract boolean isMemberOf(Group paramGroup);

  public abstract List<Role> getRoles();

  public abstract boolean hasRole(Role paramRole);

  public abstract boolean hasPermission(Right paramRight, GuardedObject paramGuardedObject);

  public abstract boolean hasPermissionIgnoreOwner(Right paramRight, GuardedObject paramGuardedObject);

  public abstract boolean hasPermission(Right paramRight, Class<? extends Service> paramClass);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.AuthUser
 * JD-Core Version:    0.5.4
 */