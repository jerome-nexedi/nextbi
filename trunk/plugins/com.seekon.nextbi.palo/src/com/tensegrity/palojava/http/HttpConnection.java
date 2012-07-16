/*      */package com.tensegrity.palojava.http;

/*      */
/*      */import com.tensegrity.palojava.CellInfo; /*      */
import com.tensegrity.palojava.ConnectionInfo; /*      */
import com.tensegrity.palojava.CubeInfo; /*      */
import com.tensegrity.palojava.DatabaseInfo; /*      */
import com.tensegrity.palojava.DbConnection; /*      */
import com.tensegrity.palojava.DimensionInfo; /*      */
import com.tensegrity.palojava.ElementInfo; /*      */
import com.tensegrity.palojava.ExportContextInfo; /*      */
import com.tensegrity.palojava.HierarchyInfo; /*      */
import com.tensegrity.palojava.LockInfo; /*      */
import com.tensegrity.palojava.PaloException; /*      */
import com.tensegrity.palojava.PaloInfo; /*      */
import com.tensegrity.palojava.PropertyInfo; /*      */
import com.tensegrity.palojava.RuleInfo; /*      */
import com.tensegrity.palojava.ServerInfo; /*      */
import com.tensegrity.palojava.events.ServerEvent; /*      */
import com.tensegrity.palojava.events.ServerListener; /*      */
import com.tensegrity.palojava.http.handlers.CellHandler; /*      */
import com.tensegrity.palojava.http.handlers.CubeHandler; /*      */
import com.tensegrity.palojava.http.handlers.DatabaseHandler; /*      */
import com.tensegrity.palojava.http.handlers.DimensionHandler; /*      */
import com.tensegrity.palojava.http.handlers.ElementHandler; /*      */
import com.tensegrity.palojava.http.handlers.HandlerRegistry; /*      */
import com.tensegrity.palojava.http.handlers.RuleHandler; /*      */
import com.tensegrity.palojava.http.handlers.ServerHandler; /*      */
import com.tensegrity.palojava.http.loader.HttpCubeLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpDatabaseLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpDimensionLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpElementLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpFunctionLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpHierarchyLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpPropertyLoader; /*      */
import com.tensegrity.palojava.http.loader.HttpRuleLoader; /*      */
import com.tensegrity.palojava.impl.ConnectionInfoImpl; /*      */
import com.tensegrity.palojava.impl.PropertyInfoImpl; /*      */
import com.tensegrity.palojava.loader.CubeLoader; /*      */
import com.tensegrity.palojava.loader.DatabaseLoader; /*      */
import com.tensegrity.palojava.loader.DimensionLoader; /*      */
import com.tensegrity.palojava.loader.ElementLoader; /*      */
import com.tensegrity.palojava.loader.FunctionLoader; /*      */
import com.tensegrity.palojava.loader.HierarchyLoader; /*      */
import com.tensegrity.palojava.loader.PaloInfoLoader; /*      */
import com.tensegrity.palojava.loader.PropertyLoader; /*      */
import com.tensegrity.palojava.loader.RuleLoader; /*      */
import java.io.IOException; /*      */
import java.net.ConnectException; /*      */
import java.security.MessageDigest; /*      */
import java.security.NoSuchAlgorithmException; /*      */
import java.util.ArrayList; /*      */
import java.util.HashMap; /*      */
import java.util.HashSet; /*      */
import java.util.Iterator; /*      */
import java.util.Timer;

