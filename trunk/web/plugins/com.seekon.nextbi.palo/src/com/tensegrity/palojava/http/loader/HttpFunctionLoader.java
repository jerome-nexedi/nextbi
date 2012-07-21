/*    */package com.tensegrity.palojava.http.loader;

/*    */
/*    */import com.tensegrity.palojava.DbConnection; /*    */
import com.tensegrity.palojava.loader.FunctionLoader; /*    */
import com.tensegrity.palojava.loader.RuleLoader;

/*    */
/*    */public class HttpFunctionLoader extends FunctionLoader
/*    */{
  /*    */public HttpFunctionLoader(DbConnection paloConnection)
  /*    */{
    /* 52 */super(paloConnection);
    /*    */}

  /*    */
  /*    */public String loadAll() {
    /* 56 */if (RuleLoader.supportsRules(this.paloConnection))
      /* 57 */return this.paloConnection.listFunctions();
    /* 58 */return "";
    /*    */}

  /*    */
  /*    */protected final void reload()
  /*    */{
    /*    */}
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.http.loader.HttpFunctionLoader
 * JD-Core Version: 0.5.4
 */