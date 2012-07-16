/*      */package org.palo.api.impl;

/*      */
/*      */import com.tensegrity.palojava.CellInfo; /*      */
import com.tensegrity.palojava.CubeInfo; /*      */
import com.tensegrity.palojava.DatabaseInfo; /*      */
import com.tensegrity.palojava.DbConnection; /*      */
import com.tensegrity.palojava.ElementInfo; /*      */
import com.tensegrity.palojava.LockInfo; /*      */
import com.tensegrity.palojava.PaloException; /*      */
import com.tensegrity.palojava.PropertyInfo; /*      */
import com.tensegrity.palojava.RuleInfo; /*      */
import com.tensegrity.palojava.impl.CubeInfoImpl; /*      */
import com.tensegrity.palojava.loader.PropertyLoader; /*      */
import com.tensegrity.palojava.loader.RuleLoader; /*      */
import java.math.BigInteger; /*      */
import java.text.DecimalFormat; /*      */
import java.text.NumberFormat; /*      */
import java.text.ParsePosition; /*      */
import java.util.ArrayList; /*      */
import java.util.Collection; /*      */
import java.util.HashMap; /*      */
import java.util.HashSet; /*      */
import java.util.LinkedHashMap; /*      */
import java.util.List; /*      */
import java.util.Map; /*      */
import org.palo.api.Cell; /*      */
import org.palo.api.ConnectionEvent; /*      */
import org.palo.api.Cube; /*      */
import org.palo.api.CubeView; /*      */
import org.palo.api.Database; /*      */
import org.palo.api.Dimension; /*      */
import org.palo.api.Element; /*      */
import org.palo.api.ExportContext; /*      */
import org.palo.api.ExportDataset; /*      */
import org.palo.api.Hierarchy; /*      */
import org.palo.api.Lock; /*      */
import org.palo.api.PaloAPIException; /*      */
import org.palo.api.Property; /*      */
import org.palo.api.Property2; /*      */
import org.palo.api.Rule; /*      */
import org.palo.api.exceptions.PaloIOException; /*      */
import org.palo.api.persistence.PaloPersistenceException; /*      */
import org.palo.api.persistence.PersistenceError; /*      */
import org.palo.api.persistence.PersistenceObserver;

