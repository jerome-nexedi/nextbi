/*     */package org.palo.viewapi.exporters;

/*     */
/*     */import com.itextpdf.text.PageSize; /*     */
import com.itextpdf.text.Rectangle;

/*     */
/*     */public class PaloPDFExporterConfiguration
/*     */{
  /* 46 */public static final String[] PAGE_FORMATS = { "A0", "A1",
  /* 47 */"A2", "A3", "A4", "A5", "A6", "B0", "B1", "B2", "B3", "B4", "B5",
  /* 48 */"B6", "Executive", "Legal", "Letter" };

  /*     */
  /* 50 */private boolean portrait = true;

  /*     */private int pageFormat;

  /*     */private String title;

  /*     */private boolean showTitle;

  /*     */private boolean showPOV;

  /*     */private boolean showExpansionStates;

  /*     */private boolean indent;

  /*     */private boolean showPageNumbers;

  /*     */private String path;

  /*     */private String maxColString;

  /*     */private String maxRowsHeaderString;

  /*     */private String cellReplaceString;

  /*     */
  /*     */public static final PaloPDFExporterConfiguration createDefault()
  /*     */{
    /* 67 */PaloPDFExporterConfiguration conf = new PaloPDFExporterConfiguration();
    /* 68 */conf.portrait = true;
    /* 69 */conf.pageFormat = 0;
    /* 70 */return conf;
    /*     */}

  /*     */
  /*     */public void setPath(String s) {
    /* 74 */this.path = s;
    /*     */}

  /*     */
  /*     */public String getPath() {
    /* 78 */return this.path;
    /*     */}

  /*     */
  /*     */public void setTitle(String t) {
    /* 82 */this.title = t;
    /*     */}

  /*     */
  /*     */public String getTitle() {
    /* 86 */return this.title;
    /*     */}

  /*     */
  /*     */public void setShowTitle(boolean b) {
    /* 90 */this.showTitle = b;
    /*     */}

  /*     */
  /*     */public boolean isShowTitle() {
    /* 94 */return this.showTitle;
    /*     */}

  /*     */
  /*     */public void setShowPOV(boolean b) {
    /* 98 */this.showPOV = b;
    /*     */}

  /*     */
  /*     */public boolean isShowPOV() {
    /* 102 */return this.showPOV;
    /*     */}

  /*     */
  /*     */public void setShowExpansionStates(boolean b) {
    /* 106 */this.showExpansionStates = b;
    /*     */}

  /*     */
  /*     */public boolean isShowExpansionStates() {
    /* 110 */return this.showExpansionStates;
    /*     */}

  /*     */
  /*     */public void setIndent(boolean b) {
    /* 114 */this.indent = b;
    /*     */}

  /*     */
  /*     */public boolean isIndent() {
    /* 118 */return this.indent;
    /*     */}

  /*     */
  /*     */public void setShowPageNumbers(boolean b) {
    /* 122 */this.showPageNumbers = b;
    /*     */}

  /*     */
  /*     */public boolean isShowPageNumbers() {
    /* 126 */return this.showPageNumbers;
    /*     */}

  /*     */
  /*     */public void setPortrait(boolean b) {
    /* 130 */this.portrait = b;
    /*     */}

  /*     */
  /*     */public boolean isPortrait() {
    /* 134 */return this.portrait;
    /*     */}

  /*     */
  /*     */public void setPageFormat(int i) {
    /* 138 */if ((i < 0) || (i >= PAGE_FORMATS.length))
      /* 139 */throw new IllegalArgumentException("Illegal Page Format");
    /* 140 */this.pageFormat = i;
    /*     */}

  /*     */
  /*     */Rectangle getPageSize() {
    /* 144 */Rectangle ps = null;
    /* 145 */switch (this.pageFormat)
    /*     */{
    /*     */case 0:
      /* 147 */
      ps = PageSize.A0;
      /* 148 */break;
    /*     */case 1:
      /* 150 */
      ps = PageSize.A1;
      /* 151 */break;
    /*     */case 2:
      /* 153 */
      ps = PageSize.A2;
      /* 154 */break;
    /*     */case 3:
      /* 156 */
      ps = PageSize.A3;
      /* 157 */break;
    /*     */case 4:
      /* 159 */
      ps = PageSize.A4;
      /* 160 */break;
    /*     */case 5:
      /* 162 */
      ps = PageSize.A5;
      /* 163 */break;
    /*     */case 6:
      /* 165 */
      ps = PageSize.A6;
      /* 166 */break;
    /*     */case 7:
      /* 168 */
      ps = PageSize.B0;
      /* 169 */break;
    /*     */case 8:
      /* 171 */
      ps = PageSize.B1;
      /* 172 */break;
    /*     */case 9:
      /* 174 */
      ps = PageSize.B2;
      /* 175 */break;
    /*     */case 10:
      /* 177 */
      ps = PageSize.B3;
      /* 178 */break;
    /*     */case 11:
      /* 180 */
      ps = PageSize.B4;
      /* 181 */break;
    /*     */case 12:
      /* 183 */
      ps = PageSize.B5;
      /* 184 */break;
    /*     */case 13:
      /* 186 */
      ps = PageSize.B6;
      /* 187 */break;
    /*     */case 14:
      /* 189 */
      ps = PageSize.EXECUTIVE;
      /* 190 */break;
    /*     */case 15:
      /* 192 */
      ps = PageSize.LEGAL;
      /* 193 */break;
    /*     */case 16:
      /* 195 */
      ps = PageSize.LETTER;
      /* 196 */break;
    /*     */default:
      /* 198 */
      throw new IllegalStateException("invalid page format");
      /*     */
    }
    /* 200 */if (!this.portrait)
      /* 201 */ps = ps.rotate();
    /* 202 */return ps;
    /*     */}

  /*     */
  /*     */public void setMaxWidths(String maxColString,
    String maxRowsHeaderString, String cellReplaceString) {
    /* 206 */this.maxColString = maxColString;
    /* 207 */this.maxRowsHeaderString = maxRowsHeaderString;
    /* 208 */this.cellReplaceString = cellReplaceString;
    /*     */}

  /*     */
  /*     */public String getMaxColString() {
    /* 212 */return this.maxColString;
    /*     */}

  /*     */
  /*     */public String getMaxRowsHeaderString() {
    /* 216 */return this.maxRowsHeaderString;
    /*     */}

  /*     */
  /*     */public String getCellReplaceString() {
    /* 220 */return this.cellReplaceString;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloviewapi
 * .jar Qualified Name: org.palo.viewapi.exporters.PaloPDFExporterConfiguration
 * JD-Core Version: 0.5.4
 */