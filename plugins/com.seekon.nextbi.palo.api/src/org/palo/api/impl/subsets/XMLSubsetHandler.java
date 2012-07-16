/*     */ package org.palo.api.impl.subsets;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.impl.xml.IPaloEndHandler;
/*     */ import org.palo.api.impl.xml.IPaloStartHandler;
/*     */ 
/*     */ abstract class XMLSubsetHandler
/*     */ {
/*  58 */   private final HashMap endHandlers = new HashMap();
/*  59 */   private final HashMap startHandlers = new HashMap();
/*     */   protected SubsetBuilder subsetBuilder;
/*     */   protected SubsetStateBuilder stateBuilder;
/*     */ 
/*     */   XMLSubsetHandler(Database database)
/*     */   {
/*  64 */     registerHandlers(database);
/*     */   }
/*     */ 
/*     */   final Subset getSubset()
/*     */   {
/*  72 */     if (this.subsetBuilder != null)
/*  73 */       return this.subsetBuilder.createSubset();
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   final IPaloStartHandler[] getStartHandlers()
/*     */   {
/*  83 */     return (IPaloStartHandler[])this.startHandlers.values().toArray(
/*  84 */       new IPaloStartHandler[this.startHandlers.size()]);
/*     */   }
/*     */ 
/*     */   final IPaloEndHandler[] getEndHandlers()
/*     */   {
/*  93 */     return (IPaloEndHandler[])this.endHandlers.values().toArray(
/*  94 */       new IPaloEndHandler[this.endHandlers.size()]);
/*     */   }
/*     */ 
/*     */   abstract IPaloStartHandler[] getStartHandlers(Database paramDatabase);
/*     */ 
/*     */   abstract IPaloEndHandler[] getEndHandlers(Database paramDatabase);
/*     */ 
/*     */   private final void registerHandlers(Database database)
/*     */   {
/* 114 */     IPaloStartHandler[] stHandlers = getStartHandlers(database);
/* 115 */     for (int i = 0; i < stHandlers.length; ++i) {
/* 116 */       this.startHandlers.put(stHandlers[i].getPath(), stHandlers[i]);
/*     */     }
/* 118 */     IPaloEndHandler[] enHandlers = getEndHandlers(database);
/* 119 */     for (int i = 0; i < enHandlers.length; ++i)
/* 120 */       this.endHandlers.put(enHandlers[i].getPath(), enHandlers[i]);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.subsets.XMLSubsetHandler
 * JD-Core Version:    0.5.4
 */