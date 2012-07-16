package org.palo.viewapi.internal;

import java.sql.SQLException;
import org.palo.viewapi.DomainObject;

public abstract interface IAssociationManagement
{
  public abstract void update(DomainObject paramDomainObject1, DomainObject paramDomainObject2)
    throws SQLException;

  public abstract void insert(DomainObject paramDomainObject1, DomainObject paramDomainObject2)
    throws SQLException;

  public abstract void delete(DomainObject paramDomainObject1, DomainObject paramDomainObject2)
    throws SQLException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.IAssociationManagement
 * JD-Core Version:    0.5.4
 */