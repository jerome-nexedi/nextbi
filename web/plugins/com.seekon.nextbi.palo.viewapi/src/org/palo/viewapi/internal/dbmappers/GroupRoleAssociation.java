/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.Group; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.IGroupRoleManagement;

/*     */
/*     */final class GroupRoleAssociation extends AssociationTableMapper
/*     */implements IGroupRoleManagement
/*     */{
  /* 59 */private static final String TABLE = DbService
    .getQuery("GroupsRolesAssociation.tableName");

  /* 60 */private static final String COLUMNS = DbService
    .getQuery("GroupsRolesAssociation.columns");

  /* 61 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "GroupsRolesAssociation.createTable", new String[] { TABLE });

  /* 62 */private static final String INSERT_STMT = DbService.getQuery(
    "GroupsRolesAssociation.insert", new String[] { TABLE });

  /* 63 */private static final String FIND_BY_GROUP_STMT = DbService.getQuery(
    "GroupsRolesAssociation.findByGroup", new String[] { COLUMNS, TABLE });

  /* 64 */private static final String FIND_BY_ROLE_STMT = DbService.getQuery(
    "GroupsRolesAssociation.findByRole", new String[] { COLUMNS, TABLE });

  /* 65 */private static final String UPDATE_STMT = DbService.getQuery(
    "GroupsRolesAssociation.update", new String[] { TABLE });

  /* 66 */private static final String DELETE_STMT = DbService.getQuery(
    "GroupsRolesAssociation.delete", new String[] { TABLE });

  /* 67 */private static final String DELETE_GROUP_STMT = DbService.getQuery(
    "GroupsRolesAssociation.deleteGroup", new String[] { TABLE });

  /* 68 */private static final String DELETE_ROLE_STMT = DbService.getQuery(
    "GroupsRolesAssociation.deleteRole", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getGroups(Role role)
  /*     */throws SQLException
  /*     */{
    /* 73 */return getGroups(role.getId());
    /*     */}

  /*     */public final List<String> getGroups(String roleId) throws SQLException {
    /* 76 */PreparedStatement stmt = null;
    /* 77 */ResultSet results = null;
    /* 78 */List groups = new ArrayList();
    /* 79 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 81 */stmt = connection.prepareStatement(FIND_BY_ROLE_STMT);
      /* 82 */stmt.setString(1, roleId);
      /* 83 */results = stmt.executeQuery();
      /* 84 */while (results.next()) {
        /* 85 */String id = results.getString(2);
        /* 86 */if ((id != null) && (!groups.contains(id)))
          /* 87 */groups.add(id);
        /*     */}
      /*     */} finally {
      /* 90 */cleanUp(stmt);
      /*     */}
    /* 92 */return groups;
    /*     */}

  /*     */
  /*     */public final List<String> getRoles(Group group) throws SQLException {
    /* 96 */return getRoles(group.getId());
    /*     */}

  /*     */public final List<String> getRoles(String groupId) throws SQLException {
    /* 99 */PreparedStatement stmt = null;
    /* 100 */ResultSet results = null;
    /* 101 */List roles = new ArrayList();
    /* 102 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 104 */stmt = connection.prepareStatement(FIND_BY_GROUP_STMT);
      /* 105 */stmt.setString(1, groupId);
      /* 106 */results = stmt.executeQuery();
      /* 107 */while (results.next()) {
        /* 108 */String id = results.getString(3);
        /* 109 */if ((id != null) && (!roles.contains(id)))
          /* 110 */roles.add(id);
        /*     */}
      /*     */} finally {
      /* 113 */cleanUp(stmt, results);
      /*     */}
    /* 115 */return roles;
    /*     */}

  /*     */
  /*     */public final void delete(Group group) throws SQLException
  /*     */{
    /* 120 */delete(group, DELETE_GROUP_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(Role role) throws SQLException
  /*     */{
    /* 125 */delete(role, DELETE_ROLE_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 129 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 133 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String updateStatement() {
    /* 137 */return UPDATE_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 141 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 145 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.GroupRoleAssociation
 * JD-Core Version: 0.5.4
 */