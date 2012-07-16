package org.palo.viewapi.internal;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import org.palo.viewapi.Account;
import org.palo.viewapi.DbConnection;
import org.palo.viewapi.Group;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.Right;
import org.palo.viewapi.Role;
import org.palo.viewapi.exceptions.NotConnectedException;
import org.palo.viewapi.internal.dbmappers.MapperRegistry;

public class DbService
{
  private static final ResourceBundle rb = ResourceBundle.getBundle("deploy", Locale.ITALIAN);
  private static DbConnection connection;

  private static final UserImpl createUser(IUserManagement usrMgmt, String login, String passw)
    throws SQLException
  {
    String pass = AuthUserImpl.encrypt(passw);
    UserImpl user = new UserImpl((String)null);
    user.setLoginName(login);
    user.setPassword(pass);
    usrMgmt.insert(user);
    return user;
  }

  private static final UserImpl createUser(IUserManagement usrMgmt, String login) throws SQLException {
    return createUser(usrMgmt, login, login);
  }

  private static final Role createRole(IRoleManagement roleMgmt, String name, Right right) throws SQLException {
    Role role = new RoleImpl.Builder(null).name(
      name).permission(right).build();
    roleMgmt.insert(role);
    return role;
  }

  private static final boolean initUsers() throws SQLException {
    MapperRegistry registry = MapperRegistry.getInstance();
    IUserManagement usrMgmt = registry.getUserManagement();

    String adminUser = rb.getString("jpalo.admin.user");
    String adminPwd = rb.getString("jpalo.admin.password");

    UserImpl admin = (UserImpl)usrMgmt.findByName(adminUser);
    UserImpl editor = (UserImpl)usrMgmt.findByName("editor");
    UserImpl viewer = (UserImpl)usrMgmt.findByName("viewer");
    UserImpl poweruser = (UserImpl)usrMgmt.findByName("poweruser");
    UserImpl creator = (UserImpl)usrMgmt.findByName("creator");
    UserImpl publisher = (UserImpl)usrMgmt.findByName("publisher");
    UserImpl directLink = (UserImpl)usrMgmt.findByName("direct-link");

    if (admin == null)
    {
      admin = createUser(usrMgmt, adminUser, adminPwd);
      if (editor == null) editor = createUser(usrMgmt, "editor");
      if (viewer == null) viewer = createUser(usrMgmt, "viewer");
      if (poweruser == null) poweruser = createUser(usrMgmt, "poweruser");
      if (creator == null) creator = createUser(usrMgmt, "creator");
      if (publisher == null) publisher = createUser(usrMgmt, "publisher");
      if (directLink == null) directLink = createUser(usrMgmt, "direct-link");
      return true;
    }
    return false;
  }

  private static final void initRoles() throws SQLException {
    MapperRegistry registry = MapperRegistry.getInstance();
    IRoleManagement roleMgmt = registry.getRoleManagement();

    Role adminRole = (Role)roleMgmt.findByName("ADMIN");
    Role poweruserRole = (Role)roleMgmt.findByName("POWERUSER");
    Role editorRole = (Role)roleMgmt.findByName("EDITOR");
    Role creatorRole = (Role)roleMgmt.findByName("CREATOR");
    Role publisherRole = (Role)roleMgmt.findByName("PUBLISHER");
    Role viewerRole = (Role)roleMgmt.findByName("VIEWER");
    Role ownerRole = (Role)roleMgmt.findByName("OWNER");

    if (adminRole == null)
    {
      adminRole = createRole(roleMgmt, "ADMIN", Right.GRANT);
      if (editorRole == null) editorRole = createRole(roleMgmt, "EDITOR", Right.CREATE);
      if (viewerRole == null) viewerRole = createRole(roleMgmt, "VIEWER", Right.READ);
      if (ownerRole == null) ownerRole = createRole(roleMgmt, "OWNER", Right.CREATE);
      if (poweruserRole == null) poweruserRole = createRole(roleMgmt, "POWERUSER", Right.GRANT);
      if (creatorRole == null) creatorRole = createRole(roleMgmt, "CREATOR", Right.CREATE);
      if (publisherRole == null) publisherRole = createRole(roleMgmt, "PUBLISHER", Right.GRANT);
    }

    if (adminRole != null) {
      adminRole.setDescription("Grants the right to view & edit administration area and modify & share all existing views (System)");
      roleMgmt.update(adminRole);
    }
    if (poweruserRole != null) {
      poweruserRole.setDescription("Grants the right to share views created by this user");
      roleMgmt.update(poweruserRole);
    }
    if (editorRole != null) {
      editorRole.setDescription("Grants the right to modify views shared by other users (System)");
      roleMgmt.update(editorRole);
    }
    if (creatorRole != null) {
      creatorRole.setDescription("Grants the right to create and modify own views");
      roleMgmt.update(creatorRole);
    }
    if (viewerRole != null) {
      viewerRole.setDescription("Grants the right to see views shared by other users (System)");
      roleMgmt.update(viewerRole);
    }
    if (ownerRole != null) {
      ownerRole.setDescription("Grants the right to create views and edit these views (System)");
      roleMgmt.update(ownerRole);
    }
    if (publisherRole != null) {
      publisherRole.setDescription("Grants the right to create, modify and publish own views");
      roleMgmt.update(publisherRole);
    }
  }

