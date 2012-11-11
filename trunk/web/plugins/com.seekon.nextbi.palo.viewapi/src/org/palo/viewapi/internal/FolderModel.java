/*     */package org.palo.viewapi.internal;

/*     */
/*     */import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.palo.api.PaloAPIException;
import org.palo.api.exceptions.PaloIOException;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.Role;
import org.palo.viewapi.internal.dbmappers.MapperRegistry;
import org.palo.viewapi.services.FolderService;
import org.palo.viewapi.services.ServiceProvider;

/*     */
/*     */public class FolderModel
/*     */{
  /* 64 */private static final FolderModel instance = new FolderModel();

  /*     */private static final int FOLDER_DESC_IDX = 2;

  /*     */
  /*     */public static FolderModel getInstance()
  /*     */{
    /* 73 */return instance;
    /*     */}

  /*     */
  /*     */public synchronized void save(AuthUser user, ExplorerTreeNode rootFolder)
  /*     */throws PaloIOException
  /*     */{
    /* 91 */createTableIfNotExists();
    /* 92 */long time = System.currentTimeMillis();
    /* 93 */String xmlDef = null;
    /*     */try {
      /* 95 */ByteArrayOutputStream bout = new ByteArrayOutputStream();
      /*     */try {
        /* 97 */FolderWriter.getInstance().toXML(bout, rootFolder);
        /* 98 */xmlDef = bout.toString("UTF-8");
        /*     */} finally {
        /* 100 */bout.close();
        /*     */}
      /* 102 */if (xmlDef == null) {
        /* 103 */throw new PaloIOException("Could not store folder '" +
        /* 104 */rootFolder.getName() + "' !!");
        /*     */}
      /* 106 */writeFolder(user, xmlDef);
      /*     */} catch (PaloIOException pex) {
      /* 108 */throw new PaloIOException("Could not store folder '" +
      /* 109 */rootFolder.getName() + "'!!", pex);
      /*     */} catch (PaloAPIException pex) {
      /* 111 */throw new PaloIOException("Could not store folder '" +
      /* 112 */rootFolder.getName() + "'!!", pex);
      /*     */} catch (IOException ioe) {
      /* 114 */throw new PaloIOException("Could not store folder '" +
      /* 115 */rootFolder.getName() + "'!!", ioe);
      /*     */}
    /*     */}

  /*     */
  /*     */public synchronized ExplorerTreeNode load(AuthUser user)
  /*     */throws PaloIOException
  /*     */{
    /* 128 */ExplorerTreeNode root = null;
    /* 129 */MapperRegistry.getInstance().getFolderManagement().setUser(user);
    /* 130 */String xmlDef = loadFolder(user);
    /* 131 */if (xmlDef == null) {
      /* 132 */return null;
      /*     */}
    /*     */
    /* 137 */return load(user, xmlDef);
    /*     */}

  /*     */
  /*     */ExplorerTreeNode loadPure(AuthUser user) throws PaloIOException {
    /* 141 */ExplorerTreeNode root = null;
    /* 142 */String xmlDef = loadFolder(user);
    /* 143 */if (xmlDef == null) {
      /* 144 */return null;
      /*     */}
    /*     */
    /* 149 */return loadPure(user, xmlDef);
    /*     */}

  /*     */
  /*     */ExplorerTreeNode load(AuthUser user, String xmlDef)
    throws PaloIOException {
    /* 153 */ExplorerTreeNode root = null;
    /*     */try {
      /* 155 */ByteArrayInputStream bin = new ByteArrayInputStream(
      /* 156 */xmlDef.getBytes("UTF-8"));
      /*     */try {
        /* 158 */root = FolderReader.getInstance().fromXML(user, bin);
        /* 159 */FolderService fs = ServiceProvider.getFolderService(user);
        /* 160 */assignRights(root, fs);
        /*     */} finally {
        /* 162 */bin.close();
        /*     */}
      /* 164 */return root;
      /*     */} catch (IOException e) {
      /* 166 */e.printStackTrace();
      /* 167 */throw new PaloIOException("failed to load folders for user '" +
      /* 168 */user + "'", e);
      /*     */} catch (PaloIOException e) {
      /* 170 */e.printStackTrace();
      /* 171 */throw new PaloIOException("failed to load folders for user '" +
      /* 172 */user + "'", e);
      /*     */}
    /*     */}

  /*     */
  /*     */ExplorerTreeNode loadPure(AuthUser user, String xmlDef)
    throws PaloIOException {
    /* 177 */ExplorerTreeNode root = null;
    /*     */try {
      /* 179 */ByteArrayInputStream bin = new ByteArrayInputStream(
      /* 180 */xmlDef.getBytes("UTF-8"));
      /*     */try {
        /* 182 */root = FolderReader.getInstance().fromXML(user, bin);
        /*     */} finally {
        /* 184 */bin.close();
        /*     */}
      /* 186 */return root;
      /*     */} catch (IOException e) {
      /* 188 */e.printStackTrace();
      /* 189 */throw new PaloIOException("failed to load folders for user '" +
      /* 190 */user + "'", e);
      /*     */} catch (PaloIOException e) {
      /* 192 */e.printStackTrace();
      /* 193 */throw new PaloIOException("failed to load folders for user '" +
      /* 194 */user + "'", e);
      /*     */}
    /*     */}

  /*     */
  /*     */private final void assignRights(ExplorerTreeNode root, FolderService fs) {
    /* 199 */if (root == null) {
      /* 200 */return;
      /*     */}
    /* 202 */ExplorerTreeNode etn = fs.getTreeNode(root.getId());
    /* 203 */if (etn == null) {
      /* 204 */System.err.println("Did not find " + root.getId());
      /*     */} else {
      /* 206 */for (Role role : etn.getRoles()) {
        /* 207 */((AbstractExplorerTreeNode) root).add(role);
        /*     */}
      /* 209 */((AbstractExplorerTreeNode) root).setOwner(etn.getOwner());
      /* 210 */((AbstractExplorerTreeNode) root).setConnectionId(etn
        .getConnectionId());
      /*     */}
    /* 212 */for (ExplorerTreeNode kid : root.getChildren())
      /* 213 */assignRights(kid, fs);
    /*     */}

  /*     */
  /*     */private final boolean writeFolder(AuthUser user, String xmlDef)
  /*     */{
    /* 226 */Connection conn = DbService.getConnection();
    /* 227 */if ((!executeTest(DbService.getQuery("Folder.tableExists"), conn)) &&
    /* 228 */(update(DbService.getQuery("Folder.createTable"), conn) == 0))
      /* 229 */return false;
    /*     */int rowCount;
    /* 233 */if ((rowCount = update(
    /* 234 */DbService.getQuery("Folder.update", new String[] {
    /* 234 */user.getId(), xmlDef }), conn)) == 0) {
      /* 235 */rowCount = update(
      /* 236 */DbService.getQuery("Folder.insert", new String[] { user.getId(),
      /* 236 */xmlDef }), conn);
      /*     */}
    /* 238 */return rowCount == 1;
    /*     */}

  /*     */
  /*     */private final void createTableIfNotExists() {
    /* 242 */if (executeTest(DbService.getQuery("Folder.tableExists"),
    /* 243 */DbService.getConnection()))
      return;
    /* 244 */update(DbService.getQuery("Folder.createTable"), DbService
      .getConnection());
    /*     */}

  /*     */
  /*     */private final String loadFolder(AuthUser user)
  /*     */{
    /* 257 */createTableIfNotExists();
    /* 258 */ResultSet result = query(
    /* 259 */DbService.getQuery("Folder.load", new String[] { user.getId() }),
    /* 260 */DbService.getConnection());
    /*     */try {
      /* 262 */if ((result == null) || (!result.next()))
        return null;
      /* 263 */return result.getString(2);
      /*     */}
    /*     */catch (SQLException e) {
      /* 266 */e.printStackTrace();
      /*     */}
    return null;
    /*     */}

  /*     */
  /*     */private final ResultSet query(String query, Connection connection) {
    /* 272 */if (connection == null) {
      /* 273 */return null;
      /*     */}
    /* 275 */Statement statement = null;
    /* 276 */ResultSet results = null;
    /*     */try {
      /* 278 */statement = connection.createStatement();
      /* 279 */results = statement.executeQuery(query);
      /* 280 */return results;
      /*     */} catch (SQLException e) {
      /* 282 */e.printStackTrace();
      /* 283 */}
    return null;
    /*     */}

  /*     */
  /*     */private final int update(String query, Connection connection)
  /*     */{
    /* 288 */if (connection == null) {
      /* 289 */return 0;
      /*     */}
    /* 291 */Statement statement = null;
    /* 292 */int rowCount = 0;
    /*     */try {
      /* 294 */statement = connection.createStatement();
      /* 295 */rowCount = statement.executeUpdate(query);
      /* 296 */return rowCount;
      /*     */} catch (SQLException e) {
      /* 298 */e.printStackTrace();
      /* 299 */return 0;
      /*     */} finally {
      /* 301 */if (statement != null)
        try {
          statement.close();
        } catch (SQLException localSQLException3) {
          /*     */}
      /*     */}
    /*     */}

  /*     */
  /*     */private final boolean executeTest(String command, Connection connection) {
    /* 306 */if (connection == null) {
      /* 307 */return false;
      /*     */}
    /* 309 */Statement statement = null;
    /*     */try {
      /* 311 */statement = connection.createStatement();
      /* 312 */statement.executeQuery(command);
      /*     */} catch (SQLException localSQLException) {
      /* 314 */return false;
      /*     */} finally {
      /* 316 */if (statement != null)
        try {
          statement.close();
        } catch (SQLException localSQLException2) {
          /*     */}
      /*     */}
    /* 318 */return true;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.FolderModel JD-Core Version:
 * 0.5.4
 */