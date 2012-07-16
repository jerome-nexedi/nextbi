/*     */ package org.palo.viewapi;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public enum Right
/*     */ {
/*  57 */   NONE(0, "N"), 
/*  58 */   READ(1, "R"), 
/*  59 */   WRITE(2, "W"), 
/*  60 */   DELETE(4, "D"), 
/*  61 */   CREATE(8, "C"), 
/*  62 */   GRANT(16, "G");
/*     */ 
/*     */   private static final Map<String, Right> stringToRight;
/*     */   private final String tag;
/*     */   private final int priority;
/*     */ 
/*  64 */   static { stringToRight = 
/*  65 */       new HashMap();
/*     */ 
/*  67 */     for (Right right : values())
/*  68 */       stringToRight.put(right.toString(), right); }
/*     */ 
/*     */ 
/*     */   private Right(int priority, String tag)
/*     */   {
/*  75 */     this.tag = tag;
/*  76 */     this.priority = priority;
/*     */   }
/*     */ 
/*     */   public final int getPriority()
/*     */   {
/*  84 */     return this.priority;
/*     */   }
/*     */ 
/*     */   public final String toString() {
/*  88 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public static final Right fromString(String right)
/*     */   {
/* 100 */     return (Right)stringToRight.get(right);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.Right
 * JD-Core Version:    0.5.4
 */