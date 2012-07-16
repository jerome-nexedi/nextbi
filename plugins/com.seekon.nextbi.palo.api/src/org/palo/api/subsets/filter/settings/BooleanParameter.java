/*    */ package org.palo.api.subsets.filter.settings;
/*    */ 
/*    */ public class BooleanParameter extends AbstractParameter
/*    */ {
/*    */   private final String name;
/*    */   private Boolean value;
/*    */ 
/*    */   public BooleanParameter()
/*    */   {
/* 57 */     this(null);
/*    */   }
/*    */ 
/*    */   public BooleanParameter(String name)
/*    */   {
/* 66 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public final String getName() {
/* 70 */     return this.name;
/*    */   }
/*    */ 
/*    */   public final Boolean getValue() {
/* 74 */     return this.value;
/*    */   }
/*    */ 
/*    */   public final void setValue(boolean value)
/*    */   {
/* 82 */     this.value = ((value) ? Boolean.TRUE : Boolean.FALSE);
/* 83 */     markDirty();
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.BooleanParameter
 * JD-Core Version:    0.5.4
 */