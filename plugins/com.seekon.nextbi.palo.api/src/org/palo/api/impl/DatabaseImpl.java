/*      */ package org.palo.api.impl;
/*      */ 
/*      */ import com.tensegrity.palojava.CubeInfo;
/*      */ import com.tensegrity.palojava.DatabaseInfo;
/*      */ import com.tensegrity.palojava.DbConnection;
/*      */ import com.tensegrity.palojava.DimensionInfo;
/*      */ import com.tensegrity.palojava.PaloException;
/*      */ import com.tensegrity.palojava.PropertyInfo;
/*      */ import com.tensegrity.palojava.loader.CubeLoader;
/*      */ import com.tensegrity.palojava.loader.DimensionLoader;
/*      */ import com.tensegrity.palojava.loader.PropertyLoader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import org.palo.api.Connection;
/*      */ import org.palo.api.ConnectionContext;
/*      */ import org.palo.api.ConnectionEvent;
/*      */ import org.palo.api.Cube;
/*      */ import org.palo.api.CubeView;
/*      */ import org.palo.api.Database;
/*      */ import org.palo.api.Dimension;
/*      */ import org.palo.api.PaloAPIException;
/*      */ import org.palo.api.Property2;
/*      */ import org.palo.api.Rights;
/*      */ import org.palo.api.Subset;
/*      */ import org.palo.api.VirtualCubeDefinition;
/*      */ import org.palo.api.impl.views.CubeViewManager;
/*      */ import org.palo.api.subsets.io.SubsetIOHandler;
/*      */ 
/*      */ public class DatabaseImpl extends AbstractPaloObject
/*      */   implements Database
/*      */ {
/*      */   private final DatabaseInfo dbInfo;
/*      */   private final ConnectionImpl connection;
/*      */   private final DbConnection dbConnection;
/*      */   private final LinkedHashSet<Cube> virtualCubes;
/*      */   private final Map<String, DimensionImpl> loadedDimensions;
/*      */   private final Map<String, CubeImpl> loadedCubes;
/*      */   private final CompoundKey key;
/*      */   private final SubsetStorageHandler legacySubsetHandler;
/*      */   private final CubeViewStorageHandler viewStorageHandler;
/*      */   private final CubeLoader cubeLoader;
/*      */   private final DimensionLoader dimLoader;
/*      */   private final PropertyLoader propertyLoader;
/*      */   private final Map<String, Property2Impl> loadedProperties;
/*      */   private final SubsetIOHandler subsetIOHandler;
/*      */   private boolean batchMode;
/*      */   private boolean isSystem;
/*      */ 
/*      */   static final DatabaseImpl create(ConnectionImpl connection, DatabaseInfo dbInfo, boolean doEvents)
/*      */   {
/*   85 */     return new DatabaseImpl(connection, dbInfo);
/*      */   }
/*      */ 
/*      */   private DatabaseImpl(ConnectionImpl connection, DatabaseInfo dbInfo)
/*      */   {
/*  118 */     this.dbInfo = dbInfo;
/*  119 */     this.connection = connection;
/*  120 */     this.dbConnection = connection.getConnectionInternal();
/*  121 */     this.virtualCubes = new LinkedHashSet();
/*      */ 
/*  123 */     this.loadedDimensions = new LinkedHashMap();
/*      */ 
/*  127 */     this.loadedCubes = new LinkedHashMap();
/*      */ 
/*  131 */     this.cubeLoader = this.dbConnection.getCubeLoader(dbInfo);
/*  132 */     this.dimLoader = this.dbConnection.getDimensionLoader(dbInfo);
/*      */ 
/*  136 */     this.legacySubsetHandler = 
/*  137 */       new SubsetStorageHandler(this);
/*  138 */     this.viewStorageHandler = new CubeViewStorageHandler(this);
/*      */ 
/*  140 */     this.subsetIOHandler = new SubsetIOHandler(this);
/*      */ 
/*  142 */     this.key = 
/*  143 */       new CompoundKey(new Object[] { DatabaseImpl.class, 
/*  143 */       connection, dbInfo.getId() });
/*  144 */     this.isSystem = ((dbInfo.getType() == 1) || 
/*  145 */       (dbInfo.getName().equals("AdvancedSystem")));
/*  146 */     this.loadedProperties = new HashMap();
/*  147 */     this.propertyLoader = this.dbConnection.getTypedPropertyLoader(dbInfo);
/*      */ 
/*  150 */     init();
/*      */   }
/*      */ 
/*      */   public final Cube addCube(String name, Dimension[] dimensions) {
/*  154 */     return addCubeInternal(name, dimensions, 2);
/*      */   }
/*      */ 
/*      */   public final Cube addUserInfoCube(String name, Dimension[] dimensions) {
/*  158 */     return addCubeInternal(name, dimensions, 16);
/*      */   }
/*      */ 
/*      */   public final Cube addCube(VirtualCubeDefinition definition)
/*      */   {
/*  164 */     VirtualCubeImpl impl = new VirtualCubeImpl(definition);
/*      */ 
/*  166 */     if (this.virtualCubes.contains(impl))
/*  167 */       this.virtualCubes.remove(impl);
/*  168 */     this.virtualCubes.add(impl);
/*  169 */     fireCubesAdded(new Cube[] { impl });
/*      */ 
/*  174 */     return impl;
/*      */   }
/*      */ 
/*      */   public final Dimension addDimension(String name) {
/*  178 */     return addDimensionInternal(name, 2);
/*      */   }
/*      */   public final Dimension addUserInfoDimension(String name) {
/*  181 */     return addDimensionInternal(name, 16);
/*      */   }
/*      */ 
/*      */   public final synchronized void endBatchUpdate() {
/*  185 */     this.batchMode = false;
/*  186 */     reloadInternal(true);
/*      */   }
/*      */ 
/*      */   public final Connection getConnection() {
/*  190 */     return this.connection;
/*      */   }
/*      */ 
/*      */   public final Cube getCubeAt(int index) {
/*  194 */     CubeInfo cube = this.cubeLoader.load(index);
/*  195 */     return getCube(cube);
/*      */   }
/*      */ 
/*      */   public final Cube getCubeByName(String name)
/*      */   {
/*  216 */     if (name.indexOf("@@") > 0)
/*      */     {
/*  218 */       for (Cube cube : this.virtualCubes)
/*  219 */         if (cube.getName().equalsIgnoreCase(name))
/*  220 */           return cube;
/*      */     }
/*      */     else {
/*  223 */       CubeInfo cube = this.cubeLoader.loadByName(name);
/*  224 */       return getCube(cube);
/*      */     }
/*  226 */     return null;
/*      */   }
/*      */ 
/*      */   public final Cube getCubeById(String id)
/*      */   {
/*      */     try
/*      */     {
/*  240 */       CubeInfo cube = this.cubeLoader.load(id);
/*  241 */       return getCube(cube);
/*      */     }
/*      */     catch (PaloException localPaloException) {
/*      */     }
/*  245 */     return null;
/*      */   }
/*      */ 
/*      */   public final int getCubeCount()
/*      */   {
/*  252 */     return this.dbInfo.getCubeCount();
/*      */   }
/*      */ 
/*      */   public final Cube[] getCubes() {
/*  256 */     String[] cubeIds = this.cubeLoader.getAllCubeIds();
/*  257 */     return loadCubes(cubeIds);
/*      */   }
/*      */ 
/*      */   public final Cube[] getCubes(int typeMask)
/*      */   {
/*  279 */     String[] cubeIds = this.cubeLoader.getCubeIds(typeMask);
/*  280 */     return loadCubes(cubeIds);
/*      */   }
/*      */ 
/*      */   final Cube[] getCubes(Dimension dimension) {
/*  284 */     if (!(dimension instanceof DimensionImpl))
/*  285 */       return new Cube[0];
/*  286 */     DimensionInfo dimInfo = ((DimensionImpl)dimension).getInfo();
/*  287 */     String[] cubeIds = this.cubeLoader.getCubeIds(dimInfo);
/*  288 */     return loadCubes(cubeIds);
/*      */   }
/*      */ 
/*      */   public final Dimension getDimensionAt(int index)
/*      */   {
/*  300 */     DimensionInfo dim = this.dimLoader.load(index);
/*  301 */     return getDimension(dim);
/*      */   }
/*      */ 
/*      */   public final Dimension getDimensionByName(String name)
/*      */   {
/*  323 */     DimensionInfo dim = this.dimLoader.loadByName(name);
/*  324 */     return getDimension(dim);
/*      */   }
/*      */ 
/*      */   public final Dimension getDimensionById(String id)
/*      */   {
/*      */     try
/*      */     {
/*  337 */       DimensionInfo dim = this.dimLoader.load(id);
/*  338 */       return getDimension(dim);
/*      */     }
/*      */     catch (PaloException localPaloException) {
/*      */     }
/*  342 */     return null;
/*      */   }
/*      */ 
/*      */   public final int getDimensionCount()
/*      */   {
/*  349 */     return this.dbInfo.getDimensionCount();
/*      */   }
/*      */ 
/*      */   public final Dimension[] getDimensions() {
/*  353 */     String[] ids = this.dimLoader.getAllDimensionIds();
/*  354 */     ArrayList dims = new ArrayList();
/*  355 */     for (String id : ids) {
/*  356 */       DimensionInfo info = this.dimLoader.load(id);
/*  357 */       Dimension dim = getDimension(info);
/*  358 */       if (dim != null)
/*  359 */         dims.add(dim);
/*      */     }
/*  361 */     return (Dimension[])dims.toArray(new Dimension[dims.size()]);
/*      */   }
/*      */ 
/*      */   public final Dimension[] getDimensions(int typeMask)
/*      */   {
/*  375 */     String[] ids = this.dimLoader.getDimensionIds(typeMask);
/*  376 */     ArrayList dims = new ArrayList();
/*  377 */     for (String id : ids) {
/*  378 */       DimensionInfo info = this.dimLoader.load(id);
/*  379 */       Dimension dim = getDimension(info);
/*  380 */       if (dim != null)
/*  381 */         dims.add(dim);
/*      */     }
/*  383 */     return (Dimension[])dims.toArray(new Dimension[dims.size()]);
/*      */   }
/*      */ 
/*      */   public final String getName() {
/*  387 */     return this.dbInfo.getName();
/*      */   }
/*      */ 
/*      */   private Database findPaloDatabase()
/*      */   {
/*  399 */     Connection[] cons = ((ConnectionFactoryImpl)
/*  400 */       ConnectionFactoryImpl.getInstance()).getActiveConnections();
/*  401 */     int length = cons.length;
/*  402 */     if (length == 0) {
/*  403 */       return null;
/*      */     }
/*  405 */     for (int i = 0; i < length; ++i) {
/*  406 */       if ((cons[i].getDatabaseCount() > 0) && 
/*  407 */         (cons[i].getDatabaseAt(0).getCubeCount() > 0)) {
/*  408 */         return cons[i].getDatabaseAt(0);
/*      */       }
/*      */     }
/*      */ 
/*  412 */     return null;
/*      */   }
/*      */ 
/*      */   public final String parseRule(Cube cube, String definition, String functions) {
/*      */     try {
/*  417 */       CubeImpl _cube = (CubeImpl)cube;
/*  418 */       String rule = "";
/*  419 */       if (this.connection.getType() == 3) {
/*  420 */         Database paloDatabase = findPaloDatabase();
/*  421 */         if (paloDatabase == null) {
/*  422 */           return "";
/*      */         }
/*      */ 
/*  428 */         definition = this.dbConnection.parseRule(
/*  429 */           _cube.getInfo(), definition, functions);
/*      */ 
/*  438 */         rule = paloDatabase.parseRule(paloDatabase.getCubeAt(0), 
/*  439 */           definition, functions.toLowerCase());
/*      */       } else {
/*  441 */         rule = this.dbConnection.parseRule(_cube.getInfo(), definition, functions);
/*      */       }
/*  443 */       return rule;
/*      */     } catch (PaloException pex) {
/*  445 */       throw new PaloAPIException(pex);
/*      */     } catch (RuntimeException ex) {
/*  447 */       throw new PaloAPIException(ex.getLocalizedMessage(), ex);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final Rights getRights() {
/*  452 */     return this.connection.getContext().getRights();
/*      */   }
/*      */ 
/*      */   public final void reload()
/*      */   {
/*  457 */     resetAllLoaders();
/*      */ 
/*  460 */     if (ConnectionImpl.WPALO)
/*  461 */       clearCache();
/*      */     else
/*  463 */       reloadInternal(true);
/*      */   }
/*      */ 
/*      */   public final void removeCube(Cube cube) {
/*  467 */     if (!this.loadedCubes.containsKey(cube.getId())) {
/*  468 */       return;
/*      */     }
/*      */ 
/*  471 */     CubeView[] views = cube.getCubeViews();
/*  472 */     for (int i = 0; i < views.length; ++i) {
/*  473 */       cube.removeCubeView(views[i]);
/*      */     }
/*  475 */     if (cube.getExtendedType() == 1) {
/*  476 */       this.loadedCubes.remove(cube.getId());
/*  477 */       this.virtualCubes.remove(cube);
/*      */     } else {
/*  479 */       CubeImpl _cube = (CubeImpl)cube;
/*  480 */       if (this.cubeLoader.delete(_cube.getInfo())) {
/*  481 */         this.loadedCubes.remove(cube.getId());
/*      */       }
/*      */ 
/*  484 */       this.dbConnection.reload(this.dbInfo);
/*  485 */       fireCubesRemoved(new Cube[] { cube });
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void removeDimension(Dimension dimension) {
/*  490 */     if (!this.loadedDimensions.containsKey(dimension.getId())) {
/*  491 */       return;
/*      */     }
/*  493 */     DimensionImpl _dimension = (DimensionImpl)dimension;
/*      */ 
/*  517 */     if (!this.dimLoader.delete(_dimension.getInfo()))
/*      */       return;
/*  519 */     Subset[] subsets = dimension.getSubsets();
/*  520 */     for (int i = 0; i < subsets.length; ++i) {
/*  521 */       dimension.removeSubset(subsets[i]);
/*      */     }
/*      */ 
/*  524 */     Dimension attrDim = dimension.getAttributeDimension();
/*  525 */     if (attrDim != null) {
/*  526 */       this.loadedDimensions.remove(attrDim.getId());
/*  527 */       this.dimLoader.removed(attrDim.getId());
/*      */     }
/*  529 */     Cube attrCube = dimension.getAttributeCube();
/*  530 */     if (attrCube != null) {
/*  531 */       this.loadedCubes.remove(attrCube.getId());
/*  532 */       this.cubeLoader.removed(attrCube.getId());
/*      */     }
/*      */ 
/*  536 */     this.loadedDimensions.remove(dimension.getId());
/*      */ 
/*  539 */     this.dbConnection.reload(this.dbInfo);
/*      */ 
/*  541 */     fireDimensionsRemoved(new Dimension[] { dimension, attrDim });
/*      */   }
/*      */ 
/*      */   public final void rename(String newName)
/*      */   {
/*  547 */     String oldName = getName();
/*  548 */     if (newName.equals(oldName))
/*  549 */       return;
/*      */     try {
/*  551 */       this.dbConnection.rename(this.dbInfo, newName);
/*      */ 
/*  553 */       fireDatabaseRenamed(new Database[] { this }, oldName);
/*      */     } catch (PaloException pex) {
/*  555 */       throw new PaloAPIException(pex.getMessage(), pex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void fireDatabaseRenamed(Object[] dbs, String oldName) {
/*  559 */     ConnectionEvent ev = new ConnectionEvent(this.connection, null, 
/*  560 */       22, 
/*  561 */       dbs);
/*  562 */     ev.oldValue = oldName;
/*  563 */     this.connection.fireEvent(ev);
/*      */   }
/*      */ 
/*      */   public final boolean save() {
/*  567 */     return this.dbConnection.save(this.dbInfo);
/*      */   }
/*      */ 
/*      */   public final synchronized void startBatchUpdate() {
/*  571 */     this.batchMode = true;
/*      */   }
/*      */ 
/*      */   public final String getId() {
/*  575 */     return this.dbInfo.getId();
/*      */   }
/*      */ 
/*      */   public final DatabaseInfo getInfo() {
/*  579 */     return this.dbInfo;
/*      */   }
/*      */ 
/*      */   public final boolean isSystem() {
/*  583 */     return this.isSystem;
/*      */   }
/*      */ 
/*      */   public final boolean equals(Object other)
/*      */   {
/*  591 */     if (other instanceof DatabaseImpl) {
/*  592 */       return this.key.equals(((DatabaseImpl)other).key);
/*      */     }
/*  594 */     return false;
/*      */   }
/*      */ 
/*      */   public final int hashCode() {
/*  598 */     return this.key.hashCode();
/*      */   }
/*      */ 
/*      */   public final org.palo.api.subsets.SubsetStorageHandler getSubsetStorageHandler() {
/*  602 */     return this.subsetIOHandler;
/*      */   }
/*      */ 
/*      */   public final boolean supportsNewSubsets() {
/*  606 */     return SubsetIOHandler.supportsNewSubsets(this);
/*      */   }
/*      */ 
/*      */   public final int getType()
/*      */   {
/*  611 */     return getType(this.dbInfo);
/*      */   }
/*      */ 
/*      */   final boolean isSubsetDimension(Dimension dimension)
/*      */   {
/*  618 */     return SubsetIOHandler.isSubsetDimension(dimension);
/*      */   }
/*      */ 
/*      */   final void isSystem(boolean b) {
/*  622 */     this.isSystem = b;
/*      */   }
/*      */ 
/*      */   final void init(boolean doEvents)
/*      */   {
/*  630 */     reloadInternal(doEvents);
/*      */ 
/*  633 */     if (this.dbInfo.getType() == 1) {
/*  634 */       return;
/*      */     }
/*  636 */     if ((getConnection().getType() != 2) && 
/*  637 */       (getConnection().getType() != 1))
/*      */     {
/*      */       return;
/*      */     }
/*      */ 
/*  643 */     this.viewStorageHandler.initStorage();
/*      */   }
/*      */ 
/*      */   final SubsetStorageHandler getLegacySubsetHandler()
/*      */   {
/*  660 */     return this.legacySubsetHandler;
/*      */   }
/*      */ 
/*      */   final CubeViewStorageHandler getViewStorageHandler() {
/*  664 */     return this.viewStorageHandler;
/*      */   }
/*      */ 
/*      */   public final void clearCache()
/*      */   {
/*  670 */     for (DimensionImpl dimension : this.loadedDimensions.values()) {
/*  671 */       dimension.clearCache();
/*      */     }
/*  673 */     this.loadedDimensions.clear();
/*  674 */     this.dimLoader.reset();
/*      */ 
/*  682 */     for (CubeImpl cube : this.loadedCubes.values()) {
/*  683 */       cube.clearCache();
/*      */     }
/*  685 */     this.loadedCubes.clear();
/*  686 */     this.cubeLoader.reset();
/*      */ 
/*  688 */     for (Property2Impl property : this.loadedProperties.values()) {
/*  689 */       property.clearCache();
/*      */     }
/*  691 */     this.loadedProperties.clear();
/*  692 */     this.propertyLoader.reset();
/*      */ 
/*  695 */     this.subsetIOHandler.reset();
/*      */   }
/*      */ 
/*      */   public final void init()
/*      */   {
/*  712 */     if (isSystem()) {
/*  713 */       return;
/*      */     }
/*  715 */     if (getConnection().getType() != 3) {
/*  716 */       CubeViewManager.getInstance().init(this);
/*      */     }
/*  718 */     this.viewStorageHandler.initStorage();
/*      */   }
/*      */ 
/*      */   private final void reloadInternal(boolean doEvents)
/*      */   {
/*  744 */     synchronized (this) {
/*  745 */       if (this.batchMode) {
/*  746 */         return;
/*      */       }
/*      */     }
/*  749 */     this.legacySubsetHandler.reload();
/*  750 */     this.subsetIOHandler.reset();
/*  751 */     this.viewStorageHandler.reload();
/*  752 */     reloadDatabase(doEvents);
/*      */   }
/*      */ 
/*      */   private final void reloadDatabase(boolean doEvents)
/*      */   {
/*  802 */     LinkedHashMap oldDimensions = 
/*  803 */       new LinkedHashMap(this.loadedDimensions);
/*  804 */     LinkedHashMap oldCubes = 
/*  805 */       new LinkedHashMap(this.loadedCubes);
/*      */ 
/*  808 */     this.dbConnection.reload(this.dbInfo);
/*      */ 
/*  814 */     this.loadedDimensions.clear();
/*  815 */     this.dimLoader.reset();
/*      */ 
/*  828 */     String[] dimIDs = this.dimLoader.getAllDimensionIds();
/*      */     DimensionImpl dim;
/*  829 */     for (String id : dimIDs) {
/*  830 */       dim = (DimensionImpl)oldDimensions.get(id);
/*  831 */       if (dim == null) {
/*  832 */         DimensionInfo info = this.dimLoader.load(id);
/*  833 */         dim = createDimension(info, doEvents);
/*      */       } else {
/*  835 */         this.loadedDimensions.put(id, dim);
/*      */       }
/*  837 */       dim.reload(doEvents);
/*      */     }
/*      */ 
/*  842 */     this.loadedCubes.clear();
/*  843 */     this.cubeLoader.reset();
/*      */ 
/*  854 */     String[] cubeIDs = this.cubeLoader.getAllCubeIds();
/*  855 */     for (String id : cubeIDs) {
/*  856 */       CubeImpl cube = (CubeImpl)oldCubes.get(id);
/*  857 */       if (cube == null) {
/*  858 */         CubeInfo info = this.cubeLoader.load(id);
/*  859 */         cube = createCube(info, doEvents);
/*      */       } else {
/*  861 */         this.loadedCubes.put(id, cube);
/*      */       }
/*  863 */       cube.reload(doEvents);
/*      */     }
/*      */ 
/*  882 */     if (!doEvents)
/*      */       return;
/*  884 */     LinkedHashSet removedDimensions = new LinkedHashSet(
/*  885 */       oldDimensions.values());
/*  886 */     removedDimensions.removeAll(this.loadedDimensions.values());
/*  887 */     if (removedDimensions.size() > 0) {
/*  888 */       fireDimensionsRemoved(removedDimensions.toArray());
/*      */     }
/*      */ 
/*  891 */     LinkedHashSet addedDimensions = 
/*  892 */       new LinkedHashSet(this.loadedDimensions.values());
/*  893 */     addedDimensions.removeAll(oldDimensions.values());
/*  894 */     if (addedDimensions.size() > 0) {
/*  895 */       fireDimensionsAdded(addedDimensions.toArray());
/*      */     }
/*      */ 
/*  898 */     LinkedHashSet removedCubes = new LinkedHashSet(oldCubes.values());
/*  899 */     removedCubes.removeAll(this.loadedCubes.values());
/*  900 */     if (removedCubes.size() > 0) {
/*  901 */       fireCubesRemoved(removedCubes.toArray());
/*      */     }
/*      */ 
/*  904 */     LinkedHashSet addedCubes = new LinkedHashSet(this.loadedCubes.values());
/*  905 */     addedCubes.removeAll(oldCubes.values());
/*  906 */     if (addedCubes.size() > 0)
/*  907 */       fireCubesAdded(addedCubes.toArray());
/*      */   }
/*      */ 
/*      */   private final void fireDimensionsAdded(Object[] dimensions)
/*      */   {
/*  913 */     this.connection.fireEvent(
/*  916 */       new ConnectionEvent(getConnection(), this, 
/*  915 */       2, 
/*  916 */       dimensions));
/*      */   }
/*      */   private final void fireDimensionsRemoved(Object[] dimensions) {
/*  919 */     this.connection.fireEvent(
/*  922 */       new ConnectionEvent(getConnection(), this, 
/*  921 */       3, 
/*  922 */       dimensions));
/*      */   }
/*      */ 
/*      */   private final void fireCubesAdded(Object[] cubes) {
/*  926 */     this.connection.fireEvent(
/*  929 */       new ConnectionEvent(getConnection(), this, 
/*  928 */       9, 
/*  929 */       cubes));
/*      */   }
/*      */   private final void fireCubesRemoved(Object[] cubes) {
/*  932 */     this.connection.fireEvent(
/*  935 */       new ConnectionEvent(getConnection(), this, 
/*  934 */       10, 
/*  935 */       cubes));
/*      */   }
/*      */ 
/*      */   private final CubeImpl getCube(CubeInfo cubeInfo) {
/*  939 */     if (cubeInfo == null)
/*  940 */       return null;
/*  941 */     CubeImpl cube = (CubeImpl)this.loadedCubes.get(cubeInfo.getId());
/*  942 */     if (cube == null)
/*      */     {
/*  944 */       cube = createCube(cubeInfo, true);
/*      */     }
/*  946 */     return cube;
/*      */   }
/*      */ 
/*      */   private final CubeImpl createCube(CubeInfo cubeInfo, boolean fireEvent) {
/*  950 */     CubeImpl cube = CubeImpl.create(this.connection, this, cubeInfo);
/*  951 */     this.loadedCubes.put(cube.getId(), cube);
/*  952 */     return cube;
/*      */   }
/*      */ 
/*      */   private final DimensionImpl getDimension(DimensionInfo dimInfo)
/*      */   {
/*  968 */     if (dimInfo == null) {
/*  969 */       return null;
/*      */     }
/*  971 */     DimensionImpl dimension = (DimensionImpl)this.loadedDimensions.get(dimInfo.getId());
/*  972 */     if (dimension == null)
/*      */     {
/*  974 */       dimension = createDimension(dimInfo, true);
/*      */     }
/*  976 */     return dimension;
/*      */   }
/*      */ 
/*      */   private final DimensionImpl createDimension(DimensionInfo dimInfo, boolean fireEvent)
/*      */   {
/*  986 */     DimensionImpl dimension = DimensionImpl.create(this.connection, this, dimInfo, fireEvent);
/*  987 */     this.loadedDimensions.put(dimension.getId(), dimension);
/*  988 */     return dimension;
/*      */   }
/*      */ 
/*      */   public String[] getAllPropertyIds()
/*      */   {
/* 1087 */     return this.propertyLoader.getAllPropertyIds();
/*      */   }
/*      */ 
/*      */   public Property2 getProperty(String id) {
/* 1091 */     PropertyInfo propInfo = this.propertyLoader.load(id);
/* 1092 */     if (propInfo == null) {
/* 1093 */       return null;
/*      */     }
/* 1095 */     Property2 property = (Property2)this.loadedProperties.get(propInfo.getId());
/* 1096 */     if (property == null) {
/* 1097 */       property = createProperty(propInfo);
/*      */     }
/*      */ 
/* 1100 */     return property;
/*      */   }
/*      */ 
/*      */   public void addProperty(Property2 property) {
/* 1104 */     if (property == null) {
/* 1105 */       return;
/*      */     }
/* 1107 */     Property2Impl _property = (Property2Impl)property;
/* 1108 */     this.propertyLoader.loaded(_property.getPropInfo());
/* 1109 */     this.loadedProperties.put(_property.getId(), _property);
/*      */   }
/*      */ 
/*      */   public void removeProperty(String id) {
/* 1113 */     Property2 property = getProperty(id);
/* 1114 */     if (property == null) {
/* 1115 */       return;
/*      */     }
/* 1117 */     if (property.isReadOnly()) {
/* 1118 */       return;
/*      */     }
/* 1120 */     this.loadedProperties.remove(property);
/*      */   }
/*      */ 
/*      */   private void createProperty(Property2 parent, PropertyInfo kid) {
/* 1124 */     Property2 p2Kid = Property2Impl.create(parent, kid);
/* 1125 */     parent.addChild(p2Kid);
/* 1126 */     for (PropertyInfo kidd : kid.getChildren())
/* 1127 */       createProperty(p2Kid, kidd);
/*      */   }
/*      */ 
/*      */   private Property2 createProperty(PropertyInfo propInfo)
/*      */   {
/* 1132 */     Property2 prop = Property2Impl.create(null, propInfo);
/* 1133 */     for (PropertyInfo kid : propInfo.getChildren()) {
/* 1134 */       createProperty(prop, kid);
/*      */     }
/* 1136 */     return prop;
/*      */   }
/*      */ 
/*      */   private final Cube[] loadCubes(String[] cubeIds) {
/* 1140 */     ArrayList cubes = new ArrayList();
/* 1141 */     for (String id : cubeIds) {
/* 1142 */       CubeInfo info = this.cubeLoader.load(id);
/* 1143 */       Cube cube = getCube(info);
/* 1144 */       if (cube != null)
/* 1145 */         cubes.add(cube);
/*      */     }
/* 1147 */     return (Cube[])cubes.toArray(new Cube[cubes.size()]);
/*      */   }
/*      */ 
/*      */   public boolean canBeModified()
/*      */   {
/* 1153 */     return this.dbInfo.canBeModified();
/*      */   }
/*      */ 
/*      */   public boolean canCreateChildren() {
/* 1157 */     return this.dbInfo.canCreateChildren();
/*      */   }
/*      */ 
/*      */   private final Cube addCubeInternal(String name, Dimension[] dimensions, int type) {
/*      */     try {
/* 1162 */       if ((dimensions == null) || (name == null)) {
/* 1163 */         return null;
/*      */       }
/* 1165 */       int infoType = getInfoType(type);
/* 1166 */       if (infoType == -1) {
/* 1167 */         infoType = 0;
/*      */       }
/* 1169 */       DimensionInfo[] dimInfos = new DimensionInfo[dimensions.length];
/* 1170 */       for (int i = 0; i < dimInfos.length; ++i) {
/* 1171 */         dimInfos[i] = ((DimensionImpl)dimensions[i]).getInfo();
/*      */       }
/*      */ 
/* 1174 */       CubeInfo cubeInfo = this.cubeLoader.create(name, dimInfos, infoType);
/*      */ 
/* 1176 */       Cube cube = createCube(cubeInfo, true);
/*      */ 
/* 1178 */       this.dbConnection.reload(this.dbInfo);
/*      */ 
/* 1180 */       fireCubesAdded(new Cube[] { cube });
/*      */ 
/* 1182 */       return cube;
/*      */     }
/*      */     catch (PaloException pex)
/*      */     {
/* 1193 */       throw new PaloAPIException(pex.getMessage(), pex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final Dimension addDimensionInternal(String name, int type) {
/*      */     try {
/* 1199 */       int infoType = getInfoType(type);
/* 1200 */       if (infoType == -1) {
/* 1201 */         infoType = 0;
/*      */       }
/* 1203 */       DimensionInfo dimInfo = this.dimLoader.create(name, infoType);
/* 1204 */       Dimension dimension = createDimension(dimInfo, true);
/*      */ 
/* 1207 */       DimensionInfo attrDimInfo = 
/* 1208 */         this.dbConnection.getAttributeDimension(dimInfo);
/* 1209 */       if (attrDimInfo != null) {
/* 1210 */         this.dimLoader.loaded(attrDimInfo);
/* 1211 */         createDimension(attrDimInfo, false);
/*      */       }
/* 1213 */       CubeInfo attrCubeInfo = this.dbConnection.getAttributeCube(dimInfo);
/* 1214 */       if (attrCubeInfo != null) {
/* 1215 */         createCube(attrCubeInfo, false);
/* 1216 */         this.cubeLoader.loaded(attrCubeInfo);
/*      */       }
/*      */ 
/* 1219 */       this.dbConnection.reload(this.dbInfo);
/* 1220 */       fireDimensionsAdded(new Dimension[] { dimension });
/* 1221 */       return dimension;
/*      */     } catch (PaloException pex) {
/* 1223 */       throw new PaloAPIException(pex.getMessage(), pex);
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void resetAllLoaders() {
/* 1228 */     this.dimLoader.reset();
/* 1229 */     this.cubeLoader.reset();
/* 1230 */     this.propertyLoader.reset();
/*      */   }
/*      */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.DatabaseImpl
 * JD-Core Version:    0.5.4
 */