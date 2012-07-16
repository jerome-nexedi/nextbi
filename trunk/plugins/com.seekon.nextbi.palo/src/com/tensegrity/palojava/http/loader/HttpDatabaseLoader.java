/*     */ package com.tensegrity.palojava.http.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import com.tensegrity.palojava.loader.DatabaseLoader;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class HttpDatabaseLoader extends DatabaseLoader
/*     */ {
/*     */   public HttpDatabaseLoader(DbConnection paloConnection)
/*     */   {
/*  55 */     super(paloConnection);
/*     */   }
/*     */ 
/*     */   public int getDatabaseCount() {
/*  59 */     reload();
/*  60 */     return getLoaded().size();
/*     */   }
/*     */ 
/*     */   public final String[] getAllDatabaseIds() {
/*  64 */     if (!this.loaded) {
/*  65 */       reload();
/*  66 */       this.loaded = true;
/*     */     }
/*  68 */     return getLoadedIds();
/*     */   }
/*     */ 
/*     */   public final DatabaseInfo loadByName(String name)
/*     */   {
/*  73 */     DatabaseInfo dbInfo = findDatabase(name);
/*  74 */     if (dbInfo == null)
/*     */     {
/*  76 */       reload();
/*  77 */       dbInfo = findDatabase(name);
/*     */     }
/*  79 */     return dbInfo;
/*     */   }
/*     */ 
/*     */   protected final void reload() {
/*  83 */     reset();
/*  84 */     DatabaseInfo[] dbInfos = this.paloConnection.getDatabases();
/*  85 */     for (DatabaseInfo dbInfo : dbInfos)
/*  86 */       loaded(dbInfo);
/*     */   }
/*     */ 
/*     */   private final DatabaseInfo findDatabase(String name)
/*     */   {
/*  91 */     Collection<PaloInfo> infos = getLoaded();
/*  92 */     for (PaloInfo info : infos) {
/*  93 */       if (info instanceof DatabaseInfo) {
/*  94 */         DatabaseInfo dbInfo = (DatabaseInfo)info;
/*     */ 
/*  96 */         if (dbInfo.getName().equalsIgnoreCase(name))
/*  97 */           return dbInfo;
/*     */       }
/*     */     }
/* 100 */     return null;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.loader.HttpDatabaseLoader
 * JD-Core Version:    0.5.4
 */