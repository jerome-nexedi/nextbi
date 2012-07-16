/*     */package com.tensegrity.palojava;

/*     */
/*     */public class ExportContextInfo
/*     */{
  /*     */private String conditionRepresentation;

  /*     */private int type;

  /*     */private int blocksize;

  /*     */private double progress;

  /*     */private volatile boolean useRules;

  /*     */private boolean baseCellsOnly;

  /*     */private boolean ignoreEmptyCells;

  /*     */private String[] exportAfterPath;

  /*     */private String[][] cellArea;

  /*     */
  /*     */public final synchronized void setConditionRepresentation(
    String conditionRepresentation)
  /*     */{
    /* 63 */this.conditionRepresentation = conditionRepresentation;
    /*     */}

  /*     */
  /*     */public final synchronized String getConditionRepresentation()
  /*     */{
    /* 70 */return this.conditionRepresentation;
    /*     */}

  /*     */
  /*     */public final synchronized boolean isBaseCellsOnly()
  /*     */{
    /* 80 */return this.baseCellsOnly;
    /*     */}

  /*     */
  /*     */public final synchronized void setBaseCellsOnly(boolean baseCellsOnly)
  /*     */{
    /* 90 */this.baseCellsOnly = baseCellsOnly;
    /*     */}

  /*     */
  /*     */public final synchronized boolean ignoreEmptyCells()
  /*     */{
    /* 100 */return this.ignoreEmptyCells;
    /*     */}

  /*     */
  /*     */public final synchronized void setIgnoreEmptyCells(
    boolean ignoreEmptyCells)
  /*     */{
    /* 110 */this.ignoreEmptyCells = ignoreEmptyCells;
    /*     */}

  /*     */
  /*     */public final synchronized int getBlocksize()
  /*     */{
    /* 119 */return this.blocksize;
    /*     */}

  /*     */
  /*     */public final synchronized void setBlocksize(int blocksize)
  /*     */{
    /* 126 */this.blocksize = blocksize;
    /*     */}

  /*     */
  /*     */public final synchronized int getType() {
    /* 130 */return this.type;
    /*     */}

  /*     */
  /*     */public final synchronized void setType(int type) {
    /* 134 */this.type = type;
    /*     */}

  /*     */
  /*     */public final synchronized double getProgress()
  /*     */{
    /* 142 */return this.progress;
    /*     */}

  /*     */
  /*     */public final synchronized void setProgress(double progress)
  /*     */{
    /* 149 */this.progress = progress;
    /*     */}

  /*     */
  /*     */public final synchronized String[] getExportAfter()
  /*     */{
    /* 159 */return this.exportAfterPath;
    /*     */}

  /*     */
  /*     */public final synchronized void setExportAfter(String[] exportAfterPath)
  /*     */{
    /* 168 */this.exportAfterPath = exportAfterPath;
    /*     */}

  /*     */
  /*     */public final synchronized String[][] getCellsArea()
  /*     */{
    /* 177 */return this.cellArea;
    /*     */}

  /*     */
  /*     */public final synchronized void setCellsArea(String[][] cellArea)
  /*     */{
    /* 185 */this.cellArea = cellArea;
    /*     */}

  /*     */
  /*     */public final boolean useRules()
  /*     */{
    /* 194 */return this.useRules;
    /*     */}

  /*     */
  /*     */public final void setUseRules(boolean useRules)
  /*     */{
    /* 204 */this.useRules = useRules;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.ExportContextInfo JD-Core Version:
 * 0.5.4
 */