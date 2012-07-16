/*     */ package org.palo.api.subsets.impl;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.ElementNode;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.impl.CompoundKey;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.subsets.SubsetFilter;
/*     */ import org.palo.api.subsets.filter.EffectiveFilter;
/*     */ import org.palo.api.subsets.filter.PicklistFilter;
/*     */ import org.palo.api.subsets.filter.RestrictiveFilter;
/*     */ import org.palo.api.subsets.filter.SortingFilter;
/*     */ import org.palo.api.subsets.filter.StructuralFilter;
/*     */ 
/*     */ class Subset2Impl
/*     */   implements Subset2
/*     */ {
/*     */   private final String id;
/*     */   private final Hierarchy hierarchy;
/*     */   private String name;
/*     */   private String description;
/*  76 */   private int indent = 1;
/*     */ 
/*  80 */   private final HashSet<String> elements = new HashSet();
/*     */ 
/*  82 */   private final HashMap<Integer, SubsetFilter> filters = new HashMap();
/*     */ 
/*  84 */   private final ArrayList<ElementNode> rootNodes = new ArrayList();
/*     */   private final CompoundKey key;
/*     */   private final int type;
/*     */   private boolean modified;
/*     */ 
/*     */   Subset2Impl(String id, String name, Hierarchy hierarchy, int type)
/*     */   {
/* 100 */     this.id = id;
/* 101 */     this.type = type;
/* 102 */     this.name = name;
/* 103 */     this.hierarchy = hierarchy;
/* 104 */     this.key = 
/* 107 */       new CompoundKey(new Object[] { Subset2Impl.class, 
/* 105 */       hierarchy.getDimension().getDatabase().getId(), 
/* 106 */       hierarchy.getId(), id, 
/* 107 */       Integer.valueOf(type) });
/* 108 */     reset();
/*     */   }
/*     */ 
/*     */   public final Subset2 copy()
/*     */   {
/* 113 */     Subset2Impl copy = new Subset2Impl(this.id, this.name, this.hierarchy, this.type);
/*     */ 
/* 115 */     copy.indent = this.indent;
/* 116 */     copy.description = this.description;
/*     */ 
/* 118 */     for (SubsetFilter filter : this.filters.values()) {
/* 119 */       copy.add(filter.copy());
/*     */     }
/* 121 */     return copy;
/*     */   }
/*     */ 
/*     */   public final String getDescription() {
/* 125 */     return this.description;
/*     */   }
/*     */ 
/*     */   public final String getId() {
/* 129 */     return this.id;
/*     */   }
/*     */ 
/*     */   public final int getType() {
/* 133 */     return this.type;
/*     */   }
/*     */ 
/*     */   public final void modified() {
/* 137 */     this.modified = true;
/*     */   }
/*     */ 
/*     */   public final boolean contains(Element element) {
/* 141 */     applyFilters();
/* 142 */     return this.elements.contains(element.getId());
/*     */   }
/*     */ 
/*     */   public final String[] getElementIds() {
/* 146 */     applyFilters();
/* 147 */     return (String[])this.elements.toArray(new String[this.elements.size()]);
/*     */   }
/*     */ 
/*     */   public final Element[] getElements()
/*     */   {
/* 152 */     applyFilters();
/* 153 */     Element[] _elements = new Element[this.elements.size()];
/* 154 */     int i = 0;
/* 155 */     for (String elID : this.elements) {
/* 156 */       Element element = this.hierarchy.getElementById(elID);
/* 157 */       _elements[(i++)] = element;
/*     */     }
/* 159 */     return _elements;
/*     */   }
/*     */ 
/*     */   public final ElementNode[] getHierarchy()
/*     */   {
/* 164 */     return getRootNodes();
/*     */   }
/*     */ 
/*     */   public final ElementNode[] getRootNodes() {
/* 168 */     applyFilters();
/* 169 */     return (ElementNode[])this.rootNodes.toArray(new ElementNode[this.rootNodes.size()]);
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 174 */     for (SubsetFilter filter : this.filters.values())
/* 175 */       filter.unbind();
/* 176 */     this.filters.clear();
/* 177 */     this.elements.clear();
/* 178 */     modified();
/*     */   }
/*     */ 
/*     */   public final void save() throws PaloAPIException
/*     */   {
/*     */     try {
/* 184 */       Database database = this.hierarchy.getDimension().getDatabase();
/* 185 */       SubsetStorageHandlerImpl storageHandler = 
/* 186 */         (SubsetStorageHandlerImpl)database.getSubsetStorageHandler();
/* 187 */       storageHandler.save(this);
/*     */     } catch (PaloIOException pio) {
/* 189 */       throw new PaloAPIException("Failed to save subset '" + getName() + 
/* 190 */         "'!", pio);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void add(SubsetFilter filter)
/*     */   {
/* 196 */     if (filter == null)
/* 197 */       return;
/* 198 */     if (this.filters.put(Integer.valueOf(filter.getType()), filter) == null) {
/* 199 */       filter.bind(this);
/* 200 */       modified();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SubsetFilter[] getFilters()
/*     */   {
/* 206 */     return (SubsetFilter[])this.filters.values().toArray(new SubsetFilter[this.filters.size()]);
/*     */   }
/*     */ 
/*     */   public final boolean isActive(int filterType) {
/* 210 */     return this.filters.containsKey(Integer.valueOf(filterType));
/*     */   }
/*     */ 
/*     */   public final void remove(SubsetFilter filter) {
/* 214 */     if (filter == null)
/* 215 */       return;
/* 216 */     if (this.filters.remove(Integer.valueOf(filter.getType())) != null) {
/* 217 */       filter.unbind();
/* 218 */       modified();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SubsetFilter getFilter(int type)
/*     */   {
/* 224 */     return (SubsetFilter)this.filters.get(new Integer(type));
/*     */   }
/*     */ 
/*     */   public final int getIndent() {
/* 228 */     return this.indent;
/*     */   }
/*     */ 
/*     */   public final void setIndent(int indent)
/*     */   {
/* 233 */     if (indent < 1)
/* 234 */       indent = 1;
/* 235 */     this.indent = indent;
/* 236 */     modified();
/*     */   }
/*     */ 
/*     */   public final String getName()
/*     */   {
/* 241 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final void rename(String newName) {
/*     */     try {
/* 246 */       Database database = this.hierarchy.getDimension().getDatabase();
/* 247 */       SubsetStorageHandlerImpl storageHandler = 
/* 248 */         (SubsetStorageHandlerImpl)database.getSubsetStorageHandler();
/* 249 */       storageHandler.rename(this, newName);
/* 250 */       this.name = newName;
/*     */     } catch (PaloIOException pio) {
/* 252 */       throw new PaloAPIException("Failed to rename subset '" + getName() + 
/* 253 */         "'!", pio);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Dimension getDimension() {
/* 258 */     return this.hierarchy.getDimension();
/*     */   }
/*     */ 
/*     */   public Hierarchy getDimHierarchy() {
/* 262 */     return this.hierarchy;
/*     */   }
/*     */ 
/*     */   public final void setDescription(String description) {
/* 266 */     this.description = description;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object obj)
/*     */   {
/* 271 */     if (!(obj instanceof Subset2)) {
/* 272 */       return false;
/*     */     }
/* 274 */     Subset2Impl other = (Subset2Impl)obj;
/* 275 */     return this.key.equals(other.key);
/*     */   }
/*     */ 
/*     */   public final int hashCode() {
/* 279 */     return this.key.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean canBeModified()
/*     */   {
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canCreateChildren() {
/* 288 */     return false;
/*     */   }
/*     */ 
/*     */   private final void applyFilters() {
/* 292 */     if (this.modified) {
/* 293 */       this.modified = false;
/* 294 */       this.rootNodes.clear();
/* 295 */       this.elements.clear();
/* 296 */       HashSet<Element> elements = new HashSet(
/* 297 */         Arrays.asList(this.hierarchy.getElements()));
/*     */ 
/* 300 */       Integer type_sort = new Integer(16);
/* 301 */       SubsetFilter sortfilter = (SubsetFilter)this.filters.get(type_sort);
/* 302 */       if (sortfilter == null) {
/* 303 */         sortfilter = new SortingFilter(this.hierarchy);
/* 304 */         sortfilter.initialize();
/* 305 */         this.filters.put(type_sort, sortfilter);
/*     */       }
/*     */       int[] effectiveTypes;
/* 309 */       for (SubsetFilter filter : this.filters.values()) {
/* 310 */         if (filter instanceof EffectiveFilter) {
/* 311 */           EffectiveFilter effectiveFilter = (EffectiveFilter)filter;
/* 312 */           effectiveTypes = effectiveFilter.getEffectiveFilter();
/* 313 */           for (int type : effectiveTypes) {
/* 314 */             SubsetFilter effectiveSubsetFilter = (SubsetFilter)this.filters.get(
/* 315 */               new Integer(type));
/*     */ 
/* 317 */             if (effectiveSubsetFilter != null) {
/* 318 */               effectiveSubsetFilter.add(effectiveFilter);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 323 */       for (SubsetFilter filter : this.filters.values()) {
/* 324 */         if (filter instanceof RestrictiveFilter) {
/* 325 */           ((RestrictiveFilter)filter).filter(elements);
/*     */         }
/*     */       }
/*     */ 
/* 329 */       SubsetFilter filter = (SubsetFilter)this.filters.get(
/* 330 */         new Integer(4));
/* 331 */       if (filter != null) {
/* 332 */         ((PicklistFilter)filter).merge(elements);
/*     */       }
/*     */ 
/* 335 */       filter = (SubsetFilter)this.filters.get(type_sort);
/* 336 */       if (filter == null) {
/* 337 */         filter = new SortingFilter(this.hierarchy);
/* 338 */         filter.initialize();
/*     */       }
/* 340 */       List sortedNodes = ((SortingFilter)filter)
/* 341 */         .sort(elements);
/* 342 */       ((StructuralFilter)filter).filter(sortedNodes, elements);
/*     */ 
/* 345 */       filter = (SubsetFilter)this.filters.get(new Integer(4));
/* 346 */       if ((filter != null) && (filter instanceof StructuralFilter)) {
/* 347 */         ((StructuralFilter)filter).filter(sortedNodes, elements);
/*     */       }
/*     */ 
/* 350 */       filter = (SubsetFilter)this.filters.get(new Integer(2));
/* 351 */       if (filter != null) {
/* 352 */         ((StructuralFilter)filter).filter(sortedNodes, elements);
/*     */       }
/* 354 */       for (Element element : elements) {
/* 355 */         this.elements.add(element.getId());
/*     */       }
/* 357 */       this.rootNodes.addAll(sortedNodes);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getDefinition() {
/*     */     try {
/* 363 */       Database database = this.hierarchy.getDimension().getDatabase();
/* 364 */       SubsetStorageHandlerImpl storageHandler = 
/* 365 */         (SubsetStorageHandlerImpl)database.getSubsetStorageHandler();
/* 366 */       return storageHandler.getDefinition(this);
/*     */     } catch (PaloIOException pio) {
/* 368 */       throw new PaloAPIException("Failed to get definition of subset '" + getName() + 
/* 369 */         "'!", pio);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Subset2 setDefinition(String definition) {
/*     */     try {
/* 375 */       Database database = this.hierarchy.getDimension().getDatabase();
/* 376 */       SubsetStorageHandlerImpl storageHandler = 
/* 377 */         (SubsetStorageHandlerImpl)database.getSubsetStorageHandler();
/* 378 */       storageHandler.setDefinition(this, definition);
/* 379 */       return storageHandler.load(this.id, getDimHierarchy(), getType(), (SubsetHandlerImpl)getDimHierarchy().getSubsetHandler());
/*     */     } catch (PaloIOException pio) {
/* 381 */       throw new PaloAPIException("Failed to get definition of subset '" + getName() + 
/* 382 */         "'!", pio);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean validate()
/*     */   {
/*     */     try {
/* 389 */       Database database = this.hierarchy.getDimension().getDatabase();
/* 390 */       SubsetStorageHandlerImpl storageHandler = 
/* 391 */         (SubsetStorageHandlerImpl)database.getSubsetStorageHandler();
/* 392 */       storageHandler.validate(this);
/* 393 */       return true;
/*     */     } catch (PaloIOException pio) {
/* 395 */       throw new PaloAPIException("Validation of subset '" + getName() + 
/* 396 */         "' failed!", pio);
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.impl.Subset2Impl
 * JD-Core Version:    0.5.4
 */