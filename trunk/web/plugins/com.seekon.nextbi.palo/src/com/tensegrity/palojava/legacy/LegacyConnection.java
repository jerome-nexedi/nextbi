/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy;

import java.util.HashMap;
import java.util.Map;

import com.tensegrity.palojava.CellInfo;
import com.tensegrity.palojava.ConnectionInfo;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DatabaseInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.ElementInfo;
import com.tensegrity.palojava.ExportContextInfo;
import com.tensegrity.palojava.HierarchyInfo;
import com.tensegrity.palojava.LicenseInfo;
import com.tensegrity.palojava.LockInfo;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.PaloInfo;
import com.tensegrity.palojava.PropertyInfo;
import com.tensegrity.palojava.RuleInfo;
import com.tensegrity.palojava.ServerInfo;
import com.tensegrity.palojava.events.ServerListener;
import com.tensegrity.palojava.impl.ConnectionInfoImpl;
import com.tensegrity.palojava.impl.ServerInfoImpl;
import com.tensegrity.palojava.legacy.builders.CellInfoBuilder;
import com.tensegrity.palojava.legacy.builders.CubeInfoBuilder;
import com.tensegrity.palojava.legacy.builders.DatabaseInfoBuilder;
import com.tensegrity.palojava.legacy.builders.DimensionInfoBuilder;
import com.tensegrity.palojava.legacy.builders.ElementInfoBuilder;
import com.tensegrity.palojava.loader.CubeLoader;
import com.tensegrity.palojava.loader.DatabaseLoader;
import com.tensegrity.palojava.loader.DimensionLoader;
import com.tensegrity.palojava.loader.ElementLoader;
import com.tensegrity.palojava.loader.FunctionLoader;
import com.tensegrity.palojava.loader.HierarchyLoader;
import com.tensegrity.palojava.loader.PropertyLoader;
import com.tensegrity.palojava.loader.RuleLoader;

/**
 * <code>LegacyConnection</code>
 * <p>
 * An implementation of the <code>DbConnection</code> interface which is based
 * on the old C API of the palo server
 * </p>
 * <p><b>Important note:</b>
 * The old palo server does not support the complete functionality of the new
 * palo server. Therefore its usage is not recommended!!
 * </p>
 * @author Arnd Houben
 * @author Stepan Rutz
 * @version $Id: LegacyConnection.java,v 1.16 2007/11/27 13:47:04 PhilippBouillon Exp $
 */
public class LegacyConnection implements DbConnection {

  private static final String NOT_CONNECTED = "Not connected to Palo-Server.";

  private static final int ADDORUPDATE_MODE_ADD = 0, ADDORUPDATE_MODE_FORCE_ADD = 1,
    ADDORUPDATE_MODE_UPDATE = 2, ADDORUPDATE_MODE_ADD_OR_UPDATE = 3;

  //LEGACY SPLASH MODES:
  public static final int SPLASH_MODE_DISABLED = 0, SPLASH_MODE_UNKNOWN = 4;

  /** recompute the values of the consolidated elements */
  public static final int SPLASH_MODE_DEFAULT = 1;

  /** set the value as the new value of the consolidated elements */
  public static final int SPLASH_MODE_BASE_SET = 2;

  /** adds the new value to the values of the consolidated elements */
  public static final int SPLASH_MODE_BASE_ADD = 3;

  private final ConnectionInfoImpl connectionInfo;

  private final ServerInfo srvInfo;

  /** return values from the C-Api */
  private int descriptor;

  private String errorMessage;

  private final String server;

  private final String service;

  private String username;

  private String password;

  LegacyConnection(String server, String service) throws PaloException {

    this.server = server;
    this.service = service;

    connectionInfo = new ConnectionInfoImpl(server, service, username, password);
    srvInfo = new ServerInfoImpl(-1, -1, 1, 1, 0, 2, true);
    descriptor = -1;
  }

