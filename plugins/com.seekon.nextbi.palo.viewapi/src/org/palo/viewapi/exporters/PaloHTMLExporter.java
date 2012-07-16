/*     */ package org.palo.viewapi.exporters;
/*     */ 
/*     */ import java.text.DecimalFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import org.palo.api.Cell;
/*     */ import org.palo.api.Cube;
/*     */ import org.palo.api.Dimension;
/*     */ import org.palo.api.Element;
/*     */ import org.palo.api.Hierarchy;
/*     */ import org.palo.api.ext.ui.ColorDescriptor;
/*     */ import org.palo.api.ext.ui.FontDescriptor;
/*     */ import org.palo.viewapi.Axis;
/*     */ import org.palo.viewapi.AxisHierarchy;
/*     */ import org.palo.viewapi.CubeView;
/*     */ import org.palo.viewapi.uimodels.axis.AxisFlatModel;
/*     */ import org.palo.viewapi.uimodels.axis.AxisItem;
/*     */ import org.palo.viewapi.uimodels.formats.Format;
/*     */ import org.palo.viewapi.uimodels.formats.FormatRangeInfo;
/*     */ import org.palo.viewapi.uimodels.formats.TrafficLightData;
/*     */ 
/*     */ public class PaloHTMLExporter
/*     */   implements CubeViewExporter
/*     */ {
/*  71 */   private static final DecimalFormat DEFAULT_FORMATTER = new DecimalFormat("#,##0.00");
/*     */ 
/*  76 */   private DecimalFormat customFormatter = new DecimalFormat("#,##0.00");
/*     */   private Cell[] cellValues;
/* 147 */   private int cellPosition = 0;
/*     */ 
/* 152 */   private int columnCount = 0;
/*     */ 
/* 157 */   private int rowCount = 0;
/*     */   private IndexRangeInfo[] indexRanges;
/*     */   private HashMap<Dimension, ArrayList<LevelRangeInfo>> allDimensionLevels;
/* 173 */   private HashMap<String, Element[]> indexCellMap = new HashMap();
/*     */ 
/* 178 */   private final StringBuffer html = new StringBuffer();
/*     */ 
/*     */   public final String getHTML()
/*     */   {
/* 185 */     return this.html.toString();
/*     */   }
/*     */ 
/*     */   private final String getString(Element[] coord)
/*     */   {
/* 195 */     StringBuffer b = new StringBuffer();
/* 196 */     for (Element e : coord) {
/* 197 */       b.append(e.getId() + ",");
/*     */     }
/* 199 */     return b.toString();
/*     */   }
/*     */ 
/*     */   public int traverseVisible(AxisItem item) {
/* 203 */     ArrayList result = new ArrayList();
/* 204 */     result.add(Integer.valueOf(0));
/* 205 */     traverseVisible(item, null, null, result, true);
/* 206 */     return ((Integer)result.get(0)).intValue();
/*     */   }
/*     */ 
/*     */   public void traverseVisible(AxisItem item, AxisItem parent, AxisItem parentInPrevHierarchy, ArrayList<Integer> result, boolean start) {
/* 210 */     if (!item.hasRootsInNextHierarchy()) {
/* 211 */       result.set(0, Integer.valueOf(((Integer)result.get(0)).intValue() + 1));
/*     */     }
/* 213 */     for (AxisItem rootInNextHierarchy : item.getRootsInNextHierarchy()) {
/* 214 */       traverseVisible(rootInNextHierarchy, null, item, result, false);
/*     */     }
/* 216 */     if ((start) || 
/* 217 */       (!item.hasChildren()) || (!item.hasState(2))) return;
/* 218 */     for (AxisItem child : item.getChildren())
/* 219 */       traverseVisible(child, item, parentInPrevHierarchy, result, false);
/*     */   }
/*     */ 
/*     */   private final String indentItem(AxisItem it)
/*     */   {
/* 226 */     Element e = it.getElement();
/* 227 */     int indentDepth = e.getDepth();
/* 228 */     if (indentDepth == 0) {
/* 229 */       return e.getName();
/*     */     }
/* 231 */     StringBuffer sb = new StringBuffer();
/* 232 */     for (int i = 0; i < indentDepth; ++i) {
/* 233 */       sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
/*     */     }
/* 235 */     sb.append(it.getName());
/* 236 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   private final String drawRows(AxisItem[][] rowModel, AxisItem[][] colModel, ArrayList<Integer> emptyColumns, ArrayList<Integer> emptyRows) {
/* 240 */     StringBuffer result = new StringBuffer();
/* 241 */     HashMap<Integer,Integer> isEmpty = new HashMap();
/* 242 */     for (int i = 0; i < rowModel.length; ++i)
/* 243 */       if (emptyRows.contains(Integer.valueOf(i))) {
/* 244 */         for (Integer j : isEmpty.keySet()) {
/* 245 */           int val = ((Integer)isEmpty.get(j)).intValue();
/* 246 */           if (val != 0) {
/* 247 */             isEmpty.put(j, Integer.valueOf(val - 1));
/*     */           }
/*     */         }
/* 250 */         this.cellPosition += this.columnCount;
/*     */       }
/*     */       else {
/* 253 */         result.append("<tr>");
/* 254 */         for (int j = 0; j < rowModel[i].length; ++j) {
/* 255 */           AxisItem item = rowModel[i][j];
/* 256 */           if (!isEmpty.containsKey(Integer.valueOf(j))) {
/* 257 */             isEmpty.put(Integer.valueOf(j), Integer.valueOf(0));
/*     */           }
/* 259 */           if (((Integer)isEmpty.get(Integer.valueOf(j))).intValue() != 0) {
/* 260 */             isEmpty.put(Integer.valueOf(j), Integer.valueOf(((Integer)isEmpty.get(Integer.valueOf(j))).intValue() - 1));
/* 261 */             result.append("<td>&nbsp;</td>");
/*     */           } else {
/* 263 */             result.append("<td>" + indentItem(item) + "</td>");
/* 264 */             int width = traverseVisible(item);
/* 265 */             if (width > 0) {
/* 266 */               --width;
/*     */             }
/* 268 */             isEmpty.put(Integer.valueOf(j), Integer.valueOf(width));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 273 */         int start = i * this.columnCount;
/* 274 */         int end = start + this.columnCount;
/* 275 */         for (int c = start; c < end; ++c)
/* 276 */           if (emptyColumns.contains(Integer.valueOf(c - start))) {
/* 277 */             this.cellPosition += 1;
/*     */           }
/*     */           else {
/* 280 */             String formatted = this.cellValues[(this.cellPosition++)].getValue().toString();
/* 281 */             Format f = getFormat(this.rowCount, i, this.indexRanges, 
/* 282 */               (Element[])this.indexCellMap.get(this.rowCount + ";" + i));
/* 283 */             formatted = applyFormat(f, formatted);
/* 284 */             result.append(formatted);
/*     */           }
/* 286 */         result.append("</tr>\r\n");
/*     */       }
/* 288 */     return result.toString();
/*     */   }
/*     */ 
/*     */   private int getMaxDimDepth(AxisItem[] roots)
/*     */   {
/* 297 */     int localMax = 0;
/* 298 */     for (AxisItem root : roots) {
/* 299 */       int sum = 1 + getMaxDimDepth(root.getRootsInNextHierarchy());
/* 300 */       if (sum > localMax) {
/* 301 */         localMax = sum;
/*     */       }
/*     */     }
/* 304 */     return localMax;
/*     */   }
/*     */ 
/*     */   public final void export(CubeView view)
/*     */   {
/* 311 */     Cube cube = view.getCube();
/*     */ 
/* 313 */     Dimension[] cubeDims = cube.getDimensions();
/* 314 */     HashMap hierIndex = 
/* 315 */       new HashMap();
/* 316 */     HashMap cellIndexMap = 
/* 317 */       new HashMap();
/* 318 */     for (int i = 0; i < cubeDims.length; ++i) {
/* 319 */       hierIndex.put(cubeDims[i].getDefaultHierarchy(), Integer.valueOf(i));
/*     */     }
/*     */ 
/* 322 */     Element[] coord = new Element[cubeDims.length];
/*     */ 
/* 324 */     Axis selAxis = view.getAxis("selected");
/*     */     Element[] selElements;
/* 325 */     for (int i = 0; i < cubeDims.length; ++i) {
/* 326 */       AxisHierarchy axisHierarchy = selAxis.getAxisHierarchy(
/* 327 */         cubeDims[i].getDefaultHierarchy());
/* 328 */       if (axisHierarchy != null) {
/* 329 */         selElements = axisHierarchy.getSelectedElements();
/*     */ 
/* 331 */         if ((selElements != null) && (selElements.length > 0)) {
/* 332 */           coord[i] = selElements[0];
/*     */         }
/*     */       }
/*     */     }
/* 336 */     this.html.append("<html><body>\r\n");
/* 337 */     AxisHierarchy[] axisHierarchies = selAxis.getAxisHierarchies();
/* 338 */     if (axisHierarchies.length != 0) {
/* 339 */       this.html.append("<table border=\"1\"><tr>");
/*     */       
/* 340 */       int arrayOfElement1 = axisHierarchies.length; 
							for (int selElements1 = 0; selElements1 < arrayOfElement1; ++selElements1) { 
					AxisHierarchy ah = axisHierarchies[selElements1];
/* 341 */         this.html.append("<td>" + ah.getHierarchy().getName() + "</td>"); }
/*     */ 
/* 343 */       this.html.append("</tr>\r\n<tr>");
/* 344 */       int arrayOfElement2 = axisHierarchies.length; 
for (int selElements1 = 0; selElements1 < arrayOfElement2; ++selElements1) { 
	AxisHierarchy ah = axisHierarchies[selElements1];
/* 345 */         this.html.append("<td>" + ah.getSelectedElements()[0].getName() + "</td>"); }
/*     */ 
/* 347 */       this.html.append("</tr></table>\r\n");
/*     */     }
/*     */ 
/* 350 */     Object rc = view.getPropertyValue("reverseHorizontal");
/* 351 */     AxisFlatModel cols = new AxisFlatModel(view.getAxis("cols"), (rc != null) && (Boolean.parseBoolean(rc.toString())));
/* 352 */     AxisItem[][] colModel = cols.getModel();
/* 353 */     if ((colModel == null) || (colModel.length == 0)) {
/* 354 */       this.html.append("</body></html>\r\n");
/* 355 */       return;
/*     */     }
/* 357 */     Object rr = view.getPropertyValue("reverseVertical");
/* 358 */     AxisFlatModel rows = new AxisFlatModel(view.getAxis("rows"), 1, (rr != null) && (Boolean.parseBoolean(rr.toString())));
/* 359 */     AxisItem[][] rowModel = rows.getModel();
/*     */ 
/* 361 */     if ((rowModel == null) || (rowModel.length == 0)) {
/* 362 */       this.html.append("<table border=\"1\">");
/* 363 */       int emptyCellRowCount = getMaxDimDepth(rowModel[0]);
/*     */ 
/* 365 */       for (AxisItem[] items : colModel) {
/* 366 */         this.html.append("<tr>");
/* 367 */         for (int i = 0; i < emptyCellRowCount; ++i) {
/* 368 */           this.html.append("<td>&nbsp;</td>");
/*     */         }
/* 370 */         for (AxisItem item : items) {
/* 371 */           this.html.append("<td>" + item.getElement().getName() + "</td>");
/* 372 */           int emptyCellCount = traverseVisible(item) - 1;
/*     */ 
/* 374 */           for (int i = 0; i < emptyCellCount; ++i) {
/* 375 */             this.html.append("<td>&nbsp;</td>");
/*     */           }
/*     */         }
/* 378 */         this.html.append("</tr>\r\n");
/*     */       }
/*     */ 
/* 381 */       this.html.append("</table>\r\n</body></html>\r\n");
/* 382 */       return;
/*     */     }
/*     */ 
/* 385 */     int cellIndex = 0;
/* 386 */     int rowLeaf = rowModel[0].length - 1;
/* 387 */     int colLeaf = colModel.length - 1;
/* 388 */     int cells = rowModel.length * colModel[colLeaf].length;
/* 389 */     Element[][] cellCoords = new Element[cells][];
/*     */ 
/* 391 */     for (int r = 0; r < rowModel.length; ++r)
/*     */     {
/* 392 */       for (int l = 0; l < colModel[colLeaf].length; ++l) {
/* 393 */         cellCoords[cellIndex] = ((Element[])coord.clone());
/*     */ 
/* 395 */         fill(cellCoords[cellIndex], rowModel[r][rowLeaf], hierIndex);
/*     */ 
/* 397 */         fill(cellCoords[cellIndex], colModel[colLeaf][l], hierIndex);
/* 398 */         cellIndexMap.put(getString(cellCoords[cellIndex]), 
/* 399 */           new Integer[] { Integer.valueOf(r), Integer.valueOf(l) });
/* 400 */         this.indexCellMap.put(r + ";" + l, cellCoords[cellIndex]);
/* 401 */         ++cellIndex;
/*     */       }
/*     */     }
/*     */ 
/* 405 */     this.cellValues = cube.getCells(cellCoords);
/* 406 */     Object hz = view.getPropertyValue("hideEmpty");
/* 407 */     ArrayList emptyColumns = new ArrayList();
/* 408 */     ArrayList emptyRows = new ArrayList();
/* 409 */     if ((hz != null) && (Boolean.parseBoolean(hz.toString()))) {
/* 410 */       cellIndex = 0;
/* 411 */       boolean[] hasRowValue = new boolean[colModel[colLeaf].length];
/* 412 */       for (int c = 0; c < colModel[colLeaf].length; ++c) {
/* 413 */         hasRowValue[c] = false;
/*     */       }
/* 415 */       for (int r = 0; r < rowModel.length; ++r) {
/* 416 */         boolean hasColValues = false;
/* 417 */         for (int c = 0; c < colModel[colLeaf].length; ++c) {
/* 418 */           if (!this.cellValues[cellIndex].isEmpty()) {
/* 419 */             hasColValues = true;
/* 420 */             hasRowValue[c] = true;
/*     */           }
/* 422 */           ++cellIndex;
/*     */         }
/* 424 */         if (!hasColValues) {
/* 425 */           emptyRows.add(Integer.valueOf(r));
/*     */         }
/*     */       }
/* 428 */       for (int c = 0; c < colModel[colLeaf].length; ++c)
/* 429 */         if (hasRowValue[c] == false)
/* 430 */           emptyColumns.add(Integer.valueOf(c));
/*     */     }

/* 435 */     int i2 = 0;
/*     */     Format[] arrayOfFormat1;
/* 436 */     int c = (arrayOfFormat1 = view.getFormats()).length; 
for (int hasColValues = 0; hasColValues < c; ++hasColValues) { 
	Format f = arrayOfFormat1[hasColValues];
/* 437 */       i2 += f.getRangeCount(); }
/*     */ 
/* 439 */     this.allDimensionLevels = new HashMap();
/* 440 */     ArrayList allCoordinates = new ArrayList();
/* 441 */     for (Format f : view.getFormats()) {
/* 442 */       for (FormatRangeInfo r : f.getRanges()) {
/* 443 */         if (r.getCells() != null) {
/* 444 */           for (Element[] e : r.getCells()) {
/* 445 */             Integer[] rcCoord = (Integer[])cellIndexMap.get(getString(e));
/* 446 */             if (rcCoord != null)
/* 447 */               allCoordinates.add(new IndexRangeInfo(f, rcCoord[0].intValue(), rcCoord[1].intValue()));
/*     */           }
/*     */         }
/*     */         else {
/* 451 */           for (Dimension d : r.getDimensions())
/*     */           {
/* 453 */             ArrayList l = (ArrayList)this.allDimensionLevels.get(d);
/* 454 */             if (l == null) {
/* 455 */               l = new ArrayList();
/*     */             }
/* 457 */             l.add(new LevelRangeInfo(f, r.getLevel(d)));
/* 458 */             this.allDimensionLevels.put(d, l);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 463 */     this.indexRanges = ((IndexRangeInfo[])allCoordinates.toArray(new IndexRangeInfo[0]));
/*     */ 
/* 466 */     this.cellPosition = 0;
/* 467 */     this.columnCount = colModel[colLeaf].length;
/*     */ 
/* 469 */     this.html.append(drawCols(rowModel, colModel, emptyColumns));
/* 470 */     this.html.append(drawRows(rowModel, colModel, emptyColumns, emptyRows));
/* 471 */     this.html.append("</table>\r\n</body></html>\r\n");
/*     */   }
/*     */ 
/*     */   private final String drawCols(AxisItem[][] rowModel, AxisItem[][] colModel, ArrayList<Integer> emptyColumns) {
/* 475 */     int colLeaf = colModel.length - 1;
/*     */ 
/* 477 */     StringBuffer result = new StringBuffer();
/* 478 */     result.append("<table border=\"1\">");
/*     */ 
/* 480 */     int emptyCellRowCount = getMaxDimDepth(rowModel[0]);
/* 481 */     StringBuffer[] header = new StringBuffer[colModel.length];
/* 482 */     for (int i = 0; i < colModel.length; ++i) {
/* 483 */       header[i] = new StringBuffer();
/* 484 */       header[i].append("<tr>");
/* 485 */       for (int j = 0; j < emptyCellRowCount; ++j) {
/* 486 */         header[i].append("<td>&nbsp;</td>");
/*     */       }
/*     */     }
/*     */ 
/* 490 */     HashMap<Integer, Integer> isEmpty = new HashMap();
/* 491 */     for (int c = 0; c < colModel[colLeaf].length; ++c) {
/* 492 */       if (emptyColumns.contains(Integer.valueOf(c))) {
/* 493 */         for (Integer i : isEmpty.keySet()) {
/* 494 */           int val = ((Integer)isEmpty.get(i)).intValue();
/* 495 */           if (val != 0)
/* 496 */             isEmpty.put(i, Integer.valueOf(val - 1));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 501 */         AxisItem origItem = colModel[colLeaf][c];

/* 502 */         for (int i = 0; i < colModel.length; ++i)
/*     */         {
/* 504 */           AxisItem item = origItem;
/* 505 */           int counter = colLeaf;
/* 506 */           while (i < counter) {
/* 507 */             item = item.getParentInPrevHierarchy();
/* 508 */             --counter;
/*     */           }
/* 510 */           if (!isEmpty.containsKey(Integer.valueOf(i))) {
/* 511 */             isEmpty.put(Integer.valueOf(i), Integer.valueOf(0));
/*     */           }
/* 513 */           if (((Integer)isEmpty.get(Integer.valueOf(i))).intValue() != 0) {
/* 514 */             isEmpty.put(Integer.valueOf(i), Integer.valueOf(((Integer)isEmpty.get(Integer.valueOf(i))).intValue() - 1));
/* 515 */             header[i].append("<td>&nbsp;</td>");
/*     */           } else {
/* 517 */             header[i].append("<td>" + item.getName() + "</td>");
/* 518 */             int width = traverseVisible(item);
/* 519 */             if (width > 0) {
/* 520 */               --width;
/*     */             }
/* 522 */             isEmpty.put(Integer.valueOf(i), Integer.valueOf(width));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 527 */     for (int i = 0; i < colModel.length; ++i) {
/* 528 */       result.append(header[i] + "</tr>\r\n");
/*     */     }
/*     */ 
/* 531 */     return result.toString();
/*     */   }
/*     */ 
/*     */   private final String applyNumberFormat(Format f, String text)
/*     */   {
/* 543 */     if ((f == null) || (f.getNumberFormat() == null)) {
/*     */       try {
/* 545 */         Double d = Double.valueOf(Double.parseDouble(text));
/* 546 */         return DEFAULT_FORMATTER.format(d);
/*     */       } catch (NumberFormatException e) {
/* 548 */         return text;
/*     */       }
/*     */     }
/* 551 */     this.customFormatter.applyPattern(f.getNumberFormat());
/*     */     try {
/* 553 */       Double d = Double.valueOf(Double.parseDouble(text));
/* 554 */       return this.customFormatter.format(d); } catch (NumberFormatException e) {
/*     */     }
/* 556 */     return text;
/*     */   }
/*     */ 
/*     */   private final String getHTMLColor(ColorDescriptor desc)
/*     */   {
/* 569 */     String red = Integer.toHexString(desc.getRed());
/* 570 */     String green = Integer.toHexString(desc.getGreen());
/* 571 */     String blue = Integer.toHexString(desc.getBlue());
/* 572 */     if (red.length() < 2) red = "0" + red;
/* 573 */     if (green.length() < 2) green = "0" + green;
/* 574 */     if (blue.length() < 2) blue = "0" + blue;
/* 575 */     return "#" + red + green + blue;
/*     */   }
/*     */ 
/*     */   private final String applyFontAndColor(FontDescriptor fd, ColorDescriptor fg, String text)
/*     */   {
/* 587 */     if (text.equals("&nbsp;")) {
/* 588 */       return text;
/*     */     }
/* 590 */     String colorString = "";
/* 591 */     String fontFaceString = "";
/* 592 */     if (fg != null) {
/* 593 */       colorString = "color=\"" + getHTMLColor(fg) + "\"";
/*     */     }
/* 595 */     if (fd != null) {
/* 596 */       fontFaceString = "face=\"" + fd.getName() + "\"";
/* 597 */       if (fd.isBold()) {
/* 598 */         text = "<b>" + text + "</b>";
/*     */       }
/* 600 */       if (fd.isItalic()) {
/* 601 */         text = "<i>" + text + "</i>";
/*     */       }
/* 603 */       if (fd.isUnderlined()) {
/* 604 */         text = "<u>" + text + "</u>";
/*     */       }
/*     */     }
/* 607 */     if ((fontFaceString.length() != 0) || (colorString.length() != 0)) {
/* 608 */       text = "<font " + fontFaceString + " " + colorString + ">" + text + 
/* 609 */         "</font>";
/*     */     }
/* 611 */     return text;
/*     */   }
/*     */ 
/*     */   private final Object[] applyTrafficLightData(TrafficLightData traffic, String text, String originalText)
/*     */   {
/*     */     try
/*     */     {
/* 628 */       Double d = Double.valueOf(Double.parseDouble(originalText));
/* 629 */       for (int i = 0; i < traffic.getSize(); ++i)
/* 630 */         if ((traffic.getMinValueAt(i) <= d.doubleValue()) && (d.doubleValue() <= traffic.getMaxValueAt(i))) {
/* 631 */           ColorDescriptor bg = traffic.getBackgroundColorAt(i);
/* 632 */           ColorDescriptor fg = traffic.getForegroundColorAt(i);
/* 633 */           FontDescriptor fd = traffic.getFontAt(i);
/* 634 */           text = applyFontAndColor(fd, fg, text);
/* 635 */           String bgColorString = "";
/* 636 */           if (bg != null) {
/* 637 */             bgColorString = "bgColor=\"" + getHTMLColor(bg) + "\"";
/*     */           }
/* 639 */           return new Object[] { Boolean.valueOf(true), "<td align=\"right\" " + bgColorString + ">" + text + "</td>" };
/*     */         }
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 643 */       return new Object[] { Boolean.valueOf(false) };
/*     */     }
/* 645 */     return new Object[] { Boolean.valueOf(false) };
/*     */   }
/*     */ 
/*     */   private final String applyFormat(Format f, String text)
/*     */   {
/* 656 */     String originalText = text;
/* 657 */     if (text.length() == 0) {
/* 658 */       text = "&nbsp;";
/*     */     }
/* 660 */     text = applyNumberFormat(f, text);
/* 661 */     if (f == null) {
/* 662 */       String align = "";
/*     */       try {
/* 664 */         Double.parseDouble(originalText);
/* 665 */         align = "align=\"right\"";
/*     */       } catch (NumberFormatException localNumberFormatException) {
/*     */       }
/* 668 */       return "<td " + align + ">" + text + "</td>";
/*     */     }
/* 670 */     Object[] result = new Object[2];
/* 671 */     result[0] = Boolean.valueOf(false);
/* 672 */     if (f.getTrafficLightData() != null) {
/* 673 */       result = applyTrafficLightData(f.getTrafficLightData(), text, originalText);
/*     */     }
/* 675 */     if (!((Boolean)result[0]).booleanValue())
/*     */     {
/* 677 */       text = applyFontAndColor(f.getFontData(), f.getForegroundColor(), text);
/*     */ 
/* 679 */       String bgColorString = "";
/* 680 */       if (f.getBackgroundColor() != null) {
/* 681 */         ColorDescriptor bg = f.getBackgroundColor();
/* 682 */         bgColorString = "bgColor=\"" + getHTMLColor(bg) + "\"";
/*     */       }
/* 684 */       String align = "";
/*     */       try {
/* 686 */         Double.parseDouble(originalText);
/* 687 */         align = "align=\"right\"";
/*     */       } catch (NumberFormatException localNumberFormatException1) {
/*     */       }
/* 690 */       return "<td " + align + " " + bgColorString + ">" + text + "</td>";
/*     */     }
/* 692 */     return result[1].toString();
/*     */   }
/*     */ 
/*     */   private final void fill(Element[] coord, AxisItem item, HashMap<Hierarchy, Integer> hierIndex)
/*     */   {
/* 705 */     if (item == null) {
/* 706 */       return;
/*     */     }
/*     */ 
/* 710 */     coord[((Integer)hierIndex.get(item.getHierarchy())).intValue()] = item.getElement();
/* 711 */     fill(coord, item.getParentInPrevHierarchy(), hierIndex);
/*     */   }
/*     */ 
/*     */   private final Format getFormat(int row, int col, IndexRangeInfo[] ranges, Element[] coords)
/*     */   {
/* 724 */     Format f = null;
/* 725 */     if (coords != null) {
/* 726 */       for (Element e : coords) {
/* 727 */         boolean valid = true;
/* 728 */         if (this.allDimensionLevels.containsKey(e.getDimension())) {
/* 729 */           valid = false;
/* 730 */           for (LevelRangeInfo i : this.allDimensionLevels.get(e.getDimension())) {
/* 731 */             if (i.level == e.getLevel()) {
/* 732 */               valid = true;
/* 733 */               f = i.format;
/* 734 */               break;
/*     */             }
/*     */           }
/* 737 */           if (!valid) {
/* 738 */             return null;
/*     */           }
/*     */         }
/*     */       }
/* 742 */       if (f != null) {
/* 743 */         return f;
/*     */       }
/*     */     }
/* 746 */     for (IndexRangeInfo r : ranges) {
/* 747 */       if ((row == r.row) && (col == r.col)) {
/* 748 */         return r.format;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 756 */     return (Format)null;
/*     */   }
/*     */ 
/*     */   class IndexRangeInfo
/*     */   {
/*     */     int row;
/*     */     int col;
/*     */     Format format;
/*     */ 
/*     */     IndexRangeInfo(Format f, int row, int col)
/*     */     {
/* 106 */       this.format = f;
/* 107 */       this.row = row;
/* 108 */       this.col = col;
/*     */     }
/*     */   }
/*     */ 
/*     */   class LevelRangeInfo
/*     */   {
/*     */     int level;
/*     */     Format format;
/*     */ 
/*     */     LevelRangeInfo(Format f, int level)
/*     */     {
/* 134 */       this.level = level;
/* 135 */       this.format = f;
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi.jar
 * Qualified Name:     org.palo.viewapi.exporters.PaloHTMLExporter
 * JD-Core Version:    0.5.4
 */