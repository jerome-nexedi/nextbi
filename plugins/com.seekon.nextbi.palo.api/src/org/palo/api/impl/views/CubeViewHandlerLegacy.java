/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import org.palo.api.Axis;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.impl.xml.IPaloEndHandler;
/*     */ import org.palo.api.impl.xml.IPaloStartHandler;
/*     */ import org.palo.api.impl.xml.XMLUtil;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ class CubeViewHandlerLegacy extends CubeViewHandler
/*     */ {
/*     */   private Dimension currDim;
/*     */   private String currDimId;
/*     */   private final String key;
/*     */ 
/*     */   CubeViewHandlerLegacy(Database database, String key)
/*     */   {
/*  62 */     super(database);
/*  63 */     this.key = key;
/*     */   }
/*     */ 
/*     */   protected void registerEndHandlers() {
/*  67 */     clearEndHandlers();
/*  68 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/*  70 */         return "view/columns/column";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/*  74 */         CubeViewHandlerLegacy.this.handleDimensionEnd();
/*     */       }
/*     */     });
/*  77 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/*  79 */         return "view/columns";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/*  83 */         CubeViewHandlerLegacy.this.handleAxisEnd();
/*     */       }
/*     */     });
/*  86 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/*  88 */         return "view/rows/row";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/*  92 */         CubeViewHandlerLegacy.this.handleDimensionEnd();
/*     */       }
/*     */     });
/*  95 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/*  97 */         return "view/rows";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 101 */         CubeViewHandlerLegacy.this.handleAxisEnd();
/*     */       }
/*     */     });
/* 104 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/* 106 */         return "view/selects/select";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 110 */         CubeViewHandlerLegacy.this.handleDimensionEnd();
/*     */       }
/*     */     });
/* 113 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/* 115 */         return "view/selects";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/* 119 */         CubeViewHandlerLegacy.this.handleAxisEnd();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   protected final void registerStartHandlers() {
/* 125 */     clearStartHandlers();
/* 126 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 128 */         return "view";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 133 */         CubeViewBuilder viewBuilder = new CubeViewBuilder();
/* 134 */         viewBuilder.setId(CubeViewHandlerLegacy.this.key);
/* 135 */         viewBuilder.setName(attributes.getValue("name"));
/* 136 */         viewBuilder.setDescription(XMLUtil.dequoteString(
/* 137 */           attributes.getValue("description")));
/* 138 */         String cubeId = attributes.getValue("cube");
/*     */ 
/* 140 */         cubeId = CubeViewReader.getLeafName(cubeId);
/* 141 */         viewBuilder.setCube(CubeViewHandlerLegacy.this.database.getCubeByName(cubeId));
/* 142 */         CubeViewHandlerLegacy.this.cubeView = viewBuilder.createView(CubeViewHandlerLegacy.this);
/*     */ 
/* 144 */         if (CubeViewHandlerLegacy.this.cubeView == null)
/* 145 */           throw new PaloAPIException("CubeView creation failed!");
/*     */       }
/*     */     });
/* 150 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 152 */         return "view/rows";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 157 */         CubeViewHandlerLegacy.this.currAxis = CubeViewHandlerLegacy.this.cubeView.getAxis("rows");
/* 158 */         if (CubeViewHandlerLegacy.this.currAxis == null)
/* 159 */           CubeViewHandlerLegacy.this.currAxis = CubeViewHandlerLegacy.this.cubeView.addAxis("rows", "rows");
/*     */       }
/*     */     });
/* 162 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 164 */         return "view/rows/row";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 169 */         CubeViewHandlerLegacy.this.handleAxisProperties(attributes);
/*     */       }
/*     */     });
/* 172 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 174 */         return "view/rows/row/hidden";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 179 */         CubeViewHandlerLegacy.this.handleHidden(attributes);
/*     */       }
/*     */     });
/* 182 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 184 */         return "view/rows/row/expanded";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 189 */         CubeViewHandlerLegacy.this.handleExpanded(attributes);
/*     */       }
/*     */     });
/* 194 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 196 */         return "view/columns";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 201 */         CubeViewHandlerLegacy.this.currAxis = CubeViewHandlerLegacy.this.cubeView.getAxis("cols");
/* 202 */         if (CubeViewHandlerLegacy.this.currAxis == null)
/* 203 */           CubeViewHandlerLegacy.this.currAxis = CubeViewHandlerLegacy.this.cubeView.addAxis("cols", "columns");
/*     */       }
/*     */     });
/* 206 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 208 */         return "view/columns/column";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 213 */         CubeViewHandlerLegacy.this.handleAxisProperties(attributes);
/*     */       }
/*     */     });
/* 216 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 218 */         return "view/columns/column/hidden";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 223 */         CubeViewHandlerLegacy.this.handleHidden(attributes);
/*     */       }
/*     */     });
/* 226 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 228 */         return "view/columns/column/expanded";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 233 */         CubeViewHandlerLegacy.this.handleExpanded(attributes);
/*     */       }
/*     */     });
/* 238 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 240 */         return "view/selects";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 245 */         CubeViewHandlerLegacy.this.currAxis = CubeViewHandlerLegacy.this.cubeView.getAxis("selected");
/* 246 */         if (CubeViewHandlerLegacy.this.currAxis == null)
/* 247 */           CubeViewHandlerLegacy.this.currAxis = CubeViewHandlerLegacy.this.cubeView.addAxis("selected", "selects");
/*     */       }
/*     */     });
/* 250 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 252 */         return "view/selects/select";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 257 */         CubeViewHandlerLegacy.this.handleAxisProperties(attributes);
/*     */       }
/*     */     });
/* 260 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 262 */         return "view/selects/select/hidden";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 267 */         CubeViewHandlerLegacy.this.handleHidden(attributes);
/*     */       }
/*     */     });
/* 270 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 272 */         return "view/selects/select/expanded";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 277 */         CubeViewHandlerLegacy.this.handleExpanded(attributes);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private final void handleDimensionEnd() {
/* 283 */     this.currDim = null;
/*     */   }
/*     */   private final void handleAxisEnd() {
/* 286 */     this.currAxis = null;
/*     */   }
/*     */ 
/*     */   private final void handleAxisProperties(Attributes attributes) {
/* 290 */     this.currDimId = attributes.getValue("dimension");
/* 291 */     String subsetName = attributes.getValue("subset");
/* 292 */     String selElement = attributes.getValue("selectedElement");
/* 293 */     boolean useSubset = 
/* 294 */       Boolean.valueOf(attributes.getValue("useSubset")).booleanValue();
/*     */ 
/* 296 */     this.currDimId = CubeViewReader.getLeafName(this.currDimId);
/* 297 */     this.currDim = this.database.getDimensionByName(this.currDimId);
/* 298 */     if (this.currDim == null) {
/* 299 */       addError("CubeViewHandlerLegacy: unknown dimension '" + this.currDimId + 
/* 300 */         "'!!", this.cubeView.getId(), this.cubeView, this.database, this.currDimId, 
/* 301 */         0, 
/* 302 */         null, -1);
/* 303 */       return;
/*     */     }
/* 305 */     this.currAxis.add(this.currDim);
/*     */ 
/* 307 */     if (useSubset) {
/* 308 */       Subset activeSub = this.currDim.getSubset(subsetName);
/* 309 */       if (activeSub == null)
/* 310 */         activeSub = getSubsetByName(this.currDim, subsetName);
/* 311 */       this.currAxis.setActiveSubset(this.currDim, activeSub);
/*     */     } else {
/* 313 */       this.currAxis.setActiveSubset(this.currDim, null);
/* 314 */     }this.currAxis.setSelectedElement(this.currDim, 
/* 315 */       this.currDim.getDefaultHierarchy().getElementByName(selElement));
/*     */   }
/*     */ 
/*     */   private final void handleHidden(Attributes attributes)
/*     */   {
/* 320 */     if (this.currDim == null) {
/* 321 */       addError(
/* 322 */         "CubeViewHandlerLegacy: (hidden path) no dimension defined!!", 
/* 323 */         this.cubeView.getId(), this.cubeView, this.database, this.currDimId, 
/* 324 */         0, 
/* 325 */         this.currAxis, 4);
/* 326 */       return;
/*     */     }
/* 328 */     String path = attributes.getValue("path");
/* 329 */     if (path == null) {
/* 330 */       addError("CubeViewHandlerLegacy: no hidden path defined!!", 
/* 331 */         this.cubeView.getId(), this.cubeView, this.currAxis, path, 
/* 332 */         16, 
/* 333 */         this.currAxis, 4);
/* 334 */       return;
/*     */     }
/*     */ 
/* 337 */     Element[] elPath = getPath(path, this.currDim.getDefaultHierarchy(), this.currAxis, 
/* 338 */       4);
/* 339 */     this.currAxis.addHidden(this.currDim, elPath);
/*     */   }
/*     */ 
/*     */   private final void handleExpanded(Attributes attributes)
/*     */   {
/* 344 */     if (this.currDim == null) {
/* 345 */       addError(
/* 346 */         "CubeViewHandlerLegacy: (expanded path) no dimension defined!!", 
/* 347 */         this.cubeView.getId(), this.cubeView, this.database, this.currDimId, 
/* 348 */         0, 
/* 349 */         this.currAxis, 2);
/* 350 */       return;
/*     */     }
/* 352 */     String path = attributes.getValue("path");
/* 353 */     String reps = attributes.getValue("repetitions");
/* 354 */     int[] repetitions = CubeViewReader.getRepetitions(reps);
/* 355 */     if (path == null) {
/* 356 */       addError("CubeViewHandlerLegacy: no expanded path defined!!", 
/* 357 */         this.cubeView.getId(), this.cubeView, this.currAxis, path, 
/* 358 */         16, 
/* 359 */         this.currAxis, 2);
/* 360 */       return;
/*     */     }
/* 362 */     Element[] elPath = getPath(path, this.currDim.getDefaultHierarchy(), this.currAxis, 
/* 363 */       2);
/* 364 */     for (int i = 0; i < repetitions.length; ++i)
/* 365 */       this.currAxis.addExpanded(this.currDim, elPath, repetitions[i]);
/*     */   }
/*     */ 
/*     */   private final Subset getSubsetByName(Dimension dim, String subName) {
/* 369 */     Subset[] subsets = dim.getSubsets();
/* 370 */     for (int i = 0; i < subsets.length; ++i) {
/* 371 */       if (subsets[i].getName().equals(subName))
/* 372 */         return subsets[i];
/*     */     }
/* 374 */     return null;
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewHandlerLegacy
 * JD-Core Version:    0.5.4
 */