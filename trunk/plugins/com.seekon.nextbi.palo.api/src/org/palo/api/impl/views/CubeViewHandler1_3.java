/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import com.tensegrity.palojava.PaloException;
/*     */ import java.io.PrintStream;
/*     */ import org.palo.api.Axis;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.PaloAPIException;
/*     */ import org.palo.api.Subset;
/*     */ import org.palo.api.impl.xml.IPaloStartHandler;
/*     */ import org.palo.api.impl.xml.XMLUtil;
/*     */ import org.palo.api.subsets.Subset2;
/*     */ import org.palo.api.subsets.SubsetHandler;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ class CubeViewHandler1_3 extends CubeViewHandler1_2
/*     */ {
/*     */   CubeViewHandler1_3(Database database)
/*     */   {
/*  69 */     super(database);
/*     */   }
/*     */ 
/*     */   protected void registerStartHandlers() {
/*  73 */     super.registerStartHandlers();
/*     */ 
/*  76 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/*  78 */         return "view";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/*  83 */         CubeViewBuilder viewBuilder = new CubeViewBuilder();
/*  84 */         viewBuilder.setId(attributes.getValue("id"));
/*  85 */         viewBuilder.setName(attributes.getValue("name"));
/*  86 */         viewBuilder.setDescription(XMLUtil.dequoteString(
/*  87 */           attributes.getValue("description")));
/*  88 */         String str = attributes.getValue("hideempty");
/*  89 */         if ((str != null) && (str.equalsIgnoreCase("true")))
/*  90 */           viewBuilder.addProperty("hideEmpty", 
/*  91 */             Boolean.toString(true));
/*  92 */         String cubeId = attributes.getValue("cube");
/*  93 */         Cube srcCube = CubeViewHandler1_3.this.database.getCubeById(cubeId);
/*  94 */         if (srcCube == null) {
/*  95 */           System.err.println("view(" + attributes.getValue("id") + "): unknown source cube '" + cubeId + "' in database '" + CubeViewHandler1_3.this.database.getName() + "'");
/*  96 */           throw new PaloAPIException("CubeView creation failed! No source cube found with id: " + cubeId);
/*     */         }
/*  98 */         if (cubeId != null)
/*  99 */           viewBuilder.setCube(CubeViewHandler1_3.this.database.getCubeById(cubeId));
/* 100 */         CubeViewHandler1_3.this.cubeView = viewBuilder.createView(CubeViewHandler1_3.this);
/*     */ 
/* 102 */         if (CubeViewHandler1_3.this.cubeView == null)
/* 103 */           throw new PaloAPIException("CubeView creation failed!");
/*     */       }
/*     */     });
/* 127 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 129 */         return "view/axis/selected";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes) {
/* 133 */         String element = attributes.getValue("element");
/* 134 */         String dimId = attributes.getValue("dimension");
/* 135 */         String hierId = attributes.getValue("hierarchy");
/*     */ 
/* 138 */         if ((dimId == null) && (hierId != null))
/*     */         {
/* 141 */           String[] allIds = 
/* 142 */             hierId.split("~~~");
/* 143 */           dimId = allIds[0];
/* 144 */           hierId = allIds[1];
/*     */         }
/* 146 */         Dimension dim = CubeViewHandler1_3.this.database.getDimensionById(dimId);
/* 147 */         if (dim == null) {
/* 148 */           CubeViewHandler1_3.this
/* 151 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 149 */             "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, CubeViewHandler1_3.this.database, 
/* 150 */             dimId, 0, 
/* 151 */             CubeViewHandler1_3.this.currAxis, 0);
/*     */         }
/* 153 */         Hierarchy hier = null;
/* 154 */         if ((hierId != null) && (dim != null)) {
/* 155 */           hier = dim.getHierarchyById(hierId);
/* 156 */           if (hier == null)
/* 157 */             CubeViewHandler1_3.this
/* 160 */               .addError("CubeViewReader: unknown hierarchy id '" + hierId + 
/* 158 */               "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, CubeViewHandler1_3.this.database, 
/* 159 */               hierId, 0, 
/* 160 */               CubeViewHandler1_3.this.currAxis, 0);
/*     */         }
/* 162 */         else if (dim != null) {
/* 163 */           hier = CubeViewHandler1_3.this.currAxis.getHierarchy(dim);
/*     */         }
/* 165 */         Element selected = null;
/*     */         try {
/* 167 */           if (hier != null)
/* 168 */             selected = hier.getElementById(element);
/*     */           else
/* 170 */             selected = dim.getDefaultHierarchy().getElementById(element);
/*     */         }
/*     */         catch (PaloException e) {
/* 173 */           e.printStackTrace();
/* 174 */           selected = null;
/*     */         }
/* 176 */         if (selected == null)
/*     */         {
/* 186 */           CubeViewHandler1_3.this
/* 189 */             .addError("CubeViewReader: unknown element id '" + element + 
/* 187 */             "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, dim, element, 
/* 188 */             1, 
/* 189 */             CubeViewHandler1_3.this.currAxis, 1);
/*     */         }
/*     */ 
/* 192 */         if (hier == null)
/* 193 */           CubeViewHandler1_3.this.currAxis.setSelectedElement(dim, selected);
/*     */         else
/* 195 */           CubeViewHandler1_3.this.currAxis.setSelectedElement(hier, selected);
/*     */       }
/*     */     });
/* 200 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 202 */         return "view/axis/active";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 207 */         String subset = attributes.getValue("subset");
/* 208 */         String subset2 = attributes.getValue("subset2");
/* 209 */         String dimId = attributes.getValue("dimension");
/*     */ 
/* 211 */         Dimension dim = CubeViewHandler1_3.this.database.getDimensionById(dimId);
/* 212 */         if (dim == null) {
/* 213 */           CubeViewHandler1_3.this
/* 216 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 214 */             "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, CubeViewHandler1_3.this.database, 
/* 215 */             dimId, 0, 
/* 216 */             CubeViewHandler1_3.this.currAxis, 0);
/*     */         }
/* 218 */         if ((subset2 != null) && (subset2.length() > 0)) {
/* 219 */           SubsetHandler subHandler = dim.getSubsetHandler();
/* 220 */           String attrType = attributes.getValue("type");
/* 221 */           int type = 0;
/*     */           try {
/* 223 */             type = Integer.parseInt(attrType);
/*     */           }
/*     */           catch (NumberFormatException localNumberFormatException)
/*     */           {
/*     */           }
/*     */ 
/* 229 */           Subset2 activeSub2 = null;
/*     */           try {
/* 231 */             activeSub2 = subHandler.getSubset(subset2, type);
/*     */           }
/*     */           catch (Exception localException) {
/*     */           }
/* 235 */           if (activeSub2 == null)
/*     */           {
/* 238 */             ((AxisImpl)CubeViewHandler1_3.this.currAxis).setData(
/* 239 */               "com.tensegrity.palo.unknown_subset_" + dimId, 
/* 240 */               ((dim != null) ? dim.getName() : dimId) + 
/* 241 */               "," + subset2 + "," + type);
/* 242 */             CubeViewHandler1_3.this
/* 245 */               .addError("CubeViewReader: unknown subset id '" + subset + 
/* 243 */               "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, dim, 
/* 244 */               subset, 8, 
/* 245 */               CubeViewHandler1_3.this.currAxis, 8);
/*     */           }
/*     */ 
/* 248 */           CubeViewHandler1_3.this.currAxis.setActiveSubset2(dim, activeSub2);
/*     */         } else {
/* 250 */           Subset activeSub = dim.getSubset(subset);
/* 251 */           if (activeSub == null) {
/* 252 */             CubeViewHandler1_3.this
/* 255 */               .addError("CubeViewReader: unknown subset id '" + subset + 
/* 253 */               "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, dim, 
/* 254 */               subset, 8, 
/* 255 */               CubeViewHandler1_3.this.currAxis, 8);
/*     */           }
/* 257 */           CubeViewHandler1_3.this.currAxis.setActiveSubset(dim, activeSub);
/*     */         }
/*     */       }
/*     */     });
/* 288 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/* 290 */         return "view/axis/hidden";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/* 295 */         String path = attributes.getValue("path");
/* 296 */         String dimId = attributes.getValue("dimension");
/* 297 */         String hierId = attributes.getValue("hierarchy");
/*     */ 
/* 299 */         Dimension dim = CubeViewHandler1_3.this.database.getDimensionById(dimId);
/* 300 */         if (dim == null) {
/* 301 */           CubeViewHandler1_3.this
/* 304 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/* 302 */             "'!!", CubeViewHandler1_3.this.cubeView.getId(), CubeViewHandler1_3.this.cubeView, CubeViewHandler1_3.this.database, 
/* 303 */             dimId, 0, 
/* 304 */             CubeViewHandler1_3.this.currAxis, 4);
/*     */         }
/* 306 */         Hierarchy hier = null;
/* 307 */         if (dim != null) {
/* 308 */           if (hierId == null) {
/* 309 */             hier = CubeViewHandler1_3.this.currAxis.getHierarchy(dim);
/* 310 */             if (hier == null)
/* 311 */               hier = dim.getDefaultHierarchy();
/*     */           }
/*     */           else {
/* 314 */             hier = dim.getHierarchyById(hierId);
/*     */           }
/*     */         }
/* 317 */         Element[] hiddenPath = CubeViewHandler1_3.this.getPathById(
/* 318 */           path, dim, hier, CubeViewHandler1_3.this.currAxis, 4);
/* 319 */         CubeViewHandler1_3.this.currAxis.addHidden(dim, hiddenPath);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewHandler1_3
 * JD-Core Version:    0.5.4
 */