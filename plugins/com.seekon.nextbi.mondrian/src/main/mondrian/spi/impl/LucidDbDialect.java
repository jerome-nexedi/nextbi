/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2008-2010 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.spi.impl;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Implementation of {@link mondrian.spi.Dialect} for the LucidDB database.
 * 
 * @author jhyde
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/spi/impl/LucidDbDialect.java#5 $
 * @since Nov 23, 2008
 */
public class LucidDbDialect extends JdbcDialectImpl {

  public static final JdbcDialectFactory FACTORY = new JdbcDialectFactory(
    LucidDbDialect.class, DatabaseProduct.LUCIDDB);

  /**
   * Creates a LucidDbDialect.
   * 
   * @param connection
   *          Connection
   * 
   * @throws java.sql.SQLException
   *           on error
   */
  public LucidDbDialect(Connection connection) throws SQLException {
    super(connection);
  }

  public boolean allowsMultipleDistinctSqlMeasures() {
    return false;
  }

  public boolean needsExponent(Object value, String valueString) {
    return value instanceof Double && !valueString.contains("E");
  }

  public boolean supportsUnlimitedValueList() {
    return true;
  }

  public boolean supportsMultiValueInExpr() {
    return true;
  }
}

// End LucidDbDialect.java
