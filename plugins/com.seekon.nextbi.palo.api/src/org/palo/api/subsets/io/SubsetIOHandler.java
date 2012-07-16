/*     */ package org.palo.api.subsets.io;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Hierarchy;
import org.palo.api.PaloAPIException;
import org.palo.api.Subset;
import org.palo.api.exceptions.PaloIOException;
import org.palo.api.subsets.Subset2;
import org.palo.api.subsets.SubsetFilter;
import org.palo.api.subsets.impl.SubsetHandlerImpl;
import org.palo.api.subsets.impl.SubsetStorageHandlerImpl;
/*     */ 
/*     */ public class SubsetIOHandler extends SubsetStorageHandlerImpl
/*     */ {
/*     */   private static final String SUBSET_DIM = "#_SUBSET_";
/*     */   private static final String SUBSET_USER_DIM = "#_USER_";
/*     */   private static final String SUBSET_DIMENSION_DIM = "#_DIMENSION_";
/*     */   private static final String SUBSET_CUBE_LOCAL = "#_SUBSET_LOCAL";
/*     */   private static final String SUBSET_CUBE_GLOBAL = "#_SUBSET_GLOBAL";
/*     */   private final HashMap<Hierarchy, HashMap<String, SubsetCell>> localCells;
/*     */   private final HashMap<Hierarchy, HashMap<String, SubsetCell>> globalCells;
/*     */   private final Database database;
/*     */ 
/*     */   public SubsetIOHandler(Database database)
/*     */   {
/*  84 */     this.database = database;
/*  85 */     this.localCells = new LinkedHashMap();
/*  86 */     this.globalCells = new LinkedHashMap();
/*     */   }
/*     */ 
/*     */   public static final boolean isSubsetHierarchy(Hierarchy hierarchy)
/*     */   {
/*  96 */     return (hierarchy != null) && (hierarchy.getName().equals("#_SUBSET_"));
/*     */   }
/*     */ 
/*     */   public static final boolean isSubsetDimension(Dimension dimension) {
/* 100 */     return (dimension != null) && (dimension.getName().equals("#_SUBSET_"));
/*     */   }
/*     */ 
/*     */   public static final boolean supportsNewSubsets(Database database)
/*     */   {
/* 110 */     if (database.getConnection().getType() == 3) {
/* 111 */       return false;
/*     */     }
/*     */ 
/* 114 */     return (database.getCubeByName("#_SUBSET_LOCAL") != null) && 
/* 114 */       (database.getCubeByName("#_SUBSET_GLOBAL") != null);
/*     */   }
/*     */ 
/*     */   public final boolean canRead(int type) {
/* 118 */     Cube cube = getSubsetCube(type);
/* 119 */     return (cube != null) && (this.database.getRights().mayRead(cube));
/*     */   }
/*     */ 
/*     */   public final boolean canWrite(int type) {
/* 123 */     Cube cube = getSubsetCube(type);
/* 124 */     return (cube != null) && (this.database.getRights().mayWrite(cube));
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */   {
/* 129 */     this.localCells.clear();
/* 130 */     this.globalCells.clear();
/*     */   }
/*     */ 
/*     */   public final void convert(Subset[] legacySubsets, int type, boolean remove) {
/* 134 */     SubsetConverter transformer = new SubsetConverter();
/*     */     try {
/* 136 */       transformer.convert(legacySubsets, type, remove);
/*     */     } catch (PaloIOException e) {
/* 138 */       throw new PaloAPIException(
/* 139 */         "Errors during transform of legacy subsets!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final String getSubsetId(Hierarchy hierarchy, String name, int type) {
/* 144 */     String matchingId = null;
/* 145 */     HashMap<String, SubsetCell> cache = getCache(hierarchy, type);
/* 146 */     Hierarchy subsetHierarchy = this.database.getDimensionByName("#_SUBSET_")
/* 147 */       .getDefaultHierarchy();
/* 148 */     for (String id : cache.keySet()) {
/* 149 */       Element subset = subsetHierarchy.getElementById(id);
/* 150 */       if (subset.getName().equalsIgnoreCase(name)) {
/* 151 */         matchingId = id;
/* 152 */         break;
/*     */       }
/*     */     }
/* 155 */     return matchingId;
/*     */   }
/*     */ 
/*     */   protected String[] getSubsetIDs(Hierarchy hierarchy) {
/* 159 */     ArrayList subsetIDs = new ArrayList();
/* 160 */     HashMap cache = 
/* 161 */       getCache(hierarchy, 1);
/* 162 */     subsetIDs.addAll(cache.keySet());
/* 163 */     cache = getCache(hierarchy, 0);
/* 164 */     subsetIDs.addAll(cache.keySet());
/* 165 */     return (String[])subsetIDs.toArray(new String[subsetIDs.size()]);
/*     */   }
/*     */ 
/*     */   protected String[] getSubsetIDs(Hierarchy hierarchy, int type) {
/* 169 */     HashMap cache = getCache(hierarchy, type);
/* 170 */     return (String[])cache.keySet().toArray(new String[0]);
/*     */   }
/*     */ 
/*     */   protected String getSubsetName(String id)
/*     */   {
/* 175 */     Element subElement = this.database.getDimensionByName("#_SUBSET_")
/* 176 */       .getDefaultHierarchy().getElementById(id);
/* 177 */     if (subElement == null)
/* 178 */       return null;
/* 179 */     return subElement.getName();
/*     */   }
/*     */ 
/*     */   protected String[] getSubsetNames(Hierarchy hierarchy) {
/* 183 */     ArrayList names = new ArrayList();
/* 184 */     String[] subIDs = getSubsetIDs(hierarchy);
/* 185 */     for (String id : subIDs) {
/* 186 */       String name = getSubsetName(id);
/* 187 */       if (name != null)
/* 188 */         names.add(name);
/*     */     }
/* 190 */     return (String[])names.toArray(new String[names.size()]);
/*     */   }
/*     */   protected String[] getSubsetNames(Hierarchy hierarchy, int type) {
/* 193 */     ArrayList names = new ArrayList();
/* 194 */     String[] subIDs = getSubsetIDs(hierarchy, type);
/* 195 */     for (String id : subIDs) {
/* 196 */       String name = getSubsetName(id);
/* 197 */       if (name != null)
/* 198 */         names.add(name);
/*     */     }
/* 200 */     return (String[])names.toArray(new String[names.size()]);
/*     */   }
/*     */ 
/*     */   protected final boolean hasSubsets(Hierarchy hierarchy, int type) {
/* 204 */     HashMap cache = getCache(hierarchy, type);
/* 205 */     return !cache.isEmpty();
/*     */   }
/*     */ 
/*     */   protected Subset2 load(String id, Hierarchy hierarchy, int type, SubsetHandlerImpl handler) throws PaloIOException
/*     */   {
/* 210 */     Subset2 subset = null;
/* 211 */     HashMap cache = getCache(hierarchy, type);
/* 212 */     SubsetCell cell = (SubsetCell)cache.get(id);
/* 213 */     if (cell != null) {
/* 214 */       String xmlDef = cell.getXmlDef();
/*     */       try {
/* 216 */         subset = loadFromXML(handler, getSubsetName(id), xmlDef, type);
/*     */       }
/*     */       catch (IOException e) {
/* 219 */         throw new PaloIOException("failed to load subset '" + 
/* 220 */           getSubsetName(id) + "' for hierarchy '" + 
/* 221 */           hierarchy.getName() + "'!!", e);
/*     */       } catch (PaloIOException e) {
/* 223 */         throw new PaloIOException("failed to load subset '" + 
/* 224 */           getSubsetName(id) + "' for hierarchy '" + 
/* 225 */           hierarchy.getName() + "'!!", e);
/*     */       }
/*     */     }
/* 228 */     return subset;
/*     */   }
/*     */ 
/*     */   protected String newSubsetCell(String name, Hierarchy hierarchy, int type) throws PaloIOException
/*     */   {
/* 233 */     Hierarchy subsetHierarchy = this.database.getDimensionByName("#_SUBSET_")
/* 234 */       .getDefaultHierarchy();
/* 235 */     Element subElement = subsetHierarchy.getElementByName(name);
/* 236 */     if (subElement == null) {
/* 237 */       subElement = 
/* 238 */         subsetHierarchy.addElement(name, 1);
/*     */     }
/* 240 */     HashMap cache = getCache(hierarchy, type);
/* 241 */     String subsetId = subElement.getId();
/* 242 */     SubsetCell cell = (SubsetCell)cache.get(subsetId);
/* 243 */     if (cell != null) {
/* 244 */       String xmlDef = cell.getXmlDef();
/* 245 */       if ((xmlDef != null) && (xmlDef.length() > 0)) {
/* 246 */         throw new PaloIOException("A subset '" + name + 
/* 247 */           "' already exists in hierarchy '" + 
/* 248 */           hierarchy.getName() + "'!");
/*     */       }
/*     */     }
/* 251 */     cell = new SubsetCell(subsetId);
/* 252 */     Element[] coordinate = getCoordinate(subsetId, hierarchy, type);
/* 253 */     if (coordinate == null)
/* 254 */       return null;
/* 255 */     cell.setCoordinate(coordinate);
/*     */ 
/* 257 */     cache.put(cell.getSubsetId(), cell);
/* 258 */     return cell.getSubsetId();
/*     */   }
/*     */ 
/*     */   protected void remove(Subset2 subset) {
/* 262 */     int type = subset.getType();
/* 263 */     Hierarchy hierarchy = subset.getDimHierarchy();
/* 264 */     HashMap cache = getCache(hierarchy, type);
/* 265 */     SubsetCell cell = (SubsetCell)cache.get(subset.getId());
/* 266 */     if (cell != null) {
/* 267 */       Cube subCube = getSubsetCube(type);
/* 268 */       if (subCube == null)
/*     */       {
/*     */         return;
/*     */       }
/*     */ 
/* 277 */       subCube.setData(cell.getCoordinate(), "");
/* 278 */       cache.remove(cell.getSubsetId());
/* 279 */       removeCompletely(subset.getId());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void remove(String id, int type, Hierarchy hierarchy)
/*     */   {
/* 285 */     HashMap cache = getCache(hierarchy, type);
/* 286 */     SubsetCell cell = (SubsetCell)cache.get(id);
/* 287 */     if (cell != null) {
/* 288 */       Cube subCube = getSubsetCube(type);
/* 289 */       if (subCube == null)
/*     */         return;
/* 291 */       subCube.setData(cell.getCoordinate(), "");
/* 292 */       cache.remove(cell.getSubsetId());
/* 293 */       removeCompletely(id);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void rename(Subset2 subset, String newName)
/*     */   {
/* 300 */     Hierarchy subHierarchy = this.database.getDimensionByName("#_SUBSET_")
/* 301 */       .getDefaultHierarchy();
/* 302 */     Element subElement = subHierarchy.getElementById(subset.getId());
/* 303 */     subElement.rename(newName);
/*     */   }
/*     */ 
/*     */   protected void save(Subset2 subset) throws PaloIOException {
/* 307 */     validate(subset);
/* 308 */     Hierarchy hierarchy = subset.getDimHierarchy();
/* 309 */     HashMap cache = 
/* 310 */       getCache(hierarchy, subset.getType());
/* 311 */     SubsetCell cell = (SubsetCell)cache.get(subset.getId());
/* 312 */     if (cell == null)
/* 313 */       throw new PaloIOException("Subset saving failed!\nUnknown subset '" + 
/* 314 */         subset.getName() + "' for hierarchy '" + 
/* 315 */         hierarchy.getName() + "'!");
/*     */     try
/*     */     {
/* 318 */       Cube subCube = getSubsetCube(subset.getType());
/* 319 */       String xmlDef = storeToXML(subset);
/* 320 */       subCube.setData(cell.getCoordinate(), xmlDef);
/* 321 */       cell.setXmlDef(xmlDef);
/*     */     } catch (PaloIOException pex) {
/* 323 */       throw new PaloIOException("Could not store subset'" + 
/* 324 */         subset.getName() + "'!!", pex);
/*     */     } catch (PaloAPIException pex) {
/* 326 */       throw new PaloIOException("Could not store subset'" + 
/* 327 */         subset.getName() + "'!!", pex);
/*     */     } catch (IOException ioe) {
/* 329 */       throw new PaloIOException("Could not store subset'" + 
/* 330 */         subset.getName() + "'!!", ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String getDefinition(Subset2 subset) throws PaloIOException
/*     */   {
/* 336 */     validate(subset);
/* 337 */     Hierarchy hierarchy = subset.getDimHierarchy();
/* 338 */     HashMap cache = 
/* 339 */       getCache(hierarchy, subset.getType());
/* 340 */     SubsetCell cell = (SubsetCell)cache.get(subset.getId());
/* 341 */     if (cell == null)
/* 342 */       throw new PaloIOException("Subset saving failed!\nUnknown subset '" + 
/* 343 */         subset.getName() + "' for hierarchy '" + 
/* 344 */         hierarchy.getName() + "'!");
/*     */     try
/*     */     {
/* 347 */       return cell.getXmlDef();
/*     */     } catch (Exception e) {
/* 349 */       throw new PaloIOException("Could not read subset definition for subset '" + 
/* 350 */         subset.getName() + "'", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setDefinition(Subset2 subset, String xmlDefinition) throws PaloIOException {
/* 355 */     Hierarchy hierarchy = subset.getDimHierarchy();
/* 356 */     HashMap cache = 
/* 357 */       getCache(hierarchy, subset.getType());
/* 358 */     SubsetCell cell = (SubsetCell)cache.get(subset.getId());
/* 359 */     if (cell == null)
/* 360 */       throw new PaloIOException("Setting subset definition failed!\nUnknown subset '" + 
/* 361 */         subset.getName() + "' for hierarchy '" + 
/* 362 */         hierarchy.getName() + "'!");
/*     */     try
/*     */     {
/* 365 */       cell.setXmlDef(xmlDefinition);
/*     */     } catch (Exception e) {
/* 367 */       throw new PaloIOException("Could not write subset definition for subset '" + 
/* 368 */         subset.getName() + "'", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final Cube getSubsetCube(int type)
/*     */   {
/* 376 */     String cube = (type == 1) ? "#_SUBSET_GLOBAL" : 
/* 377 */       "#_SUBSET_LOCAL";
/* 378 */     return this.database.getCubeByName(cube);
/*     */   }
/*     */ 
/*     */   private final HashMap<String, SubsetCell> getCache(Hierarchy hierarchy, int type)
/*     */   {
/* 383 */     HashMap cache = 
/* 384 */       (type == 1) ? this.globalCells : this.localCells;
/* 385 */     HashMap cells = (HashMap)cache.get(hierarchy);
/* 386 */     if (cells == null) {
/* 387 */       cells = new LinkedHashMap();
/* 388 */       cache.put(hierarchy, cells);
/*     */ 
/* 390 */       fillCache(hierarchy, type, cells);
/*     */     }
/* 392 */     return cells;
/*     */   }
/*     */ 
/*     */   private final void fillCache(Hierarchy hierarchy, int type, HashMap<String, SubsetCell> cache)
/*     */   {
/* 397 */     int idIndex = (type == 1) ? 1 : 2;
/* 398 */     Cube subsets = getSubsetCube(type);
/* 399 */     Element[][] rowCoordinates = getRow(hierarchy, type);
/* 400 */     if ((rowCoordinates == null) || (rowCoordinates.length == 0))
/* 401 */       return;
/* 402 */     Object[] values = subsets.getDataBulk(rowCoordinates);
/* 403 */     for (int i = 0; i < values.length; ++i)
/* 404 */       if (values[i] != null) {
/* 405 */         String xmlDef = values[i].toString();
/* 406 */         if (xmlDef.length() <= 1)
/*     */           continue;
/* 408 */         SubsetCell cell = 
/* 409 */           new SubsetCell(rowCoordinates[i][idIndex].getId());
/* 410 */         cell.setCoordinate(rowCoordinates[i]);
/* 411 */         cell.setXmlDef(xmlDef);
/* 412 */         cache.put(cell.getSubsetId(), cell);
/*     */       }
/*     */   }
/*     */ 
/*     */   private final Subset2 loadFromXML(SubsetHandlerImpl handler, String name, String def, int type)
/*     */     throws IOException, PaloIOException
/*     */   {
/* 420 */     Subset2 subset = null;
/* 421 */     ByteArrayInputStream bin = new ByteArrayInputStream(
/* 422 */       def.getBytes("UTF-8"));
/*     */     try {
/* 424 */       subset = SubsetReader.getInstance().fromXML(handler, name, bin, 
/* 425 */         type);
/*     */     } finally {
/* 427 */       bin.close();
/*     */     }
/* 429 */     return subset;
/*     */   }
/*     */ 
/*     */   private final String storeToXML(Subset2 subset) throws IOException, PaloIOException
/*     */   {
/* 434 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */     try {
/* 436 */       SubsetWriter.getInstance().toXML(bout, subset);
/* 437 */       return bout.toString("UTF-8");
/*     */     } finally {
/* 439 */       bout.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void removeCompletely(String subId)
/*     */   {
/* 445 */     boolean canBeRemoved = true;
/*     */ 
/* 448 */     for (Dimension dim : this.database.getDimensions()) {
/* 449 */       if (dim.isSystemDimension()) {
/*     */         continue;
/*     */       }
/* 452 */       for (Hierarchy hierarchy : dim.getHierarchies()) {
/* 453 */         if (!hierarchy.isAttributeHierarchy()) {
/* 454 */           if (hierarchy.isSubsetHierarchy())
/*     */             continue;
/* 456 */           if ((!existsInCache(subId, hierarchy, 1)) && 
/* 457 */             (!existsInCache(subId, hierarchy, 
/* 458 */             0))) continue;
/* 459 */           canBeRemoved = false;
/* 460 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 464 */     if (canBeRemoved) {
/* 465 */       Hierarchy subHierarchy = this.database.getDimensionByName("#_SUBSET_")
/* 466 */         .getDefaultHierarchy();
/* 467 */       Element subElement = subHierarchy.getElementById(subId);
/* 468 */       if (subElement != null)
/* 469 */         subHierarchy.removeElement(subElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final boolean existsInCache(String subId, Hierarchy hierarchy, int type) {
/* 474 */     HashMap cache = getCache(hierarchy, type);
/* 475 */     SubsetCell cell = (SubsetCell)cache.get(subId);
/* 476 */     if (cell != null) {
/* 477 */       String xmlDef = cell.getXmlDef();
/* 478 */       return (xmlDef != null) && (xmlDef.length() > 0);
/*     */     }
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   private final Element[] getCoordinate(String id, Hierarchy hierarchy, int type) {
/* 484 */     Element subset = 
/* 485 */       this.database.getDimensionByName("#_SUBSET_").getDefaultHierarchy()
/* 486 */       .getElementById(id);
/* 487 */     Element dimElement = getHierarchy(hierarchy);
/* 488 */     if ((dimElement == null) && (hierarchy.getDimension().isSystemDimension()))
/* 489 */       return null;
/* 490 */     if (type == 1) {
/* 491 */       return new Element[] { dimElement, subset };
/*     */     }
/* 493 */     return new Element[] { dimElement, getUser(), subset };
/*     */   }
/*     */ 
/*     */   private final Element[][] getRow(Hierarchy hierarchy, int type) {
/* 497 */     Hierarchy dim = this.database.getDimensionByName("#_SUBSET_").getDefaultHierarchy();
/* 498 */     Element[] subsets = dim.getElements();
/* 499 */     Element dimElement = getHierarchy(hierarchy);
/* 500 */     if ((dimElement == null) || (hierarchy.getDimension().isSystemDimension()))
/* 501 */       return null;
/* 502 */     Element[][] coordinates = new Element[subsets.length][];
/* 503 */     if (type == 1) {
/* 504 */       for (int i = 0; i < subsets.length; ++i)
/* 505 */         coordinates[i] = new Element[]{ dimElement, subsets[i] };
/*     */     }
/*     */     else {
/* 508 */       Element usrElement = getUser();
/* 509 */       for (int i = 0; i < subsets.length; ++i) {
/* 510 */         coordinates[i] = new Element[]{ dimElement, usrElement, 
/* 511 */           subsets[i] };
/*     */       }
/*     */     }
/* 514 */     return coordinates;
/*     */   }
/*     */ 
/*     */   private final Element getHierarchy(Hierarchy hierarchy) {
/* 518 */     Hierarchy dimHierarchy = 
/* 519 */       this.database.getDimensionByName("#_DIMENSION_").getDefaultHierarchy();
/* 520 */     return dimHierarchy.getElementByName(hierarchy.getName());
/*     */   }
/*     */ 
/*     */   private final Element getUser() {
/* 524 */     Hierarchy usrHierarchy = this.database.getDimensionByName("#_USER_")
/* 525 */       .getDefaultHierarchy();
/* 526 */     return usrHierarchy.getElementByName(
/* 527 */       this.database.getConnection().getUsername());
/*     */   }
/*     */ 
/*     */   protected final void validate(Subset2 subset) throws PaloIOException
/*     */   {
/* 532 */     Hierarchy subsetHierarchy = this.database.getDimensionByName("#_SUBSET_")
/* 533 */       .getDefaultHierarchy();
/* 534 */     if (subsetHierarchy.getElementById(subset.getId()) == null)
/* 535 */       throw new PaloIOException("Subset(" + subset.getId() + ") in'" + 
/* 536 */         this.database.getName() + "' has unknown id!");
/* 537 */     if (subset.getDimHierarchy() == null)
/* 538 */       throw new PaloIOException("Subset(" + subset.getId() + ") in'" + 
/* 539 */         this.database.getName() + "' has no defined hierarchy!");
/* 540 */     for (SubsetFilter filter : subset.getFilters())
/* 541 */       filter.validateSettings();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.io.SubsetIOHandler
 * JD-Core Version:    0.5.4
 */