package org.palo.viewapi.services;

import java.util.List;
import org.palo.viewapi.Report;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.View;
import org.palo.viewapi.exceptions.OperationFailedException;

public abstract interface ReportService extends Service {
  public abstract Report createReport(String paramString)
    throws OperationFailedException;

  public abstract Report getReport(String paramString);

  public abstract Report getReportByName(String paramString);

  public abstract List<Report> getReports();

  public abstract void save(Report paramReport) throws OperationFailedException;

  public abstract void delete(Report paramReport) throws OperationFailedException;

  public abstract void setName(String paramString, Report paramReport);

  public abstract void setDescription(String paramString, Report paramReport);

  public abstract void setOwner(User paramUser, Report paramReport);

  public abstract void add(View paramView, Report paramReport)
    throws OperationFailedException;

  public abstract void remove(View paramView, Report paramReport)
    throws OperationFailedException;

  public abstract void add(Role paramRole, Report paramReport)
    throws OperationFailedException;

  public abstract void remove(Role paramRole, Report paramReport)
    throws OperationFailedException;
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.services.ReportService JD-Core Version:
 * 0.5.4
 */