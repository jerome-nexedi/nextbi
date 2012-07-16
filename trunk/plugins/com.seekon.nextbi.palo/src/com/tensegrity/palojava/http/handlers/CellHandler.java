/*     */ package com.tensegrity.palojava.http.handlers;
/*     */ 
/*     */ import com.tensegrity.palojava.CellInfo;
/*     */ import com.tensegrity.palojava.CubeInfo;
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.ExportContextInfo;
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import com.tensegrity.palojava.http.HttpConnection;
/*     */ import com.tensegrity.palojava.http.builders.CellInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.InfoBuilderRegistry;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class CellHandler extends HttpHandler
/*     */ {
/*     */   private static final String AREA_PREFIX = "/cell/area?database=";
/*     */   private static final String EXPORT_PREFIX = "/cell/export?database=";
/*     */   private static final String REPLACE_PREFIX = "/cell/replace?database=";
/*     */   private static final String REPLACE_BULK_PREFIX = "/cell/replace_bulk?database=";
/*     */   private static final String VALUE_PREFIX = "/cell/value?database=";
/*     */   private static final String VALUES_PREFIX = "/cell/values?database=";
/*     */   private static final String BLOCKSIZE_PREFIX = "&blocksize=";
/*     */   private static final String TYPE_PREFIX = "&type=";
/*     */   private static final String CONDITION_PREFIX = "&condition=";
/*     */   private static final String BASE_ONLY_PREFIX = "&base_only=";
/*     */   private static final String SKIP_EMPTY_PREFIX = "&skip_empty=";
/*     */   private static final String USE_RULES_PREFIX = "&use_rules=";
/*     */   private static final String SHOW_RULE = "&show_rule=1";
/*  89 */   private static final CellHandler instance = new CellHandler();
/*     */   private final InfoBuilderRegistry builderReg;
/*     */ 
/*     */   static final CellHandler getInstance(HttpConnection connection)
/*     */   {
/*  91 */     instance.use(connection);
/*  92 */     return instance;
/*     */   }
/*     */ 
/*     */   private CellHandler()
/*     */   {
/* 100 */     this.builderReg = InfoBuilderRegistry.getInstance();
/*     */   }
/*     */ 
/*     */   public final CellInfo[] getCellArea(CubeInfo cube, ElementInfo[][] coordinates)
/*     */     throws IOException
/*     */   {
/* 106 */     DatabaseInfo database = cube.getDatabase();
/* 107 */     StringBuffer query = new StringBuffer();
/* 108 */     query.append("/cell/area?database=");
/* 109 */     query.append(database.getId());
/* 110 */     query.append("&cube=");
/* 111 */     query.append(cube.getId());
/* 112 */     query.append("&area=");
/* 113 */     query.append(getAreaIds(coordinates));
/* 114 */     query.append("&show_rule=1");
/* 115 */     String[][] response = request(query.toString());
/* 116 */     CellInfo[] cells = new CellInfo[response.length];
/* 117 */     CellInfoBuilder cellBuilder = this.builderReg.getCellBuilder();
/* 118 */     for (int i = 0; i < response.length; ++i) {
/* 119 */       cells[i] = cellBuilder.create(null, response[i]);
/*     */     }
/*     */ 
/* 122 */     return cells;
/*     */   }
/*     */ 
/*     */   public final boolean copyCell(CubeInfo cube, ElementInfo[] fromCoordinate, ElementInfo[] toCoordinate)
/*     */   {
/* 128 */     throw new PaloException("Currently not supported!!");
/*     */   }
/*     */ 
/*     */   public final boolean copyCell(CubeInfo cube, ElementInfo[] fromCoordinate, ElementInfo[] targetCoordinate, Object targetValue)
/*     */   {
/* 133 */     throw new PaloException("Currently not supported!!");
/*     */   }
/*     */ 
/*     */   public final boolean replaceValue(CubeInfo cube, ElementInfo[] coordinate, Object newValue, int splashMode)
/*     */     throws IOException
/*     */   {
/* 139 */     DatabaseInfo database = cube.getDatabase();
/* 140 */     String path = getIdString(coordinate);
/* 141 */     if (splashMode == -1)
/* 142 */       splashMode = 0;
/* 143 */     StringBuffer query = new StringBuffer();
/* 144 */     query.append("/cell/replace?database=");
/* 145 */     query.append(database.getId());
/* 146 */     query.append("&cube=");
/* 147 */     query.append(cube.getId());
/* 148 */     query.append("&path=");
/* 149 */     query.append(path);
/* 150 */     query.append("&value=");
/* 151 */     query.append(encode(newValue.toString()));
/* 152 */     query.append("&splash=");
/* 153 */     query.append(splashMode);
/* 154 */     String[][] response = request(query.toString());
/* 155 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final boolean replaceValues(CubeInfo cube, ElementInfo[][] coordinates, Object[] newValues, boolean add, int splashMode, boolean notifyProcessors)
/*     */     throws IOException
/*     */   {
/* 162 */     DatabaseInfo database = cube.getDatabase();
/* 163 */     String paths = getPaths(coordinates);
/* 164 */     if (splashMode == -1)
/* 165 */       splashMode = 0;
/* 166 */     StringBuffer query = new StringBuffer();
/* 167 */     query.append("/cell/replace_bulk?database=");
/* 168 */     query.append(database.getId());
/* 169 */     query.append("&cube=");
/* 170 */     query.append(cube.getId());
/* 171 */     query.append("&paths=");
/* 172 */     query.append(paths);
/* 173 */     query.append("&values=");
/* 174 */     query.append(encode(newValues));
/* 175 */     if (add)
/* 176 */       query.append("&add=1");
/*     */     else
/* 178 */       query.append("&add=0");
/* 179 */     query.append("&splash=");
/* 180 */     query.append(splashMode);
/* 181 */     if (notifyProcessors)
/* 182 */       query.append("&event_processor=1");
/*     */     else
/* 184 */       query.append("&event_processor=0");
/* 185 */     String[][] response = request(query.toString());
/* 186 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final String getRule(CubeInfo cube, ElementInfo[] coordinate) throws IOException
/*     */   {
/* 191 */     StringBuffer path = new StringBuffer();
/* 192 */     int lastId = coordinate.length - 1;
/* 193 */     for (int i = 0; i < coordinate.length; ++i) {
/* 194 */       path.append(coordinate[i].getId());
/* 195 */       if (i < lastId)
/* 196 */         path.append(",");
/*     */     }
/* 198 */     StringBuffer query = new StringBuffer();
/* 199 */     query.append("/cell/value?database=");
/* 200 */     query.append(cube.getDatabase().getId());
/* 201 */     query.append("&cube=");
/* 202 */     query.append(cube.getId());
/* 203 */     query.append("&path=");
/* 204 */     query.append(path);
/* 205 */     query.append("&show_rule=1");
/* 206 */     String[][] response = request(query.toString());
/* 207 */     if (response[0].length > 3)
/* 208 */       return response[0][3];
/* 209 */     return null;
/*     */   }
/*     */ 
/*     */   public final CellInfo getValue(CubeInfo cube, ElementInfo[] coordinate)
/*     */     throws IOException
/*     */   {
/* 221 */     StringBuffer path = new StringBuffer();
/* 222 */     int lastId = coordinate.length - 1;
/* 223 */     for (int i = 0; i < coordinate.length; ++i) {
/* 224 */       path.append(coordinate[i].getId());
/* 225 */       if (i < lastId)
/* 226 */         path.append(",");
/*     */     }
/* 228 */     StringBuffer query = new StringBuffer();
/* 229 */     query.append("/cell/value?database="); query.append(cube.getDatabase().getId());
/* 230 */     query.append("&cube="); query.append(cube.getId());
/* 231 */     query.append("&path="); query.append(path);
/* 232 */     query.append("&show_rule=1");
/* 233 */     String[][] response = request(query.toString());
/* 234 */     CellInfoBuilder cellBuilder = this.builderReg.getCellBuilder();
/* 235 */     CellInfo cell = cellBuilder.create(null, response[0]);
/* 236 */     return cell;
/*     */   }
/*     */ 
/*     */   public final CellInfo[] getValues(CubeInfo cube, ElementInfo[][] area)
/*     */     throws IOException
/*     */   {
/* 243 */     DatabaseInfo database = cube.getDatabase();
/* 244 */     String paths = getPaths(area);
/* 245 */     StringBuffer query = new StringBuffer();
/* 246 */     query.append("/cell/values?database=");
/* 247 */     query.append(database.getId());
/* 248 */     query.append("&cube=");
/* 249 */     query.append(cube.getId());
/* 250 */     query.append("&paths=");
/* 251 */     query.append(paths);
/* 252 */     query.append("&show_rule=1");
/* 253 */     String[][] response = request(query.toString());
/* 254 */     CellInfo[] cells = new CellInfo[response.length];
/* 255 */     CellInfoBuilder cellBuilder = this.builderReg.getCellBuilder();
/* 256 */     for (int i = 0; i < response.length; ++i) {
/* 257 */       cells[i] = cellBuilder.create(null, response[i]);
/*     */     }
/* 259 */     return cells;
/*     */   }
/*     */ 
/*     */   public final CellInfo[] export(CubeInfo cube, ExportContextInfo context)
/*     */     throws IOException
/*     */   {
/* 265 */     DatabaseInfo database = cube.getDatabase();
/* 266 */     String[] dimIds = cube.getDimensions();
/* 267 */     StringBuffer query = new StringBuffer();
/* 268 */     query.append("/cell/export?database=");
/* 269 */     query.append(database.getId());
/* 270 */     query.append("&cube=");
/* 271 */     query.append(cube.getId());
/* 272 */     query.append("&blocksize=");
/* 273 */     query.append(context.getBlocksize());
/* 274 */     query.append("&type=");
/* 275 */     query.append(context.getType());
/*     */ 
/* 277 */     String condition = context.getConditionRepresentation();
/* 278 */     if ((condition != null) && (condition.length() > 0)) {
/* 279 */       query.append("&condition=");
/* 280 */       query.append(encode(condition));
/*     */     }
/* 282 */     query.append("&use_rules=").append((context.useRules()) ? "1" : "0");
/* 283 */     query.append("&base_only=").append(
/* 284 */       (context.isBaseCellsOnly()) ? "1" : "0");
/* 285 */     query.append("&skip_empty=").append(
/* 286 */       (context.ignoreEmptyCells()) ? "1" : "0");
/* 287 */     String[] startAfterPath = context.getExportAfter();
/* 288 */     if ((startAfterPath != null) && (startAfterPath.length > 0)) {
/* 289 */       query.append("&path=");
/* 290 */       int lastElement = dimIds.length - 1;
/* 291 */       for (int i = 0; i < dimIds.length; ++i) {
/* 292 */         query.append(startAfterPath[i]);
/* 293 */         if (i < lastElement)
/* 294 */           query.append(",");
/*     */       }
/*     */     }
/* 297 */     query.append("&area=");
/* 298 */     StringBuffer elIDPaths = new StringBuffer(1000);
/* 299 */     String[][] area = context.getCellsArea();
/* 300 */     int lastPath = area.length - 1;
/* 301 */     for (int i = 0; i < area.length; ++i) {
/* 302 */       int lastID = area[i].length - 1;
/* 303 */       for (int j = 0; j < area[i].length; ++j) {
/* 304 */         elIDPaths.append(area[i][j]);
/* 305 */         if (j < lastID)
/* 306 */           elIDPaths.append(":");
/*     */       }
/* 308 */       if (i < lastPath)
/* 309 */         elIDPaths.append(",");
/*     */     }
/* 311 */     query.append(elIDPaths);
/* 312 */     String[][] response = request(query.toString());
/* 313 */     if ((response.length == 1) && (response[0].length == 3))
/* 314 */       throw new PaloException("getDataExport failed: " + response[0][0] + 
/* 315 */         ", " + response[0][1] + ", " + response[0][2]);
/* 316 */     int lastCell = response.length - 1;
/* 317 */     CellInfo[] cells = new CellInfo[response.length - 1];
/* 318 */     CellInfoBuilder cellBuilder = this.builderReg.getCellBuilder();
/* 319 */     for (int i = 0; i < lastCell; ++i) {
/* 320 */       cells[i] = cellBuilder.create(null, response[i]);
/*     */     }
/*     */ 
/* 323 */     context.setProgress(
/* 324 */       Double.parseDouble(response[lastCell][0]) / 
/* 325 */       Double.parseDouble(response[lastCell][1]));
/* 326 */     return cells;
/*     */   }
/*     */ 
/*     */   private final String getAreaIds(ElementInfo[][] coordinates)
/*     */   {
/* 334 */     StringBuffer ids = new StringBuffer();
/* 335 */     int lastCoordinate = coordinates.length - 1;
/* 336 */     for (int i = 0; i < coordinates.length; ++i) {
/* 337 */       ids.append(getIdString(coordinates[i], ":"));
/* 338 */       if (i < lastCoordinate)
/* 339 */         ids.append(",");
/*     */     }
/* 341 */     return ids.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.CellHandler
 * JD-Core Version:    0.5.4
 */