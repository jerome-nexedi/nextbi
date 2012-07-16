/*     */ package org.palo.viewapi.internal;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import org.palo.viewapi.Account;
/*     */ import org.palo.viewapi.AuthUser;
/*     */ import org.palo.viewapi.PaloConnection;
/*     */ import org.palo.viewapi.User;
/*     */ import org.palo.viewapi.internal.dbmappers.MapperRegistry;
/*     */ 
/*     */ public abstract class AccountImpl extends DomainObjectImpl
/*     */   implements Account
/*     */ {
/*     */   private String name;
/*     */   private String pass;
/*     */   private String accConnection;
/*     */   protected Object paloConnection;
/*     */   private String user;
/*     */ 
/*     */   AccountImpl(String id, String user)
/*     */   {
/*  62 */     super(id);
/*  63 */     this.user = user;
/*     */   }
/*     */   protected AccountImpl(Builder builder) {
/*  66 */     super(builder.id);
/*  67 */     this.user = builder.user;
/*  68 */     this.name = builder.username;
/*  69 */     this.pass = builder.pass;
/*  70 */     this.accConnection = builder.connId;
/*     */   }
/*     */ 
/*     */   private static final Account createAccountImpl(Builder builder) {
/*  74 */     PaloConnection conn = null;
/*     */     try {
/*  76 */       conn = (PaloConnection)MapperRegistry.getInstance()
/*  77 */         .getConnectionManagement().find(builder.connId);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/*  80 */     if (conn == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     if (conn.getType() == 4) {
/*  84 */       return new WSSAccountImpl(builder);
/*     */     }
/*  86 */     return new PaloAccountImpl(builder);
/*     */   }
/*     */ 
/*     */   public final PaloConnection getConnection()
/*     */   {
/*  92 */     PaloConnection conn = null;
/*     */     try {
/*  94 */       conn = (PaloConnection)MapperRegistry.getInstance()
/*  95 */         .getConnectionManagement().find(this.accConnection);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/*  98 */     return conn;
/*     */   }
/*     */ 
/*     */   public final String getLoginName() {
/* 102 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final String getPassword() {
/* 106 */     return this.pass;
/*     */   }
/*     */ 
/*     */   public final AuthUser getUser()
/*     */   {
/*     */     try {
/* 112 */       User usr = (User)MapperRegistry.getInstance()
/* 113 */         .getUserManagement().find(this.user);
/* 114 */       return new AuthUserImpl(usr);
/*     */     } catch (SQLException localSQLException) {
/*     */     }
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean isLoggedIn()
/*     */   {
/* 155 */     return this.paloConnection != null;
/*     */   }
/*     */ 
/*     */   final void setConnection(PaloConnection connection)
/*     */   {
/* 161 */     this.accConnection = connection.getId();
/*     */   }
/*     */   final void setConnection(String id) {
/* 164 */     this.accConnection = id;
/*     */   }
/*     */   final void setLoginName(String name) {
/* 167 */     this.name = name;
/*     */   }
/*     */   final void setPassword(String pass) {
/* 170 */     this.pass = pass;
/*     */   }
/*     */   final void setUser(User user) {
/* 173 */     this.user = user.getId();
/* 174 */     ((UserImpl)user).add(this);
/*     */   }
/*     */ 
/*     */   public static final class Builder
/*     */   {
/*     */     private final String id;
/*     */     private String username;
/*     */     private String pass;
/*     */     private String connId;
/*     */     private final String user;
/*     */ 
/*     */     public Builder(String id, String user)
/*     */     {
/* 189 */       AccessController.checkAccess(Account.class);
/* 190 */       this.id = id;
/* 191 */       this.user = user;
/*     */     }
/*     */ 
/*     */     public Builder username(String username) {
/* 195 */       this.username = username;
/* 196 */       return this;
/*     */     }
/*     */     public Builder password(String pass) {
/* 199 */       this.pass = pass;
/* 200 */       return this;
/*     */     }
/*     */     public Builder connection(String connId) {
/* 203 */       this.connId = connId;
/* 204 */       return this;
/*     */     }
/*     */     public Account build() {
/* 207 */       return  AccountImpl.createAccountImpl(this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.AccountImpl
 * JD-Core Version:    0.5.4
 */