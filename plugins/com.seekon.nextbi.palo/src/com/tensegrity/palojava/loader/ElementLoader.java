/*     */ package com.tensegrity.palojava.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.HierarchyInfo;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class ElementLoader extends PaloInfoLoader
/*     */ {
/*     */   protected final HierarchyInfo hierarchy;
/*     */ 
/*     */   public ElementLoader(DbConnection paloConnection, HierarchyInfo hierarchy)
/*     */   {
/*  67 */     super(paloConnection);
/*  68 */     this.hierarchy = hierarchy;
/*     */   }
/*     */ 
/*     */   public abstract String[] getAllElementIds();
/*     */ 
/*     */   public abstract ElementInfo loadByName(String paramString);
/*     */ 
/*     */   public abstract ElementInfo load(int paramInt);
/*     */ 
/*     */   public abstract ElementInfo[] getElementsAtDepth(int paramInt);
/*     */ 
/*     */   public abstract ElementInfo[] getChildren(ElementInfo paramElementInfo);
/*     */ 
/*     */   public final ElementInfo create(String name, int type, ElementInfo[] children, double[] weights)
/*     */   {
/* 105 */     ElementInfo elInfo = 
/* 106 */       this.paloConnection.addElement(this.hierarchy, name, type, children, weights);
/* 107 */     loaded(elInfo);
/* 108 */     return elInfo;
/*     */   }
/*     */ 
/*     */   public final ElementInfo[] createBulk(String[] names, int type, ElementInfo[][] children, double[][] weights)
/*     */   {
/* 113 */     List newElements = new ArrayList(names.length);
/* 114 */     if (this.paloConnection.addElements(this.hierarchy.getDimension(), names, type, 
/* 115 */       children, weights)) {
/* 116 */       ElementInfo[] allElements = 
/* 117 */         this.paloConnection.getElements(this.hierarchy.getDimension());
/* 118 */       Set reqNames = new HashSet(Arrays.asList(names));
/* 119 */       for (ElementInfo el : allElements) {
/* 120 */         if (reqNames.contains(el.getName())) {
/* 121 */           newElements.add(el);
/* 122 */           loaded(el);
/*     */         }
/*     */       }
/*     */     }
/* 126 */     return (ElementInfo[])newElements.toArray(new ElementInfo[0]);
/*     */   }
/*     */ 
/*     */   public final ElementInfo[] createBulk(String[] names, int[] types, ElementInfo[][] children, double[][] weights)
/*     */   {
/* 131 */     List newElements = new ArrayList(names.length);
/* 132 */     if (this.paloConnection.addElements(this.hierarchy.getDimension(), names, types, 
/* 133 */       children, weights)) {
/* 134 */       ElementInfo[] allElements = 
/* 135 */         this.paloConnection.getElements(this.hierarchy.getDimension());
/* 136 */       Set reqNames = new HashSet(Arrays.asList(names));
/* 137 */       for (ElementInfo el : allElements) {
/* 138 */         if (reqNames.contains(el.getName())) {
/* 139 */           newElements.add(el);
/* 140 */           loaded(el);
/*     */         }
/*     */       }
/*     */     }
/* 144 */     return (ElementInfo[])newElements.toArray(new ElementInfo[0]);
/*     */   }
/*     */ 
/*     */   public final ElementInfo[] replaceBulk(ElementInfo[] elements, int type, ElementInfo[][] children, Double[][] weights)
/*     */   {
/* 149 */     if (this.paloConnection.replaceBulk(this.hierarchy.getDimension(), elements, type, children, weights)) {
/* 150 */       return this.paloConnection.getElements(this.hierarchy.getDimension());
/*     */     }
/* 152 */     return new ElementInfo[0];
/*     */   }
/*     */ 
/*     */   public final boolean delete(ElementInfo elInfo)
/*     */   {
/* 163 */     if (this.paloConnection.delete(elInfo)) {
/* 164 */       removed(elInfo);
/* 165 */       return true;
/*     */     }
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean delete(ElementInfo[] elInfos) {
/* 171 */     if (this.paloConnection.delete(elInfos)) {
/* 172 */       for (ElementInfo elInfo : elInfos)
/* 173 */         removed(elInfo);
/* 174 */       return true;
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   public final ElementInfo load(String id)
/*     */   {
/* 186 */     PaloInfo el = (PaloInfo)this.loadedInfo.get(id);
/* 187 */     if (el == null) {
/* 188 */       el = this.paloConnection.getElement(this.hierarchy, id);
/* 189 */       loaded(el);
/*     */     }
/* 191 */     return (ElementInfo)el;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.loader.ElementLoader
 * JD-Core Version:    0.5.4
 */