/*
 * (c) Tensegrity Software 2005. All rights reserved.
 */
package com.tensegrity.palojava.legacy;

/**
 * <code>DimElementInfo</code>
 * A simple data class which holds the name and the type of an element.
 * All valid element types are defined as constants within this class.
 *
 * @author Stepan Rutz
 * @author Axel Kiselev
 * @version $Id: DimElementInfo.java,v 1.1 2007/04/11 09:15:04 ArndHouben Exp $
 */
public class DimElementInfo {

  /** valid element types. */
  public static final int DIMELEMENTTYPENUMERIC = 0, DIMELEMENTTYPESTRING = 1,
    DIMELEMENTTYPECONSOLIDATED = 2, DIMELEMENTTYPERULE = 3;

  private static final String S_DIMELEMENTTYPENUMERIC = "1",
    S_DIMELEMENTTYPESTRING = "2", S_DIMELEMENTTYPECONSOLIDATED = "4";

  private final String name;

  private final int type;

  /**
   * Creates a new <code>DimElementInfo</code> instance.
   * @param name the element name
   * @param type the element type as defined in this class
   */
  public DimElementInfo(String name, int type) {
    this.name = name;
    this.type = type;
  }

  /**
   * Returns the element name
   * @return the element name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the element type, i.e. one of defined type constants 
   * @return the element type
   */
  public int getType() {
    return type;
  }

  public String toString() {
    return "DimElementInfo (name=\"" + name + "\", type=" + type + ")";
  }

  /**
   * Returns a <code>String</code> representation of the given element type
   * @param type one of the defined element type constants
   * @return a corresponding <code>String</code> representation
   */
  public static String dimElementType2String(int type) {
    switch (type) {
    default:
    case DIMELEMENTTYPENUMERIC:
      return "Numeric";
    case DIMELEMENTTYPESTRING:
      return "String";
    case DIMELEMENTTYPECONSOLIDATED:
      return "Consolidated";
    case DIMELEMENTTYPERULE:
      return "Rule";
    }
  }

  /**
   * Returns a type code for given Palo's <code>String</code> code 
   * representation
   * @param type one of the defined element type constants
   * @return a corresponding int representation
   */
  public static int stringCode2Type(String type) {
    if (type.equals(S_DIMELEMENTTYPENUMERIC))
      return DIMELEMENTTYPENUMERIC;
    else if (type.equals(S_DIMELEMENTTYPECONSOLIDATED))
      return DIMELEMENTTYPECONSOLIDATED;
    else if (type.equals(S_DIMELEMENTTYPESTRING))
      return DIMELEMENTTYPESTRING;
    else
      return DIMELEMENTTYPERULE;
  }

}
