/*    */ package org.palo.api.subsets.filter.settings;
/*    */ 
/*    */ import org.palo.api.subsets.Subset2;
/*    */ 
/*    */ abstract class AbstractParameter
/*    */   implements Parameter
/*    */ {
/*    */   private Subset2 subset;
/*    */ 
/*    */   public final void bind(Subset2 subset)
/*    */   {
/* 52 */     this.subset = subset;
/* 53 */     markDirty();
/*    */   }
/*    */ 
/*    */   public final void unbind() {
/* 57 */     this.subset = null;
/*    */   }
/*    */ 
/*    */   protected final void markDirty() {
/* 61 */     if (this.subset != null)
/* 62 */       this.subset.modified();
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.AbstractParameter
 * JD-Core Version:    0.5.4
 */