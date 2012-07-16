/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.Hierarchy;
/*     */ 
/*     */ class AttributeSorter
/*     */   implements Comparator<ElementNode>
/*     */ {
/*     */   private final String attributeID;
/*     */ 
/*     */   public AttributeSorter(String attributeID)
/*     */   {
/* 460 */     this.attributeID = attributeID;
/*     */   }
/*     */   public int compare(ElementNode o1, ElementNode o2) {
/* 463 */     Hierarchy hier1 = o1.getElement().getHierarchy();
/* 464 */     Hierarchy attrHier1 = hier1.getAttributeHierarchy();
/* 465 */     if (attrHier1 == null) {
/* 466 */       return 0;
/*     */     }
/* 468 */     Element attrEl = attrHier1.getElementById(this.attributeID);
/* 469 */     if (attrEl == null)
/* 470 */       return 0;
/* 471 */     Attribute attr = hier1.getAttribute(attrEl.getId());
/*     */ 
/* 473 */     if (attr == null)
/* 474 */       return 0;
/* 475 */     Object attr1 = o1.getElement().getAttributeValue(attr);
/* 476 */     Object attr2 = o2.getElement().getAttributeValue(attr);
/* 477 */     String a1 = (attr1 != null) ? attr1.toString() : "";
/* 478 */     String a2 = (attr2 != null) ? attr2.toString() : "";
/*     */ 
/* 480 */     if (attr.getType() == 0)
/*     */       try {
/* 482 */         Double db1 = (a1.length() > 0) ? Double.valueOf(a1) : new Double(0.0D);
/* 483 */         Double db2 = (a2.length() > 0) ? Double.valueOf(a2) : new Double(0.0D);
/* 484 */         return db1.compareTo(db2);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException)
/*     */       {
/*     */       }
/* 489 */     return a1.compareTo(a2);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.AttributeSorter
 * JD-Core Version:    0.5.4
 */