/*     */ package org.palo.api.impl.subsets;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.SubsetState;
/*     */ 
/*     */ class SubsetBuilder
/*     */ {
/*     */   private String id;
/*     */   private String name;
/*     */   private Attribute alias;
/*     */   private String activeStateId;
/*     */   private Hierarchy srcHierarchy;
/*  64 */   private Set states = new HashSet();
/*     */   private String description;
/*     */ 
/*     */   final void setId(String id)
/*     */   {
/*  70 */     this.id = id;
/*     */   }
/*     */ 
/*     */   final void setName(String name) {
/*  74 */     this.name = name;
/*     */   }
/*     */ 
/*     */   final void setDescription(String description) {
/*  78 */     this.description = description;
/*     */   }
/*     */ 
/*     */   final void setActiveState(String activeStateId) {
/*  82 */     this.activeStateId = activeStateId;
/*     */   }
/*     */ 
/*     */   final void setSourceHierarchy(Hierarchy srcHierarchy) {
/*  86 */     this.srcHierarchy = srcHierarchy;
/*     */   }
/*     */ 
/*     */   final void setAlias(Attribute alias)
/*     */   {
/*  91 */     this.alias = alias;
/*     */   }
/*     */ 
/*     */   final Hierarchy getSourceHierarchy() {
/*  95 */     return this.srcHierarchy;
/*     */   }
/*     */ 
/*     */   final void addState(SubsetState state) {
/*  99 */     this.states.add(state);
/*     */   }
/*     */ 
/*     */   final Subset createSubset() {
/* 103 */     if ((this.id == null) || (this.name == null) || (this.activeStateId == null) || 
/* 104 */       (this.srcHierarchy == null)) {
/* 105 */       throw new PaloAPIException(
/* 106 */         "Cannot create subset, insufficient information");
/*     */     }
/* 108 */     Subset subset = new SubsetImpl(this.id, this.name, this.srcHierarchy);
/*     */ 
/* 113 */     ((SubsetImpl)subset).reset();
/*     */ 
/* 116 */     for (Iterator it = this.states.iterator(); it.hasNext(); )
/* 117 */       subset.addState((SubsetState)it.next());
/* 118 */     ((SubsetImpl)subset).setActiveState(this.activeStateId);
/* 119 */     if (this.description != null)
/* 120 */       subset.setDescription(this.description);
/* 121 */     if (this.alias != null)
/* 122 */       subset.setAlias(this.alias);
/* 123 */     return subset;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.subsets.SubsetBuilder
 * JD-Core Version:    0.5.4
 */