/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.palo.viewapi.DomainObject;
import org.palo.viewapi.Group;
import org.palo.viewapi.Role;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.GroupImpl;
import org.palo.viewapi.internal.IGroupManagement;
import org.palo.viewapi.internal.IGroupRoleManagement;
import org.palo.viewapi.internal.IRoleManagement;
import org.palo.viewapi.internal.IUserGroupManagement;
import org.palo.viewapi.internal.IUserManagement;

/*     */
/*     */final class GroupMapper extends AbstractMapper
/*     */implements IGroupManagement
/*     */{
  /* 66 */private static final String TABLE = DbService
    .getQuery("Groups.tableName");

  /* 67 */private static final String COLUMNS = DbService
    .getQuery("Groups.columns");

  /* 68 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "Groups.createTable", new String[] { TABLE });

  /* 69 */private static final String FIND_ALL_STMT = DbService.getQuery(
    "Groups.findAll", new String[] { COLUMNS, TABLE });

  /* 70 */private static final String FIND_BY_ID_STMT = DbService.getQuery(
    "Groups.findById", new String[] { COLUMNS, TABLE });

  /* 71 */private static final String FIND_BY_NAME_STMT = DbService.getQuery(
    "Groups.findByName", new String[] { COLUMNS, TABLE });

  /*     */
  /* 73 */private static final String INSERT_STMT = DbService.getQuery(
    "Groups.insert", new String[] { TABLE });

  /* 74 */private static final String UPDATE_STMT = DbService.getQuery(
    "Groups.update", new String[] { TABLE });

  /* 75 */private static final String DELETE_STMT = DbService.getQuery(
    "Groups.delete", new String[] { TABLE });

  /*     */
  /*     */public final List<Group> findAll() throws SQLException
  /*     */{
    /* 79 */PreparedStatement stmt = null;
    /* 80 */ResultSet results = null;
    /* 81 */List groups = new ArrayList();
    /* 82 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 84 */stmt = connection.prepareStatement(FIND_ALL_STMT);
      /* 85 */results = stmt.executeQuery();
      /* 86 */while (results.next()) {
        /* 87 */Group group = (Group) load(results);
        /* 88 */if (group != null)
          /* 89 */groups.add(group);
        /*     */}
      /*     */} finally {
      /* 92 */cleanUp(stmt, results);
      /*     */}
    /* 94 */return groups;
    /*     */}

  /*     */
  /*     */public final void update(DomainObject obj) throws SQLException {
    /* 98 */Group group = (Group) obj;
    /* 99 */PreparedStatement stmt = null;
    /* 100 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 102 */stmt = connection.prepareStatement(UPDATE_STMT);
      /* 103 */stmt.setString(1, group.getName());
      /* 104 */stmt.setString(2, group.getDescription());
      /* 105 */stmt.setString(3, group.getId());
      /* 106 */stmt.execute();
      /* 107 */handleAssociations(group);
      /*     */} finally {
      /* 109 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected final void doInsert(DomainObject obj, PreparedStatement stmt)
    throws SQLException
  /*     */{
    /* 115 */Group group = (Group) obj;
    /* 116 */stmt.setString(1, group.getName());
    /* 117 */stmt.setString(2, group.getDescription());
    /* 118 */handleAssociations(group);
    /*     */}

  /*     */
  /*     */protected final DomainObject doLoad(String id, ResultSet result)
    throws SQLException
  /*     */{
    /* 123 */GroupImpl.Builder groupBuilder = new GroupImpl.Builder(id);
    /* 124 */groupBuilder.name(result.getString(2));
    /* 125 */groupBuilder.description(result.getString(3));
    /* 126 */MapperRegistry mapperReg = MapperRegistry.getInstance();
    /*     */
    /* 128 */IGroupRoleManagement grAssoc = mapperReg.getGroupRoleAssociation();
    /* 129 */groupBuilder.roles(grAssoc.getRoles(id));
    /*     */
    /* 131 */IUserGroupManagement ugAssoc = mapperReg.getUserGroupAssociation();
    /* 132 */groupBuilder.users(ugAssoc.getUsers(id));
    /* 133 */return groupBuilder.build();
    /*     */}

  /*     */
  /*     */protected final void deleteAssociations(DomainObject obj)
  /*     */throws SQLException
  /*     */{
    /* 149 */Group group = (Group) obj;
    /* 150 */MapperRegistry mapperReg = MapperRegistry.getInstance();
    /*     */
    /* 152 */IUserGroupManagement ugAssoc = mapperReg.getUserGroupAssociation();
    /* 153 */ugAssoc.delete(group);
    /*     */
    /* 155 */IGroupRoleManagement grAssoc = mapperReg.getGroupRoleAssociation();
    /* 156 */grAssoc.delete(group);
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
  /*     */private final void handleAssociations(Group group)
  /*     */throws SQLException
  /*     */{
    /* 186 */MapperRegistry mapperReg = MapperRegistry.getInstance();
    /*     */
    /* 188 */IRoleManagement rolesMgmt = mapperReg.getRoleManagement();
    /* 189 */IGroupRoleManagement grAssoc = mapperReg.getGroupRoleAssociation();
    /* 190 */List<String> roles = ((GroupImpl) group).getRoleIDs();
    /* 191 */List<String> savedRoles = grAssoc.getRoles(group);
    /* 192 */for (String id : roles) {
      /* 193 */if (!savedRoles.contains(id)) {
        /* 194 */grAssoc.insert(group, rolesMgmt.find(id));
        /*     */}
      /*     */}
    /* 197 */savedRoles.removeAll(roles);
    /* 198 */for (String id : savedRoles) {
      /* 199 */grAssoc.delete(group, rolesMgmt.find(id));
      /*     */}
    /*     */
    /* 203 */IUserManagement usrMgmt = mapperReg.getUserManagement();
    /* 204 */IUserGroupManagement ugAssoc = mapperReg.getUserGroupAssociation();
    /* 205 */List<String> users = ((GroupImpl) group).getUserIDs();
    /* 206 */List<String> savedUsers = ugAssoc.getUsers(group);
    /* 207 */for (String id : users) {
      /* 208 */if (!savedUsers.contains(id)) {
        /* 209 */ugAssoc.insert(group, usrMgmt.find(id));
        /*     */}
      /*     */}
    /* 212 */savedUsers.removeAll(users);
    /* 213 */for (String id : savedUsers)
      /* 214 */ugAssoc.delete(group, usrMgmt.find(id));
    /*     */}

  /*     */
  /*     */public List<Group> findAllGroupsFor(Role role) throws SQLException {
    /* 218 */MapperRegistry mapperReg = MapperRegistry.getInstance();
    /* 219 */IGroupManagement groupMgmt = mapperReg.getGroupManagement();
    /* 220 */IGroupRoleManagement grAssoc = mapperReg.getGroupRoleAssociation();
    /* 221 */List<String> groupIDs = grAssoc.getGroups(role);
    /* 222 */List groups = new ArrayList();
    /* 223 */for (String id : groupIDs) {
      /* 224 */Group group = (Group) groupMgmt.find(id);
      /* 225 */if (group != null)
        /* 226 */groups.add(group);
      /*     */}
    /* 228 */return groups;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.GroupMapper JD-Core
 * Version: 0.5.4
 */