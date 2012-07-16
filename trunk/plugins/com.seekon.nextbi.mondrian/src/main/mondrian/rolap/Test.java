/*
// $Id: //open/mondrian/src/main/mondrian/rolap/Test.java#23 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2001-2002 Kana Software, Inc.
// Copyright (C) 2001-2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
//
// jhyde, 21 December, 2001
 */

package mondrian.rolap;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import mondrian.olap.DriverManager;
import mondrian.olap.Id;
import mondrian.olap.Level;

/**
 * todo:
 * 
 * @author jhyde
 * @since 21 December, 2001
 * @version $Id: //open/mondrian/src/main/mondrian/rolap/Test.java#23 $
 */
public class Test {
	PrintWriter pw;
	RolapConnection connection;

	static public void main(String[] args) {
		Test test = new Test(args);
		if (true) {
			test.run();
		} else {
			try {
				test.convertFoodMart();
			} catch (java.sql.SQLException e) {
				System.out.println("Error: " + mondrian.olap.Util.getErrorMessage(e));
			}
		}
	}

	Test(String[] args) {
		pw = new PrintWriter(System.out, true);
		String connectString = "Data Source=LOCALHOST;Provider=msolap;Catalog=Foodmart";
		connection = (RolapConnection) DriverManager.getConnection(connectString,
				null);
	}

	void convertFoodMart() throws java.sql.SQLException {
		java.sql.Connection connection = null;
		java.sql.Statement statement = null, statement2 = null;
		try {
			try {
				Class.forName("com.ms.jdbc.odbc.JdbcOdbcDriver");
			} catch (ClassNotFoundException e) {
			}
			String connectString = "jdbc:odbc:DSN=FoodMart2";
			connection = java.sql.DriverManager.getConnection(connectString);
			statement = connection.createStatement();
			statement2 = connection.createStatement();
			String sql = "select * from ("
					+ " select *, \"fname\" + ' ' + \"lname\" as \"name\" from \"customer\")"
					+ "order by \"country\", \"state_province\", \"city\", \"name\"";
			java.sql.ResultSet resultSet = statement.executeQuery(sql);
			int i = 0;
			while (resultSet.next()) {
				int customer_id = resultSet.getInt("customer_id");
				statement2.executeUpdate("update \"customer\" set \"ordinal\" = "
						+ (++i * 3) + " where \"customer_id\" = " + customer_id);
			}
			connection.commit();
		} finally {
			if (statement2 != null) {
				try {
					statement2.close();
				} catch (java.sql.SQLException e) {
				}
			}
			if (statement != null) {
				try {
					statement.close();
				} catch (java.sql.SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (java.sql.SQLException e) {
				}
			}
		}
	}

	void run() {
		RolapCube salesCube = (RolapCube) connection.getSchema().lookupCube(
				"Sales", true);
		RolapHierarchy measuresHierarchy = (RolapHierarchy) salesCube
				.getMeasuresHierarchy();
		testMemberReader(measuresHierarchy.getMemberReader());

		RolapHierarchy genderHierarchy = (RolapHierarchy) salesCube
				.lookupHierarchy(new Id.Segment("Gender", Id.Quoting.QUOTED), false);
		testMemberReader(genderHierarchy.getMemberReader());

		RolapHierarchy customerHierarchy = (RolapHierarchy) salesCube
				.lookupHierarchy(new Id.Segment("Customers", Id.Quoting.QUOTED), false);
		testMemberReader(customerHierarchy.getMemberReader());
	}

	void testMemberReader(MemberReader reader) {
		pw.println();
		pw.println("MemberReader class=" + reader.getClass());
		pw.println("Count=" + reader.getMemberCount());

		pw.print("Root member(s)=");
		List<RolapMember> rootMembers = reader.getRootMembers();
		print(rootMembers);
		pw.println();

		Level[] levels = rootMembers.get(0).getHierarchy().getLevels();
		Level level = levels[levels.length > 1 ? 1 : 0];
		pw.print("Members at level " + level.getUniqueName() + " are ");
		List<RolapMember> members = reader.getMembersInLevel((RolapLevel) level, 0,
				Integer.MAX_VALUE);
		print(members);
		pw.println();

		pw.println("First children of first children: {");
		List<RolapMember> firstChildren = new ArrayList<RolapMember>();
		RolapMember member = rootMembers.get(0);
		while (member != null) {
			firstChildren.add(member);
			pw.print("\t");
			print(member);
			List<RolapMember> children = new ArrayList<RolapMember>();
			reader.getMemberChildren(member, children);
			if (children.isEmpty()) {
				break;
			}
			pw.print(" (" + children.size() + " children)");
			RolapMember leadMember = reader.getLeadMember(member, 5);
			pw.print(", lead(5)=");
			print(leadMember);
			if (children.size() > 1) {
				member = children.get(1);
			} else if (children.size() > 0) {
				member = children.get(0);
			} else {
				member = null;
			}
			pw.println();
		}
		pw.println("}");
	}

	private void print(RolapMember member) {
		if (member == null) {
			pw.print("Member(null)");
			return;
		}
		pw.print("Member(" + member.getUniqueName() + ")");
	}

	private void print(List<RolapMember> members) {
		pw.print("{");
		for (int i = 0; i < members.size(); i++) {
			if (i > 0) {
				pw.print(", ");
			}
			print(members.get(i));
		}
		pw.print("}");
	}
}

// End Test.java
