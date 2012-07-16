package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.PaloConnection;

public abstract interface IConnectionManagement extends IDomainObjectManagement {
  public abstract List<PaloConnection> findAll() throws SQLException;

  public abstract PaloConnection findBy(String paramString1, String paramString2)
    throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IConnectionManagement JD-Core
 * Version: 0.5.4
 */