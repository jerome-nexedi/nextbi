/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/MemberDimensionFunDef.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2009-2009 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.calc.*;
import mondrian.calc.impl.AbstractDimensionCalc;
import mondrian.mdx.ResolvedFunCall;
import mondrian.olap.*;

/**
 * Definition of the <code>&lt;Measure&gt;.Dimension</code> MDX builtin
 * function.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/olap/fun/MemberDimensionFunDef
 *          .java#1 $
 * @since Jul 20, 2009
 */
class MemberDimensionFunDef extends FunDefBase {
	public static final FunDefBase INSTANCE = new MemberDimensionFunDef();

	private MemberDimensionFunDef() {
		super("Dimension",
				"Returns the dimension that contains a specified member.", "pdm");
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		final MemberCalc memberCalc = compiler.compileMember(call.getArg(0));
		return new AbstractDimensionCalc(call, new Calc[] { memberCalc }) {
			public Dimension evaluateDimension(Evaluator evaluator) {
				Member member = memberCalc.evaluateMember(evaluator);
				return member.getDimension();
			}
		};
	}
}

// End MemberDimensionFunDef.java
