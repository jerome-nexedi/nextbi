/*    */ package com.tensegrity.palojava.impl;
/*    */ 
/*    */ import com.tensegrity.palojava.LockInfo;
/*    */ 
/*    */ public class LockInfoImpl
/*    */   implements LockInfo
/*    */ {
/*    */   private final String id;
/*    */   private final String user;
/*    */   private int steps;
/*    */   private String[][] area;
/*    */ 
/*    */   public LockInfoImpl(String id, String user)
/*    */   {
/* 56 */     this.id = id;
/* 57 */     this.user = user;
/*    */   }
/*    */ 
/*    */   public final String getId() {
/* 61 */     return this.id;
/*    */   }
/*    */ 
/*    */   public final String getUser() {
/* 65 */     return this.user;
/*    */   }
/*    */ 
/*    */   public final void setSteps(int steps) {
/* 69 */     this.steps = steps;
/*    */   }
/*    */ 
/*    */   public final int getSteps() {
/* 73 */     return this.steps;
/*    */   }
/*    */ 
/*    */   public final void setArea(String[][] area) {
/* 77 */     this.area = area;
/*    */   }
/*    */ 
/*    */   public final String[][] getArea() {
/* 81 */     return this.area;
/*    */   }
/*    */ 
/*    */   public final int hashCode() {
/* 85 */     int hc = 17;
/* 86 */     hc += 23 * this.id.hashCode();
/* 87 */     hc += 23 * this.user.hashCode();
/* 88 */     return hc;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.impl.LockInfoImpl
 * JD-Core Version:    0.5.4
 */