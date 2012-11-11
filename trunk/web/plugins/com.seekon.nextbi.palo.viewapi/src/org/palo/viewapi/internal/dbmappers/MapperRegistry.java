/*     */package org.palo.viewapi.internal.dbmappers;

/*     */
/*     */import org.palo.viewapi.Account;
import org.palo.viewapi.AuthUser;
import org.palo.viewapi.DomainObject;
import org.palo.viewapi.Group;
import org.palo.viewapi.PaloConnection;
import org.palo.viewapi.Report;
import org.palo.viewapi.Role;
import org.palo.viewapi.User;
import org.palo.viewapi.View;
import org.palo.viewapi.internal.AdminServiceImpl;
import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.ExplorerTreeNode;
import org.palo.viewapi.internal.IAccountManagement;
import org.palo.viewapi.internal.IConnectionManagement;
import org.palo.viewapi.internal.IFolderManagement;
import org.palo.viewapi.internal.IFolderRoleManagement;
import org.palo.viewapi.internal.IGroupManagement;
import org.palo.viewapi.internal.IGroupRoleManagement;
import org.palo.viewapi.internal.IReportManagement;
import org.palo.viewapi.internal.IReportRoleManagement;
import org.palo.viewapi.internal.IReportViewManagement;
import org.palo.viewapi.internal.IRoleManagement;
import org.palo.viewapi.internal.IUserGroupManagement;
import org.palo.viewapi.internal.IUserManagement;
import org.palo.viewapi.internal.IUserRoleManagement;
import org.palo.viewapi.internal.IViewManagement;
import org.palo.viewapi.internal.IViewRoleManagement;
import org.palo.viewapi.internal.UserImpl;

