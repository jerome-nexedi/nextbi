/*     */ package com.tensegrity.palojava.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class DimensionLoader extends PaloInfoLoader
/*     */ {
/*     */   protected final DatabaseInfo database;
/*     */ 
/*     */   public DimensionLoader(DbConnection paloConnection, DatabaseInfo database)
/*     */   {
/*  60 */     super(paloConnection);
/*  61 */     this.database = database;
/*     */   }
/*     */ 
/*     */   public abstract String[] getAllDimensionIds();
/*     */ 
/*     */   public abstract String[] getDimensionIds(int paramInt);
/*     */ 
/*     */   public abstract DimensionInfo loadByName(String paramString);
/*     */ 
/*     */   public final DimensionInfo create(String name, int type)
/*     */   {
/*  92 */     DimensionInfo dimInfo = this.paloConnection.addDimension(this.database, name, type);
/*  93 */     loaded(dimInfo);
/*  94 */     return dimInfo;
/*     */   }
/*     */ 
/*     */   public final boolean delete(DimensionInfo dimInfo)
/*     */   {
/* 105 */     if (this.paloConnection.delete(dimInfo)) {
/* 106 */       removed(dimInfo);
/* 107 */       return true;
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   public final DimensionInfo load(String id)
/*     */   {
/* 119 */     PaloInfo dim = (PaloInfo)this.loadedInfo.get(id);
/* 120 */     if (dim == null) {
/* 121 */       dim = this.paloConnection.getDimension(this.database, id);
/* 122 */       if (dim == null) {
/* 123 */         return null;
/*     */       }
/* 125 */       loaded(dim);
/*     */     }
/* 127 */     return (DimensionInfo)dim;
/*     */   }
/*     */ 
/*     */   public final DimensionInfo load(int index)
/*     */   {
/* 136 */     String[] dimIds = getAllDimensionIds();
/* 137 */     if ((index < 0) || (index > dimIds.length - 1))
/* 138 */       return null;
/* 139 */     return load(dimIds[index]);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.loader.DimensionLoader
 * JD-Core Version:    0.5.4
 */