/*    */package com.tensegrity.palojava.loader;

/*    */
/*    */import com.tensegrity.palojava.DbConnection;

/*    */
/*    */public abstract class FunctionLoader extends PaloInfoLoader
/*    */{
  /*    */public FunctionLoader(DbConnection paloConnection)
  /*    */{
    /* 54 */super(paloConnection);
    /*    */}

  /*    */
  /*    */public abstract String loadAll();
  /*    */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.loader.FunctionLoader JD-Core
 * Version: 0.5.4
 */