// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2012/4/30 11:21:29
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CubeInfoBuilder.java

package com.tensegrity.palo.xmla.builders;

import com.tensegrity.palo.xmla.*;
import com.tensegrity.palojava.*;
import com.tensegrity.palojava.impl.CellInfoImpl;
import com.tensegrity.palojava.loader.PropertyLoader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.*;
import org.w3c.dom.*;

// Referenced classes of package com.tensegrity.palo.xmla.builders:
//            BuilderRegistry, VariableInfoBuilder, DimensionInfoBuilder, ElementInfoBuilder

public class CubeInfoBuilder {

  public CubeInfoBuilder() {
  }

  public XMLACubeInfo getCubeInfo(XMLAClient client, XMLADatabaseInfo dbInfo,
    String name, XMLAConnection con) {
    xmlaConnection = con;
    xmlaClient = client;
    database = dbInfo;
    connectionName = client.getConnections()[0].getName();
    return requestCube(name);
  }

  private XMLACubeInfo requestCube(String name) {
    try {
      NodeList nl;
      XMLARestrictions rest = new XMLARestrictions();
      XMLAProperties prop = new XMLAProperties();
      prop.setDataSourceInfo(connectionName);
      prop.setFormat("Tabular");
      prop.setContent("SchemaData");
      prop.setCatalog(database.getId());
      rest.setCatalog(database.getId());
      rest.setCubeName(name);
      Document result = xmlaClient.getCubeList(rest, prop);
      nl = result.getElementsByTagName("row");
      if (nl == null || nl.getLength() == 0)
        return null;
      XMLACubeInfo cubeInfo;
      NodeList nlRow = nl.item(0).getChildNodes();
      cubeInfo = null;
      for (int j = 0; j < nlRow.getLength(); j++)
        if (nlRow.item(j).getNodeType() == 1)
          if (nlRow.item(j).getNodeName().equals("CUBE_NAME")) {
            String text = XMLAClient.getTextFromDOMElement(nlRow.item(j));
            cubeInfo = new XMLACubeInfo(text, text, database, connectionName,
              xmlaClient, xmlaConnection);
            cellCount = new BigInteger("1");
            PropertyInfo pi = xmlaConnection.getPropertyLoader().load(
              "SAP_VARIABLES");
            if (pi != null && Boolean.parseBoolean(pi.getValue())) {
              com.tensegrity.palo.xmla.XMLAVariableInfo infos[] = BuilderRegistry
                .getInstance().getVariableInfoBuilder().requestVariables(xmlaClient,
                  (XMLADatabaseInfo) cubeInfo.getDatabase(), cubeInfo.getId());
              cubeInfo.setVariables(infos);
            }
          } else if (nlRow.item(j).getNodeName().equals("DESCRIPTION")
            && xmlaClient.isSAP())
            cubeInfo.setName(XMLAClient.getTextFromDOMElement(nlRow.item(j)));
          else if (nlRow.item(j).getNodeName().equals("CUBE_CAPTION"))
            cubeInfo.setName(XMLAClient.getTextFromDOMElement(nlRow.item(j)));

      return cubeInfo;
    } catch (IOException localIOException) {
      localIOException.printStackTrace();
    }
    return null;
  }

  private CellInfo parseValueNode(Node node, String strCoord[]) {
    NamedNodeMap map = node.getAttributes();
    boolean doubleType = false;
    if (map.getLength() > 0) {
      Node type = map.getNamedItem("xsi:type");
      if (type != null) {
        String value = type.getNodeValue();
        if (value.equals("xsd:decimal") || value.equals("xsd:integer")
          || value.equals("xsd:double") || value.equals("xsd:int")
          || value.equals("xsd:unsignedint"))
          doubleType = true;
      }
    }
    String text = XMLAClient.getTextFromDOMElement(node);
    if (doubleType) {
      CellInfoImpl cell = new CellInfoImpl(1, true, new Double(text));
      cell.setCoordinate(strCoord);
      return cell;
    } else {
      CellInfoImpl cell = new CellInfoImpl(2, true, text);
      cell.setCoordinate(strCoord);
      return cell;
    }
  }

