/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.palo.viewapi.Role;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.ExplorerTreeNode;
import org.palo.viewapi.internal.IFolderRoleManagement;

/*     */
/*     */final class FolderRoleAssociation extends AssociationTableMapper
/*     */implements IFolderRoleManagement
/*     */{
  /* 65 */private static final String TABLE = DbService
    .getQuery("FoldersRolesAssociation.tableName");

  /* 66 */private static final String COLUMNS = DbService
    .getQuery("FoldersRolesAssociation.columns");

  /* 67 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "FoldersRolesAssociation.createTable", new String[] { TABLE });

  /* 68 */private static final String INSERT_STMT = DbService.getQuery(
    "FoldersRolesAssociation.insert", new String[] { TABLE });

  /* 69 */private static final String FIND_BY_FOLDER_STMT = DbService.getQuery(
    "FoldersRolesAssociation.findByFolder", new String[] { COLUMNS, TABLE });

  /* 70 */private static final String FIND_BY_ROLE_STMT = DbService.getQuery(
    "FoldersRolesAssociation.findByRole", new String[] { COLUMNS, TABLE });

  /* 71 */private static final String UPDATE_STMT = DbService.getQuery(
    "FoldersRolesAssociation.update", new String[] { TABLE });

  /* 72 */private static final String DELETE_STMT = DbService.getQuery(
    "FoldersRolesAssociation.delete", new String[] { TABLE });

  /* 73 */private static final String DELETE_ROLE_STMT = DbService.getQuery(
    "FoldersRolesAssociation.deleteRole", new String[] { TABLE });

  /* 74 */private static final String DELETE_FOLDER_STMT = DbService.getQuery(
    "FoldersRolesAssociation.deleteFolder", new String[] { TABLE });

  /*     */
  /*     */public final List<String> getRoles(ExplorerTreeNode node)
    throws SQLException
  /*     */{
    /* 78 */return getRoles(node.getId());
    /*     */}

  /*     */
  /*     */public final List<String> getRoles(String nodeId) throws SQLException {
    /* 82 */Connection connection = DbService.getConnection();
    /* 83 */PreparedStatement stmt = null;
    /* 84 */ResultSet results = null;
    /* 85 */List roles = new ArrayList();
    /*     */try {
      /* 87 */stmt = connection.prepareStatement(FIND_BY_FOLDER_STMT);
      /* 88 */stmt.setString(1, nodeId);
      /* 89 */results = stmt.executeQuery();
      /* 90 */while (results.next()) {
        /* 91 */String id = results.getString(3);
        /* 92 */if ((id != null) && (!roles.contains(id)))
          /* 93 */roles.add(id);
        /*     */}
      /*     */} finally {
      /* 96 */cleanUp(stmt, results);
      /*     */}
    /* 98 */return roles;
    /*     */}

  /*     */
  /*     */public final List<String> getFolders(Role role) throws SQLException {
    /* 102 */Connection connection = DbService.getConnection();
    /* 103 */PreparedStatement stmt = null;
    /* 104 */ResultSet results = null;
    /* 105 */List folders = new ArrayList();
    /*     */try {
      /* 107 */stmt = connection.prepareStatement(FIND_BY_ROLE_STMT);
      /* 108 */stmt.setString(1, role.getId());
      /* 109 */results = stmt.executeQuery();
      /* 110 */while (results.next()) {
        /* 111 */String id = results.getString(2);
        /* 112 */if ((id != null) && (!folders.contains(id)))
          /* 113 */folders.add(id);
        /*     */}
      /*     */}
    /*     */finally {
      /* 117 */cleanUp(stmt, results);
      /*     */}
    /* 119 */return folders;
    /*     */}

  /*     */
  /*     */public final void delete(Role role) throws SQLException
  /*     */{
    /* 124 */delete(role, DELETE_ROLE_STMT);
    /*     */}

  /*     */
  /*     */public final void delete(ExplorerTreeNode node) throws SQLException {
    /* 128 */delete(node, DELETE_FOLDER_STMT);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 132 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 136 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String updateStatement() {
    /* 140 */return UPDATE_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 144 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 148 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name:
 * org.palo.viewapi.internal.dbmappers.FolderRoleAssociation JD-Core Version:
 * 0.5.4
 */