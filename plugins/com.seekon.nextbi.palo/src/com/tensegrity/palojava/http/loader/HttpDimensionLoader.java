/*     */ package com.tensegrity.palojava.http.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import com.tensegrity.palojava.loader.DimensionLoader;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class HttpDimensionLoader extends DimensionLoader
/*     */ {
/*     */   public HttpDimensionLoader(DbConnection paloConnection, DatabaseInfo database)
/*     */   {
/*  57 */     super(paloConnection, database);
/*     */   }
/*     */ 
/*     */   public String[] getAllDimensionIds()
/*     */   {
/*  62 */     if (!this.loaded) {
/*  63 */       reload();
/*  64 */       this.loaded = true;
/*     */     }
/*  66 */     return getLoadedIds();
/*     */   }
/*     */ 
/*     */   public String[] getDimensionIds(int typeMask) {
/*  70 */     DimensionInfo[] dims = this.paloConnection.getDimensions(this.database, typeMask);
/*  71 */     String[] ids = new String[dims.length];
/*  72 */     int counter = 0;
/*  73 */     for (DimensionInfo dim : dims) {
/*  74 */       loaded(dim);
/*  75 */       ids[(counter++)] = dim.getId();
/*     */     }
/*  77 */     return ids;
/*     */   }
/*     */ 
/*     */   public DimensionInfo loadByName(String name)
/*     */   {
/*  82 */     DimensionInfo dimInfo = findDimension(name);
/*  83 */     if (dimInfo == null)
/*     */     {
/*  85 */       reload();
/*  86 */       dimInfo = findDimension(name);
/*     */     }
/*  88 */     return dimInfo;
/*     */   }
/*     */ 
/*     */   protected final void reload() {
/*  92 */     reset();
/*  93 */     DimensionInfo[] dimInfos = this.paloConnection.getDimensions(this.database);
/*  94 */     for (DimensionInfo dimInfo : dimInfos)
/*  95 */       loaded(dimInfo);
/*     */   }
/*     */ 
/*     */   private final DimensionInfo findDimension(String name)
/*     */   {
/* 100 */     Collection<PaloInfo> infos = getLoaded();
/* 101 */     for (PaloInfo info : infos) {
/* 102 */       if (info instanceof DimensionInfo) {
/* 103 */         DimensionInfo dimInfo = (DimensionInfo)info;
/*     */ 
/* 105 */         if (dimInfo.getName().equalsIgnoreCase(name))
/* 106 */           return dimInfo;
/*     */       }
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.loader.HttpDimensionLoader
 * JD-Core Version:    0.5.4
 */