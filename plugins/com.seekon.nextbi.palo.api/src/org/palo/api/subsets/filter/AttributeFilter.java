/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.filter.settings.AttributeConstraint;
/*     */ import org.palo.api.subsets.filter.settings.AttributeConstraintsMatrix;
/*     */ import org.palo.api.subsets.filter.settings.AttributeFilterSetting;
/*     */ import org.palo.api.subsets.filter.settings.ObjectParameter;
/*     */ 
/*     */ public class AttributeFilter extends AbstractSubsetFilter
/*     */   implements RestrictiveFilter
/*     */ {
/*     */   private final AttributeFilterSetting setting;
/*     */ 
/*     */   /** @deprecated */
/*     */   public AttributeFilter(Dimension dimension)
/*     */   {
/*  76 */     this(dimension.getDefaultHierarchy(), new AttributeFilterSetting());
/*     */   }
/*     */ 
/*     */   public AttributeFilter(Hierarchy hierarchy)
/*     */   {
/*  85 */     this(hierarchy, new AttributeFilterSetting());
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public AttributeFilter(Dimension dimension, AttributeFilterSetting setting)
/*     */   {
/*  96 */     super(dimension.getDefaultHierarchy());
/*  97 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public AttributeFilter(Hierarchy hierarchy, AttributeFilterSetting setting)
/*     */   {
/* 107 */     super(hierarchy);
/* 108 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public final AttributeFilter copy() {
/* 112 */     AttributeFilter copy = new AttributeFilter(this.hierarchy);
/* 113 */     copy.getSettings().adapt(this.setting);
/* 114 */     return copy;
/*     */   }
/*     */ 
/*     */   public final AttributeFilterSetting getSettings() {
/* 118 */     return this.setting;
/*     */   }
/*     */ 
/*     */   public final void filter(Set<Element> elements)
/*     */   {
/* 123 */     AttributeConstraintsMatrix filterMatrix = 
/* 124 */       (AttributeConstraintsMatrix)this.setting.getFilterConstraints().getValue();
/* 125 */     if (filterMatrix.getRowsCount() == 0)
/* 126 */       return;
/* 127 */     List newElements = new ArrayList();
/* 128 */     for (Element element : elements) {
/* 129 */       if (accept(element, filterMatrix)) {
/* 130 */         newElements.add(element);
/*     */       }
/*     */     }
/* 133 */     elements.clear();
/* 134 */     elements.addAll(newElements);
/*     */   }
/*     */ 
/*     */   public final int getType()
/*     */   {
/* 144 */     return 32;
/*     */   }
/*     */ 
/*     */   public final void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void validateSettings() throws PaloIOException
/*     */   {
/* 153 */     if (!getSettings().hasFilterConsraints())
/* 154 */       throw new PaloIOException(
/* 155 */         "AttributeFilter: at least one attribute constraint must be added!");
/*     */   }
/*     */ 
/*     */   private final boolean accept(Element element, AttributeConstraintsMatrix filterMatrix)
/*     */   {
/* 165 */     int i = 0; for (int n = filterMatrix.getRowsCount(); i < n; ++i) {
/* 166 */       AttributeConstraint[] row = filterMatrix.getRow(i);
/* 167 */       if ((row != null) && (rowFulfilled(row, element)))
/* 168 */         return true;
/*     */     }
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   private final boolean rowFulfilled(AttributeConstraint[] row, Element element)
/*     */   {
/* 175 */     for (AttributeConstraint constraint : row) {
/* 176 */       Attribute attribute = 
/* 177 */         this.hierarchy.getAttribute(constraint.getAttributeId());
/* 178 */       if (attribute != null) {
/* 179 */         String attrValue = element.getAttributeValue(attribute)
/* 180 */           .toString();
/* 181 */         if (!constraint.accept(attrValue, attribute.getType())) {
/* 182 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 186 */     return true;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.AttributeFilter
 * JD-Core Version:    0.5.4
 */