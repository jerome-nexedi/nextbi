package com.seekon.nextbi.spreadsheet.palo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.palo.api.Connection;
import org.palo.api.ConnectionFactory;
import org.palo.api.Consolidation;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Rule;
import org.palo.api.Subset;

import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DimensionInfo;

import com.caucho.quercus.annotation.ResourceType;

@ResourceType("palo service")
public class PaloResource {

  private Connection connection = null;

  public PaloResource(String server, String port, String username, String password) {
    super();
    connection = ConnectionFactory.getInstance().newConnection(server, port,
      username, password);
  }

  public void disconnect() {
    connection.disconnect();
  }

  public void ping() {
    connection.ping();
  }

  public Object[] dimension_list_elements(String databaseName,
    String dimName) {
    Database database = connection.getDatabaseByName(databaseName);
    Dimension dimension = database.getDimensionByName(dimName);
    Element[] elements = dimension.getElements();

    List<Map<String, String>> result = new ArrayList<Map<String, String>>();
    for (Element element : elements) {
      Map<String, String> entry = new HashMap<String, String>();
      entry.put("name", element.getName());
      entry.put("type", element.getTypeAsString());
      entry.put("identifier", element.getId());
      result.add(entry);
    }
    return result.toArray();
  }

  public Object[] palo_datav(String databaseName, String cubeName, List rowElements,
    String columnElement) {
    Object[] data = palo_datav(databaseName, cubeName,
      checkAndGetAllowedElementList(rowElements), new String[] { columnElement });
    List result = rowElements.subList(0, 2);
    result.addAll(Arrays.asList(data));
    return result.toArray();
  }

  public Object[] palo_datav(String databaseName, String cubeName, List rowElements,
    List columnElements) {
    Object[] data = palo_datav(databaseName, cubeName,
      checkAndGetAllowedElementList(rowElements),
      checkAndGetAllowedElementList(columnElements));

    List result = new ArrayList();
    int fromIndex_0 = Integer.valueOf(rowElements.get(0).toString());
    int toIndex_0 = Integer.valueOf(rowElements.get(1).toString());
    int fromIndex_1 = Integer.valueOf(columnElements.get(0).toString());
    int toIndex_1 = Integer.valueOf(columnElements.get(1).toString());
    if ((fromIndex_0 > toIndex_0) && (fromIndex_1 != toIndex_1)) {//按先行再列的顺序输出
      int rowCount = Math.abs(toIndex_0 - fromIndex_0) + 1;
      int columnCount = Math.abs(toIndex_1 - fromIndex_1) + 1;
      for (int j = 0; j < rowCount; j++) {
        for (int i = 0; i < columnCount * rowCount; i = i + rowCount) {
          result.add(data[j + i]);
        }
      }
    } else {
      result.addAll(Arrays.asList(data));
    }

    result.add(0, fromIndex_1 * toIndex_1);
    result.add(0, fromIndex_0 * toIndex_0);

    return result.toArray();
  }

  public Object[] palo_datav(String databaseName, String cubeName, String rowElement,
    List columnElements) {
    Object[] data = palo_datav(databaseName, cubeName, new String[] { rowElement },
      checkAndGetAllowedElementList(columnElements));
    List result = columnElements.subList(0, 2);
    result.addAll(Arrays.asList(data));
    return result.toArray();
  }

  private String[] checkAndGetAllowedElementList(List elements) {
    if (elements.size() < 3) {
      throw new RuntimeException("参数rowElements中的元素个数必须大于2。");
    }

    int fromIndex = Integer.valueOf(elements.get(0).toString());
    int toIndex = Integer.valueOf(elements.get(1).toString());
    if (fromIndex > toIndex) {
      int temp = fromIndex;
      fromIndex = toIndex;
      toIndex = temp;
    }

    if (elements.size() < toIndex + 2) {
      throw new RuntimeException("参数rowElements中的元素个数必须大于等于" + (toIndex + 2));
    }

    List subElementList = elements.subList(fromIndex + 1, toIndex + 2);
    return (String[]) subElementList.toArray(new String[subElementList.size()]);
  }

