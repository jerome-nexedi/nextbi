/*    */ package com.tensegrity.palojava.http.loader;
/*    */ 
/*    */ import com.tensegrity.palojava.DbConnection;
/*    */ import com.tensegrity.palojava.PaloInfo;
/*    */ import com.tensegrity.palojava.PropertyInfo;
/*    */ import com.tensegrity.palojava.loader.PropertyLoader;
/*    */ 
/*    */ public class HttpPropertyLoader extends PropertyLoader
/*    */ {
/*    */   private final PaloInfo paloObject;
/*    */ 
/*    */   public HttpPropertyLoader(DbConnection paloConnection)
/*    */   {
/* 43 */     super(paloConnection);
/* 44 */     this.paloObject = null;
/*    */   }
/*    */ 
/*    */   public HttpPropertyLoader(DbConnection paloConnection, PaloInfo paloObject) {
/* 48 */     super(paloConnection);
/* 49 */     this.paloObject = paloObject;
/*    */   }
/*    */ 
/*    */   public String[] getAllPropertyIds()
/*    */   {
/* 54 */     return new String[0];
/*    */   }
/*    */ 
/*    */   public PropertyInfo load(String id)
/*    */   {
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   protected void reload()
/*    */   {
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.loader.HttpPropertyLoader
 * JD-Core Version:    0.5.4
 */