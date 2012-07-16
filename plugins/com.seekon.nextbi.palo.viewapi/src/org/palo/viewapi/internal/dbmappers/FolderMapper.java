/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import java.sql.Connection; /*     */
import java.sql.PreparedStatement; /*     */
import java.sql.ResultSet; /*     */
import java.sql.SQLException; /*     */
import java.util.ArrayList; /*     */
import java.util.List; /*     */
import org.palo.viewapi.AuthUser; /*     */
import org.palo.viewapi.DomainObject; /*     */
import org.palo.viewapi.Role; /*     */
import org.palo.viewapi.User; /*     */
import org.palo.viewapi.internal.AbstractExplorerTreeNode; /*     */
import org.palo.viewapi.internal.AbstractExplorerTreeNode.Builder; /*     */
import org.palo.viewapi.internal.DbService; /*     */
import org.palo.viewapi.internal.ExplorerTreeNode; /*     */
import org.palo.viewapi.internal.IFolderManagement; /*     */
import org.palo.viewapi.internal.IFolderRoleManagement; /*     */
import org.palo.viewapi.internal.IRoleManagement; /*     */
import org.palo.viewapi.internal.IUserManagement; /*     */
import org.palo.viewapi.services.FolderService; /*     */
import org.palo.viewapi.services.ServiceProvider;

