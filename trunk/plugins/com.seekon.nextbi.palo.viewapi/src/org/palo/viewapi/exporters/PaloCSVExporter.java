/*     */ package org.palo.viewapi.exporters;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.viewapi.Axis;
/*     */ import org.palo.viewapi.AxisHierarchy;
/*     */ import org.palo.viewapi.CubeView;
/*     */ import org.palo.viewapi.uimodels.axis.AxisFlatModel;
/*     */ import org.palo.viewapi.uimodels.axis.AxisItem;
/*     */ 
/*     */ public class PaloCSVExporter
/*     */   implements CubeViewExporter
/*     */ {
/*  60 */   private final StringBuffer csv = new StringBuffer();
/*     */ 
/*  66 */   private final char separator = '\t';
/*     */ 
/*     */   public final String getCSV()
/*     */   {
/*  73 */     return this.csv.toString();
/*     */   }
/*     */ 
/*     */   public final void export(CubeView view)
/*     */   {
/*  80 */     Cube cube = view.getCube();
/*     */ 
/*  82 */     Dimension[] cubeDims = cube.getDimensions();
/*  83 */     HashMap hierIndex = 
/*  84 */       new HashMap();
/*  85 */     for (int i = 0; i < cubeDims.length; ++i) {
/*  86 */       hierIndex.put(cubeDims[i].getDefaultHierarchy(), Integer.valueOf(i));
/*     */     }
/*  88 */     Element[] coord = new Element[cubeDims.length];
/*     */ 
/*  90 */     Axis selAxis = view.getAxis("selected");
/*  91 */     for (int i = 0; i < cubeDims.length; ++i) {
/*  92 */       AxisHierarchy axisHierarchy = selAxis.getAxisHierarchy(
/*  93 */         cubeDims[i].getDefaultHierarchy());
/*  94 */       if (axisHierarchy != null) {
/*  95 */         Element[] selElements = axisHierarchy.getSelectedElements();
/*     */ 
/*  97 */         if ((selElements != null) && (selElements.length > 0)) {
/*  98 */           coord[i] = selElements[0];
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 104 */     Object rr = view.getPropertyValue("reverseVertical");
/* 105 */     AxisFlatModel rows = 
/* 106 */       new AxisFlatModel(view.getAxis("rows"), 1, (rr != null) && (Boolean.parseBoolean(rr.toString())));
/* 107 */     AxisItem[][] rowsModel = rows.getModel();
/* 108 */     Object rc = view.getPropertyValue("reverseHorizontal");
/* 109 */     AxisFlatModel cols = new AxisFlatModel(view.getAxis("cols"), (rc != null) && (Boolean.parseBoolean(rc.toString())));
/* 110 */     AxisItem[][] colsModel = cols.getModel();
/*     */ 
/* 113 */     int colLeaf = colsModel.length - 1;
/*     */ 
/* 115 */     int cells = rowsModel.length * colsModel[colLeaf].length;
/* 116 */     Element[][] cellCoords = new Element[cells][];
/*     */ 
/* 118 */     int cellIndex = 0;
/* 119 */     int rowLeaf = rowsModel[0].length - 1;
/* 120 */     for (int r = 0; r < rowsModel.length; ++r) {
/* 121 */       for (int c = 0; c < colsModel[colLeaf].length; ++c) {
/* 122 */         cellCoords[cellIndex] = ((Element[])coord.clone());
/*     */ 
/* 124 */         fill(cellCoords[cellIndex], rowsModel[r][rowLeaf], hierIndex);
/*     */ 
/* 126 */         fill(cellCoords[cellIndex], colsModel[colLeaf][c], hierIndex);
/* 127 */         ++cellIndex;
/*     */       }
/*     */     }
/* 130 */     Object[] cellValues = cube.getDataBulk(cellCoords);
/*     */ 
/* 133 */     dumpSelectedElements(coord);
/* 134 */     dumpColumns(colsModel, rowLeaf + 1);
/* 135 */     dumpRowsAndCells(rowsModel, colsModel[colLeaf].length, cellValues);
/*     */   }
/*     */ 
/*     */   private final void fill(Element[] coord, AxisItem item, HashMap<Hierarchy, Integer> hierIndex)
/*     */   {
/* 148 */     if (item == null) {
/* 149 */       return;
/*     */     }
/*     */ 
/* 153 */     coord[((Integer)hierIndex.get(item.getHierarchy())).intValue()] = item.getElement();
/* 154 */     fill(coord, item.getParentInPrevHierarchy(), hierIndex);
/*     */   }
/*     */ 
/*     */   private final void dumpSelectedElements(Element[] coord)
/*     */   {
/* 164 */     for (int i = 0; i < coord.length; ++i) {
/* 165 */       if (coord[i] != null) {
/* 166 */         this.csv.append(coord[i].getDimension().getName());
/* 167 */         this.csv.append('\t');
/*     */       }
/*     */     }
/* 170 */     if (this.csv.length() < 1) {
/* 171 */       return;
/*     */     }
/* 173 */     this.csv.replace(this.csv.length() - 1, this.csv.length(), "\n");
/*     */ 
/* 175 */     for (int i = 0; i < coord.length; ++i) {
/* 176 */       if (coord[i] != null) {
/* 177 */         this.csv.append(coord[i].getName());
/* 178 */         this.csv.append('\t');
/*     */       }
/*     */     }
/*     */ 
/* 182 */     this.csv.replace(this.csv.length() - 1, this.csv.length(), "\n");
/* 183 */     this.csv.append("\n\n");
/*     */   }
/*     */ 
/*     */   private final void dumpColumns(AxisItem[][] colModel, int rowDimCount)
/*     */   {
/* 193 */     for (int i = 0; i < colModel.length; ++i) {
/* 194 */       for (int k = 0; k < rowDimCount; ++k)
/* 195 */         this.csv.append('\t');
/* 196 */       for (int j = 0; j < colModel[i].length; ++j) {
/* 197 */         AxisItem item = colModel[i][j];
/* 198 */         this.csv.append(item.getName());
/* 199 */         this.csv.append('\t');
/*     */       }
/*     */ 
/* 202 */       this.csv.replace(this.csv.length() - 1, this.csv.length(), "\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void dumpRowsAndCells(AxisItem[][] rowModel, int colLeafCount, Object[] cellValues)
/*     */   {
/* 220 */     for (int i = 0; i < rowModel.length; ++i) {
/* 221 */       for (int j = 0; j < rowModel[i].length; ++j) {
/* 222 */         AxisItem item = rowModel[i][j];
/* 223 */         this.csv.append(item.getName());
/* 224 */         this.csv.append('\t');
/*     */       }
/*     */ 
/* 227 */       int start = i * colLeafCount;
/* 228 */       int end = start + colLeafCount;
/* 229 */       for (int c = start; c < end; ++c) {
/* 230 */         this.csv.append(cellValues[c].toString());
/* 231 */         this.csv.append('\t');
/*     */       }
/*     */ 
/* 234 */       this.csv.replace(this.csv.length() - 1, this.csv.length(), "\n");
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.exporters.PaloCSVExporter
 * JD-Core Version:    0.5.4
 */