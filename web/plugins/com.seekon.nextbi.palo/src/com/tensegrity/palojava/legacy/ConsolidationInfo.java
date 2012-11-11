/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy;

/**
 * <code>ConsolidationInfo</code> 
 * <p>A simple data class which holds informations about a consolidated 
 * child element.
 * </p>
 * 
 * @author Stepan Rutz
 * @version $Id: ConsolidationInfo.java,v 1.2 2007/04/11 16:45:38 ArndHouben Exp $
 */
final class ConsolidationInfo {

  /** the child name */
  private final String name;

  //	/** the consolidation type */
  //	private final int type;

  /** the weight factor */
  private final double factor;

  /**
   * Creates a new <code>ConsolidationInfo</code>instance.
   * 
   * @param name the element (child) name
   * @param type is not used anymore (subject to be removed...)
   * @param factor the weight factor
   */
  ConsolidationInfo(String name, int type, double factor) {
    this.name = name;
    //		this.type = type;
    this.factor = factor;
  }

  /**
   * Returns the element name
   * @return the element name
   */
  public String getName() {
    return name;
  }

  //	/**
  //	 * Returns the consolidation type
  //	 * @return the consolidation type
  //	 */
  //	public int getType() {
  //		return type;
  //	}

  /**
   * Returns the weight factor
   * @return the weight factor
   */
  public double getFactor() {
    return factor;
  }

  public String toString() {
    return "ConsolidationInfo (name=\"" + name + "\", factor=" + factor + ")";
  }

}
