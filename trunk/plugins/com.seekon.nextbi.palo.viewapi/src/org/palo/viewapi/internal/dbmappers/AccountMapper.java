/*     */ package org.palo.viewapi.internal.dbmappers;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.palo.viewapi.Account;
/*     */ import org.palo.viewapi.AuthUser;
/*     */ import org.palo.viewapi.DomainObject;
/*     */ import org.palo.viewapi.PaloConnection;
/*     */ import org.palo.viewapi.User;
/*     */ import org.palo.viewapi.internal.AccountImpl.Builder;
import org.palo.viewapi.internal.AccountImpl;
/*     */ import org.palo.viewapi.internal.DbService;
import org.palo.viewapi.internal.IAccountManagement;
/*     */ 
/*     */ final class AccountMapper extends AbstractMapper
/*     */   implements IAccountManagement
/*     */ {
/*  63 */   private static final String TABLE = DbService.getQuery("Accounts.tableName");
/*  64 */   private static final String COLUMNS = DbService.getQuery("Accounts.columns");
/*  65 */   private static final String CREATE_TABLE_STMT = DbService.getQuery("Accounts.createTable", new String[] { TABLE });
/*  66 */   private static final String FIND_ALL_STMT = DbService.getQuery("Accounts.findAll", new String[] { COLUMNS, TABLE });
/*  67 */   private static final String FIND_BY_ID_STMT = DbService.getQuery("Accounts.findById", new String[] { COLUMNS, TABLE });
/*  68 */   private static final String FIND_BY_NAME_STMT = DbService.getQuery("Accounts.findByName", new String[] { COLUMNS, TABLE });
/*  69 */   private static final String FIND_BY_USER_STMT = DbService.getQuery("Accounts.findByUser", new String[] { COLUMNS, TABLE });
/*  70 */   private static final String FIND_BY_CONNECTION_STMT = DbService.getQuery("Accounts.findByConnection", new String[] { COLUMNS, TABLE });
/*  71 */   private static final String FIND_BY_USER_CONNECTION_STMT = DbService.getQuery("Accounts.findByUserConnection", new String[] { COLUMNS, TABLE });
/*  72 */   private static final String FIND_BY_LOGIN_CONNECTION_STMT = DbService.getQuery("Accounts.findByLoginConnection", new String[] { COLUMNS, TABLE });
/*  73 */   private static final String INSERT_STMT = DbService.getQuery("Accounts.insert", new String[] { TABLE });
/*  74 */   private static final String UPDATE_STMT = DbService.getQuery("Accounts.update", new String[] { TABLE });
/*  75 */   private static final String DELETE_STMT = DbService.getQuery("Accounts.delete", new String[] { TABLE });
/*  76 */   private static final String DELETE_CONNECTION_STMT = DbService.getQuery("Accounts.deleteConnection", new String[] { TABLE });
/*     */ 
/*     */   public final Account findBy(String login, PaloConnection connection) throws SQLException
/*     */   {
/*  80 */     PreparedStatement stmt = null;
/*  81 */     ResultSet results = null;
/*  82 */     Account account = null;
/*  83 */     Connection _conn = DbService.getConnection();
/*     */     try {
/*  85 */       stmt = _conn.prepareStatement(FIND_BY_LOGIN_CONNECTION_STMT);
/*  86 */       stmt.setString(1, login);
/*  87 */       stmt.setString(2, connection.getId());
/*  88 */       results = stmt.executeQuery();
/*  89 */       if (results.next()) 
/*  90 */        account = (Account)load(results);
/*     */     } finally {
/*  92 */       cleanUp(stmt, results);
/*     */     }
/*  94 */     return account;
/*     */   }
/*     */ 
/*     */   public final Account findBy(User user, PaloConnection connection) throws SQLException
/*     */   {
/*  99 */     PreparedStatement stmt = null;
/* 100 */     ResultSet results = null;
/* 101 */     Account account = null;
/* 102 */     Connection _conn = DbService.getConnection();
/*     */     try {
/* 104 */       stmt = _conn.prepareStatement(FIND_BY_USER_CONNECTION_STMT);
/* 105 */       stmt.setString(1, user.getId());
/* 106 */       stmt.setString(2, connection.getId());
/* 107 */       results = stmt.executeQuery();
/* 108 */       if (results.next()) 
/* 109 */        account = (Account)load(results);
/*     */     } finally {
/* 111 */       cleanUp(stmt, results);
/*     */     }
/* 113 */     return account;
/*     */   }
/*     */ 
/*     */   public final List<Account> findAll() throws SQLException {
/* 117 */     PreparedStatement stmt = null;
/* 118 */     ResultSet results = null;
/* 119 */     List accounts = new ArrayList();
/* 120 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 122 */       stmt = connection.prepareStatement(FIND_ALL_STMT);
/* 123 */       results = stmt.executeQuery();
/* 124 */       while (results.next()) {
/* 125 */         Account account = (Account)load(results);
/* 126 */         if (account != null)
/* 127 */           accounts.add(account);
/*     */       }
/*     */     } finally {
/* 130 */       cleanUp(stmt, results);
/*     */     }
/* 132 */     return accounts;
/*     */   }
/*     */ 
/*     */   public List<Account> getAccounts(User user) throws SQLException {
/* 136 */     if (user == null) {
/* 137 */       return new ArrayList();
/*     */     }
/* 139 */     return getAccounts(user.getId());
/*     */   }
/*     */   public List<Account> getAccounts(String userId) throws SQLException {
/* 142 */     PreparedStatement stmt = null;
/* 143 */     ResultSet results = null;
/* 144 */     List accounts = new ArrayList();
/* 145 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 147 */       stmt = connection.prepareStatement(FIND_BY_USER_STMT);
/* 148 */       stmt.setString(1, userId);
/* 149 */       results = stmt.executeQuery();
/* 150 */       while (results.next()) {
/* 151 */         Account account = (Account)load(results);
/* 152 */         if (account != null)
/* 153 */           accounts.add(account);
/*     */       }
/*     */     } finally {
/* 156 */       cleanUp(stmt, results);
/*     */     }
/* 158 */     return accounts;
/*     */   }
/*     */ 
/*     */   public List<Account> getAccountsBy(String connectionId) throws SQLException {
/* 162 */     PreparedStatement stmt = null;
/* 163 */     ResultSet results = null;
/* 164 */     List accounts = new ArrayList();
/* 165 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 167 */       stmt = connection.prepareStatement(FIND_BY_CONNECTION_STMT);
/* 168 */       stmt.setString(1, connectionId);
/* 169 */       results = stmt.executeQuery();
/* 170 */       while (results.next()) {
/* 171 */         Account account = (Account)load(results);
/* 172 */         if (account != null)
/* 173 */           accounts.add(account);
/*     */       }
/*     */     } finally {
/* 176 */       cleanUp(stmt, results);
/*     */     }
/* 178 */     return accounts;
/*     */   }
/*     */ 
/*     */   public final void update(DomainObject obj) throws SQLException {
/* 182 */     PreparedStatement stmt = null;
/* 183 */     Account acc = (Account)obj;
/* 184 */     Connection connection = DbService.getConnection();
/*     */     try {
/* 186 */       stmt = connection.prepareStatement(UPDATE_STMT);
/* 187 */       stmt.setString(1, acc.getLoginName());
/* 188 */       stmt.setString(2, acc.getPassword());
/* 189 */       stmt.setString(3, acc.getConnection().getId());
/* 190 */       stmt.setString(4, acc.getUser().getId());
/* 191 */       stmt.setString(5, acc.getId());
/* 192 */       stmt.execute();
/*     */     } finally {
/* 194 */       cleanUp(stmt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void delete(PaloConnection conn) throws SQLException {
/* 199 */     PreparedStatement stmt = null;
/* 200 */     Connection connection = DbService.getConnection();
/*     */     try
/*     */     {
/* 203 */       stmt = connection.prepareStatement(DELETE_CONNECTION_STMT);
/* 204 */       stmt.setString(1, conn.getId());
/* 205 */       stmt.execute();
/*     */     }
/*     */     finally
/*     */     {
/* 212 */       cleanUp(stmt);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void doInsert(DomainObject obj, PreparedStatement stmt) throws SQLException
/*     */   {
/* 218 */     Account acc = (Account)obj;
/* 219 */     stmt.setString(1, acc.getId());
/* 220 */     stmt.setString(2, acc.getLoginName());
/* 221 */     stmt.setString(3, acc.getPassword());
/* 222 */     stmt.setString(4, acc.getConnection().getId());
/* 223 */     stmt.setString(5, acc.getUser().getId());
/*     */   }
/*     */ 
/*     */   protected final DomainObject doLoad(String id, ResultSet result) throws SQLException
/*     */   {
/* 228 */     String userId = result.getString(5);
/* 229 */     AccountImpl.Builder accBuilder = new AccountImpl.Builder(id, userId);
/* 230 */     accBuilder.username(result.getString(2));
/* 231 */     accBuilder.password(result.getString(3));
/* 232 */     accBuilder.connection(result.getString(4));
/* 233 */     return accBuilder.build();
/*     */   }
/*     */ 
/*     */   protected final void deleteAssociations(DomainObject obj) throws SQLException {
/*     */   }
/*     */ 
/*     */   protected final String deleteStatement() {
/* 240 */     return DELETE_STMT;
/*     */   }
/*     */ 
/*     */   protected final String findStatement() {
/* 244 */     return FIND_BY_ID_STMT;
/*     */   }
/*     */   protected final String findByNameStatement() {
/* 247 */     return FIND_BY_NAME_STMT;
/*     */   }
/*     */   protected final String insertStatement() {
/* 250 */     return INSERT_STMT;
/*     */   }
/*     */ 
/*     */   protected final String createTableStatement() {
/* 254 */     return CREATE_TABLE_STMT;
/*     */   }
/*     */ 
/*     */   protected final String getTableName() {
/* 258 */     return TABLE;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.dbmappers.AccountMapper
 * JD-Core Version:    0.5.4
 */