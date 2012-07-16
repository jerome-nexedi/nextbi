/*
// $Id: //open/mondrian/src/main/mondrian/olap/Explain.java#1 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2011-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap;

import java.io.PrintWriter;

/**
 * Explain statement.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/olap/Explain.java#1 $
 */
public class Explain extends QueryPart {
	private final QueryPart query;

	/**
	 * Creates an Explain statement.
	 * 
	 * @param query
	 *          Query (SELECT or DRILLTHROUGH)
	 */
	Explain(QueryPart query) {
		this.query = query;
		assert this.query != null;
		assert this.query instanceof Query || this.query instanceof DrillThrough;
	}

	@Override
	public void unparse(PrintWriter pw) {
		pw.print("EXPLAIN PLAN FOR ");
		query.unparse(pw);
	}

	@Override
	public Object[] getChildren() {
		return new Object[] { query };
	}

	public QueryPart getQuery() {
		return query;
	}
}

// End Explain.java