  static final void mapRoles(UserImpl usr, Role[] roles) throws SQLException {
    if ((usr == null) || (roles == null)) {
      return;
    }
    IUserRoleManagement mgmt = MapperRegistry.getInstance().getUserRoleAssociation();
    for (Role r : roles) {
      if (r == null) {
        continue;
      }
      if (!usr.hasRole(r)) {
        mgmt.insert(usr, r);
        usr.add(r);
      }
    }
  }

  static final void initUserRoleAssociations() throws SQLException {
    MapperRegistry registry = MapperRegistry.getInstance();

    String adminUser = rb.getString("jpalo.admin.user");

    IUserManagement usrMgmt = registry.getUserManagement();
    UserImpl admin = (UserImpl)usrMgmt.findByName(adminUser);
    UserImpl poweruser = (UserImpl)usrMgmt.findByName("poweruser");
    UserImpl editor = (UserImpl)usrMgmt.findByName("editor");
    UserImpl creator = (UserImpl)usrMgmt.findByName("creator");
    UserImpl viewer = (UserImpl)usrMgmt.findByName("viewer");
    UserImpl directLink = (UserImpl)usrMgmt.findByName("direct-link");
    UserImpl publisher = (UserImpl)usrMgmt.findByName("publisher");

    IRoleManagement roleMgmt = registry.getRoleManagement();
    Role adminRole = (Role)roleMgmt.findByName("ADMIN");
    Role poweruserRole = (Role)roleMgmt.findByName("POWERUSER");
    Role editorRole = (Role)roleMgmt.findByName("EDITOR");
    Role creatorRole = (Role)roleMgmt.findByName("CREATOR");
    Role viewerRole = (Role)roleMgmt.findByName("VIEWER");
    Role publisherRole = (Role)roleMgmt.findByName("PUBLISHER");

    mapRoles(admin, new Role[] { adminRole, editorRole, viewerRole });
    mapRoles(poweruser, new Role[] { poweruserRole, editorRole, viewerRole });
    mapRoles(editor, new Role[] { editorRole, viewerRole });
    mapRoles(creator, new Role[] { creatorRole, viewerRole });
    mapRoles(publisher, new Role[] { publisherRole, viewerRole });
    mapRoles(viewer, new Role[] { viewerRole });
    mapRoles(directLink, new Role[] { viewerRole });
  }

  static final void createConnection(IConnectionManagement conMgmt, String name, String host, String port, int type) throws SQLException {
    PaloConnection paloCon = conMgmt.findBy(host, port);
    if (paloCon != null)
      return;
    paloCon = new PaloConnectionImpl.Builder(null).name(name)
      .host(host).service(port).type(type).build();
    conMgmt.insert(paloCon);
  }

  static final void initDefaultConnections() throws SQLException
  {
    MapperRegistry registry = MapperRegistry.getInstance();
    IConnectionManagement conMgmt = registry.getConnectionManagement();

    String mondrianConnectionService = rb.getString("jpalo.mondrian.connection.service");
    String mondrianConnectionUrl = rb.getString("jpalo.mondrian.connection.url");
    String mondrianConnectionName = rb.getString("jpalo.mondrian.connection.name");

    createConnection(conMgmt, "Palo", "localhost", "7777", 2);
    createConnection(conMgmt, mondrianConnectionName, mondrianConnectionUrl, mondrianConnectionService, 3);
    createConnection(conMgmt, "MS AS 2000", "localhost", "xmla/msxisapi.dll", 3);
    createConnection(conMgmt, "MS AS 2005", "localhost", "olap/msmdpump.dll", 3);
    createConnection(conMgmt, "SAP BW", "10.0.0.113:8011", "sap/bw/xml/soap/xmla", 3);
  }

