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
/*     */ class CubeViewHandler1_0 extends CubeViewHandler
/*     */ {
/*     */   CubeViewHandler1_0(Database database)
/*     */   {
/*  66 */     super(database);
/*     */   }
/*     */ 
/*     */   protected void registerEndHandlers() {
/*  70 */     registerEndHandler(new IPaloEndHandler() {
/*     */       public String getPath() {
/*  72 */         return "view/axis";
/*     */       }
/*     */ 
/*     */       public void endElement(String uri, String localName, String qName) {
/*  76 */         CubeViewHandler1_0.this.currAxis = null;
/*     */       } } );
/*     */   }
/*     */ 
/*     */   protected void registerStartHandlers() {
/*  81 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/*  83 */         return "view";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/*  88 */         CubeViewBuilder viewBuilder = new CubeViewBuilder();
/*  89 */         viewBuilder.setId(attributes.getValue("id"));
/*  90 */         viewBuilder.setName(attributes.getValue("name"));
/*  91 */         viewBuilder.setDescription(XMLUtil.dequoteString(
/*  92 */           attributes.getValue("description")));
/*  93 */         String str = attributes.getValue("hideempty");
/*  94 */         if ((str != null) && (str.equalsIgnoreCase("true")))
/*  95 */           viewBuilder.addProperty("hideEmpty", 
/*  96 */             Boolean.toString(true));
/*  97 */         String cubeId = attributes.getValue("cube");
/*  98 */         if (cubeId != null)
/*  99 */           viewBuilder.setCube(CubeViewHandler1_0.this.database.getCubeByName(cubeId));
/* 100 */         CubeViewHandler1_0.this.cubeView = viewBuilder.createView(CubeViewHandler1_0.this);
/*     */ 
/* 102 */         if (CubeViewHandler1_0.this.cubeView == null)
/* 103 */           throw new PaloAPIException("CubeView creation failed!");
/*     */       }
/*     */     });
/* 107 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 109 */         return "view/axis";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 114 */         String id = attributes.getValue("id");
/* 115 */         String name = attributes.getValue("name");
/* 116 */         if (id == null) {
/* 117 */           CubeViewHandler1_0.this
/* 120 */             .addError("CubeViewReader: missing id attribute for axis", 
/* 118 */             CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, null, null, 
/* 119 */             4, 
/* 120 */             null, 0);
/*     */         }
/* 122 */         CubeViewHandler1_0.this.currAxis = CubeViewHandler1_0.this.cubeView.getAxis(id);
/* 123 */         if (CubeViewHandler1_0.this.currAxis == null)
/* 124 */           CubeViewHandler1_0.this.currAxis = CubeViewHandler1_0.this.cubeView.addAxis(id, name);
/*     */       }
/*     */     });
/* 128 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 130 */         return "view/axis/dimension";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 135 */         String dimId = attributes.getValue("id");
/* 136 */         dimId = CubeViewReader.getLeafName(dimId);
/* 137 */         Dimension dim = CubeViewHandler1_0.this.database.getDimensionByName(dimId);
/* 138 */         if (dim == null) {
/* 139 */           CubeViewHandler1_0.this
/* 142 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 140 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, CubeViewHandler1_0.this.database, 
/* 141 */             dimId, 0, 
/* 142 */             CubeViewHandler1_0.this.currAxis, 0);
/*     */         }
/* 144 */         CubeViewHandler1_0.this.currAxis.add(dim);
/*     */       }
/*     */     });
/* 148 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 150 */         return "view/axis/selected";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes) {
/* 154 */         String element = attributes.getValue("element");
/* 155 */         String dimId = attributes.getValue("dimension");
/* 156 */         String hierId = attributes.getValue("hierarchy");
/* 157 */         dimId = CubeViewReader.getLeafName(dimId);
/* 158 */         Dimension dim = CubeViewHandler1_0.this.database.getDimensionByName(dimId);
/* 159 */         if (dim == null)
/* 160 */           CubeViewHandler1_0.this
/* 163 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 161 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, CubeViewHandler1_0.this.database, 
/* 162 */             dimId, 0, 
/* 163 */             CubeViewHandler1_0.this.currAxis, 0);
/*     */         Hierarchy hier;
/* 166 */         if (hierId == null)
/* 167 */           hier = dim.getDefaultHierarchy();
/*     */         else {
/* 169 */           hier = dim.getHierarchyById(hierId);
/*     */         }
/* 171 */         if (hier == null) {
/* 172 */           CubeViewHandler1_0.this
/* 175 */             .addError("CubeViewReader: unknown hierarchy id '" + hierId + 
/* 173 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, CubeViewHandler1_0.this.database, 
/* 174 */             hierId, 0, 
/* 175 */             CubeViewHandler1_0.this.currAxis, 0);
/*     */         }
/* 177 */         Element selected = hier.getElementByName(element);
/* 178 */         if (selected == null) {
/* 179 */           CubeViewHandler1_0.this
/* 182 */             .addError("CubeViewReader: unknown element id '" + element + 
/* 180 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, dim, element, 
/* 181 */             1, 
/* 182 */             CubeViewHandler1_0.this.currAxis, 1);
/*     */         }
/* 184 */         CubeViewHandler1_0.this.currAxis.setSelectedElement(dim, selected);
/*     */       }
/*     */     });
/* 188 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 190 */         return "view/axis/active";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 195 */         String subset = attributes.getValue("subset");
/* 196 */         String dimId = attributes.getValue("dimension");
/* 197 */         dimId = CubeViewReader.getLeafName(dimId);
/* 198 */         Dimension dim = CubeViewHandler1_0.this.database.getDimensionByName(dimId);
/* 199 */         if (dim == null) {
/* 200 */           CubeViewHandler1_0.this
/* 203 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 201 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, CubeViewHandler1_0.this.database, 
/* 202 */             dimId, 0, 
/* 203 */             CubeViewHandler1_0.this.currAxis, 0);
/*     */         }
/* 205 */         Subset activeSub = dim.getSubset(subset);
/* 206 */         if (activeSub == null) {
/* 207 */           CubeViewHandler1_0.this
/* 210 */             .addError("CubeViewReader: unknown subset id '" + subset + 
/* 208 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, dim, subset, 
/* 209 */             8, 
/* 210 */             CubeViewHandler1_0.this.currAxis, 8);
/*     */         }
/* 212 */         CubeViewHandler1_0.this.currAxis.setActiveSubset(dim, activeSub);
/*     */       }
/*     */     });
/* 216 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 218 */         return "view/axis/expanded";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 223 */         String path = attributes.getValue("path");
/* 224 */         String dimId = attributes.getValue("dimension");
/* 225 */         String hierId = attributes.getValue("hierarchy");
/* 226 */         String reps = attributes.getValue("repetitions");
/* 227 */         dimId = CubeViewReader.getLeafName(dimId);
/* 228 */         Dimension dim = CubeViewHandler1_0.this.database.getDimensionByName(dimId);
/* 229 */         int[] repetitions = CubeViewReader.getRepetitions(reps);
/* 230 */         if (dim == null)
/* 231 */           CubeViewHandler1_0.this
/* 234 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 232 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, CubeViewHandler1_0.this.database, 
/* 233 */             dimId, 0, 
/* 234 */             CubeViewHandler1_0.this.currAxis, 2);
/*     */         Hierarchy hier;
/* 237 */         if (hierId == null)
/* 238 */           hier = dim.getDefaultHierarchy();
/*     */         else {
/* 240 */           hier = dim.getHierarchyById(hierId);
/*     */         }
/* 242 */         Element[] expPath = CubeViewHandler1_0.this.getPath(path, hier, 
/* 243 */           CubeViewHandler1_0.this.currAxis, 2);
/* 244 */         for (int i = 0; i < repetitions.length; ++i)
/* 245 */           CubeViewHandler1_0.this.currAxis.addExpanded(dim, expPath, repetitions[i]);
/*     */       }
/*     */     });
/* 249 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 251 */         return "view/axis/hidden";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 256 */         String path = attributes.getValue("path");
/* 257 */         String dimId = attributes.getValue("dimension");
/* 258 */         String hierId = attributes.getValue("hierarchy");
/* 259 */         dimId = CubeViewReader.getLeafName(dimId);
/* 260 */         Dimension dim = CubeViewHandler1_0.this.database.getDimensionByName(dimId);
/* 261 */         if (dim == null)
/* 262 */           CubeViewHandler1_0.this
/* 265 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 263 */             "'!!", CubeViewHandler1_0.this.cubeView.getId(), CubeViewHandler1_0.this.cubeView, CubeViewHandler1_0.this.database, 
/* 264 */             dimId, 0, 
/* 265 */             CubeViewHandler1_0.this.currAxis, 4);
/*     */         Hierarchy hier;
/* 268 */         if (hierId == null)
/* 269 */           hier = dim.getDefaultHierarchy();
/*     */         else {
/* 271 */           hier = dim.getHierarchyById(hierId);
/*     */         }
/* 273 */         Element[] hiddenPath = CubeViewHandler1_0.this.getPath(path, 
/* 274 */           hier, CubeViewHandler1_0.this.currAxis, 4);
/* 275 */         CubeViewHandler1_0.this.currAxis.addHidden(dim, hiddenPath);
/*     */       }
/*     */     });
/* 279 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 281 */         return "view/axis/property";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 286 */         String propId = attributes.getValue("id");
/* 287 */         String propVal = attributes.getValue("value");
/* 288 */         CubeViewHandler1_0.this.currAxis.addProperty(propId, propVal);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewHandler1_0
 * JD-Core Version:    0.5.4
 */