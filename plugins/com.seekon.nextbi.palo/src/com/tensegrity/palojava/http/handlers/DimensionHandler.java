/*     */ package com.tensegrity.palojava.http.handlers;
/*     */ 
/*     */ import com.tensegrity.palojava.CubeInfo;
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.http.HttpConnection;
/*     */ import com.tensegrity.palojava.http.builders.CubeInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.DimensionInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.ElementInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.InfoBuilderRegistry;
/*     */ import com.tensegrity.palojava.impl.DimensionInfoImpl;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class DimensionHandler extends HttpHandler
/*     */ {
/*     */   private static final String CLEAR_PREFIX = "/dimension/clear?database=";
/*     */   private static final String CREATE_PREFIX = "/dimension/create?database=";
/*     */   private static final String CUBES_PREFIX = "/dimension/cubes?database=";
/*     */   private static final String DELETE_PREFIX = "/dimension/destroy?database=";
/*     */   private static final String ELEMENT_PREFIX = "/dimension/element?database=";
/*     */   private static final String ELEMENTS_PREFIX = "/dimension/elements?database=";
/*     */   private static final String INFO_PREFIX = "/dimension/info?database=";
/*     */   private static final String RENAME_PREFIX = "/dimension/rename?database=";
/*  86 */   private static final DimensionHandler instance = new DimensionHandler();
/*     */   private final InfoBuilderRegistry builderReg;
/*     */ 
/*     */   static final DimensionHandler getInstance(HttpConnection connection)
/*     */   {
/*  88 */     instance.use(connection);
/*  89 */     return instance;
/*     */   }
/*     */ 
/*     */   private DimensionHandler()
/*     */   {
/*  97 */     this.builderReg = InfoBuilderRegistry.getInstance();
/*     */   }
/*     */ 
/*     */   public final DimensionInfo clear(DimensionInfo dimension) throws IOException
/*     */   {
/* 102 */     DatabaseInfo database = dimension.getDatabase();
/* 103 */     StringBuffer query = new StringBuffer();
/* 104 */     query.append("/dimension/clear?database=");
/* 105 */     query.append(database.getId());
/* 106 */     query.append("&dimension=");
/* 107 */     query.append(dimension.getId());
/* 108 */     String[][] response = request(query.toString());
/* 109 */     if (response[0].length < 11) {
/* 110 */       String[] _response = new String[11];
/* 111 */       System.arraycopy(response, 0, _response, 0, 7);
/* 112 */       _response[7] = dimension.getAttributeDimension();
/* 113 */       _response[8] = dimension.getAttributeCube();
/* 114 */       _response[9] = dimension.getRightsCube();
/* 115 */       _response[10] = Integer.toString(dimension.getToken());
/* 116 */       response[0] = _response;
/*     */     }
/* 118 */     DimensionInfoBuilder dimensionBuilder = 
/* 119 */       this.builderReg.getDimensionBuilder();
/* 120 */     return dimensionBuilder.create(database, response[0]);
/*     */   }
/*     */ 
/*     */   public final DimensionInfo create(DatabaseInfo database, String name)
/*     */     throws IOException
/*     */   {
/* 126 */     StringBuffer query = new StringBuffer();
/* 127 */     query.append("/dimension/create?database=");
/* 128 */     query.append(database.getId());
/* 129 */     query.append("&new_name=");
/* 130 */     query.append(encode(name));
/* 131 */     String[][] response = request(query.toString());
/* 132 */     DimensionInfoBuilder dimensionBuilder = 
/* 133 */       this.builderReg.getDimensionBuilder();
/* 134 */     return dimensionBuilder.create(database, response[0]);
/*     */   }
/*     */ 
/*     */   public final DimensionInfo create(DatabaseInfo database, String name, int type)
/*     */     throws IOException
/*     */   {
/* 140 */     StringBuffer query = new StringBuffer();
/* 141 */     query.append("/dimension/create?database=");
/* 142 */     query.append(database.getId());
/* 143 */     query.append("&new_name=");
/* 144 */     query.append(encode(name));
/* 145 */     query.append("&type=");
/* 146 */     query.append(type);
/* 147 */     String[][] response = request(query.toString());
/* 148 */     DimensionInfoBuilder dimensionBuilder = this.builderReg
/* 149 */       .getDimensionBuilder();
/* 150 */     return dimensionBuilder.create(database, response[0]);
/*     */   }
/*     */ 
/*     */   public final CubeInfo[] getCubes(DimensionInfo dimension) throws IOException
/*     */   {
/* 155 */     DatabaseInfo database = dimension.getDatabase();
/* 156 */     StringBuffer query = new StringBuffer();
/* 157 */     query.append("/dimension/cubes?database=");
/* 158 */     query.append(database.getId());
/* 159 */     query.append("&dimension=");
/* 160 */     query.append(dimension.getId());
/* 161 */     String[][] response = request(query.toString());
/* 162 */     CubeInfo[] cubes = new CubeInfo[response.length];
/* 163 */     CubeInfoBuilder cubeBuilder = this.builderReg.getCubeBuilder();
/* 164 */     for (int i = 0; i < cubes.length; ++i) {
/* 165 */       cubes[i] = cubeBuilder.create(database, response[i]);
/*     */     }
/* 167 */     return cubes;
/*     */   }
/*     */ 
/*     */   public final boolean delete(DimensionInfo dimension)
/*     */     throws IOException
/*     */   {
/* 205 */     DatabaseInfo database = dimension.getDatabase();
/* 206 */     StringBuffer req = new StringBuffer();
/* 207 */     req.append("/dimension/destroy?database=");
/* 208 */     req.append(database.getId());
/* 209 */     req.append("&dimension=");
/* 210 */     req.append(dimension.getId());
/* 211 */     String[][] response = request(req.toString());
/* 212 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final ElementInfo getElementAt(DimensionInfo dimension, int position)
/*     */     throws IOException
/*     */   {
/* 218 */     DatabaseInfo database = dimension.getDatabase();
/* 219 */     StringBuffer query = new StringBuffer();
/* 220 */     query.append("/dimension/element?database=");
/* 221 */     query.append(database.getId());
/* 222 */     query.append("&dimension=");
/* 223 */     query.append(dimension.getId());
/* 224 */     query.append("&position=");
/* 225 */     query.append(position);
/* 226 */     String[][] response = request(query.toString());
/* 227 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 228 */     return elementBuilder.create(dimension, response[0]);
/*     */   }
/*     */ 
/*     */   public final ElementInfo[] getElements(DimensionInfo dimension)
/*     */     throws IOException
/*     */   {
/* 234 */     DatabaseInfo database = dimension.getDatabase();
/* 235 */     StringBuffer query = new StringBuffer();
/* 236 */     query.append("/dimension/elements?database=");
/* 237 */     query.append(database.getId());
/* 238 */     query.append("&dimension=");
/* 239 */     query.append(dimension.getId());
/* 240 */     String[][] response = request(query.toString());
/* 241 */     ElementInfo[] elements = new ElementInfo[response.length];
/* 242 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 243 */     for (int i = 0; i < elements.length; ++i) {
/* 244 */       elements[i] = elementBuilder.create(dimension, response[i]);
/*     */     }
/*     */ 
/* 247 */     return elements;
/*     */   }
/*     */ 
/*     */   public final DimensionInfo getInfo(DatabaseInfo database, String id)
/*     */     throws IOException
/*     */   {
/* 253 */     StringBuffer query = new StringBuffer();
/* 254 */     query.append("/dimension/info?database=");
/* 255 */     query.append(database.getId());
/* 256 */     query.append("&dimension=");
/* 257 */     query.append(id);
/* 258 */     String[][] response = request(query.toString());
/* 259 */     DimensionInfoBuilder dimensionBuilder = 
/* 260 */       this.builderReg.getDimensionBuilder();
/* 261 */     return dimensionBuilder.create(database, response[0]);
/*     */   }
/*     */ 
/*     */   public final void rename(DimensionInfo dimension, String newName)
/*     */     throws IOException
/*     */   {
/* 267 */     DatabaseInfo database = dimension.getDatabase();
/* 268 */     StringBuffer query = new StringBuffer();
/* 269 */     query.append("/dimension/rename?database=");
/* 270 */     query.append(database.getId());
/* 271 */     query.append("&dimension=");
/* 272 */     query.append(dimension.getId());
/* 273 */     query.append("&new_name=");
/* 274 */     query.append(encode(newName));
/* 275 */     String[][] response = request(query.toString());
/* 276 */     DimensionInfoBuilder dimensionBuilder = 
/* 277 */       this.builderReg.getDimensionBuilder();
/* 278 */     dimensionBuilder.update((DimensionInfoImpl)dimension, response[0]);
/*     */   }
/*     */ 
/*     */   public final DimensionInfo reload(DimensionInfo dimension) throws IOException
/*     */   {
/* 283 */     DatabaseInfo database = dimension.getDatabase();
/* 284 */     StringBuffer query = new StringBuffer();
/* 285 */     query.append("/dimension/info?database=");
/* 286 */     query.append(database.getId());
/* 287 */     query.append("&dimension=");
/* 288 */     query.append(dimension.getId());
/* 289 */     String[][] response = request(query.toString());
/* 290 */     DimensionInfoBuilder dimensionBuilder = this.builderReg
/* 291 */       .getDimensionBuilder();
/* 292 */     dimensionBuilder.update((DimensionInfoImpl)dimension, response[0]);
/* 293 */     return dimension;
/*     */   }
/*     */ 
/*     */   public final DimensionInfo getAttributeDimension(DimensionInfo dimension) throws IOException
/*     */   {
/* 298 */     DatabaseInfo database = dimension.getDatabase();
/* 299 */     StringBuffer query = new StringBuffer();
/* 300 */     query.append("/dimension/info?database=");
/* 301 */     query.append(database.getId());
/* 302 */     query.append("&dimension=");
/* 303 */     query.append(dimension.getAttributeDimension());
/* 304 */     String[][] response = request(query.toString());
/* 305 */     DimensionInfoBuilder dimensionBuilder = this.builderReg
/* 306 */       .getDimensionBuilder();
/* 307 */     return dimensionBuilder.create(database, response[0]);
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.DimensionHandler
 * JD-Core Version:    0.5.4
 */