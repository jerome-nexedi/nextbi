/*    */ package com.tensegrity.palojava.loader;
/*    */ 
/*    */ import com.tensegrity.palojava.DbConnection;
/*    */ import com.tensegrity.palojava.PropertyInfo;
/*    */ 
/*    */ public abstract class PropertyLoader extends PaloInfoLoader
/*    */ {
/*    */   public PropertyLoader(DbConnection paloConnection)
/*    */   {
/* 51 */     super(paloConnection);
/*    */   }
/*    */ 
/*    */   public abstract String[] getAllPropertyIds();
/*    */ 
/*    */   public abstract PropertyInfo load(String paramString);
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.loader.PropertyLoader
 * JD-Core Version:    0.5.4
 */