  private String getCharacteristicVariableString(PropertyInfo info) {
    PropertyInfo prop = info.getChild("SELECTEDVALUES");
    if (prop == null)
      return "";
    String selectedText = prop.getValue();
    if (selectedText == null || selectedText.length() == 0)
      return "";
    PropertyInfo selType = info.getChild("SELECTIONTYPE");
    if (selType == null)
      return "";
    int selectionType;
    try {
      selectionType = Integer.parseInt(selType.getValue());
    } catch (NumberFormatException e) {
      return "";
    }
    PropertyInfo idProp = info.getChild("ID");
    String idString = idProp.getValue();
    String result = "";
    if (selectionType == 1) {
      result = (new StringBuilder(String.valueOf(result))).append(" ").append(
        idString).append(" INCLUDING ").toString();
      result = (new StringBuilder(String.valueOf(result))).append(
        XMLADimensionInfo.transformId(selectedText)).toString();
    } else if (selectionType == 2) {
      StringTokenizer tok = new StringTokenizer(selectedText, "\n");
      String uniqueName[] = new String[2];
      if (tok.hasMoreTokens())
        uniqueName[0] = tok.nextToken();
      else
        return "";
      if (tok.hasMoreTokens())
        uniqueName[1] = tok.nextToken();
      else
        uniqueName[1] = uniqueName[0];
      result = (new StringBuilder(String.valueOf(result))).append(" ").append(
        idString).append(" INCLUDING ").toString();
      if (uniqueName[0].equals(uniqueName[1]))
        result = (new StringBuilder(String.valueOf(result))).append(
          XMLADimensionInfo.transformId(uniqueName[0])).toString();
      else
        result = (new StringBuilder(String.valueOf(result))).append(
          XMLADimensionInfo.transformId(uniqueName[0])).append(":").append(
          XMLADimensionInfo.transformId(uniqueName[1])).toString();
    } else if (selectionType == 3) {
      StringTokenizer tok = new StringTokenizer(selectedText, "\n");
      StringBuffer tempResult = new StringBuffer();
      for (; tok.hasMoreTokens(); tempResult.append((new StringBuilder(" ")).append(
        idString).append(" INCLUDING ").append(
        XMLADimensionInfo.transformId(tok.nextToken())).toString()))
        ;
      result = tempResult.toString();
    }
    return result;
  }

  private String getFloatingPointVariableString(PropertyInfo info) {
    PropertyInfo prop = info.getChild("SELECTEDVALUES");
    if (prop == null)
      return "";
    String text = prop.getValue();
    if (text == null || text.length() == 0) {
      return "";
    } else {
      PropertyInfo idProp = info.getChild("ID");
      return (new StringBuilder(" ")).append(
        XMLADimensionInfo.transformId(idProp.getValue())).append(" INCLUDING ")
        .append(text).toString();
    }
  }

  private String analyzeVariable(PropertyInfo info) {
    PropertyInfo propInfos[] = info.getChildren();
    if (propInfos == null)
      return "";
    PropertyInfo apropertyinfo[];
    int j = (apropertyinfo = propInfos).length;
    for (int i = 0; i < j; i++) {
      PropertyInfo pi = apropertyinfo[i];
      if (pi.getId().equals("DATATYPE")) {
        if (pi.getValue().equals("CHAR"))
          return getCharacteristicVariableString(info);
        if (pi.getValue().equals("FLTP"))
          return getFloatingPointVariableString(info);
        System.out.println((new StringBuilder("Unknown variable datatype: "))
          .append(pi.getValue()).toString());
      }
    }

    return "";
  }

  private String applyDatabaseSpecificMDX(String query, XMLAClient client,
    XMLACubeInfo cube) {
    PropertyInfo pi = xmlaConnection.getPropertyLoader().load("SAP_VARIABLES");
    if (pi == null)
      return query;
    if (Boolean.parseBoolean(pi.getValue())) {
      PropertyInfo varDef = xmlaConnection.getTypedPropertyLoader(cube).load(
        "SAP_VARIABLE_DEF");
      if (varDef == null)
        return query;
      if (varDef.getChildCount() == 0)
        return query;
      PropertyInfo varInfos[] = varDef.getChildren();
      if (varInfos != null && varInfos.length > 0) {
        boolean added = false;
        boolean first = true;
        int i = 0;
        for (int n = varInfos.length; i < n; i++) {
          PropertyInfo info = varInfos[i];
          String text = analyzeVariable(info);
          if (text.length() != 0) {
            if (!added) {
              query = (new StringBuilder(String.valueOf(query))).append(
                " SAP VARIABLES").toString();
              added = true;
            }
            if (!first)
              query = (new StringBuilder(String.valueOf(query))).append(",\n")
                .toString();
            query = (new StringBuilder(String.valueOf(query))).append(text)
              .toString();
            first = false;
          }
        }

      }
    }
    return query;
  }