  public Object[] palo_datav(String databaseName, String cubeName,
    String[] rowElements, String[] columnElements) {
    Database database = connection.getDatabaseByName(databaseName);
    Cube cube = database.getCubeByName(cubeName);

    return cube.getDataArray(new String[][] { rowElements, columnElements });
  }

  public Object[] palo_root_list_databases() {
    Database[] databases = connection.getDatabases();
    List<String> result = new ArrayList<String>();
    for (Database db : databases) {
      result.add(db.getName());
    }
    return result.toArray();
  }

  public Object[] palo_database_list_dimensions(String databaseName,
    int dimExtendedType) {
    Database database = connection.getDatabaseByName(databaseName);
    Dimension[] dimensions = database.getDimensions();
    List<String> result = new ArrayList<String>();
    //System.out.println("###" + databaseName);
    for (Dimension dim : dimensions) {
      if (dimExtendedType != -1) {
        //System.out.println(dim.getType() + ":" + dim.getName() + ":" + dim.getExtendedType());
        if (dimExtendedType == dim.getExtendedType()) {
          result.add(dim.getName());
        }
      } else {
        result.add(dim.getName());
      }
    }
    return result.toArray();
  }

  public Object[] palo_cube_list_dimensions(String databaseName, String cubeName) {
    Database database = connection.getDatabaseByName(databaseName);
    Cube cube = database.getCubeByName(cubeName);
    Dimension[] dimensions = cube.getDimensions();

    List<String> result = new ArrayList<String>();
    for (Dimension dimesion : dimensions) {
      result.add(dimesion.getName());
    }
    return result.toArray();
  }

