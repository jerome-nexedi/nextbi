package org.palo.viewapi;

import java.util.List;

public abstract interface GuardedObject extends DomainObject
{
  public abstract List<Role> getRoles();

  public abstract boolean hasRole(Role paramRole);

  public abstract User getOwner();

  public abstract boolean isOwner(User paramUser);
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.GuardedObject
 * JD-Core Version:    0.5.4
 */