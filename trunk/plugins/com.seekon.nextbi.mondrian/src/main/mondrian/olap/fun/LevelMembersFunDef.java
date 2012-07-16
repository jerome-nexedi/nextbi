/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/LevelMembersFunDef.java#3 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2009-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.calc.*;
import mondrian.calc.impl.AbstractListCalc;
import mondrian.mdx.ResolvedFunCall;
import mondrian.olap.Evaluator;
import mondrian.olap.Level;

/**
 * Definition of the <code>&lt;Level&gt;.Members</code> MDX function.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/olap/fun/LevelMembersFunDef.java#3
 *          $
 * @since Jan 17, 2009
 */
public class LevelMembersFunDef extends FunDefBase {
	public static final LevelMembersFunDef INSTANCE = new LevelMembersFunDef();

	private LevelMembersFunDef() {
		super("Members", "Returns the set of members in a level.", "pxl");
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		final LevelCalc levelCalc = compiler.compileLevel(call.getArg(0));
		return new AbstractListCalc(call, new Calc[] { levelCalc }) {
			public TupleList evaluateList(Evaluator evaluator) {
				Level level = levelCalc.evaluateLevel(evaluator);
				return levelMembers(level, evaluator, false);
			}
		};
	}
}

// End LevelMembersFunDef.java
