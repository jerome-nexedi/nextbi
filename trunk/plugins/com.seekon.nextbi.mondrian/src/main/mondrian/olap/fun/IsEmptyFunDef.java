/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/IsEmptyFunDef.java#4 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2006-2009 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.olap.FunDef;
import mondrian.olap.Evaluator;
import mondrian.olap.Util;
import mondrian.calc.Calc;
import mondrian.calc.ExpCompiler;
import mondrian.calc.impl.AbstractBooleanCalc;
import mondrian.mdx.ResolvedFunCall;

/**
 * Definition of the <code>IsEmpty</code> MDX function.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/olap/fun/IsEmptyFunDef.java#4
 *          $
 * @since Mar 23, 2006
 */
class IsEmptyFunDef extends FunDefBase {
	static final ReflectiveMultiResolver FunctionResolver = new ReflectiveMultiResolver(
			"IsEmpty", "IsEmpty(<Value Expression>)",
			"Determines if an expression evaluates to the empty cell value.",
			new String[] { "fbS", "fbn" }, IsEmptyFunDef.class);

	static final ReflectiveMultiResolver PostfixResolver = new ReflectiveMultiResolver(
			"IS EMPTY", "<Value Expression> IS EMPTY",
			"Determines if an expression evaluates to the empty cell value.",
			new String[] { "Qbm", "Qbt" }, IsEmptyFunDef.class);

	public IsEmptyFunDef(FunDef dummyFunDef) {
		super(dummyFunDef);
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		final Calc calc = compiler.compileScalar(call.getArg(0), true);
		return new AbstractBooleanCalc(call, new Calc[] { calc }) {
			public boolean evaluateBoolean(Evaluator evaluator) {
				Object o = calc.evaluate(evaluator);
				return o == null;
			}
		};
	}
}

// End IsEmptyFunDef.java
