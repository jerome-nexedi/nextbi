package com.tensegrity.palo.xmla.loader;

import com.tensegrity.palo.xmla.XMLAClient;
import com.tensegrity.palojava.CubeInfo;
import com.tensegrity.palojava.DbConnection;
import com.tensegrity.palojava.RuleInfo;
import com.tensegrity.palojava.loader.RuleLoader;

public class XMLARuleLoader extends RuleLoader {
  private final XMLAClient xmlaClient;

  public XMLARuleLoader(DbConnection paramDbConnection, XMLAClient paramXMLAClient,
    CubeInfo paramCubeInfo) {
    super(paramDbConnection, paramCubeInfo);
    this.xmlaClient = paramXMLAClient;
  }

  public String[] getAllRuleIds() {
    reload();
    return getLoadedIds();
  }

  protected final void reload() {
    reset();
    RuleInfo[] arrayOfRuleInfo1 = this.paloConnection.getRules(this.cube);
    for (RuleInfo localRuleInfo : arrayOfRuleInfo1)
      loaded(localRuleInfo);
  }
}

/*
 * Location:
 * D:\server\apache-tomcat-5.5.20\webapps\Palo-Pivot\WEB-INF\lib\paloxmla.jar
 * Qualified Name: com.tensegrity.palo.xmla.loader.XMLARuleLoader JD-Core
 * Version: 0.5.4
 */