/*     */ package com.tensegrity.palojava.http.handlers;
/*     */ 
/*     */ import com.tensegrity.palojava.DatabaseInfo;
/*     */ import com.tensegrity.palojava.DimensionInfo;
/*     */ import com.tensegrity.palojava.ElementInfo;
/*     */ import com.tensegrity.palojava.ServerInfo;
/*     */ import com.tensegrity.palojava.http.HttpConnection;
/*     */ import com.tensegrity.palojava.http.builders.ElementInfoBuilder;
/*     */ import com.tensegrity.palojava.http.builders.InfoBuilderRegistry;
/*     */ import com.tensegrity.palojava.impl.ElementInfoImpl;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public class ElementHandler extends HttpHandler
/*     */ {
/*     */   private static final String APPEND_PREFIX = "/element/append?database=";
/*     */   private static final String DELETE_PREFIX = "/element/destroy?database=";
/*     */   private static final String DELETE_BULK_PREFIX = "/element/destroy_bulk?database=";
/*     */   private static final String CREATE_PREFIX = "/element/create?database=";
/*     */   private static final String CREATE_BULK_PREFIX = "/element/create_bulk?database=";
/*     */   private static final String INFO_PREFIX = "/element/info?database=";
/*     */   private static final String MOVE_PREFIX = "/element/move?database=";
/*     */   private static final String RENAME_PREFIX = "/element/rename?database=";
/*     */   private static final String REPLACE_PREFIX = "/element/replace?database=";
/*     */   private static final String REPLACE_BULK_PREFIX = "/element/replace_bulk?database=";
/*  90 */   private static final ElementHandler instance = new ElementHandler();
/*     */   private final InfoBuilderRegistry builderReg;
/*     */ 
/*     */   static final ElementHandler getInstance(HttpConnection connection)
/*     */   {
/*  92 */     instance.use(connection);
/*  93 */     return instance;
/*     */   }
/*     */ 
/*     */   private ElementHandler()
/*     */   {
/* 101 */     this.builderReg = InfoBuilderRegistry.getInstance();
/*     */   }
/*     */ 
/*     */   public final ElementInfo append(ElementInfo element, ElementInfo[] children, double[] weights)
/*     */     throws IOException
/*     */   {
/* 107 */     DimensionInfo dimension = element.getDimension();
/* 108 */     DatabaseInfo database = dimension.getDatabase();
/* 109 */     String childrenStr = getIdString(children);
/* 110 */     String weightStr = getWeightString(weights);
/*     */ 
/* 112 */     StringBuffer query = new StringBuffer();
/* 113 */     query.append("/element/append?database=");
/* 114 */     query.append(database.getId());
/* 115 */     query.append("&dimension=");
/* 116 */     query.append(dimension.getId());
/* 117 */     query.append("&element=");
/* 118 */     query.append(element.getId());
/* 119 */     query.append("&children=");
/* 120 */     query.append(childrenStr);
/* 121 */     query.append("&weights=");
/* 122 */     query.append(weightStr);
/* 123 */     String[][] response = request(query.toString());
/* 124 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 125 */     elementBuilder.update((ElementInfoImpl)element, response[0]);
/* 126 */     return element;
/*     */   }
/*     */ 
/*     */   public final ElementInfo create(DimensionInfo dimension, String name, int type, ElementInfo[] children, double[] weights)
/*     */     throws IOException
/*     */   {
/* 132 */     DatabaseInfo database = dimension.getDatabase();
/* 133 */     String childrenStr = getIdString(children);
/* 134 */     String weightStr = getWeightString(weights);
/*     */ 
/* 137 */     StringBuffer query = new StringBuffer();
/* 138 */     query.append("/element/create?database=");
/* 139 */     query.append(database.getId());
/* 140 */     query.append("&dimension=");
/* 141 */     query.append(dimension.getId());
/*     */ 
/* 143 */     query.append("&new_name=");
/* 144 */     query.append(encode(name));
/* 145 */     query.append("&type=");
/* 146 */     query.append(type);
/* 147 */     query.append("&children=");
/* 148 */     query.append(childrenStr);
/* 149 */     query.append("&weights=");
/* 150 */     query.append(weightStr);
/* 151 */     String[][] response = request(query.toString());
/* 152 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 153 */     return elementBuilder.create(dimension, response[0]);
/*     */   }
/*     */ 
/*     */   public final boolean createBulk(DimensionInfo dimension, ElementInfo[] names, int type, ElementInfo[][] children, double[][] weights) throws IOException
/*     */   {
/* 158 */     DatabaseInfo database = dimension.getDatabase();
/* 159 */     String parents = getParentString(names);
/* 160 */     String childrenStr = getChildrenString(children);
/* 161 */     String weightStr = getWeightsString(weights);
/*     */ 
/* 164 */     StringBuffer query = new StringBuffer();
/* 165 */     query.append("/element/create_bulk?database=");
/* 166 */     query.append(database.getId());
/* 167 */     query.append("&dimension=");
/* 168 */     query.append(dimension.getId());
/*     */ 
/* 170 */     query.append("&name_elements=");
/* 171 */     query.append(encode(parents));
/* 172 */     query.append("&type=");
/* 173 */     query.append(type);
/* 174 */     if (children.length != 0) {
/* 175 */       query.append("&children=");
/* 176 */       query.append(childrenStr);
/*     */     }
/* 178 */     if (weights.length != 0) {
/* 179 */       query.append("&weights=");
/* 180 */       query.append(weightStr);
/*     */     }
/* 182 */     String[][] response = request(query.toString());
/* 183 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final boolean createBulk(DimensionInfo dimension, String[] names, int type, ElementInfo[][] children, double[][] weights) throws IOException
/*     */   {
/* 188 */     DatabaseInfo database = dimension.getDatabase();
/* 189 */     StringBuffer par = new StringBuffer();
/* 190 */     int i = 0; for (int n = names.length; i < n; ++i) {
/* 191 */       String val = names[i];
/*     */ 
/* 193 */       val = val.replaceAll("\"", "\"\"");
/* 194 */       val = encode(val);
/* 195 */       par.append("%22");
/* 196 */       par.append(val);
/* 197 */       par.append("%22");
/*     */ 
/* 206 */       if (i < n - 1) {
/* 207 */         par.append("%2C");
/*     */       }
/*     */     }
/* 210 */     String parents = par.toString();
/*     */ 
/* 213 */     StringBuffer query = new StringBuffer();
/* 214 */     query.append("/element/create_bulk?database=");
/* 215 */     query.append(database.getId());
/* 216 */     query.append("&dimension=");
/* 217 */     query.append(dimension.getId());
/*     */ 
/* 219 */     query.append("&name_elements=");
/* 220 */     query.append(parents);
/* 221 */     query.append("&type=");
/* 222 */     query.append(type);
/* 223 */     if (children.length != 0) {
/* 224 */       String childrenStr = getChildrenString(children);
/* 225 */       query.append("&children=");
/* 226 */       query.append(childrenStr);
/*     */     }
/* 228 */     if (weights.length != 0) {
/* 229 */       String weightStr = getWeightsString(weights);
/* 230 */       query.append("&weights=");
/* 231 */       query.append(weightStr);
/*     */     }
/*     */ 
/* 234 */     String[][] response = request(query.toString());
/* 235 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   private final void verifyParameters(String[] names, int[] types, ElementInfo[][] children, double[][] weights)
/*     */   {
/* 241 */     HashSet created = new HashSet();
/* 242 */     ArrayList newNames = new ArrayList();
/* 243 */     ArrayList newTypes = new ArrayList();
/* 244 */     ArrayList newChildren = new ArrayList();
/* 245 */     ArrayList newWeights = new ArrayList();
/*     */ 
/* 247 */     int i = 0; for (int n = names.length; i < n; ++i) {
/* 248 */       String e = names[i];
/* 249 */       String s = e.toLowerCase();
/* 250 */       if (!created.contains(s))
/* 251 */         created.add(s);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean createBulk(DimensionInfo dimension, String[] names, int[] types, ElementInfo[][] children, double[][] weights)
/*     */     throws IOException
/*     */   {
/* 258 */     DatabaseInfo database = dimension.getDatabase();
/* 259 */     StringBuffer par = new StringBuffer();
/* 260 */     int i = 0; for (int n = names.length; i < n; ++i) {
/* 261 */       String val = names[i];
/*     */ 
/* 263 */       val = val.replaceAll("\"", "\"\"");
/* 264 */       val = encode(val);
/* 265 */       par.append("%22");
/* 266 */       par.append(val);
/* 267 */       par.append("%22");
/* 268 */       if (i < n - 1) {
/* 269 */         par.append("%2C");
/*     */       }
/*     */     }
/* 272 */     String parents = par.toString();
/*     */ 
/* 275 */     verifyParameters(names, types, children, weights);
/* 276 */     StringBuffer query = new StringBuffer();
/* 277 */     query.append("/element/create_bulk?database=");
/* 278 */     query.append(database.getId());
/* 279 */     query.append("&dimension=");
/* 280 */     query.append(dimension.getId());
/*     */ 
/* 282 */     query.append("&name_elements=");
/* 283 */     query.append(parents);
/* 284 */     query.append("&types=");
/* 285 */     i = 0; for (int n = types.length; i < n; ++i) {
/* 286 */       query.append(types[i]);
/* 287 */       if (i < n - 1) {
/* 288 */         query.append(",");
/*     */       }
/*     */     }
/* 291 */     if (children.length != 0) {
/* 292 */       String childrenStr = getChildrenNameString(children);
/* 293 */       query.append("&name_children=");
/* 294 */       query.append(childrenStr);
/*     */     }
/* 296 */     if (weights.length != 0) {
/* 297 */       String weightStr = getWeightsString(weights);
/* 298 */       query.append("&weights=");
/* 299 */       query.append(weightStr);
/*     */     }
/*     */ 
/* 302 */     String[][] response = request(query.toString());
/* 303 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final boolean replaceBulk(DimensionInfo dimension, ElementInfo[] elements, int type, ElementInfo[][] children, Double[][] weights)
/*     */     throws IOException
/*     */   {
/* 310 */     DatabaseInfo database = dimension.getDatabase();
/* 311 */     StringBuffer query = new StringBuffer();
/* 312 */     query.append("/element/replace_bulk?database=");
/* 313 */     query.append(database.getId());
/* 314 */     query.append("&dimension=");
/* 315 */     query.append(dimension.getId());
/* 316 */     query.append("&elements=");
/* 317 */     int i = 0; for (int n = elements.length; i < n; ++i) {
/* 318 */       query.append(elements[i].getId());
/* 319 */       if (i < n - 1) {
/* 320 */         query.append(",");
/*     */       }
/*     */     }
/* 323 */     query.append("&type=");
/* 324 */     query.append(type);
/* 325 */     if ((children != null) && (children.length != 0)) {
/* 326 */       String childrenStr = getChildrenString(children);
/* 327 */       query.append("&children=");
/* 328 */       query.append(childrenStr);
/*     */     }
/* 330 */     if ((weights != null) && (weights.length != 0)) {
/* 331 */       String weightStr = getWeightsString(weights);
/* 332 */       query.append("&weights=");
/* 333 */       query.append(weightStr);
/*     */     }
/*     */ 
/* 336 */     String[][] response = request(query.toString());
/* 337 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final boolean destroy(ElementInfo element) throws IOException
/*     */   {
/* 342 */     DimensionInfo dimension = element.getDimension();
/* 343 */     DatabaseInfo database = dimension.getDatabase();
/* 344 */     StringBuffer query = new StringBuffer();
/* 345 */     query.append("/element/destroy?database=");
/* 346 */     query.append(database.getId());
/* 347 */     query.append("&dimension=");
/* 348 */     query.append(dimension.getId());
/* 349 */     query.append("&element=");
/* 350 */     query.append(element.getId());
/* 351 */     String[][] response = request(query.toString());
/* 352 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final boolean destroy(ElementInfo[] elements) throws IOException
/*     */   {
/* 357 */     if (elements.length < 1)
/* 358 */       return true;
/* 359 */     DimensionInfo dimension = elements[0].getDimension();
/* 360 */     DatabaseInfo database = dimension.getDatabase();
/* 361 */     String elementIDs = getIdString(elements);
/* 362 */     StringBuffer query = new StringBuffer();
/* 363 */     query.append("/element/destroy_bulk?database=");
/* 364 */     query.append(database.getId());
/* 365 */     query.append("&dimension=");
/* 366 */     query.append(dimension.getId());
/* 367 */     query.append("&elements=");
/* 368 */     query.append(elementIDs);
/* 369 */     String[][] response = request(query.toString());
/* 370 */     return response[0][0].equals("1");
/*     */   }
/*     */ 
/*     */   public final ElementInfo getInfo(DimensionInfo dimension, String id)
/*     */     throws IOException
/*     */   {
/* 377 */     DatabaseInfo database = dimension.getDatabase();
/* 378 */     StringBuffer query = new StringBuffer();
/* 379 */     query.append("/element/info?database=");
/* 380 */     query.append(database.getId());
/* 381 */     query.append("&dimension=");
/* 382 */     query.append(dimension.getId());
/* 383 */     query.append("&element=");
/* 384 */     query.append(id);
/* 385 */     String[][] response = request(query.toString());
/* 386 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 387 */     return elementBuilder.create(dimension, response[0]);
/*     */   }
/*     */ 
/*     */   public final ElementInfo move(ElementInfo element, int newPosition)
/*     */     throws IOException
/*     */   {
/* 393 */     DimensionInfo dimension = element.getDimension();
/* 394 */     DatabaseInfo database = dimension.getDatabase();
/*     */ 
/* 396 */     StringBuffer query = new StringBuffer();
/* 397 */     query.append("/element/move?database=");
/* 398 */     query.append(database.getId());
/* 399 */     query.append("&dimension=");
/* 400 */     query.append(dimension.getId());
/* 401 */     query.append("&element=");
/* 402 */     query.append(element.getId());
/* 403 */     query.append("&position=");
/* 404 */     query.append(newPosition);
/* 405 */     String[][] response = request(query.toString());
/* 406 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 407 */     elementBuilder.update((ElementInfoImpl)element, response[0]);
/*     */ 
/* 409 */     return element;
/*     */   }
/*     */ 
/*     */   public final ElementInfo reload(ElementInfo element) throws IOException {
/* 413 */     DimensionInfo dimension = element.getDimension();
/* 414 */     DatabaseInfo database = dimension.getDatabase();
/* 415 */     StringBuffer query = new StringBuffer();
/* 416 */     query.append("/element/info?database=");
/* 417 */     query.append(database.getId());
/* 418 */     query.append("&dimension=");
/* 419 */     query.append(dimension.getId());
/* 420 */     query.append("&element=");
/* 421 */     query.append(element.getId());
/* 422 */     String[][] response = request(query.toString());
/* 423 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 424 */     elementBuilder.update((ElementInfoImpl)element, response[0]);
/* 425 */     return element;
/*     */   }
/*     */ 
/*     */   public final ElementInfo rename(ElementInfo element, String newName) throws IOException
/*     */   {
/* 430 */     DimensionInfo dimension = element.getDimension();
/* 431 */     DatabaseInfo database = dimension.getDatabase();
/* 432 */     StringBuffer query = new StringBuffer();
/* 433 */     query.append("/element/rename?database=");
/* 434 */     query.append(database.getId());
/* 435 */     query.append("&dimension=");
/* 436 */     query.append(dimension.getId());
/* 437 */     query.append("&element=");
/* 438 */     query.append(element.getId());
/* 439 */     query.append("&new_name=");
/* 440 */     query.append(encode(newName));
/* 441 */     String[][] response = request(query.toString());
/* 442 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 443 */     elementBuilder.update((ElementInfoImpl)element, response[0]);
/* 444 */     return element;
/*     */   }
/*     */ 
/*     */   public final void update(ElementInfo element, int type, String[] children, double[] weights, ServerInfo serverInfo) throws IOException
/*     */   {
/* 449 */     DimensionInfo dimension = element.getDimension();
/* 450 */     DatabaseInfo database = dimension.getDatabase();
/*     */ 
/* 452 */     String childrenStr = getIdString(children);
/* 453 */     String weightStr = getWeightString(weights);
/* 454 */     StringBuffer query = new StringBuffer();
/* 455 */     query.append("/element/replace?database=");
/* 456 */     query.append(database.getId());
/* 457 */     query.append("&dimension=");
/* 458 */     query.append(dimension.getId());
/* 459 */     query.append("&element=");
/* 460 */     query.append(element.getId());
/*     */ 
/* 463 */     query.append("&type=");
/* 464 */     query.append(type);
/* 465 */     if ((serverInfo.getMajor() < 3) || (childrenStr.length() > 0)) {
/* 466 */       query.append("&children=");
/* 467 */       query.append(childrenStr);
/*     */     }
/* 469 */     if ((serverInfo.getMajor() < 3) || (weightStr.length() > 0)) {
/* 470 */       query.append("&weights=");
/* 471 */       query.append(weightStr);
/*     */     }
/* 473 */     String[][] response = request(query.toString());
/* 474 */     ElementInfoBuilder elementBuilder = this.builderReg.getElementBuilder();
/* 475 */     elementBuilder.update((ElementInfoImpl)element, response[0]);
/*     */   }
/*     */ 
/*     */   private final String getParentString(ElementInfo[] parents)
/*     */   {
/* 480 */     StringBuffer str = new StringBuffer();
/* 481 */     int lastParent = parents.length - 1;
/* 482 */     for (int i = 0; i < lastParent; ++i) {
/* 483 */       str.append(parents[i].getName());
/* 484 */       str.append("_NEW");
/* 485 */       str.append(",");
/*     */     }
/*     */ 
/* 488 */     str.append(parents[lastParent].getName());
/* 489 */     str.append("_NEW");
/* 490 */     return str.toString();
/*     */   }
/*     */ 
/*     */   private final String getChildrenString(ElementInfo[][] children) {
/* 494 */     StringBuffer str = new StringBuffer();
/* 495 */     int lastChild = children.length - 1;
/* 496 */     for (int i = 0; i < lastChild; ++i) {
/* 497 */       str.append(getIdString(children[i]));
/* 498 */       str.append(":");
/*     */     }
/*     */ 
/* 501 */     str.append(getIdString(children[lastChild]));
/* 502 */     return str.toString();
/*     */   }
/*     */   private final String getChildrenNameString(ElementInfo[][] children) {
/* 505 */     StringBuffer str = new StringBuffer();
/* 506 */     int lastChild = children.length - 1;
/* 507 */     for (int i = 0; i < lastChild; ++i) {
/* 508 */       str.append(getNameString(children[i]));
/* 509 */       str.append(":");
/*     */     }
/*     */ 
/* 512 */     str.append(getNameString(children[lastChild]));
/* 513 */     return str.toString();
/*     */   }
/*     */   private final String getWeightsString(double[][] weights) {
/* 516 */     StringBuffer str = new StringBuffer();
/* 517 */     int lastWeight = weights.length - 1;
/* 518 */     for (int i = 0; i < lastWeight; ++i) {
/* 519 */       str.append(getWeightString(weights[i]));
/* 520 */       str.append(":");
/*     */     }
/*     */ 
/* 523 */     str.append(getWeightString(weights[lastWeight]));
/* 524 */     return str.toString();
/*     */   }
/*     */   private final String getWeightsString(Double[][] weights) {
/* 527 */     StringBuffer str = new StringBuffer();
/* 528 */     int lastWeight = weights.length - 1;
/* 529 */     for (int i = 0; i < lastWeight; ++i) {
/* 530 */       str.append(getWeightString(weights[i]));
/* 531 */       str.append(":");
/*     */     }
/*     */ 
/* 534 */     str.append(getWeightString(weights[lastWeight]));
/* 535 */     return str.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name:     com.tensegrity.palojava.http.handlers.ElementHandler
 * JD-Core Version:    0.5.4
 */