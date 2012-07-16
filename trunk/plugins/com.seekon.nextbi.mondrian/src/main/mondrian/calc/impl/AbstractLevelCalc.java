/*
// $Id: //open/mondrian/src/main/mondrian/calc/impl/AbstractLevelCalc.java#4 $
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
import mondrian.olap.type.LevelType;
import mondrian.calc.LevelCalc;
import mondrian.calc.Calc;

/**
 * Abstract implementation of the {@link mondrian.calc.LevelCalc} interface.
 * 
 * <p>
 * The derived class must implement the
 * {@link #evaluateLevel(mondrian.olap.Evaluator)} method, and the
 * {@link #evaluate(mondrian.olap.Evaluator)} method will call it.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/calc/impl/AbstractLevelCalc.java#4
 *          $
 * @since Sep 26, 2005
 */
public abstract class AbstractLevelCalc extends AbstractCalc implements
		LevelCalc {
	/**
	 * Creates an AbstractLevelCalc.
	 * 
	 * @param exp
	 *          Source expression
	 * @param calcs
	 *          Child compiled expressions
	 */
	protected AbstractLevelCalc(Exp exp, Calc[] calcs) {
		super(exp, calcs);
		assert getType() instanceof LevelType;
	}

	public Object evaluate(Evaluator evaluator) {
		return evaluateLevel(evaluator);
	}
}

// End AbstractLevelCalc.java
