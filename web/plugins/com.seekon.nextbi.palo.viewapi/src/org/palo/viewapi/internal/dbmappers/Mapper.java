/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.palo.viewapi.internal.DbService;

/*     */
/*     */abstract class Mapper
/*     */{
  /*     */Mapper()
  /*     */{
    /* 59 */createTable();
    /*     */}

  /*     */protected abstract String getTableName();

  /*     */
  /*     */protected abstract String createTableStatement();

  /*     */
  /*     */protected final void cleanUp(PreparedStatement stmt) {
    /* 66 */cleanUp(stmt, null);
    /*     */}

  /*     */protected final void cleanUp(PreparedStatement stmt, ResultSet rs) {
    /*     */try {
      /* 70 */if (stmt != null)
        /* 71 */stmt.close();
      /*     */} catch (SQLException localSQLException) {
      /*     */}
    /*     */try {
      /* 74 */if (rs != null)
        /* 75 */rs.close();
      /*     */} catch (SQLException localSQLException1) {
      /*     */}
    /*     */}

  /*     */
  /*     */private final void createTable() {
    /* 80 */String table = getTableName();
    /*     */try
    /*     */{
      /* 83 */if (!existsInMetadata(table))
        /* 84 */return;
      /*     */}
    /*     */catch (SQLException ex) {
      /* 87 */if (exists(table)) {
        /* 88 */return;
        /*     */}
      /*     */
      /*     */try
      /*     */{
        /* 93 */doCreateTable();
        /*     */} catch (SQLException ex0) {
        /* 95 */throw new RuntimeException(
        /* 96 */"Cannot create required database table '" + table + "'!!",
        /* 97 */ex0);
        /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */private final void doCreateTable() throws SQLException {
    /* 102 */PreparedStatement stmt = null;
    /* 103 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 105 */stmt = connection.prepareStatement(createTableStatement());
      /* 106 */stmt.execute();
      /*     */} finally {
      /* 108 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */private final boolean existsInMetadata(String table) throws SQLException {
    /* 113 */Connection connection = DbService.getConnection();
    /* 114 */DatabaseMetaData metaData = connection.getMetaData();
    /* 115 */ResultSet rs = metaData.getTables(null, null, "%", null);
    /* 116 */int colCount = rs.getMetaData().getColumnCount();
    /* 117 */while (rs.next()) {
      /* 118 */for (int i = 1; i <= colCount; ++i) {
        /* 119 */String tbl = rs.getString(i);
        /* 120 */if ((tbl != null) && (tbl.equalsIgnoreCase(table)))
          /* 121 */return true;
        /*     */}
      /*     */}
    /* 124 */return false;
    /*     */}

  /*     */private final boolean exists(String table) {
    /* 127 */PreparedStatement stmt = null;
    /* 128 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 130 */stmt = connection.prepareStatement("SELECT * FROM " + table);
      /* 131 */stmt.execute();
      /* 132 */return true;
      /*     */} catch (SQLException localSQLException) {
      /*     */}
    /*     */finally {
      /* 136 */cleanUp(stmt);
      /*     */}
    /* 138 */return false;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.Mapper JD-Core
 * Version: 0.5.4
 */