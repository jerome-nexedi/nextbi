/*     */ package org.palo.viewapi.internal;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.palo.viewapi.Account;
/*     */ import org.palo.viewapi.PaloAccount;
/*     */ import org.palo.viewapi.PaloConnection;
/*     */ 
/*     */ public class ConnectionPoolManager
/*     */ {
/*  43 */   private final Map<String, ServerConnectionPool> pools = new HashMap();
/*     */ 
/*  46 */   private static final ConnectionPoolManager instance = new ConnectionPoolManager();
/*     */ 
/*  49 */   private final PaloConnectionFactory factory = new PaloConnectionFactory();
/*     */ 
/*     */   public static ConnectionPoolManager getInstance()
/*     */   {
/*  55 */     return instance;
/*     */   }
/*     */ 
/*     */   public ServerConnectionPool getPool(Account account, String sessionId)
/*     */   {
	ServerConnectionPool pool;
/*  60 */     synchronized (this.pools) {
/*  61 */       String serverId = account.getLoginName() + ":" + account.getConnection().getHost() + ":" + account.getConnection().getService();
/*  62 */       pool = (ServerConnectionPool)this.pools.get(serverId);
/*  63 */       if (pool == null) {
/*  64 */         String sProvider = "palo";
/*  65 */         switch (account.getConnection().getType())
/*     */         {
/*     */         case 3:
/*  66 */           sProvider = "xmla";
/*     */         }
/*  68 */         pool = new ServerConnectionPool(this.factory, 
/*  69 */           account.getConnection().getHost(), 
/*  70 */           account.getConnection().getService(), 
/*  71 */           account.getLoginName(), 
/*  72 */           account.getPassword(), 
/*  73 */           1, sProvider);
/*  74 */         this.pools.put(serverId, pool);
/*     */       }
/*     */     }
/*  77 */     return pool;
/*     */   }
/*     */ 
/*     */   public ServerConnectionPool onlyGetPool(Account account, String sessionId)
/*     */   {
/*     */     ServerConnectionPool pool;
/*  82 */     synchronized (this.pools) {
/*  83 */       if (account.getConnection() == null) {
/*  84 */         return null;
/*     */       }
/*  86 */       String serverId = account.getLoginName() + ":" + account.getConnection().getHost() + ":" + account.getConnection().getService();
/*  87 */       pool = (ServerConnectionPool)this.pools.get(serverId);
/*     */     }
/*  89 */     return pool;
/*     */   }
/*     */ 
/*     */   public synchronized void disconnect(Account account, String sessionId, String from)
/*     */   {
/*  94 */     if (!(account instanceof PaloAccount))
/*     */       return;
/*  96 */     synchronized (this.pools) {
/*  97 */       String serverId = account.getLoginName() + ":" + account.getConnection().getHost() + ":" + account.getConnection().getService();
/*  98 */       ServerConnectionPool pool = (ServerConnectionPool)this.pools.get(serverId);
/*  99 */       if (pool == null) {
/* 100 */         String sProvider = "palo";
/* 101 */         switch (account.getConnection().getType())
/*     */         {
/*     */         case 3:
/* 102 */           sProvider = "xmla";
/*     */         }
/* 104 */         pool = new ServerConnectionPool(this.factory, 
/* 105 */           account.getConnection().getHost(), 
/* 106 */           account.getConnection().getService(), 
/* 107 */           account.getLoginName(), 
/* 108 */           account.getPassword(), 
/* 109 */           1, sProvider);
/* 110 */         this.pools.put(serverId, pool);
									pool.shutdown();
/*     */       }
/*     */     }
/* 113 */     
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.ConnectionPoolManager
 * JD-Core Version:    0.5.4
 */