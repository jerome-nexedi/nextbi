package org.palo.viewapi.internal;

import java.sql.SQLException;
import java.util.List;

import org.palo.viewapi.Report;
import org.palo.viewapi.View;

public abstract interface IReportViewManagement extends IAssociationManagement {
  public abstract List<String> getReports(View paramView) throws SQLException;

  public abstract List<String> getViews(Report paramReport) throws SQLException;

  public abstract List<String> getViews(String paramString) throws SQLException;

  public abstract void delete(Report paramReport) throws SQLException;

  public abstract void delete(View paramView) throws SQLException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.IReportViewManagement JD-Core
 * Version: 0.5.4
 */