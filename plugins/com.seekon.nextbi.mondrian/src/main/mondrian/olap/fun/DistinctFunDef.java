/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/DistinctFunDef.java#6 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2007-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mondrian.calc.*;
import mondrian.calc.impl.AbstractListCalc;
import mondrian.mdx.ResolvedFunCall;
import mondrian.olap.Evaluator;
import mondrian.olap.Member;

/**
 * Definition of the <code>Distinct</code> MDX function.
 * 
 * <p>
 * Syntax: <blockquote><code>Distinct(&lt;Set&gt;)</code></blockquote>
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/olap/fun/DistinctFunDef.java#6 $
 * @since Jun 10, 2007
 */
class DistinctFunDef extends FunDefBase {
	public static final DistinctFunDef instance = new DistinctFunDef();

	private DistinctFunDef() {
		super("Distinct", "Eliminates duplicate tuples from a set.", "fxx");
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		final ListCalc listCalc = compiler.compileList(call.getArg(0));
		return new CalcImpl(call, listCalc);
	}

	static class CalcImpl extends AbstractListCalc {
		private final ListCalc listCalc;

		public CalcImpl(ResolvedFunCall call, ListCalc listCalc) {
			super(call, new Calc[] { listCalc });
			this.listCalc = listCalc;
		}

		public TupleList evaluateList(Evaluator evaluator) {
			TupleList list = listCalc.evaluateList(evaluator);
			Set<List<Member>> set = new HashSet<List<Member>>(list.size());
			TupleList result = list.cloneList(list.size());
			for (List<Member> element : list) {
				if (set.add(element)) {
					result.add(element);
				}
			}
			return result;
		}
	}
}

// End DistinctFunDef.java
