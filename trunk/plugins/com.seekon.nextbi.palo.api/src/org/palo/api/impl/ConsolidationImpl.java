/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import org.palo.api.Consolidation;
/*     */ import org.palo.api.Element;
/*     */ 
/*     */ class ConsolidationImpl
/*     */   implements Consolidation
/*     */ {
/*     */   private final Element parent;
/*     */   private final Element child;
/*     */   private final double weight;
/*     */ 
/*     */   static final ConsolidationImpl create(ConnectionImpl connection, Element parent, Element element, double weight)
/*     */   {
/*  73 */     return new ConsolidationImpl(parent, element, weight);
/*     */   }
/*     */ 
/*     */   private ConsolidationImpl(Element parent, Element child, double weight)
/*     */   {
/*  85 */     this.parent = parent;
/*  86 */     this.child = child;
/*  87 */     this.weight = weight;
/*     */   }
/*     */ 
/*     */   private final CompoundKey createKey()
/*     */   {
/*  92 */     return new CompoundKey(new Object[] { 
/*  93 */       ConsolidationImpl.class, 
/*  94 */       this.parent, 
/*  95 */       this.child, 
/*  96 */       new Double(this.weight) });
/*     */   }
/*     */ 
/*     */   public final Element getParent()
/*     */   {
/* 102 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public final Element getChild()
/*     */   {
/* 107 */     return this.child;
/*     */   }
/*     */ 
/*     */   public final double getWeight()
/*     */   {
/* 112 */     return this.weight;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj)
/*     */   {
/* 117 */     if (!(obj instanceof ConsolidationImpl))
/* 118 */       return false;
/* 119 */     ConsolidationImpl other = (ConsolidationImpl)obj;
/* 120 */     return createKey().equals(other.createKey());
/*     */   }
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 125 */     return createKey().hashCode();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.ConsolidationImpl
 * JD-Core Version:    0.5.4
 */