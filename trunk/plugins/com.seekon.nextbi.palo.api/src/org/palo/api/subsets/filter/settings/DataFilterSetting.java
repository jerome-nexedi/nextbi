/*     */ package org.palo.api.subsets.filter.settings;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ 
/*     */ public class DataFilterSetting extends AbstractFilterSettings
/*     */ {
/*     */   public static final int SUM_OP = 0;
/*     */   public static final int ALL_OP = 1;
/*     */   public static final int AVG_OP = 2;
/*     */   public static final int MAX_OP = 3;
/*     */   public static final int ANY_OP = 4;
/*     */   public static final int MIN_OP = 5;
/*     */   public static final int STR_OP = 7;
/*     */   private DataCriteria criteria;
/*     */   private IntegerParameter top;
/*     */   private IntegerParameter cellOperator;
/*     */   private StringParameter sourceCube;
/*     */   private DoubleParameter upperPercentage;
/*     */   private DoubleParameter lowerPercentage;
/*     */   private BooleanParameter useRules;
/*     */   private final ArrayList<ObjectParameter> slice;
/*     */ 
/*     */   public DataFilterSetting(String sourceCube)
/*     */   {
/*  88 */     this(sourceCube, new DataCriteria(">", ""));
/*     */   }
/*     */ 
/*     */   public DataFilterSetting(String sourceCube, DataCriteria criteria)
/*     */   {
/*  98 */     this.top = new IntegerParameter();
/*  99 */     this.cellOperator = new IntegerParameter();
/* 100 */     this.upperPercentage = new DoubleParameter();
/* 101 */     this.lowerPercentage = new DoubleParameter();
/* 102 */     this.useRules = new BooleanParameter();
/* 103 */     this.sourceCube = new StringParameter();
/* 104 */     this.sourceCube.setValue(sourceCube);
/* 105 */     this.slice = new ArrayList();
/*     */ 
/* 107 */     reset();
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getTop()
/*     */   {
/* 115 */     return this.top;
/*     */   }
/*     */ 
/*     */   public final void setTop(int top)
/*     */   {
/* 122 */     this.top.setValue(top);
/*     */   }
/*     */ 
/*     */   public final void setTop(IntegerParameter top)
/*     */   {
/* 131 */     this.top = top;
/* 132 */     this.top.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final IntegerParameter getCellOperator()
/*     */   {
/* 141 */     return this.cellOperator;
/*     */   }
/*     */ 
/*     */   public final void setCellOperator(int cellOperator)
/*     */   {
/* 149 */     this.cellOperator.setValue(cellOperator);
/*     */   }
/*     */ 
/*     */   public final void setCellOperator(IntegerParameter cellOperator)
/*     */   {
/* 158 */     this.cellOperator = cellOperator;
/* 159 */     this.cellOperator.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final void setCriteria(DataCriteria criteria)
/*     */   {
/* 167 */     if (criteria != null) {
/* 168 */       this.criteria = criteria;
/* 169 */       this.criteria.bind(this.subset);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final DataCriteria getCriteria()
/*     */   {
/* 190 */     return this.criteria;
/*     */   }
/*     */ 
/*     */   public final void setUseRules(boolean useRules)
/*     */   {
/* 199 */     this.useRules.setValue(useRules);
/*     */   }
/*     */ 
/*     */   public final void setUseRules(BooleanParameter useRules)
/*     */   {
/* 207 */     this.useRules = useRules;
/* 208 */     this.useRules.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final BooleanParameter getUseRules()
/*     */   {
/* 217 */     return this.useRules;
/*     */   }
/*     */ 
/*     */   public final StringParameter getSourceCube()
/*     */   {
/* 224 */     return this.sourceCube;
/*     */   }
/*     */ 
/*     */   public final void setSourceCube(String sourceCube)
/*     */   {
/* 231 */     this.sourceCube.setValue(sourceCube);
/*     */   }
/*     */ 
/*     */   public final void setSourceCube(StringParameter sourceCube)
/*     */   {
/* 240 */     this.sourceCube = sourceCube;
/* 241 */     this.sourceCube.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final DoubleParameter getUpperPercentage()
/*     */   {
/* 249 */     return this.upperPercentage;
/*     */   }
/*     */ 
/*     */   public final void setUpperPercentage(double upperPercentage)
/*     */   {
/* 256 */     this.upperPercentage.setValue(upperPercentage);
/*     */   }
/*     */ 
/*     */   public final void setUpperPercentage(DoubleParameter upperPercentage)
/*     */   {
/* 264 */     this.upperPercentage = upperPercentage;
/* 265 */     this.upperPercentage.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final DoubleParameter getLowerPercentage()
/*     */   {
/* 273 */     return this.lowerPercentage;
/*     */   }
/*     */ 
/*     */   public final void setLowerPercentage(double lowerPercentage)
/*     */   {
/* 280 */     this.lowerPercentage.setValue(lowerPercentage);
/*     */   }
/*     */   public final void setLowerPercentage(DoubleParameter lowerPercentage) {
/* 283 */     this.lowerPercentage = lowerPercentage;
/* 284 */     this.lowerPercentage.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final ObjectParameter[] getSliceParameters()
/*     */   {
/* 297 */     return (ObjectParameter[])this.slice.toArray(new ObjectParameter[0]);
/*     */   }
/*     */ 
/*     */   public final String[][] getSlice()
/*     */   {
/* 307 */     ObjectParameter[] params = getSliceParameters();
/* 308 */     String[][] slice = new String[params.length][];
/* 309 */     for (int i = 0; i < slice.length; ++i) {
/* 310 */       String[] elIds = (String[])params[i].getValue();
/* 311 */       slice[i] = ((String[])elIds.clone());
/*     */     }
/* 313 */     return slice;
/*     */   }
/*     */ 
/*     */   public final void addSliceElement(String id, int index) {
/* 317 */     ObjectParameter dimSlice = getDimensionSlice(index);
/* 318 */     String[] ids = (String[])dimSlice.getValue();
/* 319 */     if (ids == null) {
/* 320 */       dimSlice.setValue(new String[] { id });
/*     */     } else {
/* 322 */       String[] newIds = new String[ids.length + 1];
/* 323 */       System.arraycopy(ids, 0, newIds, 0, ids.length);
/* 324 */       newIds[ids.length] = id;
/* 325 */       dimSlice.setValue(newIds);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void setSliceElements(String[] ids, int index) {
/* 330 */     ObjectParameter dimSlice = getDimensionSlice(index);
/* 331 */     dimSlice.setValue(ids.clone());
/*     */   }
/*     */ 
/*     */   public final ObjectParameter getSlicePart(int index)
/*     */   {
/* 341 */     return (ObjectParameter)this.slice.get(index);
/*     */   }
/*     */ 
/*     */   public final void clearSlice() {
/* 345 */     this.slice.clear();
/* 346 */     markDirty();
/*     */   }
/*     */ 
/*     */   public final void addSlicePart(ObjectParameter part)
/*     */   {
/* 352 */     this.slice.add(part);
/* 353 */     part.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final ObjectParameter getSlicePart() {
/* 357 */     return (ObjectParameter)this.slice.get(this.slice.size() - 1);
/*     */   }
/*     */ 
/*     */   public final void addSliceElement(String id) {
/* 361 */     addSliceElement(id, this.slice.size() - 1);
/*     */   }
/*     */ 
/*     */   final void setSlice(ObjectParameter[] slice) {
/* 365 */     clearSlice();
/* 366 */     for (ObjectParameter param : slice) {
/* 367 */       this.slice.add(param);
/* 368 */       param.bind(this.subset);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 374 */     this.top.setValue(-1);
/* 375 */     this.upperPercentage.setValue(-1.0D);
/* 376 */     this.lowerPercentage.setValue(-1.0D);
/*     */ 
/* 378 */     this.cellOperator.setValue(0);
/* 379 */     this.useRules.setValue(false);
/*     */ 
/* 381 */     if (this.slice != null)
/* 382 */       clearSlice();
/* 383 */     this.criteria = new DataCriteria(">", "");
/* 384 */     this.criteria.bind(this.subset);
/*     */   }
/*     */ 
/*     */   public final void bind(Subset2 subset) {
/* 388 */     super.bind(subset);
/*     */ 
/* 390 */     this.criteria.bind(subset);
/* 391 */     this.top.bind(subset);
/* 392 */     this.cellOperator.bind(subset);
/* 393 */     this.sourceCube.bind(subset);
/* 394 */     this.upperPercentage.bind(subset);
/* 395 */     this.lowerPercentage.bind(subset);
/* 396 */     this.useRules.bind(subset);
/*     */ 
/* 398 */     for (ObjectParameter params : this.slice)
/* 399 */       params.bind(subset); 
/*     */   }
/*     */ 
/*     */   public final void unbind() {
/* 402 */     super.unbind();
/*     */ 
/* 404 */     this.criteria.unbind();
/* 405 */     this.top.unbind();
/* 406 */     this.cellOperator.unbind();
/* 407 */     this.sourceCube.unbind();
/* 408 */     this.upperPercentage.unbind();
/* 409 */     this.lowerPercentage.unbind();
/* 410 */     this.useRules.unbind();
/*     */ 
/* 412 */     for (ObjectParameter params : this.slice)
/* 413 */       params.unbind();
/*     */   }
/*     */ 
/*     */   public final void adapt(FilterSetting from) {
/* 417 */     if (!(from instanceof DataFilterSetting))
/* 418 */       return;
/* 419 */     DataFilterSetting setting = (DataFilterSetting)from;
/* 420 */     reset();
/* 421 */     setSourceCube(setting.getSourceCube().getValue());
/* 422 */     setSlice(setting.getSliceParameters());
/* 423 */     DataCriteria fromCriteria = setting.getCriteria();
/* 424 */     if (fromCriteria != null)
/* 425 */       setCriteria(fromCriteria.copy());
/* 426 */     setTop(setting.getTop().getValue().intValue());
/* 427 */     setCellOperator(setting.getCellOperator().getValue().intValue());
/* 428 */     setUpperPercentage(setting.getUpperPercentage().getValue().doubleValue());
/* 429 */     setLowerPercentage(setting.getLowerPercentage().getValue().doubleValue());
/* 430 */     setUseRules(setting.getUseRules().getValue().booleanValue());
/*     */   }
/*     */ 
/*     */   private final ObjectParameter getDimensionSlice(int index) {
/* 434 */     if (index == this.slice.size()) {
/* 435 */       ObjectParameter param = new ObjectParameter();
/* 436 */       param.bind(this.subset);
/* 437 */       this.slice.add(param);
/*     */     }
/* 439 */     return (ObjectParameter)this.slice.get(index);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.DataFilterSetting
 * JD-Core Version:    0.5.4
 */