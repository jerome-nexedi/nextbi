/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.viewapi.Account; /*     */
import org.palo.viewapi.DomainObject; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.User; /*     */
import org.palo.viewapi.View; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.IAccountManagement; /*     */
import org.palo.viewapi.internal.IRoleManagement; /*     */
import org.palo.viewapi.internal.IUserManagement; /*     */
import org.palo.viewapi.internal.IViewManagement; /*     */
import org.palo.viewapi.internal.IViewRoleManagement; /*     */
import org.palo.viewapi.internal.ViewImpl;
import org.palo.viewapi.internal.ViewImpl.Builder;

/*     */
/*     */final class ViewMapper extends AbstractMapper
/*     */implements IViewManagement
/*     */{
  /* 69 */private static final String TABLE = DbService.getQuery("Views.tableName");

  /* 70 */private static final String COLUMNS = DbService.getQuery("Views.columns");

  /* 71 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "Views.createTable", new String[] { TABLE });

  /* 72 */private static final String FIND_BY_ID_STMT = DbService.getQuery(
    "Views.findById", new String[] { COLUMNS, TABLE });

  /* 73 */private static final String FIND_BY_OWNER_STMT = DbService.getQuery(
    "Views.findByOwner", new String[] { COLUMNS, TABLE });

  /* 74 */private static final String FIND_BY_NAME_STMT = DbService.getQuery(
    "Views.findByName", new String[] { COLUMNS, TABLE });

  /* 75 */private static final String FIND_BY_ACCOUNT_STMT = DbService.getQuery(
    "Views.findByAccount", new String[] { COLUMNS, TABLE });

  /* 76 */private static final String FIND_BY_NAME_CUBE_ACCOUNT_STMT = DbService
    .getQuery("Views.findByNameCubeAccount", new String[] { COLUMNS, TABLE });

  /* 77 */private static final String FIND_ALL_STMT = DbService.getQuery(
    "Views.findAll", new String[] { COLUMNS, TABLE });

  /*     */
  /* 79 */private static final String INSERT_STMT = DbService.getQuery(
    "Views.insert", new String[] { TABLE });

  /* 80 */private static final String UPDATE_STMT = DbService.getQuery(
    "Views.update", new String[] { TABLE });

  /* 81 */private static final String DELETE_STMT = DbService.getQuery(
    "Views.delete", new String[] { TABLE });

  /*     */
  /*     */public final View findByName(String name, Cube cube, Account account)
  /*     */throws SQLException
  /*     */{
    /* 86 */PreparedStatement stmt = null;
    /* 87 */ResultSet results = null;
    /* 88 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 90 */View view = null;
      /* 91 */Database db = cube.getDatabase();
      /* 92 */stmt = connection
      /* 93 */.prepareStatement(FIND_BY_NAME_CUBE_ACCOUNT_STMT);
      /* 94 */stmt.setString(1, name);
      /* 95 */stmt.setString(2, db.getId());
      /* 96 */stmt.setString(3, cube.getId());
      /* 97 */stmt.setString(4, account.getId());
      /* 98 */results = stmt.executeQuery();
      /* 99 */if (results.next()) {
        /* 100 */view = (View) load(results);
        /*     */}
      /* 102 */return view;
      /*     */} finally {
      /* 104 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final List<View> findViews(Role role) throws SQLException {
    /* 109 */IViewRoleManagement vr =
    /* 110 */MapperRegistry.getInstance().getViewRoleAssociation();
    /* 111 */IViewManagement viewMgmt =
    /* 112 */MapperRegistry.getInstance().getViewManagement();
    /* 113 */List<String> viewIDs = vr.getViews(role);
    /* 114 */List views = new ArrayList();
    /* 115 */for (String id : viewIDs) {
      /* 116 */View view = (View) viewMgmt.find(id);
      /* 117 */if ((view != null) && (!views.contains(view)))
        /* 118 */views.add(view);
      /*     */}
    /* 120 */return views;
    /*     */}

  /*     */
  /*     */public final List<View> findViews(User owner) throws SQLException
  /*     */{
    /* 125 */List views = new ArrayList();
    /* 126 */PreparedStatement stmt = null;
    /* 127 */ResultSet results = null;
    /* 128 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 130 */stmt = connection.prepareStatement(FIND_BY_OWNER_STMT);
      /* 131 */stmt.setString(1, owner.getId());
      /* 132 */results = stmt.executeQuery();
      /* 133 */while (results.next()) {
        /* 134 */View view = (View) load(results);
        /* 135 */if (!views.contains(view))
          /* 136 */views.add(view);
        /*     */}
      /* 138 */return views;
      /*     */} finally {
      /* 140 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final List<View> listViews() throws SQLException {
    /* 145 */List views = new ArrayList();
    /* 146 */PreparedStatement stmt = null;
    /* 147 */ResultSet results = null;
    /* 148 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 150 */stmt = connection.prepareStatement(FIND_ALL_STMT);
      /* 151 */results = stmt.executeQuery();
      /* 152 */while (results.next()) {
        /* 153 */View view = (View) load(results);
        /* 154 */if (!views.contains(view))
          /* 155 */views.add(view);
        /*     */}
      /* 157 */return views;
      /*     */} finally {
      /* 159 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final List<View> findViews(Account account) throws SQLException {
    /* 164 */List views = new ArrayList();
    /* 165 */PreparedStatement stmt = null;
    /* 166 */ResultSet results = null;
    /* 167 */Connection _conn = DbService.getConnection();
    /*     */try {
      /* 169 */stmt = _conn.prepareStatement(FIND_BY_ACCOUNT_STMT);
      /* 170 */stmt.setString(1, account.getId());
      /* 171 */results = stmt.executeQuery();
      /* 172 */while (results.next()) {
        /* 173 */View view = (View) load(results);
        /* 174 */if (!views.contains(view))
          /* 175 */views.add(view);
        /*     */}
      /* 177 */return views;
      /*     */} finally {
      /* 179 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final boolean hasViews(Account account) throws SQLException {
    /* 184 */PreparedStatement stmt = null;
    /* 185 */ResultSet results = null;
    /* 186 */Connection _conn = DbService.getConnection();
    /*     */try {
      /* 188 */stmt = _conn.prepareStatement(FIND_BY_ACCOUNT_STMT);
      /* 189 */stmt.setString(1, account.getId());
      /* 190 */results = stmt.executeQuery();
      /* 191 */return results.next();
      /*     */} finally {
      /* 193 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void update(DomainObject obj) throws SQLException {
    /* 198 */View view = (View) obj;
    /* 199 */PreparedStatement stmt = null;
    /* 200 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 202 */stmt = connection.prepareStatement(UPDATE_STMT);
      /* 203 */stmt.setString(1, view.getName());
      /* 204 */stmt.setString(2, view.getOwner().getId());
      /* 205 */stmt.setString(3, view.getDefinition());
      /* 206 */stmt.setString(4, view.getDatabaseId());
      /* 207 */stmt.setString(5, view.getCubeId());
      /* 208 */stmt.setString(6, view.getAccount().getId());
      /* 209 */stmt.setString(7, view.getId());
      /* 210 */stmt.execute();
      /* 211 */handleAssociations(view);
      /*     */} finally {
      /* 213 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected void doInsert(DomainObject obj, PreparedStatement stmt)
  /*     */throws SQLException
  /*     */{
    /* 221 */View view = (View) obj;
    /* 222 */stmt.setString(1, view.getName());
    /* 223 */stmt.setString(2, view.getOwner().getId());
    /* 224 */stmt.setString(3, view.getDefinition());
    /* 225 */stmt.setString(4, view.getDatabaseId());
    /* 226 */stmt.setString(5, view.getCubeId());
    /* 227 */stmt.setString(6, view.getAccount().getId());
    /*     */
    /* 229 */handleAssociations(view);
    /*     */}

  /*     */
  /*     */protected final DomainObject doLoad(String id, ResultSet result)
    throws SQLException
  /*     */{
    /* 234 */ViewImpl.Builder viewBuilder = new ViewImpl.Builder(id);
    /* 235 */viewBuilder.name(result.getString(2));
    /*     */
    /* 237 */IUserManagement usrMgmt = MapperRegistry.getInstance()
      .getUserManagement();
    /* 238 */viewBuilder.owner((User) usrMgmt.find(result.getString(3)));
    /* 239 */viewBuilder.definition(result.getString(4));
    /* 240 */viewBuilder.database(result.getString(5));
    /* 241 */viewBuilder.cube(result.getString(6));
    /*     */
    /* 243 */IAccountManagement accMgmt =
    /* 244 */MapperRegistry.getInstance().getAccountManagement();
    /* 245 */viewBuilder.account((Account) accMgmt.find(result.getString(7)));
    /*     */
    /* 247 */IRoleManagement roleMgmt =
    /* 248 */MapperRegistry.getInstance().getRoleManagement();
    /* 249 */IViewRoleManagement vrAssoc =
    /* 250 */MapperRegistry.getInstance().getViewRoleAssociation();
    /*     */
    /* 252 */List<String> roles = vrAssoc.getRoles(id);
    /* 253 */for (String roleId : roles) {
      /* 254 */Role role = (Role) roleMgmt.find(roleId);
      /* 255 */if (role != null) {
        /* 256 */viewBuilder.add(role);
        /*     */}
      /*     */}
    /* 259 */return viewBuilder.build();
    /*     */}

  /*     */
  /*     */protected final void deleteAssociations(DomainObject obj)
    throws SQLException {
    /* 263 */View view = (View) obj;
    /*     */
    /* 265 */IViewRoleManagement vrAssoc =
    /* 266 */MapperRegistry.getInstance().getViewRoleAssociation();
    /* 267 */vrAssoc.delete(view);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 271 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String findStatement() {
    /* 275 */return FIND_BY_ID_STMT;
    /*     */}

  /*     */protected final String findByNameStatement() {
    /* 278 */return FIND_BY_NAME_STMT;
    /*     */}

  /*     */protected final String insertStatement() {
    /* 281 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 285 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 289 */return TABLE;
    /*     */}

  /*     */
  /*     */private final void handleAssociations(View view) throws SQLException
  /*     */{
    /* 294 */IRoleManagement roleMgmt =
    /* 295 */MapperRegistry.getInstance().getRoleManagement();
    /* 296 */IViewRoleManagement vrAssoc =
    /* 297 */MapperRegistry.getInstance().getViewRoleAssociation();
    /*     */
    /* 299 */List<String> savedRoles = vrAssoc.getRoles(view);
    /* 300 */List<String> roles = ((ViewImpl) view).getRoleIDs();
    /* 301 */for (String id : roles) {
      /* 302 */if (!savedRoles.contains(id)) {
        /* 303 */vrAssoc.insert(view, roleMgmt.find(id));
        /*     */}
      /*     */}
    /* 306 */savedRoles.removeAll(roles);
    /* 307 */for (String id : savedRoles)
      /* 308 */vrAssoc.delete(view, roleMgmt.find(id));
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.ViewMapper JD-Core
 * Version: 0.5.4
 */