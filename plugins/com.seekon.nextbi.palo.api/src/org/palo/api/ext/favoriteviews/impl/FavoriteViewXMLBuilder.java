/*     */ package org.palo.api.ext.favoriteviews.impl;
/*     */ 
/*     */ import org.palo.api.Connection;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.CubeView;
/*     */ import org.palo.api.ext.favoriteviews.FavoriteViewTreeNode;
/*     */ import org.palo.api.ext.favoriteviews.FavoriteViewsFolder;
/*     */ 
/*     */ public class FavoriteViewXMLBuilder
/*     */ {
/*     */   private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
/*     */   private StringBuffer result;
/*     */   private int indent;
/*     */   private Connection connection;
/*     */ 
/*     */   public FavoriteViewXMLBuilder(Connection con)
/*     */   {
/*  84 */     if (con == null) {
/*  85 */       throw new NullPointerException("Connection must not be null.");
/*     */     }
/*  87 */     this.result = new StringBuffer("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
/*  88 */     this.indent = 0;
/*  89 */     this.connection = con;
/*     */   }
/*     */ 
/*     */   public void preOrderTraversal(FavoriteViewTreeNode node)
/*     */   {
/* 100 */     visitNodeBegin(node);
/* 101 */     FavoriteViewTreeNode[] children = node.getChildren();
/* 102 */     if (children == null) {
/* 103 */       visitNodeEnd(node);
/* 104 */       return;
/*     */     }
/* 106 */     for (int i = 0; i < children.length; ++i) {
/* 107 */       preOrderTraversal(children[i]);
/*     */     }
/* 109 */     visitNodeEnd(node);
/*     */   }
/*     */ 
/*     */   protected void visitNodeBegin(FavoriteViewTreeNode node)
/*     */   {
/* 122 */     if (node.getUserObject() instanceof FavoriteViewsFolder) {
/* 123 */       FavoriteViewsFolder folder = 
/* 124 */         (FavoriteViewsFolder)node.getUserObject();
/* 125 */       if (folder.getName().equals("Invisible Root")) {
/* 126 */         return;
/*     */       }
/* 128 */       doIndent();
/* 129 */       this.result.append("<folder name=\"" + 
/* 130 */         ((FavoriteViewsFolder)node.getUserObject()).getName() + "\" position=\"" + 
/* 131 */         ((FavoriteViewsFolder)node.getUserObject()).getPosition() + "\">\n");
/* 132 */       this.indent += 2;
/* 133 */     } else if (node.getUserObject() instanceof FavoriteViewImpl) {
/* 134 */       doIndent();
/* 135 */       FavoriteViewImpl bm = (FavoriteViewImpl)node.getUserObject();
/* 136 */       CubeView view = bm.getCubeView();
/* 137 */       if (view != null) {
/* 138 */         if (this.connection != null) {
/* 139 */           Connection con = bm.getConnection();
/* 140 */           if ((!con.getServer().equals(this.connection.getServer())) || 
/* 141 */             (!con.getService().equals(this.connection.getService()))) {
/* 142 */             return;
/*     */           }
/*     */         }
/* 145 */         Cube cube = view.getCube();
/* 146 */         this.result.append("<bookmark name=\"" + 
/* 147 */           bm.getName() + "\" position=\"" + 
/* 148 */           bm.getPosition() + "\" viewId=\"" + 
/* 149 */           view.getId() + "\" databaseID=\"" + 
/* 150 */           bm.getDatabaseId() + "\" cubeID=\"" + 
/* 151 */           cube.getId() + "\"/>\n");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void visitNodeEnd(FavoriteViewTreeNode node)
/*     */   {
/* 164 */     if (node.getUserObject() instanceof FavoriteViewsFolder) {
/* 165 */       FavoriteViewsFolder folder = (FavoriteViewsFolder)node.getUserObject();
/* 166 */       if (folder.getName().equals("Invisible Root")) {
/* 167 */         return;
/*     */       }
/* 169 */       this.indent -= 2;
/* 170 */       doIndent();
/* 171 */       this.result.append("</folder>\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private void doIndent()
/*     */   {
/* 180 */     for (int i = 0; i < this.indent; ++i)
/* 181 */       this.result.append(" ");
/*     */   }
/*     */ 
/*     */   public String getResult()
/*     */   {
/* 190 */     return this.result.toString();
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.ext.favoriteviews.impl.FavoriteViewXMLBuilder
 * JD-Core Version:    0.5.4
 */