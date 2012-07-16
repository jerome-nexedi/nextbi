/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import org.palo.api.Axis;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Database;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.impl.xml.IPaloStartHandler;
/*     */ import org.palo.api.utils.ElementPath;
/*     */ import org.xml.sax.Attributes;
/*     */ 
/*     */ class CubeViewHandler1_4 extends CubeViewHandler1_3
/*     */ {
/*     */   CubeViewHandler1_4(Database database)
/*     */   {
/*  61 */     super(database);
/*     */   }
/*     */ 
/*     */   protected void registerStartHandlers()
/*     */   {
/*  68 */     super.registerStartHandlers();
/*     */ 
/*  70 */     registerStartHandler(new IPaloStartHandler() {
/*     */       public String getPath() {
/*  72 */         return "view/axis/visible";
/*     */       }
/*     */ 
/*     */       public void startElement(String uri, String localName, String qName, Attributes attributes)
/*     */       {
/*  77 */         String path = attributes.getValue("path");
/*  78 */         String dimId = attributes.getValue("dimension");
/*  79 */         String hierId = attributes.getValue("hierarchy");
/*  80 */         if ((dimId == null) && (hierId != null))
/*     */         {
/*  83 */           String[] allIds = 
/*  84 */             hierId.split("~~~");
/*  85 */           dimId = allIds[0];
/*  86 */           hierId = allIds[1];
/*     */         }
/*  88 */         Dimension dim = CubeViewHandler1_4.this.database.getDimensionById(dimId);
/*  89 */         if (dim == null) {
/*  90 */           CubeViewHandler1_4.this
/*  93 */             .addError("CubeViewReader: unknown dimension id '" + dimId + 
/*  91 */             "'!!", CubeViewHandler1_4.this.cubeView.getId(), CubeViewHandler1_4.this.cubeView, CubeViewHandler1_4.this.database, 
/*  92 */             dimId, 0, 
/*  93 */             CubeViewHandler1_4.this.currAxis, 0);
/*     */         }
/*  95 */         Hierarchy hier = null;
/*  96 */         if ((hierId != null) && (dim != null)) {
/*  97 */           hier = dim.getHierarchyById(hierId);
/*  98 */           if (hier == null)
/*  99 */             CubeViewHandler1_4.this
/* 102 */               .addError("CubeViewReader: unknown hierarchy id '" + hierId + 
/* 100 */               "'!!", CubeViewHandler1_4.this.cubeView.getId(), CubeViewHandler1_4.this.cubeView, CubeViewHandler1_4.this.database, 
/* 101 */               hierId, 0, 
/* 102 */               CubeViewHandler1_4.this.currAxis, 0);
/*     */         }
/* 104 */         else if (dim != null) {
/* 105 */           hier = CubeViewHandler1_4.this.currAxis.getHierarchy(dim);
/*     */         }
/*     */         ElementPath elPath;
/* 108 */         if (hier == null)
/* 109 */           elPath = ElementPath.restore(new Hierarchy[] { dim.getDefaultHierarchy() }, path);
/*     */         else {
/* 111 */           elPath = ElementPath.restore(new Hierarchy[] { hier }, path);
/*     */         }
/* 113 */         CubeViewHandler1_4.this.currAxis.addVisible(elPath);
/*     */       }
/*     */     });
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewHandler1_4
 * JD-Core Version:    0.5.4
 */