  static final void createAccount(IAccountManagement accMgmt, UserImpl user, PaloConnection paloCon, String login, String pass) throws SQLException {
    if (user == null) {
      return;
    }
    Account paloAccount = accMgmt.findBy(user, paloCon);
    if (paloAccount != null)
      return;
    paloAccount = new AccountImpl.Builder(null, user.getId())
      .connection(paloCon.getId()).username(login)
      .password(pass).build();
    accMgmt.insert(paloAccount);
    user.add(paloAccount);
  }

  static final void initDefaultAccounts() throws SQLException
  {
    MapperRegistry registry = MapperRegistry.getInstance();
    IConnectionManagement conMgmt = registry.getConnectionManagement();

    PaloConnection paloCon = conMgmt.findBy("localhost", "7777");
    if (paloCon == null) {
      return;
    }

    String adminUser = rb.getString("jpalo.admin.user");

    IUserManagement usrMgmt = registry.getUserManagement();
    UserImpl admin = (UserImpl)usrMgmt.findByName(adminUser);
    UserImpl poweruser = (UserImpl)usrMgmt.findByName("poweruser");
    UserImpl editor = (UserImpl)usrMgmt.findByName("editor");
    UserImpl creator = (UserImpl)usrMgmt.findByName("creator");
    UserImpl viewer = (UserImpl)usrMgmt.findByName("viewer");
    UserImpl directLink = (UserImpl)usrMgmt.findByName("direct-link");
    UserImpl publisher = (UserImpl)usrMgmt.findByName("publisher");

    IAccountManagement accMgmt = registry.getAccountManagement();
    createAccount(accMgmt, admin, paloCon, "admin", "admin");
    createAccount(accMgmt, editor, paloCon, "editor", "editor");
    createAccount(accMgmt, viewer, paloCon, "viewer", "viewer");
    createAccount(accMgmt, poweruser, paloCon, "poweruser", "poweruser");
    createAccount(accMgmt, creator, paloCon, "editor", "editor");
    createAccount(accMgmt, publisher, paloCon, "editor", "editor");
    createAccount(accMgmt, directLink, paloCon, "viewer", "viewer");
  }

  static final GroupImpl createGroup(IGroupManagement grpMgmt, String name, Role[] roles) throws SQLException {
    GroupImpl group = new GroupImpl(null);
    group.setName(name);
    grpMgmt.insert(group);

    if (roles == null) {
      return group;
    }
    IGroupRoleManagement groles = MapperRegistry.getInstance().getGroupRoleAssociation();
    for (Role r : roles) {
      if (r == null) {
        continue;
      }
      groles.insert(group, r);
      group.add(r);
    }
    return group;
  }

