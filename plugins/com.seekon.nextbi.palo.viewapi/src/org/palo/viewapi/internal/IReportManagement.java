package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Report;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;

public abstract interface IReportManagement extends IDomainObjectManagement
{
  public abstract List<Report> findReports(Role paramRole)
    throws SQLException;

  public abstract List<Report> findReports(User paramUser)
    throws SQLException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.IReportManagement
 * JD-Core Version:    0.5.4
 */