/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.Report; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.IReportRoleManagement;

/*     */
/*     */final class ReportRoleAssociation extends AssociationTableMapper
/*     */implements IReportRoleManagement
/*     */{
  /* 60 */private static final String TABLE = DbService
    .getQuery("ReportsRolesAssociation.tableName");

  /* 61 */private static final String COLUMNS = DbService
    .getQuery("ReportsRolesAssociation.columns");

  /* 62 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "ReportsRolesAssociation.createTable", new String[] { TABLE });

  /* 63 */private static final String INSERT_STMT = DbService.getQuery(
    "ReportsRolesAssociation.insert", new String[] { TABLE });

  /* 64 */private static final String FIND_BY_REPORT_STMT = DbService.getQuery(
    "ReportsRolesAssociation.findByReport", new String[] { COLUMNS, TABLE });

  /* 65 */private static final String FIND_BY_ROLE_STMT = DbService.getQuery(
    "ReportsRolesAssociation.findByRole", new String[] { COLUMNS, TABLE });

  /* 66 */private static final String UPDATE_STMT = DbService.getQuery(
    "ReportsRolesAssociation.update", new String[] { TABLE });

  /* 67 */private static final String DELETE_STMT = DbService.getQuery(
    "ReportsRolesAssociation.delete", new String[] { TABLE });

  /* 68 */private static final String DELETE_REPORT_STMT = DbService.getQuery(
    "ReportsRolesAssociation.deleteReport", new String[] { TABLE });

  /* 69 */private static final String DELETE_ROLE_STMT = DbService.getQuery(
    "ReportsRolesAssociation.deleteRole", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getRoles(Report report) throws SQLException
  /*     */{
    /* 73 */return getRoles(report.getId());
    /*     */}

  /*     */public final List<String> getRoles(String reportId) throws SQLException {
    /* 76 */PreparedStatement stmt = null;
    /* 77 */ResultSet results = null;
    /* 78 */List roles = new ArrayList();
    /* 79 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 81 */stmt = connection.prepareStatement(FIND_BY_REPORT_STMT);
      /* 82 */stmt.setString(1, reportId);
      /* 83 */results = stmt.executeQuery();
      /* 84 */while (results.next()) {
        /* 85 */String id = results.getString(3);
        /* 86 */if ((id != null) && (!roles.contains(id)))
          /* 87 */roles.add(id);
        /*     */}
      /*     */} finally {
      /* 90 */cleanUp(stmt, results);
      /*     */}
    /* 92 */return roles;
    /*     */}

  /*     */
  /*     */public final List<String> getReports(Role role) throws SQLException
  /*     */{
    /* 97 */PreparedStatement stmt = null;
    /* 98 */ResultSet results = null;
    /*     */
    /* 115 */List reports = new ArrayList();
    /* 116 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 118 */stmt = connection.prepareStatement(FIND_BY_ROLE_STMT);
      /* 119 */stmt.setString(1, role.getId());
      /* 120 */results = stmt.executeQuery();
      /* 121 */while (results.next()) {
        /* 122 */String id = results.getString(2);
        /* 123 */if ((id != null) && (!reports.contains(id)))
          /* 124 */reports.add(id);
        /*     */}
      /*     */} finally {
      /* 127 */cleanUp(stmt, results);
      /*     */}
    /* 129 */return reports;
    /*     */}

  /*     */
  /*     */public final void delete(Role role) throws SQLException
  /*     */{
    /* 134 */delete(role, DELETE_ROLE_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(Report report) throws SQLException {
    /* 138 */delete(report, DELETE_REPORT_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 142 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 146 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String updateStatement() {
    /* 150 */return UPDATE_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 154 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 158 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name:
 * org.palo.viewapi.internal.dbmappers.ReportRoleAssociation JD-Core Version:
 * 0.5.4
 */