  static final void initGroups() throws SQLException {
    MapperRegistry registry = MapperRegistry.getInstance();
    IGroupManagement grpMgmt = registry.getGroupManagement();
    Group adminGroup = (Group)grpMgmt.findByName("admin");
    Group poweruserGroup = (Group)grpMgmt.findByName("poweruser");
    Group editorGroup = (Group)grpMgmt.findByName("editor");
    Group creatorGroup = (Group)grpMgmt.findByName("creator");
    Group viewerGroup = (Group)grpMgmt.findByName("viewer");
    Group publisherGroup = (Group)grpMgmt.findByName("publisher");

    IRoleManagement roleMgmt = registry.getRoleManagement();
    Role adminRole = (Role)roleMgmt.findByName("ADMIN");
    Role poweruserRole = (Role)roleMgmt.findByName("POWERUSER");
    Role editorRole = (Role)roleMgmt.findByName("EDITOR");
    Role creatorRole = (Role)roleMgmt.findByName("CREATOR");
    Role viewerRole = (Role)roleMgmt.findByName("VIEWER");
    Role publisherRole = (Role)roleMgmt.findByName("PUBLISHER");

    if (adminGroup == null) {
      adminGroup = createGroup(grpMgmt, "admin", new Role[] { adminRole, editorRole, viewerRole });
      if (editorGroup == null) editorGroup = createGroup(grpMgmt, "editor", new Role[] { editorRole, viewerRole });
      if (viewerGroup == null) viewerGroup = createGroup(grpMgmt, "viewer", new Role[] { viewerRole });
      if (poweruserGroup == null) poweruserGroup = createGroup(grpMgmt, "poweruser", new Role[] { poweruserRole, editorRole, viewerRole });
      if (creatorGroup == null) creatorGroup = createGroup(grpMgmt, "creator", new Role[] { creatorRole, viewerRole });
      if (publisherGroup == null) publisherGroup = createGroup(grpMgmt, "publisher", new Role[] { publisherRole, viewerRole });
    }
    if (adminGroup != null) {
      ((GroupImpl)adminGroup).setDescription("Grants the right to view & edit administration area and modify & share all existing views (System)");
      grpMgmt.update(adminGroup);
    }
    if (poweruserGroup != null) {
      ((GroupImpl)poweruserGroup).setDescription("Grants the right to share views created by this user");
      grpMgmt.update(poweruserGroup);
    }
    if (editorGroup != null) {
      ((GroupImpl)editorGroup).setDescription("Grants the right to modify views shared by other users (System)");
      grpMgmt.update(editorGroup);
    }
    if (creatorGroup != null) {
      ((GroupImpl)creatorGroup).setDescription("Grants the right to create and modify own views");
      grpMgmt.update(creatorGroup);
    }
    if (viewerGroup != null) {
      ((GroupImpl)viewerGroup).setDescription("Grants the right to see views shared by other users (System)");
      grpMgmt.update(viewerGroup);
    }
    if (publisherGroup != null) {
      ((GroupImpl)publisherGroup).setDescription("Grants the right to create, modify and publish own views");
      grpMgmt.update(publisherGroup);
    }
  }

  static final void initialize(DbConnection paramDbConnection, boolean paramBoolean)
  throws SQLException
{
  if ((paramDbConnection == null) || (paramDbConnection.equals(connection)))
    return;
  if (connection != null)
    throw new RuntimeException("Already initialized");
  if (connection != null)
    release(connection);
  connection = paramDbConnection;
  if (!connection.isConnected())
    connection.connect();
  try
  {
    boolean bool = initUsers();
    initRoles();
    if (bool)
    {
      initUserRoleAssociations();
      initDefaultConnections();
      if (paramBoolean)
        initDefaultAccounts();
    }
    initGroups();
  }
  catch (Throwable localThrowable)
  {
    localThrowable.printStackTrace();
  }
}

  static final void initSpagoBIAccount() throws SQLException {
    MapperRegistry registry = MapperRegistry.getInstance();
    IAccountManagement accMgmt = registry.getAccountManagement();
    IUserManagement usrMgmt = registry.getUserManagement();
    IConnectionManagement conMgmt = registry.getConnectionManagement();

    String spagobiUser = rb.getString("jpalo.admin.user");
    String spagobiPwd = rb.getString("jpalo.admin.user");
    String mondrianName = rb.getString("jpalo.mondrian.connection.name");

    UserImpl admin = (UserImpl)usrMgmt.findByName(spagobiUser);
    PaloConnection connection = (PaloConnection)conMgmt.findByName(mondrianName);
    createAccount(accMgmt, admin, connection, spagobiUser, spagobiPwd);
  }

  public static final void release(DbConnection connection) {
    if (connection != null) {
      connection.disconnect();
      if (!connection.equals(connection))
        return;
      MapperRegistry.getInstance().clearCaches();
      connection = null;
    }
  }

  public static final DbConnection getDbConnection()
  {
    return connection;
  }

  public static final Connection getConnection()
  {
    checkConnection();
    return connection.getConnection();
  }

  public static final String getQuery(String key) {
    return connection.getSqlCommands().getProperty(key);
  }
  public static final String getQuery(String key, String[] parameters) {
    String format = connection.getSqlCommands().getProperty(key);
    if (format != null) {
      String result = MessageFormat.format(format, parameters);
      return result;
    }
    return null;
  }

  public static final void checkConnection() {
    if ((connection == null) || (!connection.isConnected()))
      throw new NotConnectedException("No database connection established!");
  }
}