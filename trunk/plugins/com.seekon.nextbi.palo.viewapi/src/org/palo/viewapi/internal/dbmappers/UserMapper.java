/*     */ package org.palo.viewapi.internal.dbmappers;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.palo.viewapi.Account;
/*     */ import org.palo.viewapi.DomainObject;
/*     */ import org.palo.viewapi.Role;
/*     */ import org.palo.viewapi.User;
/*     */ import org.palo.viewapi.internal.DbService;
/*     */ import org.palo.viewapi.internal.IAccountManagement;
/*     */ import org.palo.viewapi.internal.IGroupManagement;
/*     */ import org.palo.viewapi.internal.IRoleManagement;
/*     */ import org.palo.viewapi.internal.IUserGroupManagement;
/*     */ import org.palo.viewapi.internal.IUserManagement;
/*     */ import org.palo.viewapi.internal.IUserRoleManagement;
/*     */ import org.palo.viewapi.internal.UserImpl;
/*     */ import org.palo.viewapi.internal.UserImpl.Builder;
/*     */ 
/*     */ final class UserMapper extends AbstractMapper
/*     */   implements IUserManagement
/*     */ {
/*  70 */   private static final String TABLE = DbService.getQuery("Users.tableName");
/*  71 */   private static final String COLUMNS = DbService.getQuery("Users.columns");
/*  72 */   private static final String CREATE_TABLE_STMT = DbService.getQuery("Users.createTable", new String[] { TABLE });
/*  73 */   private static final String FIND_ALL_STMT = DbService.getQuery("Users.findAll", new String[] { COLUMNS, TABLE });
/*  74 */   private static final String FIND_BY_ID_STMT = DbService.getQuery("Users.findById", new String[] { COLUMNS, TABLE });
/*  75 */   private static final String FIND_BY_LOGIN_STMT = DbService.getQuery("Users.findByLogin", new String[] { COLUMNS, TABLE });
/*  76 */   private static final String INSERT_STMT = DbService.getQuery("Users.insert", new String[] { TABLE });
/*  77 */   private static final String UPDATE_STMT = DbService.getQuery("Users.update", new String[] { TABLE });
/*  78 */   private static final String DELETE_STMT = DbService.getQuery("Users.delete", new String[] { TABLE });
/*     */ 
/*     */   public final List<User> findAll() throws SQLException
/*     */   {
/*  82 */     PreparedStatement stmt = null;
/*  83 */     ResultSet results = null;
/*  84 */     List users = new ArrayList();
/*  85 */     Connection connection = DbService.getConnection();
/*     */     try {
/*  87 */       stmt = connection.prepareStatement(FIND_ALL_STMT);
/*  88 */       results = stmt.executeQuery();
/*  89 */       while (results.next()) {
/*  90 */         User user = (User)load(results);
/*  91 */         if (user != null)
/*  92 */           users.add(user);
/*     */       }
/*     */     } finally {
/*  95 */       cleanUp(stmt, results);
/*     */     }
/*  97 */     return users;
/*     */   }
/*     */ 
/*     */   public final void update(DomainObject obj) throws SQLException {
/* 101 */     User user = (User)obj;
/* 102 */     PreparedStatement stmt = null;
/* 103 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 105 */       stmt = connection.prepareStatement(UPDATE_STMT);
/* 106 */       stmt.setString(1, user.getFirstname());
/* 107 */       stmt.setString(2, user.getLastname());
/* 108 */       stmt.setString(3, user.getLoginName());
/* 109 */       stmt.setString(4, user.getPassword());
/* 110 */       stmt.setString(5, user.getId());
/* 111 */       stmt.execute();
/*     */ 
/* 113 */       handleAssociations(user);
/*     */     } finally {
/* 115 */       cleanUp(stmt);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected DomainObject doLoad(String id, ResultSet rs) throws SQLException {
/* 120 */     UserImpl.Builder usrBuilder = new UserImpl.Builder(id);
/* 121 */     usrBuilder.firstname(rs.getString(2));
/* 122 */     usrBuilder.lastname(rs.getString(3));
/* 123 */     usrBuilder.login(rs.getString(4));
/* 124 */     String pass = rs.getString(5);
/* 125 */     usrBuilder.password(pass);
/*     */ 
/* 127 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/*     */ 
/* 129 */     IUserGroupManagement ugAssoc = mapperReg.getUserGroupAssociation();
/* 130 */     List groups = ugAssoc.getGroups(id);
/* 131 */     usrBuilder.groups(groups);
/*     */ 
/* 133 */     IUserRoleManagement urAssoc = mapperReg.getUserRoleAssociation();
/* 134 */     List roles = urAssoc.getRoles(id);
/* 135 */     usrBuilder.roles(roles);
/*     */ 
/* 137 */     IAccountManagement accMgmt = mapperReg.getAccountManagement();
/* 138 */     List accounts = accMgmt.getAccounts(id);
/* 139 */     usrBuilder.accounts(accounts);
/*     */ 
/* 141 */     return usrBuilder.build();
/*     */   }
/*     */ 
/*     */   protected final void doInsert(DomainObject obj, PreparedStatement stmt) throws SQLException
/*     */   {
/* 146 */     User user = (User)obj;
/* 147 */     stmt.setString(1, user.getFirstname());
/* 148 */     stmt.setString(2, user.getLastname());
/* 149 */     stmt.setString(3, user.getLoginName());
/* 150 */     stmt.setString(4, user.getPassword());
/*     */ 
/* 152 */     handleAssociations(user);
/*     */   }
/*     */ 
/*     */   protected final void deleteAssociations(DomainObject obj) throws SQLException {
/* 156 */     User user = (User)obj;
/* 157 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/*     */ 
/* 160 */     IUserGroupManagement ugAssoc = mapperReg.getUserGroupAssociation();
/* 161 */     if (ugAssoc != null) {
/* 162 */       ugAssoc.delete(user);
/*     */     }
/*     */ 
/* 166 */     IUserRoleManagement urAssoc = mapperReg.getUserRoleAssociation();
/* 167 */     if (urAssoc != null) {
/* 168 */       urAssoc.delete(user);
/*     */     }
/*     */ 
/* 172 */     IAccountManagement accMgmt = mapperReg.getAccountManagement();
/* 173 */     List<Account> accounts = accMgmt.getAccounts(user);
/* 174 */     if (accounts != null)
/* 175 */       for (Account account : accounts)
/* 176 */         accMgmt.delete(account);
/*     */   }
/*     */ 
/*     */   protected final String deleteStatement()
/*     */   {
/* 181 */     return DELETE_STMT;
/*     */   }
/*     */   protected final String findStatement() {
/* 184 */     return FIND_BY_ID_STMT;
/*     */   }
/*     */   protected final String findByNameStatement() {
/* 187 */     return FIND_BY_LOGIN_STMT;
/*     */   }
/*     */   protected final String insertStatement() {
/* 190 */     return INSERT_STMT;
/*     */   }
/*     */ 
/*     */   protected final String createTableStatement() {
/* 194 */     return CREATE_TABLE_STMT;
/*     */   }
/*     */ 
/*     */   protected final String getTableName() {
/* 198 */     return TABLE;
/*     */   }
/*     */ 
/*     */   private final void handleAssociations(User user) throws SQLException {
/* 202 */     if (user.getId() == null) {
/* 203 */       return;
/*     */     }
/* 205 */     UserImpl usr = (UserImpl)user;
/*     */ 
/* 207 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/*     */ 
/* 209 */     IGroupManagement groupMgmt = mapperReg.getGroupManagement();
/* 210 */     IUserGroupManagement ugAssoc = mapperReg.getUserGroupAssociation();
/* 211 */     List<String> groups = usr.getGroupIDs();
/* 212 */     List<String> savedGroups = ugAssoc.getGroups(user);
/* 213 */     for (String id : groups) {
/* 214 */       if (!savedGroups.contains(id)) {
/* 215 */         ugAssoc.insert(user, groupMgmt.find(id));
/*     */       }
/*     */     }
/* 218 */     savedGroups.removeAll(groups);
/* 219 */     for (String id : savedGroups) {
/* 220 */       ugAssoc.delete(user, groupMgmt.find(id));
/*     */     }
/*     */ 
/* 224 */     IRoleManagement roleMgmt = mapperReg.getRoleManagement();
/* 225 */     IUserRoleManagement urAssoc = mapperReg.getUserRoleAssociation();
/* 226 */     List<String> roles = usr.getRoleIDs();
/* 227 */     List<String> savedRoles = urAssoc.getRoles(user);
/* 228 */     for (String id : roles) {
/* 229 */       if (!savedRoles.contains(id)) {
/* 230 */         urAssoc.insert(user, roleMgmt.find(id));
/*     */       }
/*     */     }
/* 233 */     savedRoles.removeAll(roles);
/* 234 */     for (String id : savedRoles)
/* 235 */       urAssoc.delete(user, roleMgmt.find(id));
/*     */   }
/*     */ 
/*     */   public List<User> findAllUsersFor(Role role)
/*     */     throws SQLException
/*     */   {
/* 253 */     MapperRegistry mapperReg = MapperRegistry.getInstance();
/* 254 */     IUserManagement usrMgmt = mapperReg.getUserManagement();
/* 255 */     IUserRoleManagement urAssoc = mapperReg.getUserRoleAssociation();
/* 256 */     List<String> userIDs = urAssoc.getUsers(role);
/* 257 */     List users = new ArrayList();
/* 258 */     for (String id : userIDs) {
/* 259 */       User user = (User)usrMgmt.find(id);
/* 260 */       if (user != null)
/* 261 */         users.add(user);
/*     */     }
/* 263 */     return users;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.dbmappers.UserMapper
 * JD-Core Version:    0.5.4
 */