/*
// $Id: //open/mondrian/src/main/mondrian/calc/impl/AbstractBooleanCalc.java#7 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2006-2009 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.calc.impl;

import mondrian.olap.Evaluator;
import mondrian.olap.Exp;
import mondrian.olap.type.BooleanType;
import mondrian.calc.*;

/**
 * Abstract implementation of the {@link mondrian.calc.BooleanCalc} interface.
 * 
 * <p>
 * The derived class must implement the
 * {@link #evaluateBoolean(mondrian.olap.Evaluator)} method, and the
 * {@link #evaluate(mondrian.olap.Evaluator)} method will call it.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/calc/impl/AbstractBooleanCalc.
 *          java#7 $
 * @since Sep 26, 2005
 */
public abstract class AbstractBooleanCalc extends AbstractCalc implements
		BooleanCalc {
	/**
	 * Creates an AbstractBooleanCalc.
	 * 
	 * @param exp
	 *          Source expression
	 * @param calcs
	 *          Child compiled expressions
	 */
	public AbstractBooleanCalc(Exp exp, Calc[] calcs) {
		super(exp, calcs);
		// now supports int and double conversion (see
		// AbstractExpCompiler.compileBoolean():
		// assert getType() instanceof BooleanType;
	}

	public Object evaluate(Evaluator evaluator) {
		return Boolean.valueOf(evaluateBoolean(evaluator));
	}
}

// End AbstractBooleanCalc.java
