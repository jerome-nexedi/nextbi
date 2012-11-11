/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.palo.viewapi.Group;
import org.palo.viewapi.User;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.IUserGroupManagement;

/*     */
/*     */final class UserGroupAssociation extends AssociationTableMapper
/*     */implements IUserGroupManagement
/*     */{
  /* 59 */private static final String TABLE = DbService
    .getQuery("UsersGroupsAssociation.tableName");

  /* 60 */private static final String COLUMNS = DbService
    .getQuery("UsersGroupsAssociation.columns");

  /* 61 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "UsersGroupsAssociation.createTable", new String[] { TABLE });

  /* 62 */private static final String INSERT_STMT = DbService.getQuery(
    "UsersGroupsAssociation.insert", new String[] { TABLE });

  /* 63 */private static final String FIND_BY_GROUP_STMT = DbService.getQuery(
    "UsersGroupsAssociation.findByGroup", new String[] { COLUMNS, TABLE });

  /* 64 */private static final String FIND_BY_USER_STMT = DbService.getQuery(
    "UsersGroupsAssociation.findByUser", new String[] { COLUMNS, TABLE });

  /* 65 */private static final String UPDATE_STMT = DbService.getQuery(
    "UsersGroupsAssociation.update", new String[] { TABLE });

  /* 66 */private static final String DELETE_STMT = DbService.getQuery(
    "UsersGroupsAssociation.delete", new String[] { TABLE });

  /* 67 */private static final String DELETE_GROUP_STMT = DbService.getQuery(
    "UsersGroupsAssociation.deleteGroup", new String[] { TABLE });

  /* 68 */private static final String DELETE_USER_STMT = DbService.getQuery(
    "UsersGroupsAssociation.deleteUser", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getGroups(User user) throws SQLException
  /*     */{
    /* 72 */return getGroups(user.getId());
    /*     */}

  /*     */public final List<String> getGroups(String userId) throws SQLException {
    /* 75 */PreparedStatement stmt = null;
    /* 76 */ResultSet results = null;
    /* 77 */List groups = new ArrayList();
    /* 78 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 80 */stmt = connection.prepareStatement(FIND_BY_USER_STMT);
      /* 81 */stmt.setString(1, userId);
      /* 82 */results = stmt.executeQuery();
      /* 83 */while (results.next()) {
        /* 84 */String id = results.getString(3);
        /* 85 */if ((id != null) && (!groups.contains(id)))
          /* 86 */groups.add(id);
        /*     */}
      /*     */} finally {
      /* 89 */cleanUp(stmt, results);
      /*     */}
    /* 91 */return groups;
    /*     */}

  /*     */
  /*     */public final List<String> getUsers(Group group) throws SQLException {
    /* 95 */return getUsers(group.getId());
    /*     */}

  /*     */public final List<String> getUsers(String groupId) throws SQLException {
    /* 98 */PreparedStatement stmt = null;
    /* 99 */ResultSet results = null;
    /* 100 */List users = new ArrayList();
    /* 101 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 103 */stmt = connection.prepareStatement(FIND_BY_GROUP_STMT);
      /* 104 */stmt.setString(1, groupId);
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
  /*     */public final void delete(Group group) throws SQLException {
    /* 118 */delete(group, DELETE_GROUP_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(User user) throws SQLException {
    /* 122 */delete(user, DELETE_USER_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement()
  /*     */{
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
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.UserGroupAssociation
 * JD-Core Version: 0.5.4
 */