/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2008-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.spi.impl;

import mondrian.olap.Util;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

/**
 * Implementation of {@link mondrian.spi.Dialect} for the MySQL database.
 * 
 * @author jhyde
 * @version $Id: //open/mondrian/src/main/mondrian/spi/impl/MySqlDialect.java#19
 *          $
 * @since Nov 23, 2008
 */
public class MySqlDialect extends JdbcDialectImpl {

	private final String flagsRegexp = "^(\\(\\?([a-zA-Z])\\)).*$";
	private final Pattern flagsPattern = Pattern.compile(flagsRegexp);
	private final String escapeRegexp = "^.*(\\\\Q(.*)\\\\E).*$";
	private final Pattern escapePattern = Pattern.compile(escapeRegexp);

	public static final JdbcDialectFactory FACTORY = new JdbcDialectFactory(
			MySqlDialect.class, DatabaseProduct.MYSQL) {
		protected boolean acceptsConnection(Connection connection) {
			try {
				// Infobright looks a lot like MySQL. If this is an
				// Infobright connection, yield to the Infobright dialect.
				return super.acceptsConnection(connection)
						&& !isInfobright(connection.getMetaData());
			} catch (SQLException e) {
				throw Util.newError(e, "Error while instantiating dialect");
			}
		}
	};

	/**
	 * Creates a MySqlDialect.
	 * 
	 * @param connection
	 *          Connection
	 * 
	 * @throws SQLException
	 *           on error
	 */
	public MySqlDialect(Connection connection) throws SQLException {
		super(connection);
	}

	/**
	 * Detects whether this database is Infobright.
	 * 
	 * <p>
	 * Infobright uses the MySQL driver and appears to be a MySQL instance. The
	 * only difference is the presence of the BRIGHTHOUSE engine.
	 * 
	 * @param databaseMetaData
	 *          Database metadata
	 * 
	 * @return Whether this is Infobright
	 */
	public static boolean isInfobright(DatabaseMetaData databaseMetaData) {
		Statement statement = null;
		try {
			String productVersion = databaseMetaData.getDatabaseProductVersion();
			if (productVersion.compareTo("5.1") >= 0) {
				statement = databaseMetaData.getConnection().createStatement();
				final ResultSet resultSet = statement
						.executeQuery("select * from INFORMATION_SCHEMA.engines "
								+ "where ENGINE = 'BRIGHTHOUSE'");
				if (resultSet.next()) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			throw Util.newInternal(e,
					"while running query to detect Brighthouse engine");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
	}

	@Override
	protected String deduceProductName(DatabaseMetaData databaseMetaData) {
		final String productName = super.deduceProductName(databaseMetaData);
		if (isInfobright(databaseMetaData)) {
			return "MySQL (Infobright)";
		}
		return productName;
	}

	protected String deduceIdentifierQuoteString(DatabaseMetaData databaseMetaData) {
		String quoteIdentifierString = super
				.deduceIdentifierQuoteString(databaseMetaData);

		if (quoteIdentifierString == null) {
			// mm.mysql.2.0.4 driver lies. We know better.
			quoteIdentifierString = "`";
		}
		return quoteIdentifierString;
	}

	protected boolean deduceSupportsSelectNotInGroupBy(Connection connection)
			throws SQLException {
		boolean supported = false;
		String sqlmode = getCurrentSqlMode(connection);
		if (sqlmode == null) {
			supported = true;
		} else {
			if (!sqlmode.contains("ONLY_FULL_GROUP_BY")) {
				supported = true;
			}
		}
		return supported;
	}

	private String getCurrentSqlMode(Connection connection) throws SQLException {
		return getSqlMode(connection, Scope.SESSION);
	}

	private String getSqlMode(Connection connection, Scope scope)
			throws SQLException {
		String sqlmode = null;
		Statement s = null;
		ResultSet rs = null;
		try {
			s = connection.createStatement();
			if (s.execute("SELECT @@" + scope + ".sql_mode")) {
				rs = s.getResultSet();
				if (rs.next()) {
					sqlmode = rs.getString(1);
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// ignore
				}
			}
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					// ignore
				}
			}
		}
		return sqlmode;
	}

	public void appendHintsAfterFromClause(StringBuilder buf,
			Map<String, String> hints) {
		if (hints != null) {
			String forcedIndex = hints.get("force_index");
			if (forcedIndex != null) {
				buf.append(" FORCE INDEX (");
				buf.append(forcedIndex);
				buf.append(")");
			}
		}
	}

	public boolean requiresAliasForFromQuery() {
		return true;
	}

	public boolean allowsFromQuery() {
		// MySQL before 4.0 does not allow FROM
		// subqueries in the FROM clause.
		return productVersion.compareTo("4.") >= 0;
	}

	public boolean allowsCompoundCountDistinct() {
		return true;
	}

	@Override
	public void quoteStringLiteral(StringBuilder buf, String s) {
		// Go beyond Util.singleQuoteString; also quote backslash.
		buf.append('\'');
		String s0 = Util.replace(s, "'", "''");
		String s1 = Util.replace(s0, "\\", "\\\\");
		buf.append(s1);
		buf.append('\'');
	}

	public String generateInline(List<String> columnNames,
			List<String> columnTypes, List<String[]> valueList) {
		return generateInlineGeneric(columnNames, columnTypes, valueList, null,
				false);
	}

	@Override
	protected String generateOrderByNulls(String expr, boolean ascending,
			boolean collateNullsLast) {
		// In MYSQL, Null values are worth negative infinity.
		if (collateNullsLast) {
			if (ascending) {
				return "ISNULL(" + expr + ") ASC, " + expr + " ASC";
			} else {
				return expr + " DESC";
			}
		} else {
			if (ascending) {
				return expr + " ASC";
			} else {
				return "ISNULL(" + expr + ") DESC, " + expr + " DESC";
			}
		}
	}

	public boolean requiresOrderByAlias() {
		return true;
	}

	public boolean requiresHavingAlias() {
		return true;
	}

	public boolean supportsMultiValueInExpr() {
		return true;
	}

	private enum Scope {
		SESSION, GLOBAL
	}

	public boolean allowsRegularExpressionInWhereClause() {
		return true;
	}

	public String generateRegularExpression(String source, String javaRegex) {
		try {
			Pattern.compile(javaRegex);
		} catch (PatternSyntaxException e) {
			// Not a valid Java regex. Too risky to continue.
			return null;
		}
		final Matcher flagsMatcher = flagsPattern.matcher(javaRegex);
		if (flagsMatcher.matches()) {
			javaRegex = javaRegex.substring(0, flagsMatcher.start(1))
					+ javaRegex.substring(flagsMatcher.end(1));
		}
		final Matcher escapeMatcher = escapePattern.matcher(javaRegex);
		if (escapeMatcher.matches()) {
			String sequence = escapeMatcher.group(2);
			sequence = sequence.replaceAll("\\\\", "\\\\");
			javaRegex = javaRegex.substring(0, escapeMatcher.start(1))
					+ sequence.toUpperCase() + javaRegex.substring(escapeMatcher.end(1));
		}
		final StringBuilder sb = new StringBuilder();
		sb.append("UPPER(");
		sb.append(source);
		sb.append(") REGEXP ");
		quoteStringLiteral(sb, javaRegex);
		return sb.toString();
	}
}

// End MySqlDialect.java
