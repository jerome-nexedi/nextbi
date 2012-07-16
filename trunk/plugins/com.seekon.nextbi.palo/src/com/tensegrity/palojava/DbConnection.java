package com.tensegrity.palojava;

import com.tensegrity.palojava.events.ServerListener;
import com.tensegrity.palojava.loader.CubeLoader;
import com.tensegrity.palojava.loader.DatabaseLoader;
import com.tensegrity.palojava.loader.DimensionLoader;
import com.tensegrity.palojava.loader.ElementLoader;
import com.tensegrity.palojava.loader.FunctionLoader;
import com.tensegrity.palojava.loader.HierarchyLoader;
import com.tensegrity.palojava.loader.PropertyLoader;
import com.tensegrity.palojava.loader.RuleLoader;

public abstract interface DbConnection {
  public abstract DatabaseLoader getDatabaseLoader();

  public abstract FunctionLoader getFunctionLoader();

  public abstract CubeLoader getCubeLoader(DatabaseInfo paramDatabaseInfo);

  public abstract DimensionLoader getDimensionLoader(DatabaseInfo paramDatabaseInfo);

  public abstract ElementLoader getElementLoader(DimensionInfo paramDimensionInfo);

  public abstract ElementLoader getElementLoader(HierarchyInfo paramHierarchyInfo);

  public abstract RuleLoader getRuleLoader(CubeInfo paramCubeInfo);

  public abstract HierarchyLoader getHierarchyLoader(DimensionInfo paramDimensionInfo);

  public abstract PropertyLoader getPropertyLoader();

  public abstract PropertyLoader getTypedPropertyLoader(PaloInfo paramPaloInfo);

  public abstract PropertyInfo getProperty(String paramString);

  public abstract boolean supportsRules();

  public abstract PropertyInfo createNewProperty(String paramString1,
    String paramString2, PropertyInfo paramPropertyInfo, int paramInt,
    boolean paramBoolean);

  public abstract ConnectionInfo getInfo();

  public abstract boolean isConnected();

  public abstract boolean login(String paramString1, String paramString2);

  public abstract void ping() throws PaloException;

  public abstract ServerInfo getServerInfo();

  public abstract void addServerListener(ServerListener paramServerListener);

  public abstract void removeServerListener(ServerListener paramServerListener);

  public abstract DatabaseInfo[] getDatabases();

  public abstract DatabaseInfo getDatabase(String paramString);

  public abstract CubeInfo[] getCubes(DatabaseInfo paramDatabaseInfo);

  public abstract CubeInfo[] getCubes(DatabaseInfo paramDatabaseInfo, int paramInt);

  public abstract CubeInfo getCube(DatabaseInfo paramDatabaseInfo, String paramString);

  public abstract int convert(CubeInfo paramCubeInfo, int paramInt);

  public abstract DimensionInfo[] getDimensions(DatabaseInfo paramDatabaseInfo);

  public abstract DimensionInfo[] getDimensions(DatabaseInfo paramDatabaseInfo,
    int paramInt);

  public abstract DimensionInfo getDimension(DatabaseInfo paramDatabaseInfo,
    String paramString);

  public abstract HierarchyInfo[] getHierarchies(DimensionInfo paramDimensionInfo);

  public abstract HierarchyInfo getHierarchy(DimensionInfo paramDimensionInfo,
    String paramString);

  public abstract CubeInfo getAttributeCube(DimensionInfo paramDimensionInfo);

  public abstract DimensionInfo getAttributeDimension(
    DimensionInfo paramDimensionInfo);

  public abstract CubeInfo[] getCubes(DimensionInfo paramDimensionInfo);

  public abstract ElementInfo[] getElements(DimensionInfo paramDimensionInfo);

  public abstract ElementInfo[] getElements(HierarchyInfo paramHierarchyInfo);

  public abstract ElementInfo getElement(DimensionInfo paramDimensionInfo,
    String paramString);

  public abstract ElementInfo getElement(HierarchyInfo paramHierarchyInfo,
    String paramString);

  public abstract ElementInfo getElementAt(DimensionInfo paramDimensionInfo,
    int paramInt);

  public abstract ElementInfo getElementAt(HierarchyInfo paramHierarchyInfo,
    int paramInt);

  public abstract CellInfo getData(CubeInfo paramCubeInfo,
    ElementInfo[] paramArrayOfElementInfo);

  public abstract CellInfo[] getDataArea(CubeInfo paramCubeInfo,
    ElementInfo[][] paramArrayOfElementInfo);

  public abstract CellInfo[] getDataExport(CubeInfo paramCubeInfo,
    ExportContextInfo paramExportContextInfo);

  public abstract CellInfo[] getDataArray(CubeInfo paramCubeInfo,
    ElementInfo[][] paramArrayOfElementInfo);

  public abstract void setDataString(CubeInfo paramCubeInfo,
    ElementInfo[] paramArrayOfElementInfo, String paramString);

  public abstract void setDataNumericSplashed(CubeInfo paramCubeInfo,
    ElementInfo[] paramArrayOfElementInfo, double paramDouble, int paramInt);

  public abstract void setDataArray(CubeInfo paramCubeInfo,
    ElementInfo[][] paramArrayOfElementInfo, Object[] paramArrayOfObject,
    boolean paramBoolean1, int paramInt, boolean paramBoolean2);

