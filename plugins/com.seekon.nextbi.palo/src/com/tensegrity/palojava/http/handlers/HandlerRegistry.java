/*    */ package com.tensegrity.palojava.http.handlers;
/*    */ 
/*    */ import com.tensegrity.palojava.http.HttpConnection;
/*    */ 
/*    */ public class HandlerRegistry
/*    */ {
/*    */   private final HttpConnection connection;
/*    */ 
/*    */   public HandlerRegistry(HttpConnection connection)
/*    */   {
/* 53 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   public final ServerHandler getServerHandler() {
/* 57 */     return ServerHandler.getInstance(this.connection);
/*    */   }
/*    */ 
/*    */   public final CellHandler getCellHandler() {
/* 61 */     return CellHandler.getInstance(this.connection);
/*    */   }
/*    */ 
/*    */   public final CubeHandler getCubeHandler() {
/* 65 */     return CubeHandler.getInstance(this.connection);
/*    */   }
/*    */ 
/*    */   public final DatabaseHandler getDatabaseHandler() {
/* 69 */     return DatabaseHandler.getInstance(this.connection);
/*    */   }
/*    */ 
/*    */   public final DimensionHandler getDimensionHandler() {
/* 73 */     return DimensionHandler.getInstance(this.connection);
/*    */   }
/*    */ 
/*    */   public final ElementHandler getElementHandler() {
/* 77 */     return ElementHandler.getInstance(this.connection);
/*    */   }
/*    */ 
/*    */   public final RuleHandler getRuleHandler() {
/* 81 */     return RuleHandler.getInstance(this.connection);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.HandlerRegistry
 * JD-Core Version:    0.5.4
 */