/*      */
/*      */class CubeImpl extends AbstractPaloObject
/*      */implements Cube
/*      */{
  /* 137 */private boolean tryReloadingDims = true;

  /*      */private Dimension[] dimensions;

  /*      */private ExportContext exportContext;

  /*      */private final DbConnection dbConnection;

  /*      */private final ConnectionImpl connection;

  /*      */private final CubeInfo cubeInfo;

  /*      */private final Map<String, RuleImpl> loadedRules;

  /*      */private final Map<String, Property2Impl> loadedProperties;

  /*      */private final Database database;

  /*      */private final CompoundKey key;

  /*      */private final List viewObservers;

  /*      */private final CubeViewStorageHandler viewStorageHandler;

  /*      */private final RuleLoader ruleLoader;

  /*      */private final PropertyLoader propertyLoader;

  /*      */
  /*      */static final CubeImpl create(ConnectionImpl connection,
    Database database, CubeInfo cubeInfo)
  /*      */{
    /* 130 */return create(connection, database, cubeInfo, null);
    /*      */}

  /*      */
  /*      */static final CubeImpl create(ConnectionImpl connection,
    Database database, CubeInfo cubeInfo, Dimension[] dimensions) {
    /* 134 */return new CubeImpl(connection, database, cubeInfo, dimensions);
    /*      */}

  /*      */
  /*      */private CubeImpl(ConnectionImpl connection, Database database,
    CubeInfo cubeInfo, Dimension[] dimensions)
  /*      */{
    /* 174 */this.cubeInfo = cubeInfo;
    /* 175 */this.dimensions = dimensions;
    /* 176 */this.connection = connection;
    /* 177 */this.dbConnection = connection.getConnectionInternal();
    /*      */
    /* 180 */this.loadedRules = new LinkedHashMap();
    /* 181 */this.loadedProperties = new LinkedHashMap();
    /* 182 */this.viewObservers = new ArrayList();
    /* 183 */this.database = database;
    /*      */
    /* 185 */this.ruleLoader = this.dbConnection.getRuleLoader(cubeInfo);
    /* 186 */this.viewStorageHandler = ((DatabaseImpl) database)
      .getViewStorageHandler();
    /* 187 */this.key =
    /* 188 */new CompoundKey(new Object[] { CubeImpl.class, connection,
    /* 188 */cubeInfo.getDatabase().getId(), cubeInfo.getId() });
    /* 189 */this.propertyLoader = this.dbConnection
      .getTypedPropertyLoader(cubeInfo);
    /*      */}

  /*      */
  /*      */public final CubeView addCubeView(String id, String name,
    Property[] properties)
  /*      */{
    /* 199 */return this.viewStorageHandler.addCubeView(this, id, name, properties);
    /*      */}

  /*      */
  /*      */public final CubeView addCubeView(String name, Property[] properties)
  /*      */{
    /* 210 */return this.viewStorageHandler.addCubeView(this, name, properties);
    /*      */}

  /*      */
  /*      */public final void commitLog()
  /*      */{
    /* 221 */this.dbConnection.save(this.cubeInfo);
    /*      */}

  /*      */
  /*      */public final CubeView getCubeView(String id) throws PaloIOException {
    /*      */try {
      /* 226 */return this.viewStorageHandler.getCubeView(this, id);
      /*      */}
    /*      */catch (PaloPersistenceException e)
    /*      */{
      /* 231 */PaloIOException pio = new PaloIOException(
        "CubeView loading failed!", e);
      /* 232 */PersistenceError[] errors = e.getErrors();
      /* 233 */for (PersistenceError err : errors) {
        /* 234 */Object src = err.getSource();
        /* 235 */if (src instanceof CubeView)
          /* 236 */pio.setData(src);
        /*      */}
      /* 238 */throw pio;
      /*      */}
    /*      */}

  /*      */
  /*      */public final String[] getCubeViewIds()
  /*      */{
    /* 258 */return this.viewStorageHandler.getViewIds(this);
    /*      */}

  /*      */
  /*      */public final String getCubeViewName(String id) {
    /* 262 */return this.viewStorageHandler.getViewName(id);
    /*      */}

  /*      */
  /*      */public final void getCubeViews(PersistenceObserver observer) {
    /* 266 */String[] ids = this.viewStorageHandler.getViewIds(this);
    /* 267 */for (int i = 0; i < ids.length; ++i)
      /*      */try {
        /* 269 */CubeView view = this.viewStorageHandler.getCubeView(this, ids[i]);
        /* 270 */observer.loadComplete(view);
        /*      */}
      /*      */catch (PaloPersistenceException pex) {
        /* 273 */if (pex.getType() == 1) {
          /* 274 */PersistenceError[] errors = pex.getErrors();
          /* 275 */Object view = (errors.length > 0) ? errors[0].getSource() : null;
          /* 276 */observer.loadIncomplete(view, errors);
          /*      */} else {
          /* 278 */observer.loadFailed(ids[i], pex.getErrors());
          /*      */}
        /*      */}
    /*      */}

  /*      */
  /*      */public final CubeView[] getCubeViews() {
    /* 284 */ArrayList views = new ArrayList();
    /* 285 */String[] ids = this.viewStorageHandler.getViewIds(this);
    /* 286 */for (int i = 0; i < ids.length; ++i) {
      /* 287 */CubeView view = getCubeViewOld(ids[i]);
      /* 288 */if (view != null)
        /* 289 */views.add(view);
      /*      */}
    /* 291 */return (CubeView[]) views.toArray(new CubeView[views.size()]);
    /*      */}

  /*      */
  /*      */public final int getCubeViewCount()
  /*      */{
    /* 296 */return this.viewStorageHandler.getViewCount(this);
    /*      */}

  /*      */
  /*      */public final Object getData(String[] coordinates)
  /*      */{
    /*      */try
    /*      */{
      /* 329 */CellInfo cell = this.dbConnection.getData(this.cubeInfo,
        getCoordinates(coordinates));
      /* 330 */return cell.getValue();
      /*      */} catch (PaloException pex) {
      /* 332 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 334 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final Object getData(Element[] coordinates) {
    /*      */try {
      /* 340 */CellInfo cell = this.dbConnection.getData(this.cubeInfo,
      /* 341 */getCoordinates(coordinates));
      /* 342 */return cell.getValue();
      /*      */} catch (PaloException pex) {
      /* 344 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 346 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final Object[] getDataArray(String[][] elements)
  /*      */{
    /*      */try {
      /* 353 */ElementInfo[][] elInfos = new ElementInfo[elements.length][];
      /* 354 */for (int i = 0; i < elInfos.length; ++i) {
        /* 355 */elInfos[i] = getCoordinates(i, elements[i]);
        /*      */}
      /* 357 */CellInfo[] cells = this.dbConnection.getDataArea(this.cubeInfo,
        elInfos);
      /* 358 */Object[] values = new Object[cells.length];
      /* 359 */for (int i = 0; i < values.length; ++i)
        /* 360 */values[i] = cells[i].getValue();
      return values;
      /*      */} catch (PaloException pex) {
      /* 362 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 364 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final Object[] getDataArray(Element[][] elements)
  /*      */{
    /*      */try {
      /* 372 */ElementInfo[][] elInfos = new ElementInfo[elements.length][];
      /* 373 */for (int i = 0; i < elInfos.length; ++i) {
        /* 374 */elInfos[i] = getCoordinates(elements[i]);
        /*      */}
      /* 376 */CellInfo[] cells = this.dbConnection.getDataArea(this.cubeInfo,
        elInfos);
      /* 377 */Object[] values = new Object[cells.length];
      /* 378 */for (int i = 0; i < values.length; ++i)
        /* 379 */values[i] = cells[i].getValue();
      return values;
      /*      */} catch (PaloException pex) {
      /* 381 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 383 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final Object[] getDataBulk(Element[][] coordinates) {
    /* 389 */if ((coordinates == null) || (coordinates.length == 0)) {
      /* 390 */return new Object[0];
      /*      */}
    /*      */try
    /*      */{
      /* 394 */ElementInfo[][] coords = new ElementInfo[coordinates.length][];
      /* 395 */for (int i = 0; i < coords.length; ++i) {
        /* 396 */coords[i] = getCoordinates(coordinates[i]);
        /*      */}
      /* 398 */CellInfo[] cells = this.dbConnection.getDataArray(this.cubeInfo,
        coords);
      /* 399 */Object[] values = new Object[cells.length];
      /* 400 */for (int i = 0; i < values.length; ++i)
        /* 401 */values[i] = cells[i].getValue();
      return values;
      /*      */} catch (PaloException pex) {
      /* 403 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 405 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final synchronized ExportDataset getDataExport(
    ExportContext context) {
    /*      */try {
      /* 412 */this.exportContext = context;
      /* 413 */ExportDatasetImpl dataset = new ExportDatasetImpl(this);
      /* 414 */dataset.start();
      /* 415 */return dataset;
      /*      */} catch (PaloException pex) {
      /* 417 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 419 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final Database getDatabase() {
    /* 424 */return this.database;
    /*      */}

  /*      */
  /*      */public final Dimension getDimensionAt(int index) {
    /* 428 */reloadDims();
    /* 429 */if ((this.dimensions == null) || (index < 0)
      || (index >= this.dimensions.length))
      /* 430 */return null;
    /* 431 */return this.dimensions[index];
    /*      */}

  /*      */
  /*      */public final Dimension getDimensionByName(String name) {
    /* 435 */reloadDims();
    /* 436 */if (this.dimensions == null)
      /* 437 */return null;
    /* 438 */for (int i = 0; i < this.dimensions.length; ++i)
    /*      */{
      /* 440 */Dimension dimension = this.dimensions[i];
      /* 441 */if (dimension.getName().equalsIgnoreCase(name))
        /* 442 */return dimension;
      /*      */}
    /* 444 */return null;
    /*      */}

  /*      */
  /*      */public final Dimension getDimensionById(String id) {
    /* 448 */reloadDims();
    /* 449 */if (this.dimensions == null)
      /* 450 */return null;
    /* 451 */for (int i = 0; i < this.dimensions.length; ++i) {
      /* 452 */if (this.dimensions[i].getId().equals(id))
        /* 453 */return this.dimensions[i];
      /*      */}
    /* 455 */return null;
    /*      */}

  /*      */
  /*      */public final int getDimensionCount() {
    /* 459 */return this.cubeInfo.getDimensions().length;
    /*      */}

  /*      */
  /*      */public final Dimension[] getDimensions() {
    /* 463 */reloadDims();
    /* 464 */return (this.dimensions == null) ? null
      : (Dimension[]) this.dimensions.clone();
    /*      */}

  /*      */
  /*      */private void reloadDims() {
    /* 468 */if ((this.dimensions == null) && (this.tryReloadingDims)) {
      /* 469 */this.tryReloadingDims = false;
      /* 470 */String[] dims = this.cubeInfo.getDimensions();
      /* 471 */this.dimensions = new Dimension[dims.length];
      /* 472 */for (int i = 0; i < this.dimensions.length; ++i)
        /* 473 */this.dimensions[i] = this.database.getDimensionById(dims[i]);
      /*      */}
    /*      */}

  /*      */
  /*      */public final synchronized ExportContext getExportContext() {
    /* 478 */if (this.exportContext == null)
      /* 479 */this.exportContext = new ExportContextImpl(this);
    /* 480 */return this.exportContext;
    /*      */}

  /*      */
  /*      */public final void convert(int type) {
    /* 484 */int result = this.dbConnection.convert(this.cubeInfo,
      getInfoType(type));
    /* 485 */((CubeInfoImpl) this.cubeInfo).setType(result);
    /*      */}

  /*      */
  /*      */public final synchronized ExportContext getExportContext(
    Element[][] area) {
    /* 489 */if (this.exportContext == null)
      /* 490 */this.exportContext = new ExportContextImpl(this, area);
    /* 491 */this.exportContext.setCellsArea(area);
    /* 492 */return this.exportContext;
    /*      */}

  /*      */
  /*      */public final int getExtendedType() {
    /* 496 */return 0;
    /*      */}

  /*      */
  /*      */public final String getName() {
    /* 500 */this.dbConnection.reload(this.cubeInfo);
    /* 501 */return this.cubeInfo.getName();
    /*      */}

  /*      */
  /*      */public final BigInteger getCellCount() {
    /* 505 */this.dbConnection.reload(this.cubeInfo);
    /* 506 */return this.cubeInfo.getCellCount();
    /*      */}

  /*      */
  /*      */public final BigInteger getFilledCellCount() {
    /* 510 */this.dbConnection.reload(this.cubeInfo);
    /* 511 */return this.cubeInfo.getFilledCellCount();
    /*      */}

  /*      */
  /*      */public final int getStatus() {
    /* 515 */this.dbConnection.reload(this.cubeInfo);
    /* 516 */return this.cubeInfo.getStatus();
    /*      */}

  /*      */
  /*      */public final boolean isAttributeCube() {
    /* 520 */return this.cubeInfo.getType() == 2;
    /*      */}

  /*      */
  /*      */public final boolean isSubsetCube() {
    /* 524 */return PaloObjects.isSubsetCube(this);
    /*      */}

  /*      */
  /*      */public final boolean isViewCube() {
    /* 528 */return PaloObjects.isViewsCube(this);
    /*      */}

  /*      */
  /*      */public final void removeCubeView(CubeView view) {
    /* 532 */this.viewStorageHandler.removeCubeView(this, view);
    /*      */}

  /*      */
  /*      */public final void setData(String[] coordinates, Object value)
  /*      */{
    /* 540 */setDataInternal(getCoordinates(coordinates), value,
    /* 541 */0);
    /*      */}

  /*      */
  /*      */public final void setData(Element[] coordinates, Object value) {
    /* 545 */setDataInternal(getCoordinates(coordinates), value,
    /* 546 */0);
    /*      */}

  /*      */
  /*      */public final void addDataArray(Element[][] coordinates,
    Object[] values, int splashMode)
  /*      */{
    /* 551 */setDataArray(coordinates, values, true, splashMode, true);
    /*      */}

  /*      */
  /*      */public final void setDataArray(Element[][] coordinates,
    Object[] values, int splashMode)
  /*      */{
    /* 556 */setDataArray(coordinates, values, false, splashMode, true);
    /*      */}

  /*      */
  /*      */public final void setDataArray(Element[][] coordinates,
    Object[] values, boolean add, int splashMode, boolean notifyEventProcessors) {
    /* 560 */if ((values == null) || (values.length == 0)) {
      /* 561 */return;
      /*      */}
    /*      */try
    /*      */{
      /* 565 */ElementInfo[][] coords = new ElementInfo[coordinates.length][];
      /* 566 */for (int i = 0; i < coordinates.length; ++i) {
        /* 567 */coords[i] = getCoordinates(coordinates[i]);
        /*      */}
      /* 569 */this.dbConnection.setDataArray(this.cubeInfo, coords, values, add,
        splashMode, notifyEventProcessors);
      /*      */} catch (PaloException pex) {
      /* 571 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 573 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void setDataSplashed(Element[] coordinates, Object value)
  /*      */{
    /* 579 */int splashMode = 1;
    /* 580 */String valStr = value.toString();
    /* 581 */if (valStr.startsWith("#")) {
      /* 582 */splashMode = 1;
      /* 583 */if (valStr.endsWith("%")) {
        /* 584 */double newVal = Double.parseDouble(valStr.substring(1, valStr
          .length() - 1));
        /* 585 */double oldValue = ((Double) getData(coordinates)).doubleValue();
        /* 586 */newVal = oldValue * percent(newVal);
        /* 587 */value = new Double(newVal);
        /*      */} else {
        /* 589 */value = new Double(valStr.substring(1));
        /*      */}
      /* 591 */} else if (valStr.startsWith("!!")) {
      /* 592 */splashMode = 2;
      /* 593 */value = new Double(valStr.substring(2));
      /* 594 */} else if (valStr.startsWith("!")) {
      /* 595 */splashMode = 3;
      /* 596 */value = new Double(valStr.substring(1));
      /*      */} else {
      /* 598 */value = new Double(valStr);
      /*      */}
    /* 600 */ElementInfo[] coords = getCoordinates(coordinates);
    /* 601 */setDataInternal(coords, value, splashMode);
    /*      */}

  /*      */
  /*      */public final void setDataSplashed(Element[] coordinates, Object value,
    NumberFormat formatter) {
    /* 605 */if (formatter == null) {
      /* 606 */setDataSplashed(coordinates, value);
      /*      */}
    /* 608 */int splashMode = 1;
    /* 609 */String valStr = value.toString();
    /* 610 */if (valStr.startsWith("#")) {
      /* 611 */splashMode = 1;
      /* 612 */if (valStr.endsWith("%")) {
        /* 613 */valStr = valStr.substring(1, valStr.length() - 1);
        /* 614 */setPercentage(coordinates, valStr, formatter);
        /* 615 */return;
        /*      */}
      /* 617 */valStr = valStr.substring(1);
      /* 618 */} else if (valStr.startsWith("!!")) {
      /* 619 */splashMode = 2;
      /* 620 */valStr = valStr.substring(2);
      /* 621 */} else if (valStr.startsWith("!")) {
      /* 622 */splashMode = 3;
      /* 623 */valStr = valStr.substring(1);
      /*      */}
    /*      */
    /* 626 */String _value = formatValue(valStr, formatter);
    /*      */
    /* 629 */ElementInfo[] coords = getCoordinates(coordinates);
    /* 630 */setDataInternal(coords, new Double(_value), splashMode);
    /*      */}

  /*      */
  /*      */public final void setData(Element[] coordinates, Object value,
    NumberFormat formatter)
  /*      */{
    /* 635 */String valStr = value.toString();
    /* 636 */String _value = formatValue(value.toString(), formatter);
    /* 637 */if (valStr.equals(_value))
      /* 638 */setDataInternal(getCoordinates(coordinates), value,
      /* 639 */0);
    /*      */else
      /* 641 */setDataInternal(getCoordinates(coordinates), new Double(_value),
      /* 642 */0);
    /*      */}

  /*      */
  /*      */public final void setDataSplashed(String[] coordinates, Object value,
    int splashMode)
  /*      */{
    /* 647 */if (value == null)
      /* 648 */return;
    /*      */try
    /*      */{
      /* 651 */ElementInfo[] coords = getCoordinates(coordinates);
      /* 652 */setDataInternal(coords, value, splashMode);
      /*      */} catch (PaloException pex) {
      /* 654 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 656 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void setDataSplashed(Element[] coordinates, Object value,
    int splashMode)
  /*      */{
    /* 662 */if (value == null)
      /* 663 */return;
    /*      */try
    /*      */{
      /* 666 */ElementInfo[] coords = getCoordinates(coordinates);
      /* 667 */setDataInternal(coords, value, splashMode);
      /*      */} catch (PaloException pex) {
      /* 669 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 671 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final String getId() {
    /* 676 */return this.cubeInfo.getId();
    /*      */}

  /*      */
  /*      */public final CubeInfo getInfo() {
    /* 680 */this.dbConnection.reload(this.cubeInfo);
    /* 681 */return this.cubeInfo;
    /*      */}

  /*      */
  /*      */public final boolean isSystemCube() {
    /* 685 */return this.cubeInfo.getType() == 1;
    /*      */}

  /*      */
  /*      */public final boolean isUserInfoCube() {
    /* 689 */return this.cubeInfo.getType() == 3;
    /*      */}

  /*      */
  /*      */public final int getType()
  /*      */{
    /* 705 */return getType(this.cubeInfo);
    /*      */}

  /*      */
  /*      */public final boolean equals(Object other)
  /*      */{
    /* 710 */if (other instanceof CubeImpl) {
      /* 711 */return this.key.equals(((CubeImpl) other).key);
      /*      */}
    /* 713 */return false;
    /*      */}

  /*      */
  /*      */public final int hashCode() {
    /* 717 */return this.key.hashCode();
    /*      */}

  /*      */
  /*      */public final Rule addRule(String definition)
  /*      */{
    /* 722 */return addRule(definition, null, false, null);
    /*      */}

  /*      */
  /*      */public final Rule addRule(String definition, String externalIdentifier,
    boolean useIt, String comment)
  /*      */{
    /* 733 */return addRule(definition, externalIdentifier, useIt, comment, true);
    /*      */}

  /*      */
  /*      */public final Rule addRule(String definition, String externalIdentifier,
    boolean useIt, String comment, boolean activate)
  /*      */{
    /* 748 */RuleInfo rule = this.ruleLoader.create(definition, externalIdentifier,
    /* 749 */useIt, comment, activate);
    /* 750 */Rule newRule = createRule(rule);
    /* 751 */fireRulesAdded(new Rule[] { newRule });
    /* 752 */return newRule;
    /*      */}

  /*      */
  /*      */public final Rule[] getRules() {
    /* 756 */String[] ids = this.ruleLoader.getAllRuleIds();
    /* 757 */ArrayList rules = new ArrayList();
    /* 758 */for (String id : ids) {
      /* 759 */RuleInfo info = this.ruleLoader.load(id);
      /* 760 */Rule rule = getRule(info);
      /* 761 */if (rule != null)
        /* 762 */rules.add(rule);
      /*      */}
    /* 764 */return (Rule[]) rules.toArray(new Rule[rules.size()]);
    /*      */}

  /*      */
  /*      */public final Rule getRule(Element[] coordinate)
  /*      */{
    /*      */try
    /*      */{
      /* 784 */ElementInfo[] coord = getCoordinates(coordinate);
      /* 785 */RuleInfo rule = this.ruleLoader.load(coord);
      /* 786 */return getRule(rule);
      /*      */} catch (PaloException pex) {
      /* 788 */throw new PaloAPIException("Failed to load rule!", pex);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean removeRule(Rule rule)
  /*      */{
    /* 803 */RuleInfo _rule = ((RuleImpl) rule).getInfo();
    /*      */try {
      /* 805 */if (this.ruleLoader.delete(_rule)) {
        /* 806 */this.loadedRules.remove(rule.getId());
        /* 807 */fireRulesRemoved(new Rule[] { rule });
        /* 808 */return true;
        /*      */}
      /*      */}
    /*      */catch (PaloException localPaloException) {
      /*      */}
    /* 813 */return false;
    /*      */}

  /*      */
  /*      */public final boolean removeRule(String ruleId)
  /*      */{
    /* 831 */if (this.ruleLoader.delete(ruleId)) {
      /* 832 */Rule rmRule = (Rule) this.loadedRules.remove(ruleId);
      /* 833 */if (rmRule != null)
        /* 834 */fireRulesRemoved(new Rule[] { rmRule });
      /* 835 */return true;
      /*      */}
    /* 837 */return false;
    /*      */}

  /*      */
  /*      */public void rename(String newName) {
    /* 841 */String oldName = getName();
    /*      */
    /* 843 */this.dbConnection.rename(this.cubeInfo, newName);
    /*      */
    /* 850 */fireCubeRenamed(this, oldName);
    /*      */}

  /*      */
  /*      */public void registerViewObserver(PersistenceObserver cubeViewObserver)
  /*      */{
    /* 855 */if (!this.viewObservers.contains(cubeViewObserver))
      /* 856 */this.viewObservers.add(cubeViewObserver);
    /*      */}

  /*      */
  /*      */public void unregisterViewObserver(PersistenceObserver cubeViewObserver) {
    /* 860 */this.viewObservers.remove(cubeViewObserver);
    /*      */}

  /*      */
  /*      */public final Cell getCell(Element[] coordinate)
  /*      */{
    /*      */try
    /*      */{
      /* 869 */CellInfo cell =
      /* 870 */this.dbConnection.getData(this.cubeInfo, getCoordinates(coordinate));
      /* 871 */return new CellImpl(this, cell, coordinate);
      /*      */} catch (PaloException pex) {
      /* 873 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 875 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final Cell[] getCells(Element[][] coordinates) {
    /*      */try {
      /* 881 */ElementInfo[][] coords = new ElementInfo[coordinates.length][];
      /* 882 */for (int i = 0; i < coords.length; ++i) {
        /* 883 */coords[i] = getCoordinates(coordinates[i]);
        /*      */}
      /* 885 */CellInfo[] _cells = this.dbConnection.getDataArray(this.cubeInfo,
        coords);
      /* 886 */Cell[] cells = new Cell[_cells.length];
      /* 887 */for (int i = 0; i < _cells.length; ++i) {
        /* 888 */String[] coordinate = _cells[i].getCoordinate();
        /* 889 */if (coordinate == null)
          /* 890 */cells[i] = new CellImpl(this, _cells[i], coordinates[i]);
        /*      */else
          /* 892 */cells[i] = new CellImpl(this, _cells[i], coordinates[i]);
        /*      */}
      return cells;
      /*      */}
    /*      */catch (PaloException pex) {
      /* 896 */throw new PaloAPIException(pex);
      /*      */} catch (NullPointerException ex) {
      /* 898 */throw new PaloAPIException(
        "Could not match cell coordinates to cube data. Has a dimension been removed or added to this cube?",
        ex);
      /*      */} catch (RuntimeException e) {
      /* 900 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */private final boolean isEmpty(CellInfo cell) {
    /* 906 */Object value = cell.getValue();
    /* 907 */boolean isEmpty = (value == null) || (value.toString().equals(""));
    /* 908 */if (isEmpty) {
      /* 909 */return true;
      /*      */}
    /* 911 */if (cell.getType() == 1)
      /*      */try {
        /* 913 */Double d = Double.valueOf(Double.parseDouble(value.toString()));
        /* 914 */if (Math.abs(d.doubleValue()) < 1.0E-006D)
          /* 915 */isEmpty = true;
        /*      */}
      /*      */catch (Exception localException)
      /*      */{
        /*      */}
    /* 920 */return isEmpty;
    /*      */}

  /*      */
  /*      */public final Cell[] getCells(Element[][] coordinates,
    boolean hideEmptyCells) {
    /* 924 */if (!hideEmptyCells) {
      /* 925 */return getCells(coordinates);
      /*      */}
    /* 927 */ArrayList cellList = new ArrayList();
    /*      */try {
      /* 929 */ElementInfo[][] coords = new ElementInfo[coordinates.length][];
      /* 930 */for (int i = 0; i < coords.length; ++i) {
        /* 931 */coords[i] = getCoordinates(coordinates[i]);
        /*      */}
      /* 933 */CellInfo[] _cells = this.dbConnection.getDataArray(this.cubeInfo,
        coords);
      /* 934 */for (int i = 0; i < _cells.length; ++i) {
        /* 935 */if (isEmpty(_cells[i])) {
          /*      */continue;
          /*      */}
        /* 938 */String[] coordinate = _cells[i].getCoordinate();
        /* 939 */if (coordinate == null)
          /* 940 */cellList.add(new CellImpl(this, _cells[i], coordinates[i]));
        /*      */else
          /* 942 */cellList.add(new CellImpl(this, _cells[i], coordinates[i]));
        /*      */}
      /*      */}
    /*      */catch (PaloException pex) {
      /* 946 */throw new PaloAPIException(pex);
      /*      */} catch (NullPointerException ex) {
      /* 948 */throw new PaloAPIException(
        "Could not match cell coordinates to cube data. Has a dimension been removed or added to this cube?",
        ex);
      /*      */} catch (RuntimeException e) {
      /* 950 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /* 952 */return (Cell[]) cellList.toArray(new Cell[0]);
    /*      */}

  /*      */
  /*      */public final Cell[] getCellArea(Element[][] coordinates)
  /*      */{
    /*      */try {
      /* 958 */ElementInfo[][] coords = new ElementInfo[coordinates.length][];
      /* 959 */for (int i = 0; i < coords.length; ++i) {
        /* 960 */coords[i] = getCoordinates(coordinates[i]);
        /*      */}
      /* 962 */CellInfo[] _cells = this.dbConnection.getDataArea(this.cubeInfo,
        coords);
      /* 963 */Cell[] cells = new Cell[_cells.length];
      /* 964 */for (int i = 0; i < _cells.length; ++i)
        /* 965 */cells[i] = new CellImpl(this, _cells[i]);

      return cells;
      /*      */} catch (PaloException pex) {
      /* 967 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 969 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}

    /*      */}

  /*      */
  /*      */public final Lock requestLock(Element[][] area) {
    /*      */try {
      /* 976 */ElementInfo[][] coords = new ElementInfo[area.length][];
      /* 977 */for (int i = 0; i < coords.length; ++i) {
        /* 978 */coords[i] = getCoordinates(area[i]);
        /*      */}
      /* 980 */LockInfo lock = this.dbConnection.requestLock(this.cubeInfo, coords);
      /* 981 */return new LockImpl(lock);
      /*      */}
    /*      */catch (Exception localException) {
      /*      */}
    /* 985 */return null;
    /*      */}

  /*      */public final Lock[] getLocks() {
    /*      */try {
      /* 989 */LockInfo[] lockInfos = this.dbConnection.getLocks(this.cubeInfo);
      /* 990 */Lock[] locks = new Lock[lockInfos.length];
      /* 991 */for (int i = 0; i < locks.length; ++i)
        /* 992 */locks[i] = new LockImpl(lockInfos[i]);
      /* 993 */return locks;
      /*      */} catch (PaloException pex) {
      /* 995 */throw new PaloAPIException(pex);
      /*      */} catch (Exception e) {
      /* 997 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean commit(Lock lock) {
    /*      */try {
      /* 1002 */if (lock != null)
        /* 1003 */return this.dbConnection.commit(this.cubeInfo, ((LockImpl) lock)
          .getInfo());
      /*      */}
    /*      */catch (Exception localException) {
      /*      */}
    /* 1007 */return false;
    /*      */}

  /*      */public final boolean rollback(Lock lock, int steps) {
    /*      */try {
      /* 1011 */if (lock != null)
        /* 1012 */return this.dbConnection.rollback(this.cubeInfo,
        /* 1013 */((LockImpl) lock).getInfo(), steps);
      /*      */}
    /*      */catch (Exception localException) {
      /*      */}
    /* 1017 */return false;
    /*      */}

  /*      */
  /*      */public final boolean save() {
    /* 1021 */return this.dbConnection.save(this.cubeInfo);
    /*      */}

  /*      */
  /*      */final void removeAllCubeViews()
  /*      */{
    /* 1032 */this.viewStorageHandler.removeLoadedViews(this);
    /*      */}

  /*      */
  /*      */final void reload(boolean doEvents)
  /*      */{
    /* 1037 */this.tryReloadingDims = true;
    /* 1038 */this.dimensions = null;
    /*      */
    /* 1041 */this.dbConnection.reload(this.cubeInfo);
    /*      */
    /* 1044 */reloadRuleInfos(doEvents);
    /*      */}

  /*      */
  /*      */final void clearCache()
  /*      */{
    /* 1087 */for (RuleImpl rule : this.loadedRules.values()) {
      /* 1088 */rule.clearCache();
      /*      */}
    /* 1090 */this.loadedRules.clear();
    /* 1091 */this.ruleLoader.reset();
    /*      */
    /* 1093 */for (Property2Impl property : this.loadedProperties.values()) {
      /* 1094 */property.clearCache();
      /*      */}
    /* 1096 */this.loadedProperties.clear();
    /* 1097 */this.propertyLoader.reset();
    /*      */}

  /*      */
  /*      */final void reloadRuleInfos(boolean doEvents)
  /*      */{
    /* 1104 */HashMap oldRules =
    /* 1105 */new HashMap(this.loadedRules);
    /* 1106 */HashSet addedRules = new HashSet();
    /*      */
    /* 1108 */this.ruleLoader.reset();
    /* 1109 */this.loadedRules.clear();
    /*      */
    /* 1111 */String[] ruleIds = this.ruleLoader.getAllRuleIds();
    /* 1112 */for (String id : ruleIds) {
      /* 1113 */RuleInfo info = this.ruleLoader.load(id);
      /* 1114 */RuleImpl rule = getRule(info);
      /* 1115 */if (rule != null) {
        /* 1116 */this.loadedRules.put(rule.getId(), rule);
        /*      */}
      /* 1118 */if (oldRules.containsKey(id))
        /* 1119 */oldRules.remove(id);
      /*      */else
        /* 1121 */addedRules.add(rule);
      /*      */}
    /* 1123 */if (doEvents) {
      /* 1124 */if (!oldRules.isEmpty())
        /* 1125 */fireRulesRemoved((Rule[]) oldRules.values().toArray(new Rule[0]));
      /* 1126 */if (!addedRules.isEmpty())
        /* 1127 */fireRulesAdded((Rule[]) addedRules.toArray(new Rule[0]));
      /*      */}
    /*      */}

  /*      */
  /*      */private final RuleImpl getRule(RuleInfo ruleInfo)
  /*      */{
    /* 1150 */if (ruleInfo == null) {
      /* 1151 */return null;
      /*      */}
    /* 1153 */RuleImpl rule = (RuleImpl) this.loadedRules.get(ruleInfo.getId());
    /* 1154 */if (rule == null)
    /*      */{
      /* 1156 */rule = createRule(ruleInfo);
      /*      */}
    /* 1158 */return rule;
    /*      */}

  /*      */
  /*      */private final RuleImpl createRule(RuleInfo ruleInfo)
  /*      */{
    /* 1168 */RuleImpl rule = new RuleImpl(this.dbConnection, this, ruleInfo);
    /* 1169 */this.loadedRules.put(rule.getId(), rule);
    /*      */
    /* 1171 */return rule;
    /*      */}

  /*      */
  /*      */private final void setDataInternal(ElementInfo[] coordinates,
    Object value, int splashMode)
  /*      */{
    /* 1177 */if (value == null)
      /* 1178 */return;
    /*      */try
    /*      */{
      /* 1181 */if (value instanceof Number)
        /* 1182 */this.dbConnection.setDataNumericSplashed(this.cubeInfo,
          coordinates,
          /* 1183 */((Number) value).doubleValue(), splashMode);
      /*      */else
        /* 1185 */this.dbConnection.setDataString(this.cubeInfo, coordinates,
        /* 1186 */value.toString());
      /*      */}
    /*      */catch (PaloException pex) {
      /* 1189 */throw new PaloAPIException(pex);
      /*      */} catch (RuntimeException e) {
      /* 1191 */throw new PaloAPIException(e.getMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */private final ElementInfo[] getCoordinates(String[] names)
  /*      */{
    /* 1198 */ElementInfo[] coords = new ElementInfo[names.length];
    /* 1199 */for (int i = 0; i < coords.length; ++i) {
      /* 1200 */Dimension dimension = getDimensionAt(i);
      /* 1201 */ElementImpl element =
      /* 1202 */(ElementImpl) dimension.getDefaultHierarchy().getElementByName(
        names[i]);
      /* 1203 */if (element == null)
        /* 1204 */throw new PaloAPIException("Element not found! Dimension '"
          + dimension.getName() + "' contains no element with name '" + names[i]
          + "'!!");
      /* 1205 */coords[i] = element.getInfo();
      /*      */}
    /* 1207 */return coords;
    /*      */}

  /*      */
  /*      */private final ElementInfo[] getCoordinates(int dimIndex, String[] names)
  /*      */{
    /* 1213 */ElementInfo[] coords = new ElementInfo[names.length];
    /* 1214 */for (int i = 0; i < coords.length; ++i) {
      /* 1215 */Dimension dimension = getDimensionAt(dimIndex);
      /* 1216 */coords[i] =
      /* 1217 */((ElementImpl) dimension.getDefaultHierarchy().getElementByName(
        names[i])).getInfo();
      /*      */}
    /* 1219 */return coords;
    /*      */}

  /*      */
  /*      */private final ElementInfo[] getCoordinates(Element[] elements) {
    /* 1223 */ElementInfo[] coords = new ElementInfo[elements.length];
    /* 1224 */for (int i = 0; i < coords.length; ++i) {
      /* 1225 */if (elements[i] instanceof VirtualElementImpl)
        /* 1226 */coords[i] = ((VirtualElementImpl) elements[i]).getInfo();
      /*      */else
        /* 1228 */coords[i] = ((ElementImpl) elements[i]).getInfo();
      /*      */}
    /* 1230 */return coords;
    /*      */}

  /*      */
  /*      */private final void notifyLoadFailed(String sourceId,
    PersistenceError[] errors)
  /*      */{
    /* 1277 */int i = 0;
    for (int n = this.viewObservers.size(); i < n; ++i) {
      /* 1278 */PersistenceObserver observer =
      /* 1279 */(PersistenceObserver) this.viewObservers.get(i);
      /* 1280 */observer.loadFailed(sourceId, errors);
      /*      */}
    /*      */}

  /*      */
  /*      */private final void notifyLoadIncomplete(Object view,
    PersistenceError[] errors)
  /*      */{
    /* 1286 */int i = 0;
    for (int n = this.viewObservers.size(); i < n; ++i) {
      /* 1287 */PersistenceObserver observer =
      /* 1288 */(PersistenceObserver) this.viewObservers
      /* 1288 */.get(i);
      /* 1289 */observer.loadIncomplete(view, errors);
      /*      */}
    /*      */}

  /*      */
  /*      */private final void notifyLoadComplete(Object view) {
    /* 1294 */int i = 0;
    for (int n = this.viewObservers.size(); i < n; ++i) {
      /* 1295 */PersistenceObserver observer =
      /* 1296 */(PersistenceObserver) this.viewObservers
      /* 1296 */.get(i);
      /* 1297 */observer.loadComplete(view);
      /*      */}
    /*      */}

  /*      */
  /*      */private final void setPercentage(Element[] coordinates, String valStr,
    NumberFormat formatter)
  /*      */{
    /* 1328 */valStr = formatValue(valStr, formatter);
    /* 1329 */double newVal = Double.parseDouble(valStr);
    /* 1330 */double oldValue = ((Double) getData(coordinates)).doubleValue();
    /* 1331 */newVal = oldValue * percent(newVal);
    /* 1332 */Double value = new Double(newVal);
    /* 1333 */ElementInfo[] coords = getCoordinates(coordinates);
    /* 1334 */setDataInternal(coords, value, 1);
    /*      */}

  /*      */
  /*      */private final double percent(double val)
  /*      */{
    /* 1339 */return 1.0D + val / 100.0D;
    /*      */}

  /*      */
  /*      */public String[] getAllPropertyIds() {
    /* 1343 */return this.propertyLoader.getAllPropertyIds();
    /*      */}

  /*      */
  /*      */public Property2 getProperty(String id) {
    /* 1347 */PropertyInfo propInfo = this.propertyLoader.load(id);
    /* 1348 */if (propInfo == null) {
      /* 1349 */return null;
      /*      */}
    /* 1351 */Property2 property = (Property2) this.loadedProperties.get(propInfo
      .getId());
    /* 1352 */if (property == null) {
      /* 1353 */property = createProperty(propInfo);
      /*      */}
    /*      */
    /* 1356 */return property;
    /*      */}

  /*      */
  /*      */public void addProperty(Property2 property) {
    /* 1360 */if (property == null) {
      /* 1361 */return;
      /*      */}
    /* 1363 */Property2Impl _property = (Property2Impl) property;
    /* 1364 */this.propertyLoader.loaded(_property.getPropInfo());
    /* 1365 */this.loadedProperties.put(_property.getId(), _property);
    /*      */}

  /*      */
  /*      */public void removeProperty(String id) {
    /* 1369 */Property2 property = getProperty(id);
    /* 1370 */if (property == null) {
      /* 1371 */return;
      /*      */}
    /* 1373 */if (property.isReadOnly()) {
      /* 1374 */return;
      /*      */}
    /* 1376 */this.loadedProperties.remove(property);
    /*      */}

  /*      */
  /*      */private void createProperty(Property2 parent, PropertyInfo kid) {
    /* 1380 */Property2 p2Kid = Property2Impl.create(parent, kid);
    /* 1381 */parent.addChild(p2Kid);
    /* 1382 */for (PropertyInfo kidd : kid.getChildren())
      /* 1383 */createProperty(p2Kid, kidd);
    /*      */}

  /*      */
  /*      */private Property2 createProperty(PropertyInfo propInfo)
  /*      */{
    /* 1388 */Property2 prop = Property2Impl.create(null, propInfo);
    /* 1389 */for (PropertyInfo kid : propInfo.getChildren()) {
      /* 1390 */createProperty(prop, kid);
      /*      */}
    /* 1392 */return prop;
    /*      */}

  /*      */
  /*      */private final void fireCubeRenamed(Cube cube, String oldValue)
  /*      */{
    /* 1404 */ConnectionEvent ev = new ConnectionEvent(getDatabase()
      .getConnection(),
    /* 1405 */getDatabase(),
    /* 1406 */18, new Cube[] { cube });
    /*      */
    /* 1408 */ev.oldValue = oldValue;
    /* 1409 */this.connection.fireEvent(ev);
    /*      */}

  /*      */
  /*      */private final void fireRulesAdded(Rule[] rules) {
    /* 1413 */ConnectionEvent ev = new ConnectionEvent(getDatabase()
      .getConnection(),
    /* 1414 */getDatabase(), 19,
    /* 1415 */rules);
    /* 1416 */this.connection.fireEvent(ev);
    /*      */}

  /*      */
  /*      */private final void fireRulesRemoved(Rule[] rules) {
    /* 1420 */ConnectionEvent ev = new ConnectionEvent(getDatabase()
      .getConnection(),
    /* 1421 */getDatabase(), 20,
    /* 1422 */rules);
    /* 1423 */this.connection.fireEvent(ev);
    /*      */}

  /*      */
  /*      */final void fireRuleChanged(Rule rule, Object changedValue) {
    /* 1427 */ConnectionEvent ev = new ConnectionEvent(getDatabase()
      .getConnection(),
    /* 1428 */getDatabase(), 21,
    /* 1429 */new Rule[] { rule });
    /* 1430 */ev.oldValue = changedValue;
    /* 1431 */this.connection.fireEvent(ev);
    /*      */}

  /*      */
  /*      */private final String formatValue(String str, NumberFormat formatter)
  /*      */{
    /* 1477 */ParsePosition pos = new ParsePosition(0);
    /* 1478 */Number numVal = formatter.parse(str, pos);
    /* 1479 */if (pos.getIndex() != str.length()) {
      /* 1480 */char[] chars = str.toCharArray();
      /* 1481 */StringBuffer buffer = new StringBuffer();
      /* 1482 */boolean valid = false;
      /* 1483 */boolean hasSign = false;
      /* 1484 */boolean hasExponent = false;
      /* 1485 */int i = 0;
      for (int n = chars.length; i < n; ++i) {
        /* 1486 */char c = chars[i];
        /* 1487 */if ((Character.isDigit(c)) || (c == '.') || (c == ',')
          || (c == '-') || (c == '+') || (c == 'e') || (c == 'E')) {
          /* 1488 */if ((c == 'e') || ((c == 'E') && (valid))) {
            /* 1489 */if (hasExponent) {
              /* 1490 */valid = false;
              /* 1491 */break;
              /*      */}
            /* 1493 */hasExponent = true;
            /* 1494 */hasSign = false;
            /* 1495 */if (i >= n - 1) {
              /* 1496 */valid = false;
              /* 1497 */break;
              /*      */}
            /* 1499 */if ((chars[(i + 1)] == '+') || (chars[(i + 1)] == '-')) {
              /* 1500 */if (i >= n - 2) {
                /* 1501 */valid = false;
                /* 1502 */break;
                /*      */}
              /* 1504 */if (!Character.isDigit(chars[(i + 2)])) {
                /* 1505 */valid = false;
                /* 1506 */break;
                /*      */}
              /*      */}
            /*      */}
          /* 1510 */if ((c == '-') || ((c == '+') && (valid))) {
            /* 1511 */if (hasSign) {
              /* 1512 */valid = false;
              /* 1513 */break;
              /*      */}
            /* 1515 */hasSign = true;
            /*      */}
          /* 1517 */if (!valid) {
            /* 1518 */valid = true;
            /*      */}
          /* 1520 */buffer.append(c);
          /*      */} else {
          /* 1522 */if (valid) {
            /*      */break;
            /*      */}
          /*      */}
        /*      */}
      /* 1527 */String res = buffer.toString().trim();
      /* 1528 */DecimalFormat customFormatter = new DecimalFormat("#,##0.00");
      /* 1529 */pos = new ParsePosition(0);
      /* 1530 */numVal = customFormatter.parse(res, pos);
      /* 1531 */if (pos.getIndex() != res.length()) {
        /* 1532 */return str;
        /*      */}
      /*      */}
    /* 1535 */return numVal.toString();
    /*      */}

  /*      */
  /*      */private final CubeView getCubeViewOld(String id) {
    /*      */try {
      /* 1540 */CubeView view = this.viewStorageHandler.getCubeView(this, id);
      /* 1541 */if (view != null)
        /* 1542 */notifyLoadComplete(view);
      /* 1543 */return view;
      /*      */}
    /*      */catch (PaloPersistenceException pex) {
      /* 1546 */if (pex.getType() == 1) {
        /* 1547 */PersistenceError[] errors = pex.getErrors();
        /* 1548 */Object view = (errors.length > 0) ? errors[0].getSource() : null;
        /* 1549 */notifyLoadIncomplete(view, errors);
        /*      */} else {
        /* 1551 */notifyLoadFailed(id, pex.getErrors());
        /*      */}
      /*      */}
    /* 1553 */return null;
    /*      */}

  /*      */
  /*      */public void clear() {
    /*      */try {
      /* 1558 */this.dbConnection.clear(this.cubeInfo);
      /*      */} catch (PaloException pex) {
      /* 1560 */throw new PaloAPIException(pex);
      /*      */}
    /*      */}

  /*      */
  /*      */public void clear(Element[][] area) {
    /*      */try {
      /* 1566 */ElementInfo[][] coords = new ElementInfo[area.length][];
      /* 1567 */for (int i = 0; i < coords.length; ++i) {
        /* 1568 */coords[i] = getCoordinates(area[i]);
        /*      */}
      /* 1570 */this.dbConnection.clear(this.cubeInfo, coords);
      /*      */} catch (PaloException pex) {
      /* 1572 */throw new PaloAPIException(pex);
      /*      */}
    /*      */}

  /*      */
  /*      */public boolean canBeModified()
  /*      */{
    /* 1614 */return this.cubeInfo.canBeModified();
    /*      */}

  /*      */
  /*      */public boolean canCreateChildren() {
    /* 1618 */return this.cubeInfo.canCreateChildren();
    /*      */}
  /*      */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.CubeImpl JD-Core Version: 0.5.4
 */