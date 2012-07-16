/*    */ package org.palo.api.subsets.filter.settings;
/*    */ 
/*    */ import org.palo.api.subsets.Subset2;
/*    */ 
/*    */ public abstract class AbstractFilterSettings
/*    */   implements FilterSetting
/*    */ {
/*    */   protected Subset2 subset;
/*    */ 
/*    */   public void bind(Subset2 subset)
/*    */   {
/* 55 */     this.subset = subset;
/* 56 */     markDirty();
/*    */   }
/*    */   public void unbind() {
/* 59 */     this.subset = null;
/*    */   }
/*    */ 
/*    */   protected final void markDirty() {
/* 63 */     if (this.subset != null)
/* 64 */       this.subset.modified();
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.AbstractFilterSettings
 * JD-Core Version:    0.5.4
 */