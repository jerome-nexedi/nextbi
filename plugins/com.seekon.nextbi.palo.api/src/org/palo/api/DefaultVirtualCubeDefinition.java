/*    */ package org.palo.api;
/*    */ 
/*    */ /** @deprecated */
/*    */ public class DefaultVirtualCubeDefinition
/*    */   implements VirtualCubeDefinition
/*    */ {
/*    */   private final Cube sourceCube;
/*    */   private final String name;
/*    */   private final VirtualDimensionDefinition[] virtualDimensionDefinitions;
/*    */ 
/*    */   public DefaultVirtualCubeDefinition(String name, Cube sourceCube, VirtualDimensionDefinition[] virtualDimensionDefinitions)
/*    */   {
/* 60 */     this.name = name;
/* 61 */     this.sourceCube = sourceCube;
/* 62 */     this.virtualDimensionDefinitions = virtualDimensionDefinitions;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 66 */     return this.name;
/*    */   }
/*    */ 
/*    */   public Cube getSourceCube() {
/* 70 */     return this.sourceCube;
/*    */   }
/*    */ 
/*    */   public VirtualDimensionDefinition[] getVirtualDimensionDefinitions() {
/* 74 */     return this.virtualDimensionDefinitions;
/*    */   }
/*    */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.DefaultVirtualCubeDefinition
 * JD-Core Version:    0.5.4
 */