/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palojava.CellInfo; /*     */
import org.palo.api.Cell; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.Hierarchy;

/*     */
/*     */class CellImpl
/*     */implements Cell
/*     */{
  /*     */private final Cube cube;

  /*     */private final CellInfo cInfo;

  /*     */private CompoundKey coordKey;

  /*     */private final Element[] coordinate;

  /*     */
  /*     */CellImpl(Cube cube, CellInfo cInfo)
  /*     */{
    /* 61 */this(cube, cInfo, null);
    /*     */}

  /*     */
  /*     */CellImpl(Cube cube, CellInfo cInfo, Element[] coordinate) {
    /* 65 */this.cube = cube;
    /* 66 */this.cInfo = cInfo;
    /* 67 */this.coordinate = ((coordinate != null) ? (Element[]) coordinate
      .clone() : getCoordinate(cInfo));
    /* 68 */String[] ids = new String[this.coordinate.length];
    /* 69 */for (int i = 0; i < this.coordinate.length; ++i)
      /* 70 */ids[i] = this.coordinate[i].getId();
    /* 71 */this.coordKey = new CompoundKey(ids);
    /*     */}

  /*     */
  /*     */public final Cube getCube() {
    /* 75 */return this.cube;
    /*     */}

  /*     */
  /*     */public final Element[] getCoordinate() {
    /* 79 */return this.coordinate;
    /*     */}

  /*     */
  /*     */public final Element[] getPath() {
    /* 83 */return this.coordinate;
    /*     */}

  /*     */
  /*     */public String getRuleId() {
    /* 87 */return this.cInfo.getRule();
    /*     */}

  /*     */
  /*     */public boolean hasRule() {
    /* 91 */return this.cInfo.getRule() != null;
    /*     */}

  /*     */
  /*     */public final int getType() {
    /* 95 */return this.cInfo.getType();
    /*     */}

  /*     */
  /*     */public final Object getValue() {
    /* 99 */return this.cInfo.getValue();
    /*     */}

  /*     */
  /*     */public final boolean isConsolidated()
  /*     */{
    /* 120 */boolean consolidated = false;
    /* 121 */for (Element element : this.coordinate) {
      /* 122 */switch (element.getType())
      /*     */{
      /*     */case 2:
        /* 124 */
        consolidated = true;
        /* 125 */break;
      /*     */case 1:
        /*     */
      case 3:
        /* 128 */
        return false;
        /*     */
      }
      /*     */}
    /* 131 */return consolidated;
    /*     */}

  /*     */
  /*     */public final boolean isEmpty() {
    /* 135 */Object val = getValue();
    /* 136 */return (val == null) || (val.toString().equals(""));
    /*     */}

  /*     */
  /*     */public final boolean equals(Object obj) {
    /* 140 */if (obj instanceof Cell) {
      /* 141 */CellImpl other = (CellImpl) obj;
      /* 142 */return (this.cube.equals(other.cube))
        && (this.coordKey.equals(other.coordKey));
      /*     */}
    /* 144 */return false;
    /*     */}

  /*     */public final int hashCode() {
    /* 147 */int hc = 23;
    /* 148 */hc += 37 * this.cube.hashCode();
    /* 149 */hc += 37 + this.coordKey.hashCode();
    /* 150 */return hc;
    /*     */}

  /*     */
  /*     */final CellInfo getInfo()
  /*     */{
    /* 157 */return this.cInfo;
    /*     */}

  /*     */
  /*     */private final Element[] getCoordinate(CellInfo cInfo)
  /*     */{
    /* 165 */String[] ids = cInfo.getCoordinate();
    /*     */
    /* 167 */if (ids != null) {
      /* 168 */Element[] coordinate = new Element[ids.length];
      /*     */
      /* 170 */for (int i = 0; i < ids.length; ++i) {
        /* 171 */coordinate[i] =
        /* 172 */this.cube.getDimensionAt(i).getDefaultHierarchy().getElementById(
          ids[i]);
        /*     */}
      /* 174 */return coordinate;
      /*     */}
    /* 176 */return new Element[0];
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.CellImpl JD-Core Version: 0.5.4
 */