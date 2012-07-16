package org.palo.viewapi;

import java.util.List;

public abstract interface Group extends DomainObject
{
  public abstract String getName();

  public abstract String getDescription();

  public abstract List<User> getUsers();

  public abstract boolean hasMember(User paramUser);

  public abstract List<Role> getRoles();
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.Group
 * JD-Core Version:    0.5.4
 */