/*
// $Id: //open/mondrian/src/main/mondrian/calc/TupleIterator.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2011-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.calc;

import mondrian.olap.Member;

import java.util.Iterator;
import java.util.List;

/**
 * Extension to {@link java.util.Iterator} that returns tuples.
 * 
 * <p>
 * Extends {@link TupleCursor} to support the standard Java iterator API. For
 * some implementations, using the iterator API (in particular the {@link #next}
 * and {@link #hasNext} methods) may be more expensive than using cursor's
 * {@link #forward} method.
 * 
 * @version $Id: //open/mondrian/src/main/mondrian/calc/TupleIterator.java#1 $
 * @author jhyde
 */
public interface TupleIterator extends Iterator<List<Member>>, TupleCursor {

}

// End TupleIterator.java
