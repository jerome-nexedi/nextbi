/*    */ package org.palo.api.ext.ui.table.impl;
/*    */ 
/*    */ import org.palo.api.ext.ui.Format;
/*    */ import org.palo.api.ext.ui.table.TableFormat;
/*    */ 
/*    */ class DefaultTableFormat
/*    */   implements TableFormat
/*    */ {
/*    */   private Format cellFormat;
/*    */   private Format headerFormat;
/*    */ 
/*    */   final void setCellFormat(Format cellFormat)
/*    */   {
/* 58 */     this.cellFormat = cellFormat;
/*    */   }
/*    */ 
/*    */   final void setHeaderFormat(Format headerFormat)
/*    */   {
/* 63 */     this.headerFormat = headerFormat;
/*    */   }
/*    */ 
/*    */   public Format getCellFormat()
/*    */   {
/* 73 */     return this.cellFormat;
/*    */   }
/*    */ 
/*    */   public Format getHeaderFormat() {
/* 77 */     return this.headerFormat;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.ui.table.impl.DefaultTableFormat
 * JD-Core Version:    0.5.4
 */