package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Group;
import org.palo.viewapi.Role;

public abstract interface IGroupManagement extends IDomainObjectManagement
{
  public abstract List<Group> findAll()
    throws SQLException;

  public abstract List<Group> findAllGroupsFor(Role paramRole)
    throws SQLException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.IGroupManagement
 * JD-Core Version:    0.5.4
 */