  public final synchronized CubeInfo addCube(DatabaseInfo database, String name,
    DimensionInfo[] dimensions) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String[] dimNames = new String[dimensions.length];
    for (int i = 0; i < dimNames.length; i++)
      dimNames[i] = dimensions[i].getName();
    databaseAddCube0(descriptor, database.getName(), name, dimNames);
    if (getErrorMessage() != null) {
      throw new PaloException("addCube failed: " + getErrorMessage());
    }
    //create info object:
    CubeInfoBuilder cubeBuilder = CubeInfoBuilder.getInstance();
    cubeBuilder.setName(name);
    cubeBuilder.setDatabase(database);
    cubeBuilder.setDimensions(dimNames);
    return cubeBuilder.create();
  }

  public final synchronized DatabaseInfo addDatabase(String database) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    addDatabase0(descriptor, database);
    if (getErrorMessage() != null) {
      throw new PaloException("addDatabase failed: " + getErrorMessage());
    }
    DatabaseInfoBuilder databaseBuilder = DatabaseInfoBuilder.getInstance();
    databaseBuilder.setName(database);
    return databaseBuilder.create();
  }

  public final synchronized DimensionInfo addDimension(DatabaseInfo database,
    String name) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);

    databaseAddDimension0(descriptor, database.getName(), name);
    if (getErrorMessage() != null) {
      throw new PaloException("addDimension failed for database '"
        + database.getName() + "' and dimension '" + name + "': "
        + getErrorMessage());
    }
    // create dimension info object:
    DimensionInfoBuilder dimensionBuilder = DimensionInfoBuilder.getInstance();
    dimensionBuilder.setName(name);
    dimensionBuilder.setDatabase(database);
    return dimensionBuilder.create();
  }

  public final synchronized ElementInfo addElement(DimensionInfo dimension,
    String name, int type, ElementInfo[] children, double[] weights) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);

    String database = dimension.getDatabase().getName();
    ConsolidationInfo[] ci = createConsolidationInfos(children, weights);
    dimensionAddOrUpdateDimElement0(descriptor, database, dimension.getName(), name,
      ADDORUPDATE_MODE_ADD, type, ci, true);
    if (getErrorMessage() != null) {
      throw new PaloException("addElement failed: " + getErrorMessage());
    }
    return createElement(dimension, name, type, children, weights);
  }

  public void addServerListener(ServerListener listener) {
    throw new PaloException("Not supported by legacy server!!");
  }

  public final void addConsolidations(ElementInfo element, ElementInfo[] children,
    double[] weights) {
    throw new PaloException("Not supported by legacy server!!");
  }

  public final synchronized void clear(DimensionInfo dimension) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = dimension.getDatabase().getName();
    clearDimension0(descriptor, database, dimension.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("clear dimension failed: " + getErrorMessage());
    }
  }

  public final synchronized boolean delete(ElementInfo element) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    DimensionInfo dimension = element.getDimension();
    String database = dimension.getDatabase().getName();
    dimElementDelete0(descriptor, database, dimension.getName(), element.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("element delete failed: " + getErrorMessage());
    }
    return true;
  }

  public final synchronized boolean delete(CubeInfo cube) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = cube.getDatabase().getName();
    deleteCube0(descriptor, database, cube.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("deleteCube failed: " + getErrorMessage());
    }
    return true;
  }

  public final synchronized boolean delete(DatabaseInfo database) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    deleteDatabase0(descriptor, database.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("deleteDatabase failed: " + getErrorMessage());
    }
    return true;
  }

  public final synchronized boolean delete(DimensionInfo dimension) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = dimension.getDatabase().getName();
    deleteDimension0(descriptor, database, dimension.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("delete dimension failed: " + getErrorMessage());
    }
    return true;
  }

  public final synchronized CubeInfo[] getCubes(DatabaseInfo database) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String _cubes[] = databaseListCubes0(descriptor, database.getName());
    if (_cubes == null || getErrorMessage() != null) {
      throw new PaloException("getCubes() failed: " + getErrorMessage());
    }

    CubeInfo[] cubes = new CubeInfo[_cubes.length];
    for (int i = 0; i < cubes.length; ++i) {
      CubeInfoBuilder cubeBuilder = CubeInfoBuilder.getInstance();
      String[] dimensions = cubeListDimensions0(descriptor, database.getName(),
        _cubes[i]);
      cubeBuilder.setName(_cubes[i]);
      cubeBuilder.setDatabase(database);
      cubeBuilder.setDimensions(dimensions);
      cubes[i] = cubeBuilder.create();
    }
    return cubes;
  }

  public final synchronized CubeInfo[] getCubes(DimensionInfo dimension) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    DatabaseInfo database = dimension.getDatabase();
    String _cubes[] = dimensionListCubes0(descriptor, database.getName(), dimension
      .getName());
    if (_cubes == null || getErrorMessage() != null) {
      throw new PaloException("getCubes() failed: " + getErrorMessage());
    }
    CubeInfo[] cubes = new CubeInfo[_cubes.length];
    for (int i = 0; i < cubes.length; ++i) {
      CubeInfoBuilder cubeBuilder = CubeInfoBuilder.getInstance();
      String[] dimensions = cubeListDimensions0(descriptor, database.getName(),
        _cubes[i]);
      cubeBuilder.setName(_cubes[i]);
      cubeBuilder.setDatabase(database);
      cubeBuilder.setDimensions(dimensions);
      cubes[i] = cubeBuilder.create();
    }
    return cubes;
  }

  public final synchronized CellInfo getData(CubeInfo cube, ElementInfo[] coordinate) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = cube.getDatabase().getName();
    String[] _coordinate = new String[coordinate.length];
    for (int i = 0; i < coordinate.length; ++i)
      _coordinate[i] = coordinate[i].getName();
    Object val = getData0(descriptor, database, cube.getName(), _coordinate.length,
      _coordinate);
    if (val == null && getErrorMessage() != null) {
      throw new PaloException("getData failed: " + getErrorMessage());
    }
    CellInfoBuilder cellBuilder = CellInfoBuilder.getInstance();
    cellBuilder.setValue(val);
    cellBuilder.setCoordinate(_coordinate);
    return cellBuilder.create();
  }

  public final synchronized CellInfo[] getDataArea(CubeInfo cube,
    ElementInfo[][] coordinates) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = cube.getDatabase().getName();
    String[][] coords = new String[coordinates.length][];
    for (int i = 0; i < coords.length; ++i) {
      coords[i] = new String[coordinates[i].length];
      for (int j = 0; j < coords[i].length; j++)
        coords[i][j] = coordinates[i][j].getName();
    }
    Object val[] = getDataArea0(descriptor, database, cube.getName(), coords);
    if (val == null && getErrorMessage() != null) {
      throw new PaloException("getDataArea failed: " + getErrorMessage());
    }
    CellInfo[] cells = new CellInfo[val.length];
    for (int i = 0; i < cells.length; ++i) {
      CellInfoBuilder cellBuilder = CellInfoBuilder.getInstance();
      cellBuilder.setValue(val);
      cellBuilder.setCoordinate(coords[i]);
      cells[i] = cellBuilder.create();

    }
    return cells;
  }

  public final synchronized CellInfo[] getDataArray(CubeInfo cube,
    ElementInfo[][] coordinates) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized CellInfo[] getDataExport(CubeInfo cube,
    ExportContextInfo context) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    throw new PaloException("Not supported by legacy server");
    //TODO currently we have problems with underlying c-code...
    //		Object o[][] = getDataExport0(descriptor, database, cube, context);
    //		if (o == null && getErrorMessage() != null) {
    //			throw new PaloException("getDataExport failed: " + getErrorMessage());
    //		}
    //		return o;
  }

  public DatabaseInfo[] getDatabases() {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String databases[] = rootListDatabases0(descriptor);
    if (databases == null || getErrorMessage() != null) {
      throw new PaloException("getDatabases() failed: " + getErrorMessage());
    }
    DatabaseInfo[] dbInfos = new DatabaseInfo[databases.length];
    DatabaseInfoBuilder databaseBuilder = DatabaseInfoBuilder.getInstance();
    for (int i = 0; i < dbInfos.length; ++i) {
      databaseBuilder.setName(databases[i]);
      dbInfos[i] = databaseBuilder.create();

    }
    return dbInfos;
  }

  public final synchronized DimensionInfo[] getDimensions(DatabaseInfo database) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String dimensions[] = databaseListDimensions0(descriptor, database.getName());
    if (dimensions == null || getErrorMessage() != null) {
      throw new PaloException("getDimensions() failed: " + getErrorMessage());
    }
    DimensionInfo[] dimInfos = new DimensionInfo[dimensions.length];
    // create dimension info objects:
    DimensionInfoBuilder dimensionBuilder = DimensionInfoBuilder.getInstance();
    for (int i = 0; i < dimInfos.length; ++i) {
      dimensionBuilder.setName(dimensions[i]);
      dimensionBuilder.setDatabase(database);
      dimInfos[i] = dimensionBuilder.create();
    }
    return dimInfos;
  }

  public final synchronized HierarchyInfo[] getHierarchies(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized DimensionInfo[] getDimensions(HierarchyInfo hierarchy) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized ElementInfo getElementAt(DimensionInfo dimension,
    int position) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized ElementInfo[] getElements(DimensionInfo dimension) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);

    String database = dimension.getDatabase().getName();
    DimElementInfo[] infos = dimensionListDimElements0(descriptor, database,
      dimension.getName());
    if (infos == null || getErrorMessage() != null) {
      throw new PaloException("getElements() failed: " + getErrorMessage());
    }

    Map typeMap = createTypeMap(infos);
    ElementInfo[] elInfos = new ElementInfo[infos.length];
    //create element info object:
    for (int i = 0; i < elInfos.length; ++i) {
      //			ConsolidationInfo[] ci = dimElementListConsolidated0(descriptor, database, dimension.getName(), infos[i].getName());
      //			elInfos[i] = createElement(dimension, infos[i].getName(), infos[i]
      //					.getType(), ci);
      elInfos[i] = createElement(database, dimension, infos[i].getName(), infos[i]
        .getType(), typeMap);
    }
    return elInfos;
  }

  public final synchronized ConnectionInfo getInfo() {
    return connectionInfo;
  }

  public final synchronized CubeInfo[] getNormalCubes(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized DatabaseInfo[] getNormalDatabases() {
    throw new PaloException("Not supported by legacy server");
  }

  public final DimensionInfo[] getNormalDimensions(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized ServerInfo getServerInfo() {
    return srvInfo;
  }

  public final synchronized CubeInfo[] getSystemCubes(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized DatabaseInfo[] getSystemDatabases() {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized boolean isConnected() {
    return descriptor != -1;
  }

  public final synchronized void load(CubeInfo cube) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = cube.getDatabase().getName();
    cubeLoad0(descriptor, database, cube.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("cube load failed: " + getErrorMessage());
    }
  }

  public final synchronized void load(DatabaseInfo database) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    databaseLoad0(descriptor, database.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("databaseLoad failed: " + getErrorMessage());
    }
  }

  public final synchronized boolean login(String user, String password) {
    connectionInfo.setUser(user);
    connectionInfo.setPassword(password);
    this.username = user;
    this.password = password;
    descriptor = connect0();
    if (descriptor == -1) {
      throw new PaloException("Failed to connect to Palo at '"
        + connectionInfo.getHost() + ":" + connectionInfo.getPort()
        + "' using username '" + connectionInfo.getUsername() + "'");
    }
    return descriptor != -1;
  }

  public final synchronized void move(ElementInfo element, int newPosition) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    DimensionInfo dimension = element.getDimension();
    String database = dimension.getDatabase().getName();
    dimElementMove0(descriptor, database, dimension.getName(), element.getName(),
      newPosition);
    if (getErrorMessage() != null) {
      throw new PaloException("element move failed: " + getErrorMessage());
    }
  }

  public final synchronized String parseRule(CubeInfo cube, String ruleDefinition,
    String functions) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized void ping() throws PaloException {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    ping0(descriptor);
    if (getErrorMessage() != null) {
      throw new PaloException("ping failed: " + getErrorMessage());
    }
  }

  public final void reload(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public final void reload(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public final void reload(DimensionInfo dimension) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized void reload(ElementInfo element) {
    throw new PaloException("Not supported by legacy server");
    //		if (descriptor == -1)
    //			throw new PaloException(NOT_CONNECTED);
    //		DimensionInfo dimension = element.getDimension();
    //		DatabaseInfo database = dimension.getDatabase();
    //		DimElementInfo[] infos = dimensionListDimElements0(descriptor, database
    //				.getName(), dimension.getName());
    //		if (infos == null || getErrorMessage() != null) 
    //			throw new PaloException("reload() failed: " + getErrorMessage());
    //		
    //		//create element info object:
    //		Map typeMap = createTypeMap(infos);
    //		String elName = element.getName();
    //		for(int i=0;i<infos.length;++i) {
    //			if(!infos[i].getName().equals(elName))
    //				continue;
    ////			ConsolidationInfo[] ci = dimElementListConsolidated0(descriptor, database.getName(), dimension.getName(), infos[i].getName());
    ////			return createElement(dimension, infos[i].getName(), infos[i].getType(), ci);
    //			return createElement(database.getName(),dimension, infos[i].getName(), infos[i].getType(),typeMap);
    //		}
    //		return null;
  }

  public final synchronized void removeServerListener(ServerListener listener) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized void rename(ElementInfo element, String newName) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    DimensionInfo dimension = element.getDimension();
    String database = dimension.getDatabase().getName();
    dimElementRename0(descriptor, database, dimension.getName(), element.getName(),
      newName);
    if (getErrorMessage() != null) {
      throw new PaloException("rename element failed: " + getErrorMessage());
    }
  }

  public final synchronized void rename(DimensionInfo dimension, String newName) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = dimension.getDatabase().getName();
    databaseRenameDimension0(descriptor, database, dimension.getName(), newName);
    if (getErrorMessage() != null) {
      throw new PaloException("rename dimension failed: " + getErrorMessage());
    }
  }

  public final synchronized boolean save(DatabaseInfo database) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    databaseSave0(descriptor, database.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("database save failed: " + getErrorMessage());
    }
    return true;
  }

  public final synchronized boolean save(ServerInfo server) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    rootSave0(descriptor);
    if (getErrorMessage() != null) {
      throw new PaloException("server save failed: " + getErrorMessage());
    }
    return true;
  }

  public final synchronized boolean save(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public final synchronized void setDataArray(CubeInfo cube,
    ElementInfo[][] coordinates, Object[] values, boolean add, int splashMode,
    boolean notifyEventProcessors) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);

    //we have to break that down to single setData...
    for (int i = 0; i < coordinates.length; ++i) {
      if (values[i] instanceof Number) {
        double value = ((Number) values[i]).doubleValue();
        setDataNumericSplashed(cube, coordinates[i], value, splashMode);
      } else {
        setDataString(cube, coordinates[i], values[i].toString());
      }
    }
  }

  public final synchronized void setDataNumericSplashed(CubeInfo cube,
    ElementInfo[] coordinate, double value, int splashMode) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String[] _coordinate = new String[coordinate.length];
    for (int i = 0; i < _coordinate.length; ++i)
      _coordinate[i] = coordinate[i].getName();
    String database = cube.getDatabase().getName();
    setDataNumericSplashed0(descriptor, database, cube.getName(), coordinate.length,
      _coordinate, value, splashMode);
    if (getErrorMessage() != null) {
      throw new PaloException("setDataNumericSplashed failed: " + getErrorMessage());
    }
  }

  public final synchronized void setDataString(CubeInfo cube,
    ElementInfo[] coordinate, String value) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String[] _coordinate = new String[coordinate.length];
    for (int i = 0; i < _coordinate.length; ++i)
      _coordinate[i] = coordinate[i].getName();
    String database = cube.getDatabase().getName();
    setDataString0(descriptor, database, cube.getName(), coordinate.length,
      _coordinate, value);
    if (getErrorMessage() != null) {
      throw new PaloException("setDataString failed: " + getErrorMessage());
    }
  }

  public final synchronized void unload(CubeInfo cube) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    String database = cube.getDatabase().getName();
    unloadCube0(descriptor, database, cube.getName());
    if (getErrorMessage() != null) {
      throw new PaloException("unload cube failed: " + getErrorMessage());
    }

  }

  public final synchronized void update(ElementInfo element, int type,
    String[] childrenIds, double[] weights) {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    DimensionInfo dimension = element.getDimension();
    String database = dimension.getDatabase().getName();
    ConsolidationInfo[] ci = createConsolidationInfos(childrenIds, weights);
    dimensionAddOrUpdateDimElement0(descriptor, database, dimension.getName(),
      element.getName(), ADDORUPDATE_MODE_UPDATE, type, ci, true);
    if (getErrorMessage() != null) {
      throw new PaloException("addElement failed: " + getErrorMessage());
    }
  }

  public String listFunctions() {
    throw new PaloException("Not supported by legacy server");
  }

  public RuleInfo createRule(CubeInfo cube, String definition) {
    throw new PaloException("Not supported by legacy server");
  }

  public RuleInfo createRule(CubeInfo cube, String definition,
    String externalIdentifier, boolean useIt, String comment) {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean delete(RuleInfo rule) {
    throw new PaloException("Not supported by legacy server");
  }

  public RuleInfo[] getRules(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public String getRule(CubeInfo cube, ElementInfo[] coordinate) {
    throw new PaloException("Not supported by legacy server");
  }

  public void disconnect() {
    if (descriptor == -1)
      throw new PaloException(NOT_CONNECTED);
    disconnect0(descriptor);
    descriptor = -1;
  }

  public CubeInfo getAttributeCube(DimensionInfo dimension) {
    throw new PaloException("Not supported by legacy server");
  }

  public DimensionInfo getAttributeDimension(DimensionInfo dimension) {
    throw new PaloException("Not supported by legacy server");
  }

  //--------------------------------------------------------------------------
  // REQUIRED FOR LEGACY CONNECTION
  //
  public final synchronized String getServer() {
    return connectionInfo.getHost();
  }

  public final synchronized String getService() {
    return connectionInfo.getPort();
  }

  public final synchronized String getUsername() {
    return connectionInfo.getUsername();
  }

  public final synchronized String getPassword() {
    return connectionInfo.getPassword();
  }

  /**
   * Returns the current error message
   * @return the error message
   */
  public final synchronized String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Sets the specified message as new error message
   * @param errorMessage the new error message
   */
  public final synchronized void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  private final ConsolidationInfo[] createConsolidationInfos(ElementInfo[] children,
    double[] weights) {
    ConsolidationInfo[] cInfos = new ConsolidationInfo[children.length];
    for (int i = 0; i < cInfos.length; ++i)
      cInfos[i] = new ConsolidationInfo(children[i].getName(), -1, weights[i]);
    return cInfos;
  }

  private final ConsolidationInfo[] createConsolidationInfos(String[] children,
    double[] weights) {
    ConsolidationInfo[] cInfos = new ConsolidationInfo[children.length];
    for (int i = 0; i < cInfos.length; ++i)
      cInfos[i] = new ConsolidationInfo(children[i], -1, weights[i]);
    return cInfos;
  }

  private final ElementInfo createElement(DimensionInfo dimension, String name,
    int type, ElementInfo[] children, double[] weights) {
    //create element info object:
    ElementInfoBuilder elementBuilder = ElementInfoBuilder.getInstance();
    elementBuilder.setName(name);
    elementBuilder.setType(type);
    elementBuilder.setDimension(dimension);
    elementBuilder.setChildren(children);
    elementBuilder.setWeights(weights);
    String database = dimension.getDatabase().getName();
    String dimName = dimension.getName();
    elementBuilder.setIndent(elementIndent0(descriptor, database, dimName, name));
    elementBuilder.setPosition(elementIndex0(descriptor, database, dimName, name));
    elementBuilder.setLevel(elementLevel0(descriptor, database, dimName, name));
    //		elementBuilder.setLevel(elementTopLevel0(descriptor, database, dimension));

    return elementBuilder.create();
  }

  //	private final ElementInfo createElement(DimensionInfo dimension, String name, int type, ConsolidationInfo[] consolidations) {
  //		ElementInfoBuilder elementBuilder = ElementInfoBuilder.getInstance();
  //		ElementInfo[] children = new ElementInfo[consolidations.length];
  //		double[] weights = new double[consolidations.length];
  //		for(int i=0;i<consolidations.length;++i) {
  //			children[i] = createElement(dimension)
  //			weights[i] = consolidations[i].getFactor();
  //		}
  //		elementBuilder.setName(name);
  //		elementBuilder.setType(type);
  //		elementBuilder.setDimension(dimension);
  //		elementBuilder.setChildren(children);
  //		elementBuilder.setWeights(weights);
  //		String database = dimension.getDatabase().getName();
  //		String dimName = dimension.getName();
  //		elementBuilder.setIndent(elementIndent0(descriptor, database, dimName, name));
  //		elementBuilder.setPosition(elementIndex0(descriptor, database, dimName, name));
  //		elementBuilder.setLevel(elementLevel0(descriptor, database, dimName, name));
  ////		elementBuilder.setLevel(elementTopLevel0(descriptor, database, dimension));
  //		
  //		return elementBuilder.create();
  //	}
  //	
  private final ElementInfo createElement(String database, DimensionInfo dimension,
    String name, int type, Map types) {
    ElementInfoBuilder elementBuilder = ElementInfoBuilder.getInstance();
    ConsolidationInfo[] ci = dimElementListConsolidated0(descriptor, database,
      dimension.getName(), name);
    ElementInfo[] children = new ElementInfo[ci.length];
    double[] weights = new double[ci.length];
    for (int i = 0; i < ci.length; ++i) {
      String child = ci[i].getName();
      int childType = ((Integer) types.get(child)).intValue();
      children[i] = createElement(database, dimension, child, childType, types);
      weights[i] = ci[i].getFactor();
    }
    elementBuilder.setName(name);
    elementBuilder.setType(type);
    elementBuilder.setDimension(dimension);
    elementBuilder.setChildren(children);
    elementBuilder.setWeights(weights);
    String dimName = dimension.getName();
    elementBuilder.setIndent(elementIndent0(descriptor, database, dimName, name));
    elementBuilder.setPosition(elementIndex0(descriptor, database, dimName, name));
    elementBuilder.setLevel(elementLevel0(descriptor, database, dimName, name));
    //		elementBuilder.setLevel(elementTopLevel0(descriptor, database, dimension));

    return elementBuilder.create();
  }

  private final Map createTypeMap(DimElementInfo[] elInfos) {
    Map types = new HashMap();
    for (int i = 0; i < elInfos.length; ++i)
      types.put(elInfos[i].getName(), new Integer(elInfos[i].getType()));
    return types;
  }

  //--------------------------------------------------------------------------
  // NATIVE STUBS
  //
  private native int connect0();

  private native void disconnect0(int descriptor);

  private native void ping0(int descriptor);

  private native Object getData0(int descriptor, String database, String cube,
    int nCoordinates, String coordinates[]);

  private native Object[] getDataArea0(int descriptor, String database, String cube,
    String[][] elements);

  private native Object[][] getDataExport0(int descriptor, String database,
    String cube, ExportContextInfo context);

  private native void setDataString0(int descriptor, String database, String cube,
    int nCoordinates, String coordinates[], String value) throws PaloException;

  private native void setDataNumeric0(int descriptor, String database, String cube,
    int nCoordinates, String coordinates[], double value) throws PaloException;

  private native String[] rootListDatabases0(int descriptor);

  private native String[] databaseListCubes0(int descriptor, String database);

  private native String[] databaseListDimensions0(int descriptor, String database);

  private native String[] cubeListDimensions0(int descriptor, String database,
    String cube);

  private native String[] dimensionListCubes0(int descriptor, String database,
    String dimension);

  private native DimElementInfo[] dimensionListDimElements0(int descriptor,
    String database, String dimension);

  private native ConsolidationInfo[] dimElementListConsolidated0(int descriptor,
    String database, String dimension, String element);

  private native int elementChildCount0(int descriptor, String database,
    String dimension, String element);

  private native int elementParentCount0(int descriptor, String database,
    String dimension, String element);

  private native int elementCount0(int descriptor, String database, String dimension);

  private native int elementIndex0(int descriptor, String database,
    String dimension, String element);

  private native int elementIsChild0(int descriptor, String database,
    String dimension, String parent, String child);

  private native int elementLevel0(int descriptor, String database,
    String dimension, String element);

  private native int elementIndent0(int descriptor, String database,
    String dimension, String element);

  private native int elementTopLevel0(int descriptor, String database,
    String dimension);

  private native void addDatabase0(int descriptor, String database);

  private native void unloadDatabase0(int descriptor, String database);

  private native void deleteDatabase0(int descriptor, String database);

  private native void unloadDimension0(int descriptor, String database,
    String dimension);

  private native void deleteDimension0(int descriptor, String database,
    String dimension);

  private native void clearDimension0(int descriptor, String database,
    String dimension);

  private native void databaseAddCube0(int descriptor, String database, String cube,
    String dimensions[]);

  private native void databaseAddDimension0(int descriptor, String database,
    String dimension);

  private native void databaseRenameDimension0(int descriptor, String database,
    String dimensionOldname, String dimensionNewname);

  private native void dimensionAddOrUpdateDimElement0(int descriptor,
    String database, String dimension, String element, int addOrUpdateMode,
    int dimElementType, ConsolidationInfo ci[], boolean append);

  private native void dimElementRename0(int descriptor, String database,
    String dimension, String element, String newName);

  private native void dimElementMove0(int descriptor, String database,
    String dimension, String element, int newPosition);

  private native void dimElementDelete0(int descriptor, String database,
    String dimension, String element);

  private native void cubeLoad0(int descriptor, String database, String cube);

  private native void databaseLoad0(int descriptor, String database);

  private native void cubeCommitLog0(int descriptor, String database, String cube);

  private native void databaseSave0(int descriptor, String database);

  private native void rootSave0(int descriptor);

  private native void dimensionProcess0(int descriptor, String database,
    String dimension);

  private native void unloadCube0(int descriptor, String database, String cube);

  private native void deleteCube0(int descriptor, String database, String cube);

  private native void dimensionCommitLog0(int descriptor, String database,
    String dimension);

  private native void setDataNumericSplashed0(int descriptor, String database,
    String cube, int nCoordinates, String coordinates[], double value, int splashMode);

  public void update(RuleInfo rule, String definition, String externalIdentifier,
    boolean useIt, String comment) {
    throw new PaloException("Not supported by legacy server");
  }

  public void rename(CubeInfo cube, String newName) {
    throw new PaloException("Not supported by legacy server");
  }

  public DatabaseInfo getDatabase(String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public CubeInfo getCube(DatabaseInfo database, String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public DimensionInfo getDimension(DatabaseInfo database, String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public ElementInfo getElement(DimensionInfo dimension, String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public RuleInfo getRule(CubeInfo cube, String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public HierarchyInfo getHierarchy(DatabaseInfo database, String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public CubeLoader getCubeLoader(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public DatabaseLoader getDatabaseLoader() {
    throw new PaloException("Not supported by legacy server");
  }

  public DimensionLoader getDimensionLoader(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public ElementLoader getElementLoader(DimensionInfo dimension) {
    throw new PaloException("Not supported by legacy server");
  }

  public FunctionLoader getFunctionLoader() {
    throw new PaloException("Not supported by legacy server");
  }

  public HierarchyLoader getHierarchyLoader(DatabaseInfo database) {
    throw new PaloException("Not supported by legacy server");
  }

  public RuleLoader getRuleLoader(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public PropertyLoader getPropertyLoader() {
    throw new PaloException("Not supported by legacy server");
  }

  public PropertyLoader getTypedPropertyLoader(PaloInfo paloObject) {
    throw new PaloException("Not supported by legacy server");
  }

  public PropertyInfo getProperty(String id) {
    throw new PaloException("Not supported by legacy server");
  }

  public boolean supportsRules() {
    return false;
  }

  public PropertyInfo createNewProperty(String id, String value,
    PropertyInfo parent, int type, boolean readOnly) {
    throw new PaloException("Not supported by legacy server");
  }

  public void clear(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public void clear(CubeInfo cube, ElementInfo[][] area) {
    throw new PaloException("Not supported by legacy server");
  }

  public HierarchyInfo[] getHierarchies(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public HierarchyLoader getHierarchyLoader(CubeInfo cube) {
    throw new PaloException("Not supported by legacy server");
  }

  public HierarchyInfo getHierarchy(CubeInfo cube, String id) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public CubeInfo addCube(DatabaseInfo paramDatabaseInfo, String paramString,
    DimensionInfo[] paramArrayOfDimensionInfo, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public DatabaseInfo addDatabase(String paramString, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public DimensionInfo addDimension(DatabaseInfo paramDatabaseInfo,
    String paramString, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public ElementInfo addElement(HierarchyInfo paramHierarchyInfo,
    String paramString, int paramInt, ElementInfo[] paramArrayOfElementInfo,
    double[] paramArrayOfDouble) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean addElements(DimensionInfo paramDimensionInfo,
    String[] paramArrayOfString, int paramInt,
    ElementInfo[][] paramArrayOfElementInfo, double[][] paramArrayOfDouble) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean addElements(DimensionInfo paramDimensionInfo,
    String[] paramArrayOfString, int[] paramArrayOfInt,
    ElementInfo[][] paramArrayOfElementInfo, double[][] paramArrayOfDouble) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean commit(CubeInfo paramCubeInfo, LockInfo paramLockInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public int convert(CubeInfo paramCubeInfo, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public RuleInfo createRule(CubeInfo paramCubeInfo, String paramString1,
    String paramString2, boolean paramBoolean1, String paramString3,
    boolean paramBoolean2) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean delete(ElementInfo[] paramArrayOfElementInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean delete(String paramString, CubeInfo paramCubeInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public CubeInfo[] getCubes(DatabaseInfo paramDatabaseInfo, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public DimensionInfo[] getDimensions(DatabaseInfo paramDatabaseInfo, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public ElementInfo getElement(HierarchyInfo paramHierarchyInfo, String paramString) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public ElementInfo getElementAt(HierarchyInfo paramHierarchyInfo, int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public ElementLoader getElementLoader(HierarchyInfo paramHierarchyInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public ElementInfo[] getElements(HierarchyInfo paramHierarchyInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public HierarchyInfo[] getHierarchies(DimensionInfo paramDimensionInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public HierarchyInfo getHierarchy(DimensionInfo paramDimensionInfo,
    String paramString) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public HierarchyLoader getHierarchyLoader(DimensionInfo paramDimensionInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public LockInfo[] getLocks(CubeInfo paramCubeInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public void rename(DatabaseInfo paramDatabaseInfo, String paramString) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean replaceBulk(DimensionInfo paramDimensionInfo,
    ElementInfo[] paramArrayOfElementInfo, int paramInt,
    ElementInfo[][] paramArrayOfElementInfo1, Double[][] paramArrayOfDouble) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public LockInfo requestLock(CubeInfo paramCubeInfo,
    ElementInfo[][] paramArrayOfElementInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean rollback(CubeInfo paramCubeInfo, LockInfo paramLockInfo,
    int paramInt) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public void update(ElementInfo paramElementInfo, int paramInt,
    String[] paramArrayOfString, double[] paramArrayOfDouble,
    ServerInfo paramServerInfo) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public void update(RuleInfo paramRuleInfo, String paramString1,
    String paramString2, boolean paramBoolean1, String paramString3,
    boolean paramBoolean2) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean copyCell(CubeInfo cube, ElementInfo[] src, ElementInfo[] dest,
    double value) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public boolean copyCell(CubeInfo cube, ElementInfo[] src, ElementInfo[] dest) {
    throw new PaloException("Not supported by legacy server");
  }

  @Override
  public LicenseInfo getLicenseInfo() {
    throw new PaloException("Not supported by legacy server");
  }
}
