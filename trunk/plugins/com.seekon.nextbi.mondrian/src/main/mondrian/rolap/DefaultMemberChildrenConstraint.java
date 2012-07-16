/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2004-2005 TONBELLER AG
// Copyright (C) 2006-2010 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.rolap;

import java.util.List;

import mondrian.rolap.sql.MemberChildrenConstraint;
import mondrian.rolap.sql.SqlQuery;
import mondrian.rolap.aggmatcher.AggStar;

/**
 * Restricts the SQL result set to the parent member of a MemberChildren query.
 * If called with a calculated member an exception will be thrown.
 * 
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/rolap/DefaultMemberChildrenConstraint
 *          .java#16 $
 */
public class DefaultMemberChildrenConstraint implements
		MemberChildrenConstraint {
	private static final MemberChildrenConstraint instance = new DefaultMemberChildrenConstraint();

	protected DefaultMemberChildrenConstraint() {
	}

	public void addMemberConstraint(SqlQuery sqlQuery, RolapCube baseCube,
			AggStar aggStar, RolapMember parent) {
		SqlConstraintUtils.addMemberConstraint(sqlQuery, baseCube, aggStar, parent,
				true);
	}

	public void addMemberConstraint(SqlQuery sqlQuery, RolapCube baseCube,
			AggStar aggStar, List<RolapMember> parents) {
		boolean exclude = false;
		SqlConstraintUtils.addMemberConstraint(sqlQuery, baseCube, aggStar,
				parents, true, false, exclude);
	}

	public void addLevelConstraint(SqlQuery query, RolapCube baseCube,
			AggStar aggStar, RolapLevel level) {
	}

	public String toString() {
		return "DefaultMemberChildrenConstraint";
	}

	public Object getCacheKey() {
		// we have no state, so all instances are equal
		return this;
	}

	public static MemberChildrenConstraint instance() {
		return instance;
	}
}

// End DefaultMemberChildrenConstraint.java

