/*     */package com.tensegrity.palojava.impl;

/*     */
/*     */import com.tensegrity.palojava.CellInfo;

/*     */
/*     */public class CellInfoImpl
/*     */implements CellInfo
/*     */{
  /*     */private final int type;

  /*     */private final boolean exists;

  /*     */private final Object value;

  /*     */private String[] coordinate;

  /*     */private String rule;

  /*     */
  /*     */public CellInfoImpl(int type, boolean exists, Object value)
  /*     */{
    /* 56 */this.exists = exists;
    /* 57 */this.value = value;
    /* 58 */this.type = type;
    /*     */}

  /*     */
  /*     */public final boolean exists() {
    /* 62 */return this.exists;
    /*     */}

  /*     */
  /*     */public final int getType() {
    /* 66 */return this.type;
    /*     */}

  /*     */
  /*     */public final Object getValue() {
    /* 70 */return this.value;
    /*     */}

  /*     */
  /*     */public final String getId() {
    /* 74 */return null;
    /*     */}

  /*     */
  /*     */public final String[] getCoordinate() {
    /* 78 */return this.coordinate;
    /*     */}

  /*     */
  /*     */public final void setCoordinate(String[] coord) {
    /* 82 */this.coordinate = ((coord != null) ? (String[]) coord.clone() : null);
    /*     */}

  /*     */
  /*     */public final String toString() {
    /* 86 */return this.value.toString();
    /*     */}

  /*     */
  /*     */public final boolean canBeModified() {
    /* 90 */return true;
    /*     */}

  /*     */
  /*     */public final boolean canCreateChildren() {
    /* 94 */return false;
    /*     */}

  /*     */
  /*     */public final String getRule() {
    /* 98 */return this.rule;
    /*     */}

  /*     */
  /*     */public final void setRule(String id) {
    /* 102 */if (id.equals(""))
      /* 103 */id = null;
    /* 104 */this.rule = id;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.impl.CellInfoImpl JD-Core Version:
 * 0.5.4
 */