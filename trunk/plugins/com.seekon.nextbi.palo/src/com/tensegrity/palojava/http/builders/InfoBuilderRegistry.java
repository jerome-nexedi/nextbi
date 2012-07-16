/*     */ package com.tensegrity.palojava.http.builders;
/*     */ 
/*     */ public class InfoBuilderRegistry
/*     */ {
/*  51 */   private static final InfoBuilderRegistry instance = new InfoBuilderRegistry();
/*     */ 
/*  59 */   private final CellInfoBuilder cellBuilder = new CellInfoBuilder();
/*  60 */   private final CubeInfoBuilder cubeBuilder = new CubeInfoBuilder();
/*  61 */   private final DatabaseInfoBuilder databaseBuilder = new DatabaseInfoBuilder();
/*  62 */   private final DimensionInfoBuilder dimensionBuilder = new DimensionInfoBuilder();
/*  63 */   private final ElementInfoBuilder elementBuilder = new ElementInfoBuilder();
/*  64 */   private final ServerInfoBuilder serverBuilder = new ServerInfoBuilder();
/*  65 */   private final RuleInfoBuilder ruleBuilder = new RuleInfoBuilder();
/*  66 */   private final LockInfoBuilder lockBuilder = new LockInfoBuilder();
/*     */ 
/*     */   public static final InfoBuilderRegistry getInstance()
/*     */   {
/*  53 */     return instance;
/*     */   }
/*     */ 
/*     */   public final CellInfoBuilder getCellBuilder()
/*     */   {
/*  72 */     return this.cellBuilder;
/*     */   }
/*     */ 
/*     */   public final CubeInfoBuilder getCubeBuilder() {
/*  76 */     return this.cubeBuilder;
/*     */   }
/*     */ 
/*     */   public final DatabaseInfoBuilder getDatabaseBuilder() {
/*  80 */     return this.databaseBuilder;
/*     */   }
/*     */ 
/*     */   public final DimensionInfoBuilder getDimensionBuilder() {
/*  84 */     return this.dimensionBuilder;
/*     */   }
/*     */ 
/*     */   public final ElementInfoBuilder getElementBuilder() {
/*  88 */     return this.elementBuilder;
/*     */   }
/*     */ 
/*     */   public final ServerInfoBuilder getServerBuilder() {
/*  92 */     return this.serverBuilder;
/*     */   }
/*     */ 
/*     */   public final RuleInfoBuilder getRuleBuilder() {
/*  96 */     return this.ruleBuilder;
/*     */   }
/*     */ 
/*     */   public final LockInfoBuilder getLockBuilder() {
/* 100 */     return this.lockBuilder;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.builders.InfoBuilderRegistry
 * JD-Core Version:    0.5.4
 */