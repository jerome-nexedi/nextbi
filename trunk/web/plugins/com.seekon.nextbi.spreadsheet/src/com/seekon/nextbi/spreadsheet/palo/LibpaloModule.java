package com.seekon.nextbi.spreadsheet.palo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.palo.api.Element;

import com.caucho.quercus.module.AbstractQuercusModule;

/**
 * 
 * palo olap服务的php访问接口
 *
 */
public class LibpaloModule extends AbstractQuercusModule {

  public void palo_disconnect(PaloResource resource) {
    resource.disconnect();
  }

  public PaloResource palo_init(String server, String port, String username,
    String password) {
    return new PaloResource(server, port, username, password);
  }

  public void palo_ping(PaloResource resource) {
    resource.ping();
  }

  public void palo_use_unicode(boolean unicode) {
    //do nothing
  }

  public Object[] palo_dimension_list_elements2(PaloResource resource,
    String databaseName, String dimName, boolean sign) {
    return resource.dimension_list_elements(databaseName, dimName);
  }

  public Object[] palo_dimension_list_elements(PaloResource resource,
    String databaseName, String dimName, boolean sign) {
    return resource.dimension_list_elements(databaseName, dimName);
  }

  public Object[] palo_dimension_list_elements(PaloResource resource,
    String databaseName, String dimName) {
    return resource.dimension_list_elements(databaseName, dimName);
  }

  public Object[] palo_datav(PaloResource resource, String databaseName,
    String cubeName, List rowElements, List columnElements) {
    return resource.palo_datav(databaseName, cubeName, rowElements, columnElements);
  }

  public Object[] palo_datav(PaloResource resource, String databaseName,
    String cubeName, List rowElements, String columnElement) {
    return resource.palo_datav(databaseName, cubeName, rowElements, columnElement);
  }

  public Object[] palo_datav(PaloResource resource, String databaseName,
    String cubeName, String rowElement, List columnElements) {
    return resource.palo_datav(databaseName, cubeName, rowElement, columnElements);
  }

  public Object[] palo_root_list_databases(PaloResource resource) {
    return resource.palo_root_list_databases();
  }

  public Object[] palo_database_list_dimensions(PaloResource resource,
    String databaseName, int dimType) {
    return resource.palo_database_list_dimensions(databaseName, dimType);
  }

  public Object[] palo_database_list_dimensions(PaloResource resource,
    String databaseName) {
    return resource.palo_database_list_dimensions(databaseName, -1);
  }

  public Object[] palo_cube_list_dimensions(PaloResource resource,
    String databaseName, String cubeName) {
    return resource.palo_cube_list_dimensions(databaseName, cubeName);
  }

  public boolean palo_database_add_dimension(PaloResource resource,
    String databaseName, String dimName) {
    return resource.palo_database_add_dimension(databaseName, dimName);
  }

  public boolean palo_database_rename_dimension(PaloResource resource,
    String databaseName, String dimName, String newDimName) {
    return resource
      .palo_database_rename_dimension(databaseName, dimName, newDimName);
  }

  public Object[] palo_dimension_list_cubes(PaloResource resource,
    String databaseName, String dimName) {
    return resource.palo_dimension_list_cubes(databaseName, dimName);
  }

  public boolean palo_database_delete_dimension(PaloResource resource,
    String databaseName, String dimName) {
    return resource.palo_database_delete_dimension(databaseName, dimName);
  }

  //TODO:
  public Object[] palo_dimension_info(PaloResource resource, String databaseName,
    String dimName) {
    return resource.palo_dimension_info(databaseName, dimName);
  }

  public Object[] palo_database_list_cubes(PaloResource resource,
    String databaseName, String cubeType) {
    return resource.palo_database_list_cubes(databaseName, cubeType);
  }

  public Map<String, Object> palo_cube_info(PaloResource resource,
    String databaseName, String cubeName) {
    return resource.palo_cube_info(databaseName, cubeName);
  }

  public boolean palo_database_delete_cube(PaloResource resource,
    String databaseName, String cubeName) {
    return resource.palo_database_delete_cube(databaseName, cubeName);
  }

  public boolean palo_rename_cube(PaloResource resource, String databaseName,
    String cubeName, String newCubeName) {
    return resource.palo_rename_cube(databaseName, cubeName, newCubeName);
  }

  public boolean palo_database_add_cube(PaloResource resource, String databaseName,
    String cubeName, List<String> dimNames) {
    return resource.palo_database_add_cube(databaseName, cubeName, dimNames);
  }

  public int palo_eparentcount(PaloResource resource, String databaseName,
    String dimName, String elementName) {
    return resource.palo_eparentcount(databaseName, dimName, elementName);
  }

  public String palo_eparentname(PaloResource resource, String databaseName,
    String dimName, String elementName, int index) {
    return resource.palo_eparentname(databaseName, dimName, elementName, index);
  }

  public boolean palo_edelete(PaloResource resource, String databaseName,
    String dimName, String elementName) {
    return resource.palo_edelete(databaseName, dimName, elementName);
  }

  public int palo_echildcount(PaloResource resource, String databaseName,
    String dimName, String elementName) {
    return resource.palo_echildcount(databaseName, dimName, elementName);
  }

  public boolean palo_erename(PaloResource resource, String databaseName,
    String dimName, String elementName, String newElementName) {
    return resource.palo_erename(databaseName, dimName, elementName, newElementName);
  }

  public Object[] palo_element_list_consolidation_elements(PaloResource resource,
    String databaseName, String dimName, String elementName) {
    return resource.palo_element_list_consolidation_elements(databaseName, dimName,
      elementName);
  }

