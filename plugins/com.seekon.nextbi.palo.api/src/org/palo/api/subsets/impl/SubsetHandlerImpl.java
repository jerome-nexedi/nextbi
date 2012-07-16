/*     */ package org.palo.api.subsets.impl;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.exceptions.InsufficientRightsException;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.persistence.SubsetLoadObserver;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.subsets.SubsetHandler;
/*     */ 
/*     */ public class SubsetHandlerImpl
/*     */   implements SubsetHandler
/*     */ {
/*     */   private final Hierarchy hierarchy;
/*     */   private final boolean newSubsetsSupported;
/*     */   private final SubsetStorageHandlerImpl storageHandler;
/*     */   private HashMap<String, Subset2> localSubsets;
/*     */   private HashMap<String, Subset2> globalSubsets;
/*     */ 
/*     */   /** @deprecated */
/*     */   public SubsetHandlerImpl(Dimension dimension)
/*     */   {
/*  75 */     this(dimension.getDefaultHierarchy());
/*     */   }
/*     */ 
/*     */   private SubsetHandlerImpl(Hierarchy hierarchy)
/*     */   {
/*  90 */     this.hierarchy = hierarchy;
/*  91 */     this.localSubsets = new HashMap();
/*  92 */     this.globalSubsets = new HashMap();
/*  93 */     Database database = hierarchy.getDimension().getDatabase();
/*  94 */     this.storageHandler = 
/*  95 */       ((SubsetStorageHandlerImpl)database.getSubsetStorageHandler());
/*  96 */     if (this.storageHandler == null)
/*  97 */       throw new NullPointerException("storage handler is null!!");
/*  98 */     this.newSubsetsSupported = ((!hierarchy.getDimension().isSystemDimension()) && 
/*  99 */       (!hierarchy.isAttributeHierarchy()) && 
/* 100 */       (!hierarchy.isSubsetHierarchy()) && 
/* 101 */       (database.supportsNewSubsets()));
/*     */   }
/*     */ 
/*     */   public final boolean canRead(int type)
/*     */   {
/* 106 */     return this.storageHandler.canRead(type);
/*     */   }
/*     */ 
/*     */   public final boolean canWrite(int type) {
/* 110 */     return this.storageHandler.canWrite(type);
/*     */   }
/*     */ 
/*     */   public final Dimension getDimension() {
/* 114 */     return this.hierarchy.getDimension();
/*     */   }
/*     */ 
/*     */   public final Hierarchy getHierarchy() {
/* 118 */     return this.hierarchy;
/*     */   }
/*     */ 
/*     */   public final void reset() {
/* 122 */     this.localSubsets.clear();
/* 123 */     this.globalSubsets.clear();
/* 124 */     this.storageHandler.reset();
/*     */   }
/*     */ 
/*     */   public final Subset2 addSubset(String name, int type)
/*     */   {
/* 129 */     if (!this.newSubsetsSupported)
/* 130 */       throw new PaloAPIException(
/* 131 */         "New subsets are not supported by database '" + 
/* 132 */         this.hierarchy.getDimension().getDatabase().getName() + "'!");
/* 133 */     if (!canWrite(type))
/* 134 */       throw new InsufficientRightsException("Cannot add subset '" + name + 
/* 135 */         "' to " + getTypeStr(type) + 
/* 136 */         " subsets!\nNot enough rights!");
/*     */     try
/*     */     {
/* 139 */       String id = this.storageHandler.newSubsetCell(name, this.hierarchy, type);
/* 140 */       if (id == null)
/* 141 */         throw new PaloAPIException("Adding a subset to a system dimension is not allowed!");
/* 142 */       Subset2 subset = new Subset2Impl(id, name, this.hierarchy, type);
/* 143 */       this.storageHandler.save(subset);
/* 144 */       register(subset);
/* 145 */       return subset;
/*     */     } catch (PaloIOException e) {
/* 147 */       throw new PaloAPIException("The subset '" + name + 
/* 148 */         "' exists already!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Subset2 getSubset(String id, int type) {
/* 153 */     if ((!this.newSubsetsSupported) || (id == null))
/* 154 */       return null;
/* 155 */     HashMap cache = getCache(type);
/* 156 */     Subset2 subset = (Subset2)cache.get(id);
/* 157 */     if (subset == null) {
/*     */       try
/*     */       {
/* 160 */         subset = this.storageHandler.load(id, this.hierarchy, type, this);
/* 161 */         register(subset);
/*     */       } catch (PaloIOException e) {
/* 163 */         throw new PaloAPIException("Loading subset '" + 
/* 164 */           getSubsetName(id) + "' of hierarchy '" + 
/* 165 */           this.hierarchy.getName() + "' failed!!", e);
/*     */       }
/*     */     }
/* 168 */     return subset;
/*     */   }
/*     */ 
/*     */   public final void getSubsets(SubsetLoadObserver observer) {
/* 172 */     if ((observer == null) || (!this.newSubsetsSupported))
/* 173 */       return;
/* 174 */     String[] subIds = this.storageHandler.getSubsetIDs(this.hierarchy);
/* 175 */     for (String id : subIds) {
/* 176 */       loadSubset(id, 1, this.hierarchy, observer);
/* 177 */       loadSubset(id, 0, this.hierarchy, observer);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Subset2[] getSubsets() {
/* 182 */     if (!this.newSubsetsSupported)
/* 183 */       return new Subset2[0];
/* 184 */     String[] subIds = this.storageHandler.getSubsetIDs(this.hierarchy);
/* 185 */     LinkedHashSet subsets = new LinkedHashSet();
/* 186 */     for (String id : subIds) {
/* 187 */       Subset2 subset = loadSubset(id, 1);
/* 188 */       if (subset != null) {
/* 189 */         subsets.add(subset);
/*     */       }
/* 191 */       subset = loadSubset(id, 0);
/* 192 */       if (subset != null)
/* 193 */         subsets.add(subset);
/*     */     }
/* 195 */     return (Subset2[])subsets.toArray(new Subset2[subsets.size()]);
/*     */   }
/*     */ 
/*     */   public final void getSubsets(int type, SubsetLoadObserver observer) {
/* 199 */     if ((observer == null) || (!this.newSubsetsSupported))
/* 200 */       return;
/* 201 */     String[] subIds = this.storageHandler.getSubsetIDs(this.hierarchy);
/* 202 */     for (String id : subIds)
/* 203 */       loadSubset(id, type, this.hierarchy, observer);
/*     */   }
/*     */ 
/*     */   public final Subset2[] getSubsets(int type)
/*     */   {
/* 208 */     if (!this.newSubsetsSupported)
/* 209 */       return new Subset2[0];
/* 210 */     String[] subIds = this.storageHandler.getSubsetIDs(this.hierarchy, type);
/* 211 */     LinkedHashSet subsets = new LinkedHashSet();
/* 212 */     for (String id : subIds) {
/* 213 */       Subset2 subset = loadSubset(id, type);
/* 214 */       if (subset != null)
/* 215 */         subsets.add(subset);
/*     */     }
/* 217 */     return (Subset2[])subsets.toArray(new Subset2[subsets.size()]);
/*     */   }
/*     */ 
/*     */   public final String getSubsetId(String name, int type) {
/* 221 */     if (!this.newSubsetsSupported)
/* 222 */       return null;
/* 223 */     return this.storageHandler.getSubsetId(this.hierarchy, name, type);
/*     */   }
/*     */ 
/*     */   public final String[] getSubsetIDs() {
/* 227 */     if (!this.newSubsetsSupported) {
/* 228 */       return new String[0];
/*     */     }
/* 230 */     if (this.hierarchy.getDimension().isSystemDimension())
/* 231 */       return new String[0];
/* 232 */     return this.storageHandler.getSubsetIDs(this.hierarchy);
/*     */   }
/*     */ 
/*     */   public String[] getSubsetIDs(int type) {
/* 236 */     if (!this.newSubsetsSupported) {
/* 237 */       return new String[0];
/*     */     }
/* 239 */     if (this.hierarchy.getDimension().isSystemDimension())
/* 240 */       return new String[0];
/* 241 */     return this.storageHandler.getSubsetIDs(this.hierarchy, type);
/*     */   }
/*     */ 
/*     */   public String[] getSubsetNames() {
/* 245 */     if (!this.newSubsetsSupported) {
/* 246 */       return new String[0];
/*     */     }
/* 248 */     if (this.hierarchy.getDimension().isSystemDimension())
/* 249 */       return new String[0];
/* 250 */     return this.storageHandler.getSubsetNames(this.hierarchy);
/*     */   }
/*     */ 
/*     */   public String[] getSubsetNames(int type) {
/* 254 */     if ((!this.newSubsetsSupported) || 
/* 255 */       (this.hierarchy.getDimension().isSystemDimension()))
/* 256 */       return new String[0];
/* 257 */     return this.storageHandler.getSubsetNames(this.hierarchy, type);
/*     */   }
/*     */ 
/*     */   public final String getSubsetName(String id) {
/* 261 */     if (!this.newSubsetsSupported) {
/* 262 */       return null;
/*     */     }
/* 264 */     return this.storageHandler.getSubsetName(id);
/*     */   }
/*     */ 
/*     */   public final boolean hasSubsets(int type) {
/* 268 */     if (!this.newSubsetsSupported) {
/* 269 */       return false;
/*     */     }
/* 271 */     if (this.hierarchy.getDimension().isSystemDimension())
/* 272 */       return false;
/* 273 */     return this.storageHandler.hasSubsets(this.hierarchy, type);
/*     */   }
/*     */ 
/*     */   public void remove(Subset2 subset) {
/* 277 */     if (isRegistered(subset)) {
/* 278 */       this.storageHandler.remove(subset);
/* 279 */       unregister(subset);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(String id, int type)
/*     */   {
/* 286 */     HashMap cache = getCache(type);
/* 287 */     this.storageHandler.remove(id, type, this.hierarchy);
/* 288 */     cache.remove(id);
/*     */   }
/*     */ 
/*     */   public final void save(Subset2 subset) {
/* 292 */     if (!isRegistered(subset)) return;
/*     */     try {
/* 294 */       this.storageHandler.save(subset);
/*     */     } catch (PaloIOException e) {
/* 296 */       throw new PaloAPIException("Saving subset '" + subset.getName() + 
/* 297 */         "' of hierarchy '" + this.hierarchy.getName() + 
/* 298 */         "' failed!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Subset2 create(String id, String name, Hierarchy hierarchy, int type)
/*     */   {
/* 307 */     if (!this.newSubsetsSupported) {
/* 308 */       throw new PaloAPIException(
/* 309 */         "New subsets are not supported by database '" + 
/* 310 */         hierarchy.getDimension().getDatabase().getName() + "'!");
/*     */     }
/* 312 */     if (hierarchy.equals(this.hierarchy)) {
/* 313 */       Subset2 subset = new Subset2Impl(id, name, hierarchy, type);
/* 314 */       register(subset);
/* 315 */       return subset;
/*     */     }
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */   private final void register(Subset2 subset)
/*     */   {
/* 324 */     if (subset == null)
/* 325 */       return;
/* 326 */     HashMap cache = getCache(subset.getType());
/* 327 */     cache.put(subset.getId(), subset);
/*     */   }
/*     */ 
/*     */   private final void unregister(Subset2 subset) {
/* 331 */     if (subset == null)
/* 332 */       return;
/* 333 */     HashMap cache = getCache(subset.getType());
/* 334 */     cache.remove(subset.getId());
/*     */   }
/*     */ 
/*     */   private final boolean isRegistered(Subset2 subset) {
/* 338 */     HashMap cache = getCache(subset.getType());
/* 339 */     return cache.containsKey(subset.getId());
/*     */   }
/*     */ 
/*     */   private final String getTypeStr(int type) {
/* 343 */     return (type == 1) ? "global" : "local";
/*     */   }
/*     */ 
/*     */   private final HashMap<String, Subset2> getCache(int type) {
/* 347 */     return (type == 1) ? this.globalSubsets : this.localSubsets;
/*     */   }
/*     */ 
/*     */   private final Subset2 loadSubset(String id, int type)
/*     */   {
/*     */     try {
/* 353 */       return getSubset(id, type);
/*     */     }
/*     */     catch (PaloAPIException localPaloAPIException)
/*     */     {
/*     */     }
/*     */ 
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */   private final void loadSubset(String id, int type, Hierarchy hierarchy, SubsetLoadObserver observer)
/*     */   {
/*     */     try {
/* 366 */       Subset2 subset = getSubset(id, type);
/* 367 */       if (subset != null) {
/* 368 */         observer.loadComplete(subset);
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (PaloAPIException pex)
/*     */     {
/* 374 */       observer.loadFailed(id, getSubsetName(id), type, hierarchy);
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.impl.SubsetHandlerImpl
 * JD-Core Version:    0.5.4
 */