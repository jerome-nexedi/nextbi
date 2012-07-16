/*     */ package org.palo.api.subsets.filter;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.palo.api.Attribute;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.subsets.filter.settings.AliasFilterSetting;
/*     */ import org.palo.api.subsets.filter.settings.StringParameter;
/*     */ 
/*     */ public class AliasFilter extends AbstractSubsetFilter
/*     */   implements EffectiveFilter
/*     */ {
/*  63 */   private final int[] effectiveTypes = { 
/*  64 */     1, 16 };
/*     */   private final AliasFilterSetting setting;
/*     */ 
/*     */   /** @deprecated */
/*     */   public AliasFilter(Dimension dimension)
/*     */   {
/*  76 */     this(dimension.getDefaultHierarchy(), new AliasFilterSetting());
/*     */   }
/*     */ 
/*     */   public AliasFilter(Hierarchy hierarchy)
/*     */   {
/*  85 */     this(hierarchy, new AliasFilterSetting());
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public AliasFilter(Dimension dimension, AliasFilterSetting setting)
/*     */   {
/*  96 */     super(dimension.getDefaultHierarchy());
/*  97 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public AliasFilter(Hierarchy hierarchy, AliasFilterSetting setting)
/*     */   {
/* 108 */     super(hierarchy);
/* 109 */     this.setting = setting;
/*     */   }
/*     */ 
/*     */   public final AliasFilter copy()
/*     */   {
/* 114 */     AliasFilter copy = new AliasFilter(this.hierarchy);
/* 115 */     copy.getSettings().adapt(this.setting);
/* 116 */     return copy;
/*     */   }
/*     */ 
/*     */   public final int[] getEffectiveFilter() {
/* 120 */     return this.effectiveTypes;
/*     */   }
/*     */ 
/*     */   public final String getAlias(Element element)
/*     */   {
/* 130 */     String alias = getAlias(element, 2);
/*     */ 
/* 132 */     if (!alias.equals("")) {
/* 133 */       return alias;
/*     */     }
/* 135 */     alias = getAlias(element, 1);
/* 136 */     if (!alias.equals("")) {
/* 137 */       return alias;
/*     */     }
/* 139 */     return element.getName();
/*     */   }
/*     */ 
/*     */   public final int getType() {
/* 143 */     return 64;
/*     */   }
/*     */   public final void initialize() {
/*     */   }
/*     */ 
/*     */   public final AliasFilterSetting getSettings() {
/* 149 */     return this.setting;
/*     */   }
/*     */ 
/*     */   public void filter(List<ElementNode> elements)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void validateSettings()
/*     */     throws PaloIOException
/*     */   {
/*     */   }
/*     */ 
/*     */   private final String getAlias(Element element, int aliasNr)
/*     */   {
/* 169 */     Attribute alias = getAlias(this.setting.getAlias(aliasNr).getValue());
/* 170 */     if (alias != null) {
/* 171 */       Object value = alias.getValue(element);
/* 172 */       if (value != null)
/* 173 */         return value.toString();
/*     */     }
/* 175 */     return "";
/*     */   }
/*     */   private final Attribute getAlias(String attrId) {
/* 178 */     if (attrId != null) {
/* 179 */       Attribute[] aliases = this.hierarchy.getAttributes();
/* 180 */       for (Attribute selAlias : aliases) {
/* 181 */         if (selAlias.getId().equals(attrId))
/* 182 */           return selAlias;
/*     */       }
/*     */     }
/* 185 */     return null;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.AliasFilter
 * JD-Core Version:    0.5.4
 */