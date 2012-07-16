/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.DomainObject; /*     */
import org.palo.viewapi.Right; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.IGroupRoleManagement; /*     */
import org.palo.viewapi.internal.IReportRoleManagement; /*     */
import org.palo.viewapi.internal.IRoleManagement; /*     */
import org.palo.viewapi.internal.IUserRoleManagement; /*     */
import org.palo.viewapi.internal.IViewRoleManagement;
import org.palo.viewapi.internal.RoleImpl;
import org.palo.viewapi.internal.RoleImpl.Builder;

/*     */
/*     */final class RoleMapper extends AbstractMapper
/*     */implements IRoleManagement
/*     */{
  /* 68 */private static final String TABLE = DbService.getQuery("Roles.tableName");

  /* 69 */private static final String COLUMNS = DbService.getQuery("Roles.columns");

  /* 70 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "Roles.createTable", new String[] { TABLE });

  /* 71 */private static final String FIND_ALL_STMT = DbService.getQuery(
    "Roles.findAll", new String[] { COLUMNS, TABLE });

  /* 72 */private static final String FIND_BY_ID_STMT = DbService.getQuery(
    "Roles.findById", new String[] { COLUMNS, TABLE });

  /* 73 */private static final String FIND_BY_NAME_STMT = DbService.getQuery(
    "Roles.findByName", new String[] { COLUMNS, TABLE });

  /* 74 */private static final String FIND_BY_USER_STMT = DbService.getQuery(
    "Roles.findByUser", new String[] { COLUMNS, TABLE });

  /* 75 */private static final String INSERT_STMT = DbService.getQuery(
    "Roles.insert", new String[] { TABLE });

  /* 76 */private static final String UPDATE_STMT = DbService.getQuery(
    "Roles.update", new String[] { TABLE });

  /* 77 */private static final String DELETE_STMT = DbService.getQuery(
    "Roles.delete", new String[] { TABLE });

  /*     */
  /*     */public final List<Role> findAll() throws SQLException
  /*     */{
    /* 81 */PreparedStatement stmt = null;
    /* 82 */ResultSet results = null;
    /* 83 */List roles = new ArrayList();
    /* 84 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 86 */stmt = connection.prepareStatement(FIND_ALL_STMT);
      /* 87 */results = stmt.executeQuery();
      /* 88 */while (results.next()) {
        /* 89 */Role role = (Role) load(results);
        /* 90 */if (role != null)
          /* 91 */roles.add(role);
        /*     */}
      /*     */} finally {
      /* 94 */cleanUp(stmt, results);
      /*     */}
    /* 96 */return roles;
    /*     */}

  /*     */
  /*     */public final void update(DomainObject obj) throws SQLException {
    /* 100 */Role role = (Role) obj;
    /* 101 */PreparedStatement stmt = null;
    /* 102 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 104 */stmt = connection.prepareStatement(UPDATE_STMT);
      /* 105 */stmt.setString(1, role.getName());
      /* 106 */stmt.setString(2, role.getDescription());
      /* 107 */stmt.setString(3, role.getPermission().toString());
      /* 108 */stmt.setString(4, role.getId());
      /* 109 */stmt.execute();
      /*     */}
    /*     */finally {
      /* 112 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected final void doInsert(DomainObject obj, PreparedStatement stmt)
    throws SQLException
  /*     */{
    /* 118 */Role role = (Role) obj;
    /* 119 */stmt.setString(1, role.getName());
    /* 120 */stmt.setString(2, role.getDescription());
    /* 121 */stmt.setString(3, role.getPermission().toString());
    /*     */}

  /*     */
  /*     */protected final DomainObject doLoad(String id, ResultSet result)
  /*     */throws SQLException
  /*     */{
    /* 127 */RoleImpl.Builder roleBuilder = new RoleImpl.Builder(id);
    /* 128 */roleBuilder = roleBuilder.name(result.getString(2)).description(
    /* 129 */result.getString(3)).permission(
    /* 130 */Right.fromString(result.getString(4)));
    /*     */
    /* 146 */return roleBuilder.build();
    /*     */}

  /*     */
  /*     */protected final void deleteAssociations(DomainObject obj)
    throws SQLException {
    /* 150 */Role role = (Role) obj;
    /*     */
    /* 152 */MapperRegistry mapperReg = MapperRegistry.getInstance();
    /*     */
    /* 154 */IGroupRoleManagement grAssoc = mapperReg.getGroupRoleAssociation();
    /* 155 */grAssoc.delete(role);
    /*     */
    /* 157 */IReportRoleManagement rrAssoc = mapperReg.getReportRoleAssociation();
    /* 158 */rrAssoc.delete(role);
    /*     */
    /* 160 */IUserRoleManagement urAssoc = mapperReg.getUserRoleAssociation();
    /* 161 */urAssoc.delete(role);
    /*     */
    /* 163 */IViewRoleManagement vrAssoc = mapperReg.getViewRoleAssociation();
    /* 164 */vrAssoc.delete(role);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 168 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String findStatement() {
    /* 172 */return FIND_BY_ID_STMT;
    /*     */}

  /*     */
  /*     */protected final String findByNameStatement() {
    /* 176 */return FIND_BY_NAME_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 180 */return INSERT_STMT;
    /*     */}

  /*     */protected final String createTableStatement() {
    /* 183 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 187 */return TABLE;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.RoleMapper JD-Core
 * Version: 0.5.4
 */