/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.ConnectionInfo;
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import com.tensegrity.palojava.PaloServer;
/*     */ import com.tensegrity.palojava.PropertyInfo;
/*     */ import com.tensegrity.palojava.ServerInfo;
/*     */ import com.tensegrity.palojava.events.ServerEvent;
/*     */ import com.tensegrity.palojava.events.ServerListener;
/*     */ import com.tensegrity.palojava.loader.DatabaseLoader;
/*     */ import com.tensegrity.palojava.loader.FunctionLoader;
/*     */ import com.tensegrity.palojava.loader.PropertyLoader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import org.palo.api.Connection;
/*     */ import org.palo.api.ConnectionContext;
/*     */ import org.palo.api.ConnectionEvent;
/*     */ import org.palo.api.ConnectionListener;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.Property2;
/*     */ import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;
/*     */ import org.palo.api.ext.favoriteviews.impl.FavoriteViewModel;
/*     */ 
/*     */ public class ConnectionImpl
/*     */   implements Connection, ServerListener
/*     */ {
/*  82 */   static final boolean WPALO = Boolean.getBoolean("wpalo");
/*     */   private final DbConnection dbConnection;
/*     */   private final LinkedHashSet listeners;
/*     */   private final ConnectionInfo connectionInfo;
/*     */   private final PaloServer paloServer;
/*     */   private final ServerInfo serverInfo;
/*     */   private final Map<String, DatabaseImpl> loadedDatabases;
/*     */   private final Map<String, Property2Impl> loadedProperties;
/*     */   private final DatabaseLoader dbLoader;
/*     */   private final FunctionLoader funcLoader;
/*     */   private final PropertyLoader propertyLoader;
/*     */   private final ConnectionContextImpl context;
/*     */   private final String internalID;
/*     */ 
/*     */   ConnectionImpl(PaloServer paloServer)
/*     */   {
/* 108 */     this.internalID = UUID.randomUUID().toString();
/* 109 */     this.paloServer = paloServer;
/* 110 */     this.serverInfo = paloServer.getInfo();
/* 111 */     this.dbConnection = paloServer.connect();
/* 112 */     this.loadedDatabases = new LinkedHashMap();
/* 113 */     this.loadedProperties = new LinkedHashMap();
/*     */ 
/* 115 */     this.listeners = new LinkedHashSet();
/* 116 */     this.connectionInfo = this.dbConnection.getInfo();
/* 117 */     if (!this.serverInfo.isLegacy()) {
/* 118 */       this.dbConnection.addServerListener(this);
/*     */     }
/* 120 */     this.dbLoader = this.dbConnection.getDatabaseLoader();
/* 121 */     this.funcLoader = this.dbConnection.getFunctionLoader();
/* 122 */     this.propertyLoader = this.dbConnection.getPropertyLoader();
/*     */ 
/* 125 */     this.context = new ConnectionContextImpl(this);
/*     */ 
/* 128 */     this.connectionInfo.setData("com.tensegrity.palo.wpalo", new Boolean(WPALO));
/*     */   }
/*     */ 
/*     */   public final boolean login(String username, String password)
/*     */   {
/* 133 */     return this.dbConnection.login(username, password);
/*     */   }
/*     */ 
/*     */   public final Database addDatabase(String name) {
/*     */     try {
/* 138 */       int infoType = 0;
/* 139 */       DatabaseInfo dbInfo = this.dbLoader.create(name, infoType);
/*     */ 
/* 146 */       Database database = createDatabase(dbInfo, true);
/*     */ 
/* 148 */       fireEvent(
/* 150 */         new ConnectionEvent(this, this, 
/* 149 */         0, 
/* 150 */         new Database[] { database }));
/*     */ 
/* 152 */       return database;
/*     */     } catch (PaloException pex) {
/* 154 */       throw new PaloAPIException(pex.getMessage(), pex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Database addUserInfoDatabase(String name) {
/*     */     try {
/* 160 */       int infoType = 3;
/* 161 */       DatabaseInfo dbInfo = this.dbLoader.create(name, infoType);
/*     */ 
/* 168 */       Database database = createDatabase(dbInfo, true);
/*     */ 
/* 170 */       fireEvent(
/* 172 */         new ConnectionEvent(this, this, 
/* 171 */         0, 
/* 172 */         new Database[] { database }));
/*     */ 
/* 174 */       return database;
/*     */     } catch (PaloException pex) {
/* 176 */       throw new PaloAPIException(pex.getMessage(), pex);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void disconnect()
/*     */   {
/*     */     try {
/* 183 */       this.paloServer.disconnect();
/* 184 */       this.loadedDatabases.clear();
/*     */ 
/* 186 */       this.dbLoader.reset();
/*     */     } catch (PaloException pex) {
/* 188 */       throw new PaloAPIException(pex);
/*     */     } catch (RuntimeException e) {
/* 190 */       throw new PaloAPIException(e.getLocalizedMessage(), e);
/*     */     } finally {
/* 192 */       if (getType() == 2)
/* 193 */         ((ConnectionFactoryImpl)
/* 194 */           ConnectionFactoryImpl.getInstance()).removePaloConnection(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Database getDatabaseAt(int index)
/*     */   {
/* 200 */     DatabaseInfo dbInfo = this.dbLoader.load(index);
/* 201 */     return getDatabase(dbInfo);
/*     */   }
/*     */ 
/*     */   public final Database getDatabaseByName(String name)
/*     */   {
/* 223 */     DatabaseInfo dbInfo = this.dbLoader.loadByName(name);
/* 224 */     return getDatabase(dbInfo);
/*     */   }
/*     */ 
/*     */   public final Database getDatabaseById(String id)
/*     */   {
/*     */     try
/*     */     {
/* 236 */       DatabaseInfo dbInfo = this.dbLoader.load(id);
/* 237 */       return getDatabase(dbInfo);
/*     */     }
/*     */     catch (PaloException localPaloException) {
/*     */     }
/* 241 */     return null;
/*     */   }
/*     */ 
/*     */   public final int getDatabaseCount()
/*     */   {
/*     */     try
/*     */     {
/* 252 */       return this.dbLoader.getDatabaseCount();
/*     */     } catch (PaloException e) {
/* 254 */       throw new PaloAPIException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Database[] getDatabases() {
/* 259 */     String[] ids = this.dbLoader.getAllDatabaseIds();
/* 260 */     ArrayList databases = new ArrayList();
/* 261 */     for (String id : ids) {
/* 262 */       DatabaseInfo dbInfo = this.dbLoader.load(id);
/* 263 */       Database db = getDatabase(dbInfo);
/* 264 */       if (db != null)
/* 265 */         databases.add(db);
/*     */     }
/* 267 */     return (Database[])databases.toArray(new Database[databases.size()]);
/*     */   }
/*     */ 
/*     */   public final String getPassword()
/*     */   {
/* 286 */     return this.connectionInfo.getPassword();
/*     */   }
/*     */ 
/*     */   public final String getServer() {
/* 290 */     return this.connectionInfo.getHost();
/*     */   }
/*     */ 
/*     */   public final String getService() {
/* 294 */     return this.connectionInfo.getPort();
/*     */   }
/*     */ 
/*     */   public final Database[] getSystemDatabases() {
/* 298 */     String[] ids = this.dbLoader.getAllDatabaseIds();
/* 299 */     ArrayList sysDbs = new ArrayList();
/* 300 */     for (String id : ids) {
/* 301 */       DatabaseInfo dbInfo = this.dbLoader.load(id);
/* 302 */       if ((dbInfo != null) && (dbInfo.isSystem())) {
/* 303 */         Database db = getDatabase(dbInfo);
/* 304 */         if (db != null)
/* 305 */           sysDbs.add(db);
/*     */       }
/*     */     }
/* 308 */     return (Database[])sysDbs.toArray(new Database[sysDbs.size()]);
/*     */   }
/*     */ 
/*     */   public final Database[] getUserInfoDatabases()
/*     */   {
/* 324 */     String[] ids = this.dbLoader.getAllDatabaseIds();
/* 325 */     ArrayList sysDbs = new ArrayList();
/* 326 */     for (String id : ids) {
/* 327 */       DatabaseInfo dbInfo = this.dbLoader.load(id);
/* 328 */       if ((dbInfo != null) && (dbInfo.isUserInfo())) {
/* 329 */         Database db = getDatabase(dbInfo);
/* 330 */         if (db != null)
/* 331 */           sysDbs.add(db);
/*     */       }
/*     */     }
/* 334 */     return (Database[])sysDbs.toArray(new Database[sysDbs.size()]);
/*     */   }
/*     */ 
/*     */   public final String getUsername() {
/* 338 */     return this.connectionInfo.getUsername();
/*     */   }
/*     */ 
/*     */   public final boolean isLegacy() {
/* 342 */     return this.serverInfo.isLegacy();
/*     */   }
/*     */ 
/*     */   public final int getType() {
/* 346 */     return this.serverInfo.getType();
/*     */   }
/*     */ 
/*     */   public final boolean isConnected() {
/* 350 */     return this.dbConnection.isConnected();
/*     */   }
/*     */ 
/*     */   public final void ping() {
/* 354 */     this.paloServer.ping();
/*     */   }
/*     */ 
/*     */   public final void reload()
/*     */   {
/* 359 */     this.dbLoader.reset();
/* 360 */     if (this.funcLoader != null)
/* 361 */       this.funcLoader.reset();
/* 362 */     if (this.propertyLoader != null) {
/* 363 */       this.propertyLoader.reset();
/*     */     }
/*     */ 
/* 366 */     if (WPALO) {
/* 367 */       clearCaches();
/*     */     }
/* 369 */     else if (this.serverInfo.getType() == 3) {
/* 370 */       for (Database db : this.loadedDatabases.values())
/* 371 */         ((DatabaseImpl)db).init();
/*     */     }
/*     */     else
/* 374 */       reloadAlllDatabases(true);
/*     */   }
/*     */ 
/*     */   public final void clearCache()
/*     */   {
/* 380 */     clearCaches();
/*     */   }
/*     */ 
/*     */   public final void removeDatabase(Database database) {
/* 384 */     String dbId = database.getId();
/* 385 */     if (!this.loadedDatabases.containsKey(dbId))
/* 386 */       return;
/* 387 */     DatabaseInfo dbInfo = ((DatabaseImpl)database).getInfo();
/*     */ 
/* 396 */     if (this.dbLoader.delete(dbInfo)) {
/* 397 */       this.loadedDatabases.remove(dbId);
/* 398 */       fireEvent(
/* 400 */         new ConnectionEvent(this, this, 
/* 399 */         1, 
/* 400 */         new Database[] { database }));
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean save() {
/* 405 */     return this.dbConnection.save(this.serverInfo);
/*     */   }
/*     */ 
/*     */   public final FavoriteViewTreeNode loadFavoriteViews()
/*     */   {
/* 410 */     FavoriteViewModel bm = new FavoriteViewModel();
/* 411 */     return bm.loadFavoriteViews(this);
/*     */   }
/*     */ 
/*     */   public final synchronized void storeFavoriteViews(FavoriteViewTreeNode favoriteViews) {
/* 415 */     FavoriteViewModel bm = new FavoriteViewModel();
/* 416 */     bm.storeFavoriteViews(this, favoriteViews);
/*     */   }
/*     */ 
/*     */   public final void serverStructureChanged(ServerEvent event)
/*     */   {
/* 423 */     int evType = event.getType();
/*     */ 
/* 425 */     if (evType == 8)
/* 426 */       evType = 14;
/*     */     else {
/* 428 */       evType = 13;
/*     */     }
/* 430 */     fireEvent(new ConnectionEvent(this, this, evType, new Object[0]));
/*     */   }
/*     */ 
/*     */   public final void addConnectionListener(ConnectionListener connectionListener) {
/* 434 */     this.listeners.add(connectionListener);
/*     */   }
/*     */ 
/*     */   public final void removeConnectionListener(ConnectionListener connectionListener) {
/* 438 */     this.listeners.remove(connectionListener);
/*     */   }
/*     */ 
/*     */   public final String getFunctions()
/*     */   {
/* 446 */     return this.funcLoader.loadAll();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object other) {
/* 450 */     if (other instanceof ConnectionImpl) {
/* 451 */       ConnectionImpl ot = (ConnectionImpl)other;
/*     */ 
/* 465 */       return (this.internalID.equals(ot.internalID)) && 
/* 465 */         (this.connectionInfo.equals(ot.connectionInfo));
/*     */     }
/* 467 */     return false;
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 471 */     int hc = 87;
/* 472 */     hc += 31 * this.internalID.hashCode();
/* 473 */     hc += 27 * this.connectionInfo.hashCode();
/*     */ 
/* 478 */     return hc;
/*     */   }
/*     */ 
/*     */   public final ConnectionContext getContext()
/*     */   {
/* 483 */     return this.context;
/*     */   }
/*     */ 
/*     */   public final Object getData(String id) {
/* 487 */     return this.connectionInfo.getData(id);
/*     */   }
/*     */ 
/*     */   final void fireEvent(ConnectionEvent event)
/*     */   {
/* 495 */     ArrayList copy = new ArrayList(this.listeners);
/* 496 */     for (int i = 0; i < copy.size(); ++i) {
/* 497 */       ConnectionListener listener = (ConnectionListener)copy.get(i);
/*     */       try {
/* 499 */         listener.connectionChanged(event);
/*     */       } catch (RuntimeException e) {
/* 501 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final DbConnection getConnectionInternal()
/*     */   {
/* 516 */     return this.dbConnection;
/*     */   }
/*     */ 
/*     */   private final void clearCaches()
/*     */   {
/* 521 */     for (DatabaseImpl db : this.loadedDatabases.values()) {
/* 522 */       db.clearCache();
/*     */     }
/* 524 */     this.loadedDatabases.clear();
/* 525 */     this.dbLoader.reset();
/*     */ 
/* 527 */     for (Property2Impl property : this.loadedProperties.values()) {
/* 528 */       property.clearCache();
/*     */     }
/* 530 */     this.loadedProperties.clear();
/* 531 */     this.propertyLoader.reset();
/*     */   }
/*     */ 
/*     */   final synchronized void reloadAlllDatabases(boolean doEvents) {
/* 535 */     HashMap oldDatabases = 
/* 536 */       new HashMap(this.loadedDatabases);
/* 537 */     this.loadedDatabases.clear();
/* 538 */     this.dbLoader.reset();
/* 539 */     Exception nestedError = null;
/*     */     try {
/* 541 */       String[] dbIDs = this.dbLoader.getAllDatabaseIds();
/* 542 */       for (String id : dbIDs)
/*     */         try {
/* 544 */           DatabaseImpl db = (DatabaseImpl)oldDatabases.get(id);
/* 545 */           if (db == null) {
/* 546 */             DatabaseInfo dbInfo = this.dbLoader.load(id);
/* 547 */             db = createDatabase(dbInfo, doEvents);
/*     */           }
/* 549 */           this.loadedDatabases.put(id, db);
/*     */ 
/* 551 */           db.init(doEvents);
/*     */         } catch (RuntimeException e) {
/* 553 */           nestedError = e;
/*     */         }
/*     */     }
/*     */     catch (PaloException pex) {
/* 557 */       throw new PaloAPIException(pex);
/*     */     } catch (RuntimeException e) {
/* 559 */       throw new PaloAPIException(e.getLocalizedMessage(), e);
/*     */     }
/*     */ 
/* 563 */     if (doEvents) {
/* 564 */       LinkedHashSet removedDatabases = 
/* 565 */         new LinkedHashSet(oldDatabases.values());
/* 566 */       removedDatabases.removeAll(this.loadedDatabases.values());
/* 567 */       if (removedDatabases.size() > 0) {
/* 568 */         fireEvent(
/* 570 */           new ConnectionEvent(this, this, 
/* 569 */           1, 
/* 570 */           removedDatabases.toArray()));
/*     */       }
/*     */     }
/*     */ 
/* 574 */     if (doEvents) {
/* 575 */       LinkedHashSet addedDatabases = 
/* 576 */         new LinkedHashSet(this.loadedDatabases.values());
/* 577 */       addedDatabases.removeAll(oldDatabases.values());
/* 578 */       if (addedDatabases.size() > 0) {
/* 579 */         fireEvent(
/* 581 */           new ConnectionEvent(this, this, 
/* 580 */           0, 
/* 581 */           addedDatabases.toArray()));
/*     */       }
/*     */     }
/*     */ 
/* 585 */     if (nestedError != null)
/* 586 */       System.err.println("invalid database skipped: " + 
/* 587 */         nestedError.getLocalizedMessage());
/*     */   }
/*     */ 
/*     */   final boolean supportsRules()
/*     */   {
/* 671 */     return this.dbConnection.supportsRules();
/*     */   }
/*     */ 
/*     */   private final Database getDatabase(DatabaseInfo dbInfo)
/*     */   {
/* 699 */     if (dbInfo == null)
/* 700 */       return null;
/* 701 */     Database database = (Database)this.loadedDatabases.get(dbInfo.getId());
/* 702 */     if (database == null)
/*     */     {
/* 704 */       database = createDatabase(dbInfo, true);
/*     */     }
/* 706 */     return database;
/*     */   }
/*     */ 
/*     */   private final DatabaseImpl createDatabase(DatabaseInfo dbInfo, boolean fireEvent)
/*     */   {
/*     */     try
/*     */     {
/* 717 */       DatabaseImpl database = 
/* 718 */         DatabaseImpl.create(this, dbInfo, fireEvent);
/*     */ 
/* 720 */       this.loadedDatabases.put(database.getId(), database);
/*     */ 
/* 723 */       return database;
/*     */     } catch (PaloException pex) {
/* 725 */       pex.printStackTrace();
/* 726 */       System.err.println("failed to load database '" + dbInfo.getName() + 
/* 727 */         "'!! - skipped!! -");
/*     */     }
/* 729 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getAllPropertyIds()
/*     */   {
/* 734 */     return this.propertyLoader.getAllPropertyIds();
/*     */   }
/*     */ 
/*     */   public Property2 getProperty(String id) {
/* 738 */     PropertyInfo propInfo = this.propertyLoader.load(id);
/* 739 */     if (propInfo == null) {
/* 740 */       return null;
/*     */     }
/* 742 */     Property2 property = (Property2)this.loadedProperties.get(propInfo.getId());
/* 743 */     if (property == null) {
/* 744 */       property = createProperty(propInfo);
/*     */     }
/*     */ 
/* 747 */     return property;
/*     */   }
/*     */ 
/*     */   public void addProperty(Property2 property) {
/* 751 */     if (property == null) {
/* 752 */       return;
/*     */     }
/* 754 */     Property2Impl _property = (Property2Impl)property;
/* 755 */     this.propertyLoader.loaded(_property.getPropInfo());
/* 756 */     this.loadedProperties.put(_property.getId(), _property);
/*     */   }
/*     */ 
/*     */   public void removeProperty(String id) {
/* 760 */     Property2 property = getProperty(id);
/* 761 */     if (property == null) {
/* 762 */       return;
/*     */     }
/* 764 */     if (property.isReadOnly()) {
/* 765 */       return;
/*     */     }
/* 767 */     this.loadedProperties.remove(property);
/*     */   }
/*     */ 
/*     */   private void createProperty(Property2 parent, PropertyInfo kid) {
/* 771 */     Property2 p2Kid = Property2Impl.create(parent, kid);
/* 772 */     parent.addChild(p2Kid);
/* 773 */     for (PropertyInfo kidd : kid.getChildren())
/* 774 */       createProperty(p2Kid, kidd);
/*     */   }
/*     */ 
/*     */   private Property2 createProperty(PropertyInfo propInfo)
/*     */   {
/* 779 */     Property2 prop = Property2Impl.create(null, propInfo);
/* 780 */     for (PropertyInfo kid : propInfo.getChildren()) {
/* 781 */       createProperty(prop, kid);
/*     */     }
/* 783 */     return prop;
/*     */   }
/*     */ 
/*     */   public boolean canBeModified()
/*     */   {
/* 788 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren()
/*     */   {
/* 793 */     return getType() != 3;
/*     */   }
/*     */ 
/*     */   private final boolean saveAll() {
/* 797 */     boolean success = true;
/*     */ 
/* 799 */     for (Database database : this.loadedDatabases.values()) {
/* 800 */       Cube[] cubes = database.getCubes();
/* 801 */       for (Cube cube : cubes) {
/* 802 */         success &= ((CubeImpl)cube).save();
/*     */       }
/* 804 */       success &= database.save();
/*     */     }
/*     */ 
/* 807 */     success &= save();
/* 808 */     return success;
/*     */   }
/*     */ 
/*     */   public final ServerInfo getInfo() {
/* 812 */     return this.serverInfo;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.ConnectionImpl
 * JD-Core Version:    0.5.4
 */