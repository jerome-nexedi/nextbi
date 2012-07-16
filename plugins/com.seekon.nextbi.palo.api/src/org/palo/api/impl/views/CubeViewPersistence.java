/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import com.tensegrity.palo.xmla.ext.views.SQLConnection;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Connection;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.exceptions.PaloIOException;
/*     */ import org.palo.api.impl.PersistenceErrorImpl;
/*     */ import org.palo.api.persistence.PaloPersistenceException;
/*     */ import org.palo.api.persistence.PersistenceError;
/*     */ 
/*     */ class CubeViewPersistence
/*     */ {
/*     */   private static final String SYSTEM_PREFIX = "#";
/*     */   static final String LEGACY_DIMENSION_VIEW_COLUMNS = "#viewcolumns";
/*     */   static final String LEGACY_DIMENSION_VIEW_ROWS = "#viewrows";
/*     */   static final String LEGACY_CUBE_VIEWS = "#views";
/*     */   private static final String USER_INFO_PREFIX = "##";
/*     */   private static final String DIMENSION_VIEW_COLUMNS = "##view_columns";
/*     */   private static final String DIMENSION_VIEW_ROWS = "##view_rows";
/*     */   private static final String CUBE_VIEWS = "##view_cubes";
/*     */   static final String COL_DEF = "Def";
/*     */   static final String PATH_DELIMETER = "///";
/*     */   static final String DELIMITER = ",";
/*     */   static final String DIM_HIER_DELIMITER = "~~~";
/*     */   static final String GROUP_DELIMITER = ":";
/*  99 */   private static CubeViewPersistence instance = new CubeViewPersistence();
/*     */ 
/* 101 */   public static CubeViewPersistence getInstance() { return instance; }
/*     */ 
/*     */ 
/*     */   final boolean isViewsCube(Cube cube)
/*     */   {
/* 111 */     String name = cube.getName();
/* 112 */     return (name.equals("##view_cubes")) || (name.equals("#views"));
/*     */   }
/*     */ 
/*     */   final void save(CubeView view) {
/*     */     try {
/* 117 */       saveInternal(view);
/*     */     } catch (Exception e) {
/* 119 */       throw new PaloAPIException("Could not save view '" + view.getName() + "'!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   final Object load(Database database, String id) {
/*     */     try {
/* 125 */       ArrayList errors = new ArrayList();
/*     */ 
/* 127 */       Cube viewsCube = database.getCubeByName("#views");
/* 128 */       return loadCubeView(database, viewsCube, id, errors);
/*     */     } catch (Exception e) {
/* 130 */       System.err.println("CubeViewPersistence.load: " + e);
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */ 
/*     */   final String getRawDefinition(CubeView view) {
/* 136 */     String def = "";
/* 137 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */     try {
/* 139 */       CubeViewWriter.getInstance().toXML(bout, view);
/*     */       try {
/* 141 */         def = bout.toString("UTF-8"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */       }
/*     */     } finally {
/*     */       try {
/* 145 */         bout.close(); } catch (IOException localIOException) {
/*     */       }
/*     */     }
/* 148 */     return def;
/*     */   }
/*     */ 
/*     */   final String[] getIDs(Database db) {
/* 152 */     Cube viewsCube = db.getCubeByName("#views");
/* 153 */     if (viewsCube == null) {
/* 154 */       return new String[0];
/*     */     }
/* 156 */     Dimension viewIds = viewsCube.getDimensionByName("#viewrows");
/* 157 */     Element[] elViewIds = viewIds.getDefaultHierarchy().getElements();
/* 158 */     String[] ids = new String[elViewIds.length];
/* 159 */     for (int i = 0; i < elViewIds.length; ++i)
/* 160 */       ids[i] = elViewIds[i].getName();
/* 161 */     return ids;
/*     */   }
/*     */ 
/*     */   final boolean delete(CubeView view) {
/*     */     try {
/* 166 */       deleteInternal(view);
/* 167 */       return true;
/*     */     } catch (Exception e) {
/* 169 */       System.err.println("CubeQueryPersistence.delete: " + e);
/*     */     }
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   private final void deleteInternal(CubeView view) {
/* 175 */     Database database = view.getCube().getDatabase();
/* 176 */     if (database.getConnection().getType() == 3) {
/* 177 */       String host = database.getConnection().getServer();
/* 178 */       String service = database.getConnection().getService();
/* 179 */       String user = database.getConnection().getUsername();
/* 180 */       SQLConnection sqlCon = new SQLConnection();
/*     */       try {
/* 182 */         sqlCon.deleteView(host, service, user, database.getId(), 
/* 183 */           view.getId());
/*     */       } finally {
/* 185 */         sqlCon.close();
/*     */       }
/* 187 */       return;
/*     */     }
/* 189 */     deleteFrom("#viewrows", view.getId(), database);
/*     */   }
/*     */ 
/*     */   private final void saveInternal(CubeView view) throws Exception
/*     */   {
/* 194 */     if (view.getCube().getDatabase().getConnection().getType() == 3)
/*     */     {
/* 196 */       ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */       try {
/* 198 */         CubeViewWriter.getInstance().toXML(bout, view);
/*     */       } finally {
/* 200 */         bout.close();
/*     */       }
/* 202 */       SQLConnection sqlCon = new SQLConnection();
/*     */       try {
/* 204 */         Database database = view.getCube().getDatabase();
/* 205 */         String host = database.getConnection().getServer();
/* 206 */         String service = database.getConnection().getService();
/* 207 */         String user = database.getConnection().getUsername();
/*     */ 
/* 209 */         if (sqlCon.writeView(host, service, user, 
/* 210 */           view.getCube().getDatabase().getId(), view.getId(), 
/* 211 */           bout.toString("UTF-8"))) 
/* 212 */         System.err.println("Error when writing XMLA view...");
/*     */       }
/*     */       finally {
/* 215 */         sqlCon.close();
/*     */       }
/* 217 */       return;
/*     */     }
/*     */ 
/* 221 */     Cube viewsCube = 
/* 222 */       view.getCube().getDatabase().getCubeByName("#views");
/* 223 */     if (viewsCube == null) {
/* 224 */       CubeViewManager.getInstance().createEnvironment(view.getCube().getDatabase());
/* 225 */       viewsCube = 
/* 226 */         view.getCube().getDatabase().getCubeByName("#views");
/* 227 */       if (viewsCube == null) {
/* 228 */         throw new PaloIOException("Cannot save view '" + view.getName() + 
/* 229 */           "'!! No cube for storing views exists!");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 234 */     Dimension[] viewsDims = viewsCube.getDimensions();
/*     */ 
/* 236 */     int rowIndex = 
/* 237 */       (viewsDims[0].getName().equals("#viewrows")) ? 0 : 1;
/*     */ 
/* 239 */     int colIndex = (rowIndex == 0) ? 1 : 0;
/* 240 */     Element col = 
/* 241 */       viewsDims[colIndex].getDefaultHierarchy().getElementByName("Def");
/*     */ 
/* 244 */     Element idElement = viewsDims[rowIndex].getDefaultHierarchy()
/* 245 */       .getElementByName(view.getId());
/* 246 */     if (idElement == null) {
/* 247 */       idElement = viewsDims[rowIndex].getDefaultHierarchy().addElement(
/* 248 */         view.getId(), 1);
/*     */     }
/*     */ 
/* 251 */     Element[] coordinate = new Element[2];
/* 252 */     coordinate[rowIndex] = idElement;
/* 253 */     coordinate[colIndex] = col;
/*     */ 
/* 255 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */     try {
/* 257 */       CubeViewWriter.getInstance().toXML(bout, view);
/*     */     } finally {
/* 259 */       bout.close();
/*     */     }
/*     */ 
/* 262 */     viewsCube.setData(coordinate, bout.toString("UTF-8"));
/*     */   }
/*     */ 
/*     */   private final CubeView loadCubeView(Database database, Cube viewsCube, String viewId, Collection errors)
/*     */     throws IOException
/*     */   {
/* 268 */     if (viewsCube == null) {
/* 269 */       errors.add(
/* 273 */         new PersistenceErrorImpl("No cube for views handling found!", viewId, 
/* 271 */         null, database, viewId, 
/* 272 */         32, null, 
/* 273 */         0));
/* 274 */       return null;
/*     */     }
/* 276 */     CubeView view = null;
/* 277 */     String viewDef = null;
/* 278 */     Element[] coordinate = getViewCoordinate(viewsCube, viewId);
/*     */     try {
/* 280 */       viewDef = viewsCube.getData(coordinate).toString();
/*     */     }
/*     */     catch (PaloAPIException localPaloAPIException)
/*     */     {
/*     */     }
/* 285 */     if (viewDef == null) {
/* 286 */       PersistenceError error = new PersistenceErrorImpl(
/* 287 */         "Failed to load cube view", viewId, null, 
/* 288 */         database, viewId, 
/* 289 */         32, null, 
/* 290 */         0);
/* 291 */       errors.add(error);
/* 292 */       return null;
/*     */     }
/*     */ 
/* 295 */     ByteArrayInputStream bin = new ByteArrayInputStream(
/* 296 */       viewDef.getBytes("UTF-8"));
/*     */     try {
/* 298 */       view = CubeViewReader.getInstance().fromXML(bin, viewId, database, 
/* 299 */         errors);
/*     */     }
/*     */     finally
/*     */     {
/* 303 */       bin.close();
/*     */     }
/* 305 */     return view;
/*     */   }
/*     */ 
/*     */   private final CubeView loadCubeViewXMLA(SQLConnection sqlCon, String id, Database db) throws IOException
/*     */   {
/* 310 */     CubeView view = null;
/* 311 */     String viewDef = null;
/*     */     try {
/* 313 */       String host = db.getConnection().getServer();
/* 314 */       String service = db.getConnection().getService();
/* 315 */       String user = db.getConnection().getUsername();
/* 316 */       viewDef = sqlCon.loadView(host, service, user, db.getId(), id);
/*     */     }
/*     */     catch (PaloAPIException localPaloAPIException)
/*     */     {
/*     */     }
/* 321 */     if (viewDef == null) {
/* 322 */       return null;
/*     */     }
/*     */ 
/* 325 */     ByteArrayInputStream bin = new ByteArrayInputStream(
/* 326 */       viewDef.getBytes("UTF-8"));
/*     */     try {
/* 328 */       view = CubeViewReader.getInstance().fromXML(bin, 
/* 329 */         id, db, new HashSet());
/*     */     } finally {
/* 331 */       bin.close();
/*     */     }
/* 333 */     return view;
/*     */   }
/*     */ 
/*     */   final void load(Database database, Map cubeId2viewId, Map views) throws PaloPersistenceException
/*     */   {
/* 338 */     if (database.getConnection().getType() == 3) {
/* 339 */       SQLConnection sqlCon = new SQLConnection();
/*     */       try {
/* 341 */         String host = database.getConnection().getServer();
/* 342 */         String service = database.getConnection().getService();
/* 343 */         String user = database.getConnection().getUsername();
/* 344 */         String[] viewIds = sqlCon.getAllViewIds(
/* 345 */           host, service, user, database.getId());
/* 346 */         for (int i = 0; i < viewIds.length; ++i) {
/* 347 */           CubeView view = null;
/*     */           try {
/* 349 */             view = loadCubeViewXMLA(sqlCon, viewIds[i], database);
/*     */           } catch (IOException localIOException) {
/*     */           }
/* 352 */           if ((view == null) || 
/* 353 */             (view.getCube() == null)) continue;
/* 354 */           Set _viewIds = getViewIds(cubeId2viewId, view.getCube());
/* 355 */           _viewIds.add(view.getId());
/* 356 */           views.put(view.getId(), view);
/*     */         }
/*     */       }
/*     */       finally
/*     */       {
/* 361 */         sqlCon.close();
/*     */       }
/* 363 */       return;
/*     */     }
/*     */ 
/* 366 */     ArrayList errors = new ArrayList();
/*     */ 
/* 370 */     loadViewsFrom("#views", "#viewrows", database, 
/* 371 */       cubeId2viewId, views, errors, false);
/* 372 */     loadViewsFrom("##view_cubes", "##view_rows", database, cubeId2viewId, 
/* 373 */       views, errors, true);
/*     */ 
/* 378 */     if (!errors.isEmpty())
/* 379 */       throw new PaloPersistenceException(
/* 380 */         (PersistenceError[])errors.toArray(new PersistenceError[errors.size()]), 
/* 381 */         "Errors during loading of cube views!!");
/*     */   }
/*     */ 
/*     */   private final Set getViewIds(Map cubeId2viewId, Cube cube) {
/* 385 */     Set views = (Set)cubeId2viewId.get(cube.getId());
/* 386 */     if (views == null) {
/* 387 */       views = new LinkedHashSet();
/* 388 */       cubeId2viewId.put(cube.getId(), views);
/*     */     }
/* 390 */     return views;
/*     */   }
/*     */ 
/*     */   private final void deleteFrom(String views_dim, String elName, Database db) {
/* 394 */     Dimension rows = db.getDimensionByName(views_dim);
/* 395 */     if (rows != null) {
/* 396 */       Element viewId = rows.getDefaultHierarchy()
/* 397 */         .getElementByName(elName);
/* 398 */       if (viewId != null)
/* 399 */         rows.getDefaultHierarchy().removeElement(viewId);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final Element[] getViewCoordinate(Cube viewsCube, String viewId) {
/* 404 */     if (viewsCube == null) {
/* 405 */       return null;
/*     */     }
/* 407 */     Dimension[] viewsDims = viewsCube.getDimensions();
/*     */ 
/* 409 */     int rowIndex = (viewsDims[0].getName().equals(
/* 410 */       "##view_rows")) ? 
/* 410 */       0 : 1;
/* 411 */     int colIndex = (rowIndex == 0) ? 1 : 0;
/*     */ 
/* 413 */     Element def = 
/* 414 */       viewsDims[colIndex].getDefaultHierarchy().getElementByName("Def");
/* 415 */     Element view = 
/* 416 */       viewsDims[rowIndex].getDefaultHierarchy().getElementByName(viewId);
/* 417 */     Element[] coordinate = new Element[2];
/* 418 */     coordinate[rowIndex] = view;
/* 419 */     coordinate[colIndex] = def;
/* 420 */     return coordinate;
/*     */   }
/*     */ 
/*     */   private final void loadViewsFrom(String cubeName, String rows, Database db, Map<String, String> cubeId2viewId, Map<String, CubeView> views, Collection errors, boolean transformed)
/*     */     throws PaloPersistenceException
/*     */   {
/* 427 */     Cube viewsCube = db.getCubeByName(cubeName);
/* 428 */     if (viewsCube == null) {
/* 429 */       return;
/*     */     }
/*     */ 
/* 432 */     Dimension[] viewsDims = viewsCube.getDimensions();
/*     */ 
/* 434 */     int rowIndex = (viewsDims[0].getName().equals(rows)) ? 0 : 1;
/* 435 */     int colIndex = (rowIndex == 0) ? 1 : 0;
/*     */ 
/* 437 */     Element col = 
/* 438 */       viewsDims[colIndex].getDefaultHierarchy().getElementByName("Def");
/* 439 */     Element[] viewIds = 
/* 440 */       viewsDims[rowIndex].getDefaultHierarchy().getElements();
/*     */ 
/* 442 */     Element[][] coordinates = new Element[viewIds.length][];
/* 443 */     for (int i = 0; i < viewIds.length; ++i) {
/* 444 */       coordinates[i] = new Element[2];
/* 445 */       coordinates[i][rowIndex] = viewIds[i];
/* 446 */       coordinates[i][colIndex] = col;
/*     */     }
/*     */ 
/* 450 */     Object[] values = viewsCube.getDataBulk(coordinates);
/*     */ 
/* 452 */     Set<CubeView> transformedViews = new HashSet();
/*     */     Set _viewIds;
/*     */     PersistenceError error;
/* 453 */     for (int i = 0; i < values.length; ++i) {
/* 454 */       boolean addError = false;
/* 455 */       if (values[i] != null) {
/* 456 */         String viewDef = values[i].toString();
/* 457 */         if (!viewDef.equals(""))
/*     */           try {
/* 459 */             CubeView view = loadViewFromDefinition(viewDef, 
/* 460 */               viewIds[i].getName(), db, errors);
/* 461 */             if (view != null)
/* 462 */               if (view.getCube() != null) {
/* 463 */                 _viewIds = getViewIds(cubeId2viewId, 
/* 464 */                   view.getCube());
/* 465 */                 _viewIds.add(view.getId());
/* 466 */                 views.put(view.getId(), view);
/* 467 */                 transformedViews.add(view);
/*     */               }
/*     */             else
/* 470 */               addError = true;
/*     */           } catch (IOException ex) {
/* 472 */             System.err.println("Failed to cube view: " + 
/* 473 */               ex.getMessage());
/*     */           }
/*     */         else
/* 476 */           addError = true;
/*     */       } else {
/* 478 */         addError = true;
/*     */       }
/*     */ 
/* 482 */       if (addError) {
/* 483 */         error = new PersistenceErrorImpl(
/* 484 */           "Failed to load cube view", viewIds[i].getName(), null, 
/* 485 */           db, viewIds[i].getName(), 
/* 486 */           32, null, 
/* 487 */           0);
/* 488 */         errors.add(error);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 494 */     if (transformed) {
/* 495 */       Hierarchy rowHierarchy = viewsDims[rowIndex].getDefaultHierarchy();
/*     */ 
/* 497 */       for (CubeView view : transformedViews) {
/* 498 */         view.save();
/*     */ 
/* 500 */         Element viewEl = rowHierarchy.getElementByName(view.getId());
/* 501 */         if (viewEl != null) {
/* 502 */           rowHierarchy.removeElement(viewEl);
/*     */         }
/*     */       }
/* 505 */       if (rowHierarchy.getElementCount() != 0)
/*     */         return;
/* 507 */       db.removeCube(viewsCube);
/* 508 */       //Element viewEl = (_viewIds = viewsDims).length; 
								for (int error1 = 0; error1 < viewsDims.length; ++error1) { 
									Dimension dim = viewsDims[error1];
/* 509 */         db.removeDimension(dim); 
									}
/*     */     }
/*     */     else {
/* 512 */       transformedViews.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final CubeView loadViewFromDefinition(String xmlDef, String viewId, Database db, Collection errors) throws IOException {
/* 517 */     CubeView view = null;
/* 518 */     ByteArrayInputStream bin = new ByteArrayInputStream(
/* 519 */       xmlDef.getBytes("UTF-8"));
/*     */     try {
/* 521 */       view = CubeViewReader.getInstance().fromXML(bin, viewId, db, 
/* 522 */         errors);
/*     */     } finally {
/* 524 */       bin.close();
/*     */     }
/* 526 */     return view;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewPersistence
 * JD-Core Version:    0.5.4
 */