/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import com.tensegrity.palojava.CubeInfo;
/*     */ import java.math.BigInteger;
/*     */ import java.text.NumberFormat;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.palo.api.Cell;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ExportContext;
/*     */ import org.palo.api.ExportDataset;
/*     */ import org.palo.api.Lock;
/*     */ import org.palo.api.Property;
/*     */ import org.palo.api.Property2;
/*     */ import org.palo.api.Rule;
/*     */ import org.palo.api.VirtualCubeDefinition;
/*     */ import org.palo.api.VirtualDimensionDefinition;
/*     */ import org.palo.api.VirtualObject;
/*     */ import org.palo.api.persistence.PersistenceObserver;
/*     */ 
/*     */ class VirtualCubeImpl
/*     */   implements Cube, VirtualObject
/*     */ {
/*     */   private final VirtualCubeDefinition definition;
/*     */   private final VirtualDimensionImpl[] vdims;
/*     */   private final Map dim2vdim;
/*     */   private final CompoundKey key;
/*     */ 
/*     */   VirtualCubeImpl(VirtualCubeDefinition definition)
/*     */   {
/*  82 */     this.definition = definition;
/*     */ 
/*  84 */     VirtualDimensionDefinition[] vdimdefs = 
/*  85 */       definition.getVirtualDimensionDefinitions();
/*  86 */     this.vdims = new VirtualDimensionImpl[vdimdefs.length];
/*  87 */     for (int i = 0; i < this.vdims.length; ++i)
/*     */     {
/*  90 */       VirtualDimensionImpl vdim = new VirtualDimensionImpl(
/*  91 */         vdimdefs[i].getSourceDimension(), 
/*  92 */         vdimdefs[i].getElements(), 
/*  93 */         vdimdefs[i].getRootElements(), 
/*  94 */         vdimdefs[i].isFlat(), 
/*  95 */         vdimdefs[i].getActiveHierarchy());
/*  96 */       this.vdims[i] = vdim;
/*  97 */       vdim.setVirtualDefinition(vdimdefs[i]);
/*     */     }
/*     */ 
/* 109 */     this.dim2vdim = new HashMap();
/*     */ 
/* 111 */     for (int i = 0; i < this.vdims.length; ++i)
/*     */     {
/* 113 */       VirtualDimensionImpl vdim = this.vdims[i];
/* 114 */       Dimension dim = vdim.getSourceDimension();
/*     */ 
/* 116 */       this.dim2vdim.put(dim, vdim);
/*     */     }
/*     */ 
/* 119 */     this.key = createKey();
/*     */   }
/*     */ 
/*     */   private final CompoundKey createKey()
/*     */   {
/* 139 */     return new CompoundKey(new Object[] { 
/* 140 */       VirtualCubeImpl.class, 
/* 141 */       this.definition.getSourceCube().getName(), 
/* 142 */       this.definition.getSourceCube().getDatabase().getName(), 
/* 143 */       getName() });
/*     */   }
/*     */ 
/*     */   public int getExtendedType()
/*     */   {
/* 148 */     return 1;
/*     */   }
/*     */ 
/*     */   public final String getId() {
/* 152 */     return this.definition.getSourceCube().getId() + "@@" + 
/* 153 */       Integer.toHexString(System.identityHashCode(this));
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 158 */     String postfix = this.definition.getName();
/* 159 */     if (postfix == null)
/* 160 */       postfix = Integer.toHexString(System.identityHashCode(this));
/* 161 */     return this.definition.getSourceCube().getName() + "@@" + postfix;
/*     */   }
/*     */ 
/*     */   public Database getDatabase()
/*     */   {
/* 166 */     return this.definition.getSourceCube().getDatabase();
/*     */   }
/*     */ 
/*     */   public int getDimensionCount()
/*     */   {
/* 171 */     return this.definition.getSourceCube().getDimensionCount();
/*     */   }
/*     */ 
/*     */   public Dimension getDimensionAt(int index)
/*     */   {
/* 176 */     Dimension dim = this.definition.getSourceCube().getDimensionAt(index);
/* 177 */     Dimension vdim = (Dimension)this.dim2vdim.get(dim);
/* 178 */     return (vdim == null) ? dim : vdim;
/*     */   }
/*     */ 
/*     */   public Dimension[] getDimensions()
/*     */   {
/* 183 */     Dimension[] dims = this.definition.getSourceCube().getDimensions();
/* 184 */     if (dims == null)
/* 185 */       return null;
/* 186 */     for (int i = 0; i < dims.length; ++i)
/*     */     {
/* 188 */       Dimension dim = dims[i];
/* 189 */       Dimension vdim = (Dimension)this.dim2vdim.get(dim);
/* 190 */       if (vdim == null)
/*     */         continue;
/* 192 */       dims[i] = vdim;
/*     */     }
/* 194 */     return dims;
/*     */   }
/*     */ 
/*     */   public Dimension getDimensionByName(String name)
/*     */   {
/* 199 */     if (name == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     for (int i = 0; i < this.vdims.length; ++i)
/*     */     {
/* 204 */       VirtualDimensionImpl vdim = this.vdims[i];
/* 205 */       if ((vdim != null) && (vdim.getName().equalsIgnoreCase(name))) {
/* 206 */         return vdim;
/*     */       }
/*     */     }
/* 209 */     return this.definition.getSourceCube().getDimensionByName(name);
/*     */   }
/*     */ 
/*     */   public final Dimension getDimensionById(String id) {
/* 213 */     for (int i = 0; i < this.vdims.length; ++i) {
/* 214 */       if (this.vdims[i].getId().equals(id))
/* 215 */         return this.vdims[i];
/*     */     }
/* 217 */     return this.definition.getSourceCube().getDimensionById(id);
/*     */   }
/*     */ 
/*     */   public void commitLog()
/*     */   {
/* 222 */     this.definition.getSourceCube().commitLog();
/*     */   }
/*     */ 
/*     */   public Object getData(String[] coordinates)
/*     */   {
/* 227 */     return this.definition.getSourceCube().getData(coordinates);
/*     */   }
/*     */ 
/*     */   public Object[] getDataArray(String[][] elements)
/*     */   {
/* 232 */     return this.definition.getSourceCube().getDataArray(elements);
/*     */   }
/*     */ 
/*     */   public void convert(int type) {
/* 236 */     this.definition.getSourceCube().convert(type);
/*     */   }
/*     */ 
/*     */   public Object getData(Element[] coordinates)
/*     */   {
/* 246 */     return this.definition.getSourceCube().getData(coordinates);
/*     */   }
/*     */ 
/*     */   public Object[] getDataArray(Element[][] elements)
/*     */   {
/* 251 */     return this.definition.getSourceCube().getDataArray(elements);
/*     */   }
/*     */ 
/*     */   public Object[] getDataBulk(Element[][] elements)
/*     */   {
/* 256 */     return this.definition.getSourceCube().getDataBulk(elements);
/*     */   }
/*     */ 
/*     */   public void setData(String[] coordinates, Object value)
/*     */   {
/* 261 */     this.definition.getSourceCube().setData(coordinates, value);
/*     */   }
/*     */ 
/*     */   public void setData(Element[] coordinates, Object value)
/*     */   {
/* 266 */     this.definition.getSourceCube().setData(coordinates, value);
/*     */   }
/*     */ 
/*     */   public void setData(Element[] coordinates, Object value, NumberFormat formatter)
/*     */   {
/* 271 */     this.definition.getSourceCube().setData(coordinates, value);
/*     */   }
/*     */ 
/*     */   public void setDataSplashed(Element[] coordinate, Object value) {
/* 275 */     this.definition.getSourceCube().setDataSplashed(coordinate, value);
/*     */   }
/*     */ 
/*     */   public void setDataSplashed(Element[] coordinate, Object value, NumberFormat formatter) {
/* 279 */     this.definition.getSourceCube().setDataSplashed(coordinate, value, formatter);
/*     */   }
/*     */ 
/*     */   public void setDataSplashed(String[] coordinates, Object value, int splashMode)
/*     */   {
/* 284 */     this.definition.getSourceCube().setDataSplashed(coordinates, value, splashMode);
/*     */   }
/*     */ 
/*     */   public void setDataSplashed(Element[] coordinates, Object value, int splashMode)
/*     */   {
/* 289 */     this.definition.getSourceCube().setDataSplashed(coordinates, value, splashMode);
/*     */   }
/*     */ 
/*     */   public void setDataArray(Element[][] coordinates, Object[] values, int splashMode) {
/* 293 */     this.definition.getSourceCube().setDataArray(coordinates, values, splashMode);
/*     */   }
/*     */ 
/*     */   public boolean isAttributeCube()
/*     */   {
/* 298 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isSubsetCube()
/*     */   {
/* 303 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isViewCube() {
/* 307 */     return false;
/*     */   }
/*     */ 
/*     */   public CubeView addCubeView(String name, Property[] properties)
/*     */   {
/* 312 */     return null;
/*     */   }
/*     */ 
/*     */   public CubeView addCubeView(String id, String name, Property[] properties)
/*     */   {
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   public CubeView[] getCubeViews() {
/* 321 */     return new CubeView[0];
/*     */   }
/*     */ 
/*     */   public final int getCubeViewCount() {
/* 325 */     return 0;
/*     */   }
/*     */ 
/*     */   public void removeCubeView(CubeView view) {
/*     */   }
/*     */ 
/*     */   public CubeView getCubeView(String id) {
/* 332 */     return null;
/*     */   }
/*     */   public final String[] getCubeViewIds() {
/* 335 */     return new String[0];
/*     */   }
/*     */ 
/*     */   public String getCubeViewName(String id) {
/* 339 */     return null;
/*     */   }
/*     */ 
/*     */   public void getCubeViews(PersistenceObserver observer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 348 */     if (!(o instanceof VirtualCubeImpl))
/* 349 */       return false;
/* 350 */     VirtualCubeImpl other = (VirtualCubeImpl)o;
/*     */ 
/* 354 */     return this.key.equals(other.key);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 358 */     int result = 17;
/* 359 */     result = 37 * result * this.key.hashCode();
/* 360 */     return result;
/*     */   }
/*     */ 
/*     */   public ExportDataset getDataExport(ExportContext context)
/*     */   {
/* 368 */     return this.definition.getSourceCube().getDataExport(context);
/*     */   }
/*     */ 
/*     */   public ExportContext getExportContext() {
/* 372 */     return this.definition.getSourceCube().getExportContext();
/*     */   }
/*     */ 
/*     */   public ExportContext getExportContext(Element[][] area) {
/* 376 */     return this.definition.getSourceCube().getExportContext(area);
/*     */   }
/*     */ 
/*     */   public boolean isSystemCube() {
/* 380 */     return this.definition.getSourceCube().isSystemCube();
/*     */   }
/*     */ 
/*     */   public Rule addRule(String definition) {
/* 384 */     return this.definition.getSourceCube().addRule(definition);
/*     */   }
/*     */ 
/*     */   public Rule addRule(String definition, String externalIdentifier, boolean useIt, String comment)
/*     */   {
/* 389 */     return this.definition.getSourceCube().addRule(definition, 
/* 390 */       externalIdentifier, useIt, comment);
/*     */   }
/*     */ 
/*     */   public Rule addRule(String definition, String externalIdentifier, boolean useIt, String comment, boolean activate)
/*     */   {
/* 395 */     return this.definition.getSourceCube().addRule(definition, 
/* 396 */       externalIdentifier, useIt, comment, activate);
/*     */   }
/*     */ 
/*     */   public Rule[] getRules()
/*     */   {
/* 401 */     return this.definition.getSourceCube().getRules();
/*     */   }
/*     */ 
/*     */   public Rule getRule(Element[] coordinate) {
/* 405 */     return this.definition.getSourceCube().getRule(coordinate);
/*     */   }
/*     */ 
/*     */   public boolean removeRule(Rule rule) {
/* 409 */     return this.definition.getSourceCube().removeRule(rule);
/*     */   }
/*     */   public boolean removeRule(String ruleId) {
/* 412 */     return this.definition.getSourceCube().removeRule(ruleId);
/*     */   }
/*     */ 
/*     */   public void setDataArray(Element[][] coordinates, Object[] values, boolean add, int splashMode, boolean notifyEventProcessors)
/*     */   {
/* 417 */     this.definition.getSourceCube().setDataArray(coordinates, values, add, 
/* 418 */       splashMode, notifyEventProcessors);
/*     */   }
/*     */ 
/*     */   public void addDataArray(Element[][] coordinates, Object[] values, int splashMode)
/*     */   {
/* 423 */     this.definition.getSourceCube().addDataArray(coordinates, values, splashMode);
/*     */   }
/*     */ 
/*     */   public CubeView addCubeView(String id, String name, boolean hideEmpty) {
/* 427 */     return null;
/*     */   }
/*     */ 
/*     */   public CubeView addCubeView(String name, boolean hideEmpty) {
/* 431 */     return null;
/*     */   }
/*     */ 
/*     */   public void registerViewObserver(PersistenceObserver cubeViewObserver)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void unregisterViewObserver(PersistenceObserver cubeViewObserver)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final Object getVirtualDefinition()
/*     */   {
/* 445 */     return this.definition;
/*     */   }
/*     */ 
/*     */   public final void rename(String newName) {
/* 449 */     Util.noopWarning();
/*     */   }
/*     */ 
/*     */   public void addProperty(Property2 property) {
/* 453 */     this.definition.getSourceCube().addProperty(property);
/*     */   }
/*     */ 
/*     */   public String[] getAllPropertyIds() {
/* 457 */     return this.definition.getSourceCube().getAllPropertyIds();
/*     */   }
/*     */ 
/*     */   public Property2 getProperty(String id) {
/* 461 */     return this.definition.getSourceCube().getProperty(id);
/*     */   }
/*     */ 
/*     */   public void removeProperty(String id) {
/* 465 */     this.definition.getSourceCube().removeProperty(id);
/*     */   }
/*     */ 
/*     */   public final void clear() {
/* 469 */     this.definition.getSourceCube().clear();
/*     */   }
/*     */ 
/*     */   public final void clear(Element[][] area) {
/* 473 */     this.definition.getSourceCube().clear(area);
/*     */   }
/*     */ 
/*     */   public boolean canBeModified() {
/* 477 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren() {
/* 481 */     return true;
/*     */   }
/*     */ 
/*     */   public Cell getCell(Element[] coordinate) {
/* 485 */     return this.definition.getSourceCube().getCell(coordinate);
/*     */   }
/*     */ 
/*     */   public Cell[] getCellArea(Element[][] coordinates) {
/* 489 */     return this.definition.getSourceCube().getCellArea(coordinates);
/*     */   }
/*     */ 
/*     */   public Cell[] getCells(Element[][] coordinates) {
/* 493 */     return this.definition.getSourceCube().getCells(coordinates);
/*     */   }
/*     */ 
/*     */   public Cell[] getCells(Element[][] coordinates, boolean hideEmpty) {
/* 497 */     return this.definition.getSourceCube().getCells(coordinates, hideEmpty);
/*     */   }
/*     */ 
/*     */   public boolean isUserInfoCube() {
/* 501 */     return this.definition.getSourceCube().isUserInfoCube();
/*     */   }
/*     */ 
/*     */   public int getType() {
/* 505 */     return 0;
/*     */   }
/*     */ 
/*     */   public final Lock requestLock(Element[][] area) {
/* 509 */     return ((CubeImpl)this.definition.getSourceCube()).requestLock(area);
/*     */   }
/*     */   public final Lock[] getLocks() {
/* 512 */     return ((CubeImpl)this.definition.getSourceCube()).getLocks();
/*     */   }
/*     */   public final boolean commit(Lock lock) {
/* 515 */     return ((CubeImpl)this.definition.getSourceCube()).commit(lock);
/*     */   }
/*     */   public final boolean rollback(Lock lock, int steps) {
/* 518 */     return ((CubeImpl)this.definition.getSourceCube()).rollback(lock, steps);
/*     */   }
/*     */   public final CubeInfo getInfo() {
/* 521 */     return ((CubeImpl)this.definition.getSourceCube()).getInfo();
/*     */   }
/*     */ 
/*     */   public final BigInteger getCellCount() {
/* 525 */     return ((CubeImpl)this.definition.getSourceCube()).getCellCount();
/*     */   }
/*     */ 
/*     */   public final BigInteger getFilledCellCount() {
/* 529 */     return ((CubeImpl)this.definition.getSourceCube()).getFilledCellCount();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.VirtualCubeImpl
 * JD-Core Version:    0.5.4
 */