/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.palo.viewapi.DomainObject;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.IAssociationManagement;

/*     */
/*     */abstract class AssociationTableMapper extends Mapper
/*     */implements IAssociationManagement
/*     */{
  /*     */protected abstract String insertStatement();

  /*     */
  /*     */protected abstract String deleteStatement();

  /*     */
  /*     */protected abstract String updateStatement();

  /*     */
  /*     */public void delete(DomainObject obj, DomainObject assoc)
  /*     */throws SQLException
  /*     */{
    /* 62 */Connection connection = DbService.getConnection();
    /* 63 */PreparedStatement stmt = null;
    /*     */try {
      /* 65 */stmt = connection.prepareStatement(deleteStatement());
      /* 66 */stmt.setString(1, obj.getId());
      /* 67 */stmt.setString(2, assoc.getId());
      /* 68 */stmt.execute();
      /*     */} finally {
      /* 70 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */public void delete(DomainObject obj, String sql) throws SQLException {
    /* 75 */if (obj == null) {
      /* 76 */return;
      /*     */}
    /* 78 */Connection connection = DbService.getConnection();
    /* 79 */PreparedStatement stmt = null;
    /*     */try {
      /* 81 */stmt = connection.prepareStatement(sql);
      /* 82 */stmt.setString(1, obj.getId());
      /* 83 */stmt.execute();
      /*     */} finally {
      /* 85 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */public void insert(DomainObject obj, DomainObject assoc)
    throws SQLException {
    /* 90 */Connection connection = DbService.getConnection();
    /* 91 */PreparedStatement stmt = null;
    /*     */try {
      /* 93 */stmt = connection.prepareStatement(insertStatement());
      /* 94 */stmt.setString(1, obj.getId());
      /* 95 */stmt.setString(2, assoc.getId());
      /* 96 */stmt.execute();
      /*     */}
    /*     */finally
    /*     */{
      /* 100 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */public void update(DomainObject obj, DomainObject assoc)
    throws SQLException {
    /* 105 */Connection connection = DbService.getConnection();
    /* 106 */PreparedStatement stmt = null;
    /*     */try {
      /* 108 */stmt = connection.prepareStatement(updateStatement());
      /* 109 */stmt.setString(1, obj.getId());
      /* 110 */stmt.setString(2, assoc.getId());
      /* 111 */stmt.execute();
      /*     */} finally {
      /* 113 */cleanUp(stmt);
      /*     */}
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name:
 * org.palo.viewapi.internal.dbmappers.AssociationTableMapper JD-Core Version:
 * 0.5.4
 */