  public boolean palo_eupdate(PaloResource resource, String databaseName,
    String dimName, String elementName, int type, List fixedElemList) {
    return resource.palo_eupdate(databaseName, dimName, elementName, type,
      fixedElemList);
  }

  public int palo_eindex(PaloResource resource, String databaseName, String dimName,
    String elementName) {
    return resource.palo_eindex(databaseName, dimName, elementName);
  }

  public int palo_eindex(PaloResource resource, String databaseName, String dimName,
    String elementName, boolean sign) {
    return palo_eindex(resource, databaseName, dimName, elementName);
  }

  public boolean palo_emove(PaloResource resource, String databaseName,
    String dimName, String elementName, int newIndex) {
    return resource.palo_emove(databaseName, dimName, elementName, newIndex);
  }

  public int palo_ecount(PaloResource resource, String databaseName, String dimName) {
    return resource.palo_ecount(databaseName, dimName);
  }

  //TODO
  public void palo_eadd(PaloResource resource, String databaseName, String dimName,
    String elemType, String elemName, String parent_element_name,
    double consolidation_factor, boolean clear, boolean empty_string) {
    int type = Element.ELEMENTTYPE_NUMERIC;
    if (elemType.equalsIgnoreCase("S")) {
      type = Element.ELEMENTTYPE_STRING;
    }
    resource.palo_eadd(databaseName, dimName, type, elemName, parent_element_name,
      consolidation_factor, clear, empty_string);
  }

  //TODO
  public void palo_setdata() {

  }

  public boolean palo_root_add_database(PaloResource resource, String databaseName) {
    return resource.palo_root_add_database(databaseName);
  }

  public boolean palo_root_delete_database(PaloResource resource, String databaseName) {
    return resource.palo_root_delete_database(databaseName);
  }

  public String palo_data(PaloResource resource, String databaseName,
    String dimName, String subsetName) {
    return resource.palo_data(databaseName, dimName, subsetName);
  }

  public String palo_get_element_name(PaloResource resource, String databaseName,
    String dimName, String elementId) {
    return resource.palo_get_element_name(databaseName, dimName, elementId);
  }

  public String palo_get_cube_name(PaloResource resource, String databaseName,
    String cubeId) {
    return resource.palo_get_cube_name(databaseName, cubeId);
  }

  //TODO:
  public String palo_get_element_id(PaloResource resource, String databaseName,
    String dimName, String elemName) {
    return resource.palo_get_element_id(databaseName, dimName, elemName);
  }

  //TODO:
  public String palo_get_cube_id(PaloResource resource, String databaseName,
    String cubeName) {
    return resource.palo_get_cube_id(resource, databaseName, cubeName);
  }

  public Object[] palo_cube_rules(PaloResource resource, String databaseName,
    String cubeName) {
    return resource.palo_cube_rules(databaseName, cubeName);
  }

  public boolean palo_cube_rule_create(PaloResource resource, String databaseName,
    String cubeName, String definition, String id, String comment, boolean activate) {
    return resource.palo_cube_rule_create(databaseName, cubeName, definition, id,
      comment, activate);
  }

  public boolean palo_cube_rule_delete(PaloResource resource, String databaseName,
    String cubeName, String ruleId) {
    return resource.palo_cube_rule_delete(databaseName, cubeName, ruleId);
  }

  public boolean palo_cube_rule_modify(PaloResource resource, String databaseName,
    String cubeName, String identifier, String definition, String externId,
    String comment, boolean activate) {
    return resource.palo_cube_rule_modify(resource, databaseName, cubeName,
      identifier, definition, externId, comment, activate);
  }

  //TODO
  public boolean palo_setdataa(Object[] values, boolean splashMode,
    PaloResource resource, String databaseName, String cubeName,
    List<String> dimNames) {
    return resource.palo_setdataa(values, splashMode, databaseName, cubeName,
      dimNames);
  }

  //TODO
  public void palo_element_create_bulk(PaloResource resource, String databaseName) {

  }

  //TODO
  public void palo_setdata_bulk() {

  }

  //TODO
  public void palo_edelete_bulk() {

  }

  //TODO
  public String palo_dataa(PaloResource resource, String databaseName,
    String cubeName, String[] coordinates) {
    return resource.palo_dataa(databaseName, cubeName, coordinates);
  }

  public static void main(String[] args) {
    LibpaloModule module = new LibpaloModule();
    PaloResource resource = null;
    try {
      resource = module.palo_init("127.0.0.1", "7921", "admin", "admin");
      module.palo_ping(resource);
      module.palo_use_unicode(true);
      Object[] dimElements = module.palo_dimension_list_elements(resource, "System",
        "#_GROUP_");
      List colElements_ug = new ArrayList();
      for (Object element : dimElements) {
        colElements_ug.add(((Map<String, String>) element).get("name"));
      }
      int colElementCount = colElements_ug.size();
      colElements_ug.add(0, 1);
      colElements_ug.add(0, colElementCount);
      System.out.println(module.palo_datav(resource, "System", "#_USER_GROUP",
        "admin", colElements_ug));

      Object[] databaseList = module.palo_root_list_databases(resource);
      for (Object database : databaseList) {
        module.palo_database_list_dimensions(resource, (String) database, 0);
      }

      Object[] datav = module.palo_datav(resource, "Config", "#_connections", Arrays
        .asList(new Object[] { 2, 1, "name", "type" }), Arrays.asList(new Object[] {
        1, 2, "Palo local", "Palo remote" }));
      System.out.println(datav);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (resource != null) {
        module.palo_disconnect(resource);
      }
    }
  }
}
