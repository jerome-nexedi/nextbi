/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.filter.settings.IntegerParameter;
/*     */ import org.palo.api.subsets.filter.settings.ObjectParameter;
/*     */ import org.palo.api.subsets.filter.settings.PicklistFilterSetting;
/*     */ 
/*     */ public class PicklistFilter extends AbstractSubsetFilter
/*     */   implements RestrictiveFilter, StructuralFilter
/*     */ {
/*     */   private final PicklistFilterSetting setting;
/*     */ 
/*     */   /** @deprecated */
/*     */   public PicklistFilter(Dimension dimension)
/*     */   {
/*  74 */     this(dimension, new PicklistFilterSetting());
/*     */   }
/*     */ 
/*     */   public PicklistFilter(Hierarchy hierarchy)
/*     */   {
/*  83 */     this(hierarchy, new PicklistFilterSetting());
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public PicklistFilter(Dimension dimension, PicklistFilterSetting setting)
/*     */   {
/*  94 */     super(dimension);
/*  95 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public PicklistFilter(Hierarchy hierarchy, PicklistFilterSetting setting)
/*     */   {
/* 105 */     super(hierarchy);
/* 106 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public final PicklistFilter copy() {
/* 110 */     PicklistFilter copy = new PicklistFilter(this.hierarchy);
/* 111 */     copy.getSettings().adapt(this.setting);
/* 112 */     return copy;
/*     */   }
/*     */ 
/*     */   public final PicklistFilterSetting getSettings() {
/* 116 */     return this.setting;
/*     */   }
/*     */ 
/*     */   public final void filter(List<ElementNode> hiers, Set<Element> elements)
/*     */   {
/* 127 */     int insertMode = this.setting.getInsertMode().getValue().intValue();
/* 128 */     HashSet<String> pickedElements = 
/* 129 */       (HashSet)this.setting.getSelection().getValue();
/*     */     Element element;
/* 130 */     if (insertMode == 1) {
/* 131 */       for (String id : pickedElements) {
/* 132 */         element = this.hierarchy.getElementById(id);
/* 133 */         if (element != null) {
/* 134 */           hiers.add(new ElementNode(element));
/* 135 */           elements.add(element);
/*     */         }
/*     */       }
/* 138 */     } else if (insertMode == 0) {
/* 139 */       int index = 0;
/* 140 */       for (String id : pickedElements) {
/* 141 */         element = this.hierarchy.getElementById(id);
/* 142 */         if (element != null) {
/* 143 */           hiers.add(index++, new ElementNode(element));
/* 144 */           elements.add(element);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void filter(Set<Element> elements)
/*     */   {
/* 157 */     IntegerParameter modeParam = this.setting.getInsertMode();
/* 158 */     if (modeParam.getValue().intValue() != 3)
/*     */       return;
/* 160 */     HashSet pickedElements = 
/* 161 */       (HashSet)this.setting.getSelection().getValue();
/* 162 */     Iterator allElements = elements.iterator();
/* 163 */     while (allElements.hasNext()) {
/* 164 */       Element element = (Element)allElements.next();
/* 165 */       if (!pickedElements.contains(element.getId()))
/* 166 */         allElements.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void merge(Set<Element> elements)
/*     */   {
/* 178 */     IntegerParameter modeParam = this.setting.getInsertMode();
/* 179 */     if (modeParam.getValue().intValue() == 2) {
/* 180 */       HashSet<String> pickedElements = 
/* 181 */         (HashSet)this.setting.getSelection().getValue();
/* 182 */       for (String id : pickedElements) {
/* 183 */         Element element = this.hierarchy.getElementById(id);
/* 184 */         if (element != null)
/* 185 */           elements.add(element);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getType() {
/* 191 */     return 4;
/*     */   }
/*     */ 
/*     */   public final void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void resetInternal()
/*     */   {
/* 200 */     this.setting.reset();
/*     */   }
/*     */ 
/*     */   public final void validateSettings()
/*     */     throws PaloIOException
/*     */   {
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.PicklistFilter
 * JD-Core Version:    0.5.4
 */