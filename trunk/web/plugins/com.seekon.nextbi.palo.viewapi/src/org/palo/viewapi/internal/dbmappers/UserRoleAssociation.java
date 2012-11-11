/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.IUserRoleManagement;

/*     */
/*     */final class UserRoleAssociation extends AssociationTableMapper
/*     */implements IUserRoleManagement
/*     */{
  /* 58 */private static final String TABLE = DbService
    .getQuery("UsersRolesAssociation.tableName");

  /* 59 */private static final String COLUMNS = DbService
    .getQuery("UsersRolesAssociation.columns");

  /* 60 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "UsersRolesAssociation.createTable", new String[] { TABLE });

  /* 61 */private static final String INSERT_STMT = DbService.getQuery(
    "UsersRolesAssociation.insert", new String[] { TABLE });

  /* 62 */private static final String FIND_BY_ROLE_STMT = DbService.getQuery(
    "UsersRolesAssociation.findByRole", new String[] { COLUMNS, TABLE });

  /* 63 */private static final String FIND_BY_USER_STMT = DbService.getQuery(
    "UsersRolesAssociation.findByUser", new String[] { COLUMNS, TABLE });

  /* 64 */private static final String UPDATE_STMT = DbService.getQuery(
    "UsersRolesAssociation.update", new String[] { TABLE });

  /* 65 */private static final String DELETE_STMT = DbService.getQuery(
    "UsersRolesAssociation.delete", new String[] { TABLE });

  /* 66 */private static final String DELETE_ROLE_STMT = DbService.getQuery(
    "UsersRolesAssociation.deleteRole", new String[] { TABLE });

  /* 67 */private static final String DELETE_USER_STMT = DbService.getQuery(
    "UsersRolesAssociation.deleteUser", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getRoles(User user) throws SQLException
  /*     */{
    /* 71 */return getRoles(user.getId());
    /*     */}

  /*     */public final List<String> getRoles(String userId) throws SQLException {
    /* 74 */PreparedStatement stmt = null;
    /* 75 */ResultSet results = null;
    /* 76 */List roles = new ArrayList();
    /* 77 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 79 */stmt = connection.prepareStatement(FIND_BY_USER_STMT);
      /* 80 */stmt.setString(1, userId);
      /* 81 */results = stmt.executeQuery();
      /* 82 */while (results.next()) {
        /* 83 */String id = results.getString(3);
        /* 84 */if ((id != null) && (!roles.contains(id)))
          /* 85 */roles.add(id);
        /*     */}
      /*     */} finally {
      /* 88 */cleanUp(stmt, results);
      /*     */}
    /* 90 */return roles;
    /*     */}

  /*     */
  /*     */public final List<String> getUsers(Role role) throws SQLException
  /*     */{
    /* 95 */return getUsers(role.getId());
    /*     */}

  /*     */public final List<String> getUsers(String roleId) throws SQLException {
    /* 98 */PreparedStatement stmt = null;
    /* 99 */ResultSet results = null;
    /* 100 */List users = new ArrayList();
    /* 101 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 103 */stmt = connection.prepareStatement(FIND_BY_ROLE_STMT);
      /* 104 */stmt.setString(1, roleId);
      /* 105 */results = stmt.executeQuery();
      /* 106 */while (results.next()) {
        /* 107 */String id = results.getString(2);
        /* 108 */if ((id != null) && (!users.contains(id)))
          /* 109 */users.add(id);
        /*     */}
      /*     */} finally {
      /* 112 */cleanUp(stmt, results);
      /*     */}
    /* 114 */return users;
    /*     */}

  /*     */
  /*     */public final void delete(Role role) throws SQLException {
    /* 118 */delete(role, DELETE_ROLE_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(User user) throws SQLException
  /*     */{
    /* 123 */delete(user, DELETE_USER_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 127 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 131 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String updateStatement() {
    /* 135 */return UPDATE_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 139 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 143 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.UserRoleAssociation
 * JD-Core Version: 0.5.4
 */