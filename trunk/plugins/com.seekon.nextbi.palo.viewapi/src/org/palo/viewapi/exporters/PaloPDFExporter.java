// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2012/4/30 13:27:41
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PaloPDFExporter.java

package org.palo.viewapi.exporters;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.palo.api.Cell;
import org.palo.api.Cube;
import org.palo.api.Dimension;
import org.palo.api.Element;
import org.palo.api.ElementNode;
import org.palo.api.Hierarchy;
import org.palo.api.ext.ui.ColorDescriptor;
import org.palo.api.ext.ui.FontDescriptor;
import org.palo.viewapi.Axis;
import org.palo.viewapi.AxisHierarchy;
import org.palo.viewapi.CubeView;
import org.palo.viewapi.uimodels.axis.AxisFlatModel;
import org.palo.viewapi.uimodels.axis.AxisItem;
import org.palo.viewapi.uimodels.formats.BorderData;
import org.palo.viewapi.uimodels.formats.Format;
import org.palo.viewapi.uimodels.formats.FormatRangeInfo;

// Referenced classes of package org.palo.viewapi.exporters:
//            CubeViewExporter, PaloPDFExporterConfiguration, PDFUtils

public class PaloPDFExporter implements CubeViewExporter {
  private static class BorderPdfPCellEvent implements PdfPCellEvent {

    public void cellLayout(PdfPCell cell, Rectangle pos, PdfContentByte canvases[]) {
      if (bData == null || bData.length == 0)
        return;
      PdfContentByte cb = canvases[2];
      cb.saveState();
      float left = pos.getLeft() + 1.0F;
      float right = pos.getRight() - 1.0F;
      float bottom = pos.getBottom() + 1.0F;
      float top = pos.getTop() - 1.0F;
      for (int i = 0; i < bData.length; i++) {
        BorderData bd = bData[i];
        if (bd == null) {
          cb.setLineWidth(1.0F);
          cb.setLineDash(1.0F, 0.0F);
          cb.setColorStroke(BaseColor.BLACK);
        } else {
          cb.saveState();
          PDFUtils.applyBorderDataLineStyle(cb, bd);
          switch (bd.getLinePosition()) {
          case 3: // '\003'
            cb.moveTo(left, bottom);
            cb.lineTo(right, bottom);
            cb.stroke();
            break;

          case 5: // '\005'
            cb.moveTo(left, top);
            cb.lineTo(right, top);
            cb.stroke();
            break;

          case 0: // '\0'
            cb.moveTo(left, bottom);
            cb.lineTo(left, top);
            cb.stroke();
            break;

          case 2: // '\002'
            cb.moveTo(right, bottom);
            cb.lineTo(right, top);
            cb.stroke();
            break;
          }
          cb.restoreState();
        }
      }

      cb.closePathStroke();
      cb.restoreState();
    }

    private static final int MARGIN = 1;

    private final BorderData bData[];

    BorderPdfPCellEvent(BorderData bData[]) {
      this.bData = bData;
    }
  }

  private static class IndexRangeInfo {

    int row;

    int col;

    Format format;

    IndexRangeInfo(Format f, int row, int col) {
      format = f;
      this.row = row;
      this.col = col;
    }
  }

  class ItemPair
  /*      */{
    /*      */AxisItem parentInPrevHier;

    /*      */int col;

    /*      */
    /*      */public ItemPair(AxisItem parent, int col)
    /*      */{
      /* 998 */this.parentInPrevHier = parent;
      /* 999 */this.col = col;
      /*      */}

    /*      */
    /*      */public int hashCode() {
      /* 1003 */return (this.parentInPrevHier == null) ? 17 : this.parentInPrevHier
        .hashCode()
        +
        /* 1004 */7 * this.col;
      /*      */}

    /*      */
    /*      */public boolean equals(Object o) {
      /* 1008 */if ((o == null) || (!(o instanceof ItemPair))) {
        /* 1009 */return false;
        /*      */}
      /* 1011 */ItemPair ip = (ItemPair) o;
      /* 1012 */if (ip.parentInPrevHier == null) {
        /* 1013 */return (this.parentInPrevHier == null) && (this.col == ip.col);
        /*      */}
      /* 1015 */return (ip.parentInPrevHier.equals(this.parentInPrevHier))
        && (this.col == ip.col);
      /*      */}
    /*      */
  }

  private static class LevelRangeInfo {

    int level;

    Format format;

    LevelRangeInfo(Format f, int level) {
      this.level = level;
      format = f;
    }
  }

  private static class NumberUtil {

    static String getCharNumber(int n) {
      StringBuffer sb = new StringBuffer();
      do {
        int v = n % chars.length;
        n /= chars.length;
        sb.insert(0, getSingleCharNumber(v));
      } while (n > 0);
      return sb.toString();
    }

    private static char getSingleCharNumber(int n) {
      return chars[n % chars.length];
    }

    private static final char chars[] = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private NumberUtil() {
    }
  }

  private class PDFPageEventHandler extends PdfPageEventHelper {

    public void onEndPage(PdfWriter writer, Document document) {
      PdfContentByte cb = writer.getDirectContent();
      cb.saveState();
      if (config.isShowPageNumbers()) {
        String text = getPageText(writer);
        Phrase footerPhr = new Phrase(text, PaloPDFExporter.FONT_PNUMBER);
        float sy = document.bottomMargin() / 2.0F;
        float textBase = document.bottom() - sy - 10F;
        ColumnText.showTextAligned(cb, 1, footerPhr, (document.right() - document
          .left())
          / 2.0F + document.leftMargin(), textBase, 0.0F);
        cb.setColorStroke(BaseColor.GRAY);
        cb.setLineWidth(0.5F);
        int ey = (int) (document.bottom() - sy);
        cb.moveTo(30F, ey);
        cb.lineTo(document.getPageSize().getWidth() - 30F, ey);
        cb.stroke();
      }
      cb.restoreState();
    }

    private String getPageText(PdfWriter writer) {
      if (!isExtPage) {
        extPageCount = 0;
        return Integer.toString(++lastPage);
      } else {
        extPageCount++;
        return (new StringBuilder(String.valueOf(Integer.toString(lastPage))))
          .append(NumberUtil.getCharNumber(extPageCount)).toString();
      }
    }

    private int lastPage;

    private int extPageCount;

    private PDFPageEventHandler() {

      super();
    }

    PDFPageEventHandler(PDFPageEventHandler pdfpageeventhandler) {
      this();
    }
  }

  class Pair {

    public int hashCode() {
      return x * 35 + y * 7;
    }

