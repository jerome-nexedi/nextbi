/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ 
/*     */ class AliasSorter
/*     */   implements Comparator<ElementNode>
/*     */ {
/*     */   private final AliasFilter aliasFilter;
/*     */ 
/*     */   AliasSorter(AliasFilter aliasFilter)
/*     */   {
/* 414 */     this.aliasFilter = aliasFilter;
/*     */   }
/*     */ 
/*     */   public int compare(ElementNode o1, ElementNode o2) {
/* 418 */     Element e1 = o1.getElement();
/* 419 */     Element e2 = o2.getElement();
/*     */ 
/* 427 */     String e1Name = this.aliasFilter.getAlias(e1);
/* 428 */     String e2Name = this.aliasFilter.getAlias(e2);
/* 429 */     return e1Name.compareTo(e2Name);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.AliasSorter
 * JD-Core Version:    0.5.4
 */