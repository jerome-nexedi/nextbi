/*     */ package com.tensegrity.palojava.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ 
/*     */ public class DatabaseInfoImpl
/*     */   implements DatabaseInfo
/*     */ {
/*     */   private final String id;
/*     */   private final int type;
/*     */   private String name;
/*     */   private int cubeCount;
/*     */   private int dimCount;
/*     */   private int status;
/*     */   private int token;
/*     */ 
/*     */   public DatabaseInfoImpl(String id, int type)
/*     */   {
/*  48 */     this.id = id;
/*  49 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public final synchronized int getCubeCount() {
/*  53 */     return this.cubeCount;
/*     */   }
/*     */ 
/*     */   public final synchronized void setCubeCount(int cubeCount) {
/*  57 */     this.cubeCount = cubeCount;
/*     */   }
/*     */ 
/*     */   public final synchronized int getDimensionCount() {
/*  61 */     return this.dimCount;
/*     */   }
/*     */ 
/*     */   public final synchronized void setDimensionCount(int dimCount) {
/*  65 */     this.dimCount = dimCount;
/*     */   }
/*     */ 
/*     */   public final synchronized int getStatus() {
/*  69 */     return this.status;
/*     */   }
/*     */ 
/*     */   public final synchronized void setStatus(int status) {
/*  73 */     this.status = status;
/*     */   }
/*     */ 
/*     */   public final synchronized int getToken() {
/*  77 */     return this.token;
/*     */   }
/*     */ 
/*     */   public final synchronized void setToken(int token) {
/*  81 */     this.token = token;
/*     */   }
/*     */ 
/*     */   public final synchronized void setName(String name)
/*     */   {
/*  86 */     this.name = name;
/*     */   }
/*     */ 
/*     */   public final String getId()
/*     */   {
/*  91 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final synchronized String getName() {
/*  95 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final int getType() {
/*  99 */     return this.type;
/*     */   }
/*     */ 
/*     */   public boolean isSystem() {
/* 103 */     return this.type == 1;
/*     */   }
/*     */ 
/*     */   public boolean isUserInfo() {
/* 107 */     return this.type == 3;
/*     */   }
/*     */ 
/*     */   public boolean canBeModified() {
/* 111 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren() {
/* 115 */     return true;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.impl.DatabaseInfoImpl
 * JD-Core Version:    0.5.4
 */