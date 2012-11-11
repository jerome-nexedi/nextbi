package com.seekon.nextbi.spreadsheet.palo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.palo.api.Connection;
import org.palo.api.ConnectionFactory;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Element;

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

  public List<Map<String, String>> dimension_list_elements(String databaseName,
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
    return result;
  }

  public List palo_datav(String databaseName, String cubeName, List rowElements,
    String columnElement) {
    Object[] data = palo_datav(databaseName, cubeName,
      checkAndGetAllowedElementList(rowElements), new String[] { columnElement });
    List result = rowElements.subList(0, 2);
    result.addAll(Arrays.asList(data));
    return result;
  }

  public List palo_datav(String databaseName, String cubeName, List rowElements,
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

    return result;
  }

  public List palo_datav(String databaseName, String cubeName, String rowElement,
    List columnElements) {
    Object[] data = palo_datav(databaseName, cubeName, new String[] { rowElement },
      checkAndGetAllowedElementList(columnElements));
    List result = columnElements.subList(0, 2);
    result.addAll(Arrays.asList(data));
    return result;
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

  public List<String> palo_root_list_databases() {
    Database[] databases = connection.getDatabases();
    List<String> result = new ArrayList<String>();
    for (Database db : databases) {
      result.add(db.getName());
    }
    return result;
  }

  public List<String> palo_database_list_dimensions(String databaseName,
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
    return result;
  }

  public List<String> palo_cube_list_dimensions(String databaseName, String cubeName) {
    Database database = connection.getDatabaseByName(databaseName);
    Cube cube = database.getCubeByName(cubeName);
    Dimension[] dimensions = cube.getDimensions();
    
    List<String> result = new ArrayList<String>();
    for(Dimension dimesion: dimensions){
      result.add(dimesion.getName());
    }
    return result;
  }
}
