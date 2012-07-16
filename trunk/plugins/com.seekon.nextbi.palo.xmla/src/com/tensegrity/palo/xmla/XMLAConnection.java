// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2012/4/30 11:23:07
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XMLAConnection.java

package com.tensegrity.palo.xmla;

import com.tensegrity.palo.xmla.builders.BuilderRegistry;
import com.tensegrity.palo.xmla.builders.CubeInfoBuilder;
import com.tensegrity.palo.xmla.builders.RuleInfoBuilder;
import com.tensegrity.palo.xmla.loader.XMLACubeLoader;
import com.tensegrity.palo.xmla.loader.XMLADatabaseLoader;
import com.tensegrity.palo.xmla.loader.XMLADimensionLoader;
import com.tensegrity.palo.xmla.loader.XMLAElementLoader;
import com.tensegrity.palo.xmla.loader.XMLAFunctionLoader;
import com.tensegrity.palo.xmla.loader.XMLAHierarchyLoader;
import com.tensegrity.palo.xmla.loader.XMLAPropertyLoader;
import com.tensegrity.palo.xmla.loader.XMLARuleLoader;
import com.tensegrity.palo.xmla.parsers.XMLADimensionRequestor;
import com.tensegrity.palo.xmla.parsers.XMLAHierarchyRequestor;
import com.tensegrity.palojava.*;
import com.tensegrity.palojava.events.ServerListener;
import com.tensegrity.palojava.impl.ConnectionInfoImpl;
import com.tensegrity.palojava.impl.PropertyInfoImpl;
import com.tensegrity.palojava.loader.*;
import java.io.PrintStream;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;

// Referenced classes of package com.tensegrity.palo.xmla:
//            XMLAClient, XMLAElementInfo, XMLAServerInfo, XMLACubeInfo, 
//            XMLADimensionInfo, XMLADatabaseInfo, XMLAHierarchyInfo

