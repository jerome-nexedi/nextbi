/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ 
/*     */ class TextualSorter
/*     */   implements Comparator<ElementNode>
/*     */ {
/*     */   public int compare(ElementNode o1, ElementNode o2)
/*     */   {
/* 404 */     String e1Name = o1.getElement().getName();
/* 405 */     String e2Name = o2.getElement().getName();
/* 406 */     return e1Name.compareTo(e2Name);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.TextualSorter
 * JD-Core Version:    0.5.4
 */