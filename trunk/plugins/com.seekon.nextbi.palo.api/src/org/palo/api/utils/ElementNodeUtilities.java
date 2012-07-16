/*     */ package org.palo.api.utils;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.ElementNodeVisitor;
/*     */ import org.palo.api.Hierarchy;
/*     */ 
/*     */ public class ElementNodeUtilities
/*     */ {
/*     */   public static final String getPath(ElementNode elNode)
/*     */   {
/*  65 */     StringBuffer path = new StringBuffer();
/*  66 */     path.append(elNode.getElement().getId());
/*     */     ElementNode parent;
/*  68 */     while ((parent = elNode.getParent()) != null)
/*     */     {
/*     */       
/*  69 */       path.insert(0, ",");
/*  70 */       path.insert(0, parent.getElement().getId());
/*  71 */       elNode = parent;
/*     */     }
/*  73 */     return path.toString();
/*     */   }
/*     */ 
/*     */   public static final Element[] getPathElements(ElementNode elNode)
/*     */   {
/*  84 */     ArrayList elements = new ArrayList();
/*  85 */     addElements(elNode, elements);
/*  86 */     return (Element[])elements.toArray(new Element[elements.size()]);
/*     */   }
/*     */ 
/*     */   public static final ElementPath getPath(ElementNode elNode, Hierarchy hierarchy)
/*     */   {
/*  97 */     Element[] path = getPathElements(elNode);
/*  98 */     ElementPath elPath = new ElementPath();
/*  99 */     elPath.addPart(hierarchy, path);
/* 100 */     return elPath;
/*     */   }
/*     */ 
/*     */   public static final void traverse(ElementNode[] roots, ElementNodeVisitor visitor) {
/* 104 */     ElementNode[] arrayOfElementNode = roots; int j = roots.length; for (int i = 0; i < j; ++i) { ElementNode root = arrayOfElementNode[i];
/* 105 */       traverse(root, visitor); }
/*     */   }
/*     */ 
/*     */   private static final void traverse(ElementNode node, ElementNodeVisitor visitor)
/*     */   {
/* 110 */     traverse(node, null, visitor);
/*     */   }
/*     */ 
/*     */   private static final void addElements(ElementNode elNode, List elements)
/*     */   {
/* 115 */     ElementNode parent = elNode.getParent();
/* 116 */     if (parent != null) {
/* 117 */       addElements(parent, elements);
/*     */     }
/* 119 */     Element element = elNode.getElement();
/* 120 */     if (!elements.contains(element))
/* 121 */       elements.add(element);
/*     */   }
/*     */ 
/*     */   private static void traverse(ElementNode node, ElementNode parent, ElementNodeVisitor visitor)
/*     */   {
/* 126 */     visitor.visit(node, parent);
/* 127 */     ElementNode[] children = node.getChildren();
/* 128 */     for (ElementNode child : children)
/* 129 */       traverse(child, node, visitor);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.utils.ElementNodeUtilities
 * JD-Core Version:    0.5.4
 */