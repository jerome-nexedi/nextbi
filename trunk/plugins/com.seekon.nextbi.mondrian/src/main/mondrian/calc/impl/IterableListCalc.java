/*
// $Id: //open/mondrian/src/main/mondrian/calc/impl/IterableListCalc.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2008-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.calc.impl;

import mondrian.calc.*;
import mondrian.olap.*;

/**
 * Adapter that converts a {@link mondrian.calc.IterCalc} to a
 * {@link mondrian.calc.ListCalc}.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/calc/impl/IterableListCalc.java#1
 *          $
 * @since Oct 23, 2008
 */
public class IterableListCalc extends AbstractListCalc {
	private final IterCalc iterCalc;

	/**
	 * Creates an IterableListCalc.
	 * 
	 * @param iterCalc
	 *          Calculation that returns an iterable.
	 */
	public IterableListCalc(IterCalc iterCalc) {
		super(new DummyExp(iterCalc.getType()), new Calc[] { iterCalc });
		this.iterCalc = iterCalc;
	}

	public TupleList evaluateList(Evaluator evaluator) {
		// A TupleIterCalc is allowed to return a list. If so, save the copy.
		final TupleIterable iterable = iterCalc.evaluateIterable(evaluator);
		if (iterable instanceof TupleList) {
			return (TupleList) iterable;
		}

		final TupleList list = TupleCollections.createList(iterable.getArity());
		final TupleCursor tupleCursor = iterable.tupleCursor();
		while (tupleCursor.forward()) {
			// REVIEW: Worth creating TupleList.addAll(TupleCursor)?
			list.addCurrent(tupleCursor);
		}
		return list;
	}
}

// End IterableListCalc.java
