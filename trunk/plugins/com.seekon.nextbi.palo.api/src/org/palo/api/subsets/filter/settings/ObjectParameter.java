/*    */ package org.palo.api.subsets.filter.settings;
/*    */ 
/*    */ public class ObjectParameter extends AbstractParameter
/*    */ {
/*    */   private String name;
/*    */   private Object value;
/*    */ 
/*    */   public ObjectParameter()
/*    */   {
/* 57 */     this(null);
/*    */   }
/*    */ 
/*    */   public ObjectParameter(String name)
/*    */   {
/* 65 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public final String getName() {
/* 69 */     return this.name;
/*    */   }
/*    */ 
/*    */   public final Object getValue() {
/* 73 */     return this.value;
/*    */   }
/*    */ 
/*    */   public final void setName(String name)
/*    */   {
/* 81 */     this.name = name;
/*    */   }
/*    */ 
/*    */   public final void setValue(Object value)
/*    */   {
/* 89 */     this.value = value;
/* 90 */     markDirty();
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.subsets.filter.settings.ObjectParameter
 * JD-Core Version:    0.5.4
 */