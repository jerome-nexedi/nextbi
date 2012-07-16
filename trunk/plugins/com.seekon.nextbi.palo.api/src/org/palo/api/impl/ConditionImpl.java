/*    */ package org.palo.api.impl;
/*    */ 
/*    */ import com.tensegrity.palojava.PaloException;
/*    */ import java.util.ArrayList;
/*    */ import org.palo.api.Condition;
/*    */ 
/*    */ class ConditionImpl
/*    */   implements Condition
/*    */ {
/* 52 */   private static final ArrayList<String> ALLOWED_CONDITIONS = new ArrayList(6);
/*    */   private String value;
/*    */   private final String condition;
/*    */ 
/*    */   static
/*    */   {
/* 54 */     ALLOWED_CONDITIONS.add("==");
/* 55 */     ALLOWED_CONDITIONS.add(">");
/* 56 */     ALLOWED_CONDITIONS.add(">=");
/* 57 */     ALLOWED_CONDITIONS.add("<");
/* 58 */     ALLOWED_CONDITIONS.add("<=");
/* 59 */     ALLOWED_CONDITIONS.add("!=");
/*    */   }
/*    */ 
/*    */   public static final Condition getCondition(String condition) {
/* 63 */     if (isValid(condition)) {
/* 64 */       return new ConditionImpl(condition);
/*    */     }
/* 66 */     throw new PaloException("Unkown condition: \"" + condition + "\"");
/*    */   }
/*    */ 
/*    */   private ConditionImpl(String condition)
/*    */   {
/* 73 */     this.condition = condition;
/*    */   }
/*    */ 
/*    */   public final synchronized String getValue() {
/* 77 */     return this.value;
/*    */   }
/*    */ 
/*    */   public final synchronized void setValue(double value) {
/* 81 */     setValue(Double.toString(value));
/*    */   }
/*    */ 
/*    */   public final void setValue(String value) {
/* 85 */     this.value = value;
/*    */   }
/*    */ 
/*    */   public final String toString() {
/* 89 */     StringBuffer str = new StringBuffer();
/* 90 */     str.append(this.condition);
/* 91 */     str.append(this.value);
/* 92 */     return str.toString();
/*    */   }
/*    */ 
/*    */   private static final boolean isValid(String condition) {
/* 96 */     return ALLOWED_CONDITIONS.contains(condition);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.ConditionImpl
 * JD-Core Version:    0.5.4
 */