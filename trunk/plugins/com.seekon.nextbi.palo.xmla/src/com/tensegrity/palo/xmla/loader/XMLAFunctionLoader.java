package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.loader.FunctionLoader;

public class XMLAFunctionLoader extends FunctionLoader {
  public XMLAFunctionLoader(DbConnection paramDbConnection) {
    super(paramDbConnection);
  }

  public String loadAll() {
    return this.paloConnection.listFunctions();
  }

  protected final void reload() {
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.loader.XMLAFunctionLoader JD-Core
 * Version: 0.5.4
 */