public class XMLAConnection
    implements DbConnection
{

    XMLAConnection(String host, String service, String user, String pass)
    {
        xmlaClient = null;
        databaseLoader = null;
        functionLoader = null;
        propertyLoader = null;
        connectionInfo = new ConnectionInfoImpl(host, service, user, pass);
        try
        {
            ResourceBundle rb = ResourceBundle.getBundle("deploy", Locale.ITALIAN);
            String isSSL = rb.getString("is.ssl");
            xmlaClient = new XMLAClient(host, service, user, pass/*, Boolean.parseBoolean(isSSL)*/);
            connected = true;
        }
        catch(ParserConfigurationException e)
        {
            e.printStackTrace();
        }
    }

    public void addConsolidations(ElementInfo elementinfo, ElementInfo aelementinfo[], double ad[])
    {
    }

    public CubeInfo addCube(DatabaseInfo database, String name, DimensionInfo dimensions[])
    {
        throw new PaloException("XMLAConnections cannot add cubes.");
    }

    public DatabaseInfo addDatabase(String database, int type)
    {
        throw new PaloException("XMLAConnections cannot add databases.");
    }

    public DimensionInfo addDimension(DatabaseInfo database, String name)
    {
        DimensionInfo dims[] = getDimensions(database);
        for(int i = 0; i < dims.length; i++)
            if(dims[i].getName().equals(name))
                return dims[i];

        return null;
    }

    public ElementInfo addElement(DimensionInfo dimension, String name, int type, ElementInfo children[], double weights[])
    {
        throw new PaloException("XMLAConnections cannot add elements.");
    }

    public ElementInfo addElement(HierarchyInfo hierarchy, String name, int type, ElementInfo children[], double weights[])
    {
        throw new PaloException("XMLAConnections cannot add elements.");
    }

    public void clear(DimensionInfo dimension)
    {
        throw new PaloException("XMLAConnections cannot clear dimensions.");
    }

    public RuleInfo createRule(CubeInfo cube, String definition)
    {
        throw new PaloException("XMLAConnections cannot create rules.");
    }

    public boolean delete(ElementInfo element)
    {
        throw new PaloException("XMLAConnections cannot delete elements.");
    }

    public boolean delete(CubeInfo cube)
    {
        throw new PaloException("XMLAConnections cannot delete cubes.");
    }

    public boolean delete(DatabaseInfo database)
    {
        throw new PaloException("XMLAConnections cannot delete databases.");
    }

    public boolean delete(DimensionInfo dimension)
    {
        throw new PaloException("XMLAConnections cannot delete dimensions.");
    }

    public boolean delete(RuleInfo rule)
    {
        throw new PaloException("XMLAConnections cannot delete rules.");
    }

    public boolean delete(String rule, CubeInfo cube)
    {
        throw new PaloException("XMLAConnections cannot delete rules.");
    }

    public CubeInfo getAttributeCube(DimensionInfo dimension)
    {
        return null;
    }

    public DimensionInfo getAttributeDimension(DimensionInfo dimension)
    {
        return null;
    }

    public CubeInfo[] getCubes(DatabaseInfo database)
    {
        CubeLoader cl = getCubeLoader(database);
        String cubeIds[] = cl.getAllCubeIds();
        CubeInfo cubeInfos[] = new CubeInfo[cubeIds.length];
        int i = 0;
        for(int n = cubeIds.length; i < n; i++)
            cubeInfos[i] = cl.load(cubeIds[i]);

        return cubeInfos;
    }

    public CubeInfo[] getCubes(DimensionInfo dimension)
    {
        CubeInfo allCubes[] = getCubes(dimension.getDatabase());
        List list = new ArrayList();
        int i = 0;
        for(int n = allCubes.length; i < n; i++)
        {
            String dimIds[] = allCubes[i].getDimensions();
            int j = 0;
            for(int m = dimIds.length; j < m; j++)
            {
                if(!dimIds[j].equals(dimension.getId()))
                    continue;
                list.add(allCubes[i]);
                break;
            }

        }

        return (CubeInfo[])list.toArray(new CubeInfo[0]);
    }

    public CellInfo getData(CubeInfo cube, ElementInfo coordinate[])
    {
        int n = coordinate.length;
        XMLAElementInfo els[] = new XMLAElementInfo[n];
        for(int i = 0; i < n; i++)
            els[i] = (XMLAElementInfo)coordinate[i];

        return BuilderRegistry.getInstance().getCubeInfoBuilder().getData(xmlaClient.getConnections()[0].getName(), xmlaClient, (XMLACubeInfo)cube, els, this);
    }

    public CellInfo[] getDataArea(CubeInfo cube, ElementInfo coordinates[][])
    {
        int n = coordinates.length;
        XMLAElementInfo els[][] = new XMLAElementInfo[n][];
        for(int i = 0; i < n; i++)
        {
            int m = coordinates[i].length;
            els[i] = new XMLAElementInfo[m];
            for(int j = 0; j < m; j++)
                els[i][j] = (XMLAElementInfo)coordinates[i][j];

        }

        return BuilderRegistry.getInstance().getCubeInfoBuilder().getDataArray(xmlaClient.getConnections()[0].getName(), xmlaClient, (XMLACubeInfo)cube, els, this);
    }

    public CellInfo[] getDataArray(CubeInfo cube, ElementInfo coordinates[][])
    {
        int n = coordinates.length;
        XMLAElementInfo els[][] = new XMLAElementInfo[n][];
        for(int i = 0; i < n; i++)
        {
            int m = coordinates[i].length;
            els[i] = new XMLAElementInfo[m];
            for(int j = 0; j < m; j++)
                els[i][j] = (XMLAElementInfo)coordinates[i][j];

        }

        return BuilderRegistry.getInstance().getCubeInfoBuilder().getDataBulk(xmlaClient.getConnections()[0].getName(), xmlaClient, (XMLACubeInfo)cube, els, this);
    }

    public Object getWholeCube(CubeInfo cube)
    {
        return BuilderRegistry.getInstance().getCubeInfoBuilder().readWholeCube(xmlaClient.getConnections()[0].getName(), xmlaClient, (XMLACubeInfo)cube, this);
    }

    private final CellInfo[] performCopy(ArrayList allCoords, DimensionInfo origDims[], CubeInfo cube, boolean ignoreEmptyCells)
    {
        XMLAElementInfo coordinates[][] = new XMLAElementInfo[allCoords.size()][origDims.length];
        int counter = 0;
        for(Iterator iterator = allCoords.iterator(); iterator.hasNext();)
        {
            CoordinateStorage cst = (CoordinateStorage)iterator.next();
            coordinates[counter++] = cst.getCoords();
        }

        ArrayList filteredResult = new ArrayList();
        CellInfo result[] = getDataArray(cube, coordinates);
        CellInfo acellinfo[];
        int j = (acellinfo = result).length;
        for(int i = 0; i < j; i++)
        {
            CellInfo r = acellinfo[i];
            counter++;
            if(ignoreEmptyCells && (r == null || r.getValue().toString().equals("0.0") || r.getValue().toString().equals("")))
                continue;
            try
            {
                Double.parseDouble(r.getValue().toString());
            }
            catch(NumberFormatException e)
            {
                continue;
            }
            filteredResult.add(r);
        }

        return (CellInfo[])filteredResult.toArray(new CellInfo[0]);
    }

    private final CellInfo[] collectCoordinates(CubeInfo cube, ExportContextInfo context)
    {
        int cellCounter = 0;
        int totalCellCounter = 0;
        String elementIds[][] = context.getCellsArea();
        DimensionInfo origDims[] = getDimensions(cube);
        boolean readWholeCube = false;
        if(elementIds == null || elementIds.length == 0)
        {
            readWholeCube = true;
        } else
        {
            boolean found = false;
            for(int i = 0; i < elementIds.length; i++)
            {
                if(elementIds[i].length == 0)
                    continue;
                found = true;
                break;
            }

            if(!found)
                readWholeCube = true;
        }
        XMLAElementInfo members[][];
        int elemPos[];
        if(readWholeCube)
        {
            members = new XMLAElementInfo[origDims.length][];
            elemPos = new int[origDims.length];
            for(int i = 0; i < origDims.length; i++)
            {
                elemPos[i] = 0;
                ElementLoader loader = getElementLoader(origDims[i]);
                String ids[] = loader.getAllElementIds();
                members[i] = new XMLAElementInfo[ids.length];
                int counter = 0;
                String as1[];
                int i1 = (as1 = ids).length;
                for(int k = 0; k < i1; k++)
                {
                    String id = as1[k];
                    members[i][counter++] = (XMLAElementInfo)loader.load(id);
                }

            }

        } else
        {
            if(elementIds.length != origDims.length)
            {
                System.err.println("Must specify at least one element per dimension.");
                return new CellInfo[0];
            }
            members = new XMLAElementInfo[origDims.length][];
            elemPos = new int[origDims.length];
            for(int i = 0; i < elementIds.length; i++)
            {
                elemPos[i] = 0;
                ElementLoader loader = getElementLoader(origDims[i]);
                members[i] = new XMLAElementInfo[elementIds[i].length];
                int counter = 0;
                String as[];
                int l = (as = elementIds[i]).length;
                for(int j = 0; j < l; j++)
                {
                    String id = as[j];
                    members[i][counter++] = (XMLAElementInfo)loader.load(id);
                }

            }

        }
        elemPos[origDims.length - 1] = -1;
        boolean finished = false;
        ArrayList completeResult = new ArrayList();
        int cp = origDims.length - 1;
        ArrayList allCoords = new ArrayList();
        while(!finished) 
        {
            boolean posFound = false;
            while(!posFound) 
            {
                elemPos[cp]++;
                if(elemPos[cp] >= members[cp].length)
                {
                    elemPos[cp] = 0;
                    if(--cp != -1)
                        continue;
                    finished = true;
                    break;
                }
                boolean anotherRound = false;
                if(context.isBaseCellsOnly())
                    while(members[cp][elemPos[cp]].getChildrenCount() != 0) 
                    {
                        elemPos[cp]++;
                        if(elemPos[cp] < members[cp].length)
                            continue;
                        elemPos[cp] = 0;
                        if(--cp == -1)
                        {
                            finished = true;
                            break;
                        }
                        anotherRound = true;
                    }
                if(!anotherRound && !finished)
                {
                    cp = origDims.length - 1;
                    posFound = true;
                    for(int i = 0; i < origDims.length; i++)
                        if(members[i][elemPos[i]].getChildrenCount() != 0 && context.isBaseCellsOnly())
                            posFound = false;

                }
            }
            if(!finished)
            {
                XMLAElementInfo els[] = new XMLAElementInfo[origDims.length];
                for(int i = 0; i < origDims.length; i++)
                    els[i] = members[i][elemPos[i]];

                allCoords.add(new CoordinateStorage(els));
                cellCounter++;
                totalCellCounter++;
                if(cellCounter >= 10000)
                {
                    completeResult.addAll(Arrays.asList(performCopy(allCoords, origDims, cube, context.ignoreEmptyCells())));
                    allCoords.clear();
                    cellCounter = 0;
                }
            }
        }
        if(allCoords.size() > 0)
            completeResult.addAll(Arrays.asList(performCopy(allCoords, origDims, cube, context.ignoreEmptyCells())));
        return (CellInfo[])completeResult.toArray(new CellInfo[0]);
    }

    public CellInfo[] getDataExport(CubeInfo cube, ExportContextInfo context)
    {
        context.setProgress(1.0D);
        return collectCoordinates(cube, context);
    }

    public DatabaseInfo[] getDatabases()
    {
        DatabaseLoader dl = getDatabaseLoader();
        String ids[] = dl.getAllDatabaseIds();
        DatabaseInfo databaseInfos[] = new DatabaseInfo[ids.length];
        int i = 0;
        for(int n = ids.length; i < n; i++)
            databaseInfos[i] = dl.load(ids[i]);

        return databaseInfos;
    }

    public DimensionInfo[] getDimensions(DatabaseInfo database)
    {
        DimensionLoader dl = getDimensionLoader(database);
        String ids[] = dl.getAllDimensionIds();
        DimensionInfo dimensionInfos[] = new DimensionInfo[ids.length];
        int i = 0;
        for(int n = ids.length; i < n; i++)
            dimensionInfos[i] = dl.load(ids[i]);

        return dimensionInfos;
    }

    public DimensionInfo[] getDimensions(CubeInfo cube)
    {
        DimensionLoader dl = getDimensionLoader(cube.getDatabase());
        String ids[] = ((XMLADimensionLoader)dl).getAllDimensionIdsForCube(cube);
        DimensionInfo dimensionInfos[] = new DimensionInfo[ids.length];
        int i = 0;
        for(int n = ids.length; i < n; i++)
            dimensionInfos[i] = dl.load(ids[i]);

        return dimensionInfos;
    }

    public ElementInfo getElementAt(DimensionInfo dimension, int position)
    {
        return getElements(dimension)[position];
    }

    public ElementInfo getElementAt(HierarchyInfo hierarchy, int position)
    {
        return getElements(hierarchy)[position];
    }

    public ElementInfo[] getElements(DimensionInfo dimension)
    {
        ElementLoader el = getElementLoader(dimension);
        String ids[] = el.getAllElementIds();
        ElementInfo elementInfos[] = new ElementInfo[ids.length];
        int i = 0;
        for(int n = ids.length; i < n; i++)
            elementInfos[i] = el.load(ids[i]);

        return elementInfos;
    }

    public ElementInfo[] getElements(HierarchyInfo hierarchy)
    {
        ElementLoader el = getElementLoader(hierarchy);
        String ids[] = el.getAllElementIds();
        ElementInfo elementInfos[] = new ElementInfo[ids.length];
        int i = 0;
        for(int n = ids.length; i < n; i++)
            elementInfos[i] = el.load(ids[i]);

        return elementInfos;
    }

    public XMLAElementInfo[] getCubeElements(XMLACubeInfo cube, XMLADimensionInfo dimension)
    {
        ElementLoader el = getElementLoader(dimension.getDefaultHierarchy());
        String ids[] = el.getAllElementIds();
        ArrayList cubeElements = new ArrayList();
        int i = 0;
        for(int n = ids.length; i < n; i++)
        {
            XMLAElementInfo elInfo = (XMLAElementInfo)el.load(ids[i]);
            if(((XMLADimensionInfo)elInfo.getDimension()).getCubeId().equals(cube.getId()))
                cubeElements.add(elInfo);
        }

        return (XMLAElementInfo[])cubeElements.toArray(new XMLAElementInfo[0]);
    }

    public ConnectionInfo getInfo()
    {
        return connectionInfo;
    }

    public CubeInfo[] getNormalCubes(DatabaseInfo database)
    {
        return getCubes(database);
    }

    public DatabaseInfo[] getNormalDatabases()
    {
        return getDatabases();
    }

    public DimensionInfo[] getNormalDimensions(DatabaseInfo database)
    {
        return getDimensions(database);
    }

    public String getRule(CubeInfo cube, ElementInfo coordinate[])
    {
        return BuilderRegistry.getInstance().getRuleInfoBuilder().getRule((XMLACubeInfo)cube, coordinate);
    }

    public RuleInfo[] getRules(CubeInfo cube)
    {
        return BuilderRegistry.getInstance().getRuleInfoBuilder().getRules(this, xmlaClient, (XMLACubeInfo)cube);
    }

    public ServerInfo getServerInfo()
    {
        ServerInfo cons[] = xmlaClient.getConnections();
        if(cons == null || cons.length < 1)
            throw new PaloException((new StringBuilder("Could not login to xmla server '")).append(xmlaClient.getServer()).append("' as user '").append(xmlaClient.getUsername()).append("'!!").toString());
        else
            return cons[0];
    }

    public CubeInfo[] getSystemCubes(DatabaseInfo database)
    {
        return new XMLACubeInfo[0];
    }

    public DatabaseInfo[] getSystemDatabases()
    {
        return new XMLADatabaseInfo[0];
    }

    public boolean isConnected()
    {
        return connected;
    }

    public String listFunctions()
    {
        return BuilderRegistry.getInstance().getRuleInfoBuilder().getFunctions(xmlaClient);
    }

    public String listFunctionNames()
    {
        return BuilderRegistry.getInstance().getRuleInfoBuilder().getFunctionNames(xmlaClient);
    }

    public void load(CubeInfo cubeinfo)
    {
    }

    public void load(DatabaseInfo databaseinfo)
    {
    }

    public boolean login(String user, String password)
    {
        return true;
    }

    public void move(ElementInfo element, int newPosition)
    {
        throw new PaloException("XMLAConnections cannot move elements.");
    }

    public String parseRule(CubeInfo cube, String ruleDefinition)
    {
        return parseRule(cube, ruleDefinition, null);
    }

    public String parseRule(CubeInfo cube, String ruleDefinition, String functions)
    {
        RuleInfo rules[] = getRules(cube);
        RuleInfo rule = null;
        int i = 0;
        for(int n = rules.length; i < n; i++)
        {
            if(!rules[i].getDefinition().equals(ruleDefinition))
                continue;
            rule = rules[i];
            break;
        }

        if(rule == null)
            return "";
        DimensionInfo dimensions[] = getDimensions(cube);
        StringBuffer definition = new StringBuffer("[");
        for(i = 0; i < dimensions.length; i++)
        {
            definition.append((new StringBuilder("'")).append(dimensions[i].getId().replaceAll("'", "''")).append("':''").toString());
            if(i < dimensions.length - 1)
                definition.append(",");
        }

        String cleanDef = ruleDefinition.replaceAll("\\{", "").replaceAll("\\}", "");
        cleanDef = cleanDef.replaceAll("\\[", "\"[").replaceAll("\\]", "]\"");
        cleanDef = cleanDef.replaceAll("\"\\.\"", "\\.");
        definition.append((new StringBuilder("]=")).append(cleanDef).toString());
        return definition.toString();
    }

    public RuleInfo createRule(CubeInfo cube, String definition, String externalIdentifier, boolean useIt, String comment, boolean activate)
    {
        throw new PaloException("Cannot create rules in XMLA.");
    }

    public void update(RuleInfo rule, String definition, String externalIdentifier, boolean useIt, String comment, boolean activate)
    {
        throw new PaloException("Cannot update rules in XMLA.");
    }

    public void ping()
        throws PaloException
    {
    }

    public void reload(CubeInfo cubeinfo)
    {
    }

    public void reload(DatabaseInfo databaseinfo)
    {
    }

    public void reload(DimensionInfo dimensioninfo)
    {
    }

    public void reload(ElementInfo elementinfo)
    {
    }

    public void addServerListener(ServerListener serverlistener)
    {
    }

    public void removeServerListener(ServerListener serverlistener)
    {
    }

    public void rename(ElementInfo element, String newName)
    {
        throw new PaloException("XMLAConnections cannot rename elements.");
    }

    public void rename(DimensionInfo dimension, String newName)
    {
        throw new PaloException("XMLAConnections cannot rename dimensions.");
    }

    public void rename(DatabaseInfo database, String newName)
    {
        throw new PaloException("XMLAConnections cannot rename databases.");
    }

    public boolean save(DatabaseInfo database)
    {
        throw new PaloException("XMLAConnections cannot save databases.");
    }

    public boolean save(ServerInfo server)
    {
        throw new PaloException("XMLAConnections cannot save connections.");
    }

    public boolean save(CubeInfo cube)
    {
        throw new PaloException("XMLAConnections cannot save cubes.");
    }

    public void setDataArray(CubeInfo cube, ElementInfo coordinates[][], Object values[], boolean add, int splashMode, boolean notifyEventProcessors)
    {
        throw new PaloException("XMLAConnections cannot write data.");
    }

    public void setDataNumericSplashed(CubeInfo cube, ElementInfo coordinate[], double value, int splashMode)
    {
        throw new PaloException("XMLAConnections cannot write data.");
    }

    public void setDataString(CubeInfo cube, ElementInfo coordinate[], String value)
    {
        throw new PaloException("XMLAConnections cannot write data.");
    }

    public void unload(CubeInfo cubeinfo)
    {
    }

    public void update(ElementInfo elementinfo, int i, String as[], double ad[], ServerInfo serverinfo)
    {
    }

    public synchronized void disconnect()
    {
        xmlaClient.disconnect();
        connected = false;
    }

    public final void rename(CubeInfo cube, String newName)
    {
        throw new PaloException("XMLAConnections cannot rename cubes.");
    }

    public CubeInfo getCube(DatabaseInfo database, String id)
    {
        return getCubeLoader(database).loadByName(id);
    }

    public CubeLoader getCubeLoader(DatabaseInfo database)
    {
        XMLACubeLoader cl;
        if((cl = (XMLACubeLoader)cubeLoaders.get(database)) == null)
        {
            cl = new XMLACubeLoader(this, xmlaClient, database, this);
            cubeLoaders.put((XMLADatabaseInfo)database, cl);
        }
        return cl;
    }

    public DatabaseInfo getDatabase(String id)
    {
        return getDatabaseLoader().loadByName(id);
    }

    public DatabaseLoader getDatabaseLoader()
    {
        if(databaseLoader == null)
            databaseLoader = new XMLADatabaseLoader(this, xmlaClient);
        return databaseLoader;
    }

    public DimensionInfo getDimension(DatabaseInfo database, String id)
    {
        String cubeId = XMLADimensionInfo.getCubeNameFromId(id);
        XMLACubeInfo cubeInfo = (XMLACubeInfo)getCube(database, cubeId);
        XMLADimensionRequestor req = new XMLADimensionRequestor(cubeInfo, this);
        req.setCatalogNameRestriction(database.getId());
        req.setCubeNameRestriction(cubeInfo.getId());
        req.setDimensionUniqueNameRestriction(XMLADimensionInfo.getDimIdFromId(id));
        XMLADimensionInfo result[] = req.requestDimensions(xmlaClient);
        if(result == null || result.length < 1)
            return null;
        else
            return result[0];
    }

    public DimensionLoader getDimensionLoader(DatabaseInfo database)
    {
        XMLADimensionLoader dl;
        if((dl = (XMLADimensionLoader)dimensionLoaders.get(database)) == null)
        {
            dl = new XMLADimensionLoader(this, xmlaClient, database);
            dimensionLoaders.put((XMLADatabaseInfo)database, dl);
        }
        return dl;
    }

    public ElementInfo getElement(DimensionInfo dimension, String id)
    {
        return getElementLoader(dimension).loadByName(id);
    }

    public ElementInfo getElement(HierarchyInfo hierarchy, String id)
    {
        return getElementLoader(hierarchy).loadByName(id);
    }

    public ElementLoader getElementLoader(DimensionInfo dimension)
    {
        XMLAElementLoader el;
        if((el = (XMLAElementLoader)elementLoaders.get(dimension)) == null)
        {
            el = new XMLAElementLoader(this, xmlaClient, dimension);
            elementLoaders.put((XMLADimensionInfo)dimension, el);
        }
        return el;
    }

    public ElementLoader getElementLoader(HierarchyInfo hierarchy)
    {
        XMLAElementLoader el;
        if((el = (XMLAElementLoader)hElementLoaders.get(hierarchy)) == null)
        {
            el = new XMLAElementLoader(this, xmlaClient, hierarchy);
            hElementLoaders.put((XMLAHierarchyInfo)hierarchy, el);
        }
        return el;
    }

    public FunctionLoader getFunctionLoader()
    {
        if(functionLoader == null)
            functionLoader = new XMLAFunctionLoader(this);
        return functionLoader;
    }

    public HierarchyLoader getHierarchyLoader(DimensionInfo dimension)
    {
        XMLAHierarchyLoader hl;
        if((hl = (XMLAHierarchyLoader)hierarchyLoaders.get(dimension)) == null)
        {
            hl = new XMLAHierarchyLoader(this, dimension);
            hierarchyLoaders.put((XMLADimensionInfo)dimension, hl);
        }
        return hl;
    }

    public RuleInfo getRule(CubeInfo cube, String id)
    {
        RuleInfo rules[] = (RuleInfo[])loadedRules.get(cube);
        if(rules == null)
        {
            rules = BuilderRegistry.getInstance().getRuleInfoBuilder().getRules(this, xmlaClient, (XMLACubeInfo)cube);
            loadedRules.put((XMLACubeInfo)cube, rules);
        }
        if(rules != null)
        {
            RuleInfo aruleinfo[];
            int j = (aruleinfo = rules).length;
            for(int i = 0; i < j; i++)
            {
                RuleInfo rule = aruleinfo[i];
                if(rule.getId().equals(id))
                    return rule;
            }

        }
        return null;
    }

    public RuleLoader getRuleLoader(CubeInfo cube)
    {
        XMLARuleLoader rl;
        if((rl = (XMLARuleLoader)ruleLoaders.get(cube)) == null)
        {
            rl = new XMLARuleLoader(this, xmlaClient, cube);
            ruleLoaders.put((XMLACubeInfo)cube, rl);
        }
        return rl;
    }

    public PropertyLoader getPropertyLoader()
    {
        if(propertyLoader == null)
            propertyLoader = new XMLAPropertyLoader(this);
        return propertyLoader;
    }

    public PropertyLoader getTypedPropertyLoader(PaloInfo infoObject)
    {
        XMLAPropertyLoader pl;
        if((pl = (XMLAPropertyLoader)propertyLoaders.get(infoObject)) == null)
        {
            pl = new XMLAPropertyLoader(this, infoObject);
            propertyLoaders.put(infoObject, pl);
        }
        return pl;
    }

    public PropertyInfo getProperty(String id)
    {
        if(id.equals("SAP_VARIABLES"))
        {
            boolean isSap = xmlaClient.isSAP((XMLAServerInfo)getServerInfo());
            PropertyInfo info = new PropertyInfoImpl(id, Boolean.toString(isSap), null, 3, true);
            return info;
        } else
        {
            return null;
        }
    }

    public boolean supportsRules()
    {
        return true;
    }

    public String[] getAllKnownPropertyIds()
    {
        return (new String[] {
            "SAP_VARIABLES"
        });
    }

    public PropertyInfo createNewProperty(String id, String value, PropertyInfo parent, int type, boolean readOnly)
    {
        return new PropertyInfoImpl(id, value, parent, type, readOnly);
    }

    public void clear(CubeInfo cube)
    {
        throw new PaloException("Not supported by xmla server");
    }

    public void clear(CubeInfo cube, ElementInfo area[][])
    {
        throw new PaloException("Not supported by xmla server");
    }

    public DimensionInfo addDimension(DatabaseInfo database, String name, int type)
    {
        throw new PaloException("XMLAConnections cannot add dimensions.");
    }

    public CubeInfo[] getUserInfoCubes(DatabaseInfo database)
    {
        return new XMLACubeInfo[0];
    }

    public DimensionInfo[] getUserInfoDimensions(DatabaseInfo database)
    {
        return new XMLADimensionInfo[0];
    }

    public CubeInfo addCube(DatabaseInfo database, String name, DimensionInfo dimensions[], int type)
    {
        throw new PaloException("XMLAConnections cannot add cubes.");
    }

    public CubeInfo[] getCubes(DatabaseInfo database, int typeMask)
    {
        return getCubes(database);
    }

    public DimensionInfo[] getDimensions(DatabaseInfo database, int typeMask)
    {
        return getDimensions(database);
    }

    public final boolean usedByWPalo()
    {
        Object isWPalo = connectionInfo.getData("com.tensegrity.palo.wpalo");
        if(isWPalo != null && (isWPalo instanceof Boolean))
            return ((Boolean)isWPalo).booleanValue();
        else
            return false;
    }

    public final LockInfo[] getLocks(CubeInfo cube)
    {
        throw new PaloException("Not supported by xmla server");
    }

    public final LockInfo requestLock(CubeInfo cube, ElementInfo area[][])
    {
        throw new PaloException("Not supported by xmla server");
    }

    public final boolean rollback(CubeInfo cube, LockInfo lock, int steps)
    {
        throw new PaloException("Not supported by xmla server");
    }

    public final boolean commit(CubeInfo cube, LockInfo lock)
    {
        throw new PaloException("Not supported by xmla server");
    }

    public HierarchyInfo[] getHierarchies(DimensionInfo dimension)
    {
        XMLAHierarchyRequestor req = new XMLAHierarchyRequestor((XMLADimensionInfo)dimension, (XMLADatabaseInfo)dimension.getDatabase(), this);
        req.setCubeNameRestriction(((XMLADimensionInfo)dimension).getCubeId());
        req.setCatalogNameRestriction(((XMLADimensionInfo)dimension).getDatabase().getId());
        req.setDimensionUniqueNameRestriction(((XMLADimensionInfo)dimension).getDimensionUniqueName());
        return req.requestHierarchies(xmlaClient);
    }

    public HierarchyInfo getHierarchy(DimensionInfo dimension, String id)
    {
        XMLAHierarchyRequestor req = new XMLAHierarchyRequestor((XMLADimensionInfo)dimension, (XMLADatabaseInfo)dimension.getDatabase(), this);
        req.setCubeNameRestriction(((XMLADimensionInfo)dimension).getCubeId());
        req.setCatalogNameRestriction(((XMLADimensionInfo)dimension).getDatabase().getId());
        req.setDimensionUniqueNameRestriction(((XMLADimensionInfo)dimension).getDimensionUniqueName());
        XMLAHierarchyInfo hiers[] = req.requestHierarchies(xmlaClient);
        if(hiers != null && hiers.length > 0)
        {
            XMLAHierarchyInfo axmlahierarchyinfo[];
            int j = (axmlahierarchyinfo = hiers).length;
            for(int i = 0; i < j; i++)
            {
                XMLAHierarchyInfo hier = axmlahierarchyinfo[i];
                if(hier != null && hier.getId().equals(id))
                    return hier;
            }

        }
        return null;
    }

    public void testElementMDX(String s, String s1, String s2)
    {
    }

    public boolean addElements(DimensionInfo dimension, String names[], int type, ElementInfo children[][], double weights[][])
    {
        throw new PaloException("XMLAConnections cannot add elements.");
    }

    public boolean delete(ElementInfo elements[])
    {
        throw new PaloException("XMLAConnections cannot delete elements.");
    }

    public boolean addElements(DimensionInfo dimension, String names[], int types[], ElementInfo children[][], double weights[][])
    {
        throw new PaloException("XMLAConnections cannot add elements.");
    }

    public boolean replaceBulk(DimensionInfo dimInfo, ElementInfo elements[], int type, ElementInfo children[][], Double weights[][])
    {
        throw new PaloException("XMLAConnections cannot update consolidations.");
    }

    public int convert(CubeInfo cube, int type)
    {
        throw new PaloException("XMLAConnections cannot convert cubes.");
    }

    public static final String PROPERTY_SAP_VARIABLES = "SAP_VARIABLES";
    public static final String PROPERTY_SAP_VARIABLE_DEFINITION = "SAP_VARIABLE_DEF";
    public static final String PROPERTY_SAP_VARIABLE_INSTANCE = "SAP_VAR";
    public static final String PROPERTY_SAP_VAR_SELECTED_VALUES = "SELECTEDVALUES";
    public static final String PROPERTY_SAP_VAR_ID = "ID";
    public static final String PROPERTY_SAP_VAR_UID = "UID";
    public static final String PROPERTY_SAP_VAR_NAME = "NAME";
    public static final String PROPERTY_SAP_VAR_ORDINAL = "ORDINAL";
    public static final String PROPERTY_SAP_VAR_TYPE = "TYPE";
    public static final String PROPERTY_SAP_VAR_DATATYPE = "DATATYPE";
    public static final String PROPERTY_SAP_VAR_CHARMAXLENGTH = "CHARMAXLENGTH";
    public static final String PROPERTY_SAP_VAR_PROCESSINGTYPE = "PROCESSINGTYPE";
    public static final String PROPERTY_SAP_VAR_SELECTIONTYPE = "SELECTIONTYPE";
    public static final String PROPERTY_SAP_VAR_ENTRYTYPE = "ENTRYTYPE";
    public static final String PROPERTY_SAP_VAR_REFERENCEDIMENSION = "REFERENCEDIMENSION";
    public static final String PROPERTY_SAP_VAR_REFERENCEHIERARCHY = "REFERENCEHIERARCHY";
    public static final String PROPERTY_SAP_VAR_DEFAULTLOW = "DEFAULTLOW";
    public static final String PROPERTY_SAP_VAR_DEFAULTLOWCAP = "DEFAULTLOWCAP";
    public static final String PROPERTY_SAP_VAR_DEFAULTHIGH = "DEFAULTHIGH";
    public static final String PROPERTY_SAP_VAR_DEFAULTHIGHCAP = "DEFAULTHIGHCAP";
    public static final String PROPERTY_SAP_VAR_DESCRIPTION = "DESCRIPTION";
    public static final String PROPERTY_SAP_VAR_ELEMENTS = "ELEMENTS";
    private static final boolean CACHE_CUBES = true;
    private final ConnectionInfoImpl connectionInfo;
    private XMLAClient xmlaClient;
    private boolean connected;
    private final HashMap cachedCubes = new HashMap();
    private XMLADatabaseLoader databaseLoader;
    private final HashMap cubeLoaders = new HashMap();
    private final HashMap dimensionLoaders = new HashMap();
    private final HashMap elementLoaders = new HashMap();
    private final HashMap hElementLoaders = new HashMap();
    private XMLAFunctionLoader functionLoader;
    private final HashMap hierarchyLoaders = new HashMap();
    private final HashMap ruleLoaders = new HashMap();
    private final HashMap loadedRules = new HashMap();
    private XMLAPropertyLoader propertyLoader;
    private final HashMap propertyLoaders = new HashMap();

  class CoordinateStorage
  {
    private XMLAElementInfo[] coords;

    public CoordinateStorage(XMLAElementInfo[] arg2)
    {
      this.coords = arg2;
    }

    public XMLAElementInfo[] getCoords()
    {
      return this.coords;
    }
  }
}

/* Location:           D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name:     com.tensegrity.palo.xmla.XMLAConnection
 * JD-Core Version:    0.5.4
 */