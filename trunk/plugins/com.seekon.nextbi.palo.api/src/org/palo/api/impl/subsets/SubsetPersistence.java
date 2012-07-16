/*     */ package org.palo.api.impl.subsets;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.Subset;
/*     */ 
/*     */ public class SubsetPersistence
/*     */ {
/*     */   private static final String SYSTEM_PREFIX = "#";
/*     */   static final String DIMENSION_SUBSET_COLUMNS = "#subsetcolumns";
/*     */   static final String DIMENSION_SUBSET_ROWS = "#subsetrows";
/*     */   static final String CUBE_SUBSETS = "#subsets";
/*     */   static final String PATH_DELIM = ":";
/*     */   static final String ELEMENT_DELIM = ",";
/*     */   static final String COL_NAME = "Name";
/*     */   static final String COL_DEF = "Def";
/*  77 */   private static SubsetPersistence instance = new SubsetPersistence();
/*     */ 
/*  79 */   public static final SubsetPersistence getInstance() { return instance; }
/*     */ 
/*     */ 
/*     */   public final boolean isSubsetCube(Cube cube)
/*     */   {
/*  89 */     String cubeName = cube.getName();
/*  90 */     return cubeName.equals("#subsets");
/*     */   }
/*     */ 
/*     */   public final boolean isSubsetDimension(Dimension dimension) {
/*  94 */     String dimName = dimension.getName();
/*     */ 
/*  96 */     return (dimName.equals("#subsetcolumns")) || 
/*  96 */       (dimName.equals("#subsetrows"));
/*     */   }
/*     */ 
/*     */   public final boolean hasSubsets(Database database)
/*     */   {
/* 101 */     Hierarchy subsets = getSubsetRows(database);
/* 102 */     Hierarchy columns = getSubsetColumns(database);
/* 103 */     Cube cube = getSubsetCube(database);
/* 104 */     if ((subsets != null) && (columns != null) && (cube != null)) {
/* 105 */       return subsets.getElementCount() > 0;
/*     */     }
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   public final boolean delete(Subset subset) {
/*     */     try {
/* 112 */       return deleteInternal(subset);
/*     */     } catch (Exception e) {
/* 114 */       System.err.println("SubsetPersistence.delete: " + e);
/*     */     }
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   public final Subset[] loadAll(Database database) {
/* 120 */     if (!hasSubsets(database)) {
/* 121 */       return new Subset[0];
/*     */     }
/* 123 */     return loadBulky(database);
/*     */   }
/*     */ 
/*     */   final void save(Subset subset)
/*     */   {
/*     */     try
/*     */     {
/* 146 */       saveInternal(subset);
/*     */     } catch (Exception e) {
/* 148 */       throw new PaloAPIException("Could not save subset '" + subset.getName() + "'!", e);
/*     */     }
/*     */   }
/*     */ 
/*     */   final String[] getIDs(Database db)
/*     */   {
/* 172 */     Dimension subsets = db.getDimensionByName("#subsetrows");
/* 173 */     if (subsets == null)
/* 174 */       return new String[0];
/* 175 */     Hierarchy subsetHier = subsets.getDefaultHierarchy();
/* 176 */     Element[] elements = subsetHier.getElements();
/* 177 */     String[] ids = new String[elements.length];
/* 178 */     for (int i = 0; i < elements.length; ++i)
/* 179 */       ids[i] = elements[i].getName();
/* 180 */     return ids;
/*     */   }
/*     */ 
/*     */   private boolean deleteInternal(Subset subset)
/*     */     throws Exception
/*     */   {
/* 213 */     if (subset == null) {
/* 214 */       return true;
/*     */     }
/* 216 */     Hierarchy hier = subset.getHierarchy();
/*     */ 
/* 218 */     if (hier == null) {
/* 219 */       return false;
/*     */     }
/* 221 */     Database database = hier.getDimension().getDatabase();
/*     */ 
/* 224 */     if (database.getDimensionByName("#subsetcolumns") != null)
/*     */     {
/*     */       Dimension rows;
/* 226 */       if ((rows = database.getDimensionByName("#subsetrows")) != null)
/*     */       {
/*     */         Cube cube;
/* 228 */         if ((cube = database.getCubeByName("#subsets")) != null)
/*     */         {
/* 230 */           Hierarchy rowHier = rows.getDefaultHierarchy();
/*     */ 
/* 232 */           Element[] elements = rowHier.getElements();
/* 233 */           for (int i = 0; i < elements.length; ++i) {
/* 234 */             Element element = elements[i];
/* 235 */             String xmlDef = cube.getData(
/* 236 */               new String[] { "Def", element.getName() }).toString();
/* 237 */             ByteArrayInputStream bin = new ByteArrayInputStream(
/* 238 */               xmlDef.getBytes("UTF-8"));
/* 239 */             Subset xmlSub = SubsetReader.getInstance().fromXML(bin, 
/* 240 */               element.getName(), database);
/* 241 */             if (xmlSub != null) {
/* 242 */               Hierarchy xmlDim = xmlSub.getHierarchy();
/* 243 */               Hierarchy subDim = subset.getHierarchy();
/*     */ 
/* 245 */               if ((!xmlSub.getName().equals(subset.getName())) || 
/* 246 */                 (!xmlDim.getName().equals(subDim.getName()))) continue;
/* 247 */               rowHier.removeElement(element);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 251 */     return true;
/*     */   }
/*     */ 
/*     */   private void saveInternal(Subset subset) throws Exception {
/* 255 */     Database database = subset.getDimension().getDatabase();
/*     */     Dimension columns;
/* 258 */     if ((columns = database.getDimensionByName("#subsetcolumns")) == null) {
/* 259 */       columns = database.addDimension("#subsetcolumns");
/*     */     }
/* 261 */     Hierarchy colHier = columns.getDefaultHierarchy();
/* 262 */     if (colHier.getElementByName("Def") == null)
/* 263 */       colHier.addElement("Def", 1);
/*     */     Dimension rows;
/* 267 */     if ((rows = database.getDimensionByName("#subsetrows")) == null) {
/* 268 */       rows = database.addDimension("#subsetrows");
/*     */     }
/* 270 */     Hierarchy rowHier = rows.getDefaultHierarchy();
/* 271 */     String key = subset.getId();
/* 272 */     Element element = rowHier.getElementByName(key);
/* 273 */     if (element == null)
/* 274 */       element = rowHier.addElement(key, 1);
/*     */     Cube cube;
/* 277 */     if ((cube = database.getCubeByName("#subsets")) == null) {
/* 278 */       cube = database.addCube("#subsets", new Dimension[] { columns, 
/* 279 */         rows });
/*     */     }
/*     */ 
/* 282 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*     */     try {
/* 284 */       SubsetWriter.getInstance().toXML(bout, subset);
/*     */     } finally {
/* 286 */       bout.close();
/*     */     }
/* 288 */     cube.setData(new String[] { "Def", element.getName() }, 
/* 289 */       bout.toString("UTF-8"));
/*     */   }
/*     */ 
/*     */   private final Hierarchy getSubsetColumns(Database database)
/*     */   {
/* 294 */     Dimension columns = database.getDimensionByName("#subsetcolumns");
/* 295 */     Hierarchy colHier = null;
/* 296 */     if (columns != null) {
/* 297 */       colHier = columns.getDefaultHierarchy();
/*     */ 
/* 299 */       Element nameColumn = colHier.getElementByName("Name");
/* 300 */       if (nameColumn != null)
/* 301 */         colHier.removeElement(nameColumn);
/*     */     }
/* 303 */     return colHier;
/*     */   }
/*     */ 
/*     */   private final Hierarchy getSubsetRows(Database database) {
/* 307 */     Dimension dim = database.getDimensionByName("#subsetrows");
/* 308 */     if (dim != null) {
/* 309 */       return dim.getDefaultHierarchy();
/*     */     }
/* 311 */     return null;
/*     */   }
/*     */ 
/*     */   private final Cube getSubsetCube(Database database) {
/* 315 */     return database.getCubeByName("#subsets");
/*     */   }
/*     */ 
/*     */   private final Subset loadSubset(Database database, Cube subsetCube, Element subsetElement)
/*     */     throws IOException
/*     */   {
/* 321 */     Subset subset = null;
/* 322 */     String def = subsetCube.getData(
/* 323 */       new String[] { "Def", subsetElement.getName() }).toString();
/*     */ 
/* 326 */     if ((def.equals("-")) || (def.equals(""))) {
/* 327 */       return subset;
/*     */     }
/* 329 */     ByteArrayInputStream bin = new ByteArrayInputStream(
/* 330 */       def.getBytes("UTF-8"));
/*     */     try {
/* 332 */       subset = SubsetReader.getInstance().fromXML(bin, 
/* 333 */         subsetElement.getName(), database);
/*     */     } finally {
/* 335 */       bin.close();
/*     */     }
/* 337 */     return subset;
/*     */   }
/*     */ 
/*     */   private final Set getSubsetIds(Map dimId2subsetId, Dimension dimension) {
/* 341 */     Set subsets = (Set)dimId2subsetId.get(dimension.getId());
/* 342 */     if (subsets == null) {
/* 343 */       subsets = new LinkedHashSet();
/* 344 */       dimId2subsetId.put(dimension.getId(), subsets);
/*     */     }
/* 346 */     return subsets;
/*     */   }
/*     */ 
/*     */   private final Subset[] loadBulky(Database database) {
/* 350 */     Cube cube = getSubsetCube(database);
/* 351 */     Hierarchy rows = getSubsetRows(database);
/* 352 */     Hierarchy cols = getSubsetColumns(database);
/*     */ 
/* 354 */     if ((cols == null) || (rows == null) || (cube == null)) {
/* 355 */       return new Subset[0];
/*     */     }
/* 357 */     Element[] elements = rows.getElements();
/* 358 */     Element col = cols.getElementByName("Def");
/*     */ 
/* 360 */     Element[][] coordinates = new Element[elements.length][];
/* 361 */     for (int i = 0; i < elements.length; ++i) {
/* 362 */       coordinates[i] = new Element[]{ col, elements[i] };
/*     */     }
/*     */ 
/* 365 */     Object[] values = cube.getDataBulk(coordinates);
/*     */ 
/* 368 */     ArrayList subsets = new ArrayList(elements.length);
/* 369 */     for (int i = 0; i < values.length; ++i) {
/*     */       try {
/* 371 */         if (values[i] != null) {
/* 372 */           Subset subset = loadFromDefinition(values[i].toString(), 
/* 373 */             elements[i].getName(), database);
/* 374 */           if (subset != null)
/* 375 */             subsets.add(subset);
/*     */         }
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 380 */         System.err.println("Failed to load subset with id " + 
/* 381 */           elements[i].getName());
/*     */       }
/*     */     }
/* 384 */     return (Subset[])subsets.toArray(new Subset[subsets.size()]);
/*     */   }
/*     */ 
/*     */   private final Subset loadFromDefinition(String def, String subId, Database database) throws IOException
/*     */   {
/* 389 */     Subset subset = null;
/*     */ 
/* 392 */     if ((def.equals("-")) || (def.equals(""))) {
/* 393 */       return subset;
/*     */     }
/* 395 */     ByteArrayInputStream bin = 
/* 396 */       new ByteArrayInputStream(def.getBytes("UTF-8"));
/*     */     try {
/* 398 */       subset = SubsetReader.getInstance().fromXML(bin, subId, database);
/*     */     } finally {
/* 400 */       bin.close();
/*     */     }
/* 402 */     return subset;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.subsets.SubsetPersistence
 * JD-Core Version:    0.5.4
 */