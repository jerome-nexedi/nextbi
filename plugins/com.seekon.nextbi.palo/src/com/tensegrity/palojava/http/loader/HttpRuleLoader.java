/*    */package com.tensegrity.palojava.http.loader;

/*    */
/*    */import com.tensegrity.palojava.CubeInfo; /*    */
import com.tensegrity.palojava.DbConnection; /*    */
import com.tensegrity.palojava.RuleInfo; /*    */
import com.tensegrity.palojava.loader.RuleLoader;

/*    */
/*    */public class HttpRuleLoader extends RuleLoader
/*    */{
  /*    */public HttpRuleLoader(DbConnection paloConnection, CubeInfo cube)
  /*    */{
    /* 53 */super(paloConnection, cube);
    /*    */}

  /*    */
  /*    */public String[] getAllRuleIds() {
    /* 57 */if (!this.loaded) {
      /* 58 */reload();
      /* 59 */this.loaded = true;
      /*    */}
    /* 61 */return getLoadedIds();
    /*    */}

  /*    */
  /*    */protected final void reload() {
    /* 65 */reset();
    /* 66 */RuleInfo[] rules = this.paloConnection.getRules(this.cube);
    /* 67 */for (RuleInfo rule : rules)
      /* 68 */loaded(rule);
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.loader.HttpRuleLoader JD-Core
 * Version: 0.5.4
 */