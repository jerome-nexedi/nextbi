/*
// $Id: //open/mondrian/src/main/mondrian/calc/impl/AbstractIterCalc.java#9 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2006-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.calc.impl;

import mondrian.olap.*;
import mondrian.olap.type.SetType;
import mondrian.calc.*;

/**
 * Abstract implementation of the {@link mondrian.calc.IterCalc} interface.
 * 
 * <p>
 * The derived class must implement the
 * {@link #evaluateIterable(mondrian.olap.Evaluator)} method, and the
 * {@link #evaluate(mondrian.olap.Evaluator)} method will call it.
 * 
 * @see mondrian.calc.impl.AbstractListCalc
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/calc/impl/AbstractIterCalc.java#9
 *          $
 * @since Oct 24, 2008
 */
public abstract class AbstractIterCalc extends AbstractCalc implements IterCalc {
  /**
   * Creates an abstract implementation of a compiled expression which returns a
   * {@link TupleIterable}.
   * 
   * @param exp
   *          Expression which was compiled
   * @param calcs
   *          List of child compiled expressions (for dependency analysis)
   */
  protected AbstractIterCalc(Exp exp, Calc[] calcs) {
    super(exp, calcs);
  }

  public SetType getType() {
    return (SetType) super.getType();
  }

  public final Object evaluate(Evaluator evaluator) {
    return evaluateIterable(evaluator);
  }

  public ResultStyle getResultStyle() {
    return ResultStyle.ITERABLE;
  }

  public String toString() {
    return "AbstractIterCalc object";
  }
}

// End AbstractIterCalc.java
