/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/MinMaxFunDef.java#10 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2006-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.calc.*;
import mondrian.olap.*;
import mondrian.calc.impl.ValueCalc;
import mondrian.calc.impl.AbstractDoubleCalc;
import mondrian.mdx.ResolvedFunCall;

/**
 * Definition of the <code>Min</code> and <code>Max</code> MDX functions.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/olap/fun/MinMaxFunDef.java#10
 *          $
 * @since Mar 23, 2006
 */
class MinMaxFunDef extends AbstractAggregateFunDef {
	static final ReflectiveMultiResolver MinResolver = new ReflectiveMultiResolver(
			"Min",
			"Min(<Set>[, <Numeric Expression>])",
			"Returns the minimum value of a numeric expression evaluated over a set.",
			new String[] { "fnx", "fnxn" }, MinMaxFunDef.class);

	static final MultiResolver MaxResolver = new ReflectiveMultiResolver(
			"Max",
			"Max(<Set>[, <Numeric Expression>])",
			"Returns the maximum value of a numeric expression evaluated over a set.",
			new String[] { "fnx", "fnxn" }, MinMaxFunDef.class);

	private final boolean max;

	public MinMaxFunDef(FunDef dummyFunDef) {
		super(dummyFunDef);
		this.max = dummyFunDef.getName().equals("Max");
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		final ListCalc listCalc = compiler.compileList(call.getArg(0));
		final Calc calc = call.getArgCount() > 1 ? compiler.compileScalar(
				call.getArg(1), true) : new ValueCalc(call);
		return new AbstractDoubleCalc(call, new Calc[] { listCalc, calc }) {
			public double evaluateDouble(Evaluator evaluator) {
				TupleList memberList = evaluateCurrentList(listCalc, evaluator);
				final int savepoint = evaluator.savepoint();
				evaluator.setNonEmpty(false);
				final Double d = (Double) (max ? max(evaluator, memberList, calc)
						: min(evaluator, memberList, calc));
				evaluator.restore(savepoint);
				return d;
			}

			public boolean dependsOn(Hierarchy hierarchy) {
				return anyDependsButFirst(getCalcs(), hierarchy);
			}
		};
	}
}

// End MinMaxFunDef.java
