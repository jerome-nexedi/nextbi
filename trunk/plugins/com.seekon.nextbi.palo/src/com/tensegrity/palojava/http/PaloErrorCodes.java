/*    */ package com.tensegrity.palojava.http;
/*    */ 
/*    */ import com.tensegrity.palojava.PaloException;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.InputStreamReader;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ public final class PaloErrorCodes
/*    */ {
/* 56 */   private static final ArrayList codes = new ArrayList();
/*    */ 
/*    */   static {
/* 59 */     InputStream is = 
/* 60 */       PaloErrorCodes.class.getResourceAsStream("resources/errorCodes.txt");
/* 61 */     BufferedReader reader = new BufferedReader(new InputStreamReader(is));
/* 62 */     String line = null;
/*    */     try {
/* 64 */       while ((line = reader.readLine()) != null)
/*    */       {
/* 66 */         int start = line.indexOf('=') + 1;
/* 67 */         int end = line.indexOf(',');
/* 68 */         if (start < end) {
/* 69 */           String errCode = line.substring(start, end);
/* 70 */           codes.add(errCode.trim());
/*    */         }
/*    */       }
/*    */     } catch (IOException e) {
/* 74 */       throw new PaloException("Could not read in error codes!!", e);
/*    */     }
/*    */   }
/*    */ 
/*    */   static final boolean contains(String errorCode)
/*    */   {
/* 84 */     return codes.contains(errorCode);
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.PaloErrorCodes
 * JD-Core Version:    0.5.4
 */