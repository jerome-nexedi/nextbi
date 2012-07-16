/*     */ package com.tensegrity.palojava.http.loader;
/*     */ 
/*     */ import com.tensegrity.palojava.CubeInfo;
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DbConnection;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.PaloInfo;
/*     */ import com.tensegrity.palojava.loader.CubeLoader;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class HttpCubeLoader extends CubeLoader
/*     */ {
/*     */   public HttpCubeLoader(DbConnection paloConnection, DatabaseInfo database)
/*     */   {
/*  57 */     super(paloConnection, database);
/*     */   }
/*     */ 
/*     */   public String[] getAllCubeIds() {
/*  61 */     if (!this.loaded) {
/*  62 */       reload();
/*  63 */       this.loaded = true;
/*     */     }
/*  65 */     return getLoadedIds();
/*     */   }
/*     */ 
/*     */   public String[] getCubeIds(int typeMask) {
/*  69 */     CubeInfo[] cubes = this.paloConnection.getCubes(this.database, typeMask);
/*  70 */     String[] ids = new String[cubes.length];
/*  71 */     int counter = 0;
/*  72 */     for (CubeInfo cube : cubes) {
/*  73 */       loaded(cube);
/*  74 */       ids[(counter++)] = cube.getId();
/*     */     }
/*  76 */     return ids;
/*     */   }
/*     */ 
/*     */   public CubeInfo loadByName(String name)
/*     */   {
/*  81 */     CubeInfo cube = findCube(name);
/*  82 */     if (cube == null)
/*     */     {
/*  84 */       reload();
/*  85 */       cube = findCube(name);
/*     */     }
/*  87 */     return cube;
/*     */   }
/*     */ 
/*     */   protected final void reload() {
/*  91 */     reset();
/*  92 */     CubeInfo[] cubes = this.paloConnection.getCubes(this.database);
/*  93 */     for (CubeInfo cube : cubes)
/*  94 */       loaded(cube);
/*     */   }
/*     */ 
/*     */   private final CubeInfo findCube(String name)
/*     */   {
/*  99 */     Collection<PaloInfo> infos = getLoaded();
/* 100 */     for (PaloInfo info : infos) {
/* 101 */       if (info instanceof CubeInfo) {
/* 102 */         CubeInfo cube = (CubeInfo)info;
/*     */ 
/* 104 */         if (cube.getName().equalsIgnoreCase(name))
/* 105 */           return cube;
/*     */       }
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public String[] getCubeIds(DimensionInfo dimension) {
/* 112 */     CubeInfo[] cubes = this.paloConnection.getCubes(dimension);
/* 113 */     String[] cubeIds = new String[cubes.length];
/* 114 */     int index = 0;
/* 115 */     for (CubeInfo cube : cubes) {
/* 116 */       loaded(cube);
/* 117 */       cubeIds[(index++)] = cube.getId();
/*     */     }
/* 119 */     return cubeIds;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.loader.HttpCubeLoader
 * JD-Core Version:    0.5.4
 */