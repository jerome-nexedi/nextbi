/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.palo.viewapi.Report;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.IReportViewManagement;

/*     */
/*     */final class ReportViewAssociation extends AssociationTableMapper
/*     */implements IReportViewManagement
/*     */{
  /* 59 */private static final String TABLE = DbService
    .getQuery("ReportsViewsAssociation.tableName");

  /* 60 */private static final String COLUMNS = DbService
    .getQuery("ReportsViewsAssociation.columns");

  /* 61 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "ReportsViewsAssociation.createTable", new String[] { TABLE });

  /* 62 */private static final String INSERT_STMT = DbService.getQuery(
    "ReportsViewsAssociation.insert", new String[] { TABLE });

  /* 63 */private static final String FIND_BY_REPORT_STMT = DbService.getQuery(
    "ReportsViewsAssociation.findByReport", new String[] { COLUMNS, TABLE });

  /* 64 */private static final String FIND_BY_VIEW_STMT = DbService.getQuery(
    "ReportsViewsAssociation.findByView", new String[] { COLUMNS, TABLE });

  /* 65 */private static final String UPDATE_STMT = DbService.getQuery(
    "ReportsViewsAssociation.update", new String[] { TABLE });

  /* 66 */private static final String DELETE_STMT = DbService.getQuery(
    "ReportsViewsAssociation.delete", new String[] { TABLE });

  /* 67 */private static final String DELETE_REPORT_STMT = DbService.getQuery(
    "ReportsViewsAssociation.deleteReport", new String[] { TABLE });

  /* 68 */private static final String DELETE_VIEW_STMT = DbService.getQuery(
    "ReportsViewsAssociation.deleteView", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getViews(Report report) throws SQLException
  /*     */{
    /* 72 */return getViews(report.getId());
    /*     */}

  /*     */public final List<String> getViews(String reportId) throws SQLException {
    /* 75 */PreparedStatement stmt = null;
    /* 76 */ResultSet results = null;
    /* 77 */List views = new ArrayList();
    /* 78 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 80 */stmt = connection.prepareStatement(FIND_BY_REPORT_STMT);
      /* 81 */stmt.setString(1, reportId);
      /* 82 */results = stmt.executeQuery();
      /* 83 */while (results.next()) {
        /* 84 */String id = results.getString(3);
        /* 85 */if ((id != null) && (!views.contains(id)))
          /* 86 */views.add(id);
        /*     */}
      /*     */} finally {
      /* 89 */cleanUp(stmt, results);
      /*     */}
    /* 91 */return views;
    /*     */}

  /*     */
  /*     */public final List<String> getReports(View view) throws SQLException
  /*     */{
    /* 96 */PreparedStatement stmt = null;
    /* 97 */ResultSet results = null;
    /* 98 */List reports = new ArrayList();
    /* 99 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 101 */stmt = connection.prepareStatement(FIND_BY_VIEW_STMT);
      /* 102 */stmt.setString(1, view.getId());
      /* 103 */results = stmt.executeQuery();
      /* 104 */while (results.next()) {
        /* 105 */String id = results.getString(2);
        /* 106 */if ((id != null) && (!reports.contains(id)))
          /* 107 */reports.add(id);
        /*     */}
      /*     */} finally {
      /* 110 */cleanUp(stmt, results);
      /*     */}
    /* 112 */return reports;
    /*     */}

  /*     */
  /*     */public final void delete(Report report) throws SQLException
  /*     */{
    /* 117 */delete(report, DELETE_REPORT_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(View view) throws SQLException
  /*     */{
    /* 122 */delete(view, DELETE_VIEW_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 126 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 130 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String updateStatement() {
    /* 134 */return UPDATE_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 138 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 142 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name:
 * org.palo.viewapi.internal.dbmappers.ReportViewAssociation JD-Core Version:
 * 0.5.4
 */