/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/UnorderFunDef.java#3 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2008-2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap.fun;

import mondrian.calc.Calc;
import mondrian.calc.ExpCompiler;
import mondrian.mdx.ResolvedFunCall;
import mondrian.olap.FunDef;

/**
 * Definition of the <code>Unorder</code> MDX function.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/olap/fun/UnorderFunDef.java#3
 *          $
 * @since Sep 06, 2008
 */
class UnorderFunDef extends FunDefBase {

  static final ReflectiveMultiResolver Resolver = new ReflectiveMultiResolver(
    "Unorder", "Unorder(<Set>)",
    "Removes any enforced ordering from a specified set.", new String[] { "fxx" },
    UnorderFunDef.class);

  public UnorderFunDef(FunDef dummyFunDef) {
    super(dummyFunDef);
  }

  public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
    // Currently Unorder has no effect. In future, we may use the function
    // as a marker to weaken the ordering required from an expression and
    // therefore allow the compiler to use a more efficient implementation
    // that does not return a strict order.
    return compiler.compile(call.getArg(0));
  }
}

// End UnorderFunDef.java
