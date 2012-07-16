/*     */ package com.tensegrity.palojava.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class PaloInfoLoader
/*     */ {
/*     */   protected final DbConnection paloConnection;
/*     */   protected final Map<String, PaloInfo> loadedInfo;
/*  61 */   protected boolean loaded = false;
/*     */ 
/*     */   public PaloInfoLoader(DbConnection paloConnection)
/*     */   {
/*  79 */     this.paloConnection = paloConnection;
/*  80 */     this.loadedInfo = new LinkedHashMap();
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/*  88 */     this.loaded = false;
/*  89 */     this.loadedInfo.clear();
/*     */   }
/*     */ 
/*     */   public final void loaded(PaloInfo info)
/*     */   {
/*  97 */     if (info != null)
/*  98 */       this.loadedInfo.put(info.getId(), info);
/*     */   }
/*     */ 
/*     */   protected abstract void reload();
/*     */ 
/*     */   public final void removed(String id)
/*     */   {
/* 114 */     this.loadedInfo.remove(id);
/*     */   }
/*     */ 
/*     */   protected final void removed(PaloInfo info)
/*     */   {
/* 122 */     removed(info.getId());
/*     */   }
/*     */ 
/*     */   protected final String[] getLoadedIds()
/*     */   {
/* 131 */     return (String[])this.loadedInfo.keySet().toArray(new String[this.loadedInfo.size()]);
/*     */   }
/*     */ 
/*     */   protected final Collection<PaloInfo> getLoaded()
/*     */   {
/* 139 */     return this.loadedInfo.values();
/*     */   }
/*     */ 
/*     */   protected final boolean hasType(int typeMask, int type) {
/* 143 */     return (typeMask & type) > 0;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.loader.PaloInfoLoader
 * JD-Core Version:    0.5.4
 */