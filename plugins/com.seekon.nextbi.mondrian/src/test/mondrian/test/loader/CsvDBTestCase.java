/*
// $Id: //open/mondrian/testsrc/main/mondrian/test/loader/CsvDBTestCase.java#12 $
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// Copyright (C) 2005-2011 Julian Hyde and others
// All Rights Reserved.
// You must accept the terms of that agreement to use this software.
 */
package mondrian.test.loader;

import mondrian.olap.Schema;
import mondrian.rolap.RolapConnection;
import mondrian.test.FoodMartTestCase;
import mondrian.test.TestContext;
import mondrian.spi.Dialect;

import java.sql.SQLException;
import java.sql.Connection;
import java.io.File;

/**
 * Base class for tests that use a CSV database defined in a single file. While
 * the CsvDBLoader supports being defined by a single file, list of files, or
 * directory with optional regular expression for matching files in the
 * directory to be loaded, this is simplest at this point.
 * 
 * <p>
 * To use this file one must define both the directory and file abstract
 * methods.
 * 
 * @author Richard M. Emberson
 * @version $Id:
 *          //open/mondrian/testsrc/main/mondrian/test/loader/CsvDBTestCase.
 *          java#12 $
 */
public abstract class CsvDBTestCase extends FoodMartTestCase {

  private CsvDBLoader loader;

  private CsvDBLoader.Table[] tables;

  private TestContext testContext;

  public CsvDBTestCase() {
    super();
  }

  public CsvDBTestCase(String name) {
    super(name);
  }

  protected final boolean isApplicable() {
    final Dialect dialect = getTestContext().getDialect();
    return dialect.allowsDdl()
      && dialect.getDatabaseProduct() != Dialect.DatabaseProduct.INFOBRIGHT;
  }

  protected void setUp() throws Exception {
    // If this database does not allow DDL, the test won't run. Don't bother
    // setting up.
    if (!isApplicable()) {
      return;
    }

    super.setUp();

    Connection connection = getSqlConnection();
    String dirName = getDirectoryName();

    String fileName = getFileName();
    File inputFile = new File(dirName, fileName);

    this.loader = new CsvDBLoader();
    this.loader.setConnection(connection);
    this.loader.initialize();
    this.loader.setInputFile(inputFile);
    this.tables = this.loader.getTables();
    this.loader.generateStatements(this.tables);

    // create database tables
    this.loader.executeStatements(this.tables);

    String parameterDefs = getParameterDescription();
    String cubeDefs = getCubeDescription();
    String virtualCubeDefs = getVirtualCubeDescription();
    String namedSetDefs = getNamedSetDescription();
    String udfDefs = getUdfDescription();
    String roleDefs = getRoleDescription();
    this.testContext = TestContext.instance().create(parameterDefs, cubeDefs,
      virtualCubeDefs, namedSetDefs, udfDefs, roleDefs);
  }

  protected void tearDown() throws Exception {
    // If this database does not allow DDL, we didn't run setUp; so, nothing
    // to tear down.
    if (!isApplicable()) {
      return;
    }

    try {
      // drop database tables
      this.loader.dropTables(this.tables);
    } catch (Exception ex) {
      // ignore
    }

    super.tearDown();
  }

  protected Connection getSqlConnection() throws SQLException {
    return getConnection().getDataSource().getConnection();
  }

  protected Schema getSchema() {
    return getConnection().getSchema();
  }

  protected TestContext getCubeTestContext() {
    return testContext;
  }

  protected abstract String getDirectoryName();

  protected abstract String getFileName();

  protected String getParameterDescription() {
    return null;
  }

  protected abstract String getCubeDescription();

  protected String getVirtualCubeDescription() {
    return null;
  }

  protected String getNamedSetDescription() {
    return null;
  }

  protected String getUdfDescription() {
    return null;
  }

  protected String getRoleDescription() {
    return null;
  }
}

// End CsvDBTestCase.java