    public boolean equals(Object o) {
      if (o == null || !(o instanceof Pair))
        return false;
      Pair ot = (Pair) o;
      return ot.x == x && ot.y == y;
    }

    public int x;

    public int y;

    public Pair(int x, int y) {

      super();
      this.x = x;
      this.y = y;
    }
  }

  public PaloPDFExporter() {
    this(null);
  }

  public PaloPDFExporter(PaloPDFExporterConfiguration config) {
    indexCellMap = new HashMap();
    isExtPage = false;
    firstCell = null;
    this.config = config == null ? PaloPDFExporterConfiguration.createDefault()
      : config;
    if (config == null) {
      cellReplacementString = "#####";
      determineWidths("-999.999.999.999.999.999,00", "This is the reference string");
    } else {
      cellReplacementString = config.getCellReplaceString();
      determineWidths(config.getMaxColString(), config.getMaxRowsHeaderString());
    }
  }

  private final void determineWidths(String cmw, String rhmw) {
    columsMaxWidth = BASE_FONT_CELL.getWidthPoint((new StringBuilder(String
      .valueOf(cmw))).append("9").toString(), FONT_CELL.getSize());
    rowHeadersMaxWidth = BASE_FONT_CELL_H.getWidthPoint(rhmw, FONT_CELL_H.getSize());
  }