  public abstract DatabaseInfo addDatabase(String paramString, int paramInt);

  public abstract CubeInfo addCube(DatabaseInfo paramDatabaseInfo,
    String paramString, DimensionInfo[] paramArrayOfDimensionInfo);

  public abstract CubeInfo addCube(DatabaseInfo paramDatabaseInfo,
    String paramString, DimensionInfo[] paramArrayOfDimensionInfo, int paramInt);

  public abstract DimensionInfo addDimension(DatabaseInfo paramDatabaseInfo,
    String paramString);

  public abstract DimensionInfo addDimension(DatabaseInfo paramDatabaseInfo,
    String paramString, int paramInt);

  public abstract ElementInfo addElement(DimensionInfo paramDimensionInfo,
    String paramString, int paramInt, ElementInfo[] paramArrayOfElementInfo,
    double[] paramArrayOfDouble);

  public abstract ElementInfo addElement(HierarchyInfo paramHierarchyInfo,
    String paramString, int paramInt, ElementInfo[] paramArrayOfElementInfo,
    double[] paramArrayOfDouble);

  public abstract boolean addElements(DimensionInfo paramDimensionInfo,
    String[] paramArrayOfString, int paramInt,
    ElementInfo[][] paramArrayOfElementInfo, double[][] paramArrayOfDouble);

  public abstract boolean addElements(DimensionInfo paramDimensionInfo,
    String[] paramArrayOfString, int[] paramArrayOfInt,
    ElementInfo[][] paramArrayOfElementInfo, double[][] paramArrayOfDouble);

  public abstract void addConsolidations(ElementInfo paramElementInfo,
    ElementInfo[] paramArrayOfElementInfo, double[] paramArrayOfDouble);

  public abstract void clear(DimensionInfo paramDimensionInfo);

  public abstract void clear(CubeInfo paramCubeInfo);

  public abstract void clear(CubeInfo paramCubeInfo,
    ElementInfo[][] paramArrayOfElementInfo);

  public abstract boolean delete(ElementInfo paramElementInfo);

  public abstract boolean delete(ElementInfo[] paramArrayOfElementInfo);

  public abstract boolean delete(CubeInfo paramCubeInfo);

  public abstract boolean delete(DatabaseInfo paramDatabaseInfo);

  public abstract boolean delete(DimensionInfo paramDimensionInfo);

  public abstract void move(ElementInfo paramElementInfo, int paramInt);

  public abstract void load(CubeInfo paramCubeInfo);

  public abstract void load(DatabaseInfo paramDatabaseInfo);

  public abstract void rename(DatabaseInfo paramDatabaseInfo, String paramString);

  public abstract void rename(ElementInfo paramElementInfo, String paramString);

  public abstract void rename(DimensionInfo paramDimensionInfo, String paramString);

  public abstract void rename(CubeInfo paramCubeInfo, String paramString);

  public abstract boolean save(DatabaseInfo paramDatabaseInfo);

  public abstract boolean save(ServerInfo paramServerInfo);

  public abstract boolean save(CubeInfo paramCubeInfo);

  public abstract void unload(CubeInfo paramCubeInfo);

  public abstract void update(ElementInfo paramElementInfo, int paramInt,
    String[] paramArrayOfString, double[] paramArrayOfDouble,
    ServerInfo paramServerInfo);

  public abstract boolean replaceBulk(DimensionInfo paramDimensionInfo,
    ElementInfo[] paramArrayOfElementInfo, int paramInt,
    ElementInfo[][] paramArrayOfElementInfo1, Double[][] paramArrayOfDouble);

  public abstract void reload(CubeInfo paramCubeInfo);

  public abstract void reload(DatabaseInfo paramDatabaseInfo);

  public abstract void reload(DimensionInfo paramDimensionInfo);

  public abstract void reload(ElementInfo paramElementInfo);

  public abstract String parseRule(CubeInfo paramCubeInfo, String paramString1,
    String paramString2);

  public abstract String listFunctions();

  public abstract RuleInfo createRule(CubeInfo paramCubeInfo, String paramString);

  public abstract RuleInfo createRule(CubeInfo paramCubeInfo, String paramString1,
    String paramString2, boolean paramBoolean1, String paramString3,
    boolean paramBoolean2);

  public abstract boolean delete(RuleInfo paramRuleInfo);

  public abstract boolean delete(String paramString, CubeInfo paramCubeInfo);

  public abstract RuleInfo[] getRules(CubeInfo paramCubeInfo);

  public abstract RuleInfo getRule(CubeInfo paramCubeInfo, String paramString);

  public abstract String getRule(CubeInfo paramCubeInfo,
    ElementInfo[] paramArrayOfElementInfo);

  public abstract void update(RuleInfo paramRuleInfo, String paramString1,
    String paramString2, boolean paramBoolean1, String paramString3,
    boolean paramBoolean2);

  public abstract LockInfo[] getLocks(CubeInfo paramCubeInfo);

  public abstract LockInfo requestLock(CubeInfo paramCubeInfo,
    ElementInfo[][] paramArrayOfElementInfo);

  public abstract boolean commit(CubeInfo paramCubeInfo, LockInfo paramLockInfo);

  public abstract boolean rollback(CubeInfo paramCubeInfo, LockInfo paramLockInfo,
    int paramInt);
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.DbConnection JD-Core Version: 0.5.4
 */