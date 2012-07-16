/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/DimensionDimensionFunDef.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2009-2009 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.calc.Calc;
import mondrian.calc.ExpCompiler;
import mondrian.calc.impl.ConstantCalc;
import mondrian.mdx.ResolvedFunCall;
import mondrian.mdx.DimensionExpr;
import mondrian.olap.Dimension;
import mondrian.olap.type.DimensionType;

/**
 * Definition of the <code>&lt;Dimension&gt;.Dimension</code> MDX builtin
 * function.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/olap/fun/DimensionDimensionFunDef
 *          .java#1 $
 * @since Jul 20, 2009
 */
class DimensionDimensionFunDef extends FunDefBase {
	public static final FunDefBase INSTANCE = new DimensionDimensionFunDef();

	private DimensionDimensionFunDef() {
		super("Dimension",
				"Returns the dimension that contains a specified hierarchy.", "pdd");
	}

	public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
		Dimension dimension = ((DimensionExpr) call.getArg(0)).getDimension();
		return new ConstantCalc(DimensionType.forDimension(dimension), dimension);
	}
}

// End DimensionDimensionFunDef.java
