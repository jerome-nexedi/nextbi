/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy.builders;

import com.tensegrity.palojava.DimensionInfo;
import com.tensegrity.palojava.ElementInfo;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.impl.ElementInfoImpl;

/**
 * <code></code>
 * TODO DOCUMENT ME
 * 
 * @author Arnd Houben
 * @version $Id: ElementInfoBuilder.java,v 1.2 2007/04/13 13:56:25 ArndHouben Exp $
 */
public class ElementInfoBuilder {

  //--------------------------------------------------------------------------
  // FACTORY
  //	
  private static final ElementInfoBuilder instance = new ElementInfoBuilder();

  public static final ElementInfoBuilder getInstance() {
    instance.init();
    return instance;
  }

  //--------------------------------------------------------------------------
  // INSTANCE
  //	
  private String name;

  private int type;

  private DimensionInfo dimension;

  private ElementInfo[] children;

  private double[] weights;

  private int position;

  private int level;

  private int indent;

  private int depth;

  private ElementInfoBuilder() {
  }

  public final void setName(String name) {
    this.name = name;
  }

  public final void setType(int type) {
    this.type = type;
  }

  public final void setDimension(DimensionInfo dimension) {
    this.dimension = dimension;
  }

  public final void setChildren(ElementInfo[] children) {
    this.children = children;
  }

  public final void setWeights(double[] weights) {
    this.weights = weights;
  }

  public final void setPosition(int position) {
    this.position = position;
  }

  public final void setLevel(int level) {
    this.level = level;
  }

  public final void setIndent(int indent) {
    this.indent = indent;
  }

  public final void setDepth(int depth) {
    this.depth = depth;
  }

  public final ElementInfo create() {
    if (notEnoughInformation())
      throw new PaloException("Not enough information to create ElementInfo!!");
    ElementInfoImpl elInfo = new ElementInfoImpl(dimension, name);
    elInfo.setName(name);
    elInfo.setType(type);

    String[] childrenIds = new String[children.length];

    for (int i = 0; i < childrenIds.length; ++i)
      childrenIds[i] = children[i].getId();

    elInfo.setChildren(childrenIds, weights);

    elInfo.setDepth(depth);
    elInfo.setIndent(indent);
    elInfo.setLevel(level);
    elInfo.setPosition(position);

    return elInfo;
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  private final void init() {
    name = null;
    type = -1;
    dimension = null;
    children = null;
    weights = null;
    position = -1;
    level = -1;
    indent = -1;
    depth = -1;
  }

  private final boolean notEnoughInformation() {
    return (name == null) || (type == -1) || (dimension == null)
      || (children == null) || (weights == null);
  }

}
