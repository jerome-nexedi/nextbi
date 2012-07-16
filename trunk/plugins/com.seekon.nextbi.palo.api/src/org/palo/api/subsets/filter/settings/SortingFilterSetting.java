/*     */ package org.palo.api.subsets.filter.settings;
/*     */ 
/*     */ import org.palo.api.subsets.Subset2;
/*     */ 
/*     */ public class SortingFilterSetting extends AbstractFilterSettings
/*     */ {
/*     */   public static final int HIERARCHICAL_MODE_DISABLED = 0;
/*     */   public static final int HIERARCHICAL_MODE_SHOW_CHILDREN = 1;
/*     */   public static final int HIERARCHICAL_MODE_HIDE_CHILDREN = 2;
/*     */   public static final int SORT_TYPE_DISABLED = 0;
/*     */   public static final int SORT_TYPE_LEAFS_ONLY = 1;
/*     */   public static final int SORT_TYPE_CONSOLIDATIONS_ONLY = 2;
/*     */   public static final int ORDER_MODE_REVERSE_DISABLED = 0;
/*     */   public static final int ORDER_MODE_REVERSE_TOTAL = 1;
/*     */   public static final int ORDER_MODE_REVERSE_PER_LEVEL = 2;
/*     */   public static final int SORT_CRITERIA_DEFINITION = 0;
/*     */   public static final int SORT_CRITERIA_DATA = 1;
/*     */   public static final int SORT_CRITERIA_LEXICAL = 2;
/*     */   public static final int SORT_CRITERIA_ALIAS = 3;
/*     */   private IntegerParameter sortCriteria;
/*     */   private IntegerParameter orderMode;
/*     */   private IntegerParameter sortTypeMode;
/*     */   private IntegerParameter hierarchicalMode;
/*     */   private IntegerParameter sortLevel;
/*     */   private IntegerParameter showDuplicates;
/*     */ 
/*     */   /** @deprecated */
/*     */   private StringParameter sortLevelElementId;
/*     */   private StringParameter sortAttribute;
/*     */ 
/*     */   public SortingFilterSetting()
/*     */   {
/*  86 */     this.sortCriteria = new IntegerParameter();
/*  87 */     this.orderMode = new IntegerParameter();
/*  88 */     this.sortTypeMode = new IntegerParameter();
/*  89 */     this.hierarchicalMode = new IntegerParameter();
/*  90 */     this.sortLevelElementId = new StringParameter();
/*  91 */     this.sortAttribute = new StringParameter();
/*  92 */     this.showDuplicates = new IntegerParameter();
/*  93 */     this.sortLevel = new IntegerParameter();
/*     */ 
/*  96 */     reset();
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getShowDuplicates()
/*     */   {
/* 107 */     return this.showDuplicates;
/*     */   }
/*     */ 
/*     */   public final void setShowDuplicates(int showDuplicates)
/*     */   {
/* 116 */     this.showDuplicates.setValue(showDuplicates);
/*     */   }
/*     */ 
/*     */   public final void setShowDuplicates(IntegerParameter showDuplicates)
/*     */   {
/* 125 */     this.showDuplicates = showDuplicates;
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getSortCriteria()
/*     */   {
/* 134 */     return this.sortCriteria;
/*     */   }
/*     */ 
/*     */   public final void setSortCriteria(int sortCriteria)
/*     */   {
/* 143 */     this.sortCriteria.setValue(sortCriteria);
/*     */   }
/*     */ 
/*     */   public final void setSortCriteria(IntegerParameter sortCriteria)
/*     */   {
/* 152 */     this.sortCriteria = sortCriteria;
/* 153 */     this.sortCriteria.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final boolean doReverseOrder()
/*     */   {
/* 163 */     return this.orderMode.getValue().intValue() != 0;
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getOrderMode()
/*     */   {
/* 171 */     return this.orderMode;
/*     */   }
/*     */ 
/*     */   public final void setOrderMode(int orderMode)
/*     */   {
/* 179 */     this.orderMode.setValue(orderMode);
/*     */   }
/*     */ 
/*     */   public final void setOrderMode(IntegerParameter orderMode)
/*     */   {
/* 188 */     this.orderMode = orderMode;
/* 189 */     this.orderMode.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final boolean doSortByType()
/*     */   {
/* 199 */     return this.sortTypeMode.getValue().intValue() != 0;
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getSortTypeMode()
/*     */   {
/* 208 */     return this.sortTypeMode;
/*     */   }
/*     */ 
/*     */   public final void setSortTypeMode(int sortTypeMode)
/*     */   {
/* 216 */     this.sortTypeMode.setValue(sortTypeMode);
/*     */   }
/*     */ 
/*     */   public final void setSortTypeMode(IntegerParameter sortTypeMode)
/*     */   {
/* 225 */     this.sortTypeMode = sortTypeMode;
/* 226 */     this.sortTypeMode.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final boolean doHierarchy()
/*     */   {
/* 235 */     return this.hierarchicalMode.getValue().intValue() != 0;
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getHierarchicalMode()
/*     */   {
/* 244 */     return this.hierarchicalMode;
/*     */   }
/*     */ 
/*     */   public final void setHierarchicalMode(int hierarchicalMode)
/*     */   {
/* 252 */     this.hierarchicalMode.setValue(hierarchicalMode);
/*     */   }
/*     */ 
/*     */   public final void setHierarchicalMode(IntegerParameter hierarchicalMode)
/*     */   {
/* 261 */     this.hierarchicalMode = hierarchicalMode;
/* 262 */     this.hierarchicalMode.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final boolean doSortPerLevel()
/*     */   {
/* 272 */     return (this.sortLevelElementId.getValue() != null) || (this.sortLevel.getValue().intValue() > -1);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getSortLevel()
/*     */   {
/* 280 */     return this.sortLevel;
/*     */   }
/*     */ 
/*     */   public final void setSortLevel(int level)
/*     */   {
/* 288 */     this.sortLevel.setValue(level);
/*     */   }
/*     */ 
/*     */   public final void setSortLevel(IntegerParameter sortLevel)
/*     */   {
/* 296 */     this.sortLevel.unbind();
/* 297 */     this.sortLevel = sortLevel;
/* 298 */     this.sortLevel.bind(this.subset);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final StringParameter getSortLevelElement()
/*     */   {
/* 308 */     return this.sortLevelElementId;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setSortLevelElement(String sortLevelElementId)
/*     */   {
/* 316 */     this.sortLevelElementId.setValue(sortLevelElementId);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setSortLevelElement(StringParameter sortLevelElement)
/*     */   {
/* 326 */     this.sortLevelElementId = sortLevelElement;
/* 327 */     sortLevelElement.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final boolean doSortByAttribute()
/*     */   {
/* 336 */     return this.sortAttribute.getValue() != null;
/*     */   }
/*     */ 
/*     */   public final StringParameter getSortAttribute()
/*     */   {
/* 343 */     return this.sortAttribute;
/*     */   }
/*     */ 
/*     */   public final void setSortAttribute(String attributeId)
/*     */   {
/* 350 */     this.sortAttribute.setValue(attributeId);
/*     */   }
/*     */ 
/*     */   public final void setSortAttribute(StringParameter sortAttribute)
/*     */   {
/* 359 */     this.sortAttribute = sortAttribute;
/* 360 */     sortAttribute.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 366 */     this.sortCriteria.setValue(0);
/* 367 */     this.orderMode.setValue(0);
/* 368 */     this.sortTypeMode.setValue(0);
/* 369 */     this.hierarchicalMode.setValue(0);
/* 370 */     this.sortLevelElementId.setValue(null);
/* 371 */     this.sortAttribute.setValue(null);
/* 372 */     this.showDuplicates.setValue(1);
/* 373 */     this.sortLevel.setValue(-1);
/*     */   }
/*     */ 
/*     */   public final void bind(Subset2 subset) {
/* 377 */     super.bind(subset);
/*     */ 
/* 379 */     this.sortCriteria.bind(subset);
/* 380 */     this.orderMode.bind(subset);
/* 381 */     this.sortTypeMode.bind(subset);
/* 382 */     this.hierarchicalMode.bind(subset);
/* 383 */     this.sortLevelElementId.bind(subset);
/* 384 */     this.sortAttribute.bind(subset);
/* 385 */     this.showDuplicates.bind(subset);
/* 386 */     this.sortLevel.bind(subset);
/*     */   }
/*     */   public final void unbind() {
/* 389 */     super.unbind();
/*     */ 
/* 391 */     this.sortCriteria.unbind();
/* 392 */     this.orderMode.unbind();
/* 393 */     this.sortTypeMode.unbind();
/* 394 */     this.hierarchicalMode.unbind();
/* 395 */     this.sortLevelElementId.unbind();
/* 396 */     this.sortAttribute.unbind();
/* 397 */     this.showDuplicates.unbind();
/*     */   }
/*     */ 
/*     */   public final void adapt(FilterSetting from) {
/* 401 */     if (!(from instanceof SortingFilterSetting))
/* 402 */       return;
/* 403 */     SortingFilterSetting setting = (SortingFilterSetting)from;
/* 404 */     reset();
/* 405 */     setSortCriteria(setting.getSortCriteria().getValue().intValue());
/* 406 */     setOrderMode(setting.getOrderMode().getValue().intValue());
/* 407 */     setSortTypeMode(setting.getSortTypeMode().getValue().intValue());
/* 408 */     setHierarchicalMode(setting.getHierarchicalMode().getValue().intValue());
/* 409 */     setSortLevelElement(setting.getSortLevelElement().getValue());
/* 410 */     setSortLevel(setting.getSortLevel().getValue().intValue());
/* 411 */     setSortAttribute(setting.getSortAttribute().getValue());
/* 412 */     setShowDuplicates(setting.getShowDuplicates().getValue().intValue());
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.SortingFilterSetting
 * JD-Core Version:    0.5.4
 */