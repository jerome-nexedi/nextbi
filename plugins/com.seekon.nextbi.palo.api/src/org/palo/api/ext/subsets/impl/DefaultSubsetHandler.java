/*     */ package org.palo.api.ext.subsets.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.ElementNodeVisitor;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.HierarchyFilter;
/*     */ import org.palo.api.SubsetState;
/*     */ import org.palo.api.ext.subsets.SubsetHandler;
/*     */ import org.palo.api.utils.ElementNodeUtilities;
/*     */ 
/*     */ class DefaultSubsetHandler
/*     */   implements SubsetHandler
/*     */ {
/*     */   private Set elements;
/*     */   private Hierarchy hierarchy;
/*     */   private HierarchyFilter filter;
/*     */   private SubsetState subsetState;
/*     */ 
/*     */   final synchronized void use(Hierarchy hierarchy, HierarchyFilter filter, SubsetState subsetState)
/*     */   {
/*  75 */     this.hierarchy = hierarchy;
/*  76 */     this.filter = filter;
/*  77 */     this.subsetState = subsetState;
/*  78 */     this.elements = filterElements();
/*     */   }
/*     */ 
/*     */   public final boolean isFlat() {
/*  82 */     return this.filter.isFlat();
/*     */   }
/*     */ 
/*     */   public final ElementNode[] getVisibleRootNodes() {
/*  86 */     Collection rootNodes = getVisibleRootNodesAsList();
/*  87 */     return (ElementNode[])rootNodes.toArray(
/*  88 */       new ElementNode[rootNodes.size()]);
/*     */   }
/*     */ 
/*     */   public final synchronized List getVisibleRootNodesAsList() {
/*  92 */     final ArrayList rootNodes = new ArrayList();
/*  93 */     final LinkedHashMap elementNodes = new LinkedHashMap();
/*  94 */     final boolean isFlat = isFlat();
/*  95 */     this.hierarchy.visitElementTree(new ElementNodeVisitor()
/*     */     {
/*     */       public void visit(ElementNode elementNode, ElementNode parent)
/*     */       {
/*  99 */         Element element = elementNode.getElement();
/* 100 */         if (!DefaultSubsetHandler.this.elements.contains(element)) {
/* 101 */           return;
/*     */         }
/* 103 */         if (element == null)
/* 104 */           return;
/* 105 */         if (isFlat) {
/* 106 */           ElementNode elNode = new ElementNode(element, null);
/* 107 */           rootNodes.add(elNode);
/*     */         }
/*     */         else {
/* 110 */           if (!DefaultSubsetHandler.this.subsetState.getId().equals("regularexpression")) {
/* 111 */             String path = ElementNodeUtilities.getPath(elementNode);
/* 112 */             if (!DefaultSubsetHandler.this.subsetState.containsPath(element, path))
/* 113 */               return;
/*     */           }
/* 115 */           ElementNode elNode = 
/* 116 */             new ElementNode(element, elementNode.getConsolidation());
/* 117 */           elementNodes.put(elementNode, elNode);
/*     */           ElementNode newParent;
/* 119 */           if ((newParent = (ElementNode)elementNodes.get(parent)) != null) {
/* 120 */             newParent.forceAddChild(elNode);
/* 121 */             elNode.setParent(newParent);
/*     */           } else {
/* 123 */             rootNodes.add(elNode);
/*     */           }
/*     */         }
/*     */       }
/*     */     });
/* 128 */     if (isFlat) {
/* 129 */       if (this.filter instanceof FlatStateFilter) {
/* 130 */         rootNodes.clear();
/* 131 */         ((FlatStateFilter)this.filter).collectRootNodes(rootNodes);
/*     */       }
/*     */       else
/*     */       {
/* 139 */         ElementNode[] result = this.filter
/* 140 */           .postprocessRootNodes(
/* 141 */           (ElementNode[])rootNodes
/* 141 */           .toArray(new ElementNode[0]));
/* 142 */         if (result != null) {
/* 143 */           rootNodes.clear();
/* 144 */           rootNodes.addAll(Arrays.asList(result));
/*     */         }
/*     */       }
/*     */     }
/* 148 */     return rootNodes;
/*     */   }
/*     */ 
/*     */   public final synchronized Element[] getVisibleElements()
/*     */   {
/* 154 */     return (Element[])this.elements.toArray(new Element[this.elements.size()]);
/*     */   }
/*     */ 
/*     */   public final synchronized List getVisibleElementsAsList() {
/* 158 */     return new ArrayList(this.elements);
/*     */   }
/*     */ 
/*     */   public final synchronized boolean isVisible(Element element) {
/* 162 */     return this.elements.contains(element);
/*     */   }
/*     */ 
/*     */   private final Set filterElements()
/*     */   {
/* 172 */     if (this.filter == null)
/* 173 */       return new HashSet(Arrays.asList(this.hierarchy.getElements()));
/* 174 */     this.filter.init(this.hierarchy);
/* 175 */     Element[] sourceElements = this.hierarchy.getElements();
/* 176 */     if (sourceElements == null) {
/* 177 */       sourceElements = new Element[0];
/*     */     }
/* 179 */     Set elements = new HashSet();
/* 180 */     for (int i = 0; i < sourceElements.length; ++i) {
/* 181 */       Element element = sourceElements[i];
/* 182 */       if (!this.filter.acceptElement(element))
/*     */         continue;
/* 184 */       elements.add(element);
/*     */     }
/* 186 */     return elements;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.subsets.impl.DefaultSubsetHandler
 * JD-Core Version:    0.5.4
 */