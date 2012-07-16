/*    */ package org.palo.api.exceptions;
/*    */ 
/*    */ import org.palo.api.PaloAPIException;
/*    */ 
/*    */ public class InsufficientRightsException extends PaloAPIException
/*    */ {
/*    */   public InsufficientRightsException(String message)
/*    */   {
/* 56 */     super(message);
/*    */   }
/*    */ 
/*    */   public InsufficientRightsException(String message, Throwable cause)
/*    */   {
/* 66 */     super(message, cause);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.exceptions.InsufficientRightsException
 * JD-Core Version:    0.5.4
 */