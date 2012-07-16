/*     */ package com.tensegrity.palojava.http.builders;
/*     */ 
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import com.tensegrity.palojava.impl.DimensionInfoImpl;
/*     */ 
/*     */ public class DimensionInfoBuilder
/*     */ {
/*     */   public final DimensionInfo create(PaloInfo parent, String[] response)
/*     */   {
/*  60 */     if (response.length < 11)
/*  61 */       throw new PaloException("Not enough information to create DimensionInfo!!");
/*     */     try
/*     */     {
/*  64 */       String id = response[0];
/*  65 */       int type = Integer.parseInt(response[6]);
/*  66 */       DimensionInfoImpl info = 
/*  67 */         new DimensionInfoImpl((DatabaseInfo)parent, id, type);
/*  68 */       update(info, response);
/*     */ 
/*  89 */       return info;
/*     */     } catch (RuntimeException e) {
/*  91 */       throw new PaloException(e.getLocalizedMessage(), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void update(DimensionInfoImpl dimension, String[] response)
/*     */   {
/* 102 */     if (response.length < 11) {
/* 103 */       throw new PaloException("Not enough information to update DimensionInfo!!");
/*     */     }
/* 105 */     String name = response[1];
/* 106 */     int elCount = Integer.parseInt(response[2]);
/* 107 */     int maxLevel = Integer.parseInt(response[3]);
/* 108 */     int maxIndent = Integer.parseInt(response[4]);
/* 109 */     int maxDepth = Integer.parseInt(response[5]);
/* 110 */     String attrDimId = response[7];
/* 111 */     String attrCubeId = response[8];
/* 112 */     String rightsCubeId = response[9];
/* 113 */     int token = Integer.parseInt(response[10]);
/*     */ 
/* 115 */     dimension.setName(name);
/* 116 */     dimension.setElementCount(elCount);
/* 117 */     dimension.setMaxLevel(maxLevel);
/* 118 */     dimension.setMaxIndent(maxIndent);
/* 119 */     dimension.setMaxDepth(maxDepth);
/* 120 */     dimension.setAttributeDimension(attrDimId);
/* 121 */     dimension.setAttributeCube(attrCubeId);
/* 122 */     dimension.setRightsCube(rightsCubeId);
/* 123 */     dimension.setToken(token);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.builders.DimensionInfoBuilder
 * JD-Core Version:    0.5.4
 */