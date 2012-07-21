/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palojava.CellInfo; /*     */
import com.tensegrity.palojava.CubeInfo; /*     */
import com.tensegrity.palojava.DbConnection; /*     */
import com.tensegrity.palojava.PaloException; /*     */
import org.palo.api.Cell; /*     */
import org.palo.api.Connection; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Database; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ExportDataset; /*     */
import org.palo.api.Hierarchy; /*     */
import org.palo.api.PaloAPIException;

/*     */
/*     */class ExportDatasetImpl
/*     */implements ExportDataset
/*     */{
  /*     */private final ExportContextImpl context;

  /*     */private final Cube cube;

  /*     */private final CubeInfo cubeInfo;

  /*     */private int index;

  /*     */private CellInfo[] cells;

  /*     */
  /*     */ExportDatasetImpl(Cube cube)
  /*     */{
    /* 67 */this.cube = cube;
    /* 68 */this.cubeInfo = ((CubeImpl) cube).getInfo();
    /* 69 */this.context = ((ExportContextImpl) cube.getExportContext());
    /*     */}

  /*     */
  /*     */final synchronized void start() {
    /* 73 */this.cells = getDataExportInternal();
    /* 74 */this.index = 0;
    /*     */}

  /*     */
  /*     */public final synchronized Cell getNextCell() {
    /* 78 */CellImpl cell = null;
    /* 79 */if (this.index < this.cells.length) {
      /* 80 */cell = new CellImpl(this.cube, this.cells[(this.index++)]);
      /*     */} else {
      /* 82 */this.cells = getDataExportInternal();
      /*     */
      /* 90 */if (!this.cube.getDatabase().getConnection().isLegacy())
        /* 91 */this.index = 0;
      /*     */else
        /* 93 */this.index = 1;
      /* 94 */if (this.index < this.cells.length) {
        /* 95 */cell = new CellImpl(this.cube, this.cells[(this.index++)]);
        /*     */}
      /*     */}
    /* 98 */return cell;
    /*     */}

  /*     */
  /*     */public final synchronized boolean hasNextCell()
  /*     */{
    /* 103 */if ((this.context == null) || (this.cells == null)
      || (this.cells.length == 0)) {
      /* 104 */return false;
      /*     */}
    /* 106 */return (this.index != this.cells.length)
      || (this.context.getProgress() < 1.0D);
    /*     */}

  /*     */
  /*     */private final CellInfo[] getDataExportInternal()
  /*     */{
    /*     */try
    /*     */{
      /* 118 */ConnectionImpl cimpl =
      /* 119 */(ConnectionImpl) this.cube.getDatabase().getConnection();
      /* 120 */CellInfo[] cells = cimpl.getConnectionInternal().getDataExport(
        this.cubeInfo,
        /* 121 */this.context.getInfo());
      /*     */
      /* 124 */if ((cells != null) && (cells.length > 0)) {
        /* 125 */int lastCell = cells.length - 1;
        /* 126 */String[] pathIds = cells[lastCell].getCoordinate();
        /* 127 */if (pathIds != null) {
          /* 128 */Element[] newExportAfterPath = this.context.getExportAfter();
          /* 129 */if (newExportAfterPath == null) {
            /* 130 */newExportAfterPath = new Element[pathIds.length];
            /*     */}
          /* 132 */for (int i = 0; i < pathIds.length; ++i) {
            /* 133 */newExportAfterPath[i] =
            /* 134 */this.cube.getDimensionAt(i).getDefaultHierarchy()
              .getElementById(pathIds[i]);
            /*     */}
          /* 136 */this.context.setExportAfter(newExportAfterPath);
          /*     */}
        /*     */}
      return cells;
      /*     */} catch (PaloException pex) {
      /* 140 */throw new PaloAPIException(pex);
      /*     */} catch (RuntimeException e) {
      /* 142 */throw new PaloAPIException(e.getMessage(), e);
      /*     */}

    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ExportDatasetImpl JD-Core Version: 0.5.4
 */