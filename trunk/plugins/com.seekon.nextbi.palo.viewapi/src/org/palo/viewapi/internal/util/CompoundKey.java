/*    */ package org.palo.viewapi.internal.util;
/*    */ 
/*    */ public class CompoundKey
/*    */ {
/*    */   private final Object[] objs;
/*    */ 
/*    */   public CompoundKey(Object[] objs)
/*    */   {
/* 46 */     this.objs = objs;
/*    */   }
/*    */ 
/*    */   public final int hashCode()
/*    */   {
/* 53 */     int hc = 23;
/* 54 */     for (int i = 0; i < this.objs.length; ++i) {
/* 55 */       if (this.objs[i] != null)
/* 56 */         hc += 37 * this.objs[i].hashCode();
/*    */     }
/* 58 */     return hc;
/*    */   }
/*    */ 
/*    */   public final boolean equals(Object obj) {
/* 62 */     if (!(obj instanceof CompoundKey)) {
/* 63 */       return false;
/*    */     }
/* 65 */     CompoundKey other = (CompoundKey)obj;
/*    */ 
/* 67 */     if (this.objs.length != other.objs.length) {
/* 68 */       return false;
/*    */     }
/* 70 */     for (int i = 0; i < this.objs.length; ++i) {
/* 71 */       if ((this.objs[i] == null) && (other.objs[i] != null)) {
/* 72 */         return false;
/*    */       }
/* 74 */       if (!this.objs[i].equals(other.objs[i])) {
/* 75 */         return false;
/*    */       }
/*    */     }
/* 78 */     return true;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.internal.util.CompoundKey
 * JD-Core Version:    0.5.4
 */