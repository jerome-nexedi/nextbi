/*    */ package org.palo.api.ext.ui.table.impl;
/*    */ 
/*    */ import org.palo.api.ext.ui.table.TableFormat;
/*    */ 
/*    */ public class TableFormatPersistence
/*    */ {
/* 50 */   private static final TableFormatPersistence instance = new TableFormatPersistence();
/*    */ 
/* 52 */   public static final TableFormatPersistence getInstance() { return instance; }
/*    */ 
/*    */ 
/*    */   public TableFormat read(String xmlStr)
/*    */   {
/* 59 */     return TableFormatReader.getInstance().fromXML(xmlStr);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.ui.table.impl.TableFormatPersistence
 * JD-Core Version:    0.5.4
 */