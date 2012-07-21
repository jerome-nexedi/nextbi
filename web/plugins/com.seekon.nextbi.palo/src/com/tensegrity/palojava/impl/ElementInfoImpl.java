/*     */package com.tensegrity.palojava.impl;

/*     */
/*     */import com.tensegrity.palojava.DimensionInfo; /*     */
import com.tensegrity.palojava.ElementInfo; /*     */
import java.util.ArrayList;

/*     */
/*     */public class ElementInfoImpl
/*     */implements ElementInfo
/*     */{
  /*     */private final String id;

  /*     */private final DimensionInfo dimension;

  /*     */private int type;

  /*     */private String name;

  /*     */private int position;

  /*     */private int level;

  /*     */private int indent;

  /*     */private int depth;

  /*     */private String[] parentIds;

  /*     */private String[] childrenIds;

  /*     */private double[] weights;

  /*     */
  /*     */public ElementInfoImpl(DimensionInfo dimension, String id)
  /*     */{
    /* 56 */this.id = id;
    /* 57 */this.dimension = dimension;
    /* 58 */this.parentIds = new String[0];
    /*     */}

  /*     */
  /*     */public final synchronized String[] getChildren() {
    /* 62 */return this.childrenIds;
    /*     */}

  /*     */
  /*     */public final synchronized int getChildrenCount() {
    /* 66 */return this.childrenIds.length;
    /*     */}

  /*     */
  /*     */public final synchronized int getDepth() {
    /* 70 */return this.depth;
    /*     */}

  /*     */
  /*     */public final DimensionInfo getDimension() {
    /* 74 */return this.dimension;
    /*     */}

  /*     */
  /*     */public final synchronized int getIndent() {
    /* 78 */return this.indent;
    /*     */}

  /*     */
  /*     */public final synchronized int getLevel() {
    /* 82 */return this.level;
    /*     */}

  /*     */
  /*     */public final synchronized String getName() {
    /* 86 */return this.name;
    /*     */}

  /*     */
  /*     */public final synchronized int getParentCount() {
    /* 90 */return this.parentIds.length;
    /*     */}

  /*     */
  /*     */public final synchronized String[] getParents() {
    /* 94 */return this.parentIds;
    /*     */}

  /*     */
  /*     */public final synchronized int getPosition() {
    /* 98 */return this.position;
    /*     */}

  /*     */
  /*     */public final synchronized double[] getWeights() {
    /* 102 */return this.weights;
    /*     */}

  /*     */
  /*     */public final synchronized void setChildren(String[] children,
    double[] weights)
  /*     */{
    /* 107 */this.childrenIds = children;
    /* 108 */this.weights = weights;
    /*     */}

  /*     */
  /*     */public final synchronized void setDepth(int depth) {
    /* 112 */this.depth = depth;
    /*     */}

  /*     */
  /*     */public final synchronized void setIndent(int indent) {
    /* 116 */this.indent = indent;
    /*     */}

  /*     */
  /*     */public final synchronized void setLevel(int newLevel) {
    /* 120 */this.level = newLevel;
    /*     */}

  /*     */
  /*     */public final synchronized void setName(String name) {
    /* 124 */this.name = name;
    /*     */}

  /*     */
  /*     */public final synchronized void setParents(String[] parents) {
    /* 128 */this.parentIds = parents;
    /*     */}

  /*     */
  /*     */public final synchronized void setPosition(int newPosition)
  /*     */{
    /* 140 */this.position = newPosition;
    /*     */}

  /*     */
  /*     */public final synchronized void setType(int type) {
    /* 144 */this.type = type;
    /*     */}

  /*     */
  /*     */public final String getId() {
    /* 148 */return this.id;
    /*     */}

  /*     */
  /*     */public final synchronized int getType() {
    /* 152 */return this.type;
    /*     */}

  /*     */
  /*     */public boolean canBeModified() {
    /* 156 */return true;
    /*     */}

  /*     */
  /*     */public boolean canCreateChildren() {
    /* 160 */return true;
    /*     */}

  /*     */
  /*     */public void update(String[] children) {
    /* 164 */ArrayList cIds = new ArrayList();
    /* 165 */ArrayList<Double> wght = new ArrayList();

    /* 167 */if (children != null) {
      /* 168 */int counter = 0;
      /* 169 */int counter2 = 0;
      /* 170 */for (String s : this.childrenIds) {
        /* 171 */if (counter >= children.length) {
          /*     */break;
          /*     */}
        /* 174 */if (s.equals(children[counter])) {
          /* 175 */cIds.add(s);
          /* 176 */wght.add(Double.valueOf(this.weights[counter2]));
          /* 177 */++counter;
          /*     */}
        /* 179 */++counter2;
        /*     */}
      /*     */}
    /*     */
    /* 183 */int counter = 0;
    /* 184 */this.weights = new double[wght.size()];
    /* 185 */for (Double d : wght) {
      /* 186 */this.weights[(counter++)] = d.doubleValue();
      /*     */}
    /*     */
    /* 189 */this.childrenIds = ((String[]) cIds.toArray(new String[0]));
    /*     */}
  /*     */
}

/*
 * Location:
 * E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.paloapi\lib\palo.jar
 * Qualified Name: com.tensegrity.palojava.impl.ElementInfoImpl JD-Core Version:
 * 0.5.4
 */