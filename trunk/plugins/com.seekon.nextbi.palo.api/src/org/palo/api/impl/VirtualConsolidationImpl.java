/*    */ package org.palo.api.impl;
/*    */ 
/*    */ import org.palo.api.Consolidation;
/*    */ import org.palo.api.Element;
/*    */ 
/*    */ class VirtualConsolidationImpl
/*    */   implements Consolidation
/*    */ {
/*    */   private final Element parent;
/*    */   private final Element child;
/*    */   private final double weight;
/*    */ 
/*    */   VirtualConsolidationImpl(Element parent, Element child, double weight)
/*    */   {
/* 54 */     this.parent = parent;
/* 55 */     this.child = child;
/* 56 */     this.weight = weight;
/*    */   }
/*    */ 
/*    */   public Element getParent()
/*    */   {
/* 61 */     return this.parent;
/*    */   }
/*    */ 
/*    */   public Element getChild()
/*    */   {
/* 66 */     return this.child;
/*    */   }
/*    */ 
/*    */   public double getWeight()
/*    */   {
/* 71 */     return this.weight;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj) {
/* 75 */     if (obj instanceof VirtualConsolidationImpl) {
/* 76 */       VirtualConsolidationImpl other = (VirtualConsolidationImpl)obj;
/* 77 */       return (this.parent.equals(other.parent)) && (this.child.equals(other.child));
/*    */     }
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode() {
/* 83 */     int hc = 17;
/* 84 */     hc += 31 * this.parent.hashCode();
/* 85 */     hc += 31 * this.child.hashCode();
/* 86 */     hc = (int)(hc + 31.0D * this.weight);
/* 87 */     return hc;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.VirtualConsolidationImpl
 * JD-Core Version:    0.5.4
 */