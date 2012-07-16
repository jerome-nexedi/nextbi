/*    */ package com.tensegrity.palojava.http.builders;
/*    */ 
/*    */ public class BuilderUtils
/*    */ {
/*    */   static final String[] getIDs(String idStr)
/*    */   {
/* 48 */     String[] ids = idStr.split(",");
/* 49 */     if ((ids.length == 1) && (ids[0].equals("")))
/* 50 */       return new String[0];
/* 51 */     return ids;
/*    */   }
/*    */ 
/*    */   static final double[] getWeights(String weightStr) {
/* 55 */     String[] weights = weightStr.split(",");
/* 56 */     if ((weights.length == 1) && (weights[0].equals("")))
/* 57 */       return new double[0];
/* 58 */     double[] w = new double[weights.length];
/* 59 */     for (int i = 0; i < w.length; ++i) {
/* 60 */       w[i] = Double.parseDouble(weights[i]);
/*    */     }
/* 62 */     return w;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.builders.BuilderUtils
 * JD-Core Version:    0.5.4
 */