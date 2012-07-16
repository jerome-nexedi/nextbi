/*     */package org.palo.api.impl;

/*     */
/*     */import com.tensegrity.palojava.ExportContextInfo; /*     */
import org.palo.api.Condition; /*     */
import org.palo.api.Cube; /*     */
import org.palo.api.Dimension; /*     */
import org.palo.api.Element; /*     */
import org.palo.api.ExportContext; /*     */
import org.palo.api.Hierarchy;

/*     */
/*     */class ExportContextImpl
/*     */implements ExportContext
/*     */{
  /*     */private final Cube cube;

  /*     */private final ExportContextInfo contextInfo;

  /*     */
  /*     */ExportContextImpl(Cube cube)
  /*     */{
    /* 62 */this(cube, null);
    /*     */}

  /*     */
  /*     */ExportContextImpl(Cube cube, Element[][] area) {
    /* 66 */this.cube = cube;
    /* 67 */this.contextInfo = new ExportContextInfo();
    /* 68 */init(cube, area);
    /*     */}

  /*     */
  /*     */ExportContextInfo getInfo() {
    /* 72 */return this.contextInfo;
    /*     */}

  /*     */
  /*     */public void reset() {
    /* 76 */init(this.cube, null);
    /*     */}

  /*     */
  /*     */public Condition createCondition(String condition, double value) {
    /* 80 */Condition cond = ConditionImpl.getCondition(condition);
    /* 81 */cond.setValue(value);
    /* 82 */return cond;
    /*     */}

  /*     */
  /*     */public Condition createCondition(String condition, String value) {
    /* 86 */Condition cond = ConditionImpl.getCondition(condition);
    /* 87 */cond.setValue(value);
    /* 88 */return cond;
    /*     */}

  /*     */
  /*     */public String getConditionRepresentation() {
    /* 92 */return this.contextInfo.getConditionRepresentation();
    /*     */}

  /*     */
  /*     */public void setCombinedCondition(Condition firstCondition,
    Condition secondCondition, String operator)
  /*     */{
    /* 97 */if (isValid(operator)) {
      /* 98 */StringBuffer condition = new StringBuffer();
      /* 99 */condition.append(firstCondition.toString());
      /* 100 */condition.append(operator);
      /* 101 */condition.append(secondCondition.toString());
      /* 102 */this.contextInfo.setConditionRepresentation(condition.toString());
      /*     */}
    /*     */}

  /*     */
  /*     */public void setCondition(Condition condition) {
    /* 107 */this.contextInfo.setConditionRepresentation(condition.toString());
    /*     */}

  /*     */
  /*     */public boolean isBaseCellsOnly()
  /*     */{
    /* 112 */return this.contextInfo.isBaseCellsOnly();
    /*     */}

  /*     */
  /*     */public void setBaseCellsOnly(boolean baseCellsOnly) {
    /* 116 */this.contextInfo.setBaseCellsOnly(baseCellsOnly);
    /*     */}

  /*     */
  /*     */public boolean ignoreEmptyCells() {
    /* 120 */return this.contextInfo.ignoreEmptyCells();
    /*     */}

  /*     */
  /*     */public void setIgnoreEmptyCells(boolean ignoreEmptyCells) {
    /* 124 */this.contextInfo.setIgnoreEmptyCells(ignoreEmptyCells);
    /*     */}

  /*     */
  /*     */public final boolean isUseRules() {
    /* 128 */return this.contextInfo.useRules();
    /*     */}

  /*     */public final void setUseRules(boolean useRules) {
    /* 131 */this.contextInfo.setUseRules(useRules);
    /*     */}

  /*     */
  /*     */public int getBlocksize() {
    /* 135 */return this.contextInfo.getBlocksize();
    /*     */}

  /*     */
  /*     */public void setBlocksize(int blocksize) {
    /* 139 */this.contextInfo.setBlocksize(blocksize);
    /*     */}

  /*     */
  /*     */public int getType() {
    /* 143 */return this.contextInfo.getType();
    /*     */}

  /*     */
  /*     */public void setType(int type) {
    /* 147 */this.contextInfo.setType(type);
    /*     */}

  /*     */
  /*     */public Element[][] getCellsArea() {
    /* 151 */String[][] area = this.contextInfo.getCellsArea();
    /* 152 */Element[][] cells = new Element[area.length][];
    /* 153 */for (int i = 0; i < area.length; ++i) {
      /* 154 */cells[i] = new Element[area[i].length];
      /* 155 */for (int j = 0; j < area[i].length; ++j) {
        /* 156 */Dimension dim = this.cube.getDimensionAt(j);
        /* 157 */cells[i][j] = dim.getDefaultHierarchy().getElementByName(
          area[i][j]);
        /*     */}
      /*     */}
    /* 160 */return cells;
    /*     */}

  /*     */
  /*     */public void setCellsArea(Element[][] area) {
    /* 164 */if (area == null)
      /* 165 */setAreaToDefault();
    /*     */else
      /* 167 */setArea(area);
    /*     */}

  /*     */
  /*     */public double getProgress() {
    /* 171 */return this.contextInfo.getProgress();
    /*     */}

  /*     */public void setProgress(double progress) {
    /* 174 */this.contextInfo.setProgress(progress);
    /*     */}

  /*     */
  /*     */public Element[] getExportAfter() {
    /* 178 */String[] ids = this.contextInfo.getExportAfter();
    /* 179 */if (ids == null)
      /* 180 */return null;
    /* 181 */Element[] path = new Element[ids.length];
    /* 182 */for (int i = 0; i < ids.length; ++i) {
      /* 183 */Dimension dim = this.cube.getDimensionAt(i);
      /* 184 */path[i] = dim.getDefaultHierarchy().getElementById(ids[i]);
      /*     */}
    /* 186 */return path;
    /*     */}

  /*     */
  /*     */public void setExportAfter(Element[] path)
  /*     */{
    /* 193 */if (path == null) {
      /* 194 */this.contextInfo.setExportAfter(null);
      /* 195 */return;
      /*     */}
    /* 197 */String[] ids = new String[path.length];
    /* 198 */for (int i = 0; i < path.length; ++i)
      /* 199 */ids[i] = path[i].getId();
    /* 200 */this.contextInfo.setExportAfter(ids);
    /*     */}

  /*     */
  /*     */private final boolean isValid(String operator)
  /*     */{
    /* 210 */return (operator.equals("or")) ||
    /* 209 */(operator.equals("xor")) ||
    /* 210 */(operator.equals("and"));
    /*     */}

  /*     */
  /*     */private final void init(Cube cube, Element[][] area)
  /*     */{
    /* 215 */this.contextInfo.setProgress(0.0D);
    /* 216 */this.contextInfo.setConditionRepresentation(null);
    /* 217 */this.contextInfo.setBlocksize(1000);
    /* 218 */this.contextInfo.setType(0);
    /* 219 */this.contextInfo.setBaseCellsOnly(true);
    /* 220 */this.contextInfo.setIgnoreEmptyCells(true);
    /* 221 */this.contextInfo.setExportAfter(null);
    /* 222 */if (area == null)
    /*     */{
      /* 224 */setAreaToDefault();
      /*     */}
    /* 226 */else
      setArea(area);
    /*     */}

  /*     */
  /*     */private final void setAreaToDefault()
  /*     */{
    /* 232 */Dimension[] dims = this.cube.getDimensions();
    /* 233 */Element[][] area = new Element[dims.length][];
    /* 234 */for (int i = 0; i < area.length; ++i) {
      /* 235 */Dimension dim = this.cube.getDimensionAt(i);
      /* 236 */area[i] = dim.getDefaultHierarchy().getElements();
      /*     */}
    /* 238 */setArea(area);
    /*     */}

  /*     */
  /*     */private final void setArea(Element[][] area) {
    /* 242 */String[][] ids = new String[area.length][];
    /* 243 */for (int i = 0; i < area.length; ++i) {
      /* 244 */ids[i] = new String[area[i].length];
      /* 245 */for (int j = 0; j < area[i].length; ++j) {
        /* 246 */ids[i][j] = area[i][j].getId();
        /*     */}
      /*     */}
    /* 249 */this.contextInfo.setCellsArea(ids);
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name: org.palo.api.impl.ExportContextImpl JD-Core Version: 0.5.4
 */