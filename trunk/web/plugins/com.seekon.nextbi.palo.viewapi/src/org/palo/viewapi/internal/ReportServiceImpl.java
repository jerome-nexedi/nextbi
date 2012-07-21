/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.HashSet; /*     */
import java.util.List; /*     */
import java.util.Set; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.Report; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.User; /*     */
import org.palo.viewapi.View; /*     */
import org.palo.viewapi.exceptions.OperationFailedException; /*     */
import org.palo.viewapi.services.ReportService;

/*     */
/*     */final class ReportServiceImpl extends InternalService
/*     */implements ReportService
/*     */{
  /*     */ReportServiceImpl(AuthUser user)
  /*     */{
    /* 62 */super(user);
    /*     */}

  /*     */
  /*     */public final Report createReport(String name)
    throws OperationFailedException
  /*     */{
    /* 67 */AccessController.checkPermission(Right.CREATE, this.user);
    /*     */try {
      /* 69 */ReportImpl report = new ReportImpl(null);
      /* 70 */report.setName(name);
      /* 71 */report.setOwner(this.user);
      /* 72 */getReportManagement().insert(report);
      /* 73 */return report;
      /*     */} catch (SQLException e) {
      /* 75 */throw new OperationFailedException("Failed to create report", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void delete(Report report) throws OperationFailedException {
    /* 80 */AccessController.checkPermission(Right.DELETE, report, this.user);
    /*     */try {
      /* 82 */getReportManagement().delete(report);
      /*     */} catch (SQLException e) {
      /* 84 */throw new OperationFailedException("Failed to delete report", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final boolean doesReportExist(String name) {
    /* 89 */return getReportByName(name) != null;
    /*     */}

  /*     */
  /*     */public final void save(Report report) throws OperationFailedException {
    /* 93 */AccessController.checkPermission(Right.WRITE, report, this.user);
    /*     */try {
      /* 95 */getReportManagement().update(report);
      /*     */} catch (SQLException e) {
      /* 97 */throw new OperationFailedException("Failed to save report", e);
      /*     */}
    /*     */}

  /*     */
  /*     */public final List<Report> getReports() {
    /* 102 */AccessController.checkPermission(Right.READ, this.user);
    /* 103 */IReportManagement reportMgmt = getReportManagement();
    /* 104 */Set reports = new HashSet();
    /* 105 */List<Role> roles = this.user.getRoles();
    /* 106 */for (Role role : roles)
      /*     */try
      /*     */{
        /* 109 */if (role.hasPermission(Right.READ))
          /* 110 */reports.addAll(reportMgmt.findReports(role));
        /*     */}
      /*     */catch (SQLException localSQLException)
      /*     */{
        /*     */}
    /*     */try {
      /* 116 */reports.addAll(reportMgmt.findReports(this.user));
      /*     */} catch (SQLException localSQLException1) {
      /*     */}
    /* 119 */return new ArrayList(reports);
    /*     */}

  /*     */
  /*     */public final Report getReport(String id) {
    /* 123 */AccessController.checkPermission(Right.READ, this.user);
    /*     */try {
      /* 125 */return (Report) getReportManagement().find(id);
      /*     */}
    /*     */catch (SQLException localSQLException) {
      /*     */}
    /* 129 */return null;
    /*     */}

  /*     */
  /*     */public final Report getReportByName(String name) {
    /* 133 */AccessController.checkPermission(Right.READ, this.user);
    /*     */try {
      /* 135 */return (Report) getReportManagement().findByName(name);
      /*     */}
    /*     */catch (SQLException localSQLException) {
      /*     */}
    /* 139 */return null;
    /*     */}

  /*     */
  /*     */public final void add(View view, Report toReport)
  /*     */throws OperationFailedException
  /*     */{
    /* 146 */if (!toReport.contains(view)) {
      /* 147 */AccessController.checkPermission(Right.WRITE, toReport, this.user);
      /* 148 */ReportImpl report = (ReportImpl) toReport;
      /* 149 */report.add(view);
      /* 150 */save(report);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void remove(View view, Report fromReport)
    throws OperationFailedException
  /*     */{
    /* 156 */if (fromReport.contains(view)) {
      /* 157 */AccessController.checkPermission(Right.WRITE, fromReport, this.user);
      /* 158 */ReportImpl report = (ReportImpl) fromReport;
      /* 159 */report.remove(view);
      /* 160 */save(report);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void add(Role role, Report toReport)
    throws OperationFailedException
  /*     */{
    /* 166 */if (!toReport.hasRole(role)) {
      /* 167 */AccessController.checkPermission(Right.WRITE, toReport, this.user);
      /* 168 */ReportImpl report = (ReportImpl) toReport;
      /* 169 */report.add(role);
      /* 170 */save(report);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void remove(Role role, Report fromReport)
    throws OperationFailedException {
    /* 175 */if (fromReport.hasRole(role)) {
      /* 176 */AccessController.checkPermission(Right.WRITE, fromReport, this.user);
      /* 177 */ReportImpl report = (ReportImpl) fromReport;
      /* 178 */report.remove(role);
      /* 179 */save(report);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void setDescription(String description, Report ofReport)
  /*     */{
    /* 188 */AccessController.checkPermission(Right.WRITE, ofReport, this.user);
    /* 189 */ReportImpl report = (ReportImpl) ofReport;
    /* 190 */report.setDescription(description);
    /*     */}

  /*     */
  /*     */public final void setName(String name, Report ofReport) {
    /* 194 */AccessController.checkPermission(Right.WRITE, ofReport, this.user);
    /* 195 */ReportImpl report = (ReportImpl) ofReport;
    /* 196 */report.setName(name);
    /*     */}

  /*     */
  /*     */public final void setOwner(User owner, Report ofReport) {
    /* 200 */AccessController.checkPermission(Right.WRITE, ofReport, this.user);
    /* 201 */ReportImpl report = (ReportImpl) ofReport;
    /* 202 */report.setOwner(owner);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.ReportServiceImpl JD-Core
 * Version: 0.5.4
 */