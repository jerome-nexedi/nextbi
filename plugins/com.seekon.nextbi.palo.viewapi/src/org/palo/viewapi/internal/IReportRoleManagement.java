package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;
import org.palo.viewapi.Report;
import org.palo.viewapi.Role;

public abstract interface IReportRoleManagement extends IAssociationManagement
{
  public abstract List<String> getReports(Role paramRole)
    throws SQLException;

  public abstract List<String> getRoles(Report paramReport)
    throws SQLException;

  public abstract List<String> getRoles(String paramString)
    throws SQLException;

  public abstract void delete(Role paramRole)
    throws SQLException;

  public abstract void delete(Report paramReport)
    throws SQLException;
}

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.IReportRoleManagement
 * JD-Core Version:    0.5.4
 */