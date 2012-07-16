/*    */ package org.palo.api.impl.utils;
/*    */ 
/*    */ public class ListModifiedException extends RuntimeException
/*    */ {
/*    */   public ListModifiedException()
/*    */   {
/* 57 */     super("array modified concurrently while iterating over contents");
/*    */   }
/*    */ 
/*    */   public ListModifiedException(String what)
/*    */   {
/* 66 */     super(what);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.utils.ListModifiedException
 * JD-Core Version:    0.5.4
 */