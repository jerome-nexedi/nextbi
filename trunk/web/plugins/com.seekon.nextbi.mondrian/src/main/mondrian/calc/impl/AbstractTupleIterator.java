/*
// $Id: //open/mondrian/src/main/mondrian/calc/impl/AbstractTupleIterator.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2011-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.calc.impl;

import mondrian.calc.TupleIterator;
import mondrian.olap.Member;

import java.util.List;

/**
 * Abstract implementation of {@link TupleIterator}.
 * 
 * <p>
 * Derived classes need to implement only {@link #forward()}. {@code forward}
 * must set the {@link #current} field, and derived classes can use it.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/calc/impl/AbstractTupleIterator
 *          .java#1 $
 */
public abstract class AbstractTupleIterator extends AbstractTupleCursor implements
  TupleIterator {
  protected boolean hasNext;

  public AbstractTupleIterator(int arity) {
    super(arity);
  }

  public boolean hasNext() {
    return hasNext;
  }

  public List<Member> next() {
    List<Member> o = current();
    hasNext = forward();
    return o;
  }

  public void remove() {
    throw new UnsupportedOperationException("remove");
  }

}

// End AbstractTupleIterator.java
