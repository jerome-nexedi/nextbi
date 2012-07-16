/*     */ package org.palo.api.subsets.filter.settings;
/*     */ 
/*     */ import org.palo.api.subsets.Subset2;
/*     */ 
/*     */ public class HierarchicalFilterSetting extends AbstractFilterSettings
/*     */ {
/*     */   public static final int HIDE_MODE_DISABLED = 0;
/*     */   public static final int HIDE_MODE_LEAFS = 1;
/*     */   public static final int HIDE_MODE_CONSOLIDATIONS = 2;
/*     */   public static final int REVOLVE_ADD_DISABLED = 0;
/*     */   public static final int REVOLVE_ADD_BELOW = 1;
/*     */   public static final int REVOLVE_ADD_ABOVE = 2;
/*     */   private StringParameter refElementId;
/*     */   private BooleanParameter exclusive;
/*     */   private BooleanParameter above;
/*     */   private IntegerParameter hideMode;
/*     */ 
/*     */   /** @deprecated */
/*     */   private StringParameter startElementId;
/*     */ 
/*     */   /** @deprecated */
/*     */   private StringParameter endElementId;
/*     */   private IntegerParameter startLevel;
/*     */   private IntegerParameter endLevel;
/*     */   private StringParameter revolveElementId;
/*     */   private IntegerParameter revolveElementsCount;
/*     */   private IntegerParameter revolveMode;
/*     */ 
/*     */   public HierarchicalFilterSetting()
/*     */   {
/*  84 */     this.refElementId = new StringParameter();
/*  85 */     this.exclusive = new BooleanParameter();
/*  86 */     this.above = new BooleanParameter();
/*     */ 
/*  89 */     this.hideMode = new IntegerParameter();
/*     */ 
/*  92 */     this.startElementId = new StringParameter();
/*  93 */     this.endElementId = new StringParameter();
/*  94 */     this.startLevel = new IntegerParameter();
/*  95 */     this.endLevel = new IntegerParameter();
/*     */ 
/*  98 */     this.revolveElementId = new StringParameter();
/*  99 */     this.revolveElementsCount = new IntegerParameter();
/* 100 */     this.revolveMode = new IntegerParameter();
/*     */ 
/* 103 */     reset();
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 108 */     this.refElementId.setValue(null);
/* 109 */     this.above.setValue(false);
/* 110 */     this.exclusive.setValue(false);
/*     */ 
/* 113 */     this.hideMode.setValue(0);
/*     */ 
/* 116 */     this.startLevel.setValue(-1);
/* 117 */     this.endLevel.setValue(-1);
/* 118 */     this.startElementId.setValue(null);
/* 119 */     this.endElementId.setValue(null);
/* 120 */     this.refElementId.setValue(null);
/*     */ 
/* 123 */     this.revolveElementId.setValue(null);
/* 124 */     this.revolveElementsCount.setValue(0);
/* 125 */     this.revolveMode.setValue(0);
/*     */   }
/*     */ 
/*     */   public final boolean doAboveBelowSelection()
/*     */   {
/* 134 */     return this.refElementId.getValue() != null;
/*     */   }
/*     */ 
/*     */   public final boolean doLevelSelection()
/*     */   {
/* 145 */     return (this.startElementId.getValue() != null) || 
/* 143 */       (this.endElementId.getValue() != null) || 
/* 144 */       (this.startLevel.getValue().intValue() > -1) || 
/* 145 */       (this.endLevel.getValue().intValue() > -1);
/*     */   }
/*     */ 
/*     */   public final boolean doHide()
/*     */   {
/* 153 */     return this.hideMode.getValue().intValue() != 0;
/*     */   }
/*     */ 
/*     */   public final boolean doRevolve()
/*     */   {
/* 161 */     return this.revolveElementId.getValue() != null;
/*     */   }
/*     */ 
/*     */   public final StringParameter getRefElement()
/*     */   {
/* 169 */     return this.refElementId;
/*     */   }
/*     */ 
/*     */   public final void setRefElement(String elementId)
/*     */   {
/* 176 */     this.refElementId.setValue(elementId);
/*     */   }
/*     */ 
/*     */   public final void setRefElement(StringParameter element)
/*     */   {
/* 185 */     this.refElementId = element;
/* 186 */     this.refElementId.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final BooleanParameter getAbove()
/*     */   {
/* 196 */     return this.above;
/*     */   }
/*     */ 
/*     */   public final void setAbove(boolean above)
/*     */   {
/* 205 */     this.above.setValue(above);
/*     */   }
/*     */ 
/*     */   public final void setAbove(BooleanParameter above)
/*     */   {
/* 213 */     this.above = above;
/* 214 */     this.above.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final BooleanParameter getExclusive()
/*     */   {
/* 224 */     return this.exclusive;
/*     */   }
/*     */ 
/*     */   public final void setExclusive(boolean exclusive)
/*     */   {
/* 233 */     this.exclusive.setValue(exclusive);
/*     */   }
/*     */ 
/*     */   public final void setExclusive(BooleanParameter exclusive)
/*     */   {
/* 241 */     this.exclusive = exclusive;
/* 242 */     this.exclusive.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getHideMode()
/*     */   {
/* 251 */     return this.hideMode;
/*     */   }
/*     */ 
/*     */   public final void setHideMode(int hideMode)
/*     */   {
/* 259 */     this.hideMode.setValue(hideMode);
/*     */   }
/*     */ 
/*     */   public final void setHideMode(IntegerParameter hideMode)
/*     */   {
/* 268 */     this.hideMode = hideMode;
/* 269 */     this.hideMode.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getStartLevel()
/*     */   {
/* 277 */     return this.startLevel;
/*     */   }
/*     */ 
/*     */   public final void setStartLevel(int level)
/*     */   {
/* 285 */     this.startLevel.setValue(level);
/*     */   }
/*     */ 
/*     */   public final void setStartLevel(IntegerParameter level)
/*     */   {
/* 293 */     this.startLevel.unbind();
/* 294 */     this.startLevel = level;
/* 295 */     this.startLevel.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getEndLevel()
/*     */   {
/* 303 */     return this.endLevel;
/*     */   }
/*     */ 
/*     */   public final void setEndLevel(int level)
/*     */   {
/* 311 */     this.endLevel.setValue(level);
/*     */   }
/*     */ 
/*     */   public final void setEndLevel(IntegerParameter level)
/*     */   {
/* 318 */     this.endLevel.unbind();
/* 319 */     this.endLevel = level;
/* 320 */     this.endLevel.bind(this.subset);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final StringParameter getStartElement()
/*     */   {
/* 329 */     return this.startElementId;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setStartElement(String elementId)
/*     */   {
/* 338 */     this.startElementId.setValue(elementId);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setStartElement(StringParameter element)
/*     */   {
/* 348 */     this.startElementId = element;
/* 349 */     this.startElementId.bind(this.subset);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final StringParameter getEndElement()
/*     */   {
/* 358 */     return this.endElementId;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setEndElement(String elementId)
/*     */   {
/* 366 */     this.endElementId.setValue(elementId);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public final void setEndElement(StringParameter element)
/*     */   {
/* 376 */     this.endElementId = element;
/* 377 */     this.endElementId.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final StringParameter getRevolveElement()
/*     */   {
/* 386 */     return this.revolveElementId;
/*     */   }
/*     */ 
/*     */   public final void setRevolveElement(String elementId)
/*     */   {
/* 393 */     this.revolveElementId.setValue(elementId);
/*     */   }
/*     */ 
/*     */   public final void setRevolveElement(StringParameter elementId)
/*     */   {
/* 402 */     this.revolveElementId = elementId;
/* 403 */     this.revolveElementId.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getRevolveCount()
/*     */   {
/* 411 */     return this.revolveElementsCount;
/*     */   }
/*     */ 
/*     */   public final void setRevolveCount(int revolveElementsCount)
/*     */   {
/* 418 */     this.revolveElementsCount.setValue(revolveElementsCount);
/*     */   }
/*     */ 
/*     */   public final void setRevolveCount(IntegerParameter revolveElementsCount)
/*     */   {
/* 427 */     this.revolveElementsCount = revolveElementsCount;
/* 428 */     this.revolveElementsCount.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getRevolveMode()
/*     */   {
/* 437 */     return this.revolveMode;
/*     */   }
/*     */ 
/*     */   public final void setRevolveMode(int revolveMode)
/*     */   {
/* 445 */     this.revolveMode.setValue(revolveMode);
/*     */   }
/*     */ 
/*     */   public final void setRevolveMode(IntegerParameter revolveMode)
/*     */   {
/* 454 */     this.revolveMode = revolveMode;
/* 455 */     this.revolveMode.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final void bind(Subset2 subset) {
/* 459 */     super.bind(subset);
/*     */ 
/* 461 */     this.refElementId.bind(subset);
/* 462 */     this.exclusive.bind(subset);
/* 463 */     this.above.bind(subset);
/* 464 */     this.hideMode.bind(subset);
/* 465 */     this.startElementId.bind(subset);
/* 466 */     this.endElementId.bind(subset);
/* 467 */     this.startLevel.bind(subset);
/* 468 */     this.endLevel.bind(subset);
/* 469 */     this.revolveElementId.bind(subset);
/* 470 */     this.revolveElementsCount.bind(subset);
/* 471 */     this.revolveMode.bind(subset);
/*     */   }
/*     */   public final void unbind() {
/* 474 */     super.unbind();
/*     */ 
/* 476 */     this.refElementId.unbind();
/* 477 */     this.exclusive.unbind();
/* 478 */     this.above.unbind();
/* 479 */     this.hideMode.unbind();
/* 480 */     this.startElementId.unbind();
/* 481 */     this.endElementId.unbind();
/* 482 */     this.revolveElementId.unbind();
/* 483 */     this.revolveElementsCount.unbind();
/* 484 */     this.revolveMode.unbind();
/*     */   }
/*     */ 
/*     */   public final void adapt(FilterSetting from) {
/* 488 */     if (!(from instanceof HierarchicalFilterSetting)) {
/* 489 */       return;
/*     */     }
/* 491 */     HierarchicalFilterSetting setting = (HierarchicalFilterSetting)from;
/* 492 */     reset();
/* 493 */     setRefElement(setting.getRefElement().getValue());
/* 494 */     setExclusive(setting.getExclusive().getValue().booleanValue());
/* 495 */     setAbove(setting.getAbove().getValue().booleanValue());
/*     */ 
/* 497 */     setHideMode(setting.getHideMode().getValue().intValue());
/*     */ 
/* 499 */     setStartElement(setting.getStartElement().getValue());
/* 500 */     setEndElement(setting.getEndElement().getValue());
/* 501 */     setEndLevel(setting.getEndLevel().getValue().intValue());
/* 502 */     setStartLevel(setting.getStartLevel().getValue().intValue());
/*     */ 
/* 504 */     setRevolveElement(setting.getRevolveElement().getValue());
/* 505 */     setRevolveCount(setting.getRevolveCount().getValue().intValue());
/* 506 */     setRevolveMode(setting.getRevolveMode().getValue().intValue());
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.HierarchicalFilterSetting
 * JD-Core Version:    0.5.4
 */