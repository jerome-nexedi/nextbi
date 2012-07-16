/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.filter.settings.BooleanParameter;
/*     */ import org.palo.api.subsets.filter.settings.HierarchicalFilterSetting;
/*     */ import org.palo.api.subsets.filter.settings.IntegerParameter;
/*     */ import org.palo.api.subsets.filter.settings.StringParameter;
/*     */ 
/*     */ public class HierarchicalFilter extends AbstractSubsetFilter
/*     */   implements RestrictiveFilter, StructuralFilter
/*     */ {
/*     */   private final HierarchicalFilterSetting setting;
/*     */ 
/*     */   /** @deprecated */
/*     */   public HierarchicalFilter(Dimension dimension)
/*     */   {
/*  73 */     this(dimension.getDefaultHierarchy(), new HierarchicalFilterSetting());
/*     */   }
/*     */ 
/*     */   public HierarchicalFilter(Hierarchy hierarchy) {
/*  77 */     this(hierarchy, new HierarchicalFilterSetting());
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public HierarchicalFilter(Dimension dimension, HierarchicalFilterSetting setting)
/*     */   {
/*  84 */     super(dimension.getDefaultHierarchy());
/*  85 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public HierarchicalFilter(Hierarchy hierarchy, HierarchicalFilterSetting setting) {
/*  89 */     super(hierarchy);
/*  90 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public final HierarchicalFilter copy() {
/*  94 */     HierarchicalFilter copy = new HierarchicalFilter(this.hierarchy);
/*  95 */     copy.getSettings().adapt(this.setting);
/*  96 */     return copy;
/*     */   }
/*     */ 
/*     */   public final HierarchicalFilterSetting getSettings() {
/* 100 */     return this.setting;
/*     */   }
/*     */ 
/*     */   public final Element getReferenceElement() {
/* 104 */     return this.hierarchy.getElementById(this.setting.getRefElement().getValue());
/*     */   }
/*     */ 
/*     */   public void filter(Set<Element> elements) {
/* 108 */     IndentComparator indentFilter = new IndentComparator(getSubset());
/* 109 */     Iterator allElements = elements.iterator();
/* 110 */     while (allElements.hasNext())
/* 111 */       if (!accept((Element)allElements.next(), indentFilter))
/* 112 */         allElements.remove();
/*     */   }
/*     */ 
/*     */   public final void filter(List<ElementNode> hier, Set<Element> elements)
/*     */   {
/* 118 */     if ((!this.setting.doRevolve()) || (hier.isEmpty())) {
/* 119 */       return;
/*     */     }
/*     */ 
/* 122 */     String revId = this.setting.getRevolveElement().getValue();
/* 123 */     Element revolveElement = this.hierarchy.getElementById(revId);
/* 124 */     final int revolveLevel = 
/* 125 */       (revolveElement != null) ? revolveElement.getLevel() : 0;
/* 126 */     final int revolveMode = this.setting.getRevolveMode().getValue().intValue();
/* 127 */     final int revolveCount = this.setting.getRevolveCount().getValue().intValue();
/*     */ 
/* 129 */     final int[] counter = new int[1];
/* 130 */     final Set revolvedElements = new HashSet();
/* 131 */     final ArrayList newHierarchy = new ArrayList();
/* 132 */     ElementNodeVisitor2 visitor = new ElementNodeVisitor2() {
/*     */       public ElementNode visit(ElementNode node, ElementNode parent) {
/* 134 */         if (counter[0] < revolveCount) {
/* 135 */           boolean addIt = true;
/* 136 */           int nodeLevel = node.getElement().getLevel();
/* 137 */           switch (revolveMode)
/*     */           {
/*     */           case 2:
/* 139 */             if (nodeLevel < revolveLevel)
/* 140 */               addIt = false;
/* 141 */             break;
/*     */           case 1:
/* 143 */             if (nodeLevel > revolveLevel)
/* 144 */               addIt = false;
/* 145 */             break;
/*     */           case 0:
/* 147 */             if (nodeLevel != revolveLevel) {
/* 148 */               addIt = false;
/*     */             }
/*     */           }
/* 151 */           if (addIt) {
/* 152 */             ElementNode newNode = new ElementNode(node.getElement());
/* 153 */             revolvedElements.add(node.getElement());
/* 154 */             newNode.setParent(parent);
/* 155 */             if (parent != null)
/* 156 */               parent.forceAddChild(newNode);
/*     */             else {
/* 158 */               newHierarchy.add(newNode);
/*     */             }
/* 160 */             counter[0] += 1;
/* 161 */             return newNode;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 173 */         return null;
/*     */       }
/*     */     };
/* 176 */     counter[0] = 0;
/*     */     do {
/* 178 */       for (ElementNode node : hier)
/* 179 */         traverse(node, null, visitor);
/*     */     }
/* 181 */     while ((counter[0] < revolveCount) && (counter[0] > 0));
/* 182 */     elements.retainAll(revolvedElements);
/* 183 */     revolvedElements.clear();
/* 184 */     hier.clear();
/* 185 */     hier.addAll(newHierarchy);
/*     */   }
/*     */ 
/*     */   public final int getType()
/*     */   {
/* 190 */     return 2;
/*     */   }
/*     */ 
/*     */   public final void initialize()
/*     */   {
/* 195 */     this.setting.reset();
/*     */   }
/*     */ 
/*     */   public final void validateSettings()
/*     */     throws PaloIOException
/*     */   {
/*     */   }
/*     */ 
/*     */   private final boolean accept(Element element, IndentComparator indentFilter)
/*     */   {
/* 209 */     if (this.setting.doAboveBelowSelection())
/*     */     {
/* 211 */       String refElId = this.setting.getRefElement().getValue();
/* 212 */       if (element.getId().equals(refElId)) {
/* 213 */         if (this.setting.getExclusive().getValue().booleanValue())
/* 214 */           return false;
/*     */       } else {
/* 216 */         Element refElement = this.hierarchy.getElementById(refElId);
/* 217 */         if (this.setting.getAbove().getValue().booleanValue()) {
/* 218 */           if (!isParent(refElement, element))
/* 219 */             return false;
/*     */         }
/* 221 */         else if (!isChild(refElement, element)) {
/* 222 */           return false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 240 */     int hideMode = this.setting.getHideMode().getValue().intValue();
/* 241 */     if (hideMode != 0) {
/* 242 */       boolean isLeaf = element.getChildCount() <= 0;
/* 243 */       if ((hideMode == 1) && (isLeaf))
/* 244 */         return false;
/* 245 */       if ((hideMode == 2) && 
/* 246 */         (!isLeaf)) {
/* 247 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 254 */     return (!this.setting.doLevelSelection()) || 
/* 253 */       (isInLevelSelectionRegion(element, indentFilter));
/*     */   }
/*     */ 
/*     */   private final boolean isParent(Element element, Element parent)
/*     */   {
/* 261 */     Element[] parents = element.getParents();
/* 262 */     for (Element _parent : parents) {
/* 263 */       if (_parent.equals(parent))
/* 264 */         return true;
/*     */     }
/* 266 */     for (Element _parent : parents) {
/* 267 */       if (isParent(_parent, parent))
/* 268 */         return true;
/*     */     }
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   private final boolean isChild(Element element, Element child) {
/* 274 */     if ((element == null) || (child == null))
/* 275 */       return false;
/* 276 */     Element[] children = element.getChildren();
/* 277 */     for (Element _child : children) {
/* 278 */       if (_child.equals(child))
/* 279 */         return true;
/*     */     }
/* 281 */     for (Element _child : children) {
/* 282 */       if (isChild(_child, child))
/* 283 */         return true;
/*     */     }
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   private final boolean isInLevelSelectionRegion(Element element, IndentComparator indentFilter)
/*     */   {
/* 290 */     int endLevel = this.setting.getEndLevel().getValue().intValue();
/* 291 */     int startLevel = this.setting.getStartLevel().getValue().intValue();
/* 292 */     startLevel = (startLevel > -1) ? startLevel : 0;
/* 293 */     endLevel = (endLevel > -1) ? endLevel : 2147483647;
/*     */ 
/* 295 */     if (startLevel > endLevel) {
/* 296 */       int tmp = endLevel;
/* 297 */       endLevel = startLevel;
/* 298 */       startLevel = tmp;
/*     */     }
/*     */ 
/* 301 */     return (indentFilter.compare(element, startLevel) >= 0) && 
/* 301 */       (indentFilter.compare(element, endLevel) <= 0);
/*     */   }
/*     */ 
/*     */   private final void traverse(ElementNode node, ElementNode parent, ElementNodeVisitor2 visitor)
/*     */   {
/* 306 */     ElementNode newNode = visitor.visit(node, parent);
/* 307 */     ElementNode[] children = node.getChildren();
/* 308 */     if (children == null)
/* 309 */       return;
/* 310 */     for (ElementNode child : children)
/* 311 */       traverse(child, newNode, visitor);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.HierarchicalFilter
 * JD-Core Version:    0.5.4
 */