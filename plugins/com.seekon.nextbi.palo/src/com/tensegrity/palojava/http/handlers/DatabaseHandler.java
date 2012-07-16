/*     */ package com.tensegrity.palojava.http.handlers;
/*     */ 
/*     */ import com.tensegrity.palojava.CubeInfo;
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.http.HttpConnection;
/*     */ import com.tensegrity.palojava.http.builders.CubeInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.DatabaseInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.DimensionInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.InfoBuilderRegistry;
/*     */ import com.tensegrity.palojava.impl.DatabaseInfoImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.ConnectException;
/*     */ 
/*     */ public class DatabaseHandler extends HttpHandler
/*     */ {
/*     */   private static final String CREATE_PREFIX = "/database/create?new_name=";
/*     */   private static final String DELETE_PREFIX = "/database/destroy?database=";
/*     */   private static final String RENAME_PREFIX = "/database/rename?database=";
/*     */   private static final String DIMENSIONS_PREFIX = "/database/dimensions?database=";
/*     */   private static final String CUBES_PREFIX = "/database/cubes?database=";
/*     */   private static final String INFO_PREFIX = "/database/info?database=";
/*     */   private static final String LOAD_PREFIX = "/database/load?database=";
/*     */   private static final String UNLOAD_PREFIX = "/database/unload?database=";
/*     */   private static final String SAVE_PREFIX = "/database/save?database=";
/*     */   private static final String SHOW_NORMAL = "&show_normal=1";
/*     */   private static final String SHOW_SYSTEM = "&show_system=1";
/*     */   private static final String SHOW_USER_INFO = "&show_info=1";
/*     */   private static final String SHOW_ATTRIBUTE = "&show_attribute=1";
/*     */   private static final String HIDE_NORMAL = "&show_normal=0";
/*     */   private static final String HIDE_SYSTEM = "&show_system=0";
/*     */   private static final String HIDE_USER_INFO = "&show_info=0";
/*     */   private static final String HIDE_ATTRIBUTE = "&show_attribute=0";
/*  90 */   private static final DatabaseHandler instance = new DatabaseHandler();
/*     */   private final InfoBuilderRegistry builderReg;
/*     */ 
/*     */   static final DatabaseHandler getInstance(HttpConnection connection)
/*     */   {
/*  92 */     instance.use(connection);
/*  93 */     return instance;
/*     */   }
/*     */ 
/*     */   private DatabaseHandler()
/*     */   {
/* 101 */     this.builderReg = InfoBuilderRegistry.getInstance();
/*     */   }
/*     */ 
/*     */   public final CubeInfo[] getAllCubes(DatabaseInfo database) throws IOException
/*     */   {
/* 106 */     return getCubesInternal(database, true, -1);
/*     */   }
/*     */ 
/*     */   public final CubeInfo[] getCubes(DatabaseInfo database, int typeMask) throws IOException {
/* 110 */     return getCubesInternal(database, false, typeMask);
/*     */   }
/*     */ 
/*     */   public final DatabaseInfo create(String name, int type)
/*     */     throws ConnectException, IOException
/*     */   {
/* 138 */     StringBuffer query = new StringBuffer();
/* 139 */     query.append("/database/create?new_name=");
/* 140 */     query.append(encode(name));
/* 141 */     query.append("&type=");
/* 142 */     query.append(type);
/* 143 */     String[][] response = request(query.toString());
/* 144 */     DatabaseInfoBuilder databaseBuilder = this.builderReg.getDatabaseBuilder();
/* 145 */     return databaseBuilder.create(null, response[0]);
/*     */   }
/*     */ 
/*     */   public final boolean destroy(DatabaseInfo database)
/*     */     throws IOException
/*     */   {
/* 151 */     StringBuffer query = new StringBuffer();
/* 152 */     query.append("/database/destroy?database=");
/* 153 */     query.append(database.getId());
/* 154 */     String[][] response = request(query.toString());
/* 155 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final DimensionInfo[] getAllDimensions(DatabaseInfo database)
/*     */     throws IOException
/*     */   {
/* 161 */     return getDimensionsInternal(database, true, -1);
/*     */   }
/*     */ 
/*     */   public final DimensionInfo[] getDimensions(DatabaseInfo database, int typeMask) throws IOException
/*     */   {
/* 166 */     return getDimensionsInternal(database, false, typeMask);
/*     */   }
/*     */ 
/*     */   public final DatabaseInfo getInfo(String id)
/*     */     throws IOException
/*     */   {
/* 190 */     StringBuffer query = new StringBuffer();
/* 191 */     query.append("/database/info?database=");
/* 192 */     query.append(id);
/* 193 */     String[][] response = request(query.toString());
/* 194 */     DatabaseInfoBuilder databaseBuilder = this.builderReg.getDatabaseBuilder();
/* 195 */     return databaseBuilder.create(null, response[0]);
/*     */   }
/*     */ 
/*     */   public final boolean load(DatabaseInfo database) throws IOException
/*     */   {
/* 200 */     StringBuffer query = new StringBuffer();
/* 201 */     query.append("/database/load?database=");
/* 202 */     query.append(database.getId());
/* 203 */     String[][] response = request(query.toString());
/* 204 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final DatabaseInfo reload(DatabaseInfo database) throws IOException {
/* 208 */     StringBuffer query = new StringBuffer();
/* 209 */     query.append("/database/info?database=");
/* 210 */     query.append(database.getId());
/* 211 */     String[][] response = request(query.toString());
/* 212 */     DatabaseInfoBuilder databaseBuilder = this.builderReg.getDatabaseBuilder();
/* 213 */     databaseBuilder.update((DatabaseInfoImpl)database, response[0]);
/* 214 */     return database;
/*     */   }
/*     */ 
/*     */   public final void rename(DatabaseInfo database, String newName)
/*     */     throws IOException
/*     */   {
/* 220 */     StringBuffer query = new StringBuffer();
/* 221 */     query.append("/database/rename?database=");
/* 222 */     query.append(database.getId());
/* 223 */     query.append("&new_name=");
/* 224 */     query.append(newName);
/* 225 */     String[][] response = request(query.toString());
/* 226 */     if (response[0].length == 6) {
/* 227 */       String[] _response = new String[7];
/* 228 */       _response[6] = Integer.toString(database.getToken());
/* 229 */       System.arraycopy(response, 0, _response, 0, response.length);
/* 230 */       response[0] = _response;
/*     */     }
/* 232 */     DatabaseInfoBuilder databaseBuilder = this.builderReg.getDatabaseBuilder();
/* 233 */     databaseBuilder.update((DatabaseInfoImpl)database, response[0]);
/*     */   }
/*     */ 
/*     */   public final boolean save(DatabaseInfo database) throws IOException
/*     */   {
/* 238 */     StringBuffer query = new StringBuffer();
/* 239 */     query.append("/database/save?database=");
/* 240 */     query.append(database.getId());
/* 241 */     String[][] response = request(query.toString());
/* 242 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final boolean unload(DatabaseInfo database) throws IOException
/*     */   {
/* 247 */     StringBuffer query = new StringBuffer();
/* 248 */     query.append("/database/unload?database=");
/* 249 */     query.append(database.getId());
/* 250 */     String[][] response = request(query.toString());
/* 251 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   private final CubeInfo[] getCubesInternal(DatabaseInfo database, boolean getAll, int typeMask)
/*     */     throws IOException
/*     */   {
/* 260 */     StringBuffer query = new StringBuffer();
/* 261 */     query.append("/database/cubes?database=");
/* 262 */     query.append(database.getId());
/* 263 */     if (!getAll) {
/* 264 */       query.append(getTypeString(typeMask));
/*     */     } else {
/* 266 */       query.append("&show_system=1");
/* 267 */       query.append("&show_normal=1");
/* 268 */       query.append("&show_attribute=1");
/* 269 */       query.append("&show_info=1");
/*     */     }
/* 271 */     String[][] response = request(query.toString());
/* 272 */     CubeInfo[] cubes = new CubeInfo[response.length];
/* 273 */     CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
/* 274 */     for (int i = 0; i < cubes.length; ++i) {
/* 275 */       cubes[i] = cubeBuilder.create(database, response[i]);
/*     */     }
/* 277 */     return cubes;
/*     */   }
/*     */ 
/*     */   private final DimensionInfo[] getDimensionsInternal(DatabaseInfo database, boolean getAll, int typeMask) throws IOException
/*     */   {
/* 282 */     if (database.getStatus() == 0) {
/* 283 */       System.err.println("WARNING: database '" + database.getName() + "' not loaded!!");
/*     */     }
/*     */ 
/* 287 */     StringBuffer query = new StringBuffer();
/* 288 */     query.append("/database/dimensions?database=");
/* 289 */     query.append(database.getId());
/* 290 */     if (!getAll) {
/* 291 */       query.append(getTypeString(typeMask));
/*     */     } else {
/* 293 */       query.append("&show_system=1");
/* 294 */       query.append("&show_normal=1");
/* 295 */       query.append("&show_attribute=1");
/* 296 */       query.append("&show_info=1");
/*     */     }
/* 298 */     String[][] response = request(query.toString());
/* 299 */     DimensionInfo[] dimensions = new DimensionInfo[response.length];
/* 300 */     DimensionInfoBuilder dimensionBuilder = 
/* 301 */       this.builderReg.getDimensionBuilder();
/* 302 */     for (int i = 0; i < response.length; ++i) {
/* 303 */       dimensions[i] = dimensionBuilder.create(database, response[i]);
/*     */     }
/*     */ 
/* 306 */     return dimensions;
/*     */   }
/*     */ 
/*     */   private final String getTypeString(int typeMask) {
/* 310 */     StringBuffer showTypes = new StringBuffer();
/*     */ 
/* 312 */     showTypes.append((hasType(8, typeMask)) ? "&show_attribute=1" : "&show_attribute=0");
/* 313 */     showTypes.append((hasType(16, typeMask)) ? "&show_info=1" : "&show_info=0");
/* 314 */     showTypes.append((hasType(2, typeMask)) ? "&show_normal=1" : "&show_normal=0");
/* 315 */     showTypes.append((hasType(4, typeMask)) ? "&show_system=1" : "&show_system=0");
/* 316 */     return showTypes.toString();
/*     */   }
/*     */ 
/*     */   private final boolean hasType(int type, int typeMask) {
/* 320 */     return (typeMask & type) > 0;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.DatabaseHandler
 * JD-Core Version:    0.5.4
 */