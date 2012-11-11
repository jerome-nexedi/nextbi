/*     */package org.palo.viewapi.exporters;

/*     */
/*     */import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.palo.api.Cube;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.Hierarchy;
import org.palo.api.ext.ui.ColorDescriptor;
import org.palo.api.ext.ui.FontDescriptor;
import org.palo.viewapi.Axis;
import org.palo.viewapi.AxisHierarchy;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.Property;
import org.palo.viewapi.uimodels.axis.AxisFlatModel;
import org.palo.viewapi.uimodels.axis.AxisItem;
import org.palo.viewapi.uimodels.formats.Format;
import org.palo.viewapi.uimodels.formats.FormatRangeInfo;
import org.palo.viewapi.uimodels.formats.TrafficLightData;

/*     */
/*     */public class XMLAHTMLExporter
/*     */implements CubeViewExporter
/*     */{
  /* 70 */private static final DecimalFormat DEFAULT_FORMATTER = new DecimalFormat(
    "#,##0.00");

  /*     */
  /* 75 */private DecimalFormat customFormatter = new DecimalFormat("#,##0.00");

  /*     */private Object[] cellValues;

  /* 119 */private int cellPosition = 0;

  /*     */
  /* 124 */private int columnCount = 0;

  /*     */
  /* 129 */private int rowCount = 0;

  /*     */private IndexRangeInfo[] indexRanges;

  /* 139 */private final StringBuffer html = new StringBuffer();

  /*     */
  /*     */public final String getHTML()
  /*     */{
    /* 146 */return this.html.toString();
    /*     */}

  /*     */
  /*     */private final String getString(Element[] coord)
  /*     */{
    /* 156 */StringBuffer b = new StringBuffer();
    /* 157 */for (Element e : coord) {
      /* 158 */b.append(e.getId() + ",");
      /*     */}
    /* 160 */return b.toString();
    /*     */}

  /*     */
  /*     */private final int getWidth(AxisItem[] roots)
  /*     */{
    /* 171 */if ((roots == null) || (roots.length == 0)) {
      /* 172 */return 0;
      /*     */}
    /* 174 */int result = 0;
    /* 175 */int n = roots.length;
    /* 176 */for (int i = 0; i < n; ++i) {
      /* 177 */result += getWidth(roots[i].getChildren());
      /* 178 */result += getWidth(roots[i].getRootsInNextHierarchy());
      /*     */}
    /* 180 */return result + n;
    /*     */}

  /*     */
  /*     */private final String drawRows(AxisItem[] roots, String prefix)
  /*     */{
    /* 191 */if ((roots == null) || (roots.length == 0)) {
      /* 192 */return "";
      /*     */}
    /* 194 */StringBuffer result = new StringBuffer();
    /* 195 */for (AxisItem root : roots) {
      /* 196 */String localPrefix = prefix + "<td>" + root.getName() + "</td>";
      /* 197 */if (!root.hasRootsInNextHierarchy()) {
        /* 198 */result.append(prefix);
        /* 199 */result.append("<td>" + root.getElement().getName() + "</td>");
        /* 200 */for (int i = 0; i < this.columnCount; ++i) {
          /* 201 */String formatted = this.cellValues[(this.cellPosition++)]
            .toString();
          /* 202 */Format f = getFormat(this.rowCount, i, this.indexRanges);
          /* 203 */formatted = applyFormat(f, formatted);
          /* 204 */result.append(formatted);
          /*     */}
        /* 206 */result.append("</tr>\r\n");
        /* 207 */this.rowCount += 1;
        /*     */}
      /* 209 */result.append(drawRows(root.getRootsInNextHierarchy(), localPrefix));
      /* 210 */localPrefix = localPrefix.replaceAll("<td>.*?</td>",
        "<td>&nbsp;</td>");
      /* 211 */result.append(drawRows(root.getChildren(), prefix));
      /* 212 */prefix = prefix.replaceAll("<td>.*?</td>", "<td>&nbsp;</td>");
      /*     */}
    /* 214 */return result.toString();
    /*     */}

  /*     */
  /*     */private int getMaxDimDepth(AxisItem[] roots)
  /*     */{
    /* 223 */int localMax = 0;
    /* 224 */for (AxisItem root : roots) {
      /* 225 */int sum = 1 + getMaxDimDepth(root.getRootsInNextHierarchy());
      /* 226 */if (sum > localMax) {
        /* 227 */localMax = sum;
        /*     */}
      /*     */}
    /* 230 */return localMax;
    /*     */}

  /*     */
  /*     *//** @deprecated */
  /*     */private final Hierarchy getHier(Dimension d,
    HashMap<Dimension, Hierarchy> dimHier)
  /*     */{
    /* 242 */Hierarchy hier = (Hierarchy) dimHier.get(d);
    /* 243 */if (hier == null) {
      /* 244 */hier = d.getDefaultHierarchy();
      /*     */}
    /* 246 */return hier;
    /*     */}

  /*     */
  /*     */public final void export(CubeView view)
  /*     */{
    /* 253 */Cube cube = view.getCube();
    /*     */
    /* 255 */Property[] properties = view.getProperties();
    /* 256 */HashMap dimHier =
    /* 257 */new HashMap();
    /*     */
    /* 259 */for (Property prop : properties) {
      /* 260 */if (prop.getId().startsWith("dimensionHierarchy-")) {
        /* 261 */String dimId = prop.getId().substring(
          "dimensionHierarchy-".length());
        /* 262 */Dimension dim = cube.getDimensionById(dimId);
        /* 263 */Hierarchy hier = dim.getHierarchyById(prop.getValue().toString());
        /* 264 */dimHier.put(dim, hier);
        /*     */}
      /*     */}
    /*     */
    /* 268 */Dimension[] cubeDims = cube.getDimensions();
    /* 269 */HashMap hierIndex =
    /* 270 */new HashMap();
    /* 271 */HashMap cellIndexMap =
    /* 272 */new HashMap();

    /* 273 */for (int k = 0; k < cubeDims.length; ++k) {
      /* 274 */hierIndex.put(getHier(cubeDims[k], dimHier), Integer.valueOf(k));
      /*     */}
    /*     */
    /* 277 */Element[] coord = new Element[cubeDims.length];
    /*     */
    /* 279 */Axis selAxis = view.getAxis("selected");
    /*     */Element[] selElements;
    /* 280 */for (int i = 0; i < cubeDims.length; ++i) {
      /* 281 */AxisHierarchy axisHierarchy = selAxis.getAxisHierarchy(
      /* 282 */cubeDims[i].getDefaultHierarchy());
      /* 283 */if (axisHierarchy != null) {
        /* 284 */selElements = axisHierarchy.getSelectedElements();
        /*     */
        /* 286 */if ((selElements != null) && (selElements.length > 0)) {
          /* 287 */coord[i] = selElements[0];
          /*     */}
        /*     */}
      /*     */}
    /* 291 */this.html.append("<html><body>\r\n");
    /* 292 */AxisHierarchy[] axisHierarchies = selAxis.getAxisHierarchies();
    /* 293 */if (axisHierarchies.length != 0) {
      /* 294 */this.html.append("<table border=\"1\"><tr>");
      /* 295 */int arrayOfElement1 = axisHierarchies.length;
      for (int selElements1 = 0; selElements1 < arrayOfElement1; ++selElements1) {
        AxisHierarchy ah = axisHierarchies[selElements1];
        /* 296 */this.html.append("<td>" + ah.getHierarchy().getName() + "</td>");
      }
      /*     */
      /* 298 */this.html.append("</tr>\r\n<tr>");
      /* 299 */int arrayOfElement2 = axisHierarchies.length;
      for (int selElements1 = 0; selElements1 < arrayOfElement2; ++selElements1) {
        AxisHierarchy ah = axisHierarchies[selElements1];
        /*     */
        /* 301 */Element[] currentlySelectedElements = ah.getSelectedElements();
        /* 302 */if ((currentlySelectedElements == null)
          || (currentlySelectedElements.length == 0))
          /* 303 */this.html.append("<td>-</td>");
        /*     */else {
          /* 305 */this.html.append("<td>" + ah.getSelectedElements()[0].getName()
            + "</td>");
          /*     */}
      }
      /*     */
      /* 308 */this.html.append("</tr></table>\r\n");
      /*     */}
    /*     */
    /* 318 */Object rc = view.getPropertyValue("reverseHorizontal");
    /* 319 */AxisFlatModel cols = new AxisFlatModel(view.getAxis("cols"),
      (rc != null) && (Boolean.parseBoolean(rc.toString())));
    /* 320 */AxisItem[][] colsModel = cols.getModel();
    /* 321 */if ((colsModel == null) || (colsModel.length == 0)) {
      /* 322 */this.html.append("</body></html>\r\n");
      /* 323 */return;
      /*     */}
    /*     */
    /* 326 */Object rr = view.getPropertyValue("reverseVertical");
    /* 327 */AxisFlatModel rows = new AxisFlatModel(view.getAxis("rows"), 1,
      (rr != null) && (Boolean.parseBoolean(rr.toString())));
    /* 328 */AxisItem[][] rowsModel = rows.getModel();
    /*     */
    /* 331 */this.html.append("<table border=\"1\">");
    /* 332 */int emptyCellRowCount = getMaxDimDepth(rowsModel[0]);
    /*     */int emptyCellCount;

    /* 334 */for (AxisItem[] items : colsModel) {
      /* 335 */this.html.append("<tr>");
      /* 336 */for (int i = 0; i < emptyCellRowCount; ++i) {
        /* 337 */this.html.append("<td>&nbsp;</td>");
        /*     */}
      /* 339 */for (AxisItem item : items) {
        /* 340 */this.html.append("<td>" + item.getElement().getName() + "</td>");
        /* 341 */emptyCellCount = getWidth(item.getRootsInNextHierarchy()) - 1;
        /* 342 */for (int i = 0; i < emptyCellCount; ++i) {
          /* 343 */this.html.append("<td>&nbsp;</td>");
          /*     */}
        /*     */}
      /* 346 */this.html.append("</tr>\r\n");
      /*     */}
    /*     */
    /* 349 */if ((rowsModel == null) || (rowsModel.length == 0)) {
      /* 350 */this.html.append("</table>\r\n</body></html>\r\n");
      /* 351 */return;
      /*     */}
    /*     */
    /* 354 */int cellIndex = 0;
    /* 355 */int rowLeaf = rowsModel.length - 1;
    /* 356 */int colLeaf = colsModel.length - 1;
    /* 357 */int cells = rowsModel[rowLeaf].length * colsModel[colLeaf].length;
    /* 358 */Element[][] cellCoords = new Element[cells][];

    /* 359 */for (int i3 = 0; i3 < rowsModel[rowLeaf].length; ++i3)
    /*     */{

      /* 360 */for (int i6 = 0; i6 < colsModel[colLeaf].length; ++i6) {
        /* 361 */cellCoords[cellIndex] = ((Element[]) coord.clone());
        /*     */
        /* 363 */fill(cellCoords[cellIndex], rowsModel[rowLeaf][i3], hierIndex);
        /*     */
        /* 365 */fill(cellCoords[cellIndex], colsModel[colLeaf][i6], hierIndex);
        /* 366 */cellIndexMap.put(getString(cellCoords[cellIndex]),
        /* 367 */new Integer[] { Integer.valueOf(i3), Integer.valueOf(i6) });
        /* 368 */++cellIndex;
        /*     */}
      /*     */}
    /* 372 */int i4 = 0;
    /* 373 */for (Format f : view.getFormats()) {
      /* 374 */i4 += f.getRangeCount();
      /*     */}
    /*     */
    /* 377 */ArrayList allCoordinates = new ArrayList();
    /* 378 */for (Format f : view.getFormats()) {
      /* 379 */for (FormatRangeInfo r : f.getRanges()) {
        /* 380 */if (r.getCells() != null) {
          /* 381 */for (Element[] e : r.getCells()) {
            /* 382 */Integer[] rcCoord = (Integer[]) cellIndexMap.get(getString(e));
            /* 383 */if (rcCoord != null) {
              /* 384 */allCoordinates.add(new IndexRangeInfo(f, rcCoord[0]
                .intValue(), rcCoord[1].intValue()));
              /*     */}
            /*     */}
          /*     */}
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 392 */this.indexRanges = ((IndexRangeInfo[]) allCoordinates
      .toArray(new IndexRangeInfo[0]));
    /*     */
    /* 394 */this.cellValues = cube.getDataBulk(cellCoords);
    /* 395 */this.cellPosition = 0;
    /* 396 */this.columnCount = colsModel[colLeaf].length;
    /*     */
    /* 398 */this.html.append(drawRows(rows.getRoots(), ""));
    /* 399 */this.html.append("</table>\r\n</body></html>\r\n");
    /*     */}

  /*     */
  /*     */private final String applyNumberFormat(Format f, String text)
  /*     */{
    /* 411 */if ((f == null) || (f.getNumberFormat() == null)) {
      /*     */try {
        /* 413 */Double d = Double.valueOf(Double.parseDouble(text));
        /* 414 */return DEFAULT_FORMATTER.format(d);
        /*     */} catch (NumberFormatException e) {
        /* 416 */return text;
        /*     */}
      /*     */}
    /* 419 */this.customFormatter.applyPattern(f.getNumberFormat());
    /*     */try {
      /* 421 */Double d = Double.valueOf(Double.parseDouble(text));
      /* 422 */return this.customFormatter.format(d);
    } catch (NumberFormatException e) {
      /*     */}
    /* 424 */return text;
    /*     */}

  /*     */
  /*     */private final String getHTMLColor(ColorDescriptor desc)
  /*     */{
    /* 437 */String red = Integer.toHexString(desc.getRed());
    /* 438 */String green = Integer.toHexString(desc.getGreen());
    /* 439 */String blue = Integer.toHexString(desc.getBlue());
    /* 440 */if (red.length() < 2)
      red = "0" + red;
    /* 441 */if (green.length() < 2)
      green = "0" + green;
    /* 442 */if (blue.length() < 2)
      blue = "0" + blue;
    /* 443 */return "#" + red + green + blue;
    /*     */}

  /*     */
  /*     */private final String applyFontAndColor(FontDescriptor fd,
    ColorDescriptor fg, String text)
  /*     */{
    /* 456 */if (text.equals("&nbsp;")) {
      /* 457 */return text;
      /*     */}
    /* 459 */String colorString = "";
    /* 460 */String fontFaceString = "";
    /* 461 */if (fg != null) {
      /* 462 */colorString = "color=\"" + getHTMLColor(fg) + "\"";
      /*     */}
    /* 464 */if (fd != null) {
      /* 465 */fontFaceString = "face=\"" + fd.getName() + "\"";
      /* 466 */if (fd.isBold()) {
        /* 467 */text = "<b>" + text + "</b>";
        /*     */}
      /* 469 */if (fd.isItalic()) {
        /* 470 */text = "<i>" + text + "</i>";
        /*     */}
      /* 472 */if (fd.isUnderlined()) {
        /* 473 */text = "<u>" + text + "</u>";
        /*     */}
      /*     */}
    /* 476 */if ((fontFaceString.length() != 0) || (colorString.length() != 0)) {
      /* 477 */text = "<font " + fontFaceString + " " + colorString + ">" + text +
      /* 478 */"</font>";
      /*     */}
    /* 480 */return text;
    /*     */}

  /*     */
  /*     */private final Object[] applyTrafficLightData(TrafficLightData traffic,
    String text, String originalText)
  /*     */{
    /*     */try
    /*     */{
      /* 497 */Double d = Double.valueOf(Double.parseDouble(originalText));
      /* 498 */for (int i = 0; i < traffic.getSize(); ++i)
        /* 499 */if ((traffic.getMinValueAt(i) <= d.doubleValue())
          && (d.doubleValue() <= traffic.getMaxValueAt(i))) {
          /* 500 */ColorDescriptor bg = traffic.getBackgroundColorAt(i);
          /* 501 */ColorDescriptor fg = traffic.getForegroundColorAt(i);
          /* 502 */FontDescriptor fd = traffic.getFontAt(i);
          /* 503 */text = applyFontAndColor(fd, fg, text);
          /* 504 */String bgColorString = "";
          /* 505 */if (bg != null) {
            /* 506 */bgColorString = "bgColor=\"" + getHTMLColor(bg) + "\"";
            /*     */}
          /* 508 */return new Object[] { Boolean.valueOf(true),
            "<td align=\"right\" " + bgColorString + ">" + text + "</td>" };
          /*     */}
      /*     */}
    /*     */catch (NumberFormatException e) {
      /* 512 */return new Object[] { Boolean.valueOf(false) };
      /*     */}
    /* 514 */return new Object[] { Boolean.valueOf(false) };
    /*     */}

  /*     */
  /*     */private final String applyFormat(Format f, String text)
  /*     */{
    /* 525 */String originalText = text;
    /* 526 */if (text.length() == 0) {
      /* 527 */text = "&nbsp;";
      /*     */}
    /* 529 */text = applyNumberFormat(f, text);
    /* 530 */if (f == null) {
      /* 531 */String align = "";
      /*     */try {
        /* 533 */Double.parseDouble(originalText);
        /* 534 */align = "align=\"right\"";
        /*     */} catch (NumberFormatException localNumberFormatException) {
        /*     */}
      /* 537 */return "<td " + align + ">" + text + "</td>";
      /*     */}
    /* 539 */Object[] result = new Object[2];
    /* 540 */result[0] = Boolean.valueOf(false);
    /* 541 */if (f.getTrafficLightData() != null) {
      /* 542 */result = applyTrafficLightData(f.getTrafficLightData(), text,
        originalText);
      /*     */}
    /* 544 */if (!((Boolean) result[0]).booleanValue())
    /*     */{
      /* 546 */text = applyFontAndColor(f.getFontData(), f.getForegroundColor(),
        text);
      /*     */
      /* 548 */String bgColorString = "";
      /* 549 */if (f.getBackgroundColor() != null) {
        /* 550 */ColorDescriptor bg = f.getBackgroundColor();
        /* 551 */bgColorString = "bgColor=\"" + getHTMLColor(bg) + "\"";
        /*     */}
      /* 553 */String align = "";
      /*     */try {
        /* 555 */Double.parseDouble(originalText);
        /* 556 */align = "align=\"right\"";
        /*     */} catch (NumberFormatException localNumberFormatException1) {
        /*     */}
      /* 559 */return "<td " + align + " " + bgColorString + ">" + text + "</td>";
      /*     */}
    /* 561 */return result[1].toString();
    /*     */}

  /*     */
  /*     */private final void fill(Element[] coord, AxisItem item,
    HashMap<Hierarchy, Integer> hierIndex)
  /*     */{
    /* 574 */if (item == null) {
      /* 575 */return;
      /*     */}
    /* 577 */coord[((Integer) hierIndex.get(item.getHierarchy())).intValue()] = item
      .getElement();
    /* 578 */fill(coord, item.getParentInPrevHierarchy(), hierIndex);
    /*     */}

  /*     */
  /*     */private final Format getFormat(int row, int col, IndexRangeInfo[] ranges)
  /*     */{
    /* 590 */for (IndexRangeInfo r : ranges) {
      /* 591 */if ((row == r.row) && (col == r.col)) {
        /* 592 */return r.format;
        /*     */}
      /*     */
      /*     */}
    /*     */
    /* 600 */return null;
    /*     */}

  /*     */
  /*     */class IndexRangeInfo
  /*     */{
    /*     */int row;

    /*     */int col;

    /*     */Format format;

    /*     */
    /*     */IndexRangeInfo(Format f, int row, int col)
    /*     */{
      /* 105 */this.format = f;
      /* 106 */this.row = row;
      /* 107 */this.col = col;
      /*     */}
    /*     */
  }
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.exporters.XMLAHTMLExporter JD-Core
 * Version: 0.5.4
 */