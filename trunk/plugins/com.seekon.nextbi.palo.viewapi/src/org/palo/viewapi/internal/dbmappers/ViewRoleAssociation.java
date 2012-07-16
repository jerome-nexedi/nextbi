/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.View; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.IViewRoleManagement;

/*     */
/*     */final class ViewRoleAssociation extends AssociationTableMapper
/*     */implements IViewRoleManagement
/*     */{
  /* 60 */private static final String TABLE = DbService
    .getQuery("ViewsRolesAssociation.tableName");

  /* 61 */private static final String COLUMNS = DbService
    .getQuery("ViewsRolesAssociation.columns");

  /* 62 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "ViewsRolesAssociation.createTable", new String[] { TABLE });

  /* 63 */private static final String INSERT_STMT = DbService.getQuery(
    "ViewsRolesAssociation.insert", new String[] { TABLE });

  /* 64 */private static final String FIND_BY_VIEW_STMT = DbService.getQuery(
    "ViewsRolesAssociation.findByView", new String[] { COLUMNS, TABLE });

  /* 65 */private static final String FIND_BY_ROLE_STMT = DbService.getQuery(
    "ViewsRolesAssociation.findByRole", new String[] { COLUMNS, TABLE });

  /* 66 */private static final String UPDATE_STMT = DbService.getQuery(
    "ViewsRolesAssociation.update", new String[] { TABLE });

  /* 67 */private static final String DELETE_STMT = DbService.getQuery(
    "ViewsRolesAssociation.delete", new String[] { TABLE });

  /* 68 */private static final String DELETE_ROLE_STMT = DbService.getQuery(
    "ViewsRolesAssociation.deleteRole", new String[] { TABLE });

  /* 69 */private static final String DELETE_VIEW_STMT = DbService.getQuery(
    "ViewsRolesAssociation.deleteView", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getRoles(View view) throws SQLException
  /*     */{
    /* 73 */return getRoles(view.getId());
    /*     */}

  /*     */public final List<String> getRoles(String viewId) throws SQLException {
    /* 76 */Connection connection = DbService.getConnection();
    /* 77 */PreparedStatement stmt = null;
    /* 78 */ResultSet results = null;
    /* 79 */List roles = new ArrayList();
    /*     */try {
      /* 81 */stmt = connection.prepareStatement(FIND_BY_VIEW_STMT);
      /* 82 */stmt.setString(1, viewId);
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
  /*     */public final List<String> getViews(Role role) throws SQLException {
    /* 96 */Connection connection = DbService.getConnection();
    /* 97 */PreparedStatement stmt = null;
    /* 98 */ResultSet results = null;
    /* 99 */List views = new ArrayList();
    /*     */try {
      /* 101 */stmt = connection.prepareStatement(FIND_BY_ROLE_STMT);
      /* 102 */stmt.setString(1, role.getId());
      /* 103 */results = stmt.executeQuery();
      /* 104 */while (results.next()) {
        /* 105 */String id = results.getString(2);
        /* 106 */if ((id != null) && (!views.contains(id)))
          /* 107 */views.add(id);
        /*     */}
      /*     */} finally {
      /* 110 */cleanUp(stmt, results);
      /*     */}
    /* 112 */return views;
    /*     */}

  /*     */
  /*     */public final void delete(Role role) throws SQLException
  /*     */{
    /* 117 */delete(role, DELETE_ROLE_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(View view) throws SQLException {
    /* 121 */delete(view, DELETE_VIEW_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 125 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 129 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String updateStatement() {
    /* 133 */return UPDATE_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 137 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 141 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.ViewRoleAssociation
 * JD-Core Version: 0.5.4
 */