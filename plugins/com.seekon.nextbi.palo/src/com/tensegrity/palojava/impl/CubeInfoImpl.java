/*     */package com.tensegrity.palojava.impl;

/*     */
/*     */import com.tensegrity.palojava.CubeInfo; /*     */
import com.tensegrity.palojava.DatabaseInfo; /*     */
import java.math.BigInteger;

/*     */
/*     */public class CubeInfoImpl
/*     */implements CubeInfo
/*     */{
  /*     */private final String id;

  /*     */private int type;

  /*     */private final DatabaseInfo database;

  /*     */private final String[] dimensions;

  /*     */private String name;

  /*     */private int dimCount;

  /*     */private BigInteger cellCount;

  /*     */private BigInteger filledCellCount;

  /*     */private int status;

  /*     */private int token;

  /*     */
  /*     */public CubeInfoImpl(DatabaseInfo database, String id, int type,
    String[] dimensions)
  /*     */{
    /* 59 */this.id = id;
    /* 60 */this.type = type;
    /* 61 */this.database = database;
    /* 62 */this.dimensions = dimensions;
    /*     */}

  /*     */
  /*     */public void setType(int newType) {
    /* 66 */this.type = newType;
    /*     */}

  /*     */
  /*     */public final String getId() {
    /* 70 */return this.id;
    /*     */}

  /*     */
  /*     */public final String getName() {
    /* 74 */return this.name;
    /*     */}

  /*     */
  /*     */public final int getType() {
    /* 78 */return this.type;
    /*     */}

  /*     */
  /*     */public final DatabaseInfo getDatabase() {
    /* 82 */return this.database;
    /*     */}

  /*     */
  /*     */public final String[] getDimensions() {
    /* 86 */return this.dimensions;
    /*     */}

  /*     */
  /*     */public final synchronized void setCellCount(BigInteger cellCount) {
    /* 90 */this.cellCount = cellCount;
    /*     */}

  /*     */
  /*     */public final synchronized void setDimensionCount(int dimCount) {
    /* 94 */this.dimCount = dimCount;
    /*     */}

  /*     */
  /*     */public final synchronized void setFilledCellCount(
    BigInteger filledCellCount) {
    /* 98 */this.filledCellCount = filledCellCount;
    /*     */}

  /*     */
  /*     */public final synchronized void setName(String name) {
    /* 102 */this.name = name;
    /*     */}

  /*     */
  /*     */public final synchronized void setStatus(int status) {
    /* 106 */this.status = status;
    /*     */}

  /*     */
  /*     */public final synchronized void setToken(int token) {
    /* 110 */this.token = token;
    /*     */}

  /*     */
  /*     */public final synchronized BigInteger getCellCount() {
    /* 114 */return this.cellCount;
    /*     */}

  /*     */
  /*     */public final synchronized int getDimensionCount() {
    /* 118 */return this.dimCount;
    /*     */}

  /*     */
  /*     */public final synchronized BigInteger getFilledCellCount() {
    /* 122 */return this.filledCellCount;
    /*     */}

  /*     */
  /*     */public final synchronized int getStatus() {
    /* 126 */return this.status;
    /*     */}

  /*     */
  /*     */public final synchronized int getToken() {
    /* 130 */return this.token;
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 134 */return true;
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 138 */return true;
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.impl.CubeInfoImpl JD-Core Version:
 * 0.5.4
 */