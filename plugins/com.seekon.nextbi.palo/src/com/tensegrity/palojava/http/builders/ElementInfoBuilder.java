/*     */ package com.tensegrity.palojava.http.builders;
/*     */ 
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import com.tensegrity.palojava.impl.ElementInfoImpl;
/*     */ 
/*     */ public class ElementInfoBuilder
/*     */ {
/*     */   public final ElementInfo create(PaloInfo parent, String[] response)
/*     */   {
/*  48 */     if (response.length < 12)
/*  49 */       throw new PaloException(
/*  50 */         getExceptionMessage("Not enough information to create ElementInfo", response));
/*     */     try
/*     */     {
/*  53 */       String id = response[0];
/*     */ 
/*  55 */       ElementInfoImpl info = 
/*  56 */         new ElementInfoImpl((DimensionInfo)parent, id);
/*  57 */       update(info, response);
/*  58 */       return info;
/*     */     } catch (RuntimeException e) {
/*  60 */       throw new PaloException(e.getLocalizedMessage(), e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void update(ElementInfoImpl element, String[] response) {
/*  65 */     if (response.length < 12) {
/*  66 */       throw new PaloException(
/*  67 */         getExceptionMessage("Not enough information to update ElementInfo", response));
/*     */     }
/*     */ 
/*  70 */     String name = response[1];
/*  71 */     int position = Integer.parseInt(response[2]);
/*  72 */     int level = Integer.parseInt(response[3]);
/*  73 */     int indent = Integer.parseInt(response[4]);
/*  74 */     int depth = Integer.parseInt(response[5]);
/*  75 */     int type = Integer.parseInt(response[6]);
/*     */ 
/*  77 */     String[] parentIds = BuilderUtils.getIDs(response[8]);
/*  78 */     int childrenCount = Integer.parseInt(response[9]);
/*     */     double[] weights;
/*     */     String[] childrenIds;

/*  81 */     if (childrenCount == 0) {
/*  82 */       childrenIds = new String[0];
/*  83 */       weights = new double[0];
/*     */     } else {
/*  85 */       childrenIds = BuilderUtils.getIDs(response[10]);
/*  86 */       weights = BuilderUtils.getWeights(response[11]);
/*     */     }
/*  88 */     element.setName(name);
/*  89 */     element.setType(type);
/*  90 */     element.setPosition(position);
/*  91 */     element.setLevel(level);
/*  92 */     element.setIndent(indent);
/*  93 */     element.setDepth(depth);
/*     */ 
/*  95 */     element.setParents(parentIds);
/*     */ 
/*  97 */     element.setChildren(childrenIds, weights);
/*     */   }
/*     */ 
/*     */   private final String getExceptionMessage(String message, String[] response) {
/* 101 */     StringBuffer msg = new StringBuffer();
/* 102 */     msg.append(message);
/* 103 */     if (response.length >= 2) {
/* 104 */       msg.append(" '");
/* 105 */       msg.append(response[1]);
/* 106 */       msg.append("' (id: ");
/* 107 */       msg.append(response[0]);
/* 108 */       msg.append(")");
/*     */     }
/* 110 */     msg.append("!!");
/* 111 */     return msg.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.builders.ElementInfoBuilder
 * JD-Core Version:    0.5.4
 */