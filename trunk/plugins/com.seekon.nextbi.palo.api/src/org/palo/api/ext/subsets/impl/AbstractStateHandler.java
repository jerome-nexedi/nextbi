/*    */ package org.palo.api.ext.subsets.impl;
/*    */ 
/*    */ import org.palo.api.Subset;
/*    */ import org.palo.api.SubsetState;
/*    */ import org.palo.api.ext.subsets.SubsetStateHandler;
/*    */ 
/*    */ abstract class AbstractStateHandler
/*    */   implements SubsetStateHandler
/*    */ {
/*    */   protected Subset subset;
/*    */   protected SubsetState subsetState;
/*    */ 
/*    */   public final synchronized Subset getSubset()
/*    */   {
/* 56 */     return this.subset;
/*    */   }
/*    */ 
/*    */   public final synchronized SubsetState getSubsetState() {
/* 60 */     return this.subsetState;
/*    */   }
/*    */ 
/*    */   public final synchronized void use(Subset subset, SubsetState subsetState) {
/* 64 */     this.subset = subset;
/* 65 */     this.subsetState = subsetState;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.subsets.impl.AbstractStateHandler
 * JD-Core Version:    0.5.4
 */