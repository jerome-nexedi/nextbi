/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import org.palo.api.Consolidation;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.ElementNodeVisitor;
/*     */ 
/*     */ class DimensionUtil
/*     */ {
/*     */   public static void traverse(Element e, ElementVisitor v)
/*     */   {
/*  57 */     traverse(e, null, v);
/*     */   }
/*     */ 
/*     */   static void traverse(Element e, Element p, ElementVisitor v)
/*     */   {
/*  62 */     v.visit(e, p);
/*  63 */     Element[] children = e.getChildren();
/*  64 */     if (children == null)
/*  65 */       return;
/*  66 */     for (int i = 0; i < children.length; ++i)
/*     */     {
/*  68 */       traverse(children[i], e, v);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void traverse(ElementNode n, ElementNodeVisitor v)
/*     */   {
/*  76 */     traverse(n, null, v);
/*     */   }
/*     */ 
/*     */   public static void forceTraverse(ElementNode n, ElementNodeVisitor v)
/*     */   {
/*  81 */     forceTraverse(n, null, v);
/*     */   }
/*     */ 
/*     */   static void traverse(ElementNode n, ElementNode p, ElementNodeVisitor v)
/*     */   {
/*  86 */     v.visit(n, p);
/*  87 */     Element[] children = n.getElement().getChildren();
/*  88 */     Consolidation[] consolidations = n.getElement().getConsolidations();
/*  89 */     if (children == null)
/*  90 */       return;
/*  91 */     for (int i = 0; i < children.length; ++i)
/*     */     {
/*  93 */       if (children[i] == null)
/*     */         continue;
/*  95 */       ElementNode child = new ElementNode(children[i], consolidations[i]);
/*  96 */       n.forceAddChild(child);
/*  97 */       traverse(child, n, v);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void forceTraverse(ElementNode n, ElementNode p, ElementNodeVisitor v)
/*     */   {
/* 103 */     v.visit(n, p);
/* 104 */     Element[] children = n.getElement().getChildren();
/* 105 */     Consolidation[] consolidations = n.getElement().getConsolidations();
/* 106 */     if (children == null)
/* 107 */       return;
/* 108 */     int index = 0;
/* 109 */     for (int i = 0; i < children.length; ++i)
/*     */     {
/* 111 */       if (children[i] == null)
/*     */         continue;
/* 113 */       ElementNode child = new ElementNode(children[i], consolidations[i], index++);
/* 114 */       n.forceAddChild(child);
/* 115 */       forceTraverse(child, n, v);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface ElementVisitor
/*     */   {
/*     */     public abstract void visit(Element paramElement1, Element paramElement2);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.DimensionUtil
 * JD-Core Version:    0.5.4
 */