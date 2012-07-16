/*     */ package org.palo.api.impl;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.Property;
/*     */ import org.palo.api.exceptions.PaloObjectNotFoundException;
/*     */ import org.palo.api.persistence.PaloPersistenceException;
/*     */ import org.palo.api.persistence.PersistenceError;
/*     */ 
/*     */ class CubeViewStorageHandler
/*     */ {
/*     */   private final Database database;
/*     */   private final Map<String, CubeView> loadedViews;
/*     */   private final Map<String, List<PersistenceError>> failedViews;
/*  67 */   private final ApiExtensionController viewController = ApiExtensionController.getInstance();
/*     */   private final HashMap cubeId2viewId;
/*     */   private final HashMap<String, CubeView> allViews;
/*     */ 
/*     */   CubeViewStorageHandler(Database database)
/*     */   {
/*  75 */     this.database = database;
/*  76 */     this.cubeId2viewId = new HashMap();
/*  77 */     this.loadedViews = new HashMap();
/*  78 */     this.failedViews = new HashMap();
/*  79 */     this.allViews = new HashMap();
/*     */   }
/*     */ 
/*     */   final void initStorage()
/*     */   {
/*  84 */     if (this.database.isSystem())
/*  85 */       return;
/*     */     try
/*     */     {
/*  88 */       reload();
/*     */     } catch (PaloAPIException e) {
/*  90 */       e.printStackTrace();
/*  91 */       System.err.println("Cannot add view dimension to database '" + this.database.getName() + "'!!");
/*     */     }
/*     */   }
/*     */ 
/*     */   final int getViewCount(Cube cube)
/*     */   {
/*  99 */     Set knownViewIds = getKnownViewIds(cube);
/* 100 */     return knownViewIds.size();
/*     */   }
/*     */ 
/*     */   final String getViewName(String id)
/*     */   {
/* 105 */     CubeView view = (CubeView)this.allViews.get(id);
/* 106 */     if (view != null)
/* 107 */       return view.getName();
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   final String[] getViewIds(Cube cube)
/*     */   {
/* 117 */     Set knownViewIds = getKnownViewIds(cube);
/* 118 */     return (String[])knownViewIds.toArray(new String[knownViewIds.size()]);
/*     */   }
/*     */ 
/*     */   final CubeView addCubeView(Cube cube, String id, String name, Property[] properties)
/*     */   {
/* 123 */     Set knownViewIds = getKnownViewIds(cube);
/* 124 */     if (knownViewIds.contains(id)) {
/* 125 */       throw new PaloAPIException("CubeView already exists!");
/*     */     }
/* 127 */     CubeView view = this.viewController.createCubeView(id, name, cube, properties);
/* 128 */     this.loadedViews.put(view.getId(), view);
/* 129 */     this.allViews.put(view.getId(), view);
/* 130 */     knownViewIds.add(id);
/* 131 */     return view;
/*     */   }
/*     */ 
/*     */   final CubeView addCubeView(Cube cube, String name, Property[] properties) {
/* 135 */     Set knownViewIds = getKnownViewIds(cube);
/* 136 */     String id = Long.toString(System.currentTimeMillis());
/* 137 */     while (knownViewIds.contains(id)) {
/* 138 */       long lg = Long.parseLong(id);
/* 139 */       lg += 1L;
/* 140 */       id = Long.toString(lg);
/*     */     }
/* 142 */     return addCubeView(cube, id, name, properties);
/*     */   }
/*     */ 
/*     */   final void removeCubeView(Cube cube, CubeView view) {
/*     */     try {
/* 147 */       if ((view != null) && (view.getCube().equals(cube))) {
/* 148 */         Set knownViewIds = getKnownViewIds(cube);
/* 149 */         this.loadedViews.remove(view.getId());
/* 150 */         this.allViews.remove(view.getId());
/* 151 */         knownViewIds.remove(view.getId());
/* 152 */         ApiExtensionController.getInstance().delete(view);
/*     */       }
/*     */     }
/*     */     catch (PaloAPIException ex) {
/* 156 */       String errCode = ex.getErrorCode();
/* 157 */       if (errCode != null) {
/* 158 */         if (errCode.equals("2001"))
/* 159 */           throw new PaloObjectNotFoundException("Database not found", 
/* 160 */             ex);
/* 161 */         if (errCode.equals("3002"))
/* 162 */           throw new PaloObjectNotFoundException(
/* 163 */             "Dimension not found", ex);
/* 164 */         if (errCode.equals("4004"))
/* 165 */           throw new PaloObjectNotFoundException("Element not found", 
/* 166 */             ex);
/*     */       }
/*     */       else {
/* 169 */         throw new PaloAPIException("Couldn't remove cube view '" + 
/* 170 */           view.getName() + "'", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   final CubeView getCubeView(Cube cube, String id) throws PaloPersistenceException {
/* 175 */     if (!failed(id)) {
/* 176 */       Set knownViewIds = getKnownViewIds(cube);
/* 177 */       if (knownViewIds.contains(id)) {
/* 178 */         CubeView view = getCubeView(id);
/* 179 */         return view;
/*     */       }
/*     */     }
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */   final void removeLoadedViews(Cube cube) {
/* 186 */     Set knownViewIds = getKnownViewIds(cube);
/* 187 */     Iterator allViews = knownViewIds.iterator();
/* 188 */     while (allViews.hasNext()) {
/* 189 */       String viewId = (String)allViews.next();
/* 190 */       this.loadedViews.remove(viewId);
/*     */     }
/*     */   }
/*     */ 
/*     */   final void reload() {
/* 195 */     this.loadedViews.clear();
/* 196 */     this.cubeId2viewId.clear();
/* 197 */     this.failedViews.clear();
/* 198 */     this.allViews.clear();
/*     */     try {
/* 200 */       ApiExtensionController.getInstance().loadViews(
/* 201 */         this.database, this.cubeId2viewId, this.loadedViews);
/*     */     }
/*     */     catch (PaloPersistenceException pex)
/*     */     {
/* 205 */       PersistenceError[] errors = pex.getErrors();
/* 206 */       for (int i = 0; i < errors.length; ++i) {
/* 207 */         Object view = errors[i].getSource();
/* 208 */         if (view instanceof CubeView) {
/* 209 */           String viewId = errors[i].getSourceId();
/* 210 */           Cube srcCube = ((CubeView)view).getCube();
/* 211 */           addFailed(viewId, errors[i]);
/* 212 */           this.allViews.put(viewId, (CubeView)view);
/* 213 */           ensureIsContained(srcCube.getId(), viewId);
/*     */         }
/*     */         else
/*     */         {
/* 217 */           addFailed(errors[i].getSourceId(), errors[i]);
/* 218 */           this.allViews.put(errors[i].getSourceId(), null);
/*     */         }
/*     */       }
/*     */     }
/* 222 */     this.allViews.putAll(this.loadedViews);
/*     */   }
/*     */ 
/*     */   private final CubeView getCubeView(String id) throws PaloPersistenceException
/*     */   {
/* 227 */     if (failed(id)) {
/* 228 */       return null;
/*     */     }
/* 230 */     CubeView view = (CubeView)this.loadedViews.get(id);
/* 231 */     if (view == null)
/*     */     {
/* 233 */       view = this.viewController.loadView(this.database, id);
/* 234 */       this.loadedViews.put(view.getId(), view);
/*     */     }
/* 236 */     return view;
/*     */   }
/*     */ 
/*     */   private final Set getKnownViewIds(Cube cube)
/*     */   {
/* 246 */     return getViewIdsSet(cube.getId());
/*     */   }
/*     */ 
/*     */   private final void ensureIsContained(String cubeId, String viewId) {
/* 250 */     Set viewIds = getViewIdsSet(cubeId);
/* 251 */     viewIds.add(viewId);
/*     */   }
/*     */ 
/*     */   private final Set getViewIdsSet(String cubeId) {
/* 255 */     Set viewIds = (Set)this.cubeId2viewId.get(cubeId);
/* 256 */     if (viewIds == null) {
/* 257 */       viewIds = new LinkedHashSet();
/* 258 */       this.cubeId2viewId.put(cubeId, viewIds);
/*     */     }
/* 260 */     return viewIds;
/*     */   }
/*     */ 
/*     */   private final void addFailed(String viewId, PersistenceError error) {
/* 264 */     List errors = (List)this.failedViews.get(viewId);
/* 265 */     if (errors == null) {
/* 266 */       errors = new ArrayList();
/* 267 */       this.failedViews.put(viewId, errors);
/*     */     }
/* 269 */     errors.add(error);
/*     */   }
/*     */ 
/*     */   private final boolean failed(String viewId) throws PaloPersistenceException {
/* 273 */     if (this.failedViews.containsKey(viewId)) {
/* 274 */       List errors = (List)this.failedViews.get(viewId);
/* 275 */       this.failedViews.remove(viewId);
/* 276 */       throw new PaloPersistenceException(
/* 277 */         (PersistenceError[])errors.toArray(new PersistenceError[errors.size()]), 
/* 278 */         "Exception during cube loading");
/*     */     }
/* 280 */     return false;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.CubeViewStorageHandler
 * JD-Core Version:    0.5.4
 */