/*     */
/*     */final class FolderMapper extends AbstractMapper
/*     */implements IFolderManagement
/*     */{
  /* 67 */private static final String TABLE = DbService
    .getQuery("Folders.tableName");

  /* 68 */private static final String COLUMNS = DbService
    .getQuery("Folders.columns");

  /* 69 */private static final String CREATE_TABLE_STMT = DbService.getQuery(
    "Folders.createTable", new String[] { TABLE });

  /* 70 */private static final String FIND_BY_ID_STMT = DbService.getQuery(
    "Folders.findById", new String[] { COLUMNS, TABLE });

  /* 71 */private static final String FIND_BY_OWNER_STMT = DbService.getQuery(
    "Folders.findByOwner", new String[] { COLUMNS, TABLE });

  /* 72 */private static final String FIND_BY_CONNECTION_STMT = DbService.getQuery(
    "Folders.findByConnection", new String[] { COLUMNS, TABLE });

  /* 73 */private static final String INSERT_STMT = DbService.getQuery(
    "Folders.insert", new String[] { TABLE });

  /* 74 */private static final String UPDATE_STMT = DbService.getQuery(
    "Folders.update", new String[] { TABLE });

  /* 75 */private static final String DELETE_STMT = DbService.getQuery(
    "Folders.delete", new String[] { TABLE });

  /*     */private AuthUser authUser;

  /*     */
  /*     */public void setUser(AuthUser user)
  /*     */{
    /* 80 */this.authUser = user;
    /*     */}

  /*     */
  /*     */public final List<ExplorerTreeNode> getFolders(Role role)
    throws SQLException {
    /* 84 */IFolderRoleManagement fr =
    /* 85 */MapperRegistry.getInstance().getFolderRoleAssociation();
    /* 86 */IFolderManagement folderMgmt =
    /* 87 */MapperRegistry.getInstance().getFolderManagement();
    /* 88 */List<String> folderIDs = fr.getFolders(role);
    /* 89 */List folders = new ArrayList();
    /* 90 */for (String id : folderIDs) {
      /* 91 */ExplorerTreeNode node = (ExplorerTreeNode) folderMgmt.find(id);
      /* 92 */if ((node != null) && (!folders.contains(node))) {
        /* 93 */folders.add(node);
        /*     */}
      /*     */}
    /* 96 */return folders;
    /*     */}

  /*     */
  /*     */public final List<ExplorerTreeNode> getFolders(AuthUser user, User owner)
    throws SQLException {
    /* 100 */PreparedStatement stmt = null;
    /* 101 */ResultSet results = null;
    /* 102 */Connection connection = DbService.getConnection();
    /* 103 */List folders = new ArrayList();
    /*     */try {
      /* 105 */stmt = connection.prepareStatement(FIND_BY_OWNER_STMT);
      /* 106 */stmt.setString(1, owner.getId());
      /* 107 */results = stmt.executeQuery();
      /* 108 */while (results.next()) {
        /* 109 */this.authUser = user;
        /* 110 */ExplorerTreeNode node = (ExplorerTreeNode) load(results);
        /* 111 */if ((node != null) && (!folders.contains(node))) {
          /* 112 */folders.add(node);
          /*     */}
        /*     */}
      /* 115 */return folders;
      /*     */} finally {
      /* 117 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */protected DomainObject load(ResultSet rs) throws SQLException {
    /* 122 */String id = rs.getString(1);
    /* 123 */if ((this.cache.contains(id)) && (this.cache.get(id) != null))
      /* 124 */return this.cache.get(id);
    /* 125 */DomainObject obj = doLoad(id, rs);
    /* 126 */this.cache.add(obj);
    /* 127 */return obj;
    /*     */}

  /*     */
  /*     */public final List<ExplorerTreeNode> getFolders(AuthUser user)
    throws SQLException
  /*     */{
    /* 132 */PreparedStatement stmt = null;
    /* 133 */ResultSet results = null;
    /* 134 */Connection _conn = DbService.getConnection();
    /* 135 */List folders = new ArrayList();
    /*     */try {
      /* 137 */stmt = _conn.prepareStatement(FIND_BY_CONNECTION_STMT);
      /*     */
      /* 139 */results = stmt.executeQuery();
      /* 140 */while (results.next())
      /*     */{
        /* 142 */this.authUser = user;
        /* 143 */ExplorerTreeNode node = (ExplorerTreeNode) load(results);
        /* 144 */if ((node != null) && (!folders.contains(node))) {
          /* 145 */folders.add(node);
          /*     */}
        /*     */}
      /* 148 */return folders;
      /*     */} catch (Throwable t) {
      /* 150 */t.printStackTrace();
      /* 151 */return null;
      /*     */} finally {
      /* 153 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final List<ExplorerTreeNode> reallyGetFolders(AuthUser user)
    throws SQLException
  /*     */{
    /* 159 */PreparedStatement stmt = null;
    /* 160 */ResultSet results = null;
    /* 161 */Connection _conn = DbService.getConnection();
    /* 162 */List folders = new ArrayList();
    /*     */try {
      /* 164 */stmt = _conn.prepareStatement(FIND_BY_OWNER_STMT);
      /* 165 */stmt.setString(1, user.getId());
      /* 166 */results = stmt.executeQuery();
      /* 167 */while (results.next())
      /*     */{
        /* 169 */this.authUser = user;
        /* 170 */ExplorerTreeNode node = (ExplorerTreeNode) load(results);
        /* 171 */if ((node != null) && (!folders.contains(node))) {
          /* 172 */folders.add(node);
          /*     */}
        /*     */}
      /* 175 */return folders;
      /*     */} catch (Throwable t) {
      /* 177 */t.printStackTrace();
      /* 178 */return null;
      /*     */} finally {
      /* 180 */cleanUp(stmt, results);
      /*     */}
    /*     */}

  /*     */
  /*     */public final void update(DomainObject obj) throws SQLException {
    /* 185 */ExplorerTreeNode node = (ExplorerTreeNode) obj;
    /* 186 */PreparedStatement stmt = null;
    /* 187 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 189 */stmt = connection.prepareStatement(UPDATE_STMT);
      /* 190 */stmt.setString(1, node.getName());
      /* 191 */if (node.getOwner() == null) {
        /* 192 */ServiceProvider.getFolderService(this.authUser).setOwner(
        /* 193 */this.authUser, node);
        /*     */}
      /* 195 */stmt.setString(2, node.getOwner().getId());
      /* 196 */stmt.setInt(3, node.getType());
      /* 197 */stmt.setString(4, node.getId());
      /* 198 */stmt.execute();
      /* 199 */handleAssociations(node);
      /*     */} finally {
      /* 201 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected void doInsert(DomainObject obj, PreparedStatement stmt)
  /*     */throws SQLException
  /*     */{
    /* 209 */ExplorerTreeNode node = (ExplorerTreeNode) obj;
    /* 210 */stmt.setString(1, node.getId());
    /* 211 */stmt.setString(2, node.getName());
    /* 212 */stmt.setString(3, node.getOwner().getId());
    /* 213 */stmt.setInt(4, node.getType());
    /*     */
    /* 215 */handleAssociations(node);
    /*     */}

  /*     */
  /*     */public synchronized void insert(DomainObject obj) throws SQLException {
    /* 219 */PreparedStatement stmt = null;
    /* 220 */Connection connection = DbService.getConnection();
    /*     */try {
      /* 222 */String ins = insertStatement();
      /* 223 */stmt = connection.prepareStatement(ins);
      /* 224 */doInsert(obj, stmt);
      /* 225 */stmt.execute();
      /* 226 */this.cache.add(obj);
      /*     */} catch (Throwable t) {
      /* 228 */t.printStackTrace();
      /*     */} finally {
      /* 230 */cleanUp(stmt);
      /*     */}
    /*     */}

  /*     */
  /*     */protected final DomainObject doLoad(String id, ResultSet result)
    throws SQLException
  /*     */{
    /* 236 */AbstractExplorerTreeNode.Builder folderBuilder = new AbstractExplorerTreeNode.Builder(
      id);
    /* 237 */folderBuilder.name(result.getString(2));
    /*     */
    /* 239 */IUserManagement usrMgmt = MapperRegistry.getInstance()
      .getUserManagement();
    /* 240 */folderBuilder.owner((User) usrMgmt.find(result.getString(3)));
    /* 241 */folderBuilder.type(result.getString(4));
    /*     */
    /* 243 */folderBuilder.connection(null);
    /*     */
    /* 245 */IRoleManagement roleMgmt =
    /* 246 */MapperRegistry.getInstance().getRoleManagement();
    /* 247 */IFolderRoleManagement frAssoc =
    /* 248 */MapperRegistry.getInstance().getFolderRoleAssociation();
    /*     */
    /* 250 */List<String> roles = frAssoc.getRoles(id);
    /* 251 */for (String roleId : roles) {
      /* 252 */Role role = (Role) roleMgmt.find(roleId);
      /* 253 */if (role != null) {
        /* 254 */folderBuilder.add(role);
        /*     */}
      /*     */}
    /* 257 */ExplorerTreeNode obj = folderBuilder.build(this.authUser);
    /* 258 */return obj;
    /*     */}

  /*     */
  /*     */protected final void deleteAssociations(DomainObject obj)
    throws SQLException {
    /* 262 */ExplorerTreeNode node = (ExplorerTreeNode) obj;
    /*     */
    /* 265 */IFolderRoleManagement frAssoc =
    /* 266 */MapperRegistry.getInstance().getFolderRoleAssociation();
    /*     */
    /* 268 */frAssoc.delete(node);
    /*     */}

  /*     */
  /*     */protected final String deleteStatement() {
    /* 272 */return DELETE_STMT;
    /*     */}

  /*     */
  /*     */protected final String findStatement() {
    /* 276 */return FIND_BY_ID_STMT;
    /*     */}

  /*     */
  /*     */protected final String insertStatement() {
    /* 280 */return INSERT_STMT;
    /*     */}

  /*     */
  /*     */protected final String createTableStatement() {
    /* 284 */return CREATE_TABLE_STMT;
    /*     */}

  /*     */
  /*     */protected final String getTableName() {
    /* 288 */return TABLE;
    /*     */}

  /*     */
  /*     */private final void handleAssociations(ExplorerTreeNode node)
    throws SQLException
  /*     */{
    /* 293 */IRoleManagement roleMgmt =
    /* 294 */MapperRegistry.getInstance().getRoleManagement();
    /* 295 */IFolderRoleManagement frAssoc =
    /* 296 */MapperRegistry.getInstance().getFolderRoleAssociation();
    /*     */
    /* 298 */List<String> savedRoles = frAssoc.getRoles(node);
    /* 299 */List<String> roles = ((AbstractExplorerTreeNode) node).getRoleIDs();
    /* 300 */for (String id : roles) {
      /* 301 */if (!savedRoles.contains(id)) {
        /* 302 */frAssoc.insert(node, roleMgmt.find(id));
        /*     */}
      /*     */}
    /* 305 */savedRoles.removeAll(roles);
    /* 306 */for (String id : savedRoles)
      /* 307 */frAssoc.delete(node, roleMgmt.find(id));
    /*     */}

  /*     */
  /*     */protected String findByNameStatement() {
    /* 311 */throw new IllegalArgumentException(
      "This method is not supported for folders.");
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.FolderMapper JD-Core
 * Version: 0.5.4
 */