  public void export(CubeView view) {
    this.view = view;
    try {
      Document document = new Document(config.getPageSize());
      document.addCreator("JPalo / www.tensegrity.com");
      outFile = null;
      outFile = getOutFile();
      writer = PdfWriter.getInstance(document, new FileOutputStream(outFile));
      writer.setStrictImageSequence(true);
      writer.setViewerPreferences(129);
      PDFPageEventHandler eventHandler = new PDFPageEventHandler(null);
      writer.setPageEvent(eventHandler);
      document.addTitle(view.getName());
      document.open();
      String title;
      try {
        title = config.getTitle();
      } catch (Exception e) {
        title = view.getName();
      }
      if (config.isShowTitle()) {
        Phrase phrase = new Phrase(title, FONT_H1);
        document.add(phrase);
      }
      firstSiteYOffSet = document.topMargin()
        + (config.isShowTitle() ? FONT_H1.getSize() : 0.0F);
      export(writer, document, view);
      document.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  private final void export(PdfWriter writer, Document doc, CubeView view)
    throws DocumentException {
    Cube cube = view.getCube();
    Dimension cubeDims[] = cube.getDimensions();
    HashMap hierIndex = new HashMap();
    for (int i = 0; i < cubeDims.length; i++)
      hierIndex.put(cubeDims[i].getDefaultHierarchy(), Integer.valueOf(i));

    Element coord[] = new Element[cubeDims.length];
    Axis selAxis = view.getAxis("selected");
    Axis repAxis = view.getAxis("hierarchy-repository");
    for (int i = 0; i < cubeDims.length; i++) {
      AxisHierarchy axisHierarchy = selAxis.getAxisHierarchy(cubeDims[i]
        .getDefaultHierarchy());
      if (axisHierarchy != null) {
        Element selElements[] = axisHierarchy.getSelectedElements();
        if (selElements != null && selElements.length > 0)
          coord[i] = selElements[0];
      } else {
        axisHierarchy = repAxis.getAxisHierarchy(cubeDims[i].getDefaultHierarchy());
        if (axisHierarchy != null) {
          Element selElements[] = axisHierarchy.getSelectedElements();
          if (selElements != null && selElements.length > 0)
            coord[i] = selElements[0];
        }
      }
    }

    Object rr = view.getPropertyValue("reverseVertical");
    AxisFlatModel rows = null;
    try {
      rows = new AxisFlatModel(view.getAxis("rows"), 1, rr != null
        && Boolean.parseBoolean((new StringBuilder()).append(rr).toString()));
    } catch (Throwable t) {
      doc.close();
      return;
    }
    AxisItem rowsModel[][] = rows.getModel();
    Object rc = view.getPropertyValue("reverseHorizontal");
    AxisFlatModel cols = new AxisFlatModel(view.getAxis("cols"), rc != null
      && Boolean.parseBoolean((new StringBuilder()).append(rc).toString()));
    AxisItem colsModel[][] = cols.getModel();
    int rowLeaf = rowsModel[0].length - 1;
    int colLeaf = colsModel.length - 1;
    int cells = rowsModel.length * colsModel[colLeaf].length;
    Element cellCoords[][] = new Element[cells][];
    int cellIndex = 0;
    HashMap cellIndexMap = new HashMap();
    for (int r = 0; r < rowsModel.length; r++) {
      for (int c = 0; c < colsModel[colLeaf].length; c++) {
        cellCoords[cellIndex] = (Element[]) coord.clone();
        fill(cellCoords[cellIndex], rowsModel[r][rowLeaf], hierIndex);
        fill(cellCoords[cellIndex], colsModel[colLeaf][c], hierIndex);
        String key = getString(cellCoords[cellIndex]);
        cellIndexMap.put(key,
          new Integer[] { Integer.valueOf(r), Integer.valueOf(c) });
        indexCellMap.put((new StringBuilder(String.valueOf(r))).append(";")
          .append(c).toString(), cellCoords[cellIndex]);
        cellIndex++;
      }

    }

    Cell cellValues[];
    try {
      cellValues = cube.getCells(cellCoords);
    } catch (Throwable t) {
      doc.close();
      return;
    }
    Object hz = view.getPropertyValue("hideEmpty");
    ArrayList emptyColumns = new ArrayList();
    ArrayList emptyRows = new ArrayList();
    if (hz != null && Boolean.parseBoolean(hz.toString())) {
      cellIndex = 0;
      boolean hasRowValue[] = new boolean[colsModel[colLeaf].length];
      for (int c = 0; c < colsModel[colLeaf].length; c++)
        hasRowValue[c] = false;

      for (int r = 0; r < rowsModel.length; r++) {
        boolean hasColValues = false;
        for (int c = 0; c < colsModel[colLeaf].length; c++) {
          if (!cellValues[cellIndex].isEmpty()) {
            hasColValues = true;
            hasRowValue[c] = true;
          }
          cellIndex++;
        }

        if (!hasColValues)
          emptyRows.add(Integer.valueOf(r));
      }

      for (int c = 0; c < colsModel[colLeaf].length; c++)
        if (!hasRowValue[c])
          emptyColumns.add(Integer.valueOf(c));

    }
    allDimensionLevels = new HashMap();
    ArrayList allCoordinates = new ArrayList();
    Format aformat[];
    int i = (aformat = view.getFormats()).length;
    for (int j = 0; j < i; j++) {
      Format f = aformat[j];
      FormatRangeInfo aformatrangeinfo[];
      int i1 = (aformatrangeinfo = f.getRanges()).length;
      for (int k = 0; k < i1; k++) {
        FormatRangeInfo r = aformatrangeinfo[k];
        if (r.getCells() != null) {
          Element aelement[][];
          int l1 = (aelement = r.getCells()).length;
          for (int j1 = 0; j1 < l1; j1++) {
            Element e[] = aelement[j1];
            String key = getString(e);
            Integer rcCoord[] = (Integer[]) cellIndexMap.get(key);
            if (rcCoord != null)
              allCoordinates.add(new IndexRangeInfo(f, rcCoord[0].intValue(),
                rcCoord[1].intValue()));
          }

        } else {
          Dimension adimension[];
          int i2 = (adimension = r.getDimensions()).length;
          for (int k1 = 0; k1 < i2; k1++) {
            Dimension d = adimension[k1];
            ArrayList l = (ArrayList) allDimensionLevels.get(d);
            if (l == null)
              l = new ArrayList();
            l.add(new LevelRangeInfo(f, r.getLevel(d)));
            allDimensionLevels.put(d, l);
          }

        }
      }

    }

    indexRanges = (IndexRangeInfo[]) allCoordinates
      .toArray(new IndexRangeInfo[allCoordinates.size()]);
    int colCount = colsModel[colLeaf].length + rowLeaf + 1;
    PdfPTable table = createTable(colCount - emptyColumns.size());
    rowCount = 0;
    cWidths = new float[colCount - emptyColumns.size()];
    cHeight = -1F;
    createHeaderCells(table, rowsModel, colsModel, emptyColumns);
    createRowCells(table, rowsModel, colsModel, emptyColumns, emptyRows, cellValues);
    table.setTotalWidth(cWidths);
    table.setLockedWidth(true);
    for (i = 0; i < colsModel.length; i++)
      headerRows += table.getRowHeight(i);

    float selAxisHeight = -1F;
    selAxisHeight = config.isShowPOV() ? calcSelectionAxisHeight(doc, selAxis)
      : 0.0F;
    if (config.isShowPOV())
      addSelectionAxisHeader(doc, selAxis, selAxisHeight);
    firstSiteYOffSet -= 26F;
    if (!config.isShowTitle())
      firstSiteYOffSet -= FONT_H1.getSize();
    firstSiteYOffSet += selAxisHeight;
    addTable(writer, doc, table, cHeight, cWidths, firstSiteYOffSet, rowCount,
      colsModel.length, selAxisHeight);
  }

  private final float calcSelectionAxisHeight(Document doc, Axis selAxis) {
    AxisHierarchy axisHierarchies[] = selAxis.getAxisHierarchies();
    int width = axisHierarchies.length * 80 - 5;
    if (width <= 0) {
      return 12F;
    } else {
      float pdfWidth = doc.getPageSize().getWidth() - doc.leftMargin()
        - doc.rightMargin();
      int reps = (int) ((float) width / pdfWidth);
      return (float) (reps + 1) * 32F;
    }
  }

  private final float calcHorizontalAxisHeight(float verticalAxisWidth,
    Document doc, int numberOfDims, int emptyCellRowCount) {
    int width = numberOfDims * 80 - 5;
    if (width <= 0)
      return 0.0F;
    if ((double) verticalAxisWidth < 0.0D)
      verticalAxisWidth = 32F;
    float val = getColPos(doc, emptyCellRowCount, cWidths) + doc.leftMargin()
      + verticalAxisWidth;
    float pdfWidth = doc.getPageSize().getWidth() - doc.rightMargin() - val;
    int reps = (int) ((float) width / pdfWidth);
    return (float) (reps + 1) * 16F;
  }

  private final float calcVerticalAxisWidth(float selAxisHeight,
    float horizontalAxisHeight, Document doc, int numberOfDims) {
    int height = numberOfDims * 80 - 5;
    if (height <= 0)
      return 0.0F;
    if ((double) selAxisHeight < 0.0D)
      selAxisHeight = 32F;
    if ((double) horizontalAxisHeight < 0.0D)
      horizontalAxisHeight = 16F;
    float pdfHeight = doc.getPageSize().getHeight() - firstSiteYOffSet - 9F
      - horizontalAxisHeight - selAxisHeight - headerRows;
    int reps = (int) ((float) height / pdfHeight);
    return (float) (reps + 1) * 16F;
  }

  private final void addSelectionAxisHeader(Document doc, Axis selAxis,
    float selAxisHeight) {
    PdfContentByte cb = writer.getDirectContent();
    float pdfWidth = doc.getPageSize().getWidth() - doc.leftMargin()
      - doc.rightMargin();
    int width = selAxis.getAxisHierarchies().length * 80 - 5;
    if (width <= 0)
      return;
    PdfTemplate tp = cb.createTemplate(pdfWidth, selAxisHeight);
    Graphics2D g2d = tp.createGraphics(pdfWidth, selAxisHeight);
    int xOffset = 0;
    int yOffset = 0;
    g2d.setFont(g2d.getFont().deriveFont(8F));
    AxisHierarchy axisHierarchies[] = selAxis.getAxisHierarchies();
    for (int i = 0; i < axisHierarchies.length; i++) {
      if ((float) (xOffset + 75) > pdfWidth) {
        xOffset = 0;
        yOffset += 32;
      }
      xOffset = renderSelectionHierarchy(g2d, axisHierarchies[i], xOffset, yOffset);
    }

    g2d.dispose();
    float offset = config.isShowTitle() ? 18F + selAxisHeight : selAxisHeight - 9F;
    cb.addTemplate(tp, doc.leftMargin(), doc.getPageSize().getHeight()
      - doc.topMargin() - offset);
  }

  private final void addHorizontalAxisHeader(Document doc, AxisItem colsModel[][],
    AxisItem rowsModel[][]) {
    PdfContentByte cb = writer.getDirectContent();
    PdfTemplate tp = cb.createTemplate(400F, 16F);
    Graphics2D g2d = tp.createGraphics(400F, 16F);
    int xOffset = 0;
    int yOffset = 0;
    g2d.setFont(g2d.getFont().deriveFont(8F));
    for (int i = 0; i < colsModel.length; i++)
      xOffset = renderHierarchyName(g2d, colsModel[i][0], xOffset, yOffset, false);

    g2d.dispose();
    float left = 10F;
    int emptyCellRowCount = getMaxDimDepth(rowsModel[0]);
    float val = getColPos(doc, emptyCellRowCount, cWidths) + doc.leftMargin() + 32F;
    left = val;
    cb.addTemplate(tp, left, doc.top() - firstSiteYOffSet - 16F);
  }

  private final void addVerticalAxisHeader(Document doc, AxisItem colsModel[][],
    AxisItem rowsModel[][], float horizontalAxisHeight, float selAxisHeight) {
    PdfContentByte cb = writer.getDirectContent();
    float pdfHeight = doc.getPageSize().getHeight() - firstSiteYOffSet - 9F
      - horizontalAxisHeight - selAxisHeight - headerRows;
    PdfTemplate tp = cb.createTemplate(128F, pdfHeight);
    Graphics2D g2d = tp.createGraphics(128F, pdfHeight);
    int xPosition = (int) pdfHeight;
    int yOffset = (int) pdfHeight;
    g2d.setFont(g2d.getFont().deriveFont(8F));
    g2d.rotate(4.7123889802500001D, 0.0D, yOffset);
    for (int i = rowsModel[0].length - 1; i >= 0; i--) {
      if (xPosition - 75 - 10 <= 0) {
        xPosition = (int) pdfHeight;
        yOffset -= 16;
      }
      int xOffset = renderHierarchyName(g2d, rowsModel[0][i], xPosition, yOffset,
        true);
      int diff = xOffset - xPosition;
      xPosition -= diff;
    }

    g2d.dispose();
    float yoff = doc.getPageSize().getHeight() - firstSiteYOffSet - 9F
      - horizontalAxisHeight - selAxisHeight - headerRows;
    cb.addTemplate(tp, doc.leftMargin(), headerRows);
  }

  private final String shorten(String text, int maxWidth, FontMetrics fm) {
    int textWidth = fm.stringWidth(text);
    String origName = text;
    int origLength = origName.length();
    for (; textWidth > maxWidth; textWidth = fm.stringWidth(text)) {
      if (origLength > 40) {
        text = origName.substring(0, 40);
        origName = text;
        origLength = 40;
      }
      origName = origName.substring(0, origLength - 1);
      origLength--;
      text = (new StringBuilder(String.valueOf(origName))).append("...").toString();
    }

    return text;
  }

  private final int renderHierarchyName(Graphics2D g2d, AxisItem item, int xOffset,
    int yOffset, boolean rightAligned) {
    String name = item.getHierarchy().getName();
    int width = 75;
    int height = g2d.getFontMetrics().getHeight();
    FontMetrics fm = g2d.getFontMetrics();
    name = shorten(name, width - 10, fm);
    int textWidth = g2d.getFontMetrics().stringWidth(name);
    g2d.setColor(new Color(248, 248, 248));
    g2d.fillRoundRect(xOffset, yOffset, width, height + 1, 5, 5);
    g2d.setColor(Color.black);
    g2d.drawRoundRect(xOffset, yOffset, width, height + 1, 5, 5);
    if (rightAligned)
      g2d.drawString(name, (xOffset + 75) - 5 - textWidth, (yOffset + height) - 2);
    else
      g2d.drawString(name, xOffset + 5, (yOffset + height) - 2);
    return xOffset + width + 5;
  }

  private final int renderSelectionHierarchy(Graphics2D g2d, AxisHierarchy hier,
    int xOffset, int yOffset) {
    String name = hier.getHierarchy().getName();
    int width = 75;
    int fontHeight = g2d.getFontMetrics().getHeight();
    int height = fontHeight * 2 + 1;
    FontMetrics fm = g2d.getFontMetrics();
    java.awt.Font plainF = g2d.getFont();
    java.awt.Font f = g2d.getFont().deriveFont(1);
    FontMetrics fmbold = g2d.getFontMetrics(f);
    name = shorten(name, width - 10, fmbold);
    String elementName = "";
    if (hier.getSelectedElements() != null && hier.getSelectedElements().length > 0
      && hier.getSelectedElements()[0] != null)
      elementName = shorten(hier.getSelectedElements()[0].getName(), width - 10, fm);
    g2d.setColor(new Color(248, 248, 248));
    g2d.fillRoundRect(xOffset, yOffset, width, height + 1, 5, 5);
    g2d.setColor(Color.black);
    g2d.drawRoundRect(xOffset, yOffset, width, height + 1, 5, 5);
    g2d.drawLine(xOffset, yOffset + height / 2 + 1, (xOffset + width) - 1, yOffset
      + height / 2 + 1);
    g2d.setFont(f);
    g2d.drawString(name, xOffset + 5, (yOffset + fontHeight) - 2);
    g2d.setFont(plainF);
    g2d.drawString(elementName, xOffset + 5, (yOffset + fontHeight * 2) - 1);
    return xOffset + width + 5;
  }

  private void addTable(PdfWriter writer, Document doc, PdfPTable table,
    float colHeight, float widths[], float yOffset, int rows, int colHeaderRows,
    float selAxisHeight) {
    Rectangle ps = doc.getPageSize();
    int startRow = 0;
    int page = 0;
    for (int remainingRows = rows; remainingRows > 0;) {
      int cStart = 0;
      float sy = ps.getHeight() - doc.topMargin();
      if (page == 0)
        sy = ps.getHeight() - yOffset - 32F;
      float height = sy - doc.bottomMargin();
      int rowsPerPage = (int) (height / colHeight);
      if (page == 0) {
        int diff = (int) ((headerRows - (float) colHeaderRows * colHeight) / colHeight);
        rowsPerPage -= diff;
        rowsPerPage++;
      } else {
        rowsPerPage++;
      }
      do {
        int cEnd = getColEnd(doc, cStart, widths, page);
        if (cEnd == -1) {
          isExtPage = false;
          break;
        }
        isExtPage = cStart != 0;
        table.writeSelectedRows(cStart, cEnd, startRow, startRow + rowsPerPage, doc
          .leftMargin(), sy, writer.getDirectContent());
        doc.newPage();
        cStart = cEnd;
      } while (true);
      startRow += rowsPerPage;
      remainingRows -= rowsPerPage;
      if (remainingRows > 0)
        page++;
    }

  }

  private static float getColPos(Document doc, int col, float widths[]) {
    if (col >= widths.length)
      return 0.0F;
    float sum = 0.0F;
    for (int i = 0; i < col; i++)
      sum += widths[i];

    return sum;
  }

  private static int getColEnd(Document doc, int startCol, float widths[], int page) {
    if (startCol >= widths.length)
      return -1;
    Rectangle ps = doc.getPageSize();
    float width = ps.getWidth() - doc.leftMargin() - doc.rightMargin();
    float sum = 0.0F;
    for (int i = startCol; i < widths.length; i++) {
      if (sum + widths[i] > width)
        return i;
      sum += widths[i];
    }

    return widths.length;
  }

  private final void fill(Element coord[], AxisItem item, HashMap hierIndex) {
    if (item == null) {
      return;
    } else {
      coord[((Integer) hierIndex.get(item.getHierarchy())).intValue()] = item
        .getElement();
      fill(coord, item.getParentInPrevHierarchy(), hierIndex);
      return;
    }
  }

  private final String getString(Element coord[]) {
    if (coord == null)
      return "";
    StringBuffer b = new StringBuffer();
    Element aelement[];
    int j = (aelement = coord).length;
    for (int i = 0; i < j; i++) {
      Element e = aelement[i];
      if (e != null)
        b.append((new StringBuilder(String.valueOf(e.hashCode()))).append(",")
          .toString());
    }

    return b.toString();
  }

  public File getOutFile() {
    if (outFile != null)
      return outFile;
    if (config == null) {
      if (!tempDir.exists())
        tempDir.mkdir();
      return getOutFileChecked(tempDir);
    } else {
      return getOutFileChecked(new File(config.getPath()));
    }
  }

  private File getOutFileChecked(File dir) {
    String name;
    name = view.getName().replace(" ", "_");
    name = name.replaceAll("/", "_");
    name = name.replaceAll("\\\\", "_");
    name = name.replaceAll(":", "_");
    name = name.replaceAll("\\*", "_");
    name = name.replaceAll("\\?", "_");
    name = name.replaceAll("\"", "_");
    name = name.replaceAll("<", "_");
    name = name.replaceAll(">", "_");
    name = name.replaceAll("\\|", "_");
    File f;
    try {
      f = File.createTempFile((new StringBuilder(String.valueOf(name))).append("_(")
        .toString(), ").pdf", dir);
      return f;
    } catch (IOException e) {
      e.printStackTrace();
      f = new File(dir, (new StringBuilder(String.valueOf(name))).append(".pdf")
        .toString());
      if (!f.exists())
        return f;
      int n = 2;
      do {
        name = (new StringBuilder(String.valueOf(view.getName().replace(" ", "_"))))
          .append("_").append(Integer.toString(n++)).append(".pdf").toString();
        f = new File(dir, name);
      } while (f.exists());
    }
    return f;
  }

  private PdfPTable createTable(int cols) {
    PdfPTable table = new PdfPTable(cols);
    return table;
  }

  private PdfPCell createCell(String t, Format f) {
    Phrase phrase = new Phrase(formatNumber(t, null), FONT_CELL);
    PdfPCell c = new PdfPCell(phrase);
    c.setVerticalAlignment(4);
    c.setHorizontalAlignment(2);
    c.setPaddingTop(0.25F);
    c.setPaddingBottom(c.getPaddingBottom() + 0.25F);
    if (f != null)
      applyFormat(f, c);
    return c;
  }

  private PdfPCell createHCell(String h, int colSpan, AxisItem item, boolean row) {
    String orig = h;
    int length = h.length();
    if (row) {
      StringBuffer buf = new StringBuffer();
      for (int i = 0; i < item.getLevel(); i++)
        buf.append("    ");

      for (float rhmw = BASE_FONT_CELL_H.getWidthPoint(buf.toString(), FONT_CELL_H
        .getSize())
        + rowHeadersMaxWidth; BASE_FONT_CELL_H.getWidthPoint(h, FONT_CELL_H
        .getSize()) > rhmw
        && length > 2; h = (new StringBuilder(String.valueOf(orig.substring(0,
        length)))).append("...").toString())
        length--;

    } else {
      for (; BASE_FONT_CELL_H.getWidthPoint(h, FONT_CELL_H.getSize()) > columsMaxWidth
        && length > 2; h = (new StringBuilder(String.valueOf(orig.substring(0,
        length)))).append("...").toString())
        length--;

    }
    PdfPCell c = null;
    if (item == null) {
      Phrase ph = new Phrase(h, FONT_CELL_H);
      c = new PdfPCell(ph);
    } else {
      int depth = item.getElementNode().getDepth();
      if (item.hasChildren()) {
        if (item.hasState(2)) {
          java.net.URL url = getClass().getResource("minus2.png");
          Phrase ph = null;
          try {
            Image image = Image.getInstance(url);
            image.scaleAbsolute(6F, 6F);
            ph = new Phrase();
            if (row) {
              if (config.isIndent()) {
                for (int i = 0; i < depth; i++) {
                  ph.add(new Chunk("    ", FONT_CELL_H));
                  h = h.substring("    ".length());
                }

              }
            } else if (config.isIndent()) {
              for (int i = 0; i < depth; i++) {
                ph.add(new Chunk("\r\n", FONT_CELL_H));
                h = h.substring(2);
              }

            }
            if (config.isShowExpansionStates()) {
              ph.add(new Chunk(image, 0.0F, 0.0F));
              ph.add(new Chunk((new StringBuilder(" ")).append(h).toString(),
                FONT_CELL_H));
            } else {
              ph.add(new Chunk(h, FONT_CELL_H));
            }
          } catch (Exception e) {
            ph = new Phrase(h, FONT_CELL_H);
          }
          c = new PdfPCell(ph);
        } else {
          java.net.URL url = getClass().getResource("plus2.png");
          Phrase ph = null;
          try {
            Image image = Image.getInstance(url);
            image.scaleAbsolute(6F, 6F);
            ph = new Phrase();
            if (row) {
              if (config.isIndent()) {
                for (int i = 0; i < depth; i++) {
                  ph.add(new Chunk("    ", FONT_CELL_H));
                  h = h.substring("    ".length());
                }

              }
            } else if (config.isIndent()) {
              for (int i = 0; i < depth; i++) {
                ph.add(new Chunk("\r\n", FONT_CELL_H));
                h = h.substring(2);
              }

            }
            if (config.isShowExpansionStates()) {
              ph.add(new Chunk(image, 0.0F, 0.0F));
              ph.add(new Chunk((new StringBuilder(" ")).append(h).toString(),
                FONT_CELL_H));
            } else {
              ph.add(new Chunk(h, FONT_CELL_H));
            }
          } catch (Exception e) {
            ph = new Phrase(h, FONT_CELL_H);
          }
          c = new PdfPCell(ph);
        }
      } else {
        Phrase ph = new Phrase(h, FONT_CELL_H);
        c = new PdfPCell(ph);
      }
    }
    c.setColspan(colSpan);
    c.setBackgroundColor(COLOR_CELL_BG);
    c.setPaddingTop(0.25F);
    c.setPaddingBottom(c.getPaddingBottom() + 0.25F);
    return c;
  }

  private PdfPCell createInvisibleHCell() {
    Phrase ph = new Phrase("", FONT_CELL_H);
    PdfPCell c = new PdfPCell(ph);
    c.setBorder(0);
    c.setPaddingTop(0.25F);
    c.setPaddingBottom(c.getPaddingBottom() + 0.25F);
    return c;
  }

  public int traverseVisible(AxisItem item, HashMap emptyItems) {
    ArrayList result = new ArrayList();
    result.add(Integer.valueOf(0));
    traverseVisible(item, null, null, result, true, emptyItems);
    return ((Integer) result.get(0)).intValue();
  }

  public void traverseVisible(AxisItem item, AxisItem parent,
    AxisItem parentInPrevHierarchy, ArrayList result, boolean start,
    HashMap emptyItems) {
    if (item.hasRootsInNextHierarchy()) {
      AxisItem aaxisitem[];
      int k = (aaxisitem = item.getRootsInNextHierarchy()).length;
      for (int i = 0; i < k; i++) {
        AxisItem root = aaxisitem[i];
        traverseVisible(root, null, item, result, false, emptyItems);
      }

    } else {
      if (!emptyItems.containsKey(item)
        || !((Boolean) emptyItems.get(item)).booleanValue())
        result.set(0, Integer.valueOf(((Integer) result.get(0)).intValue() + 1));
      if (item.hasChildren() && item.hasState(2) && !start) {
        AxisItem aaxisitem1[];
        int l = (aaxisitem1 = item.getChildren()).length;
        for (int j = 0; j < l; j++) {
          AxisItem kid = aaxisitem1[j];
          traverseVisible(kid, item, parentInPrevHierarchy, result, false,
            emptyItems);
        }

      }
    }
  }

  private final int numberOfRemainingEmptyColumns(ArrayList emptyColumns, int c) {
    int result = 0;
    for (Iterator iterator = emptyColumns.iterator(); iterator.hasNext();) {
      Integer ec = (Integer) iterator.next();
      if (ec.intValue() > c)
        result++;
    }

    return result;
  }

  private final void createHeaderCells(PdfPTable table, AxisItem rowModel[][],
    AxisItem colModel[][], ArrayList emptyColumns) {
    int colLeaf = colModel.length - 1;
    headerRows = 0.0F;
    int emptyCellRowCount = getMaxDimDepth(rowModel[0]);
    PdfPCell header[][] = new PdfPCell[colModel.length][];
    HashMap offset = new HashMap();
    float headerHeight[] = new float[colModel.length];
    for (int i = 0; i < colModel.length; i++) {
      headerHeight[i] = -1F;
      header[i] = new PdfPCell[colModel[colLeaf].length + emptyCellRowCount];
      for (int j = 0; j < emptyCellRowCount; j++) {
        header[i][j] = createInvisibleHCell();
        calcColWidth(true, j, header[i][j], "", -1);
        offset.put(Integer.valueOf(i), Integer.valueOf(j));
      }

    }

    HashMap emptyItems = new HashMap();
    for (int c = 0; c < colModel[colLeaf].length; c++)
      emptyItems.put(colModel[colLeaf][c], Boolean.valueOf(emptyColumns
        .contains(Integer.valueOf(c))));

    HashMap isEmpty = new HashMap();
    HashMap colSpan = new HashMap();
    HashMap contents = new HashMap();
    for (int c = 0; c < colModel[colLeaf].length; c++) {
      AxisItem itemInPrevHier = colModel[colLeaf][c].getParentInPrevHierarchy();
      if (!emptyColumns.contains(Integer.valueOf(c))) {
        AxisItem origItem = colModel[colLeaf][c];
        for (int j = 0; j < colModel.length; j++) {
          AxisItem item = origItem;
          for (int counter = colLeaf; j < counter; counter--)
            item = item.getParentInPrevHierarchy();

          ItemPair ip = new ItemPair(origItem.getParentInPrevHierarchy(), j);
          if (!isEmpty.containsKey(ip))
            isEmpty.put(ip, Integer.valueOf(0));
          if (((Integer) isEmpty.get(ip)).intValue() > 0) {
            isEmpty.put(ip, Integer
              .valueOf(((Integer) isEmpty.get(ip)).intValue() - 1));
          } else {
            int val = ((Integer) offset.get(Integer.valueOf(j))).intValue() + 1;
            offset.put(Integer.valueOf(j), Integer.valueOf(val));
            String tv = getVerticalIndentName(item.getName(), item.getElementNode()
              .getDepth());
            int width = traverseVisible(item, emptyItems);
            header[j][val] = createHCell(tv, width, item, false);
            if (item.hasChildren() && config.isShowExpansionStates())
              tv = (new StringBuilder(String.valueOf(tv))).append("  ").toString();
            contents.put(new Pair(j, val), tv);
            colSpan.put(header[j][val], Integer.valueOf(width));
            if (width > 0)
              width--;
            isEmpty.put(ip, Integer.valueOf(width));
          }
        }

      }
    }

    for (int i = 0; i < header.length; i++) {
      for (int j = 0; j <= ((Integer) offset.get(Integer.valueOf(i))).intValue(); j++) {
        String tv = (String) contents.get(new Pair(i, j));
        if (tv == null)
          tv = "";
        calcColWidth(true, j, header[i][j], tv, -1);
        table.addCell(header[i][j]);
      }

      rowCount++;
    }

  }

  private final String getIndentName(String h, int depth) {
    String orig = h;
    int length = h.length();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < depth; i++)
      buf.append("    ");

    for (float rhmw = BASE_FONT_CELL_H.getWidthPoint(buf.toString(), FONT_CELL_H
      .getSize())
      + rowHeadersMaxWidth; BASE_FONT_CELL_H.getWidthPoint(h, FONT_CELL_H.getSize()) > rhmw
      && length > 2; h = (new StringBuilder(String
      .valueOf(orig.substring(0, length)))).append("...").toString())
      length--;

    StringBuffer name = new StringBuffer();
    if (config.isIndent()) {
      for (int i = 0; i < depth; i++)
        name.append("    ");

    }
    name.append(h);
    return name.toString();
  }

  private final String getVerticalIndentName(String h, int depth) {
    String orig = h;
    for (int length = h.length(); BASE_FONT_CELL_H.getWidthPoint(h, FONT_CELL_H
      .getSize()) > columsMaxWidth
      && length > 2; h = (new StringBuilder(String
      .valueOf(orig.substring(0, length)))).append("...").toString())
      length--;

    StringBuffer name = new StringBuffer();
    if (config.isIndent()) {
      for (int i = 0; i < depth; i++)
        name.append("\r\n");

    }
    name.append(h);
    return name.toString();
  }

  private final void createRowCells(PdfPTable table, AxisItem rowModel[][],
    AxisItem colModel[][], ArrayList emptyColumns, ArrayList emptyRows,
    Cell cellValues[]) {
    int cellPosition = 0;
    int columnCount = colModel[colModel.length - 1].length;
    ArrayList allCells = new ArrayList();
    ArrayList lastRow = new ArrayList();
    HashMap isEmpty = new HashMap();
    HashMap lastCell = new HashMap();
    HashMap emptyItems = new HashMap();
    for (int i = 0; i < rowModel.length; i++)
      emptyItems.put(rowModel[i][rowModel[i].length - 1], Boolean.valueOf(emptyRows
        .contains(isEmpty)));

    for (int i = 0; i < rowModel.length; i++) {
      int col = 0;
      if (emptyRows.contains(Integer.valueOf(i))) {
        for (Iterator iterator2 = isEmpty.keySet().iterator(); iterator2.hasNext();) {
          Integer j = (Integer) iterator2.next();
          int val = ((Integer) isEmpty.get(j)).intValue();
          if (val != 0 && val - 1 == 0 && lastCell.get(j) != null)
            ((PdfPCell) lastCell.get(j)).setBorder(14);
        }

        cellPosition += columnCount;
      } else {
        lastRow.clear();
        for (int j = 0; j < rowModel[i].length; j++) {
          AxisItem item = rowModel[i][j];
          if (!isEmpty.containsKey(Integer.valueOf(j)))
            isEmpty.put(Integer.valueOf(j), Integer.valueOf(0));
          if (((Integer) isEmpty.get(Integer.valueOf(j))).intValue() != 0) {
            isEmpty.put(Integer.valueOf(j), Integer.valueOf(((Integer) isEmpty
              .get(Integer.valueOf(j))).intValue() - 1));
            PdfPCell cell = createHCell("", 1, null, false);
            int bits = 12;
            if (((Integer) isEmpty.get(Integer.valueOf(j))).intValue() == 0)
              bits |= 2;
            cell.setBorder(bits);
            calcColWidth(true, col, cell, "", item.getLevel());
            allCells.add(cell);
            lastCell.put(Integer.valueOf(j), cell);
            lastRow.add(cell);
          } else {
            String tv = item.getName();
            int width = traverseVisible(item, emptyItems);
            tv = getIndentName(tv, item.getElementNode().getDepth());
            PdfPCell cell = createHCell(tv, 1, item, true);
            item.hasChildren();
            calcColWidth(true, col, cell, tv, item.getLevel());
            int bits = 13;
            if (width == 1 || width == 0)
              bits |= 2;
            cell.setBorder(bits);
            allCells.add(cell);
            lastRow.add(cell);
            if (width > 0)
              width--;
            isEmpty.put(Integer.valueOf(j), Integer.valueOf(width));
            lastCell.put(Integer.valueOf(j), cell);
            if (firstCell == null)
              firstCell = cell;
          }
          col++;
        }

        int start = i * columnCount;
        int end = start + columnCount;
        for (int c = start; c < end; c++)
          if (emptyColumns.contains(Integer.valueOf(c - start))) {
            cellPosition++;
          } else {
            String tv = cellValues[cellPosition++].getValue().toString();
            PdfPCell cell = createCell(tv, getFormat(i, c - start,
              (Element[]) indexCellMap.get((new StringBuilder(String.valueOf(i)))
                .append(";").append(c - start).toString())));
            calcColWidth(false, col, cell, tv, -1);
            allCells.add(cell);
            lastRow.add(cell);
            col++;
          }

        rowCount++;
      }
    }

    PdfPCell c;
    int bits;
    for (Iterator iterator = lastRow.iterator(); iterator.hasNext(); c
      .setBorder(bits)) {
      c = (PdfPCell) iterator.next();
      bits = 14;
      if ((c.getBorder() & 1) != 0)
        bits |= 1;
    }

    for (Iterator iterator1 = allCells.iterator(); iterator1.hasNext(); table
      .addCell(c))
      c = (PdfPCell) iterator1.next();

  }

  private float calcWidth(int column, PdfPCell cell, String tv) {
    if (cHeight == -1F)
      cHeight = FONT_CELL_H.getSize() + cell.getPaddingBottom()
        + cell.getPaddingTop() + cell.getBorderWidth();
    float cm = cell.getBorderWidth() + cell.getPaddingLeft()
      + cell.getPaddingRight();
    float w = BASE_FONT_CELL_H.getWidthPoint(tv, FONT_CELL_H.getSize());
    w += cm;
    return w + cm;
  }

  private void calcColWidth(boolean header, int column, PdfPCell cell, String tv,
    int rowDepth) {
    String orig = tv;
    int length = tv.length();
    if (header)
      if (rowDepth > -1) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < rowDepth; i++)
          buf.append("    ");

        for (float rhmw = BASE_FONT_CELL_H.getWidthPoint(buf.toString(), FONT_CELL_H
          .getSize())
          + rowHeadersMaxWidth; BASE_FONT_CELL_H.getWidthPoint(tv, FONT_CELL_H
          .getSize()) > rhmw
          && length > 2; tv = (new StringBuilder(String.valueOf(orig.substring(0,
          length)))).append("...").toString())
          length--;

      } else {
        for (; BASE_FONT_CELL_H.getWidthPoint(tv, FONT_CELL_H.getSize()) > columsMaxWidth
          && length > 2; tv = (new StringBuilder(String.valueOf(orig.substring(0,
          length)))).append("...").toString())
          length--;

      }
    if (cHeight == -1F)
      cHeight = FONT_CELL_H.getSize() + cell.getPaddingBottom()
        + cell.getPaddingTop();
    float cm = cell.getBorderWidth() + cell.getPaddingLeft()
      + cell.getPaddingRight();
    int imWidth = 0;
    if (cell.getPhrase().getChunks().size() >= 2 && config.isShowExpansionStates())
      imWidth = 9;
    float w = header ? BASE_FONT_CELL_H.getWidthPoint(tv, FONT_CELL_H.getSize())
      + (float) imWidth : BASE_FONT_CELL.getWidthPoint(formatNumber(tv, null),
      FONT_CELL.getSize());
    w += cm;
    if (cell.getColspan() == 1 || cell.getColspan() == 0) {
      if (column < cWidths.length && w > cWidths[column])
        cWidths[column] = w;
    } else {
      float wPerCol = w / (float) cell.getColspan();
      for (int i = 0; i < cell.getColspan(); i++)
        if (column + i < cWidths.length && wPerCol > cWidths[column + i])
          cWidths[column + i] = wPerCol;

    }
  }

  private int getMaxDimDepth(AxisItem roots[]) {
    int localMax = 0;
    AxisItem aaxisitem[];
    int j = (aaxisitem = roots).length;
    for (int i = 0; i < j; i++) {
      AxisItem root = aaxisitem[i];
      int sum = 1 + getMaxDimDepth(root.getRootsInNextHierarchy());
      if (sum > localMax)
        localMax = sum;
    }

    return localMax;
  }

  private final int getWidth(AxisItem roots[]) {
    if (roots == null || roots.length == 0)
      return 0;
    int result = 0;
    int n = roots.length;
    for (int i = 0; i < n; i++) {
      result += getWidth(roots[i].getChildren());
      result += getWidth(roots[i].getRootsInNextHierarchy());
    }

    return result + n;
  }

  private final Format getFormat(int row, int col, Element coords[]) {
    Format f = null;
    if (coords != null) {
      Element aelement[];
      int l = (aelement = coords).length;
      for (int j = 0; j < l; j++) {
        Element e = aelement[j];
        boolean valid = true;
        if (allDimensionLevels.containsKey(e.getDimension())) {
          valid = false;
          for (Iterator iterator = ((ArrayList) allDimensionLevels.get(e
            .getDimension())).iterator(); iterator.hasNext();) {
            LevelRangeInfo i = (LevelRangeInfo) iterator.next();
            if (i.level == e.getLevel()) {
              valid = true;
              f = i.format;
              break;
            }
          }

          if (!valid)
            return null;
        }
      }

      if (f != null)
        return f;
    }
    IndexRangeInfo aindexrangeinfo[];
    int i1 = (aindexrangeinfo = indexRanges).length;
    for (int k = 0; k < i1; k++) {
      IndexRangeInfo r = aindexrangeinfo[k];
      if (row == r.row && col == r.col)
        return r.format;
    }

    return null;
  }

  private final void applyFormat(Format f, PdfPCell cell) {
    Phrase phrase = cell.getPhrase();
    if (f.getBackgroundColor() != null) {
      ColorDescriptor bg = f.getBackgroundColor();
      if (bg != null)
        cell.setBackgroundColor(PDFUtils.convertColor(bg));
    }
    BorderData bd[] = f.getBorderData();
    if (bd != null && bd.length > 0)
      cell.setCellEvent(new BorderPdfPCellEvent(bd));
    for (int i = 0; i < bd.length;) {
      BorderData bData = bd[i];
      switch (bData.getLinePosition()) {
      case 0: // '\0'
      case 1: // '\001'
      case 2: // '\002'
      case 3: // '\003'
      case 4: // '\004'
      case 5: // '\005'
      default:
        i++;
        break;
      }
    }

    Font font = phrase.getFont();
    if (font != null) {
      if (f.getForegroundColor() != null)
        font.setColor(PDFUtils.convertColor(f.getForegroundColor()));
      FontDescriptor fDesc = f.getFontData();
      if (fDesc != null) {
        font.setStyle(PDFUtils.convertFontStyle(fDesc));
        font.setSize(fDesc.getSize());
      }
    }
  }

  private final String applyNumberFormat(String text) {
    try {
      Double d = Double.valueOf(Double.parseDouble(text));
      return DEFAULT_FORMATTER.format(d);
    } catch (NumberFormatException e) {

    }
    return text;
  }

  private final String formatNumber(Object number, String numberFormat) {
    String result = null;
    try {
      if (numberFormat == null)
        numberFormat = getDefaultNumberFormat();
      try {
        Double d = Double.valueOf(Double.parseDouble(number.toString()));
        DecimalFormat df = new DecimalFormat(numberFormat);
        result = df.format(d);
      } catch (Throwable throwable) {
      }
    } catch (IllegalArgumentException illegalargumentexception) {
    }
    if (result == null)
      result = number.toString();
    if (BASE_FONT_CELL.getWidthPoint(result, FONT_CELL.getSize()) > columsMaxWidth)
      result = cellReplacementString;
    return result;
  }

  private static final String getDefaultNumberFormat() {
    return "#,##0.00";
  }

  public static final BorderData[] getDummyBorderData() {
    BorderData bd[] = new BorderData[4];
    bd[0] = new BorderData(1, 1, new ColorDescriptor(0, 0, 0), 0);
    bd[1] = new BorderData(1, 1, new ColorDescriptor(0, 0, 0), 2);
    bd[2] = new BorderData(1, 2, new ColorDescriptor(255, 0, 0), 3);
    bd[3] = new BorderData(1, 1, new ColorDescriptor(0, 0, 0), 5);
    return bd;
  }

  private static final DecimalFormat DEFAULT_FORMATTER = new DecimalFormat(
    "#,##0.00");

  private static final Font FONT_H1;

  private static final Font FONT_CELL;

  private static final Font FONT_CELL_H;

  private static final BaseFont BASE_FONT_CELL;

  private static final BaseFont BASE_FONT_CELL_H;

  private static final Font FONT_PNUMBER;

  private static final BaseColor COLOR_CELL_BG = new BaseColor(248, 248, 248);

  private static final File tempDir;

  private static final int DIM_OBJECT_WIDTH = 75;

  private PaloPDFExporterConfiguration config;

  private CubeView view;

  private int rowCount;

  private IndexRangeInfo indexRanges[];

  private float cWidths[];

  private float cHeight;

  private float headerRows;

  private float firstSiteYOffSet;

  private HashMap allDimensionLevels;

  private HashMap indexCellMap;

  private File outFile;

  private PdfWriter writer;

  private static final String INDENT = "    ";

  private float columsMaxWidth;

  private float rowHeadersMaxWidth;

  private final String cellReplacementString;

  private boolean isExtPage;

  private PdfPCell firstCell;

  static {
    FONT_PNUMBER = new Font(com.itextpdf.text.Font.FontFamily.COURIER, 9F);
    String user_home = System.getProperty("user.home");
    tempDir = new File(user_home, ".palotmp");
    FONT_H1 = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14F, 1);
    FONT_CELL = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8F, 0);
    FONT_CELL_H = new Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 8F, 1);
    BASE_FONT_CELL = FONT_CELL.getCalculatedBaseFont(false);
    BASE_FONT_CELL_H = FONT_CELL_H.getCalculatedBaseFont(false);
  }

}