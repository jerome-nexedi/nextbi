/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import org.palo.viewapi.DomainObject; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.DomainObjectImpl; /*     */
import org.palo.viewapi.internal.IDomainObjectManagement;

/*     */
/*     */abstract class AbstractMapper extends Mapper
/*     */implements IDomainObjectManagement
/*     */{
  /* 55 */private final String ID_STMT = DbService.getQuery("identity");

  /*     */
  /* 57 */protected final DomainObjectCache cache = new DomainObjectCache();

  /*     */
  /*     */protected abstract String findStatement();

  /*     */
  /*     */protected abstract String findByNameStatement();

  /*     */
  /*     */protected abstract String insertStatement();

  /*     */
  /*     */protected abstract String deleteStatement();

  /*     */
  /*     */protected abstract void deleteAssociations(DomainObject paramDomainObject)
    throws SQLException;

  /*     */
  /*     */protected abstract DomainObject doLoad(String paramString,
    ResultSet paramResultSet) throws SQLException;

  /*     */
  /*     */protected abstract void doInsert(DomainObject paramDomainObject,
    PreparedStatement paramPreparedStatement) throws SQLException;

  /*     */
  /*     */public final void reset()
  /*     */{
    /* 75 */this.cache.clear();
    /*     */}

  /*     */
  /*     */public final DomainObject find(String id) throws SQLException {
    /* 79 */DomainObject obj = this.cache.get(id);
    /* 80 */if (obj != null) {
      /* 81 */return obj;
      /*     */}
    /* 83 */PreparedStatement stmt = null;
    /* 84 */ResultSet results = null;
    /* 85 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 87 */stmt = connection.prepareStatement(findStatement());
      /* 88 */stmt.setString(1, id);
      /* 89 */results = stmt.executeQuery();
      /* 90 */if (results.next())
        /* 91 */obj = load(results);
      /* 92 */return obj;
      /*     */} finally {
      /* 94 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final DomainObject findByName(String name) throws SQLException {
    /* 99 */PreparedStatement stmt = null;
    /* 100 */ResultSet results = null;
    /* 101 */DomainObject obj = null;
    /* 102 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 104 */stmt = connection.prepareStatement(findByNameStatement());
      /* 105 */stmt.setString(1, name);
      /* 106 */results = stmt.executeQuery();
      /* 107 */if (results.next())
        /* 108 */obj = load(results);
      /*     */} finally {
      /* 110 */cleanUp(stmt, results);
      /*     */}
    /* 112 */return obj;
    /*     */}

  /*     */
  /*     */public synchronized void insert(DomainObject obj) throws SQLException {
    /* 116 */PreparedStatement stmt = null;
    /* 117 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 119 */stmt = connection.prepareStatement(insertStatement());
      /* 120 */doInsert(obj, stmt);
      /* 121 */stmt.execute();
      /* 122 */((DomainObjectImpl) obj).setId(getId(connection));
      /* 123 */this.cache.add(obj);
      /*     */} finally {
      /* 125 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void delete(DomainObject obj) throws SQLException {
    /* 130 */if (obj == null) {
      /* 131 */return;
      /*     */}
    /* 133 */PreparedStatement stmt = null;
    /* 134 */Connection connection = DbService.getConnection();
    /*     */try
    /*     */{
      /* 137 */deleteAssociations(obj);
      /*     */
      /* 139 */stmt = connection.prepareStatement(deleteStatement());
      /* 140 */stmt.setString(1, obj.getId());
      /* 141 */stmt.execute();
      /* 142 */this.cache.remove(obj);
      /*     */} finally {
      /* 144 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected DomainObject load(ResultSet rs) throws SQLException {
    /* 149 */String id = rs.getString(1);
    /* 150 */if (this.cache.contains(id))
      /* 151 */return this.cache.get(id);
    /* 152 */DomainObject obj = doLoad(id, rs);
    /* 153 */this.cache.add(obj);
    /* 154 */return obj;
    /*     */}

  /*     */
  /*     */private final String getId(Connection connection) throws SQLException {
    /* 158 */Long id = Long.valueOf(-1L);
    /* 159 */PreparedStatement idStmt = connection.prepareStatement(this.ID_STMT);
    /* 160 */ResultSet rs = idStmt.executeQuery();
    /*     */try {
      /* 162 */if (rs.next())
        /* 163 */id = Long.valueOf(rs.getLong(1));
      /* 164 */if (id.longValue() == -1L)
        /* 165 */throw new SQLException("Failed to receive id!");
      /* 166 */return Long.toString(id.longValue());
      /*     */} finally {
      /* 168 */cleanUp(idStmt, rs);
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.AbstractMapper
 * JD-Core Version: 0.5.4
 */