  public boolean palo_database_add_dimension(String databaseName, String dimName) {
    Database database = connection.getDatabaseByName(databaseName);
    try {
      database.addDimension(dimName);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean palo_database_rename_dimension(String databaseName, String dimName,
    String newDimName) {
    Database database = connection.getDatabaseByName(databaseName);
    try {
      Dimension dim = database.getDimensionByName(dimName);
      dim.rename(newDimName);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public Object[] palo_dimension_list_cubes(String databaseName, String dimName) {
    List<String> result = new ArrayList<String>();
    Database database = connection.getDatabaseByName(databaseName);
    Dimension dim = database.getDimensionByName(dimName);
    Cube[] cubes = dim.getCubes();
    for (Cube cube : cubes) {
      result.add(cube.getName());
    }
    return result.toArray();
  }

  public boolean palo_database_delete_dimension(String databaseName, String dimName) {
    Database database = connection.getDatabaseByName(databaseName);
    try {
      database.removeDimension(database.getDimensionByName(dimName));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  //TODO:
  public Object[] palo_dimension_info(String databaseName, String dimName) {
    Database database = connection.getDatabaseByName(databaseName);
    Dimension dim = database.getDimensionByName(dimName);

    List<Object> result = new ArrayList<Object>();
    DimensionInfo dimInfo = dim.getInfo();
    result.add(dimInfo.getId());
    result.add(dimInfo.getName());
    result.add(dimInfo.getMaxDepth());
    result.add(dimInfo.getMaxIndent());
    result.add(dimInfo.getMaxLevel());
    result.add(dimInfo.getToken());

    return result.toArray();
  }

  public Object[] palo_database_list_cubes(String databaseName, String cubeType) {
    List<String> result = new ArrayList<String>();
    Database database = connection.getDatabaseByName(databaseName);
    Cube[] cubes = database.getCubes();
    for (Cube cube : cubes) {
      if (cubeType.equals(cube.getType())) {
        result.add(cube.getName());
      }
    }
    return result.toArray();
  }

  public Map<String, Object> palo_cube_info(String databaseName, String cubeName) {
    Database database = connection.getDatabaseByName(databaseName);
    Cube cube = database.getCubeByName(cubeName);

    Map<String, Object> result = new HashMap<String, Object>();
    CubeInfo cubeInfo = cube.getInfo();
    result.put("identifier", cubeInfo.getId());
    result.put("number_dimensions", cubeInfo.getDimensionCount());
    result.put("dimensions", cubeInfo.getDimensions());
    result.put("number_cells", cubeInfo.getCellCount());
    result.put("number_filled_cells", cubeInfo.getFilledCellCount());
    result.put("status", cubeInfo.getStatus());
    return result;
  }

  public boolean palo_database_delete_cube(String databaseName, String cubeName) {
    Database database = connection.getDatabaseByName(databaseName);
    try {
      database.removeCube(database.getCubeByName(cubeName));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean palo_rename_cube(String databaseName, String cubeName,
    String newCubeName) {
    try {
      Database database = connection.getDatabaseByName(databaseName);
      Cube cube = database.getCubeByName(cubeName);
      cube.rename(newCubeName);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean palo_database_add_cube(String databaseName, String cubeName,
    List<String> dimNames) {
    try {
      Database database = connection.getDatabaseByName(databaseName);
      List<Dimension> dimList = new ArrayList<Dimension>();
      for (String dimName : dimNames) {
        Dimension dim = database.getDimensionByName(dimName);
        if (dim == null) {
          dim = database.addDimension(dimName);
        }
        dimList.add(dim);
      }
      database.addCube(cubeName, dimList.toArray(new Dimension[dimList.size()]));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public int palo_eparentcount(String databaseName, String dimName,
    String elementName) {
    Database database = connection.getDatabaseByName(databaseName);
    Element element = database.getDimensionByName(dimName).getElementByName(
      elementName);
    return element.getParentCount();
  }

  public String palo_eparentname(String databaseName, String dimName,
    String elementName, int index) {
    Database database = connection.getDatabaseByName(databaseName);
    Element element = database.getDimensionByName(dimName).getElementByName(
      elementName);
    Element[] parents = element.getParents();
    return parents[index].getName();
  }

  public boolean palo_edelete(String databaseName, String dimName, String elementName) {
    try {
      Database database = connection.getDatabaseByName(databaseName);
      Dimension dim = database.getDimensionByName(dimName);
      dim.removeElement(dim.getElementByName(elementName));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public int palo_echildcount(String databaseName, String dimName, String elementName) {
    Database db = connection.getDatabaseByName(databaseName);
    return db.getDimensionByName(dimName).getElementByName(elementName)
      .getChildCount();
  }

  public boolean palo_erename(String databaseName, String dimName,
    String elementName, String newElementName) {
    try {
      Database db = connection.getDatabaseByName(databaseName);
      Element element = db.getDimensionByName(dimName).getElementByName(elementName);
      element.rename(newElementName);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public Object[] palo_element_list_consolidation_elements(String databaseName,
    String dimName, String elementName) {
    List<String> result = new ArrayList<String>();
    Database db = connection.getDatabaseByName(databaseName);
    Element element = db.getDimensionByName(dimName).getElementByName(elementName);
    Consolidation[] cons = element.getConsolidations();
    for (Consolidation con : cons) {
      result.add(con.getChild().getName());
    }
    return result.toArray();
  }

  //TODO:
  public boolean palo_eupdate(String databaseName, String dimName,
    String elementName, int type, List fixedElemList) {
    return true;
  }

  public int palo_eindex(String databaseName, String dimName, String elementName) {
    Database db = connection.getDatabaseByName(databaseName);
    Element element = db.getDimensionByName(dimName).getElementByName(elementName);
    return element.getPosition();
  }

  public boolean palo_emove(String databaseName, String dimName, String elementName,
    int newIndex) {
    try {
      Database db = connection.getDatabaseByName(databaseName);
      db.getDimensionByName(dimName).getElementByName(elementName).move(newIndex);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public int palo_ecount(String databaseName, String dimName) {
    Database db = connection.getDatabaseByName(databaseName);
    return db.getDimensionByName(dimName).getElementCount();
  }

  public boolean palo_root_add_database(String databaseName) {
    try {
      connection.addDatabase(databaseName);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean palo_root_delete_database(String databaseName) {
    try {
      connection.removeDatabase(connection.getDatabaseByName(databaseName));
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  //TODO
  public String palo_data(String databaseName, String dimName, String subsetName) {
    Database db = connection.getDatabaseByName(databaseName);
    Subset subset = db.getDimensionByName(databaseName).getSubset(subsetName);
    return subset.getName();
  }

  public String palo_get_element_name(String databaseName, String dimName,
    String elementId) {
    Database db = connection.getDatabaseByName(databaseName);
    return db.getDimensionByName(dimName).getElementById(elementId).getName();
  }

  public String palo_get_cube_name(String databaseName, String cubeId) {
    Database db = connection.getDatabaseByName(databaseName);
    return db.getCubeById(cubeId).getName();
  }

  public Object[] palo_cube_rules(String databaseName, String cubeName) {
    List<String> result = new ArrayList<String>();
    Database db = connection.getDatabaseByName(databaseName);
    Rule[] rules = db.getCubeByName(cubeName).getRules();
    for (Rule rule : rules) {
      result.add(rule.getId());
    }
    return result.toArray();
  }

  public boolean palo_cube_rule_create(String databaseName, String cubeName,
    String definition, String id, String comment, boolean activate) {
    try {
      Database db = connection.getDatabaseByName(databaseName);
      Cube cube = db.getCubeByName(cubeName);
      cube.addRule(definition, id, true, comment, activate);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean palo_cube_rule_delete(String databaseName, String cubeName,
    String ruleId) {
    try {
      Database db = connection.getDatabaseByName(databaseName);
      Cube cube = db.getCubeByName(cubeName);
      cube.removeRule(ruleId);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean palo_cube_rule_modify(PaloResource resource, String databaseName,
    String cubeName, String identifier, String definition, String externId,
    String comment, boolean activate) {
    try {
      Database db = connection.getDatabaseByName(databaseName);
      Cube cube = db.getCubeByName(cubeName);
      Rule[] rules = cube.getRules();
      for (Rule rule : rules) {
        if (identifier.equals(rule.getId())) {
          rule.update(definition, externId, true, comment, activate);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  //TODO:
  public boolean palo_setdataa(Object[] values, boolean splashMode,
    String databaseName, String cubeName, List<String> coordinates) {
    Database db = connection.getDatabaseByName(databaseName);
    Cube cube = db.getCubeByName(cubeName);
    //cube.setDataArray(coordinates, values, splashMode);

    return true;
  }

  public void palo_element_create_bulk(String databaseName, String dimName) {
    Database db = connection.getDatabaseByName(databaseName);
    Dimension dim = db.getDimensionByName(dimName);
  }

  public void palo_eadd(String databaseName, String dimName, int elemType,
    String elemName, String parent_element_name, double consolidation_factor,
    boolean clear, boolean empty_string) {
    Database db = connection.getDatabaseByName(databaseName);
    Dimension dim = db.getDimensionByName(dimName);
    Element element = dim.addElement(elemName, elemType);
  }
  
  public String palo_get_element_id(String databaseName, String dimName, String elemName){
    Database db = connection.getDatabaseByName(databaseName);
    Dimension dim = db.getDimensionByName(dimName);
    Element element = dim.getElementByName(elemName);
    return element.getId();
  }
  
  public String palo_get_cube_id(PaloResource resource,String databaseName, String cubeName) {
    Database db = connection.getDatabaseByName(databaseName);
    Cube cube = db.getCubeByName(cubeName);
    return cube.getId();
  }
  
  public String palo_dataa(String databaseName, String cubeName, String[] coordinates){
    Database db = connection.getDatabaseByName(databaseName);
    Cube cube = db.getCubeByName(cubeName);
    Object data = cube.getData(coordinates);
    return data.toString();
  }
}
