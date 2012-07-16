/*    */ package org.palo.api.exceptions;
/*    */ 
/*    */ public class PaloIOException extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 3563657088381734321L;
/*    */   private Object data;
/*    */ 
/*    */   public PaloIOException(String message)
/*    */   {
/* 58 */     super(message);
/*    */   }
/*    */ 
/*    */   public PaloIOException(String message, Throwable cause)
/*    */   {
/* 68 */     super(message, cause);
/*    */   }
/*    */ 
/*    */   public PaloIOException(Throwable cause)
/*    */   {
/* 77 */     super(cause);
/*    */   }
/*    */ 
/*    */   public final void setData(Object data) {
/* 81 */     this.data = data;
/*    */   }
/*    */ 
/*    */   public final Object getData() {
/* 85 */     return this.data;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.exceptions.PaloIOException
 * JD-Core Version:    0.5.4
 */