/*     */
/*     */public final class MapperRegistry
/*     */{
  /* 110 */private static final MapperRegistry instance = new MapperRegistry();

  /*     */private final UserMapper usrMapper;

  /*     */private final RoleMapper roleMapper;

  /*     */private final GroupMapper groupMapper;

  /*     */private final ViewMapper viewMapper;

  /*     */private final FolderMapper folderMapper;

  /*     */private final ReportMapper reportMapper;

  /*     */private final AccountMapper accountMapper;

  /*     */private final ConnectionMapper connectionMapper;

  /*     */private final UserRoleAssociation usrRoleAssoc;

  /*     */private final UserGroupAssociation usrGroupAssoc;

  /*     */private final GroupRoleAssociation grpRoleAssoc;

  /*     */private final ViewRoleAssociation viewRoleAssoc;

  /*     */private final FolderRoleAssociation folderRoleAssoc;

  /*     */private final ReportRoleAssociation repRoleAssoc;

  /*     */private final ReportViewAssociation repViewAssoc;

  /*     */
  /*     */public static final void checkAccess(Class<? extends DomainObject> forObj)
  /*     */{
    /* 87 */Class mapper = getMapper(forObj);
    /* 88 */Class[] elements = new AccessChecker().getClassContext();
    /*     */
    /* 90 */int lastCaller = Math.min(10, elements.length);
    /* 91 */boolean accessOk = false;
    /* 92 */for (int i = 0; i < lastCaller; ++i) {
      /* 93 */if ((!elements[i].equals(mapper)) &&
      /* 94 */(!elements[i].equals(UserImpl.class)) &&
      /* 95 */(!elements[i].equals(DbService.class)) &&
      /* 96 */(!elements[i].equals(AdminServiceImpl.class)))
        continue;
      /* 97 */accessOk = true;
      /* 98 */break;
      /*     */}
    /*     */
    /* 101 */if (!accessOk)
      /* 102 */throw new IllegalStateException("Illegal access!");
    /*     */}

  /*     */
  /*     */public static final MapperRegistry getInstance()
  /*     */{
    /* 124 */return instance;
    /*     */}

  /*     */
  /*     */private MapperRegistry()
  /*     */{
    /* 150 */this.usrMapper = new UserMapper();
    /* 151 */this.roleMapper = new RoleMapper();
    /* 152 */this.groupMapper = new GroupMapper();
    /* 153 */this.connectionMapper = new ConnectionMapper();
    /* 154 */this.accountMapper = new AccountMapper();
    /* 155 */this.viewMapper = new ViewMapper();
    /* 156 */this.folderMapper = new FolderMapper();
    /* 157 */this.reportMapper = new ReportMapper();
    /*     */
    /* 161 */this.grpRoleAssoc = new GroupRoleAssociation();
    /* 162 */this.usrRoleAssoc = new UserRoleAssociation();
    /* 163 */this.usrGroupAssoc = new UserGroupAssociation();
    /* 164 */this.viewRoleAssoc = new ViewRoleAssociation();
    /* 165 */this.folderRoleAssoc = new FolderRoleAssociation();
    /* 166 */this.repRoleAssoc = new ReportRoleAssociation();
    /* 167 */this.repViewAssoc = new ReportViewAssociation();
    /*     */}

  /*     */
  /*     */public final IAccountManagement getAccountManagement() {
    /* 171 */return this.accountMapper;
    /*     */}

  /*     */
  /*     */public final IUserManagement getUserManagement() {
    /* 175 */return this.usrMapper;
    /*     */}

  /*     */
  /*     */public final IRoleManagement getRoleManagement() {
    /* 179 */return this.roleMapper;
    /*     */}

  /*     */
  /*     */public final IGroupManagement getGroupManagement() {
    /* 183 */return this.groupMapper;
    /*     */}

  /*     */
  /*     */public final IViewManagement getViewManagement() {
    /* 187 */return this.viewMapper;
    /*     */}

  /*     */
  /*     */public final IFolderManagement getFolderManagement() {
    /* 191 */return this.folderMapper;
    /*     */}

  /*     */
  /*     */public final IReportManagement getReportManagement() {
    /* 195 */return this.reportMapper;
    /*     */}

  /*     */
  /*     */public final IConnectionManagement getConnectionManagement() {
    /* 199 */return this.connectionMapper;
    /*     */}

  /*     */
  /*     */public final IGroupRoleManagement getGroupRoleAssociation() {
    /* 203 */return this.grpRoleAssoc;
    /*     */}

  /*     */
  /*     */public final IUserRoleManagement getUserRoleAssociation() {
    /* 207 */return this.usrRoleAssoc;
    /*     */}

  /*     */public final IUserGroupManagement getUserGroupAssociation() {
    /* 210 */return this.usrGroupAssoc;
    /*     */}

  /*     */public final IViewRoleManagement getViewRoleAssociation() {
    /* 213 */return this.viewRoleAssoc;
    /*     */}

  /*     */
  /*     */public final IFolderRoleManagement getFolderRoleAssociation() {
    /* 217 */return this.folderRoleAssoc;
    /*     */}

  /*     */
  /*     */public final IReportRoleManagement getReportRoleAssociation() {
    /* 221 */return this.repRoleAssoc;
    /*     */}

  /*     */
  /*     */public final IReportViewManagement getReportViewAssociation() {
    /* 225 */return this.repViewAssoc;
    /*     */}

  /*     */
  /*     */public final void clearCaches()
  /*     */{
    /* 230 */this.usrMapper.reset();
    /* 231 */this.roleMapper.reset();
    /* 232 */this.groupMapper.reset();
    /* 233 */this.connectionMapper.reset();
    /* 234 */this.accountMapper.reset();
    /* 235 */this.viewMapper.reset();
    /* 236 */this.folderMapper.reset();
    /* 237 */this.reportMapper.reset();
    /*     */}

  /*     */
  /*     */private static final Class<? extends Mapper> getMapper(
    Class<? extends DomainObject> forObj)
  /*     */{
    /* 248 */if (Account.class.isAssignableFrom(forObj))
      /* 249 */return AccountMapper.class;
    /* 250 */if (PaloConnection.class.isAssignableFrom(forObj))
      /* 251 */return ConnectionMapper.class;
    /* 252 */if (Group.class.isAssignableFrom(forObj))
      /* 253 */return GroupMapper.class;
    /* 254 */if (Report.class.isAssignableFrom(forObj))
      /* 255 */return ReportMapper.class;
    /* 256 */if (Role.class.isAssignableFrom(forObj))
      /* 257 */return RoleMapper.class;
    /* 258 */if (User.class.isAssignableFrom(forObj))
      /* 259 */return UserMapper.class;
    /* 260 */if (AuthUser.class.isAssignableFrom(forObj))
      /* 261 */return UserMapper.class;
    /* 262 */if (View.class.isAssignableFrom(forObj))
      /* 263 */return ViewMapper.class;
    /* 264 */if (ExplorerTreeNode.class.isAssignableFrom(forObj)) {
      /* 265 */return FolderMapper.class;
      /*     */}
    /*     */
    /* 269 */throw new IllegalStateException("Unkown domain object!");
    /*     */}

  /*     */
  /*     */private static final class AccessChecker extends SecurityManager
  /*     */{
    /*     */public Class[] getClassContext()
    /*     */{
      /* 81 */return super.getClassContext();
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.internal.dbmappers.MapperRegistry
 * JD-Core Version: 0.5.4
 */