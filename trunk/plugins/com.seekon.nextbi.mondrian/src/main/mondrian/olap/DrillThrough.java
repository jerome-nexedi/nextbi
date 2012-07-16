/*
// $Id: //open/mondrian/src/main/mondrian/olap/DrillThrough.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2010-2010 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap;

import java.io.PrintWriter;
import java.util.List;

/**
 * Drill through statement.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/olap/DrillThrough.java#1 $
 */
public class DrillThrough extends QueryPart {
	private final Query query;
	private final int maxRowCount;
	private final int firstRowOrdinal;
	private final List<Exp> returnList;

	/**
	 * Creates a DrillThrough.
	 * 
	 * @param query
	 *          Query
	 * @param maxRowCount
	 *          Maximum number of rows to return, or -1
	 * @param firstRowOrdinal
	 *          Ordinal of first row to return, or -1
	 * @param returnList
	 *          List of columns to return
	 */
	DrillThrough(Query query, int maxRowCount, int firstRowOrdinal,
			List<Exp> returnList) {
		this.query = query;
		this.maxRowCount = maxRowCount;
		this.firstRowOrdinal = firstRowOrdinal;
		this.returnList = returnList;
	}

	@Override
	public void unparse(PrintWriter pw) {
		pw.print("DRILLTHROUGH");
		if (maxRowCount >= 0) {
			pw.print(" MAXROWS ");
			pw.print(maxRowCount);
		}
		if (firstRowOrdinal >= 0) {
			pw.print(" FIRSTROWSET ");
			pw.print(firstRowOrdinal);
		}
		pw.print(" ");
		query.unparse(pw);
		if (returnList != null) {
			ExpBase.unparseList(pw, returnList.toArray(new Exp[returnList.size()]),
					" RETURN ", ", ", "");
		}
	}

	@Override
	public Object[] getChildren() {
		return new Object[] { maxRowCount, firstRowOrdinal, query, returnList };
	}

	public Query getQuery() {
		return query;
	}

	public int getMaxRowCount() {
		return maxRowCount;
	}

	public int getFirstRowOrdinal() {
		return firstRowOrdinal;
	}
}

// End DrillThrough.java
