/*    */ package com.tensegrity.palojava.http.builders;
/*    */ 
/*    */ import com.tensegrity.palojava.CubeInfo;
/*    */ import com.tensegrity.palojava.DatabaseInfo;
/*    */ import com.tensegrity.palojava.PaloException;
/*    */ import com.tensegrity.palojava.PaloInfo;
/*    */ import com.tensegrity.palojava.impl.CubeInfoImpl;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class CubeInfoBuilder
/*    */ {
/*    */   public final CubeInfo create(PaloInfo parent, String[] response)
/*    */   {
/* 49 */     if (response.length < 8) {
/* 50 */       throw new PaloException("CubeInfoBuilder: not enough information to create CubeInfo!!");
/*    */     }
/*    */ 
/* 54 */     String id = response[0];
/* 55 */     String[] dims = BuilderUtils.getIDs(response[3]);
/* 56 */     int type = Integer.parseInt(response[7]);
/* 57 */     CubeInfoImpl info = new CubeInfoImpl((DatabaseInfo)parent, id, type, dims);
/* 58 */     update(info, response);
/* 59 */     return info;
/*    */   }
/*    */ 
/*    */   public final void update(CubeInfoImpl cube, String[] response)
/*    */   {
/* 66 */     if (response.length < 8)
/* 67 */       throw new PaloException("Not enough information to update CubeInfo!!");
/*    */     try
/*    */     {
/* 70 */       String name = response[1];
/* 71 */       int dimCount = Integer.parseInt(response[2]);
/* 72 */       BigInteger cellCount = new BigInteger(response[4]);
/* 73 */       BigInteger filledCellCount = new BigInteger(response[5]);
/*    */ 
/* 76 */       int status = Integer.parseInt(response[6]);
/* 77 */       int token = Integer.parseInt(response[8]);
/*    */ 
/* 79 */       cube.setName(name);
/* 80 */       cube.setDimensionCount(dimCount);
/* 81 */       cube.setCellCount(cellCount);
/* 82 */       cube.setFilledCellCount(filledCellCount);
/* 83 */       cube.setStatus(status);
/* 84 */       cube.setToken(token);
/*    */     } catch (RuntimeException e) {
/* 86 */       throw new PaloException(e.getLocalizedMessage(), e);
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.builders.CubeInfoBuilder
 * JD-Core Version:    0.5.4
 */