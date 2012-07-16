/*     */ package com.tensegrity.palojava.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.HierarchyInfo;
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import java.util.Map;
/*     */ 
/*     */ public abstract class HierarchyLoader extends PaloInfoLoader
/*     */ {
/*     */   protected final DimensionInfo dimension;
/*     */ 
/*     */   public HierarchyLoader(DbConnection paloConnection, DimensionInfo dimension)
/*     */   {
/*  63 */     super(paloConnection);
/*  64 */     this.dimension = dimension;
/*     */   }
/*     */ 
/*     */   public abstract int getHierarchyCount();
/*     */ 
/*     */   public abstract String[] getAllHierarchyIds();
/*     */ 
/*     */   public final HierarchyInfo create()
/*     */   {
/*  85 */     throw new PaloException("Currently not possible!!");
/*     */   }
/*     */ 
/*     */   public final boolean delete(HierarchyInfo hierInfo)
/*     */   {
/* 100 */     throw new PaloException("Currently not possible!!");
/*     */   }
/*     */ 
/*     */   public final HierarchyInfo load(String id)
/*     */   {
/* 115 */     PaloInfo hierarchy = (PaloInfo)this.loadedInfo.get(id);
/* 116 */     if (hierarchy == null) {
/* 117 */       hierarchy = this.paloConnection.getHierarchy(this.dimension, id);
/* 118 */       loaded(hierarchy);
/*     */     }
/* 120 */     return (HierarchyInfo)hierarchy;
/*     */   }
/*     */ 
/*     */   public final HierarchyInfo load(int index)
/*     */   {
/* 129 */     String[] hierIds = getAllHierarchyIds();
/* 130 */     if ((index < 0) || (index > hierIds.length - 1))
/* 131 */       return null;
/* 132 */     return load(hierIds[index]);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.loader.HierarchyLoader
 * JD-Core Version:    0.5.4
 */