  public CellInfo getData(String conName, XMLAClient client, XMLACubeInfo cube,
    XMLAElementInfo coordinates[], XMLAConnection connection) {
    CellInfo result[] = getDataBulk(conName, client, cube,
      new XMLAElementInfo[][] { coordinates }, connection);
    if (result != null && result.length > 0)
      return result[0];
    String strCoord[] = new String[coordinates.length];
    int counter = 0;
    XMLAElementInfo axmlaelementinfo[];
    int j = (axmlaelementinfo = coordinates).length;
    for (int i = 0; i < j; i++) {
      XMLAElementInfo el = axmlaelementinfo[i];
      strCoord[counter++] = el.getId();
    }

    CellInfoImpl cell = new CellInfoImpl(1, false, "");
    cell.setCoordinate(strCoord);
    return cell;
  }

  public CellInfo[] getDataArray(String conName, XMLAClient client,
    XMLACubeInfo cube, XMLAElementInfo elements[][], XMLAConnection connection) {
    try {
      XMLAExecuteProperties prop;
      int dimensionSize;
      CellInfo resultValues[];
      StringBuffer axes[];
      int axisCounter;
      StringBuffer where;
      HashMap elementPositions;
      prop = new XMLAExecuteProperties();
      xmlaConnection = connection;
      if (elements == null || elements.length == 0 || elements[0] == null)
        return (new CellInfo[] { new CellInfoImpl(1, false, "") });
      dimensionSize = elements.length;
      prop.setDataSourceInfo(conName);
      prop.setCatalog(cube.getDatabase().getId());
      if (dimensionSize < 2)
        return (new CellInfo[] { new CellInfoImpl(1, false, "") });
      resultValues = (CellInfo[]) null;
      axes = new StringBuffer[dimensionSize];
      axisCounter = 0;
      where = new StringBuffer("(");
      elementPositions = new HashMap();
      Document doc;
      int querySize[];
      HashMap elementMapper;
      CellInfo cInfo[];
      StringBuffer sb = new StringBuffer("SELECT ");
      boolean first = true;
      querySize = new int[dimensionSize];
      for (int i = 0; i < dimensionSize; i++) {
        querySize[i] = 0;
        axes[i] = new StringBuffer();
        if (i > 1 && elements[i].length == 1) {
          querySize[i] = 1;
          String defName = ((XMLADimensionInfo) elements[i][0].getDimension())
            .getDefaultElementName();
          if (elements[i][0].getUniqueName().equals(defName)) {
            axes[i] = new StringBuffer("-");
          } else {
            if (where.length() > 1)
              where.append(", ");
            where.append(elements[i][0].getUniqueName());
            elementPositions.put(elements[i][0].getUniqueName(), Integer.valueOf(0));
            axes[i] = new StringBuffer("-");
          }
        } else {
          int j = 0;
          for (int m = elements[i].length; j < m; j++) {
            String uniqueName = elements[i][j].getUniqueName();
            axes[i].append(uniqueName);
            elementPositions.put(uniqueName, Integer.valueOf(querySize[i]));
            querySize[i]++;
            if (j < m - 1)
              axes[i].append(", ");
          }

          if (!first)
            sb.append(", ");
          sb.append((new StringBuilder("{")).append(axes[i]).append("} on Axis (")
            .append(axisCounter).append(")").toString());
          axisCounter++;
          first = false;
        }
      }

      where.append(")");
      sb.append((new StringBuilder(" FROM [")).append(cube.getId()).append("]")
        .toString());
      if (where.length() > 2)
        sb.append((new StringBuilder(" WHERE ")).append(where).append("\n")
          .toString());
      elementMapper = new HashMap();
      for (int i = 0; i < elements.length; i++) {
        for (int j = 0; j < elements[i].length; j++)
          elementMapper.put(elements[i][j].getUniqueName(), ((Object) (new Object[] {
            elements[i][j], Integer.valueOf(i),
            elementPositions.get(elements[i][j].getUniqueName()) })));

      }

      if (sb.length() > 0x186a0)
        sb = shortenQuery(client, cube, sb, null, null, axes, dimensionSize, where,
          elementMapper);
      sb = new StringBuffer(applyDatabaseSpecificMDX(sb.toString(), client, cube));
      if (XMLAClient.isDebug())
        System.err.println((new StringBuilder("GetDataArray:\n")).append(sb).append(
          "\n------------\n\n").toString());
      doc = client.execute(sb.toString(), prop);
      NodeList faults = doc.getElementsByTagName("SOAP-ENV:Fault");
      if (faults != null && faults.getLength() > 0) {
        // //break MISSING_BLOCK_LABEL_860;
        cInfo = new CellInfo[1];
        String result = XMLAClient.getErrorString(faults);
        cInfo[0] = new CellInfoImpl(99, true, result);
        ((CellInfoImpl) cInfo[0]).setCoordinate(new String[0]);
        return cInfo;
      }

      NodeList cl;
      String[][] resultCoordinates = parseResultCoordinates(doc);
      cl = doc.getElementsByTagName("CellData");
      if (cl == null)
        return new CellInfo[0];
      if (cl.item(0) == null)
        return new CellInfo[0];
      cl = cl.item(0).getChildNodes();
      int totalElements = 1;
      for (int i = 0; i < dimensionSize; i++)
        totalElements *= elements[i].length;

      resultValues = new CellInfo[totalElements];
      for (int i = 0; i < totalElements; i++)
        resultValues[i] = new CellInfoImpl(1, false, "");

      int cellOrdinal = -1;
      if (cl != null) {
        for (int i = 0; i < cl.getLength(); i++)
          if (cl.item(i).getNodeType() == 1) {
            Node ordinal = cl.item(i).getAttributes().getNamedItem("CellOrdinal");
            if (ordinal != null) {
              cellOrdinal = Integer.parseInt(ordinal.getNodeValue());
              Node cellNode = cl.item(i);
              NodeList nl = cellNode.getChildNodes();
              for (int j = 0; j < nl.getLength(); j++)
                if (nl.item(j).getNodeType() == 1
                  && nl.item(j).getNodeName().equals("Value"))
                  resultValues[transformCellOrdinal(cellOrdinal, resultCoordinates,
                    elementMapper, querySize)] = parseValueNode(nl.item(j),
                    new String[] { "a", "b" });

            }
          }

      }
      return resultValues;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (new CellInfo[] { new CellInfoImpl(1, false, "") });
  }

  private StringBuffer shortenQuery(XMLAClient client, XMLACubeInfo cube,
    StringBuffer sb, int querySize[], HashMap elementIndexMap[],
    StringBuffer axes[], int dimensionSize, StringBuffer where, HashMap mapper) {
    int length = sb.length();
    boolean end = false;
    int i;
    do {
      int max = 0;
      int pos = -1;
      for (i = 0; i < dimensionSize; i++)
        if (querySize != null) {
          if (querySize[i] > max && !axes[i].toString().equals("-")
            && !axes[i].toString().equals("+")) {
            max = querySize[i];
            pos = i;
          }
        } else if (axes[i].length() > max && !axes[i].toString().equals("-")
          && !axes[i].toString().equals("+")) {
          max = axes[i].length();
          pos = i;
        }

      if (length - max < 0x15f90)
        end = true;
      length -= max;
      axes[pos] = new StringBuffer();
    } while (!end);
    sb = new StringBuffer("SELECT ");
    boolean first = true;
    int axisCounter = 0;
    i = 0;
    for (int n = dimensionSize; i < n; i++)
      if (axes[i].length() > 0) {
        if (!axes[i].toString().equals("-") && !axes[i].toString().equals("+")) {
          if (!first)
            sb.append(", ");
          sb.append((new StringBuilder("{")).append(axes[i]).append("} on Axis (")
            .append(axisCounter).append(")\n").toString());
          first = false;
          axisCounter++;
        }
      } else if (!axes[i].toString().equals("+")) {
        DimensionInfo dimInfo[] = BuilderRegistry.getInstance()
          .getDimensionInfoBuilder().getDimensionInfo(xmlaConnection, client,
            (XMLADatabaseInfo) cube.getDatabase(), cube);
        XMLADimensionInfo dim = (XMLADimensionInfo) dimInfo[i];
        if (!first)
          sb.append(", ");
        first = false;
        if (dim.getHierarchyUniqueName() != null)
          sb.append((new StringBuilder("{")).append(dim.getHierarchyUniqueName())
            .append(".Members} on Axis (").append(axisCounter).append(")")
            .toString());
        else
          sb.append((new StringBuilder("{")).append(dim.getName()).append(
            ".Members} on Axis (").append(axisCounter).append(")").toString());
        int counter = 0;
        com.tensegrity.palojava.ElementInfo aelementinfo[];
        int l = (aelementinfo = xmlaConnection.getElements(dim)).length;
        for (int k = 0; k < l; k++) {
          com.tensegrity.palojava.ElementInfo el = aelementinfo[k];
          mapper.put(((XMLAElementInfo) el).getUniqueName(),
            ((Object) (new Object[] { el, Integer.valueOf(i),
              Integer.valueOf(counter) })));
          counter++;
        }

        if (querySize != null)
          querySize[i] = counter;
        axisCounter++;
        if (elementIndexMap != null) {
          elementIndexMap[i].clear();
          XMLAElementInfo els[] = BuilderRegistry.getInstance()
            .getElementInfoBuilder().getElements(xmlaConnection, client,
              cube.getId(), dim);
          querySize[i] = els.length;
          int j = 0;
          for (int m = els.length; j < m; j++)
            elementIndexMap[i].put(els[j].getId(), new Integer(j));

        }
      }

    sb.append((new StringBuilder(" FROM [")).append(cube.getId()).append("]")
      .toString());
    if (where.length() > 2)
      sb
        .append((new StringBuilder(" WHERE ")).append(where).append("\n").toString());
    return sb;
  }

  private final String[] convertOrdinal(int ordinal, DimensionInfo dimInfo[]) {
    return (new String[] { (new StringBuilder()).append(ordinal).toString() });
  }

  private final String[][] parseResultCoordinates(Document doc) {
    NodeList ad = doc.getElementsByTagName("Axes");
    ArrayList resultCoordinatesArray = new ArrayList();
    if (ad != null && ad.item(0) != null) {
      ad = ad.item(0).getChildNodes();
      if (ad != null) {
        for (int i = 0; i < ad.getLength(); i++)
          if (ad.item(i).getNodeType() == 1) {
            String nodeName = ad.item(i).getNodeName();
            if (nodeName.equals("Axis")) {
              ArrayList els = new ArrayList();
              NodeList al = ad.item(i).getChildNodes();
              if (al != null && al.getLength() != 0) {
                for (int j = 0; j < al.getLength(); j++)
                  if (al.item(j).getNodeType() == 1) {
                    NodeList el = al.item(j).getChildNodes();
                    if (el != null && el.getLength() > 0) {
                      for (int k = 0; k < el.getLength(); k++)
                        if (el.item(k).getNodeType() == 1) {
                          NodeList eel = el.item(k).getChildNodes();
                          if (eel != null && eel.getLength() > 0) {
                            for (int l = 0; l < eel.getLength(); l++)
                              if (eel.item(l).getNodeType() == 1) {
                                NodeList eeel = eel.item(l).getChildNodes();
                                if (eeel != null && eeel.getLength() > 0) {
                                  for (int m = 0; m < eeel.getLength(); m++)
                                    if (eeel.item(m).getNodeType() == 1
                                      && eeel.item(m).getNodeName().equals("UName")) {
                                      String id = XMLAClient
                                        .getTextFromDOMElement(eeel.item(m));
                                      els.add(id);
                                    }

                                }
                              }

                          }
                        }

                    }
                  }

              }
              resultCoordinatesArray.add(els);
            }
          }

      }
    }
    String resultCoordinates[][] = new String[resultCoordinatesArray.size()][];
    int i = 0;
    for (int n = resultCoordinatesArray.size(); i < n; i++)
      resultCoordinates[i] = (String[]) ((ArrayList) resultCoordinatesArray.get(i))
        .toArray(new String[0]);

    return resultCoordinates;
  }

  public ExtendedCellInfo readWholeCube(String conName, XMLAClient client,
    XMLACubeInfo cube, XMLAConnection connection) {
    try {
      XMLAExecuteProperties prop;
      StringBuffer sb;
      DimensionInfo dimInfo[];
      prop = new XMLAExecuteProperties();
      xmlaConnection = connection;
      int dimensionSize = cube.getDimensionCount();
      prop.setDataSourceInfo(conName);
      prop.setCatalog(cube.getDatabase().getId());
      if (dimensionSize < 2)
        return new ExtendedCellInfo(new CellInfo[0], new String[0][0]);
      sb = new StringBuffer("SELECT ");
      dimInfo = BuilderRegistry.getInstance().getDimensionInfoBuilder()
        .getDimensionInfo(xmlaConnection, client,
          (XMLADatabaseInfo) cube.getDatabase(), cube);
      boolean first = true;
      for (int i = 0; i < dimInfo.length; i++) {
        XMLADimensionInfo dim = (XMLADimensionInfo) dimInfo[i];
        if (!first)
          sb.append(", ");
        first = false;
        if (dim.getHierarchyUniqueName() != null)
          sb.append((new StringBuilder("NON EMPTY ")).append(
            dim.getHierarchyUniqueName()).append(" on Axis (").append(i).append(")")
            .toString());
        else
          sb.append((new StringBuilder("NON EMPTY ")).append(dim.getName()).append(
            " on Axis (").append(i).append(")").toString());
      }

      sb.append((new StringBuilder(" FROM [")).append(cube.getId()).append("]")
        .toString());
      Document doc;
      String resultCoordinates[][];
      CellInfo cInfo[];
      System.out.println(sb.toString());
      XMLAClient.setVerbose(true);
      doc = client.execute(sb.toString(), prop);
      XMLAClient.setVerbose(false);
      resultCoordinates = new String[dimInfo.length][0];
      NodeList faults = doc.getElementsByTagName("SOAP-ENV:Fault");
      if (faults != null && faults.getLength() > 0) {
        // //break MISSING_BLOCK_LABEL_403;
        cInfo = new CellInfo[1];
        String result = XMLAClient.getErrorString(faults);
        cInfo[0] = new CellInfoImpl(99, true, result);
        ((CellInfoImpl) cInfo[0]).setCoordinate(new String[0]);
        return new ExtendedCellInfo(cInfo, resultCoordinates);
      }

      NodeList cl;
      resultCoordinates = parseResultCoordinates(doc);
      cl = doc.getElementsByTagName("CellData");
      if (cl == null)
        return new ExtendedCellInfo(new CellInfo[0], new String[0][0]);
      if (cl.item(0) == null)
        return new ExtendedCellInfo(new CellInfo[0], new String[0][0]);
      ArrayList resultValues;
      cl = cl.item(0).getChildNodes();
      resultValues = new ArrayList();
      if (cl != null) {
        for (int i = 0; i < cl.getLength(); i++)
          if (cl.item(i).getNodeType() == 1) {
            Node ordinal = cl.item(i).getAttributes().getNamedItem("CellOrdinal");
            if (ordinal != null) {
              int cellOrdinal = Integer.parseInt(ordinal.getNodeValue());
              Node cellNode = cl.item(i);
              NodeList nl = cellNode.getChildNodes();
              for (int j = 0; j < nl.getLength(); j++)
                if (nl.item(j).getNodeType() == 1
                  && nl.item(j).getNodeName().equals("Value"))
                  resultValues.add(parseValueNode(nl.item(j), convertOrdinal(
                    cellOrdinal, dimInfo)));

            }
          }

      }
      return new ExtendedCellInfo(
        (CellInfo[]) resultValues.toArray(new CellInfo[0]), resultCoordinates);
    } catch (IOException e) {

      e.printStackTrace();
    }
    return new ExtendedCellInfo(new CellInfo[0], new String[0][0]);
  }

  private final int transformCellOrdinal(int ordinal, String resultCoordinates[][],
    HashMap mapper, int elementLengths[]) {
    int tempOrdinal = ordinal;
    int newCellOrdinal = 0;
    int offset[] = new int[resultCoordinates.length];
    for (int i = 0; i < resultCoordinates.length; i++) {
      if (i != 0 && resultCoordinates[i - 1].length != 0)
        tempOrdinal /= resultCoordinates[i - 1].length;
      if (resultCoordinates[i].length != 0)
        offset[i] = tempOrdinal % resultCoordinates[i].length;
    }

    for (int i = resultCoordinates.length - 1; i >= 0; i--)
      if (resultCoordinates[i].length != 0) {
        Object res[] = (Object[]) mapper.get(resultCoordinates[i][offset[i]]);
        if (res != null) {
          XMLAElementInfo el = (XMLAElementInfo) res[0];
          int pos = ((Integer) res[1]).intValue();
          if (res[2] != null) {
            int elPos = ((Integer) res[2]).intValue();
            if (el == null) {
              System.err.println("coordinate translation error");
            } else {
              int newOffset = elPos;
              for (int l = 0; l < pos; l++) {
                if (l >= elementLengths.length)
                  break;
                newOffset *= elementLengths[l];
              }

              newCellOrdinal += newOffset;
            }
          }
        }
      }

    return newCellOrdinal;
  }

  public CellInfo[] getDataBulk(String conName, XMLAClient client,
    XMLACubeInfo cube, XMLAElementInfo elements[][], XMLAConnection connection) {
    try {
      XMLAExecuteProperties prop;
      int dimensionSize;
      int querySize[];
      HashMap elementIndexMap[];
      StringBuffer axes[];
      StringBuffer where;
      HashSet allXmlaTypes;
      HashMap elementPositions;
      prop = new XMLAExecuteProperties();
      xmlaConnection = connection;
      if (elements == null || elements.length == 0 || elements[0] == null
        || elements[0].length == 0)
        return (new CellInfo[] { new CellInfoImpl(1, false, "") });
      dimensionSize = elements[0].length;
      prop.setDataSourceInfo(conName);
      prop.setCatalog(cube.getDatabase().getId());
      if (dimensionSize < 2)
        return (new CellInfo[] { new CellInfoImpl(1, false, "") });
      querySize = new int[dimensionSize];
      elementIndexMap = new HashMap[dimensionSize];
      CellInfo resultValues[] = (CellInfo[]) null;
      axes = new StringBuffer[dimensionSize];
      where = new StringBuffer("(");
      allXmlaTypes = new HashSet();
      elementPositions = new HashMap();
      Document doc;
      HashMap elementMapper;
      CellInfo cInfo[];
      StringBuffer sb = new StringBuffer("SELECT ");
      boolean ffirst = true;
      int axisCounter = 0;
      int axesPresent = 0;
      boolean pot[] = new boolean[dimensionSize];
      for (int i = 0; i < dimensionSize; i++) {
        pot[i] = false;
        elementIndexMap[i] = new HashMap();
        Set axisSet = new HashSet();
        axes[i] = new StringBuffer();
        boolean first = true;
        querySize[i] = 0;
        int j = 0;
        for (int m = elements.length; j < m; j++)
          if (!axisSet.contains(elements[j][i])) {
            axisSet.add(elements[j][i]);
            elementIndexMap[i].put(elements[j][i].getId(), new Integer(axisSet
              .size() - 1));
            if (!first)
              axes[i].append(", ");
            elementPositions.put(elements[j][i].getUniqueName(), Integer
              .valueOf(querySize[i]));
            querySize[i]++;
            String uniqueName = elements[j][i].getUniqueName();
            axes[i].append(uniqueName);
            first = false;
          }

        if (axisSet.size() > 1) {
          String xmlaDimName = ((XMLADimensionInfo) elements[0][i].getDimension())
            .getDimensionUniqueName();
          allXmlaTypes.add(xmlaDimName);
          axesPresent++;
          if (!ffirst)
            sb.append(",\n");
          sb.append((new StringBuilder("{")).append(axes[i]).append("} on Axis (")
            .append(axisCounter).append(")").toString());
          axisCounter++;
          ffirst = false;
        } else if (axisSet.size() == 1)
          pot[i] = true;
      }

      for (int i = 0; i < dimensionSize; i++)
        if (pot[i] && axesPresent < 2) {
          String xmlaDimName = ((XMLADimensionInfo) elements[0][i].getDimension())
            .getDimensionUniqueName();
          String defName = ((XMLADimensionInfo) elements[0][i].getDimension())
            .getDefaultElementName();
          if (!elements[0][i].getUniqueName().equals(defName)) {
            allXmlaTypes.add(xmlaDimName);
            if (!ffirst)
              sb.append(",\n");
            sb.append((new StringBuilder("{")).append(axes[i]).append("} on Axis (")
              .append(axisCounter).append(")").toString());
            axisCounter++;
            axesPresent++;
            ffirst = false;
          }
        } else if (pot[i]) {
          String xmlaDimName = ((XMLADimensionInfo) elements[0][i].getDimension())
            .getDimensionUniqueName();
          String defName = ((XMLADimensionInfo) elements[0][i].getDimension())
            .getDefaultElementName();
          if (elements[0][i].getUniqueName().equals(defName)) {
            axes[i] = new StringBuffer("+");
          } else {
            allXmlaTypes.add(xmlaDimName);
            if (where.length() > 1)
              where.append(", ");
            where.append(axes[i]);
            axes[i] = new StringBuffer("+");
          }
        }

      where.append(")");
      sb.append((new StringBuilder(" FROM [")).append(cube.getId()).append("]")
        .toString());
      if (where.length() > 2)
        sb.append((new StringBuilder(" WHERE ")).append(where).append("\n")
          .toString());
      elementMapper = new HashMap();
      for (int i = 0; i < elements.length; i++) {
        for (int j = 0; j < elements[i].length; j++)
          elementMapper.put(elements[i][j].getUniqueName(), ((Object) (new Object[] {
            elements[i][j], Integer.valueOf(j),
            elementPositions.get(elements[i][j].getUniqueName()) })));

      }

      if (sb.length() > 0x186a0)
        sb = shortenQuery(client, cube, sb, querySize, elementIndexMap, axes,
          dimensionSize, where, elementMapper);
      sb = new StringBuffer(applyDatabaseSpecificMDX(sb.toString(), client, cube));
      if (XMLAClient.isDebug())
        System.out.println((new StringBuilder("GetDataBulk:\n")).append(sb).append(
          "\n------------\n\n").toString());
      doc = client.execute(sb.toString(), prop);
      NodeList faults = doc.getElementsByTagName("SOAP-ENV:Fault");
      if (faults != null && faults.getLength() > 0) {
        // //break MISSING_BLOCK_LABEL_1211;
        cInfo = new CellInfo[1];
        String result = XMLAClient.getErrorString(faults);
        cInfo[0] = new CellInfoImpl(99, true, result);
        ((CellInfoImpl) cInfo[0]).setCoordinate(new String[0]);
        return cInfo;
      }
      String resultCoordinates[][];
      NodeList cl;
      resultCoordinates = parseResultCoordinates(doc);
      cl = doc.getElementsByTagName("CellData");
      if (cl == null)
        return new CellInfo[0];
      if (cl.item(0) == null)
        return new CellInfo[0];
      CellInfo resValues[];
      cl = cl.item(0).getChildNodes();
      int totalElements = 1;
      for (int i = 0; i < dimensionSize; i++)
        if (querySize[i] != 0)
          totalElements *= querySize[i];
        else
          querySize[i] = 1;

      int childCardinalities[] = new int[dimensionSize];
      childCardinalities[0] = 1;
      for (int i = 1; i < dimensionSize; i++)
        childCardinalities[i] = querySize[i - 1] * childCardinalities[i - 1];

      resultValues = new CellInfo[totalElements];
      for (int i = 0; i < totalElements; i++)
        resultValues[i] = new CellInfoImpl(1, false, "");

      int cellOrdinal = -1;
      if (cl != null) {
        for (int i = 0; i < cl.getLength(); i++)
          if (cl.item(i).getNodeType() == 1) {
            Node ordinal = cl.item(i).getAttributes().getNamedItem("CellOrdinal");
            if (ordinal != null) {
              cellOrdinal = Integer.parseInt(ordinal.getNodeValue());
              Node cellNode = cl.item(i);
              NodeList nl = cellNode.getChildNodes();
              for (int j = 0; j < nl.getLength(); j++)
                if (nl.item(j).getNodeType() == 1
                  && nl.item(j).getNodeName().equals("Value"))
                  resultValues[transformCellOrdinal(cellOrdinal, resultCoordinates,
                    elementMapper, querySize)] = parseValueNode(nl.item(j),
                    new String[] { "a", "b" });

            }
          }

      }
      resValues = new CellInfo[elements.length];
      int i = 0;
      for (int n = elements.length; i < n; i++) {
        int index = 0;
        String coords[] = new String[dimensionSize];
        for (int j = 0; j < dimensionSize; j++) {
          Integer iOffset = (Integer) elementIndexMap[j].get(elements[i][j].getId());
          if (iOffset != null)
            index += iOffset.intValue() * childCardinalities[j];
          coords[j] = elements[i][j].getId();
        }

        resValues[i] = resultValues[index];
        if (resValues[i] == null) {
          resValues[i] = new CellInfoImpl(1, false, "");
          ((CellInfoImpl) resValues[i]).setCoordinate(coords);
        } else {
          ((CellInfoImpl) resValues[i]).setCoordinate(coords);
        }
      }
      return resValues;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return (new CellInfo[] { new CellInfoImpl(1, false, "") });
  }

  public void setCubeListInternal(XMLADatabaseInfo database, List cubeList) {
    throw new RuntimeException("No longer supported.");
  }

  private XMLAClient xmlaClient;

  private String connectionName;

  private XMLADatabaseInfo database;

  private BigInteger cellCount;

  private XMLAConnection xmlaConnection;
}