/*     */ package org.palo.viewapi.internal;
/*     */ 
/*     */ import com.tensegrity.palojava.ServerInfo;
/*     */ import org.palo.api.Connection;
/*     */ import org.palo.api.ConnectionContext;
/*     */ import org.palo.api.ConnectionListener;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Property2;
/*     */ import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;
/*     */ 
/*     */ public class ServerConnectionPool
/*     */ {
/*     */   private final ConnectionWrapper[] connections;
/*     */   private final IConnectionFactory factory;
/*     */   private final String host;
/*     */   private final String service;
/*     */   private final String login;
/*     */   private final String password;
/*     */   private final String provider;
/*  52 */   private final Object mutex = new Object();
/*     */ 
/*     */   public ServerConnectionPool(IConnectionFactory factory, String host, String service, String login, String password, int capacity, String provider) {
/*  55 */     this.factory = factory;
/*  56 */     this.host = host;
/*  57 */     this.service = service;
/*  58 */     this.login = login;
/*  59 */     this.password = password;
/*  60 */     this.provider = provider;
/*  61 */     this.connections = new ConnectionWrapper[capacity];
/*  62 */     synchronized (this.mutex) {
/*  63 */       getConnection("ServerConnectionPool").disconnect();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void markNeedReload()
/*     */   {
/*  73 */     for (int i = 0; i < this.connections.length; ++i)
/*  74 */       if (this.connections[i] != null)
/*  75 */         this.connections[i].setNeedReload();
/*     */   }
/*     */ 
/*     */   private ConnectionWrapper openConnection(int index)
/*     */   {
/*  80 */     synchronized (this.mutex)
/*     */     {
/*  82 */       Connection nativeConnection = this.factory.createConnection(this.host, this.service, this.login, this.password, this.provider);
/*  83 */       ConnectionWrapper conn = new ConnectionWrapper(nativeConnection, index);
/*  84 */       return conn;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Connection getConnection(String from)
/*     */   {
/*  94 */     ConnectionWrapper result = null;
/*     */ 
/*  96 */     synchronized (this.mutex) {
/*  97 */       int i = 0;
/*     */       do { result = tryGetConnection(i);
/*     */ 
/*  97 */         ++i; if (i >= this.connections.length) break;  }
/*  97 */       while (result == null);
/*     */     }
/*     */ 
/* 104 */     if (result == null)
/*     */     {
/* 106 */       shutdown();
/* 107 */       synchronized (this.mutex) {
/* 108 */         int i = 0;
/*     */         do { result = tryGetConnection(i);
/*     */ 
/* 108 */           ++i; if (i >= this.connections.length) break;  }
/* 108 */         while (result == null);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 114 */     if (result == null) {
/* 115 */       throw new RuntimeException("no more connections available");
/*     */     }
/* 117 */     return result;
/*     */   }
/*     */ 
/*     */   private ConnectionWrapper tryGetConnection(int i) {
/* 121 */     ConnectionWrapper result = null;
/* 122 */     if (this.connections[i] == null)
/* 123 */       this.connections[i] = openConnection(i);
/* 124 */     if (!this.connections[i].isBusy()) {
/* 125 */       result = this.connections[i];
/* 126 */       result.setBusy();
/* 127 */       result.reloadOnDemand();
/*     */     }
/* 129 */     return result;
/*     */   }
/*     */ 
/*     */   public void shutdown()
/*     */   {
/* 136 */     synchronized (this.mutex) {
/* 137 */       for (int i = 0; i < this.connections.length; ++i)
/* 138 */         if (this.connections[i] != null)
/* 139 */           this.connections[i].disconnect();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void disconnectAll()
/*     */   {
/* 145 */     synchronized (this.mutex) {
/* 146 */       for (int i = 0; i < this.connections.length; ++i) {
/* 147 */         if (this.connections[i] != null) {
/* 148 */           this.connections[i].clearCache();
/*     */         }
/*     */ 
/* 151 */         this.connections[i] = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ConnectionWrapper
/*     */     implements Connection
/*     */   {
/*     */     private final Connection conn;
/*     */     private final int index;
/* 159 */     private boolean busy = false;
/* 160 */     private boolean needReload = false;
/* 161 */     private final Object mutex = new Object();
/*     */ 
/*     */     public void setBusy()
/*     */     {
/* 165 */       synchronized (this.mutex) {
/* 166 */         if (!isBusy())
/* 167 */           this.busy = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isBusy()
/*     */     {
/* 177 */       return this.busy;
/*     */     }
/*     */ 
/*     */     public void setNeedReload() {
/* 181 */       synchronized (this.mutex) {
/* 182 */         if (!isNeedReload())
/* 183 */           this.needReload = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public boolean isNeedReload()
/*     */     {
/* 191 */       return this.needReload;
/*     */     }
/*     */ 
/*     */     public ConnectionWrapper(Connection conn, int index) {
/* 195 */       this.conn = conn;
/* 196 */       this.index = index;
/*     */     }
/*     */ 
/*     */     public void reloadOnDemand()
/*     */     {
/* 208 */       if (isNeedReload())
/* 209 */         reload();
/*     */     }
/*     */ 
/*     */     public void forceDisconnect()
/*     */     {
/* 215 */       this.conn.disconnect();
/*     */     }
/*     */ 
/*     */     public void addConnectionListener(ConnectionListener listener) {
/* 219 */       this.conn.addConnectionListener(listener);
/*     */     }
/*     */ 
/*     */     public Database addDatabase(String db) {
/* 223 */       return this.conn.addDatabase(db);
/*     */     }
/*     */ 
/*     */     public Database addUserInfoDatabase(String db) {
/* 227 */       return this.conn.addUserInfoDatabase(db);
/*     */     }
/*     */ 
/*     */     public void disconnect()
/*     */     {
/* 232 */       synchronized (this.mutex) {
/* 233 */         if (isBusy())
/*     */         {
/* 235 */           this.busy = false;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public void quit()
/*     */     {
/* 244 */       synchronized (this.mutex) {
/* 245 */         if (isBusy()) {
/* 246 */           this.busy = false;
/*     */         }
/* 248 */         this.conn.disconnect();
/*     */       }
/*     */     }
/*     */ 
/*     */     public Database getDatabaseAt(int index) {
/* 253 */       return this.conn.getDatabaseAt(index);
/*     */     }
/*     */ 
/*     */     public Database getDatabaseByName(String name) {
/* 257 */       return this.conn.getDatabaseByName(name);
/*     */     }
/*     */ 
/*     */     public int getDatabaseCount() {
/* 261 */       return this.conn.getDatabaseCount();
/*     */     }
/*     */ 
/*     */     public Database[] getDatabases() {
/* 265 */       return this.conn.getDatabases();
/*     */     }
/*     */ 
/*     */     public String getPassword() {
/* 269 */       return this.conn.getPassword();
/*     */     }
/*     */ 
/*     */     public String getServer() {
/* 273 */       return this.conn.getServer();
/*     */     }
/*     */ 
/*     */     public String getService() {
/* 277 */       return this.conn.getService();
/*     */     }
/*     */ 
/*     */     public Database[] getSystemDatabases() {
/* 281 */       return this.conn.getSystemDatabases();
/*     */     }
/*     */ 
/*     */     public String getUsername() {
/* 285 */       return this.conn.getUsername();
/*     */     }
/*     */ 
/*     */     public void ping() {
/* 289 */       this.conn.ping();
/*     */     }
/*     */ 
/*     */     public void reload() {
/* 293 */       synchronized (this.mutex)
/*     */       {
/* 295 */         this.conn.reload();
/* 296 */         this.needReload = false;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void removeConnectionListener(ConnectionListener listner) {
/* 301 */       this.conn.removeConnectionListener(listner);
/*     */     }
/*     */ 
/*     */     public void removeDatabase(Database db) {
/* 305 */       this.conn.removeDatabase(db);
/*     */     }
/*     */ 
/*     */     public boolean save() {
/* 309 */       return this.conn.save();
/*     */     }
/*     */ 
/*     */     public boolean isLegacy() {
/* 313 */       return this.conn.isLegacy();
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 317 */       return "connection[" + this.index + "]";
/*     */     }
/*     */ 
/*     */     public String getFunctions() {
/* 321 */       return this.conn.getFunctions();
/*     */     }
/*     */ 
/*     */     public boolean isConnected() {
/* 325 */       return this.conn.isConnected();
/*     */     }
/*     */ 
/*     */     public boolean login(String arg0, String arg1) {
/* 329 */       return this.conn.login(arg0, arg1);
/*     */     }
/*     */ 
/*     */     public FavoriteViewTreeNode loadFavoriteViews() {
/* 333 */       return this.conn.loadFavoriteViews();
/*     */     }
/*     */ 
/*     */     public void storeFavoriteViews(FavoriteViewTreeNode node) {
/* 337 */       this.conn.storeFavoriteViews(node);
/*     */     }
/*     */ 
/*     */     public Database getDatabaseById(String id) {
/* 341 */       return this.conn.getDatabaseById(id);
/*     */     }
/*     */ 
/*     */     public int getType() {
/* 345 */       return this.conn.getType();
/*     */     }
/*     */ 
/*     */     public void addProperty(Property2 arg0) {
/* 349 */       this.conn.addProperty(arg0);
/*     */     }
/*     */ 
/*     */     public String[] getAllPropertyIds() {
/* 353 */       return this.conn.getAllPropertyIds();
/*     */     }
/*     */ 
/*     */     public ConnectionContext getContext() {
/* 357 */       return this.conn.getContext();
/*     */     }
/*     */ 
/*     */     public Property2 getProperty(String arg0) {
/* 361 */       return this.conn.getProperty(arg0);
/*     */     }
/*     */ 
/*     */     public void removeProperty(String arg0) {
/* 365 */       this.conn.removeProperty(arg0);
/*     */     }
/*     */ 
/*     */     public boolean canBeModified() {
/* 369 */       return this.conn.canBeModified();
/*     */     }
/*     */ 
/*     */     public boolean canCreateChildren() {
/* 373 */       return this.conn.canCreateChildren();
/*     */     }
/*     */ 
/*     */     public Object getData(String id)
/*     */     {
/* 378 */       return null;
/*     */     }
/*     */ 
/*     */     public void clearCache() {
/* 382 */       this.conn.clearCache();
/*     */     }
/*     */ 
/*     */     public ServerInfo getInfo() {
/* 386 */       return this.conn.getInfo();
/*     */     }
/*     */ 
/*     */     public Database[] getUserInfoDatabases()
/*     */     {
/* 391 */       return null;
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.ServerConnectionPool
 * JD-Core Version:    0.5.4
 */