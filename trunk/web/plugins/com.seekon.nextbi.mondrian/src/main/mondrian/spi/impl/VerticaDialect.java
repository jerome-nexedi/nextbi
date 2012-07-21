/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2009-2011 Julian Hyde
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.spi.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of {@link mondrian.spi.Dialect} for the Vertica database.
 * 
 * @author Pedro Alves
 * @version $Id:
 *          //open/mondrian/src/main/mondrian/spi/impl/VerticaDialect.java#5 $
 * @since Sept 11, 2009
 */
public class VerticaDialect extends JdbcDialectImpl {

  public static final JdbcDialectFactory FACTORY = new JdbcDialectFactory(
    VerticaDialect.class, DatabaseProduct.VERTICA);

  /**
   * Creates a VerticaDialect.
   * 
   * @param connection
   *          Connection
   */
  public VerticaDialect(Connection connection) throws SQLException {
    super(connection);
  }

  public boolean requiresAliasForFromQuery() {
    return true;
  }

  @Override
  public DatabaseProduct getDatabaseProduct() {
    return DatabaseProduct.VERTICA;
  }

  @Override
  public boolean supportsResultSetConcurrency(int type, int concurrency) {
    return false;
  }

  public String generateInline(List<String> columnNames, List<String> columnTypes,
    List<String[]> valueList) {
    return generateInlineGeneric(columnNames, columnTypes, valueList, null, false);
  }
}

// End VerticaDialect.java
