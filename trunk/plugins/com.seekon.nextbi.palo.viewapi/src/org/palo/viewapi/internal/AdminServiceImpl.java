/*     */ package org.palo.viewapi.internal;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.palo.viewapi.Account;
/*     */ import org.palo.viewapi.AuthUser;
/*     */ import org.palo.viewapi.Group;
/*     */ import org.palo.viewapi.PaloConnection;
/*     */ import org.palo.viewapi.Right;
/*     */ import org.palo.viewapi.Role;
/*     */ import org.palo.viewapi.User;
/*     */ import org.palo.viewapi.exceptions.OperationFailedException;
/*     */ import org.palo.viewapi.internal.dbmappers.MapperRegistry;
/*     */ import org.palo.viewapi.services.AdministrationService;
/*     */ 
/*     */ public final class AdminServiceImpl extends InternalService
/*     */   implements AdministrationService
/*     */ {
/*     */   AdminServiceImpl(AuthUser user)
/*     */   {
/*  61 */     super(user);
/*     */   }
/*     */ 
/*     */   public final User getUser(String id)
/*     */   {
/*  68 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/*  70 */       IUserManagement usrMgmt = getUserManagement();
/*  71 */       usrMgmt.reset();
/*  72 */       return (User)usrMgmt.find(id);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public final List<User> getUsers() {
/*  79 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/*  81 */       IUserManagement usrMgmt = getUserManagement();
/*  82 */       usrMgmt.reset();
/*  83 */       return usrMgmt.findAll();
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/*  86 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   public final User createUser(String firstname, String lastname, String login, String password) throws OperationFailedException {
/*     */     try {
/*  91 */       IUserManagement usrMgmt = getUserManagement();
/*  92 */       UserImpl usr = new UserImpl("");
/*  93 */       password = UserImpl.encrypt(password);
/*  94 */       usr.setFirstname(firstname);
/*  95 */       usr.setLastname(lastname);
/*  96 */       usr.setLoginName(login);
/*  97 */       usr.setPassword(password);
/*     */ 
/*  99 */       usrMgmt.insert(usr);
/* 100 */       return usr;
/*     */     } catch (SQLException e) {
/* 102 */       throw new OperationFailedException("Failed to create user", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void delete(User user)
/*     */     throws OperationFailedException
/*     */   {
/*     */     try
/*     */     {
/* 117 */       IUserManagement usrMgmt = getUserManagement();
/* 118 */       usrMgmt.delete(user);
/*     */     } catch (SQLException e) {
/* 120 */       e.printStackTrace();
/* 121 */       throw new OperationFailedException("Failed to delete user", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void save(User user) throws OperationFailedException {
/*     */     try {
/* 127 */       IUserManagement usrMgmt = getUserManagement();
/* 128 */       usrMgmt.update(user);
/*     */     } catch (SQLException e) {
/* 130 */       throw new OperationFailedException("Failed to save user", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFirstname(String firstname, User ofUser) {
/* 134 */     UserImpl user = (UserImpl)ofUser;
/* 135 */     user.setFirstname(firstname);
/*     */   }
/*     */   public void setLastname(String lastname, User ofUser) {
/* 138 */     UserImpl user = (UserImpl)ofUser;
/* 139 */     user.setLastname(lastname);
/*     */   }
/*     */   public void setLoginName(String name, User ofUser) {
/* 142 */     UserImpl user = (UserImpl)ofUser;
/* 143 */     user.setLoginName(name);
/*     */   }
/*     */   public void setPassword(String password, User ofUser) {
/* 146 */     UserImpl user = (UserImpl)ofUser;
/*     */ 
/* 148 */     user.setPassword(password);
/*     */   }
/*     */ 
/*     */   public final Group getGroup(String id)
/*     */   {
/* 155 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 157 */       IGroupManagement grpMgmt = getGroupManagement();
/* 158 */       return (Group)grpMgmt.find(id);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 161 */     return null;
/*     */   }
/*     */   public final List<Group> getGroups() {
/* 164 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 166 */       IGroupManagement grpMgmt = getGroupManagement();
/* 167 */       return grpMgmt.findAll();
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 170 */     return new ArrayList();
/*     */   }
/*     */   public final List<Group> getGroups(User forUser) {
/* 173 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 175 */       IGroupManagement grpMgmt = getGroupManagement();
/* 176 */       IUserGroupManagement ugAssoc = this.mapperReg.getUserGroupAssociation();
/* 177 */       List<String> groupIDs = ugAssoc.getGroups(forUser);
/* 178 */       List groups = new ArrayList();
/* 179 */       for (String id : groupIDs) {
/* 180 */         Group group = (Group)grpMgmt.find(id);
/* 181 */         if (group != null)
/* 182 */           groups.add(group);
/*     */       }
/* 184 */       return groups;
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 187 */     return new ArrayList();
/*     */   }
/*     */   public final Group createGroup(String name) throws OperationFailedException {
/*     */     try {
/* 191 */       IGroupManagement grpMgmt = getGroupManagement();
/* 192 */       GroupImpl group = new GroupImpl(null);
/* 193 */       group.setName(name);
/* 194 */       grpMgmt.insert(group);
/* 195 */       return group;
/*     */     } catch (SQLException e) {
/* 197 */       throw new OperationFailedException("Failed to create group", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void delete(Group group) throws OperationFailedException {
/* 201 */     IUserManagement usrMgmt = getUserManagement();
/* 202 */     IGroupManagement grpMgmt = getGroupManagement();
/*     */     try {
/* 204 */       grpMgmt.delete(group);
/*     */     } catch (SQLException e) {
/* 206 */       throw new OperationFailedException("Failed to delete group", e);
/*     */     }
/*     */ 
/* 209 */     GroupImpl _group = (GroupImpl)group;
/* 210 */     List<User> users = _group.getUsers();
/* 211 */     for (User user : users)
/*     */       try {
/* 213 */         UserImpl usr = (UserImpl)usrMgmt.find(user.getId());
/* 214 */         if (usr != null)
/* 215 */           usr.remove(group);
/*     */       }
/*     */       catch (SQLException localSQLException1) {
/*     */       }
/* 219 */     List<Role> roles = group.getRoles();
/* 220 */     for (Role role : roles)
/* 221 */       remove(role, group);
/*     */   }
/*     */ 
/*     */   public final void save(Group group) throws OperationFailedException {
/*     */     try {
/* 226 */       IGroupManagement grpMgmt = getGroupManagement();
/* 227 */       grpMgmt.update(group);
/*     */     } catch (SQLException e) {
/* 229 */       throw new OperationFailedException("Failed to save group", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDescription(String description, Group ofGroup) {
/* 233 */     GroupImpl group = (GroupImpl)ofGroup;
/* 234 */     group.setDescription(description);
/*     */   }
/*     */   public void setName(String name, Group ofGroup) {
/* 237 */     GroupImpl group = (GroupImpl)ofGroup;
/* 238 */     group.setName(name);
/*     */   }
/*     */ 
/*     */   public final Role getRoleByName(String name)
/*     */   {
/* 244 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 246 */       IRoleManagement roleMgmt = getRoleManagement();
/* 247 */       return (Role)roleMgmt.findByName(name);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 250 */     return null;
/*     */   }
/*     */   public final Role getRole(String id) {
/* 253 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 255 */       IRoleManagement roleMgmt = getRoleManagement();
/* 256 */       return (Role)roleMgmt.find(id);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 259 */     return null;
/*     */   }
/*     */   public final List<Role> getRoles() {
/* 262 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 264 */       IRoleManagement roleMgmt = getRoleManagement();
/* 265 */       return roleMgmt.findAll();
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 268 */     return new ArrayList();
/*     */   }
/*     */   public final List<Role> getRoles(User forUser) {
/* 271 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 273 */       IRoleManagement roleMgmt = getRoleManagement();
/* 274 */       IUserRoleManagement urAssoc = this.mapperReg.getUserRoleAssociation();
/* 275 */       List<String> roleIDs = urAssoc.getRoles(forUser);
/* 276 */       List roles = new ArrayList();
/* 277 */       for (String id : roleIDs) {
/* 278 */         Role role = (Role)roleMgmt.find(id);
/* 279 */         if (role != null)
/* 280 */           roles.add(role);
/*     */       }
/* 282 */       return roles;
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 285 */     return new ArrayList();
/*     */   }
/*     */   public final Role createRole(String name) throws OperationFailedException {
/* 288 */     return createRole(name, Right.NONE);
/*     */   }
/*     */ 
/*     */   public final Role createRole(String name, Right right) throws OperationFailedException {
/*     */     try {
/* 293 */       IRoleManagement roleMgmt = getRoleManagement();
/* 294 */       RoleImpl role = new RoleImpl(null);
/* 295 */       role.setName(name);
/* 296 */       role.setPermission(right);
/* 297 */       roleMgmt.insert(role);
/* 298 */       return role;
/*     */     } catch (SQLException e) {
/* 300 */       throw new OperationFailedException("Failed to create role", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void delete(Role role) throws OperationFailedException {
/* 304 */     IUserManagement usrMgmt = getUserManagement();
/* 305 */     IRoleManagement roleMgmt = getRoleManagement();
/* 306 */     IGroupManagement groupMgmt = getGroupManagement();
/*     */     try
/*     */     {
/* 309 */       roleMgmt.delete(role);
/*     */     } catch (SQLException e) {
/* 311 */       throw new OperationFailedException("Failed to delete role", e);
/*     */     }
/*     */     try
/*     */     {
/* 315 */       List<Group> groups = groupMgmt.findAllGroupsFor(role);
/* 316 */       for (Group group : groups)
/* 317 */         remove(role, group);
/*     */     } catch (SQLException e) {
/* 319 */       throw new OperationFailedException(
/* 320 */         "Failed to delete role from groups", e);
/*     */     }
/*     */     try {
/* 323 */       List<User> users = usrMgmt.findAllUsersFor(role);
/* 324 */       for (User user : users)
/* 325 */         remove(role, user);
/*     */     } catch (SQLException e) {
/* 327 */       throw new OperationFailedException(
/* 328 */         "Failed to delete role from users", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void save(Role role) throws OperationFailedException
/*     */   {
/*     */     try {
/* 335 */       IRoleManagement roleMgmt = getRoleManagement();
/* 336 */       roleMgmt.update(role);
/*     */     } catch (SQLException e) {
/* 338 */       throw new OperationFailedException("Failed to delete role", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDescription(String description, Role ofRole) {
/* 342 */     RoleImpl role = (RoleImpl)ofRole;
/* 343 */     role.setDescription(description);
/*     */   }
/*     */   public void setName(String name, Role ofRole) {
/* 346 */     RoleImpl role = (RoleImpl)ofRole;
/* 347 */     role.setName(name);
/*     */   }
/*     */   public void setPermission(Right right, Role ofRole) {
/* 350 */     RoleImpl role = (RoleImpl)ofRole;
/* 351 */     role.setPermission(right);
/*     */   }
/*     */ 
/*     */   public final PaloConnection getConnection(String id)
/*     */   {
/* 357 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 359 */       IConnectionManagement connMgmt = getConnectionManagement();
/* 360 */       return (PaloConnection)connMgmt.find(id);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 363 */     return null;
/*     */   }
/*     */ 
/*     */   public final List<PaloConnection> getConnections() {
/* 367 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 369 */       IConnectionManagement connMgmt = getConnectionManagement();
/* 370 */       return connMgmt.findAll();
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 373 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   public final PaloConnection createConnection(String name, String host, String service, int type) throws OperationFailedException {
/*     */     try {
/* 378 */       IConnectionManagement connMgmt = getConnectionManagement();
/* 379 */       PaloConnectionImpl connection = new PaloConnectionImpl(null);
/* 380 */       connection.setName(name);
/* 381 */       connection.setHost(host);
/* 382 */       connection.setService(service);
/* 383 */       connection.setType(type);
/* 384 */       connMgmt.insert(connection);
/* 385 */       return connection;
/*     */     } catch (SQLException e) {
/* 387 */       throw new OperationFailedException("Failed to create connection", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void delete(PaloConnection connection) throws OperationFailedException {
/*     */     try {
/* 393 */       IConnectionManagement connMgmt = getConnectionManagement();
/* 394 */       connMgmt.delete(connection);
/*     */     } catch (SQLException e) {
/* 396 */       throw new OperationFailedException("Failed to delete connection", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void save(PaloConnection connection) throws OperationFailedException
/*     */   {
/*     */     try {
/* 403 */       IConnectionManagement connMgmt = getConnectionManagement();
/* 404 */       connMgmt.update(connection);
/*     */     } catch (SQLException e) {
/* 406 */       throw new OperationFailedException("Failed to save connection", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setDescription(String description, PaloConnection ofConnection) {
/* 410 */     PaloConnectionImpl connection = (PaloConnectionImpl)ofConnection;
/* 411 */     connection.setDescription(description);
/*     */   }
/*     */   public void setName(String name, PaloConnection ofConnection) {
/* 414 */     PaloConnectionImpl connection = (PaloConnectionImpl)ofConnection;
/* 415 */     connection.setName(name);
/*     */   }
/*     */   public void setHost(String host, PaloConnection ofConnection) {
/* 418 */     PaloConnectionImpl connection = (PaloConnectionImpl)ofConnection;
/* 419 */     connection.setHost(host);
/*     */   }
/*     */   public void setService(String service, PaloConnection ofConnection) {
/* 422 */     PaloConnectionImpl connection = (PaloConnectionImpl)ofConnection;
/* 423 */     connection.setService(service);
/*     */   }
/*     */ 
/*     */   public void setType(int type, PaloConnection ofConnection) {
/* 427 */     PaloConnectionImpl connection = (PaloConnectionImpl)ofConnection;
/* 428 */     connection.setType(type);
/*     */   }
/*     */ 
/*     */   public final Account getAccount(String id)
/*     */   {
/* 433 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 435 */       IAccountManagement accountMgmt = getAccountManagement();
/* 436 */       return (Account)accountMgmt.find(id);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 439 */     return null;
/*     */   }
/*     */   public final List<Account> getAccounts() {
/* 442 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 444 */       IAccountManagement accountMgmt = getAccountManagement();
/* 445 */       return accountMgmt.findAll();
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 448 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   public final List<Account> getAccounts(User forUser) {
/* 452 */     AccessController.checkPermission(Right.READ, this.user);
/*     */     try {
/* 454 */       IAccountManagement accountMgmt = getAccountManagement();
/* 455 */       return accountMgmt.getAccounts(forUser);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 458 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   public final Account createAccount(String name, String password, User forUser, PaloConnection connection) throws OperationFailedException
/*     */   {
/*     */     try
/*     */     {
/* 465 */       UserImpl user = (UserImpl)forUser;
/* 466 */       IAccountManagement accountMgmt = getAccountManagement();
/*     */       AccountImpl account;
/* 468 */       if (connection.getType() == 4)
/* 469 */         account = new WSSAccountImpl(null, forUser.getId());
/*     */       else {
/* 471 */         account = new PaloAccountImpl(null, forUser.getId());
/*     */       }
/* 473 */       account.setConnection(connection);
/* 474 */       account.setLoginName(name);
/* 475 */       account.setPassword(password);
/* 476 */       accountMgmt.insert(account);
/*     */ 
/* 478 */       user.add(account);
/* 479 */       return account;
/*     */     } catch (SQLException e) {
/* 481 */       throw new OperationFailedException("Failed to create account", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void delete(Account account) throws OperationFailedException {
/*     */     try {
/* 487 */       UserImpl user = (UserImpl)account.getUser();
/* 488 */       user.remove(account);
/* 489 */       IAccountManagement accountMgmt = getAccountManagement();
/* 490 */       accountMgmt.delete(account);
/*     */     } catch (SQLException e) {
/* 492 */       throw new OperationFailedException("Failed to delete account", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void save(Account account) throws OperationFailedException {
/* 496 */     if (account == null)
/* 497 */       return;
/*     */     try
/*     */     {
/* 500 */       IAccountManagement accountMgmt = getAccountManagement();
/* 501 */       accountMgmt.update(account);
/*     */     } catch (SQLException e) {
/* 503 */       throw new OperationFailedException("Failed to save account", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setConnection(PaloConnection connection, Account ofAccount) {
/* 507 */     AccountImpl account = (AccountImpl)ofAccount;
/* 508 */     account.setConnection(connection);
/*     */   }
/*     */   public void setLoginName(String name, Account ofAccount) {
/* 511 */     AccountImpl account = (AccountImpl)ofAccount;
/* 512 */     account.setLoginName(name);
/*     */   }
/*     */   public void setPassword(String password, Account ofAccount) {
/* 515 */     AccountImpl account = (AccountImpl)ofAccount;
/* 516 */     account.setPassword(password);
/*     */   }
/*     */ 
/*     */   public void setUser(User user, Account ofAccount) {
/* 520 */     AccountImpl account = (AccountImpl)ofAccount;
/* 521 */     account.setUser(user);
/*     */   }
/*     */ 
/*     */   public final void add(User user, Group toGroup) throws OperationFailedException
/*     */   {
/* 526 */     UserImpl usr = (UserImpl)user;
/* 527 */     usr.add(toGroup);
/*     */ 
/* 529 */     if (toGroup.hasMember(user)) return;
/*     */     try {
/* 531 */       GroupImpl group = (GroupImpl)toGroup;
/*     */ 
/* 533 */       IUserGroupManagement ugAssoc = this.mapperReg
/* 534 */         .getUserGroupAssociation();
/* 535 */       ugAssoc.insert(user, group);
/*     */ 
/* 537 */       group.add(user);
/*     */     }
/*     */     catch (SQLException e) {
/* 540 */       usr.remove(toGroup);
/* 541 */       throw new OperationFailedException("Failed to add user '" + 
/* 542 */         user.getLastname() + "' to group '" + 
/* 543 */         toGroup.getName() + "'!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void add(Role role, User toUser) throws OperationFailedException
/*     */   {
/* 549 */     UserImpl usr = (UserImpl)toUser;
/* 550 */     usr.add(role);
/*     */   }
/*     */ 
/*     */   public void add(Role role, Group toGroup) throws OperationFailedException {
/* 554 */     GroupImpl _group = (GroupImpl)toGroup;
/* 555 */     _group.add(role);
/*     */   }
/*     */ 
/*     */   public final void remove(User user, Group fromGroup) throws OperationFailedException
/*     */   {
/* 560 */     UserImpl usr = (UserImpl)user;
/* 561 */     usr.remove(fromGroup);
/*     */ 
/* 563 */     if (!fromGroup.hasMember(user)) return;
/*     */     try {
/* 565 */       GroupImpl group = (GroupImpl)fromGroup;
/*     */ 
/* 567 */       IUserGroupManagement ugAssoc = this.mapperReg
/* 568 */         .getUserGroupAssociation();
/* 569 */       ugAssoc.delete(user, group);
/* 570 */       group.remove(user);
/*     */     }
/*     */     catch (SQLException e) {
/* 573 */       usr.add(fromGroup);
/* 574 */       throw new OperationFailedException("Failed to remove user '" + 
/* 575 */         user.getLastname() + "' from group '" + 
/* 576 */         fromGroup.getName() + "'!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(Role role, User fromUser)
/*     */     throws OperationFailedException
/*     */   {
/* 584 */     UserImpl usr = (UserImpl)fromUser;
/* 585 */     usr.remove(role);
/*     */   }
/*     */ 
/*     */   public void remove(Role role, Group fromGroup)
/*     */     throws OperationFailedException
/*     */   {
/* 591 */     GroupImpl _group = (GroupImpl)fromGroup;
/* 592 */     _group.remove(role);
/*     */   }
/*     */ 
/*     */   public final void setRoles(List<Role> roles, Group ofGroup) throws OperationFailedException
/*     */   {
/* 597 */     GroupImpl _group = (GroupImpl)ofGroup;
/* 598 */     _group.setRoles(roles);
/*     */   }
/*     */ 
/*     */   public final void setUsers(List<User> users, Group ofGroup)
/*     */     throws OperationFailedException
/*     */   {
/* 605 */     for (User user : ofGroup.getUsers()) {
/* 606 */       remove(user, ofGroup);
/*     */     }
/* 608 */     for (User user : users)
/* 609 */       add(user, ofGroup);
/*     */   }
/*     */ 
/*     */   public final void setGroups(List<Group> groups, User user) throws OperationFailedException
/*     */   {
/* 614 */     UserImpl _user = (UserImpl)user;
/*     */ 
/* 617 */     IGroupManagement grpMgmt = getGroupManagement();
/* 618 */     for (String groupId : _user.getGroupIDs())
/*     */       try {
/* 620 */         Group group = (Group)grpMgmt.find(groupId);
/* 621 */         if (group != null)
/* 622 */           remove(user, group);
/*     */       }
/*     */       catch (SQLException localSQLException)
/*     */       {
/*     */       }
/* 627 */     for (Group group : groups)
/* 628 */       add(user, group);
/*     */   }
/*     */ 
/*     */   public void setRoles(List<Role> roles, User user)
/*     */     throws OperationFailedException
/*     */   {
/* 634 */     UserImpl _user = (UserImpl)user;
/* 635 */     _user.setRoles(roles);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.AdminServiceImpl
 * JD-Core Version:    0.5.4
 */