/*
// $Id: //open/mondrian/src/main/mondrian/olap4j/Unsafe.java#2 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2011-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.olap4j;

import mondrian.olap.QueryTiming;
import mondrian.spi.ProfileHandler;
import org.olap4j.OlapStatement;

import java.io.PrintWriter;

/**
 * Access to non-public methods in the package of the mondrian olap4j driver.
 * 
 * <p>
 * All methods in this class are subject to change without notice.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/olap4j/Unsafe.java#2 $
 * @since October, 2010
 */
public final class Unsafe {
	public static final Unsafe INSTANCE = new Unsafe();

	private Unsafe() {
	}

	public void setStatementProfiling(OlapStatement statement,
			final PrintWriter pw) {
		((MondrianOlap4jStatement) statement).enableProfiling(new ProfileHandler() {
			public void explain(String plan, QueryTiming timing) {
				pw.println(plan);
				if (timing != null) {
					pw.println(timing);
				}
			}
		});
	}
}

// End Unsafe.java
