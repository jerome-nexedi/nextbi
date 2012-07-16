/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.DomainObject; /*     */
import org.palo.viewapi.PaloConnection; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.IAccountManagement; /*     */
import org.palo.viewapi.internal.IConnectionManagement;
import org.palo.viewapi.internal.PaloConnectionImpl;
import org.palo.viewapi.internal.PaloConnectionImpl.Builder;

/*     */
/*     */final class ConnectionMapper extends AbstractMapper
/*     */implements IConnectionManagement
/*     */{
  /* 61 */private static final String TABLE = DbService
    .getQuery("Connections.tableName");

  /* 62 */private static final String COLUMNS = DbService
    .getQuery("Connections.columns");

  /* 63 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "Connections.createTable", new String[] { TABLE });

  /* 64 */private static final String FIND_ALL_STMT = DbService.getQuery(
    "Connections.findAll", new String[] { COLUMNS, TABLE });

  /* 65 */private static final String FIND_BY_ID_STMT = DbService.getQuery(
    "Connections.findById", new String[] { COLUMNS, TABLE });

  /* 66 */private static final String FIND_BY_HOST_SERVICE_STMT = DbService
    .getQuery("Connections.findByHost", new String[] { COLUMNS, TABLE });

  /* 67 */private static final String FIND_BY_NAME_STMT = DbService.getQuery(
    "Connections.findByName", new String[] { COLUMNS, TABLE });

  /* 68 */private static final String INSERT_STMT = DbService.getQuery(
    "Connections.insert", new String[] { TABLE });

  /* 69 */private static final String UPDATE_STMT = DbService.getQuery(
    "Connections.update", new String[] { TABLE });

  /* 70 */private static final String DELETE_STMT = DbService.getQuery(
    "Connections.delete", new String[] { TABLE });

  /*     */
  /*     */public final PaloConnection findBy(String host, String service)
  /*     */{
    /* 74 */PreparedStatement stmt = null;
    /* 75 */ResultSet results = null;
    /* 76 */PaloConnection conn = null;
    /* 77 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 79 */stmt = connection.prepareStatement(FIND_BY_HOST_SERVICE_STMT);
      /* 80 */stmt.setString(1, host);
      /* 81 */stmt.setString(2, service);
      /* 82 */results = stmt.executeQuery();
      /* 83 */if (results.next())
        /* 84 */conn = (PaloConnection) load(results);
      /*     */} catch (SQLException localSQLException) {
      /*     */}
    /*     */finally {
      /* 88 */cleanUp(stmt, results);
      /*     */}
    /* 90 */return conn;
    /*     */}

  /*     */
  /*     */public final List<PaloConnection> findAll() throws SQLException {
    /* 94 */PreparedStatement stmt = null;
    /* 95 */ResultSet results = null;
    /* 96 */List connections = new ArrayList();
    /* 97 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 99 */stmt = connection.prepareStatement(FIND_ALL_STMT);
      /* 100 */results = stmt.executeQuery();
      /* 101 */while (results.next()) {
        /* 102 */PaloConnection paloConn = (PaloConnection) load(results);
        /* 103 */if (paloConn != null)
          /* 104 */connections.add(paloConn);
        /*     */}
      /*     */} finally {
      /* 107 */cleanUp(stmt, results);
      /*     */}
    /* 109 */return connections;
    /*     */}

  /*     */
  /*     */public final void update(DomainObject obj) throws SQLException {
    /* 113 */PaloConnection conn = (PaloConnection) obj;
    /* 114 */PreparedStatement stmt = null;
    /* 115 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 117 */stmt = connection.prepareStatement(UPDATE_STMT);
      /* 118 */stmt.setString(1, conn.getName());
      /* 119 */stmt.setString(2, conn.getHost());
      /* 120 */stmt.setString(3, conn.getService());
      /* 121 */stmt.setInt(4, conn.getType());
      /* 122 */stmt.setString(5, conn.getDescription());
      /* 123 */stmt.setString(6, conn.getId());
      /* 124 */stmt.execute();
      /*     */} finally {
      /* 126 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected final void doInsert(DomainObject obj, PreparedStatement stmt)
  /*     */throws SQLException
  /*     */{
    /* 133 */PaloConnection conn = (PaloConnection) obj;
    /* 134 */stmt.setString(1, conn.getName());
    /* 135 */stmt.setString(2, conn.getHost());
    /* 136 */stmt.setString(3, conn.getService());
    /* 137 */stmt.setInt(4, conn.getType());
    /* 138 */stmt.setString(5, conn.getDescription());
    /*     */}

  /*     */
  /*     */protected final DomainObject doLoad(String id, ResultSet result)
    throws SQLException
  /*     */{
    /* 143 */PaloConnectionImpl.Builder conBuilder = new PaloConnectionImpl.Builder(
      id);
    /* 144 */conBuilder.name(result.getString(2));
    /* 145 */conBuilder.host(result.getString(3));
    /* 146 */conBuilder.service(result.getString(4));
    /* 147 */conBuilder.type(result.getInt(5));
    /* 148 */conBuilder.description(result.getString(6));
    /* 149 */return conBuilder.build();
    /*     */}

  /*     */
  /*     */protected final void deleteAssociations(DomainObject obj)
  /*     */throws SQLException
  /*     */{
    /* 155 */PaloConnection conn = (PaloConnection) obj;
    /* 156 */MapperRegistry.getInstance().getAccountManagement().delete(conn);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 160 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String findStatement() {
    /* 164 */return FIND_BY_ID_STMT;
    /*     */}

  /*     */
  /*     */protected final String findByNameStatement() {
    /* 168 */return FIND_BY_NAME_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 172 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 176 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 180 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.ConnectionMapper
 * JD-Core Version: 0.5.4
 */