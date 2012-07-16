/*
// $Id: //open/mondrian/src/main/mondrian/olap/fun/CorrelationFunDef.java#10 $
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
 * Definition of the <code>Correlation</code> MDX function.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/olap/fun/CorrelationFunDef.java#10
 *          $
 * @since Mar 23, 2006
 */
class CorrelationFunDef extends AbstractAggregateFunDef {
  static final ReflectiveMultiResolver Resolver = new ReflectiveMultiResolver(
    "Correlation",
    "Correlation(<Set>, <Numeric Expression>[, <Numeric Expression>])",
    "Returns the correlation of two series evaluated over a set.", new String[] {
      "fnxn", "fnxnn" }, CorrelationFunDef.class);

  public CorrelationFunDef(FunDef dummyFunDef) {
    super(dummyFunDef);
  }

  public Calc compileCall(ResolvedFunCall call, ExpCompiler compiler) {
    final ListCalc listCalc = compiler.compileList(call.getArg(0));
    final Calc calc1 = compiler.compileScalar(call.getArg(1), true);
    final Calc calc2 = call.getArgCount() > 2 ? compiler.compileScalar(call
      .getArg(2), true) : new ValueCalc(call);
    return new AbstractDoubleCalc(call, new Calc[] { listCalc, calc1, calc2 }) {
      public double evaluateDouble(Evaluator evaluator) {
        final int savepoint = evaluator.savepoint();
        evaluator.setNonEmpty(false);
        TupleList list = evaluateCurrentList(listCalc, evaluator);
        final double correlation = correlation(evaluator, list, calc1, calc2);
        evaluator.restore(savepoint);
        return correlation;
      }

      public boolean dependsOn(Hierarchy hierarchy) {
        return anyDependsButFirst(getCalcs(), hierarchy);
      }
    };
  }
}

// End CorrelationFunDef.java
