/*
 * (c) Tensegrity Software 2007
 * All rights reserved
 */
package com.tensegrity.palojava.legacy.builders;

import com.tensegrity.palojava.CellInfo;
import com.tensegrity.palojava.PaloException;
import com.tensegrity.palojava.impl.CellInfoImpl;

/**
 * <code>CellInfoBuilder</code>
 * <p>
 * A simple builder class which creates an implementation for the 
 * {@link CellInfo} interface
 * </p>
 * 
 * @author Arnd Houben
 * @version $Id: CellInfoBuilder.java,v 1.1 2007/04/11 16:45:38 ArndHouben Exp $
 */
public class CellInfoBuilder {

  //--------------------------------------------------------------------------
  // FACTORY
  //	
  private static final CellInfoBuilder instance = new CellInfoBuilder();

  /** 
   * Returns the sole instance of the <code>CellInfoBuilder</code>
   * @return <code>CellInfoBuilder</code> instance
   */
  public static final CellInfoBuilder getInstance() {
    instance.init();
    return instance;
  }

  //--------------------------------------------------------------------------
  // INSTANCE
  //	
  private int type;

  private Object value;

  private String[] coordinate;

  private CellInfoBuilder() {
  }

  /**
   * Sets the cell value
   * @param value cell value
   */
  public final void setValue(Object value) {
    this.value = value;
    type = getValueType();
  }

  /**
   * Sets the coordinate of the cell specified by given element names  
   * @param coordinate element names which build up the cell coordinate
   */
  public final void setCoordinate(String[] coordinate) {
    this.coordinate = coordinate;
  }

  /**
   * Creates a new {@link CellInfo} instance. If not all required information
   * is provided a {@link PaloException} is thrown.
   * @return {@link CellInfo} instance
   */
  public CellInfo create() {
    if (notEnoughInformation())
      throw new PaloException("Not enough information to create CellInfo!!");
    return new CellInfoImpl(type, true, value, coordinate);
  }

  //--------------------------------------------------------------------------
  // PRIVATE METHODS
  //
  private final void init() {
    type = -1;
    value = null;
    coordinate = null;
  }

  private final boolean notEnoughInformation() {
    return (type == -1) || (value == null);
  }

  private final int getValueType() {
    try {
      Double.parseDouble(value.toString());
      return CellInfo.TYPE_NUMERIC;
    } catch (Exception e) {

    }
    return CellInfo.TYPE_STRING;
  }
}
