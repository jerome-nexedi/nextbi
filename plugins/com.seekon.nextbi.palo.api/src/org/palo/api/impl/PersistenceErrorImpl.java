/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import org.palo.api.persistence.PersistenceError;
/*     */ 
/*     */ public class PersistenceErrorImpl
/*     */   implements PersistenceError
/*     */ {
/*     */   private final Object src;
/*     */   private final String msg;
/*     */   private final String srcId;
/*     */   private final Object location;
/*     */   private final String cause;
/*     */   private final int type;
/*     */   private final Object section;
/*     */   private final int sectionType;
/*     */ 
/*     */   public PersistenceErrorImpl(String msg, String srcId, Object src, Object location, String cause, int type, Object section, int sectionType)
/*     */   {
/*  60 */     this.msg = msg;
/*  61 */     this.src = src;
/*  62 */     this.srcId = srcId;
/*  63 */     this.cause = cause;
/*  64 */     this.location = location;
/*  65 */     if (!typeIsOk(type))
/*  66 */       type = -1;
/*  67 */     this.type = type;
/*  68 */     this.section = section;
/*  69 */     this.sectionType = sectionType;
/*     */   }
/*     */ 
/*     */   public final String getSourceId() {
/*  73 */     return this.srcId;
/*     */   }
/*     */ 
/*     */   public final String getCause() {
/*  77 */     return this.cause;
/*     */   }
/*     */ 
/*     */   public final Object getLocation() {
/*  81 */     return this.location;
/*     */   }
/*     */ 
/*     */   public final String getMessage() {
/*  85 */     return this.msg;
/*     */   }
/*     */ 
/*     */   public final int getType() {
/*  89 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final Object getSource() {
/*  93 */     return this.src;
/*     */   }
/*     */ 
/*     */   public final Object getSection() {
/*  97 */     return this.section;
/*     */   }
/*     */ 
/*     */   public int getTargetType() {
/* 101 */     return this.sectionType;
/*     */   }
/*     */ 
/*     */   private final boolean typeIsOk(int type)
/*     */   {
/* 106 */     for (int i = 0; i < ALL_ERROR_TYPES.length; ++i)
/* 107 */       if (type == ALL_ERROR_TYPES[i])
/* 108 */         return true;
/* 109 */     return false;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.PersistenceErrorImpl
 * JD-Core Version:    0.5.4
 */