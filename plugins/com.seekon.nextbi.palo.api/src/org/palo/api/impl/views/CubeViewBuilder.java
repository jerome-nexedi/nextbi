/*     */ package org.palo.api.impl.views;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.Property;
/*     */ import org.palo.api.impl.PersistenceErrorImpl;
/*     */ import org.palo.api.persistence.PersistenceError;
/*     */ 
/*     */ class CubeViewBuilder
/*     */ {
/*     */   private String id;
/*     */   private String name;
/*  59 */   private HashMap properties = new HashMap();
/*     */   private Cube cube;
/*     */   private String description;
/*     */ 
/*     */   final synchronized CubeView createView(CubeViewHandler viewHandler)
/*     */   {
/*  66 */     if ((this.id == null) || (this.name == null) || (this.cube == null)) {
/*  67 */       PersistenceError error = new PersistenceErrorImpl(
/*  68 */         "Could not create cube view, insufficient information", this.id, 
/*  69 */         this.cube, null, null, 32, null, 
/*  70 */         0);
/*  71 */       viewHandler.addError(error);
/*  72 */       return null;
/*     */     }
/*     */ 
/*  76 */     int n = this.properties.size();
/*  77 */     Object[] params = new Object[3 + n];
/*  78 */     params[0] = this.id;
/*  79 */     params[1] = this.name;
/*  80 */     params[2] = this.cube;
/*  81 */     Property[] props = (Property[])null;
/*  82 */     if (n > 0) {
/*  83 */       props = new Property[n];
/*  84 */       String[] keys = (String[])this.properties.keySet().toArray(
/*  85 */         new String[this.properties.size()]);
/*  86 */       for (int i = 0; i < n; ++i) {
/*  87 */         props[i] = 
/*  88 */           new Property(keys[i], 
/*  88 */           (String)this.properties.get(keys[i]));
/*  89 */         params[(3 + i)] = props[i];
/*     */       }
/*     */     }
/*  92 */     CubeViewManager creator = CubeViewManager.getInstance();
/*  93 */     CubeView view = (CubeView)creator.create(CubeView.class, params);
/*     */ 
/*  95 */     ((CubeViewImpl)view).reset();
/*     */ 
/*  97 */     if (this.description != null)
/*  98 */       view.setDescription(this.description);
/*  99 */     return view;
/*     */   }
/*     */ 
/*     */   final synchronized void setCube(Cube cube) {
/* 103 */     this.cube = cube;
/*     */   }
/*     */   final synchronized void setDescription(String description) {
/* 106 */     this.description = description;
/*     */   }
/*     */   final synchronized void setId(String id) {
/* 109 */     this.id = id;
/*     */   }
/*     */   final synchronized void setName(String name) {
/* 112 */     this.name = name;
/*     */   }
/*     */   final synchronized void addProperty(String id, String value) {
/* 115 */     this.properties.put(id, value);
/*     */   }
/*     */   final synchronized void addProperty(Property property) {
/* 118 */     if (property == null) {
/* 119 */       return;
/*     */     }
/* 121 */     this.properties.put(property.getId(), property.getValue());
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.views.CubeViewBuilder
 * JD-Core Version:    0.5.4
 */