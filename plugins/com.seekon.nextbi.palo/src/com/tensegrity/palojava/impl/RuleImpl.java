/*     */ package com.tensegrity.palojava.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.CubeInfo;
/*     */ import com.tensegrity.palojava.RuleInfo;
/*     */ 
/*     */ public class RuleImpl
/*     */   implements RuleInfo
/*     */ {
/*     */   private final String id;
/*     */   private final CubeInfo cube;
/*     */   private String definition;
/*     */   private long timestamp;
/*     */   private String comment;
/*     */   private String externalId;
/*     */   private boolean useExternalId;
/*  60 */   private boolean active = true;
/*     */ 
/*     */   public RuleImpl(CubeInfo cube, String id) {
/*  63 */     this.cube = cube;
/*  64 */     this.id = id;
/*     */   }
/*     */ 
/*     */   public final CubeInfo getCube() {
/*  68 */     return this.cube;
/*     */   }
/*     */ 
/*     */   public final String getDefinition() {
/*  72 */     return this.definition;
/*     */   }
/*     */ 
/*     */   public final String getExternalIdentifier()
/*     */   {
/*  80 */     return this.externalId;
/*     */   }
/*     */ 
/*     */   public final String getId() {
/*  84 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final int getType() {
/*  88 */     return -1;
/*     */   }
/*     */ 
/*     */   public final String getComment() {
/*  92 */     return this.comment;
/*     */   }
/*     */ 
/*     */   public final void setComment(String comment) {
/*  96 */     this.comment = comment;
/*     */   }
/*     */ 
/*     */   public final void setDefinition(String definition) {
/* 100 */     this.definition = definition;
/*     */   }
/*     */ 
/*     */   public final void setExternalIdentifier(String externalId)
/*     */   {
/* 108 */     this.externalId = externalId;
/*     */   }
/*     */ 
/*     */   public final void useExternalIdentifier(boolean b) {
/* 112 */     this.useExternalId = b;
/*     */   }
/*     */ 
/*     */   public final boolean useExternalIdentifier() {
/* 116 */     return this.useExternalId;
/*     */   }
/*     */ 
/*     */   public boolean canBeModified() {
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren() {
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   public final boolean isActive() {
/* 128 */     return this.active;
/*     */   }
/*     */   public final void setActive(boolean activate) {
/* 131 */     this.active = activate;
/*     */   }
/*     */ 
/*     */   public final long getTimestamp() {
/* 135 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public final void setTimestamp(long timestamp)
/*     */   {
/* 142 */     this.timestamp = timestamp;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.impl.RuleImpl
 * JD-Core Version:    0.5.4
 */