/*      */
/*      */public class HttpConnection
/*      */implements DbConnection
/*      */{
  /* 102 */private final long SID_RENEWAL_THRESHOLD = 20000L;

  /*      */private final HandlerRegistry handlerRegistry;

  /*      */private final ConnectionInfoImpl connectionInfo;

  /*      */private final HttpClient httpClient;

  /* 107 */private final HashSet listeners = new HashSet();

  /*      */
  /* 109 */private final HashMap<String, PaloInfoLoader> loaders = new HashMap();

  /*      */private String sid;

  /*      */private int srvToken;

  /*      */private long ttl;

  /*      */private boolean ownChange;

  /*      */private Timer httpTimer;

  /*      */private final int timeout;

  /*      */
  /*      */HttpConnection(String host, String port, int timeout)
  /*      */throws PaloException
  /*      */{
    /* 125 */this.httpClient = new HttpClient(this);
    /* 126 */this.connectionInfo = new ConnectionInfoImpl(host, port);
    /* 127 */this.handlerRegistry = new HandlerRegistry(this);
    /* 128 */this.timeout = timeout;
    /*      */try {
      /* 130 */this.httpClient.reconnect(timeout);
      /*      */} catch (IOException ioex) {
      /* 132 */throw new PaloException(
      /* 133 */"Could not connect to palo server at host '" + host +
      /* 134 */"' on port '" + port + "'", ioex);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean supportsRules() {
    /* 139 */return RuleLoader.supportsRules(this);
    /*      */}

  /*      */
  /*      */public final boolean isConnected() {
    /* 143 */return this.httpClient.isConnected();
    /*      */}

  /*      */
  /*      */public final void disconnect() {
    /*      */try {
      /* 148 */this.httpClient.disconnect();
      /*      */
      /* 150 */stopTimer();
      /*      */} catch (IOException ioex) {
      /* 152 */throw new PaloException(
      /* 153 */"Could not correctly disconnect from palo server!!", ioex);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ConnectionInfo getInfo() {
    /* 158 */return this.connectionInfo;
    /*      */}

  /*      */
  /*      */public final HttpClient getHttpClient() {
    /* 162 */return this.httpClient;
    /*      */}

  /*      */
  /*      */public final synchronized String getSID() {
    /* 166 */return this.sid;
    /*      */}

  /*      */
  /*      */public final String[] send(String request) throws ConnectException,
    IOException
  /*      */{
    /* 171 */return this.httpClient.send(request);
    /*      */}

  /*      */
  /*      */public final synchronized void setServerToken(int srvToken) {
    /* 175 */if (this.srvToken != srvToken)
      /* 176 */fireServerEvent(0);
    /* 177 */this.srvToken = srvToken;
    /*      */}

  /*      */
  /*      */public final synchronized void addServerListener(ServerListener listener)
  /*      */{
    /* 185 */this.listeners.add(listener);
    /*      */}

  /*      */public final synchronized void removeServerListener(
    ServerListener listener) {
    /* 188 */this.listeners.remove(listener);
    /*      */}

  /*      */
  /*      */final synchronized void fireServerEvent(final int type)
  /*      */{
    /* 196 */boolean ownChange = isOwnChange();
    /* 197 */if ((type == 0) && (ownChange)) {
      /* 198 */setOwnChange(false);
      /*      */}
    /* 200 */if (ownChange) {
      /* 201 */return;
      /*      */}
    /* 203 */ServerEvent ev = new ServerEvent() {
      /*      */public int getType() {
        /* 205 */return type;
        /*      */}
      /*      */
    };
    /* 208 */ArrayList copy = new ArrayList(this.listeners);
    /* 209 */for (Iterator it = copy.iterator(); it.hasNext();)
      /* 210 */((ServerListener) it.next()).serverStructureChanged(ev);
    /*      */}

  /*      */
  /*      */public final boolean login(String user, String pass)
  /*      */{
    /* 218 */if ((user == null) || (pass == null)) {
      /* 219 */return false;
      /*      */}
    /*      */
    /* 222 */this.connectionInfo.setData("com.tensegrity.palojava.pass#" + user,
      pass);
    /*      */try
    /*      */{
      /* 226 */MessageDigest md = MessageDigest.getInstance("MD5");
      /* 227 */md.update(pass.getBytes());
      /* 228 */pass = asHexString(md.digest());
      /*      */} catch (NoSuchAlgorithmException ex) {
      /* 230 */throw new PaloException(
        "Failed to create encrypted password for user '" +
        /* 231 */user + "'!", ex);
      /*      */}
    /*      */
    /* 234 */this.connectionInfo.setUser(user);
    /* 235 */this.connectionInfo.setPassword(pass);
    /* 236 */return loginInternal(user, pass);
    /*      */}

  /*      */
  /*      */public final CubeInfo addCube(DatabaseInfo database, String name,
    DimensionInfo[] dimensions)
  /*      */{
    /*      */try {
      /* 242 */setOwnChange(true);
      /* 243 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 244 */return cubeHandler.create(database, name, dimensions);
      /*      */} catch (IOException e) {
      /* 246 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeInfo addCube(DatabaseInfo database, String name,
    DimensionInfo[] dimensions, int type)
  /*      */{
    /*      */try {
      /* 253 */setOwnChange(true);
      /* 254 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 255 */return cubeHandler.create(database, name, dimensions, type);
      /*      */} catch (IOException e) {
      /* 257 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final int convert(CubeInfo cube, int type) {
    /*      */try {
      /* 263 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 264 */return cubeHandler.convert(cube, type);
      /*      */} catch (IOException e) {
      /* 266 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DatabaseInfo addDatabase(String database, int type) {
    /*      */try {
      /* 272 */setOwnChange(true);
      /* 273 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 274 */return dbHandler.create(database, type);
      /*      */} catch (IOException e) {
      /* 276 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DimensionInfo addDimension(DatabaseInfo database,
    String name)
  /*      */{
    /*      */try {
      /* 283 */setOwnChange(true);
      /* 284 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 285 */return dimHandler.create(database, name);
      /*      */} catch (IOException e) {
      /* 287 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DimensionInfo addDimension(DatabaseInfo database,
    String name, int type)
  /*      */{
    /*      */try {
      /* 294 */setOwnChange(true);
      /* 295 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 296 */return dimHandler.create(database, name, type);
      /*      */} catch (IOException e) {
      /* 298 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ElementInfo addElement(DimensionInfo dimension,
    String name, int type, ElementInfo[] children, double[] weights)
  /*      */{
    /*      */try {
      /* 305 */setOwnChange(true);
      /* 306 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 307 */return elHandler.create(dimension, name, type, children, weights);
      /*      */} catch (IOException e) {
      /* 309 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean addElements(DimensionInfo dimension,
    String[] names, int type, ElementInfo[][] children, double[][] weights)
  /*      */{
    /*      */try {
      /* 316 */setOwnChange(true);
      /* 317 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 318 */return elHandler.createBulk(dimension, names, type, children,
        weights);
      /*      */} catch (IOException e) {
      /* 320 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean addElements(DimensionInfo dimension,
    String[] names, int[] types, ElementInfo[][] children, double[][] weights)
  /*      */{
    /* 326 */if (getServerInfo().getMajor() < 3) {
      /* 327 */throw new PaloException(
        "The method 'addElements(...int [] types...)' is only available for Palo Server version 3 or above. Please use 'addElements(...int type...)' instead.");
      /*      */}
    /*      */
    /*      */try
    /*      */{
      /* 332 */setOwnChange(true);
      /* 333 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 334 */return elHandler.createBulk(dimension, names, types, children,
        weights);
      /*      */} catch (IOException e) {
      /* 336 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean replaceBulk(DimensionInfo dimension,
    ElementInfo[] elements, int type, ElementInfo[][] children, Double[][] weights)
  /*      */{
    /* 342 */if ((getServerInfo().getMajor() < 3)
      || (
      /* 343 */(getServerInfo().getMajor() == 3)
        && (getServerInfo().getMinor() == 0) && (getServerInfo().getBuildNumber() < 555))) {
      /* 344 */throw new PaloException(
        "The method 'Hierarchy.updateConsolidations(...)' is only available for Palo Server version 3 or above.");
      /*      */}
    /*      */try
    /*      */{
      /* 348 */setOwnChange(true);
      /* 349 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 350 */return elHandler.replaceBulk(dimension, elements, type, children,
        weights);
      /*      */} catch (IOException e) {
      /* 352 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ElementInfo addElement(HierarchyInfo hierarchy,
    String name, int type, ElementInfo[] children, double[] weights)
  /*      */{
    /*      */try {
      /* 359 */setOwnChange(true);
      /* 360 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 361 */return elHandler.create(hierarchy.getDimension(), name, type,
        children, weights);
      /*      */} catch (IOException e) {
      /* 363 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void addConsolidations(ElementInfo element,
    ElementInfo[] children, double[] weights)
  /*      */{
    /*      */try {
      /* 370 */setOwnChange(true);
      /* 371 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 372 */elHandler.append(element, children, weights);
      /*      */} catch (IOException e) {
      /* 374 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void clear(DimensionInfo dimension) {
    /*      */try {
      /* 380 */setOwnChange(true);
      /* 381 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 382 */dimHandler.clear(dimension);
      /*      */} catch (IOException e) {
      /* 384 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void clear(CubeInfo cube) {
    /*      */try {
      /* 390 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 391 */cubeHandler.clear(cube, null, true);
      /*      */} catch (IOException e) {
      /* 393 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void clear(CubeInfo cube, ElementInfo[][] area) {
    /*      */try {
      /* 399 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 400 */cubeHandler.clear(cube, area, false);
      /*      */} catch (IOException e) {
      /* 402 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(ElementInfo element) {
    /*      */try {
      /* 408 */setOwnChange(true);
      /* 409 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 410 */return elHandler.destroy(element);
      /*      */} catch (IOException e) {
      /* 412 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(ElementInfo[] elements) {
    /*      */try {
      /* 418 */setOwnChange(true);
      /* 419 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 420 */return elHandler.destroy(elements);
      /*      */} catch (IOException e) {
      /* 422 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(CubeInfo cube)
  /*      */{
    /*      */try {
      /* 429 */setOwnChange(true);
      /* 430 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 431 */return cubeHandler.destroy(cube);
      /*      */} catch (IOException e) {
      /* 433 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(DatabaseInfo database) {
    /*      */try {
      /* 439 */setOwnChange(true);
      /* 440 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 441 */return dbHandler.destroy(database);
      /*      */} catch (IOException e) {
      /* 443 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(DimensionInfo dimension) {
    /*      */try {
      /* 449 */setOwnChange(true);
      /* 450 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 451 */return dimHandler.delete(dimension);
      /*      */} catch (IOException e) {
      /* 453 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeInfo[] getCubes(DatabaseInfo database) {
    /*      */try {
      /* 459 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 460 */return dbHandler.getAllCubes(database);
      /*      */} catch (IOException e) {
      /* 462 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeInfo[] getCubes(DatabaseInfo database, int typeMask) {
    /*      */try {
      /* 468 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 469 */return dbHandler.getCubes(database, typeMask);
      /*      */} catch (IOException e) {
      /* 471 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeInfo[] getCubes(DimensionInfo dimension) {
    /*      */try {
      /* 477 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 478 */return dimHandler.getCubes(dimension);
      /*      */} catch (IOException e) {
      /* 480 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CellInfo getData(CubeInfo cube, ElementInfo[] coordinate)
  /*      */{
    /*      */try
    /*      */{
      /* 514 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 515 */CellInfo info = cellHandler.getValue(cube, coordinate);
      /* 516 */return info;
      /*      */} catch (IOException e) {
      /* 518 */throw new PaloException("Failed to receive cell data from cube: " +
      /* 519 */cube.getName(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CellInfo[] getDataArea(CubeInfo cube,
    ElementInfo[][] coordinates)
  /*      */{
    /*      */try {
      /* 526 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 527 */return cellHandler.getCellArea(cube, coordinates);
      /*      */} catch (IOException e) {
      /* 529 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CellInfo[] getDataArray(CubeInfo cube,
    ElementInfo[][] coordinates)
  /*      */{
    /*      */try {
      /* 536 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 537 */return cellHandler.getValues(cube, coordinates);
      /*      */} catch (IOException e) {
      /* 539 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CellInfo[] getDataExport(CubeInfo cube,
    ExportContextInfo exportContext)
  /*      */{
    /*      */try {
      /* 546 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 547 */return cellHandler.export(cube, exportContext);
      /*      */} catch (IOException e) {
      /* 549 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DatabaseInfo[] getDatabases() {
    /*      */try {
      /* 555 */ServerHandler srvHandler = this.handlerRegistry.getServerHandler();
      /* 556 */return srvHandler.getDatabases();
      /*      */} catch (IOException e) {
      /* 558 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DimensionInfo[] getDimensions(DatabaseInfo database)
  /*      */{
    /*      */try {
      /* 565 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 566 */if (database.getType() == 1)
        /* 567 */return dbHandler.getDimensions(database, 4);
      /* 568 */return dbHandler.getAllDimensions(database);
      /*      */} catch (IOException e) {
      /* 570 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public HierarchyInfo[] getHierarchies(DimensionInfo dim) {
    /* 575 */return new HierarchyInfo[] { dim.getDefaultHierarchy() };
    /*      */}

  /*      */
  /*      */public HierarchyInfo getHierarchy(DimensionInfo dimension, String id) {
    /* 579 */return dimension.getDefaultHierarchy();
    /*      */}

  /*      */
  /*      */public final ElementInfo getElementAt(DimensionInfo dimension,
    int position)
  /*      */{
    /*      */try {
      /* 585 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 586 */return dimHandler.getElementAt(dimension, position);
      /*      */} catch (IOException e) {
      /* 588 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ElementInfo getElementAt(HierarchyInfo hierarchy,
    int position)
  /*      */{
    /*      */try {
      /* 595 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 596 */return dimHandler.getElementAt(hierarchy.getDimension(), position);
      /*      */} catch (IOException e) {
      /* 598 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ElementInfo[] getElements(DimensionInfo dimension) {
    /*      */try {
      /* 604 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 605 */return dimHandler.getElements(dimension);
      /*      */} catch (IOException e) {
      /* 607 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ElementInfo[] getElements(HierarchyInfo hierarchy) {
    /*      */try {
      /* 613 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 614 */return dimHandler.getElements(hierarchy.getDimension());
      /*      */} catch (IOException e) {
      /* 616 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DimensionInfo[] getDimensions(DatabaseInfo database,
    int typeMask)
  /*      */{
    /*      */try
    /*      */{
      /* 649 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 650 */return dbHandler.getDimensions(database, typeMask);
      /*      */} catch (IOException e) {
      /* 652 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final ServerInfo getServerInfo() {
    /*      */try {
      /* 658 */ServerHandler srvHandler = this.handlerRegistry.getServerHandler();
      /* 659 */return srvHandler.getInfo();
      /*      */} catch (IOException e) {
      /* 661 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void load(CubeInfo cube)
  /*      */{
    /*      */try
    /*      */{
      /* 685 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 686 */cubeHandler.load(cube);
      /*      */} catch (IOException e) {
      /* 688 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void load(DatabaseInfo database) {
    /*      */try {
      /* 694 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 695 */dbHandler.load(database);
      /*      */} catch (IOException e) {
      /* 697 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void move(ElementInfo element, int newPosition) {
    /*      */try {
      /* 703 */setOwnChange(true);
      /* 704 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 705 */elHandler.move(element, newPosition);
      /*      */} catch (IOException e) {
      /* 707 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final String parseRule(CubeInfo cube, String ruleDefinition,
    String functions)
  /*      */{
    /*      */try {
      /* 714 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 715 */return ruleHandler.parse(cube, ruleDefinition, functions);
      /*      */} catch (IOException e) {
      /* 717 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void reload(CubeInfo cube) {
    /*      */try {
      /* 723 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 724 */cubeHandler.reload(cube);
      /*      */} catch (IOException e) {
      /* 726 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void reload(DatabaseInfo database) {
    /*      */try {
      /* 732 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 733 */dbHandler.reload(database);
      /*      */} catch (IOException e) {
      /* 735 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void reload(DimensionInfo dimension) {
    /*      */try {
      /* 741 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 742 */dimHandler.reload(dimension);
      /*      */} catch (IOException e) {
      /* 744 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void reload(ElementInfo element) {
    /*      */try {
      /* 750 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 751 */elHandler.reload(element);
      /*      */} catch (IOException e) {
      /* 753 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void rename(DatabaseInfo database, String newName) {
    /*      */try {
      /* 759 */setOwnChange(true);
      /* 760 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 761 */dbHandler.rename(database, newName);
      /*      */} catch (IOException e) {
      /* 763 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void rename(ElementInfo element, String newName) {
    /*      */try {
      /* 769 */setOwnChange(true);
      /* 770 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 771 */elHandler.rename(element, newName);
      /*      */} catch (IOException e) {
      /* 773 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void rename(DimensionInfo dimension, String newName)
  /*      */{
    /*      */try {
      /* 780 */setOwnChange(true);
      /* 781 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 782 */dimHandler.rename(dimension, newName);
      /*      */} catch (IOException e) {
      /* 784 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void rename(CubeInfo cube, String newName) {
    /*      */try {
      /* 790 */setOwnChange(true);
      /* 791 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 792 */cubeHandler.rename(cube, newName);
      /*      */} catch (IOException e) {
      /* 794 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean save(CubeInfo cube) {
    /*      */try {
      /* 800 */setOwnChange(true);
      /* 801 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 802 */return cubeHandler.save(cube);
      /*      */} catch (IOException e) {
      /* 804 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean save(DatabaseInfo database) {
    /*      */try {
      /* 810 */setOwnChange(true);
      /* 811 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 812 */return dbHandler.save(database);
      /*      */} catch (IOException e) {
      /* 814 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean save(ServerInfo server) {
    /*      */try {
      /* 820 */setOwnChange(true);
      /* 821 */ServerHandler srvHandler = this.handlerRegistry.getServerHandler();
      /* 822 */return srvHandler.save();
      /*      */} catch (IOException e) {
      /* 824 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void setDataArray(CubeInfo cube,
    ElementInfo[][] coordinates, Object[] values, boolean add, int splashMode,
    boolean notifyEventProcessors)
  /*      */{
    /*      */try
    /*      */{
      /* 843 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 844 */cellHandler.replaceValues(cube, coordinates, values, add,
      /* 845 */splashMode, notifyEventProcessors);
      /*      */} catch (IOException e) {
      /* 847 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void setDataNumericSplashed(CubeInfo cube,
    ElementInfo[] coordinate, double value, int splashMode)
  /*      */{
    /*      */try
    /*      */{
      /* 865 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 866 */cellHandler.replaceValue(cube, coordinate, new Double(value),
      /* 867 */splashMode);
      /*      */} catch (IOException e) {
      /* 869 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void setDataString(CubeInfo cube,
    ElementInfo[] coordinate, String value)
  /*      */{
    /*      */try {
      /* 876 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 877 */cellHandler.replaceValue(cube, coordinate, value,
      /* 878 */0);
      /*      */} catch (IOException e) {
      /* 880 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void unload(CubeInfo cube) {
    /*      */try {
      /* 886 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 887 */cubeHandler.unload(cube);
      /*      */} catch (IOException e) {
      /* 889 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void update(ElementInfo element, int type,
    String[] children, double[] weights, ServerInfo serverInfo)
  /*      */{
    /*      */try
    /*      */{
      /* 905 */setOwnChange(true);
      /* 906 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 907 */elHandler.update(element, type, children, weights, serverInfo);
      /*      */} catch (IOException e) {
      /* 909 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public void update(RuleInfo rule, String definition,
    String externalIdentifier, boolean useIt, String comment, boolean activate)
  /*      */{
    /*      */try
    /*      */{
      /* 917 */setOwnChange(true);
      /* 918 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 919 */ruleHandler.update(rule, definition, externalIdentifier, useIt,
      /* 920 */comment, activate);
      /*      */} catch (IOException e) {
      /* 922 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final void ping() throws PaloException
  /*      */{
    /*      */try
    /*      */{
      /* 930 */ServerHandler srvHandler = this.handlerRegistry.getServerHandler();
      /* 931 */srvHandler.getInfo();
      /*      */} catch (IOException e) {
      /* 933 */throw new PaloException(
      /* 934 */"Connection lost!! Maybe palo server is down.");
      /*      */}
    /*      */}

  /*      */
  /*      */public final String listFunctions()
  /*      */{
    /*      */try {
      /* 941 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 942 */return ruleHandler.listFunctions();
      /*      */} catch (IOException e) {
      /* 944 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final RuleInfo createRule(CubeInfo cube, String definition)
  /*      */{
    /*      */try {
      /* 951 */setOwnChange(true);
      /* 952 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 953 */return ruleHandler.create(cube, definition);
      /*      */} catch (IOException e) {
      /* 955 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final RuleInfo createRule(CubeInfo cube, String definition,
    String externalIdentifier, boolean useIt, String comment, boolean activate)
  /*      */{
    /*      */try
    /*      */{
      /* 963 */setOwnChange(true);
      /* 964 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 965 */return ruleHandler.create(cube, definition, externalIdentifier,
      /* 966 */useIt, comment, activate);
      /*      */} catch (IOException e) {
      /* 968 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(RuleInfo rule) {
    /*      */try {
      /* 974 */setOwnChange(true);
      /* 975 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 976 */return ruleHandler.delete(rule);
      /*      */} catch (IOException e) {
      /* 978 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean delete(String ruleId, CubeInfo cube) {
    /*      */try {
      /* 984 */setOwnChange(true);
      /* 985 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 986 */return ruleHandler.delete(ruleId, cube);
      /*      */} catch (IOException e) {
      /* 988 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final RuleInfo[] getRules(CubeInfo cube) {
    /*      */try {
      /* 994 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 995 */return cubeHandler.getRules(cube);
      /*      */}
    /*      */catch (PaloException pe) {
      /* 998 */return new RuleInfo[0];
      /*      */} catch (IOException e) {
      /* 1000 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final String getRule(CubeInfo cube, ElementInfo[] coordinate) {
    /*      */try {
      /* 1006 */CellHandler cellHandler = this.handlerRegistry.getCellHandler();
      /* 1007 */return cellHandler.getRule(cube, coordinate);
      /*      */} catch (IOException e) {
      /* 1009 */throw new PaloException(
        "Failed to receive rule for cell from cube: " +
        /* 1010 */cube.getName(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final PropertyInfo getProperty(String id)
  /*      */{
    /* 1016 */throw new RuntimeException("Currently not supported.");
    /*      */}

  /*      */
  /*      */public final RuleInfo getRule(CubeInfo cube, String id) {
    /*      */try {
      /* 1021 */RuleHandler ruleHandler = this.handlerRegistry.getRuleHandler();
      /* 1022 */return ruleHandler.getInfo(cube, id);
      /*      */} catch (IOException e) {
      /* 1024 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeInfo getAttributeCube(DimensionInfo dimension)
  /*      */{
    /*      */try {
      /* 1031 */String attrCubeId = dimension.getAttributeCube();
      /* 1032 */if ((attrCubeId == null) || (attrCubeId.equals("")))
        /* 1033 */return null;
      /* 1034 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 1035 */return cubeHandler.getAttributeCube(dimension);
      /*      */} catch (IOException e) {
      /* 1037 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DimensionInfo getAttributeDimension(DimensionInfo dimension) {
    /*      */try {
      /* 1043 */String attrId = dimension.getAttributeDimension();
      /* 1044 */if ((attrId == null) || (attrId.equals("")))
        /* 1045 */return null;
      /* 1046 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 1047 */return dimHandler.getAttributeDimension(dimension);
      /*      */} catch (IOException e) {
      /* 1049 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final DatabaseInfo getDatabase(String id) {
    /*      */try {
      /* 1055 */DatabaseHandler dbHandler = this.handlerRegistry
        .getDatabaseHandler();
      /* 1056 */return dbHandler.getInfo(id);
      /*      */} catch (IOException e) {
      /* 1058 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeInfo getCube(DatabaseInfo database, String id) {
    /*      */try {
      /* 1064 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 1065 */return cubeHandler.getInfo(database, id);
      /*      */} catch (IOException e) {
      /* 1067 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public DimensionInfo getDimension(DatabaseInfo database, String id) {
    /*      */try {
      /* 1073 */DimensionHandler dimHandler = this.handlerRegistry
        .getDimensionHandler();
      /* 1074 */return dimHandler.getInfo(database, id);
      /*      */} catch (IOException e) {
      /* 1076 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public ElementInfo getElement(DimensionInfo dimension, String id) {
    /*      */try {
      /* 1082 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 1083 */return elHandler.getInfo(dimension, id);
      /*      */} catch (IOException e) {
      /* 1085 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public ElementInfo getElement(HierarchyInfo hierarchy, String id) {
    /*      */try {
      /* 1091 */ElementHandler elHandler = this.handlerRegistry.getElementHandler();
      /* 1092 */return elHandler.getInfo(hierarchy.getDimension(), id);
      /*      */} catch (IOException e) {
      /* 1094 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final LockInfo[] getLocks(CubeInfo cube) {
    /*      */try {
      /* 1100 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 1101 */return cubeHandler.listLocks(cube);
      /*      */} catch (IOException e) {
      /* 1103 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final LockInfo requestLock(CubeInfo cube, ElementInfo[][] area) {
    /*      */try {
      /* 1109 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 1110 */return cubeHandler.requestLock(cube, area);
      /*      */}
    /*      */catch (IOException localIOException) {
      /*      */}
    /* 1114 */return null;
    /*      */}

  /*      */
  /*      */public final boolean rollback(CubeInfo cube, LockInfo lock, int steps) {
    /*      */try {
      /* 1119 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 1120 */return cubeHandler.rollback(cube, lock, steps);
      /*      */} catch (IOException e) {
      /* 1122 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final boolean commit(CubeInfo cube, LockInfo lock) {
    /*      */try {
      /* 1128 */CubeHandler cubeHandler = this.handlerRegistry.getCubeHandler();
      /* 1129 */return cubeHandler.commit(cube, lock);
      /*      */} catch (IOException e) {
      /* 1131 */throw new PaloException(e.getLocalizedMessage(), e);
      /*      */}
    /*      */}

  /*      */
  /*      */public final CubeLoader getCubeLoader(DatabaseInfo database)
  /*      */{
    /* 1165 */String key = HttpCubeLoader.class.getName() + database.getId();
    /* 1166 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1167 */if (loader == null) {
      /* 1168 */loader = new HttpCubeLoader(this, database);
      /* 1169 */this.loaders.put(key, loader);
      /*      */}
    /* 1171 */return (HttpCubeLoader) loader;
    /*      */}

  /*      */
  /*      */public DatabaseLoader getDatabaseLoader() {
    /* 1175 */String key = HttpDatabaseLoader.class.getName();
    /* 1176 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1177 */if (loader == null) {
      /* 1178 */loader = new HttpDatabaseLoader(this);
      /* 1179 */this.loaders.put(key, loader);
      /*      */}
    /* 1181 */return (DatabaseLoader) loader;
    /*      */}

  /*      */
  /*      */public DimensionLoader getDimensionLoader(DatabaseInfo database) {
    /* 1185 */String key = HttpDimensionLoader.class.getName() + database.getId();
    /* 1186 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1187 */if (loader == null) {
      /* 1188 */loader = new HttpDimensionLoader(this, database);
      /* 1189 */this.loaders.put(key, loader);
      /*      */}
    /* 1191 */return (HttpDimensionLoader) loader;
    /*      */}

  /*      */
  /*      */public ElementLoader getElementLoader(DimensionInfo dimension) {
    /* 1195 */return getElementLoader(dimension.getDefaultHierarchy());
    /*      */}

  /*      */
  /*      */public ElementLoader getElementLoader(HierarchyInfo hierarchy) {
    /* 1199 */String dbId = hierarchy.getDimension().getDatabase().getId();
    /* 1200 */String key = HttpElementLoader.class.getName() + "#" + dbId + "#"
      + hierarchy.getDimension().getId();
    /* 1201 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1202 */if (loader == null) {
      /* 1203 */loader = new HttpElementLoader(this, hierarchy);
      /* 1204 */this.loaders.put(key, loader);
      /*      */}
    /* 1206 */return (HttpElementLoader) loader;
    /*      */}

  /*      */
  /*      */public FunctionLoader getFunctionLoader() {
    /* 1210 */String key = HttpFunctionLoader.class.getName();
    /* 1211 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1212 */if (loader == null) {
      /* 1213 */loader = new HttpFunctionLoader(this);
      /* 1214 */this.loaders.put(key, loader);
      /*      */}
    /* 1216 */return (HttpFunctionLoader) loader;
    /*      */}

  /*      */
  /*      */public HierarchyLoader getHierarchyLoader(DimensionInfo dimension) {
    /* 1220 */String key = HttpHierarchyLoader.class.getName() + "#" +
    /* 1221 */dimension.getDatabase().getId() + "#" + dimension.getId();
    /* 1222 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1223 */if (loader == null) {
      /* 1224 */loader = new HttpHierarchyLoader(this, dimension);
      /* 1225 */this.loaders.put(key, loader);
      /*      */}
    /* 1227 */return (HttpHierarchyLoader) loader;
    /*      */}

  /*      */
  /*      */public RuleLoader getRuleLoader(CubeInfo cube) {
    /* 1231 */String dbId = cube.getDatabase().getId();
    /* 1232 */String key = HttpRuleLoader.class.getName() + "#" + dbId + "#"
      + cube.getId();
    /* 1233 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1234 */if (loader == null) {
      /* 1235 */loader = new HttpRuleLoader(this, cube);
      /* 1236 */this.loaders.put(key, loader);
      /*      */}
    /* 1238 */return (HttpRuleLoader) loader;
    /*      */}

  /*      */
  /*      */public PropertyLoader getPropertyLoader() {
    /* 1242 */String key = HttpPropertyLoader.class.getName();
    /* 1243 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1244 */if (loader == null) {
      /* 1245 */loader = new HttpPropertyLoader(this);
      /* 1246 */this.loaders.put(key, loader);
      /*      */}
    /* 1248 */return (HttpPropertyLoader) loader;
    /*      */}

  /*      */
  /*      */public PropertyLoader getTypedPropertyLoader(PaloInfo paloObject) {
    /* 1252 */String key = HttpPropertyLoader.class.getName() + "#" +
    /* 1253 */paloObject.getId();
    /* 1254 */PaloInfoLoader loader = (PaloInfoLoader) this.loaders.get(key);
    /* 1255 */if (loader == null) {
      /* 1256 */loader = new HttpPropertyLoader(this, paloObject);
      /* 1257 */this.loaders.put(key, loader);
      /*      */}
    /* 1259 */return (HttpPropertyLoader) loader;
    /*      */}

  /*      */
  /*      */public PropertyLoader getPropertyLoader(DatabaseInfo database) {
    /* 1263 */return null;
    /*      */}

  /*      */
  /*      */public PropertyLoader getPropertyLoader(DimensionInfo dimension)
  /*      */{
    /* 1268 */return null;
    /*      */}

  /*      */
  /*      */public PropertyLoader getPropertyLoader(RuleInfo rule)
  /*      */{
    /* 1273 */return null;
    /*      */}

  /*      */
  /*      */public PropertyLoader getPropertyLoader(ElementInfo element)
  /*      */{
    /* 1278 */return null;
    /*      */}

  /*      */
  /*      */final void serverDown()
  /*      */{
    /*      */try
    /*      */{
      /* 1286 */this.httpClient.disconnect();
      /* 1287 */fireServerEvent(8);
      /*      */} catch (IOException ex) {
      /* 1289 */throw new PaloException("Could not disconnect...");
      /*      */}
    /*      */}

  /*      */
  /*      */final synchronized void ensureConnection()
  /*      */throws IOException
  /*      */{
    /* 1299 */long currTime = System.currentTimeMillis();
    /* 1300 */if (this.ttl - currTime >= 20000L) {
      /*      */return;
      /*      */}
    /* 1303 */loginInternal(this.connectionInfo.getUsername(),
    /* 1304 */this.connectionInfo.getPassword());
    /*      */}

  /*      */
  /*      */final void reconnect()
  /*      */throws IOException
  /*      */{
    /* 1315 */reconnect(this.timeout);
    /*      */}

  /*      */
  /*      */final void reconnect(int timeout)
  /*      */throws IOException
  /*      */{
    /* 1323 */if (this.httpClient == null)
      /* 1324 */return;
    /* 1325 */this.httpClient.reconnect(timeout);
    /* 1326 */loginInternal(this.connectionInfo.getUsername(),
    /* 1327 */this.connectionInfo.getPassword());
    /*      */}

  /*      */
  /*      */private final synchronized void stopTimer()
  /*      */{
    /* 1333 */if (this.httpTimer != null) {
      /* 1334 */this.httpTimer.cancel();
      /* 1335 */this.httpTimer = null;
      /*      */}
    /*      */}

  /*      */
  /*      */private final synchronized void startTimer()
  /*      */{
    /* 1344 */stopTimer();
    /* 1345 */ConnectionTimerTask task = new ConnectionTimerTask(this);
    /* 1346 */this.httpTimer = new Timer();
    /* 1347 */this.httpTimer.scheduleAtFixedRate(task, 1000L, 20000L);
    /*      */}

  /*      */
  /*      */private final synchronized boolean loginInternal(String user,
    String pass) {
    /* 1351 */long currTime = System.currentTimeMillis();
    /* 1352 */ServerHandler srvHandler = this.handlerRegistry.getServerHandler();
    /*      */try {
      /* 1354 */String[] loginInfo = srvHandler.login(user, pass);
      /*      */
      /* 1356 */this.sid = loginInfo[0];
      /*      */
      /* 1358 */this.ttl = Long.parseLong(loginInfo[1]);
      /* 1359 */this.ttl = (currTime + this.ttl * 1000L);
      /*      */
      /* 1362 */startTimer();
      /* 1363 */return true;
      /*      */} catch (IOException e) {
      /* 1365 */throw new PaloException(e.getLocalizedMessage(), e);
    } catch (PaloException pe) {
      /*      */}
    /* 1367 */return false;
    /*      */}

  /*      */
  /*      */private final String asHexString(byte[] bytes)
  /*      */{
    /* 1377 */StringBuffer result = new StringBuffer();
    /* 1378 */for (int i = 0; i < bytes.length; ++i) {
      /* 1379 */result.append(
      /* 1380 */Integer.toHexString(256 + (bytes[i] & 0xFF)).substring(1));
      /*      */}
    /* 1382 */return result.toString();
    /*      */}

  /*      */
  /*      */private final synchronized void setOwnChange(boolean ownChange) {
    /* 1386 */this.ownChange = ownChange;
    /*      */}

  /*      */
  /*      */private final synchronized boolean isOwnChange() {
    /* 1390 */return this.ownChange;
    /*      */}

  /*      */
  /*      */public PropertyInfo createNewProperty(String id, String value,
    PropertyInfo parent, int type, boolean readOnly)
  /*      */{
    /* 1395 */return new PropertyInfoImpl(id, value, parent, type, readOnly);
    /*      */}
  /*      */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.HttpConnection JD-Core Version:
 * 0.5.4
 */