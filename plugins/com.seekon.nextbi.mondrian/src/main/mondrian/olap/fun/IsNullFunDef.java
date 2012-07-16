/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/IsNullFunDef.java#4 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2006-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.calc.Calc;
import mondrian.calc.ExpCompiler;
import mondrian.calc.MemberCalc;
import mondrian.calc.impl.AbstractBooleanCalc;
import mondrian.mdx.ResolvedFunCall;
import mondrian.olap.Evaluator;
import mondrian.olap.FunDef;
import mondrian.olap.Member;
import mondrian.olap.Literal;

/**
 * Definition of the <code>IS NULL</code> MDX function.
 * 
 * @author medstat
 * @version $Id: //open/mondrian/src/main/mondrian/olap/fun/IsNullFunDef.java#4
 *          $
 * @since Aug 21, 2006
 */
class IsNullFunDef extends FunDefBase {
	/**
	 * Resolves calls to the <code>IS NULL</code> postfix operator.
	 */
	static final ReflectiveMultiResolver Resolver = new ReflectiveMultiResolver(
			"IS NULL", "<Expression> IS NULL", "Returns whether an object is null",
			new String[] { "Qbm", "Qbl", "Qbh", "Qbd" }, IsNullFunDef.class);

	public IsNullFunDef(FunDef dummyFunDef) {
		super(dummyFunDef);
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		assert call.getArgCount() == 1;
		final MemberCalc memberCalc = compiler.compileMember(call.getArg(0));
		return new AbstractBooleanCalc(call, new Calc[] { memberCalc }) {
			public boolean evaluateBoolean(Evaluator evaluator) {
				Member member = memberCalc.evaluateMember(evaluator);
				return member.isNull();
			}
		};
	}
}

// End IsNullFunDef.java
