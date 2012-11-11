package com.seekon.nextbi.spreadsheet.palo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.caucho.quercus.module.AbstractQuercusModule;

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

  public List<Map<String, String>> palo_dimension_list_elements2(
    PaloResource resource, String databaseName, String dimName, boolean sign) {
    return resource.dimension_list_elements(databaseName, dimName);
  }

  public List<Map<String, String>> palo_dimension_list_elements(
    PaloResource resource, String databaseName, String dimName, boolean sign) {
    return resource.dimension_list_elements(databaseName, dimName);
  }

  public List<Map<String, String>> palo_dimension_list_elements(
    PaloResource resource, String databaseName, String dimName) {
    return resource.dimension_list_elements(databaseName, dimName);
  }

  public List palo_datav(PaloResource resource, String databaseName,
    String cubeName, List rowElements, List columnElements) {
    return resource.palo_datav(databaseName, cubeName, rowElements, columnElements);
  }

  public List palo_datav(PaloResource resource, String databaseName,
    String cubeName, List rowElements, String columnElement) {
    return resource.palo_datav(databaseName, cubeName, rowElements, columnElement);
  }

  public List palo_datav(PaloResource resource, String databaseName,
    String cubeName, String rowElement, List columnElements) {
    return resource.palo_datav(databaseName, cubeName, rowElement, columnElements);
  }

  public void palo_eindex() {

  }

  public void palo_eadd() {

  }

  public List<String> palo_root_list_databases(PaloResource resource) {
    return resource.palo_root_list_databases();
  }

  public List<String> palo_database_list_dimensions(PaloResource resource,
    String databaseName, int dimType) {
    return resource.palo_database_list_dimensions(databaseName, dimType);
  }

  public List<String> palo_database_list_dimensions(PaloResource resource,
    String databaseName) {
    return resource.palo_database_list_dimensions(databaseName, -1);
  }

  public void palo_debug_out(String message) {
    System.out.println(message);
  }

  public static void main(String[] args) {
    LibpaloModule module = new LibpaloModule();
    PaloResource resource = null;
    try {
      resource = module.palo_init("127.0.0.1", "7921", "admin", "admin");
      module.palo_ping(resource);
      module.palo_use_unicode(true);
      List<Map<String, String>> dimElements = module.palo_dimension_list_elements(
        resource, "System", "#_GROUP_");
      List colElements_ug = new ArrayList();
      for (Map<String, String> element : dimElements) {
        colElements_ug.add(element.get("name"));
      }
      int colElementCount = colElements_ug.size();
      colElements_ug.add(0, 1);
      colElements_ug.add(0, colElementCount);
      System.out.println(module.palo_datav(resource, "System", "#_USER_GROUP", "admin", colElements_ug));

      List<String> databaseList = module.palo_root_list_databases(resource);
      for (String database : databaseList) {
        module.palo_database_list_dimensions(resource, database, 0);
      }

      List datav = module.palo_datav(resource, "Config", "#_connections", Arrays
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
